package fr.istic.mob.starproviderssp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import fr.istic.mob.starproviderssp.StarContract;
import fr.istic.mob.starproviderssp.table.BusRoutes;
import fr.istic.mob.starproviderssp.table.Calendar;
import fr.istic.mob.starproviderssp.table.StopTimes;
import fr.istic.mob.starproviderssp.table.Stops;
import fr.istic.mob.starproviderssp.table.Trips;

public class DB_Access {

    private SQLiteDatabase database;
    private DB_Starprovider dbstar;

    /**
     *
     * @param context
     */
    public DB_Access(Context context){
        dbstar = new DB_Starprovider(context);
        open();
    }
    public void close() throws SQLException {
        database.close();
    }
    public void open() throws SQLException {
        database = dbstar.getWritableDatabase();
    }

    public void insertCalendar(List<Calendar> calendar) {
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (Calendar cal : calendar) {
                values.put(StarContract.Calendar.CalendarColumns.MONDAY, cal.getMonday());
                values.put(StarContract.Calendar.CalendarColumns.TUESDAY, cal.getTuesday());
                values.put(StarContract.Calendar.CalendarColumns.WEDNESDAY, cal.getWednesday());
                values.put(StarContract.Calendar.CalendarColumns.THURSDAY, cal.getThursday());
                values.put(StarContract.Calendar.CalendarColumns.FRIDAY, cal.getFriday());
                values.put(StarContract.Calendar.CalendarColumns.SATURDAY, cal.getSaturday());
                values.put(StarContract.Calendar.CalendarColumns.SUNDAY, cal.getSunday());
                values.put(StarContract.Calendar.CalendarColumns.START_DATE, cal.getStartdate());
                values.put(StarContract.Calendar.CalendarColumns.END_DATE, cal.getEnddate());
                database.insert(StarContract.Calendar.CONTENT_PATH, null, values);
            }
            database.setTransactionSuccessful();
        }
        finally{
                database.endTransaction();
            }
    }
    public void insertBusRoutes(List<BusRoutes> busRoute) {
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (BusRoutes route : busRoute) {
                values.put(StarContract.BusRoutes.BusRouteColumns.SHORT_NAME, route.getShort_name());
                values.put(StarContract.BusRoutes.BusRouteColumns.LONG_NAME, route.getLong_name());
                values.put(StarContract.BusRoutes.BusRouteColumns.DESCRIPTION, route.getDescritpion());
                values.put(StarContract.BusRoutes.BusRouteColumns.TYPE, route.getType());
                values.put(StarContract.BusRoutes.BusRouteColumns.COLOR, route.getColor());
                values.put(StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR, route.getText_color());
                database.insert(StarContract.BusRoutes.CONTENT_PATH, null, values);
            }
            database.setTransactionSuccessful();
        }
        finally {
            database.endTransaction();
        }
    }
    public void insertStops(List<Stops> stops){
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (Stops stop : stops) {
                values.put(StarContract.Stops.StopColumns.DESCRIPTION,stop.getDescription());
                values.put(StarContract.Stops.StopColumns.LATITUDE,stop.getLatitude());
                values.put(StarContract.Stops.StopColumns.LONGITUDE,stop.getLongitude());
                values.put(StarContract.Stops.StopColumns.NAME,stop.getName());
                values.put(StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING,stop.getWheelchairBoarding());
                database.insert(StarContract.Stops.CONTENT_PATH,null,values);
            }
            database.setTransactionSuccessful();
        }
        finally {
            database.endTransaction();
        }
    }

    public void insertTrip(List<Trips> trip) {
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (Trips voyage : trip) {
                values.put(StarContract.Trips.TripColumns.SERVICE_ID, voyage.getServiceId());
                values.put(StarContract.Trips.TripColumns.ROUTE_ID, voyage.getRouteId());
                values.put(StarContract.Trips.TripColumns.HEADSIGN, voyage.getHeadsign());
                values.put(StarContract.Trips.TripColumns.DIRECTION_ID, voyage.getDirectionId());
                values.put(StarContract.Trips.TripColumns.BLOCK_ID, voyage.getBlockId());
                values.put(StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE, voyage.getWheelchairAccessible());
                database.insert(StarContract.Trips.CONTENT_PATH, null, values);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }

    public void insertStopsTimes(List<StopTimes> stoptimes){
        database.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (StopTimes stopTime : stoptimes) {
                values.put(StarContract.StopTimes.StopTimeColumns.TRIP_ID,stopTime.getTripId());
                values.put(StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME,stopTime.getArrivalTime());
                values.put(StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME,stopTime.getDepartureTime());
                values.put(StarContract.StopTimes.StopTimeColumns.STOP_ID,stopTime.getStopId());
                values.put(StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE,stopTime.getStopsequence());
                database.insert(StarContract.StopTimes.CONTENT_PATH,null,values);
            }
            database.setTransactionSuccessful();
        } finally {
            database.endTransaction();
        }
    }
}
