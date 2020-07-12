package com.developer.hajira.realtimetraffic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.developer.hajira.realtimetraffic.utils.networkHelper;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class ResultActivity extends AppCompatActivity {
    private SimpleArcDialog simpleArcDialog;
    private Toolbar toolbar;
    Button cancel ;
    TextView route,city,type,reason,length,time,cars,buses,trucks,motorbikes,totat_vehicles,accident;
    private boolean isNetworkOk;
    String myCity,myRoute;
    String message;
    ProgressDialog pd;


    public static final String SERVER_URL = "http://192.168.43.233:5000/test";

    public  static final String TAG = "MyTag";

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(SERVER_URL );
        } catch (URISyntaxException e) {}
    }

    int check = 0;
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {

            ResultActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String dataFromServer;


                    try {

                        dataFromServer = data.getString("data");



                    } catch (JSONException e) {
                        return;
                    }

                    // add the message to view
                    displayData(  dataFromServer);

                }
            });
        }
    };




    private void displayData( String data) {

        if (data.equals("Sorry no camera available on that route" )){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ResultActivity.this);
            alertDialogBuilder.setMessage(data);
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            mSocket.disconnect();
                            Intent intent1 = new Intent(ResultActivity.this,Dashboard.class);


                            startActivity(intent1);
                            finish();
                        }
                    });
            simpleArcDialog.dismiss();

            alertDialogBuilder.show();



        }
        else
        {
            if (check == 0){
                check += 1;
                // Objects.requireNonNull(getActionBar()).setTitle("Data will update every 15 secs");
                simpleArcDialog.dismiss();
            }


            String[] splitedOutput = data.split(",");

            city.setText(myCity);

            route.setText(myRoute);
            type.setText(splitedOutput[0]);
            reason.setText(splitedOutput[1]);
            totat_vehicles.setText(splitedOutput[2]);
            accident.setText(splitedOutput[4]);
            cars.setText(splitedOutput[5]);
            trucks.setText(splitedOutput[6]);
            buses.setText(splitedOutput[7]);
            motorbikes.setText(splitedOutput[8]);
            length.setText(splitedOutput[9]);
            time.setText(splitedOutput[10]);



        }








    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        myRoute = intent.getStringExtra("myRoute");
        myCity = intent.getStringExtra("myCity");



        pd = new ProgressDialog(ResultActivity.this);
        pd.setMessage("It will take a minute!");
        pd.setTitle("Be Patient");
        pd.setIcon(R.mipmap.ic_launcher);



        pd.setCancelable(false);

        pd.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pd.dismiss();
                mSocket.close();
                mSocket.disconnect();
                Intent intent1 = new Intent(ResultActivity.this,Dashboard.class);


                startActivity(intent1);
                finish();
            }
        });



        route = (TextView) findViewById(R.id.route);
        city = (TextView) findViewById(R.id.city);
        type = (TextView) findViewById(R.id.type);
        reason = (TextView) findViewById(R.id.reason);
        accident = (TextView) findViewById(R.id.accident);
        length = (TextView) findViewById(R.id.length);
        time = (TextView) findViewById(R.id.timeToEnd);
        cars = (TextView) findViewById(R.id.cars);
        trucks = (TextView) findViewById(R.id.trucks);
        buses = (TextView) findViewById(R.id.buses);
        motorbikes = (TextView) findViewById(R.id.motorbikes);
        totat_vehicles = (TextView) findViewById(R.id.totalVehicles);

        simpleArcDialog = new SimpleArcDialog(this);
        setDialogConfiguration();
        cancel = (Button)findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSocket.close();
                mSocket.disconnect();


                Intent i = new Intent(ResultActivity.this,Dashboard.class);
                startActivity(i);
                finish();
            }
        });



        isNetworkOk =  networkHelper.isNetworkAvailable(ResultActivity.this);
        if (isNetworkOk) {



            simpleArcDialog.show();
            mSocket.connect();




            mSocket.emit("congestion report", myCity+"&"+myRoute);

            mSocket.on("congestion report", onNewMessage);






        }else{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ResultActivity.this);
            alertDialogBuilder.setMessage("Please check your internet connection and try again");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            Intent i = new Intent(ResultActivity.this,Dashboard.class);
                            startActivity(i);
                            finish();

                        }
                    });
            simpleArcDialog.dismiss();
            alertDialogBuilder.show();
        }



    }
    public void setDialogConfiguration(){
        ArcConfiguration configuration = new ArcConfiguration(this);
        configuration.setText("\t\t\tPlease wait\nIt will take a minute");



        configuration.setColors(new int[]{Color.parseColor("#1d6dbe")});
        simpleArcDialog.setConfiguration(configuration);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);

    }
}
