package com.example.proyectoi;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

public class DistanciaActivity extends AppCompatActivity {
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private SensorEventListener proximitySensorListener;

    private static final int IMAGE_CAPTURE_CODE = 1001;
    ImageView miImagen;
    private static final int PERMISO_CODE = 1000;

    Uri image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distancia);


        miImagen = (ImageView) findViewById(R.id.imageView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximitySensor == null) {
            Toast.makeText(this, "Proximity sensor is not available !", Toast.LENGTH_LONG).show();
            finish();
        }
        proximitySensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                if (sensorEvent.values[0] < proximitySensor.getMaximumRange()) {
                    getWindow().getDecorView().setBackgroundColor(Color.RED);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (checkCallingOrSelfPermission(Manifest.permission.CAMERA) ==
                                PackageManager.PERMISSION_DENIED ||
                                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                        PackageManager.PERMISSION_DENIED) {
                            String[] permisos = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permisos, PERMISO_CODE);
                        } else {
                            //permiso concedidos para la aplicacion
                            System.out.println("Permisos concedidos");
                            openCamera();
                        }
                    } else {

                    }

                } else {
                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {
            }
        };
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(proximitySensorListener, proximitySensor,
                60 * 1000 * 1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(proximitySensorListener);
    }


    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Nueva Imagen");
        values.put(MediaStore.Images.Media.DESCRIPTION, "De camera");
        image = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);


        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image);
        cameraIntent.putExtra(MediaStore.EXTRA_FINISH_ON_COMPLETION,image);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            miImagen.setImageURI(image);
            enviar("patricfajardonow96@gmail.com",image,"mensaje prueba", " este es un mensaje de prueba");
            Toast.makeText(getApplicationContext(), "Enviando mensaje ...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISO_CODE: {
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    Toast.makeText(this, "Permiso denegado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }


    private void enviar(String destino, Uri imagen, String asunto, String mensaje) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        String[] to = {destino};
        //  String[] cc = {destino};

        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        //emailIntent.putExtra(Intent.EXTRA_CC, cc);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, asunto);
        emailIntent.setType("image/*");
        emailIntent.putExtra(Intent.EXTRA_TEXT, mensaje);
        emailIntent.putExtra(Intent.EXTRA_STREAM,imagen);
        try {
            startActivity(Intent.createChooser(emailIntent, "Enviando mensaje..."));
            finish();
            Log.e("Test email:", "Fin envio email");

        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Ocurrio un error a enviar correo.", Toast.LENGTH_SHORT).show();
        }

    }


}
