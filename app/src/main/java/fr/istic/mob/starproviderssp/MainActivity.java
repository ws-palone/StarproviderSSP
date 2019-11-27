package fr.istic.mob.starproviderssp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.app.DownloadManager;
import android.os.Bundle;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //WorkManager
        getJSON();
    }

    public void getJSON(){
        OneTimeWorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(StarManager.class).build();
        WorkManager.getInstance().enqueue(uploadWorkRequest);
    }
}
