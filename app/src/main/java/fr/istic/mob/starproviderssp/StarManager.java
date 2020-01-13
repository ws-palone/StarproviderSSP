package fr.istic.mob.starproviderssp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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

    String urlZip;
    private DB_Starprovider database;
    private SQLiteDatabase db;
    private ArrayList busRoutes;
    private ArrayList calendars;
    private ArrayList stops;
    private ArrayList stopsTimes;
    private ArrayList trips;

    public StarManager(@NonNull Context context, @NonNull WorkerParameters workerParams) {

        super(context, workerParams);
        MainActivity.getmInstanceActivity();
        MainActivity leThis = MainActivity.getmInstanceActivity();
        database = new DB_Starprovider(leThis);
        db = leThis.getDb();

    }

    @NonNull
    @Override
    public Result doWork() {

        busRoutes = new ArrayList<BusRoutes>();
        calendars = new ArrayList<Calendar>();
        stops = new ArrayList<Stops>();
        stopsTimes = new ArrayList<StopTimes>();
        trips = new ArrayList<Trips>();
        DB_Access dbAccess = new DB_Access(MainActivity.getmInstanceActivity());
        String lien = getLien(dbAccess);

        if(lien != null) {
            Log.d("URLZIP",lien);
            unzip(lien,dbAccess);
            dbAccess.insertTrip(trips);
            MainActivity.getmInstanceActivity().updateNotif(20);
            dbAccess.insertStopsTimes(stopsTimes);
            MainActivity.getmInstanceActivity().updateNotif(20);
            dbAccess.insertStops(stops);
            MainActivity.getmInstanceActivity().updateNotif(20);
            dbAccess.insertCalendar(calendars);
            MainActivity.getmInstanceActivity().updateNotif(20);
            dbAccess.insertBusRoutes(busRoutes);
            MainActivity.getmInstanceActivity().updateNotif(20);
            db.close();
            dbAccess.close();
            database.close();
            MainActivity.getmInstanceActivity().restartApp();
            return Result.SUCCESS;
        }
        return Result.FAILURE;
    }

    //La fonction getLien permet de récupérer le lien du fichier zip à télécharger par la suite.
    private String getLien(DB_Access dbAccess) {
        String day = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        //String day = "2019-12-24";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        try {
            String myUrl = "https://data.explore.star.fr/explore/dataset/tco-busmetro-horaires-gtfs-versions-td/download/?format=json&timezone=Europe/Berlin";
            URL url = new URL(myUrl);
            HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.getmInstanceActivity());
            try {
                InputStream in = new BufferedInputStream(connexion.getInputStream());
                String strEntry = readStream(in);
                JSONArray jsonArray = new JSONArray(strEntry);
                JSONObject leZip;
                String numeroZip;
                for (int numFichier = 0; numFichier < jsonArray.length(); numFichier++) {
                    Log.d("INDEX", Integer.toString(numFichier));
                    leZip = jsonArray.getJSONObject(numFichier);
                    String debut = leZip.getJSONObject("fields").getString("debutvalidite");
                    String fin = leZip.getJSONObject("fields").getString("finvalidite");
                    Date debutvalidite = sdf.parse(debut);
                    Date finvalidite = sdf.parse(fin);
                    Date today = sdf.parse(day);
                    numeroZip = leZip.getString("recordid");
                    if (prefs.contains("zip"))
                    Log.d("Prefs", prefs.getString("zip", null));
                    Log.d("ZIP", numeroZip);


                    SharedPreferences.Editor editor = prefs.edit();
                    //1er import, rien en mémoire
                    if (!prefs.contains("zip")) {
                        editor.putString("zip", numeroZip);
                        Log.d("Statut", "First import");
                        editor.commit();
                        urlZip = leZip.getJSONObject("fields").getString("url");
                        MainActivity.getmInstanceActivity().createNotification4();
                        return urlZip;
                    }

                    Log.d("Date", today.toString());
                    Log.d("Debut", debutvalidite.toString());
                    Log.d("Fin", finvalidite.toString());
                    Log.d("BOOLEAN1", Boolean.toString(today.before(finvalidite)));
                    Log.d("BOOLEAN2", Boolean.toString(today.after(debutvalidite)));
                    File f = MainActivity.getmInstanceActivity().getDatabasePath(db.getPath());
                    long dbSize = f.length();
                    Log.d("DBSize", Long.toString(dbSize));
                    //Si la date est "valide"
                    if (today.before(finvalidite) && today.after(debutvalidite)) {
                        if (!prefs.getString("zip", null).equals(numeroZip)) {
                            editor.putString("zip", numeroZip);
                            editor.commit();
                            database.clearData(db);
                            urlZip = leZip.getJSONObject("fields").getString("url");
                            MainActivity.getmInstanceActivity().createNotification();
                        }
                        break;
                    }
                }
                return urlZip;
            } finally {
                connexion.disconnect();
            }
        } catch (Exception e) {
            MainActivity.getmInstanceActivity().createNotification2();
            e.printStackTrace();
        }
        return urlZip;
    }

    private void unzip(String urlZip,DB_Access dbAccess) {
        try {
            URL urlzip = new URL(urlZip);
            HttpURLConnection zipConnexion = (HttpURLConnection) urlzip.openConnection();
            ZipInputStream inputStreamzip = new ZipInputStream(zipConnexion.getInputStream());
            ZipEntry entry = inputStreamzip.getNextEntry();
            while(entry != null) {
                switch(entry.getName()) {
                    case "calendar.txt":
                    case "stops.txt":
                    case "routes.txt":
                    case "stop_times.txt" :
                    case "trips.txt":
                        readLines(entry,inputStreamzip,dbAccess);
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

    //Permet de lire les lignes présentes dans le fichier json.
    private String readStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new InputStreamReader(is),1000);
        for (String line = r.readLine(); line != null; line =r.readLine()){
            sb.append(line);
        }
        is.close();
        return sb.toString();

    }

    //Cette fonction lit les lignes des fichiers utiles.
    private void readLines (ZipEntry entry, ZipInputStream zip,DB_Access dbAccess) {
        BufferedReader in = new BufferedReader(new InputStreamReader(zip));
        String responseData;



        try {
            in.readLine();
            String line;
            while((line = in.readLine()) != null) {
                responseData = line;
                //String[] l = responseData.toString().replace("\"","").split(",");
                String[] l2 = responseData.replace("\"","").split(",");
                ///insertBDD(l,entry,dbAccess,i);
                insertPreBDD(l2,entry);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertPreBDD(String[] line, ZipEntry entry) {
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
                calendars.add(calendar);
                //dbAccess.insertCalendar(calendar);
                break;
            case "routes.txt":
                BusRoutes busroutes = new BusRoutes();
                busroutes.setShort_name(line[2]);
                busroutes.setLong_name(line[3]);
                busroutes.setDescritpion(line[4]);
                busroutes.setType(line[5]);
                busroutes.setColor(line[7]);
                busroutes.setText_color(line[8]);
                busRoutes.add(busroutes);
                //dbAccess.insertBusRoutes(busroutes);
                break;
            case "stops.txt":
                Stops stop = new Stops();
                stop.setName(line[2]);
                stop.setDescription(line[3]);
                stop.setLatitude(line[4]);
                stop.setLongitude(line[5]);
                stop.setWheelchairBoarding(line[11]);
                stops.add(stop);
                //dbAccess.insertStops(stops);
                break;
            case "stop_times.txt":
                StopTimes stoptimes = new StopTimes();
                stoptimes.setStopId(line[3]);
                stoptimes.setTripId(line[0]);
                stoptimes.setDepartureTime(line[2]);
                stoptimes.setArrivalTime(line[1]);
                stoptimes.setStopsequence(line[4]);
                stopsTimes.add(stoptimes);
                //dbAccess.insertStopsTimes(stoptimes);
                break;
            case "trips.txt":
                Trips trip = new Trips();
                trip.setBlockId(line[6]);
                trip.setDirectionId(Integer.parseInt(line[5]));
                trip.setHeadsign(line[3]);
                trip.setRouteId(Integer.parseInt(line[0]));
                trip.setServiceId(Integer.parseInt(line[1]));
                trip.setWheelchairAccessible(Integer.parseInt(line[8]));
                trips.add(trip);
                //dbAccess.insertTrip(trips);
                break;
        }
    }

/*
    //Ici, nous insérons dans la BDD les lignes préalablement lues.
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
    }*/
}