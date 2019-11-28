package fr.istic.mob.starproviderssp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import static android.os.SystemClock.sleep;



public class MainActivity extends AppCompatActivity {
    String CHANNEL_ID = "1";
    int PROGRESS_MAX = 100;
    int PROGRESS_CURRENT = 0;

    NotificationManagerCompat notificationManager;

    NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        getJSON();
        createNotification();

        for (int i = 0; i < 10; i++) {
            sleep(1000);
            Log.d("Sleep", " i : " + i);
            PROGRESS_CURRENT += 10;
            builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);
            notificationManager.notify(1, builder.build());

        }
        builder.setContentText("Download complete")
                .setProgress(0, 0, false);
        notificationManager.notify(1, builder.build());


    }

    public void createNotification() {
        String textTitle = "Nouveau csv !";
        String textContent = "\nUn nouveau CSV a été ajouté sur le site de la STAR, cliquez pour le télécharger !";

        builder.setSmallIcon(R.drawable.ic_stat_onesignal_default)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(textContent));


        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false);


        notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());

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


}