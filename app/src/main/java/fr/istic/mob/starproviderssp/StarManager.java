package fr.istic.mob.starproviderssp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;

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
        database = new DB_Starprovider(MainActivity.getmInstanceActivity());
        db = database.getWritableDatabase();

        if(liens != null){
            Iterator<String> it = liens.iterator();
            int i =0;
            while(it.hasNext()){
                String lien = it.next();
                unzip(lien,i);
                i++;
            }
            db.close();
            return Result.SUCCESS;
        }
        else{
            db.close();
            return Result.FAILURE;
        }
    }

    private ArrayList<String> getLien(){
        try{
            String myUrl = "https://data.explore.star.fr/explore/dataset/tco-busmetro-horaires-gtfs-versions-td/download/?format=json&timezone=Europe/Berlin";
            URL url = new URL(myUrl);
            HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getmInstanceActivity());
            try {
                InputStream in = new BufferedInputStream(connexion.getInputStream());
                String json = readStream(in);
                String[] arrStrg = json.split(",");
                String[] arrUrl = new String[10];
                int y =0;
                int nzip = 1;
                int quentin = 0;

                for (int i = 0; i < arrStrg.length; i++){
                    boolean premierfois = false;
                    String value = arrStrg[i];
                    String numzip = Integer.toString(nzip);
                    //Prends en compte seulement les cas où il y a un nouveau fichier;
                    if(value.contains("debutvalidite")) {
                         if (quentin == 0){
                            MainActivity.getmInstanceActivity().createNotification();
                            quentin++;
                        }
                        String[] arrSplit = value.split("\"");
                        if(prefs.getString(numzip,"")==""){
                            premierfois=true;
                        }
                        if (!arrSplit[3].equals(prefs.getString(numzip, ""))) {
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString(numzip, arrSplit[3]);
                            editor.commit();
                            if (database != null) {
                                database.onDelete(db);
                            }
                        } else {
                            urlZip.clear();
                            return urlZip;
                        }
                        nzip++;
                    }
                    if(value.contains("url")){
                        String[] arrSplit = value.split("\"");
                        String result = arrSplit[3];
                        arrUrl[y] = result;
                        y++;
                        urlZip.add(arrUrl[0]);
                    }
                    if(premierfois){
                        return urlZip;
                    }
                }
                return urlZip;
            } finally {
                connexion.disconnect();
            }
        }catch (Exception e){
            //NOTIF PAS DE CONNECTION
            MainActivity.getmInstanceActivity().createNotification2();
            e.printStackTrace();


        }
        return null;
    }

    private void unzip(String urlZip, int i) {
        try {
            URL urlzip = new URL(urlZip);
            HttpURLConnection zipConnexion = (HttpURLConnection) urlzip.openConnection();
            ZipInputStream inputStreamzip = new ZipInputStream(zipConnexion.getInputStream());
            ZipEntry entry = inputStreamzip.getNextEntry();
            while(entry != null) {
                switch(entry.getName()) {
                    case "calendar.txt":
                        readLines(entry,inputStreamzip,i);
                        entry = inputStreamzip.getNextEntry();
                        MainActivity.getmInstanceActivity().updateNotif(20);
                        break;
                    case "stops.txt":
                        readLines(entry,inputStreamzip,i);
                        entry = inputStreamzip.getNextEntry();
                        MainActivity.getmInstanceActivity().updateNotif(20);
                        break;
                    case "routes.txt":
                        readLines(entry,inputStreamzip,i);
                        entry = inputStreamzip.getNextEntry();
                        MainActivity.getmInstanceActivity().updateNotif(20);
                        break;
                    case "stop_times.txt":
                        readLines(entry,inputStreamzip,i);
                        entry = inputStreamzip.getNextEntry();
                        MainActivity.getmInstanceActivity().updateNotif(20);
                        break;
                    case "trips.txt":
                        readLines(entry,inputStreamzip,i);
                        entry = inputStreamzip.getNextEntry();
                        MainActivity.getmInstanceActivity().updateNotif(20);
                        break;
                    default:
                        entry = inputStreamzip.getNextEntry();
                        break;
                }
            }
        } catch (IOException e) {
            //NOTIF ERREUR ZIP
            MainActivity.getmInstanceActivity().createNotification3();
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

    private void readLines (ZipEntry entry, ZipInputStream zip, int i) {
        BufferedReader in = new BufferedReader(new InputStreamReader(zip));

        DB_Access dbAccess = new DB_Access(MainActivity.getmInstanceActivity());
        try {
            in.readLine();
            String line;
            while((line = in.readLine()) != null) {
                StringBuilder responseData = new StringBuilder(line);
                String[] l = responseData.toString().replace("\"","").split(",");
                insertBDD(l,entry,dbAccess,i);
            }
        } catch (IOException e) {
            e.printStackTrace();
            //La ligne st pétée du cul

        }
    }

    private void insertBDD(String[] line, ZipEntry entry, DB_Access dbAccess, int i) {
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
                dbAccess.insertCalendar(calendar);
                break;
            case "routes.txt":
                if(i==0) {
                    BusRoutes busroutes = new BusRoutes();
                    busroutes.setShort_name(line[2]);
                    busroutes.setLong_name(line[3]);
                    busroutes.setDescritpion(line[4]);
                    busroutes.setType(line[5]);
                    busroutes.setColor(line[7]);
                    busroutes.setText_color(line[8]);
                    dbAccess.insertBusRoutes(busroutes);
                }
                break;
            case "stops.txt":
                if(i==0) {
                    Stops stops = new Stops();
                    stops.setName(line[2]);
                    stops.setDescription(line[3]);
                    stops.setLatitude(line[4]);
                    stops.setLongitude(line[5]);
                    stops.setWheelchairBoarding(line[11]);
                    dbAccess.insertStops(stops);
                }
                break;
            case "stop_times.txt":
                StopTimes stoptimes = new StopTimes();
                stoptimes.setStopId(line[3]);
                stoptimes.setTripId(line[0]);
                stoptimes.setDepartureTime(line[2]);
                stoptimes.setArrivalTime(line[1]);
                stoptimes.setStopsequence(line[4]);
                dbAccess.insertStopsTimes(stoptimes);
                break;
            case "trips.txt":
                Trips trips = new Trips();
                trips.setBlockId(line[6]);
                trips.setDirectionId(Integer.parseInt(line[5]));
                trips.setHeadsign(line[3]);
                trips.setRouteId(Integer.parseInt(line[0]));
                trips.setServiceId(Integer.parseInt(line[1]));
                trips.setWheelchairAccessible(Integer.parseInt(line[8]));
                dbAccess.insertTrip(trips);
                break;
        }
    }
}