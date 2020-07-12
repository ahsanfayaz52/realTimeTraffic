package com.developer.hajira.realtimetraffic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.dev.materialspinner.MaterialSpinner;
import com.developer.hajira.realtimetraffic.utils.networkHelper;
import com.developer.hajira.realtimetraffic.utils.spinnerAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class Dashboard extends AppCompatActivity {

    private Toolbar toolbar;
    VideoView videoView;
    String[] city;
    String[]  Attock;
    String[]  Peshawar;
    String[]  Islamabad;
    String[]  Multan;
    String[]  Lahore;
    String myRoute = "Find Route";
    String myCity = "Find Location";
    MaterialSpinner spinner,spinner2;
    private boolean isNetworkOk;
   spinnerAdapter adapter1,adapter;
    ViewGroup viewGroup;
    View dialogView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        city = getResources().getStringArray(R.array.cityNames);
        Attock = getResources().getStringArray(R.array.Attock);
        Peshawar = getResources().getStringArray(R.array.Peshawar);
        Islamabad = getResources().getStringArray(R.array.Islamabad);
        Multan = getResources().getStringArray(R.array.Multan);
        Lahore = getResources().getStringArray(R.array.Lahore);
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_provide_information, viewGroup, false);


        videoView = findViewById(R.id.tvVideoView);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.sample2);
        videoView.setVideoURI(uri);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        videoView.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.custom_menu,menu);
        menu.add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.ic_logout), "Logout"));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        
        if(item.getItemId()==1){
            logout();
        }
        
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        finish();
        Intent i = new Intent(Dashboard.this,LoginActivity.class);
        startActivity(i);
    }
    private CharSequence menuIconWithText(Drawable r, String title) {

        r.setBounds(0, 0, r.getIntrinsicWidth(), r.getIntrinsicHeight());
        SpannableString sb = new SpannableString("    " + title);
        ImageSpan imageSpan = new ImageSpan(r, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }

    public void eventProvideInformation(View view) {

        spinner = dialogView.findViewById(R.id.cities);

        adapter = new  spinnerAdapter( this, android.R.layout.simple_list_item_1);
        adapter.addAll(city);


        spinner.setAdapter(adapter);

        spinner.setItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(adapterView.getItemAtPosition(i).toString().equals("Find Location"))
                {


                    ((TextView) view).setTextColor(ContextCompat.getColor(Dashboard.this, R.color.hint));
                }
                else{
                    switch (city[i]) {


                        case "Attock":
                            myCity  = adapterView.getItemAtPosition(i).toString();
                            SetRoute(Attock);
                            break;
                        case "Peshawar":
                            myCity = adapterView.getItemAtPosition(i).toString();
                            SetRoute(Peshawar);
                            break;
                        case "Islamabad":
                            myCity = adapterView.getItemAtPosition(i).toString();
                            SetRoute(Islamabad);
                            break;
                        case "Multan":
                            myCity = adapterView.getItemAtPosition(i).toString();
                            SetRoute(Multan);
                            break;
                        case "Lahore":
                            myCity = adapterView.getItemAtPosition(i).toString();
                            SetRoute(Lahore);
                            break;
                        default:
                            Toast.makeText(Dashboard.this, "No more Cities available", Toast.LENGTH_LONG).show();
                            break;
                    }
                }





                }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });






        //Button
        Button btnContinue = dialogView.findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                isNetworkOk = networkHelper.isNetworkAvailable(Dashboard.this);
                if (isNetworkOk) {

                    if (myCity.equals("Find Location")){
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Dashboard.this);
                        alertDialogBuilder.setMessage("Please Select a City");
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {


                                    }
                                });

                        alertDialogBuilder.show();
                    }
                    else    if (myRoute.equals("Find Route") ) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Dashboard.this);
                        alertDialogBuilder.setMessage("Please Select a Route");
                        alertDialogBuilder.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {


                                    }
                                });

                        alertDialogBuilder.show();
                    }
                    else{
                        Intent intent = new Intent(Dashboard.this,ResultActivity.class);
                        intent.putExtra("myRoute",myRoute);
                        intent.putExtra("myCity",myCity);
                        startActivity(intent);
                        finish();
                    }


                }
                else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Dashboard.this);
                    alertDialogBuilder.setMessage("Please check your internet connection");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {


                                }
                            });

                    alertDialogBuilder.show();
                }
            }
        });


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();




        alertDialog.show();

    }

    public void SetRoute(final String[] route)  {
        spinner2 = dialogView.findViewById(R.id.routes);


        adapter1 = new   spinnerAdapter(Dashboard.this, android.R.layout.simple_list_item_1);
        adapter1.addAll(route);

        spinner2.setAdapter(adapter1);

        spinner2.setItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i).toString().equals("Find Route"))
                {


                    ((TextView) view).setTextColor(ContextCompat.getColor(Dashboard.this, R.color.hint));
                }
                else{

                    myRoute = adapterView.getItemAtPosition(i).toString();


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                        Dashboard.this.onSuperBackPressed();
                        //super.onBackPressed();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    /*
    if (handleCancel()){
        super.onBackPressed();
    }
    */
    }

    public void onSuperBackPressed(){
        super.onBackPressed();
    }
}
