package university.gardencity.gcu.home;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import university.gardencity.gcu.R;
import university.gardencity.gcu.constants.AppConstants;
import university.gardencity.gcu.home.model.AboutModel;
import university.gardencity.gcu.managers.NetworkRequestManager;
import university.gardencity.gcu.managers.ServiceGenerator;
import university.gardencity.gcu.services.ApplicationLogger;

public class AboutFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView mAboutLabel;
    private TextView mHistoryLabel;
    private Button mAction;
    private ProgressBar pBar;
    private LinearLayout mMainDisplay;
    private String mParam1;
    private String mParam2;


    public AboutFragment() {
        // Required empty public constructor
    }


    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v =  inflater.inflate(R.layout.fragment_about, container, false);
        mAboutLabel = (TextView) v.findViewById(R.id.frag_about_content_about);
        mHistoryLabel = (TextView) v.findViewById(R.id.frag_about_content_history);
        mAction = (Button) v.findViewById(R.id.frag_about_btn_more);
        pBar = (ProgressBar) v.findViewById(R.id.frag_about_loading);
        mMainDisplay = (LinearLayout) v.findViewById(R.id.frag_about_main_display);
        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        initView();
    }
    private void initView(){
        NetworkRequestManager nrMan = ServiceGenerator.getRestWebService();
        Call<AboutModel> call = nrMan.getAbout(AppConstants.ABOUT_ID);

        call.enqueue(new Callback<AboutModel>() {
            @Override
            public void onResponse(Call<AboutModel> call, Response<AboutModel> response) {
                if(response.body() != null)
                    handleResponse(response.body());
            }

            @Override
            public void onFailure(Call<AboutModel> call, Throwable t) {

            }
        });
    }

    private void handleResponse(final AboutModel body) {
        mAboutLabel.setText(body.getAbout().trim());
        mHistoryLabel.setText(body.getHistory().trim());
        mAction.setText(body.getLink());
        mAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBrowser(body.getLinkAction());
            }
        });
        pBar.setVisibility(View.GONE);
        mMainDisplay.setVisibility(View.VISIBLE);
    }

    private void startBrowser(String linkAction) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkAction));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try{
            startActivity(intent);
        }
        catch (ActivityNotFoundException ex){
            new ApplicationLogger().logDebug("About", ex.getMessage());
        }
    }
}
