package com.example.realapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;

public class ContactInfo extends AppCompatActivity {

    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    TextView tvEditA,tvEditName;
    ImageView ivCall,ivEmail,ivEdit,ivDelett;
    EditText etEditName,etEditEmail,etEditNumber;
    Button btnEditSubmit;
    boolean edit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        tvEditA=findViewById(R.id.tvEditA);
        tvEditName=findViewById(R.id.tvEditName);
        ivCall=findViewById(R.id.ivCall);
        ivEmail=findViewById(R.id.ivMail);
        ivEdit=findViewById(R.id.ivEdit);
        ivDelett=findViewById(R.id.ivDelet);
        etEditEmail=findViewById(R.id.etEditEmail);
        etEditName=findViewById(R.id.etEditName);
        etEditNumber=findViewById(R.id.etEditNumber);
        btnEditSubmit=findViewById(R.id.btnEditSubmit);
        final int index=getIntent().getIntExtra("index",0);
        etEditNumber.setText(RealApplication.userlistt.get(index).getNumber());
        etEditName.setText(RealApplication.userlistt.get(index).getName());
        etEditEmail.setText(RealApplication.userlistt.get(index).getEmail());
        tvEditA.setText(RealApplication.userlistt.get(index).getName().toUpperCase().charAt(0)+"");
        tvEditName.setText(RealApplication.userlistt.get(index).getName());
        etEditName.setVisibility(View.GONE);
        etEditNumber.setVisibility(View.GONE);
        etEditEmail.setVisibility(View.GONE);
        btnEditSubmit.setVisibility(View.GONE);
        ivCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri="tel:" + RealApplication.userlistt.get(index).getNumber();
                Intent intent=new Intent(Intent.ACTION_DIAL, Uri.parse(uri));
                startActivity(intent);
            }
        });
        ivEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL,new String[] {RealApplication.userlistt.get(index).getEmail()});
                startActivity(Intent.createChooser(intent,"Send Email to " + RealApplication.userlistt.get(index).getName()));
            }
        });
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit=!edit;
                if(edit) {
                    etEditName.setVisibility(View.VISIBLE);
                    etEditNumber.setVisibility(View.VISIBLE);
                    etEditEmail.setVisibility(View.VISIBLE);
                    btnEditSubmit.setVisibility(View.VISIBLE);
                    btnEditSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(etEditEmail.getText().toString().isEmpty() || etEditName.getText().toString().isEmpty() || etEditNumber.getText().toString().isEmpty()) {
                                Toast.makeText(ContactInfo.this, "Please Fill All The Entries.!!", Toast.LENGTH_SHORT).show();
                            } else {
                                RealApplication.userlistt.get(index).setEmail(etEditEmail.getText().toString().trim());
                                RealApplication.userlistt.get(index).setName(etEditName.getText().toString().trim());
                                RealApplication.userlistt.get(index).setNumber(etEditNumber.getText().toString().trim());
                                showProgress(true);
                                tvLoad.setText("Updating Contact,Please Wait.!!");
                                Backendless.Persistence.save(RealApplication.userlistt.get(index), new AsyncCallback<Contact>() {
                                    @Override
                                    public void handleResponse(Contact response) {
                                        tvEditA.setText(RealApplication.userlistt.get(index).getName().toUpperCase().charAt(0)+"");
                                        tvEditName.setText(RealApplication.userlistt.get(index).getName());
                                        Toast.makeText(ContactInfo.this, "Contact Updated Successfully.!!", Toast.LENGTH_SHORT).show();
                                        showProgress(false);
                                    }

                                    @Override
                                    public void handleFault(BackendlessFault fault) {
                                        Toast.makeText(ContactInfo.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                        showProgress(false);
                                    }
                                });
                            }
                        }
                    });
                } else {
                    etEditName.setVisibility(View.GONE);
                    etEditNumber.setVisibility(View.GONE);
                    etEditEmail.setVisibility(View.GONE);
                    btnEditSubmit.setVisibility(View.GONE);
                }

            }
        });
        ivDelett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder alert=new AlertDialog.Builder(ContactInfo.this);
                alert.setMessage("Are You Sure You Want to Delet this Contact?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showProgress(true);
                        tvLoad.setText("Deleting Contact,Please Wait.!!");
                        Backendless.Persistence.of(Contact.class).remove(RealApplication.userlistt.get(index), new AsyncCallback<Long>() {
                            @Override
                            public void handleResponse(Long response) {
                                RealApplication.userlistt.remove(index);
                                Toast.makeText(ContactInfo.this, "Contact Has been Deleted Successfully.!!", Toast.LENGTH_SHORT).show();
                                showProgress(false);
                                setResult(RESULT_OK);
                                ContactInfo.this.finish();
                            }

                            @Override
                            public void handleFault(BackendlessFault fault) {
                                Toast.makeText(ContactInfo.this, "Error: " + fault.getMessage(), Toast.LENGTH_SHORT).show();
                                showProgress(false);
                            }
                        });
                    }
                });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alert.show();
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