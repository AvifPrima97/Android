package com.example.mqtt3;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteDeleteResult;
import com.mongodb.stitch.core.services.mongodb.remote.RemoteInsertOneResult;

import org.bson.Document;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import helpers.ChartHelper;
import helpers.ChartHelper1;

public class Datasensor extends AppCompatActivity implements MqttCallback {
    Button deleteMany;
    int i=0;
    ChartHelper mChart;
    ChartHelper1 mChart1;
    LineChart chart;
    LineChart chart1;
    public TextView datatext1;
    public TextView data_device;
    public TextView sensortext;
    public TextView sensortext1;
    public TextView datatext2;

    private StitchAppClient stitchClient;
    private RemoteMongoClient mongoClient;
    private RemoteMongoCollection itemsCollection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_datasensor);
        datatext1 = (TextView) findViewById(R.id.datasensor1);
        chart = (LineChart) findViewById(R.id.chart);
        mChart = new ChartHelper(chart);
        chart1 = (LineChart) findViewById(R.id.chart1);
        mChart1 = new ChartHelper1(chart1);
        data_device = (TextView) findViewById(R.id.datadevice);
        sensortext = (TextView) findViewById(R.id.tipesensor1);
        sensortext1 = (TextView) findViewById(R.id.tipesensor2);
        datatext2 = (TextView) findViewById(R.id.datasensor2);
        deleteMany  = (Button) findViewById(R.id.deleteManyBtn);

        stitchClient = Stitch.getDefaultAppClient();
        mongoClient = stitchClient.getServiceClient(RemoteMongoClient.factory, "myAtlasCluster");
        itemsCollection = mongoClient.getDatabase("smap").getCollection("sensor");

        try {
            MqttClient client = new MqttClient("tcp://tailor.cloudmqtt.com:12073",
                    MqttClient.generateClientId(), new MemoryPersistence());
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("cxchrfbj");
            options.setPassword("3sqAJr0_sWHV".toCharArray());
            client.setCallback(this);
            client.connect(options);
            String topic = "esp/test";
            client.subscribe(topic);
        } catch (MqttException e) {
            e.printStackTrace();
        }

        deleteMany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete();
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {}};

    @Override
    public void connectionLost(Throwable cause) {
    }

    @Override
    public void messageArrived(String topic, final MqttMessage message) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //pic.get(0).setImageDrawable(getResources().getDrawable(R.drawable.coin));
                        try {
                            JSONObject jObj = new JSONObject(String.valueOf(message));
                            String device = jObj.getString("device");
                            data_device.setText(device);
                            JSONArray sensor = jObj.getJSONArray("sensorType");
                            sensortext.setText(sensor.getString(0));
                            sensortext1.setText(sensor.getString(1));
                            JSONArray nilaisensor = jObj.getJSONArray("values");
                            datatext1.setText(nilaisensor.getString(0 )+" BPM");
                            datatext2.setText(nilaisensor.getString(1)+" %");
                            mChart.addEntry(Float.valueOf(nilaisensor.getString(0)));
                            mChart1.addEntry(Float.valueOf(nilaisensor.getString(1)));
                            Calendar c1 = Calendar.getInstance();
                            SimpleDateFormat sdf1 = new SimpleDateFormat("d/M/yyyy h:m:s a");
                            String strdate1 = sdf1.format(c1.getTime());
                            TextView txtdate1 = (TextView) findViewById(R.id.timestamp);
                            txtdate1.setText(strdate1);

                            Document doc = new Document()
                                    .append("SMAP","sensor")
                                    .append(sensor.getString(0), nilaisensor.getString(0) + " BPM")
                                    .append(sensor.getString(1), nilaisensor.getString(1) + " %")
                                    .append("timestamp", strdate1)
                                    .append("User", String.format("%s", stitchClient.getAuth().getUser().getId()));

                            Task<RemoteInsertOneResult> insertTask = itemsCollection.insertOne(doc);
                            insertTask.addOnCompleteListener(new OnCompleteListener <RemoteInsertOneResult> () {
                                @Override
                                public void onComplete(@NonNull Task<RemoteInsertOneResult> task) {
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }).start();
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

    public void newAlertWithMessageOnUIThread(final String funcName, final String result) {
        final Context context = this;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d("SMAP Apps", String.format("Alert for func %s with result: %s", funcName, result));
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage(funcName + ": " + result);
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }

    /***************************************************************************
     * DELETE MANY                                                                 *
     ***************************************************************************/
    public void delete() {
        Document filterDoc = new Document().append("SMAP", "sensor");

        final Task<RemoteDeleteResult> deleteTask = itemsCollection.deleteMany(filterDoc);
        deleteTask.addOnCompleteListener(new OnCompleteListener<RemoteDeleteResult>() {
            @Override
            public void onComplete(@NonNull Task<RemoteDeleteResult> task) {
                if (task.isSuccessful()) {
                    newAlertWithMessageOnUIThread("deleteMany", String.format("successfully deleted %d documents", task.getResult().getDeletedCount()));
                } else {
                    newAlertWithMessageOnUIThread("deleteMany", String.format("failed with err: %s", task.getException().getLocalizedMessage()));
                }
            }
        });
    }
}