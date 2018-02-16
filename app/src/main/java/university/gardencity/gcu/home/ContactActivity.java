package university.gardencity.gcu.home;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import university.gardencity.gcu.R;
import university.gardencity.gcu.constants.AppConstants;
import university.gardencity.gcu.managers.NetworkRequestManager;
import university.gardencity.gcu.managers.ServiceGenerator;
import university.gardencity.gcu.model.GenericResponse;
import university.gardencity.gcu.services.ApplicationLogger;

public class ContactActivity extends AppCompatActivity {
    private Toolbar toolbar;
    ApplicationLogger mLogger;
    private TextInputLayout mName, mEmail, mPhone, mCity;
    String name, email, phone, city;
    private ProgressDialog pDialog;
    private TextView mSuccessLabel;
    private ScrollView mFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        mLogger = new ApplicationLogger();
        toolbar = (Toolbar) findViewById(R.id.activity_contact_toolbar);
        TextView mToolbarTitle = (TextView) toolbar.findViewById(R.id.child_toolbar_title);
        mToolbarTitle.setText(AppConstants.TOOLBAR_TITLE_CONTACT_ACTIVITY);
        initToolbar();
        mFormView = (ScrollView) findViewById(R.id.activity_contact_scrollView);
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Sending enquiry form");
        pDialog.setMessage("Please wait while we save your enquiry.");
        mSuccessLabel = (TextView) findViewById(R.id.activity_contact_success);
        mName = (TextInputLayout) findViewById(R.id.contact_name);
        mEmail = (TextInputLayout) findViewById(R.id.contact_email);
        mPhone = (TextInputLayout) findViewById(R.id.contact_phone);
        mCity = (TextInputLayout) findViewById(R.id.contact_city);
        Button mSubmit = (Button) findViewById(R.id.activity_contact_btn_submit);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCity.clearFocus();
                mPhone.clearFocus();
                mEmail.clearFocus();
                mName.clearFocus();
                mFormView.setVisibility(View.GONE);
                pDialog.show();
                processForm();
            }
        });
    }


    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        try {
            ab.setDisplayShowTitleEnabled(false);
        } catch (NullPointerException ex) {
            mLogger.logDebug("SendNotification", ex.getMessage());
        }
    }

    private void processForm() {
        try {
            name = mName.getEditText().getEditableText().toString();
            email = mEmail.getEditText().getEditableText().toString();
            phone = mPhone.getEditText().getEditableText().toString();
            city = mCity.getEditText().getEditableText().toString();
        } catch (NullPointerException exp) {
            Toast.makeText(this, "Values can't be empty.", Toast.LENGTH_LONG).show();
        }

        if (verifyForm(name, email, city, phone)) {
            //Send form
            NetworkRequestManager nMan = ServiceGenerator.getRestWebService();
            Call<GenericResponse> call = nMan.saveEnquiry(name, email, phone, city);
            call.enqueue(new Callback<GenericResponse>() {
                @Override
                public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                    if (response.body().getStatus().equals("1")) {
                        successFunction();
                    }
                }

                @Override
                public void onFailure(Call<GenericResponse> call, Throwable t) {
                    Snackbar.make(mName, "Couldn't Insert. Please try again.", Snackbar.LENGTH_LONG)
                            .setActionTextColor(Color.RED).show();
                    pDialog.dismiss();
                    mFormView.setVisibility(View.VISIBLE);
                }
            });
        } else {
            Snackbar.make(mName, "Field cannot be empty.", Snackbar.LENGTH_LONG).show();
        }
    }

    private boolean verifyForm(String name, String email, String city, String phone) {
        boolean result = true;
        if (name.isEmpty() || email.isEmpty() || city.isEmpty() || phone.isEmpty()) {
            result = false;
        }
        return result;
    }

    private void successFunction() {
        pDialog.setTitle("Successful.");
        pDialog.setMessage("We'll get in touch with you shortly. Thank You for " +
                "contacting Garden City University.");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pDialog.dismiss();
            }
        }, 2500);
        mSuccessLabel.setVisibility(View.VISIBLE);
    }
}
