package fr.istic.mob.starproviderssp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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

import fr.istic.mob.starproviderssp.database.DB_Access;
import fr.istic.mob.starproviderssp.database.DB_Starprovider;
import fr.istic.mob.starproviderssp.table.BusRoutes;
import fr.istic.mob.starproviderssp.table.Calendar;
import fr.istic.mob.starproviderssp.table.StopTimes;
import fr.istic.mob.starproviderssp.table.Stops;
import fr.istic.mob.starproviderssp.table.Trips;

public class StarManager extends Worker {

    ArrayList<String> urlZip = new ArrayList<>();
    private DB_Starprovider database;
    private SQLiteDatabase db;


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

        DB_Access dbAccess = new DB_Access(MainActivity.getmInstanceActivity());
        try {
            in.readLine();
            String line;
            while((line = in.readLine()) != null) {
                StringBuilder responseData = new StringBuilder(line);
                String[] l = responseData.toString().replace("\"","").split(",");
                insertBDD(l,entry,dbAccess);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertBDD(String[] line, ZipEntry entry, DB_Access dbAccess) {

        switch(entry.getName()){
            case "calendar.txt":
                Calendar calendar= new Calendar();
                calendar.setMonday(line[1]);
                calendar.setTuesday(line[2]);
                calendar.setWednesday(line[3]);
                calendar.setThursday(line[4]);
                calendar.setFriday(line[5]);
                calendar.setSaturday(line[6]);
                calendar.setSunday(line[7]);
                calendar.setStartdate(line[8]);
                calendar.setEnddate(line[9]);
                Log.d("1","1");
                dbAccess.insertCalendar(calendar);
                break;
            case "routes.txt":
                BusRoutes busroutes = new BusRoutes();
                busroutes.setShort_name(line[2]);
                busroutes.setLong_name(line[3]);
                busroutes.setDescritpion(line[4]);
                busroutes.setType(line[5]);
                busroutes.setColor(line[7]);
                busroutes.setText_color(line[8]);
                dbAccess.insertBusRoutes(busroutes);
                Log.d("2","2");
                break;
            case "stop.txt":
                Stops stops = new Stops();
                stops.setName(line[2]);
                stops.setDescription(line[3]);
                stops.setLatitude(line[4]);
                stops.setLongitude(line[5]);
                stops.setWheelchairBoarding(line[11]);
                dbAccess.insertStops(stops);
                Log.d("3","3");
                break;
            case "stop_times.txt":
                StopTimes stoptimes = new StopTimes();
                stoptimes.setStopId(line[3]);
                stoptimes.setTripId(line[0]);
                stoptimes.setDepartureTime(line[2]);
                stoptimes.setArrivalTime(line[1]);
                stoptimes.setStopsequence(line[4]);
                dbAccess.insertStopsTimes(stoptimes);
                Log.d("4","4");
                break;
            case "trips.txt":
                Trips trips = new Trips();
                trips.setBlockId(line[6]);
                trips.setDirectionId(line[5]);
                trips.setHeadsign(line[3]);
                trips.setRouteId(line[0]);
                trips.setServiceId(line[1]);
                trips.setWheelchairAccessible(line[8]);
                dbAccess.insertTrip(trips);
                break;
        }
    }
}