package fr.istic.mob.starproviderssp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONObject;

import java.io.InputStream;
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
            connexion.setRequestMethod("GET");
            connexion.connect();
            InputStream inputStream = connexion.getInputStream();
            String result = inputStream.toString();
            JSONObject jsonObject = new JSONObject(result);
            JSONObject fields = jsonObject.getJSONObject("fields");
            JSONObject urljson = fields.getJSONObject("url");
            String urlbase = urljson.toString();
            Log.d("bonjour",urlbase);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.SUCCESS;
    }
}
