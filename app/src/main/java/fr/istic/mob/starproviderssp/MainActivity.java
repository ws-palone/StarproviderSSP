package fr.istic.mob.starproviderssp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import fr.istic.mob.starproviderssp.database.DB_Starprovider;



public class MainActivity extends AppCompatActivity {
    String CHANNEL_ID = "1";
    int PROGRESS_MAX = 100;
    int PROGRESS_CURRENT = 0;
    int CURRENT_ID = 1;
    private static MainActivity MainAct;
    public static MainActivity getmInstanceActivity(){return MainAct;}
    private DB_Starprovider database;
    private SQLiteDatabase db;
    NotificationManagerCompat notificationManager;
    public NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

    public NotificationCompat.Builder getBuilder(){
        return  builder;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        database = new DB_Starprovider(this);
        db=database.getWritableDatabase();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        Button pickDateHour = findViewById(R.id.pick_date_hour);
        pickDateHour.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                DatePicker datePicker = findViewById(R.id.datePicker);
                int month = datePicker.getMonth()+1;
                int day = datePicker.getDayOfMonth();
                int year = datePicker.getYear();
                TimePicker timePicker = findViewById(R.id.timePicker);
                int hour = timePicker.getHour();
                int minute = timePicker.getMinute();
                //Toast.makeText(MainActivity.this, timeToString(hour,minute), Toast.LENGTH_LONG).show();
            }
        });
        /*ArrayAdapter<CharSequence> adapter = new ArrayAdapter <CharSequence> (getApplicationContext(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String selectQuery ="SELECT " + StarContract.BusRoutes.BusRouteColumns._ID + " FROM " +
                StarContract.BusRoutes.CONTENT_PATH + ";";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        int u = 100;
        while (cursor.isAfterLast() == false){
            adapter.add("ssssss" + u);
            u++;
        }
        Spinner spin = findViewById(R.id.line);
        spin.setAdapter(adapter);*/

        MainAct = this;
        /*
        for (int i = 0; i < 10; i++) {
            sleep(1000);
            Log.d("Sleep", " i : " + i);
            PROGRESS_CURRENT += 10;
            builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
            notificationManager.notify(1, builder.build());

        }
        */

        getJSON();


        db.close();

    }
    public void restart(){
        Intent restartIntent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(restartIntent);
    }



    public void updateNotif(int i) {

        PROGRESS_CURRENT += i;
        if (PROGRESS_CURRENT < 100) {
            builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
            notificationManager.notify(CURRENT_ID, builder.build());
        } else {
            //Completed
            builder.setContentText("Download complete")
                    .setProgress(0, 0, false);
            notificationManager.notify(CURRENT_ID, builder.build());
        }
    }
    public void createNotification() {
        builder.setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setContentTitle(getString(R.string.newCSV))
                .setContentText(getString(R.string.newCSVtxt))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.newCSVtxt)));


        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);

        notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(CURRENT_ID, builder.build());
        CURRENT_ID++;

    }

    public void createNotification2() {
        builder.setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setContentTitle(getString(R.string.Error))
                .setContentText(getString(R.string.txtErrorInternet))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.txtErrorInternet)));
        notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(CURRENT_ID, builder.build());
        CURRENT_ID++;
    }


    public void createNotification3() {
        builder.setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setContentTitle(getString(R.string.Error))
                .setContentText(getString(R.string.txtErrorZIP))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(getString(R.string.txtErrorZIP)));
        notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(CURRENT_ID, builder.build());
        CURRENT_ID++;
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void getJSON() {
        OneTimeWorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(StarManager.class).build();
        WorkManager.getInstance().enqueue(uploadWorkRequest);
    }


    public String dateToString(int month, int day, int year){
        String monthToString, daytoString;
        if (month<10){
            monthToString = "0"+month;
        }
        else{
            monthToString = ""+month;
        }
        if (day<10){
            daytoString = "0"+day;
        }
        else{
            daytoString = ""+day;
        }
        String date = year+monthToString+daytoString;
        return date;
    }

    public String timeToString (int hour, int minute){
        String hourToString, minutetoString;
        if (hour<10){
            hourToString= "0"+hour;
        }
        else{
            hourToString = ""+hour;
        }
        if (minute<10){
            minutetoString = "0"+minute;
        }
        else{
            minutetoString = ""+minute;
        }
        String time = hourToString+":"+minutetoString;
        return time;
    }
}