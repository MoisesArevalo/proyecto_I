package com.example.proyectoi.clase;

import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

public class Nivel extends View implements SensorEventListener {
    private Paint pincel = new Paint();
    private int x0,y0;
    private float ejeX=0,ejeY=0;

    private Context c;
    public Nivel(Context context) {
        super(context);
        c=context;
        System.out.println("Entra en nivel");
        SensorManager smAdministrador=(SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        Sensor snRotacion =smAdministrador.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        smAdministrador.registerListener(this,snRotacion,SensorManager.SENSOR_DELAY_FASTEST);
        Display pantalla = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        x0=pantalla.getWidth()/2;
        y0=pantalla.getHeight()/2;


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        ejeX=event.values[0];
        ejeY=event.values[1];
        invalidate();


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public float aFloat4Decimal(float valor){
        String v=String.format("%.3f", valor);
        v=v.replace(",",".");
        return Float.parseFloat(v);
    }
    @Override
    public void onDraw(Canvas canvas){


        float xi=x0+(aFloat4Decimal(ejeX)*100);
        float yi=y0+(aFloat4Decimal(ejeY)*100);
        pincel.setColor(Color.BLUE);

        canvas.drawCircle(xi,yi,180 ,pincel);
        //canvas.drawLine(ejeX,ejeY,ejeX+10,ejeY+10,pincel);
        pincel.setColor(Color.WHITE);
        pincel.setTextSize(50);
        int angulo=(int)Math.toDegrees( Math.atan(((aFloat4Decimal(ejeY)*100))/((aFloat4Decimal(ejeX)*100))));
        System.out.println("VALORES: "+xi+" : "+yi);
        System.out.println("VALORES EN CERO "+ x0+ " : "+y0);
        boolean estado=false;
        if((xi<=x0+20 && x0-20<=xi) && (yi<=y0+20 && y0-20<=yi) ){
            System.out.println("------------------------------>>CAMBIA EL VALOR <<--------------------------------------------");
            estado=true;

            /// Toast.makeText(this,"SA",Toast.LENGTH_LONG).show();

        }else if((xi<=x0+20 && x0-20<=xi) && (yi<=200 && 50<=yi)){
            estado=true;
        }else if((xi<=x0+20 && x0-20<=xi) && (yi<=2*y0+50 && 2*y0-5<=yi)){
            estado=true;
        }
        if(estado){
            pincel.setColor(Color.rgb(25,111,61));
            canvas.drawCircle(xi,yi,180 ,pincel);
            pincel.setColor(Color.WHITE);
            canvas.drawText("Exacto " + ("\ud83d\udc4d")+("\ud83d\udc4f"),x0-100,y0,pincel);

            estado=false;
        }else{
            canvas.drawText("Casi.."+("\ud83d\udd04"),x0-100,y0,pincel);
        }
        pincel.setColor(Color.GREEN);
        canvas.drawLine(0,y0,2*x0,y0,pincel);
        canvas.drawLine(x0,0,x0,2*y0,pincel);



    }


}