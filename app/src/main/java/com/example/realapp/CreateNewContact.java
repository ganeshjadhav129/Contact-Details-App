package com.example.realapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class CreateNewContact extends AppCompatActivity {
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    EditText etNewName,etNewEmail,etNewMobileNumber;
    Button btnCreateContact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_contact);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        etNewName=findViewById(R.id.etNewName);
        etNewMobileNumber=findViewById(R.id.etNewNumber);
        etNewEmail=findViewById(R.id.etNewEmail);
        btnCreateContact=findViewById(R.id.btnCteateContact);
        btnCreateContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etNewName.getText().toString().isEmpty() || etNewMobileNumber.getText().toString().isEmpty() || etNewEmail.getText().toString().isEmpty()) {
                    Toast.makeText(CreateNewContact.this, "Please Fill All the Entries.!!", Toast.LENGTH_SHORT).show();
                } else {
                    String name=etNewName.getText().toString(),email=etNewEmail.getText().toString(),number=etNewMobileNumber.getText().toString();
                    Contact contact=new Contact();
                    contact.setName(name);
                    contact.setNumber(number);
                    contact.setEmail(email);
                    contact.setUserEmail(RealApplication.user.getEmail());
                    showProgress(true);
                    tvLoad.setText("Creating New Contact,Please Wait.!!");
                    Backendless.Persistence.save(contact, new AsyncCallback<Contact>() {
                        @Override
                        public void handleResponse(Contact response) {
                            Toast.makeText(CreateNewContact.this, "New Contact Created Successfully.!!", Toast.LENGTH_SHORT).show();
                            etNewEmail.setText("");
                            etNewMobileNumber.setText("");
                            etNewName.setText("");
                            showProgress(false);
                        }

                        @Override
                        public void handleFault(BackendlessFault fault) {
                            Toast.makeText(CreateNewContact.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                            showProgress(false);
                        }
                    });
                }
            }
        });
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

}