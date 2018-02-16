package university.gardencity.gcu.home;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import university.gardencity.gcu.R;
import university.gardencity.gcu.constants.AppConstants;
import university.gardencity.gcu.constants.UrlConstants;
import university.gardencity.gcu.home.adapter.EventAdapter;
import university.gardencity.gcu.home.model.EventsModel;
import university.gardencity.gcu.managers.NetworkRequestManager;
import university.gardencity.gcu.managers.ServiceGenerator;
import university.gardencity.gcu.services.ApplicationLogger;

public class EventsFragment extends Fragment implements EventAdapter.OnEventSelectedListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<EventsModel> eventList;
    private RecyclerView mRecycler;
    private String mParam1;
    private String mParam2;
    private EventAdapter mAdapter;
    private ProgressBar pBar;

    public EventsFragment() {
        // Required empty public constructor

    }

    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        eventList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_events, container, false);
        mRecycler = (RecyclerView) v.findViewById(R.id.frag_event_recycler);
        pBar = (ProgressBar) v.findViewById(R.id.frag_event_loading);
        mRecycler.setNestedScrollingEnabled(true);

        mAdapter = new EventAdapter(eventList, getContext(), null);
        mRecycler.setAdapter(mAdapter);
        RecyclerView.LayoutManager man = new LinearLayoutManager(getContext());
        man.setAutoMeasureEnabled(true);
        mRecycler.setLayoutManager(man);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        getEvents();
    }

    private void getEvents() {
        NetworkRequestManager requestMan = ServiceGenerator.getRestWebService();
        Call<List<EventsModel>> call = requestMan.getNews(AppConstants.NEWS_ID);

        call.enqueue(new Callback<List<EventsModel>>() {
            @Override
            public void onResponse(Call<List<EventsModel>> call, Response<List<EventsModel>> response) {
                eventList = response.body();
                initRecycler();
            }

            @Override
            public void onFailure(Call<List<EventsModel>> call, Throwable t) {
                new ApplicationLogger().logDebug("Event fragment error: ", t.getMessage());
            }
        });


    }

    private void initRecycler() {
        mAdapter = new EventAdapter(eventList, getContext(), this);
        new ApplicationLogger().logDebug("Event List", eventList.toString());
        mRecycler.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        pBar.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);
    }

    @Override
    public void onEventSelected(EventsModel eventModel) {
        new ApplicationLogger().logDebug("Event Selected", eventModel.toString());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(UrlConstants.GCU_NEWS));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            new ApplicationLogger().logDebug("About", ex.getMessage());
        }
    }
}
