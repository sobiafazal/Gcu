package university.gardencity.gcu.home.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import university.gardencity.gcu.R;
import university.gardencity.gcu.home.model.EventsModel;
import university.gardencity.gcu.services.ApplicationLogger;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {

    List<EventsModel> mList = new ArrayList<>();
    Context ctx;
    OnEventSelectedListener mListener;

    public EventAdapter(List<EventsModel> mList, Context ctx, OnEventSelectedListener mListener) {
        this.mList = mList;
        this.ctx = ctx;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_adapter,
                parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        String date[] = mList.get(position).getDate().trim().split(" ");
        //holder.mDate.setText(date[1] + " " + date[0]);
        holder.mTitle.setText(mList.get(position).getEventTitle());
        holder.mDescription.setText((mList.get(position).getEventDis()));
        holder.mEventContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onEventSelected(mList.get(position));
            }
        });
        //Picasso.with(ctx).load(mList.get(position).getImg()).into(holder.mImage);
    }

    @Override
    public int getItemCount() {
        new ApplicationLogger().logDebug("Event Adapter List Size", mList.size() + "");
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mDate;
        //private AppCompatImageView mImage;
        private TextView mTitle;
        private TextView mDescription;
        private RelativeLayout mEventContainer;

        ViewHolder(View itemView) {
            super(itemView);
            mDate = (TextView) itemView.findViewById(R.id.event_adapter_date);
            //mImage = (AppCompatImageView) itemView.findViewById(R.id.event_adapter_image);
            mTitle = (TextView) itemView.findViewById(R.id.event_adapter_title);
            mDescription = (TextView) itemView.findViewById(R.id.event_adapter_des);
            mEventContainer = (RelativeLayout) itemView.findViewById(R.id.event_adapter_container);
        }
    }

    public interface OnEventSelectedListener {
        void onEventSelected(EventsModel eventModel);
    }
}
