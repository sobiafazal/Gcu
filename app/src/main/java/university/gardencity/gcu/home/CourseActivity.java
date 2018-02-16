package university.gardencity.gcu.home;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import university.gardencity.gcu.R;
import university.gardencity.gcu.constants.AppConstants;
import university.gardencity.gcu.constants.UrlConstants;
import university.gardencity.gcu.home.adapter.CourseAdapter;
import university.gardencity.gcu.home.model.CourseModel;
import university.gardencity.gcu.managers.NetworkRequestManager;
import university.gardencity.gcu.managers.ServiceGenerator;
import university.gardencity.gcu.services.ApplicationLogger;

public class CourseActivity extends AppCompatActivity implements CourseAdapter.OnCourseClickListener {
    private Toolbar toolbar;
    ApplicationLogger mLogger;
    private RecyclerView mRecycler;
    private ArrayList<CourseModel> mCourseList;
    ScrollView mMainDisplay;
    ProgressBar mProgress;
    private CollapsingToolbarLayout mCTb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
        mLogger = new ApplicationLogger();
        toolbar = (Toolbar) findViewById(R.id.activity_course_toolbar);
        TextView mToolbarTitle = (TextView) toolbar.findViewById(R.id.child_toolbar_title);
        mToolbarTitle.setText(AppConstants.TOOLBAR_TITLE_COURSE_ACTIVITY);
        mRecycler = (RecyclerView) findViewById(R.id.activity_course_recycler);
        mMainDisplay = (ScrollView) findViewById(R.id.activity_course_scrollView);
        mProgress = (ProgressBar) findViewById(R.id.activity_course_progress);
        mCTb = (CollapsingToolbarLayout) findViewById(R.id.activity_course_collapsingToolbar);

        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        try {
            //ab.setDisplayHomeAsUpEnabled(true);
            ab.setHomeAsUpIndicator(R.drawable.ic_back_icon);
            ab.setDisplayShowTitleEnabled(false);

        } catch (NullPointerException ex) {
            mLogger.logDebug("SendNotification", ex.getMessage());
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCourseList();
    }

    private void getCourseList() {
        NetworkRequestManager man = ServiceGenerator.getRestWebService();
        Call<ArrayList<CourseModel>> call = man.getCourses();
        call.enqueue(new Callback<ArrayList<CourseModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CourseModel>> call, Response<ArrayList<CourseModel>> response) {
                if (response.body().size() > 0) {
                    mCourseList = response.body();
                    new ApplicationLogger().logDebug("Course list", mCourseList.toString());
                    initRecycler();
                } else {
                    Toast.makeText(CourseActivity.this, "Can't fetch course. Please try after " +
                            "some time.", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ArrayList<CourseModel>> call, Throwable t) {
                Toast.makeText(CourseActivity.this, "Network error. Please check your internet " +
                        "connection.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initRecycler() {
        CourseAdapter cAdapter = new CourseAdapter(mCourseList, this);
        mRecycler.setAdapter(cAdapter);
        RecyclerView.LayoutManager man = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(man);
        cAdapter.notifyDataSetChanged();
        mProgress.setVisibility(View.GONE);
        mMainDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCourseClick(CourseModel cModel) {

        Intent openChrome = new Intent(Intent.ACTION_VIEW, Uri.parse(UrlConstants.GCU_COURSES));
        openChrome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        openChrome.setPackage("com.android.chrome");
        try {
            startActivity(openChrome);
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(this, "Chrome not installed.", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
