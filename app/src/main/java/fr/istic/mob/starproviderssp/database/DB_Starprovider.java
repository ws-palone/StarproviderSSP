package fr.istic.mob.starproviderssp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import fr.istic.mob.starproviderssp.StarContract;

public class DB_Starprovider extends SQLiteOpenHelper {

    private static final String dbName = "db_starprovider.db";
    private static final Integer dbVersion = 1;

    /**
     *
     * @param context
     */
    public DB_Starprovider(Context context) {
        super(context, dbName, null, dbVersion);
    }

    /**
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createBus);
        db.execSQL(createStop);
        db.execSQL(createStopTimes);
        db.execSQL(createCalendar);
        db.execSQL(createTrip);
    }

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //le SQL de création de la base
    private static final String createBus = "CREATE TABLE IF NOT EXISTS  "+ StarContract.BusRoutes.CONTENT_PATH + " ("
                + StarContract.BusRoutes.BusRouteColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + StarContract.BusRoutes.BusRouteColumns.SHORT_NAME + " TEXT NOT NULL,"
                + StarContract.BusRoutes.BusRouteColumns.LONG_NAME + " TEXT NOT NULL,"
                + StarContract.BusRoutes.BusRouteColumns.DESCRIPTION +" TEXT,"
                + StarContract.BusRoutes.BusRouteColumns.TYPE + " INTEGER NOT NULL,"
                + StarContract.BusRoutes.BusRouteColumns.COLOR + " TEXT NOT NULL,"
                + StarContract.BusRoutes.BusRouteColumns.TEXT_COLOR + " TEXT NOT NULL);";

    public static final String createTrip = "CREATE TABLE IF NOT EXISTS  "+ StarContract.Trips.CONTENT_PATH +" ("
                + StarContract.Trips.TripColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StarContract.Trips.TripColumns.SERVICE_ID + " INTEGER NOT NULL, "
                + StarContract.Trips.TripColumns.ROUTE_ID + " INTEGER NOT NULL, "
                + StarContract.Trips.TripColumns.HEADSIGN + " TEXT NOT NULL, "
                + StarContract.Trips.TripColumns.DIRECTION_ID + " INTEGER NOT NULL, "
                + StarContract.Trips.TripColumns.BLOCK_ID + " TEXT NOT NULL, "
                + StarContract.Trips.TripColumns.WHEELCHAIR_ACCESSIBLE + " INTEGER NOT NULL);";

    public static final String createStop ="CREATE TABLE IF NOT EXISTS  "+ StarContract.Stops.CONTENT_PATH +" ("
                + StarContract.Stops.StopColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StarContract.Stops.StopColumns.DESCRIPTION  + " TEXT, "
                + StarContract.Stops.StopColumns.LATITUDE + " REAL NOT NULL, "
                + StarContract.Stops.StopColumns.LONGITUDE+ " REAL NOT NULL, "
                + StarContract.Stops.StopColumns.NAME+ " TEXT NOT NULL, "
                + StarContract.Stops.StopColumns.WHEELCHAIR_BOARDING + " INTEGER NOT NULL);";

    public static final String createStopTimes = "CREATE TABLE IF NOT EXISTS  "+ StarContract.StopTimes.CONTENT_PATH +" ("
                + StarContract.StopTimes.StopTimeColumns._ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + StarContract.StopTimes.StopTimeColumns.TRIP_ID+" INTEGER NOT NULL,"
                + StarContract.StopTimes.StopTimeColumns.ARRIVAL_TIME+" TEXT NOT NULL,"
                + StarContract.StopTimes.StopTimeColumns.DEPARTURE_TIME+" TEXT NOT NULL,"
                + StarContract.StopTimes.StopTimeColumns.STOP_ID+" TEXT NOT NULL,"
                + StarContract.StopTimes.StopTimeColumns.STOP_SEQUENCE+" INTEGER NOT NULL);";

    public static final String createCalendar ="CREATE TABLE IF NOT EXISTS  "+ StarContract.Calendar.CONTENT_PATH +" ("
                + StarContract.Calendar.CalendarColumns._ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + StarContract.Calendar.CalendarColumns.MONDAY+" INTEGER NOT NULL,"
                + StarContract.Calendar.CalendarColumns.TUESDAY+" INTEGER NOT NULL,"
                + StarContract.Calendar.CalendarColumns.WEDNESDAY+" INTEGER NOT NULL,"
                + StarContract.Calendar.CalendarColumns.THURSDAY+" INTEGER NOT NULL,"
                + StarContract.Calendar.CalendarColumns.FRIDAY+" INTEGER NOT NULL,"
                + StarContract.Calendar.CalendarColumns.SATURDAY+" INTEGER NOT NULL,"
                + StarContract.Calendar.CalendarColumns.SUNDAY+" INTEGER NOT NULL,"
                + StarContract.Calendar.CalendarColumns.START_DATE+" INTEGER NOT NULL,"
                + StarContract.Calendar.CalendarColumns.END_DATE +" INTEGER NOT NULL);";

    public void onDelete(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " +StarContract.BusRoutes.CONTENT_PATH +
                StarContract.Trips.CONTENT_PATH+
                StarContract.Stops.CONTENT_PATH+
                StarContract.StopTimes.CONTENT_PATH+
                StarContract.Calendar.CONTENT_PATH );

    }
}