package fr.istic.mob.starproviderssp.database;

import android.content.Context;

import fr.istic.mob.starproviderssp.database.DB_Starprovider;

public class DB_Access {

    private String dbName = "db_starprovider.sqlite";
    private Integer dbVersion = 1;
    private DB_Starprovider dbAccess;

    /**
     *
     * @param context
     */
    public DB_Access(Context context){
        dbAccess = new DB_Starprovider(context, dbName, null, dbVersion);
    }
}
