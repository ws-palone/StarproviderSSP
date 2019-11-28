package fr.istic.mob.starproviderssp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //WorkManager
        getJSON();
        Button pickDateHour = findViewById(R.id.pick_date_hour);
        pickDateHour.setOnClickListener(new View.OnClickListener() {
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
    }

    public void getJSON(){
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
