package com.example.proyectoi;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.proyectoi.clase.Nivel;

public class NivelActivity extends AppCompatActivity {
    private Nivel nivel=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nivel= new Nivel(this);
        setContentView(nivel);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
    }
}
