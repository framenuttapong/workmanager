package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    WorkManager workManager;
    WorkRequest workRequest;
    public static final String CHANNEL_ID = "asdf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotificationChannel();
        workManager = WorkManager.getInstance(getApplicationContext());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                //.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setSmallIcon(android.R.drawable.arrow_up_float)
                .setContentTitle("My notification")
                .setContentText("15 MINUTES")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("15 MINUTES"))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());
    }

    public void start(View view){
//        OneTimeWorkRequest worker1 = new OneTimeWorkRequest.Builder(MyWorker.class).build();
//        OneTimeWorkRequest worker2 = new OneTimeWorkRequest.Builder(MyWorker2.class).build();
//        OneTimeWorkRequest worker3 = new OneTimeWorkRequest.Builder(MyWorker3.class).build();

//        workManager.beginWith(Arrays.asList(worker1,worker2))
//                .then(worker3)
//                .enqueue();
//        workRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
//                .setConstraints(new Constraints.Builder().setRequiresBatteryNotLow(true).build())
//                .build();
        workRequest = new PeriodicWorkRequest.Builder(MyWorker.class, 1, TimeUnit.MINUTES).build();
        workManager.enqueue(workRequest);

        workManager.getWorkInfoByIdLiveData(workRequest.getId())
                .observe(this, new Observer<WorkInfo>() {
                    @Override
                    public void onChanged(WorkInfo workInfo) {
                        if(workInfo.getState() == WorkInfo.State.SUCCEEDED) {
                            // รับ result
                            int n = workInfo.getOutputData().getInt("ret", 0);
                            TextView textView = findViewById(R.id.textView);
                            textView.setText("" + n);
                            createNotificationChannel();
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                    //.setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setSmallIcon(android.R.drawable.arrow_up_float)
                                    .setContentTitle("My notification")
                                    .setContentText("15 MINUTES")
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText("15 MINUTES"))
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                            // notificationId is a unique int for each notification that you must define
                            notificationManager.notify(1, builder.build());
                        } else {
                            // รับ progress
                            int n = workInfo.getProgress().getInt("num", 0);
                            TextView textView = findViewById(R.id.textView);
                            textView.setText("" + n);
                            createNotificationChannel();
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                                    //.setSmallIcon(R.drawable.ic_launcher_foreground)
                                    .setSmallIcon(android.R.drawable.arrow_up_float)
                                    .setContentTitle("My notification")
                                    .setContentText("15 MINUTES")
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText("15 MINUTES"))
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                            // notificationId is a unique int for each notification that you must define
                            notificationManager.notify(1, builder.build());
                        }
                    }
                });
    }

    public void stop(View view){
        workManager.cancelAllWork();
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
}