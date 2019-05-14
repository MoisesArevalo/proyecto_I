package com.example.proyectoi;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LuzActivity extends Activity {
    TextView textLight;
    SensorManager sensorManager;
    Sensor sensor;
    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID ="NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luz);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        textLight = (TextView) findViewById(R.id.textLight);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
    }

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(lightListener, sensor,
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(lightListener);
    }

    public SensorEventListener lightListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) { }

        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];

            textLight.setText("Intensidad de luz:\n" +(int)x + " lx");

            if ((int)x < 20 ){
                setPendingIntent();
                createNotificationChannel();
                createNotification();
            }
        }
    };


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setPendingIntent(){
        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        pendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_CANCEL_CURRENT);

    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Notificacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }


    private void createNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.sol1);
        builder.setContentTitle("Intensidad de luz");
        builder.setContentText("La intensidad de luz es inferior a 20 lx.");
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.YELLOW, 1000, 1000);
        builder.setVibrate(new long[]{1000,1000,1000,1000, 1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        builder.setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }

}

