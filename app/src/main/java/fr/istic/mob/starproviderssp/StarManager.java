package fr.istic.mob.starproviderssp;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
                String json = readStream(in);
                String[] arrStrg = json.split(",");
                String[] arrUrl = new String[10];
                int y =0;
                for (int i = 0; i < arrStrg.length; i++){
                    String value = arrStrg[i];
                    if(value.contains("url")){
                        String[] arrSplit = value.split("\"");
                        String result = arrSplit[3];
                        arrUrl[y] = result;
                        y++;
                    }
                }
                String urlZip = arrUrl[0];
                unzip(urlZip);
            } finally {
                connexion.disconnect();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return Result.SUCCESS;
    }

    private void unzip(String urlZip) {
        try {
            URL url = new URL(urlZip);
            HttpURLConnection zipConnexion = (HttpURLConnection) url.openConnection();
            ZipInputStream inputStreamzip = new ZipInputStream(zipConnexion.getInputStream());
            ZipEntry entry = inputStreamzip.getNextEntry();
            ArrayList<String[]> arrayLine = new ArrayList<String[]>();
                while(entry != null) {
                    
                }
            } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }
}