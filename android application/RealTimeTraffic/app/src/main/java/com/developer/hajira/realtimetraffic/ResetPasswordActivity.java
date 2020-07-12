package com.developer.hajira.realtimetraffic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;

public class ResetPasswordActivity extends AppCompatActivity {
    Button resetPass;
    EditText email;
    FirebaseAuth mFirebaseAuth;
    private SimpleArcDialog simpleArcDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reset_password);
        simpleArcDialog = new SimpleArcDialog(this);
        setDialogConfiguration();
        email = (EditText) findViewById(R.id.email);
        resetPass = (Button) findViewById(R.id.reset);
        mFirebaseAuth = FirebaseAuth.getInstance();
        resetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    String Email = email.getText().toString().trim();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";




                    if (Email.isEmpty()) {

                        email.setError("Please Provide email address");
                        email.requestFocus();
                    }
                    else if (!Email.matches(emailPattern))
                    {
                        email.setError("Please provide valid email");
                        email.requestFocus();
                    }

                    else {
                        simpleArcDialog.show();
                        mFirebaseAuth.sendPasswordResetEmail(Email).addOnCompleteListener(ResetPasswordActivity.this, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    String message = task.getException().getMessage();
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                    alertDialogBuilder.setMessage("Error Occured: !"+message);
                                    alertDialogBuilder.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {

                                                }
                                            });
                                    simpleArcDialog.dismiss();
                                    alertDialogBuilder.show();
                                } else {

                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ResetPasswordActivity.this);
                                    alertDialogBuilder.setMessage("Please Check Your Email Address!");
                                    alertDialogBuilder.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {
                                                    Intent i = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                }
                                            });
                                    simpleArcDialog.dismiss();
                                    alertDialogBuilder.show();
                                }

                            }

                        });
                    }
                    simpleArcDialog.dismiss();

            }
        });
    }
    public void setDialogConfiguration(){
        ArcConfiguration configuration = new ArcConfiguration(this);
        configuration.setText("Please wait...");
        configuration.setColors(new int[]{Color.parseColor("#1d6dbe")});
        simpleArcDialog.setConfiguration(configuration);
    }
}
