package fr.istic.mob.starproviderssp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import fr.istic.mob.starproviderssp.StarContract;
import fr.istic.mob.starproviderssp.table.BusRoutes;
import fr.istic.mob.starproviderssp.table.Calendar;
import fr.istic.mob.starproviderssp.table.StopTimes;
import fr.istic.mob.starproviderssp.table.Stops;
import fr.istic.mob.starproviderssp.table.Trips;

public class DB_Access {

    private SQLiteDatabase database;
    private DB_Starprovider dbstar;
    private StarContract sc;

    /**
     *
     * @param context
     */
    public DB_Access(Context context){
        dbstar = new DB_Starprovider(context);
        open();
    }

    public void open() throws SQLException {
        database = dbstar.getWritableDatabase();
    }

    public void insertCalendar(Calendar calendar){
        ContentValues values = new ContentValues();
        values.put(StarContract.Calendar.CalendarColumns.MONDAY,calendar.getMonday());
        values.put(StarContract.Calendar.CalendarColumns.TUESDAY,calendar.getTuesday());
        values.put(StarContract.Calendar.CalendarColumns.WEDNESDAY,calendar.getWednesday());
        values.put(StarContract.Calendar.CalendarColumns.THURSDAY,calendar.getThursday());
        values.put(StarContract.Calendar.CalendarColumns.FRIDAY,calendar.getFriday());
        values.put(StarContract.Calendar.CalendarColumns.SATURDAY,calendar.getSaturday());
        values.put(StarContract.Calendar.CalendarColumns.SUNDAY,calendar.getSunday());
        values.put(StarContract.Calendar.CalendarColumns.START_DATE,calendar.getStartdate());
        values.put(StarContract.Calendar.CalendarColumns.END_DATE,calendar.getEnddate());
        database.insert(StarContract.Calendar.CONTENT_PATH,null,values);
    }

    public void insertBusRoutes(BusRoutes busRoute){
        ContentValues values = new ContentValues();
        values.put(StarContract.BusRoutes.BusRouteColumns.SHORT_NAME,busRoute.getShort_name());
        values.put(StarContract.BusRoutes.BusRouteColumns.LONG_NAME,busRoute.getLong_name());
        values.put(StarContract.BusRoutes.BusRouteColumns.DESCRIPTION,busRoute.getDescritpion());
        values.put(StarContract.BusRoutes.BusRouteColumns.TYPE,busRoute.getType());
        values.put(StarContract.BusRoutes.BusRouteColumns.COLOR,busRoute.getColor());
        values.put(StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR,busRoute.getText_color());
        database.insert(StarContract.BusRoutes.CONTENT_PATH,null,values);
    }

    public void insertStops(Stops stops){
        ContentValues values = new ContentValues();
        values.put(StarContract.Stops.StopColumns.DESCRIPTION,stops.getDescription());
        values.put(StarContract.Stops.StopColumns.LATITUDE,stops.getLatitude());
        values.put(StarContract.Stops.StopColumns.LONGITUDE,stops.getLongitude());
        values.put(StarContract.Stops.StopColumns.NAME,stops.getName());
        values.put(StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING,stops.getWheelchairBoarding());
        database.insert(StarContract.Stops.CONTENT_PATH,null,values);
    }

    public void insertTrip(Trips trip){
        ContentValues values = new ContentValues();
        values.put(StarContract.Trips.TripColumns.SERVICE_ID,trip.getServiceId());
        values.put(StarContract.Trips.TripColumns.ROUTE_ID,trip.getRouteId());
        values.put(StarContract.Trips.TripColumns.HEADSIGN,trip.getHeadsign());
        values.put(StarContract.Trips.TripColumns.DIRECTION_ID,trip.getDirectionId());
        values.put(StarContract.Trips.TripColumns.BLOCK_ID,trip.getBlockId());
        values.put(StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE,trip.getWheelchairAccessible());
        database.insert(StarContract.Trips.CONTENT_PATH,null,values);
    }

    public void insertStopsTimes(StopTimes stoptimes){
        ContentValues values = new ContentValues();
        values.put(StarContract.StopTimes.StopTimeColumns.TRIP_ID,stoptimes.getTripId());
        values.put(StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME,stoptimes.getArrivalTime());
        values.put(StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME,stoptimes.getDepartureTime());
        values.put(StarContract.StopTimes.StopTimeColumns.STOP_ID,stoptimes.getStopId());
        values.put(StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE,stoptimes.getStopsequence());
        database.insert(StarContract.StopTimes.CONTENT_PATH,null,values);
    }


}
