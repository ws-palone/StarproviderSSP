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
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class StarManager extends Worker {

    ArrayList<String> urlZip = new ArrayList<>();


    public StarManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        ArrayList liens = getLien();
        Iterator<String> it = liens.iterator();
        while(it.hasNext()){
            String lien = it.next();
            unzip(lien);
        }
        return Result.SUCCESS;
    }

    private ArrayList<String> getLien(){
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
                        urlZip.add(arrUrl[0]);
                    }
                }
                return urlZip;
            } finally {
                connexion.disconnect();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private void unzip(String urlZip) {
        try {
            URL urlzip = new URL(urlZip);
            HttpURLConnection zipConnexion = (HttpURLConnection) urlzip.openConnection();
            ZipInputStream inputStreamzip = new ZipInputStream(zipConnexion.getInputStream());
            ZipEntry entry = inputStreamzip.getNextEntry();
            while(entry != null) {
                switch(entry.getName()) {
                    case "calendar.txt":
                        readLines(entry,inputStreamzip);
                        entry = inputStreamzip.getNextEntry();
                        break;
                    case "stops.txt":
                        readLines(entry,inputStreamzip);
                        entry = inputStreamzip.getNextEntry();
                        break;
                    case "routes.txt":
                        readLines(entry,inputStreamzip);
                        entry = inputStreamzip.getNextEntry();
                        break;
                    case "stop_times.txt":
                        readLines(entry,inputStreamzip);
                        entry = inputStreamzip.getNextEntry();
                        break;
                    case "trips.txt":
                        readLines(entry,inputStreamzip);
                        entry = inputStreamzip.getNextEntry();
                        break;
                    default:
                        entry = inputStreamzip.getNextEntry();
                        break;
                }
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

    private void readLines (ZipEntry entry, ZipInputStream zip) {
        BufferedReader in = new BufferedReader(new InputStreamReader(zip));
        try {
            in.readLine();
            String line;
            while((line = in.readLine()) != null) {
                StringBuilder responseData = new StringBuilder(line);
                String[] l = responseData.toString().split(",");
                insertBDD(l,entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertBDD(String[] line, ZipEntry entry) {

        switch(entry.getName()){
            case "calendar.txt":
                /*setId(Integer.parseInt(line[0]));
                setMonday(line[1]);
                setTuesday(line[2]);
                setWednesday(line[3]);
                setThursday(line[4]);
                setFriday(line[5];
                setSaturday(line[6]);
                setSunday(line[7]);
                setStartDate(Integer.parseInt(line[8]));
                setEndDate(Integer.parseInt(line[9]));*/
                break;
            case "routes.txt":
                /*setId(Integer.parseInt(line[0]));
                setRouteShortName(line[2]);
                setRouteLongName(line[3]);
                setRouteDescritpion(line[4]);
                setRoutetype(line[5]);
                setRouteColor(line[7]);
                setRouteTextColor(line[8]);*/
                break;
            case "stop.txt":
                /*setId(line[0]);
                setStopName(line[2]);
                setStopDesc(line[3]);
                setStopLat(line[4]);
                setStopLon(line[5]);
                setWheelchairBoarding(Integer.parseInt(line[11]));*/
                break;
        }
    }
}