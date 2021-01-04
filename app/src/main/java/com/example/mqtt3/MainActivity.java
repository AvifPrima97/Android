package com.example.mqtt3;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.mqtt3.LocationOwner.RetailerDashboardFragment;
import com.example.mqtt3.LocationOwner.RetailerManageFragment;
import com.example.mqtt3.LocationOwner.RetailerProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.mongodb.stitch.android.core.Stitch;
import com.mongodb.stitch.android.core.StitchAppClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteFindIterable;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoClient;
import com.mongodb.stitch.android.services.mongodb.remote.RemoteMongoCollection;

import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    private StitchAppClient stitchClient;
    private RemoteMongoClient mongoClient;
    private RemoteMongoCollection itemsCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        stitchClient = Stitch.getDefaultAppClient();
        mongoClient = stitchClient.getServiceClient(RemoteMongoClient.factory, "myAtlasCluster");
        itemsCollection = mongoClient.getDatabase("smap").getCollection("sensor");

        chipNavigationBar = findViewById(R.id.bottom_nav_menu);
        chipNavigationBar.setItemSelected(R.id.bottom_nav_dashboard,true);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RetailerDashboardFragment()).commit();
        bottomMenu();
    }

    private void bottomMenu() {
        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;
                switch (i) {
                    case R.id.bottom_nav_dashboard:
                        fragment = new RetailerDashboardFragment();
                        break;
                    case R.id.bottom_nav_manage:
                        fragment = new RetailerManageFragment();
                        break;
                    case R.id.bottom_nav_profile:
                        fragment = new RetailerProfileFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });
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

    public void findMany() {
        Document filterDoc = new Document()
                //.append("reviews.0", new Document().append("$exists", true));
        .append("SMAP","sensor");

        RemoteFindIterable findResults = itemsCollection
                .find(filterDoc)
                .projection(new Document().append("_id", 0))
                .sort(new Document().append("name", 1));

        // One way to iterate through
        findResults.forEach(item -> {
            Log.d("SMAP Apps", String.format("successfully found:  %s", item.toString()));
        });

        // Another way to iterate through
        Task<List<Document>> itemsTask = findResults.into(new ArrayList<Document>());
        itemsTask.addOnCompleteListener(new OnCompleteListener<List<Document>>() {
            @Override
            public void onComplete(@NonNull Task<List<Document>> task) {
                if (task.isSuccessful()) {
                    String res = "";
                    List<Document> items = task.getResult();
                    res += String.format("successfully found %d documents [", items.size());
                    for (Document item : items) {
                        res +=  item.toString() + "\n";
                    }
                    res += "]";
                    newAlertWithMessageOnUIThread("findMany", res);
                } else {
                    newAlertWithMessageOnUIThread("findMany", String.format("failed with err: %s", task.getException().getLocalizedMessage()));
                }
            }
        });
    }

    public void logout() {
        // Get the default AppClient
        StitchAppClient stitchClient = Stitch.getDefaultAppClient();

        // Login with Anonymous credentials and handle the result
        stitchClient.getAuth().logout().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    newAlertWithMessageOnUIThread("logout", "success");
                } else {
                    newAlertWithMessageOnUIThread("logout", String.format("failed with err: %s", task.getException().getLocalizedMessage()));
                }
            }
        });
    }
}

