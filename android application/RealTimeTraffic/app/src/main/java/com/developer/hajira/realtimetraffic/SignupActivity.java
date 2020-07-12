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
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;

public class SignupActivity extends AppCompatActivity {
    private SimpleArcDialog simpleArcDialog;

    Button signupBtn;
    EditText Email,Password;
    TextView goToLogin;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);
        Email = (EditText)findViewById(R.id.username);
        Password = (EditText)findViewById(R.id.password);
        mFirebaseAuth = FirebaseAuth.getInstance();
        goToLogin = (TextView)findViewById(R.id.goToLogin);


        simpleArcDialog = new SimpleArcDialog(this);
        setDialogConfiguration();

    }
    public void eventJumpToLogin(View view) {
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }

    public void buttonSignup(View view) {

        String email = Email.getText().toString().trim();
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
            Toast.makeText(SignupActivity.this,"Fields are empty",Toast.LENGTH_LONG).show();
        }
        else if ( !(email.isEmpty() &&  pass.isEmpty()))
        {


            simpleArcDialog.show();
            mFirebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(SignupActivity.this,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        String message = task.getException().getMessage();


                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SignupActivity.this);
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

                        Intent intent = new Intent(SignupActivity.this,Dashboard.class);
                        startActivity(intent);
                        finish();
                    }
                }

            });

        }

        else {
            Toast.makeText(SignupActivity.this,"Error Occurred!",Toast.LENGTH_LONG).show();

        }

    }
    public void setDialogConfiguration(){
        ArcConfiguration configuration = new ArcConfiguration(this);
        configuration.setText("Please wait...");
        configuration.setColors(new int[]{Color.parseColor("#1d6dbe")});
        simpleArcDialog.setConfiguration(configuration);
    }

}
