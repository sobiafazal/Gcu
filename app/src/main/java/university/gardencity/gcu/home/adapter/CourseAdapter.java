package university.gardencity.gcu.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import university.gardencity.gcu.R;
import university.gardencity.gcu.home.model.CourseModel;


public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private ArrayList<CourseModel> mList;
    private OnCourseClickListener mListener;

    public CourseAdapter(ArrayList<CourseModel> mList, OnCourseClickListener mListener) {
        this.mList = mList;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_adapter, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mCourseAbbr.setText(mList.get(position).getCourseAbbr());
        holder.mCourseName.setText(mList.get(position).getCourseName());
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCourseClick(mList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout mContainer;
        TextView mCourseAbbr;
        TextView mCourseName;

        public ViewHolder(View itemView) {
            super(itemView);
            mContainer = (LinearLayout) itemView.findViewById(R.id.course_adapter_container);
            mCourseAbbr = (TextView) itemView.findViewById(R.id.course_adapter_course_abbr);
            mCourseName = (TextView) itemView.findViewById(R.id.course_adapter_course_name);
        }
    }

    public interface OnCourseClickListener {
        void onCourseClick(CourseModel cModel);
    }
}
