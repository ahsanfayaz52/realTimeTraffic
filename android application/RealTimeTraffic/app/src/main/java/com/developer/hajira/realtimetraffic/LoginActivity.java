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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;

public class LoginActivity extends AppCompatActivity {
    Button loginBtn;
    EditText Email,Password;
    private boolean isNetworkOk;
    TextView goToSignup,forget;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private SimpleArcDialog simpleArcDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mFirebaseAuth = FirebaseAuth.getInstance();
        Email = (EditText)findViewById(R.id.username);
        Password = (EditText)findViewById(R.id.password);
        forget = (TextView) findViewById(R.id.forget) ;
        goToSignup = (TextView)findViewById(R.id.goToSignup);

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

                if (firebaseUser!=null){

                    Intent i = new Intent(LoginActivity.this,Dashboard.class);
                    startActivity(i);
                }

            }
        };


        simpleArcDialog = new SimpleArcDialog(this);
        setDialogConfiguration();
    }
    public void buttonLogin(View view) {

        final String email = Email.getText().toString().trim();
        String pass = Password.getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.isEmpty())
        {
            Email.setError("Please provide email address");
            Email.requestFocus();
        }
        else if (!email.matches(emailPattern))
        {
            Email.setError("Please provide valid email");
            Email.requestFocus();
        }
        else if (pass.isEmpty())
        {
            Password.setError("Please provide Password");
            Password.requestFocus();
        }
        else if (pass.length()<6){
            Password.setError("Password Length must be 6 characters");
            Password.requestFocus();
        }
        else if (email.isEmpty() && pass.isEmpty()){
            Toast.makeText(LoginActivity.this,"Fields are empty",Toast.LENGTH_LONG).show();
        }
        else if ( !(email.isEmpty() &&  pass.isEmpty()))
        {

            simpleArcDialog.show();
            mFirebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        String message = task.getException().getMessage();

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this);
                        alertDialogBuilder.setMessage("Error Occured: "+message);
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {

                                    }
                                });

                        simpleArcDialog.dismiss();
                        alertDialogBuilder.show();
                    }else{
                        simpleArcDialog.dismiss();
                        startActivity(new Intent(getApplicationContext(),Dashboard.class));
                         finish();
                    }
                }

            });



        }

        else {
            Toast.makeText(LoginActivity.this,"Error Occurred!",Toast.LENGTH_LONG).show();

        }


    }
    public void setDialogConfiguration(){
        ArcConfiguration configuration = new ArcConfiguration(this);
        configuration.setText("Please wait...");
        configuration.setColors(new int[]{Color.parseColor("#1d6dbe")});
        simpleArcDialog.setConfiguration(configuration);
    }
    public void eventJumpToSignUp(View view) {
        startActivity(new Intent(getApplicationContext(),SignupActivity.class));
        finish();
    }
    public void eventJumpToForgotPass(View view) {
        startActivity(new Intent(getApplicationContext(),ResetPasswordActivity.class));
        finish();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
