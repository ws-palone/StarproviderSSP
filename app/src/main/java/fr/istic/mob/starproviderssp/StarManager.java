package fr.istic.mob.starproviderssp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StarManager extends Worker {

    public StarManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        try{
            String myUrl = "https://data.explore.star.fr/explore/dataset/tco-busmetro-horaires-gtfs-versions-td/download/?format=json&timezone=Europe/Berlin";
            URL url = new URL(myUrl);
            HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(connexion.getInputStream());
                readStream(in);
            } finally {
                connexion.disconnect();
            }
            /*InputStream inputStream = connexion.getInputStream();
            String result = inputStream.toString();
            JSONObject jsonObject = new JSONObject(result);
            JSONArray fields = jsonObject.getJSONArray("fields");
            for (int i=0; i < fields.length(); i++)
            {
                try {
                    JSONObject oneObject = fields.getJSONObject(i);
                    // Pulling items from the array
                    String urlbase = oneObject.toString();
                    Log.d("bonjour",urlbase);
                } catch (JSONException e) {
                }
            }*/
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.SUCCESS;
    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        Log.d("bonjour",sb.toString());
        return sb.toString();
    }
}
