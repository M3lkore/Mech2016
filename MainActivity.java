package com.industries.falcon.rcrobot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class MainActivity extends Activity implements OnGestureListener{

    //Class Reference -> we need to have a name reference to a class
    TextView textview;
    GestureDetector detector;

    //create Global variables - those that are used in several overrides
    boolean isDriving = false;
    boolean isForward = false;

    //create Global variables - used to know velocity
    float LastKnownVelocityX;
    float LastKnownVelocityY;

    float FB;
    float RL;

    //create Global variables - used to know coordinate positions
    int StartingX;
    int StartingY;
    int MovingX;
    int MovingY;
    int EndingX;
    int EndingY;

    int valueRL = 0;
    int valueFB = 0;

    //create Global variables - used to display text direction
    String LastKnownRL;
    String LastKnownFB;



//***************************
//     SUB-ROUTINES:
//***************************
    public void GetSpeed(){

    }









//***************************
//    OVER RIDES:
//***************************

    //Sets start-up values and metrics upon opening app
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get screen size
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displayMetrics);

        //store screen height/width
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        //instantiate the text view
        textview = new TextView(this);
        textview.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
        textview.setText("\nStatus: Idle");
        textview.setWidth(screenWidth);
        textview.setHeight(screenHeight);
        textview.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        //
        detector = new GestureDetector(this, this);

        setContentView(textview);
    }


    //SINGLE TAP - if moving then stop and if tap again continue, if not driving then nothing
    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return false;
    }

    //ON TOUCH - get coordinates, recognize direction, output a speed/direction on distance of swipe
    @Override
    public boolean onTouchEvent(MotionEvent event)  {

        //finding and displaying coordinates of Touch Down, Moving, Touch Up positions
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                StartingX = (int)event.getX();
                StartingY = (int)event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                MovingX = (int)event.getX();
                MovingY = (int)event.getY();
                EndingX = -1;
                EndingY = -1;

                int Value1 = 75; //interval
                int Value2 = 2 * Value1; //150
                int Value3 = 3 * Value1; //225
                int Value4 = 4 * Value1; //300
                int Value5 = 5 * Value1; //375

                //Shows Forward/Backwards (+ = forward, - = reverse)
                FB = StartingY - MovingY;

                if (FB > Value1 && FB <= Value2){
                    LastKnownFB = "Slowest Forward";
                    valueFB = 1;
                    //Log.d(DirectionFB, LastKnownFB + " (" + valueFB + ")");
                }
                if (FB > Value2 && FB <= Value3){
                    LastKnownFB = "Slow Forward";
                    valueFB = 2;
                }
                if (FB > Value3 && FB <= Value4){
                    LastKnownFB = "Medium Forward";
                    valueFB = 3;
                }
                if (FB > Value4 && FB <= Value5){
                    LastKnownFB = "Fast Forward";
                    valueFB = 4;
                }
                if (FB > Value5){
                    LastKnownFB = "Fastest Forward";
                    valueFB = 5;
                }
                if (FB < -Value1 && FB >= -Value2){
                    LastKnownFB = "Slowest Reverse";
                    valueFB = -1;
                }
                if (FB < -Value2 && FB >= -Value3){
                    LastKnownFB = "Slow Reverse";
                    valueFB = -2;
                }
                if (FB < -Value3 && FB >= -Value4){
                    LastKnownFB = "Medium Reverse";
                    valueFB = -3;
                }
                if (FB < -Value4 && FB >= -Value5){
                    LastKnownFB = "Fast Reverse";
                    valueFB = -4;
                }
                if (FB < -Value5){
                    LastKnownFB = "Fastest Reverse";
                    valueFB = -5;
                }
                if (FB >= -Value1 && FB <= Value1){
                    LastKnownFB = "No Forward/Reverse";
                    valueFB = 0;
                }



                //Controls Right/Left (+ = right, - = left)
                RL = MovingX - StartingX;
                if (RL > Value1 && RL <= Value2){
                    LastKnownRL = "Slowest Right";
                    valueRL = 1;
                    //Log.d(DirectionRL, LastKnownRL + " (" + valueRL + ")");
                }
                if (RL > Value2 && RL <= Value3){
                    LastKnownRL = "Slow Right";
                    valueRL = 2;
                }
                if (RL > Value3 && RL <= Value4){
                    LastKnownRL = "Medium Right";
                    valueRL = 3;
                }
                if (RL > Value4 && RL <= Value5){
                    LastKnownRL = "Fast Right";
                    valueRL = 4;
                }
                if (RL > Value5){
                    LastKnownRL = "Fastest Right";
                    valueRL = 5;
                }
                if (RL < -Value1 && RL >= -Value2){
                    LastKnownRL = "Slowest Left";
                    valueRL = -1;
                }
                if (RL < -Value2 && RL >= -Value3){
                    LastKnownRL = "Slow Left";
                    valueRL = -2;
                }
                if (RL < -Value3 && RL >= -Value4){
                    LastKnownRL = "Medium Left";
                    valueRL = -3;
                }
                if (RL < -Value4 && RL >= -Value5){
                    LastKnownRL = "Fast Left";
                    valueRL = -4;
                }
                if (RL < -Value5){
                    LastKnownRL = "Fastest Left";
                    valueRL = -5;
                }
                if (RL >= -Value1 && RL <=Value1){
                    LastKnownRL = "No Right/Left";
                    valueRL = 0;
                }

                //display text in Logcat
                String DirectionFB = " Direction in FB";
                String DirectionRL = " Direction in RL";

                Log.d(DirectionFB, " ("+FB+")");
                Log.d(DirectionFB, LastKnownFB + " (" + valueFB + ")");
                Log.d(DirectionRL, LastKnownRL + " (" + valueRL + ")");


                textview.setText("\nStatus:\n" + LastKnownFB + "\n" + LastKnownRL);

                break;

            case MotionEvent.ACTION_UP:
                EndingX = (int)event.getX();
                EndingY = (int)event.getY();

                //coordinates seen in debug Logcat view ".../Coordinates: Down = #,# \nMove = #,# ....
                String Coordinates = "Coordinates";
                String TouchDown = "DOWN = " + StartingX + "," + StartingY;
                String TouchMove = "MOVE = " + MovingX + "," + MovingY;
                String TouchUp = "UP = " + EndingX + "," + EndingY;

                Log.d(Coordinates, TouchDown);
                Log.d(Coordinates, TouchMove);
                Log.d(Coordinates, TouchUp);
                break;
        }




        return detector.onTouchEvent(event);
    }




    @Override
    public void onLongPress(MotionEvent e) {
    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float CurrentVelocityX, float CurrentVelocityY) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }
}

