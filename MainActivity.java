package com.industries.falcon.rcrobot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


/*NOTES:
ctrl+shift+R-> replace all
ctrl+/ -> comment lines with //
ctrl+shift+/ -> comments lines with sectional comments  / * ... * /

\n      -> new line
||      -> logical OR
&&      -> logical AND
==      -> match, comparison of the values
Top screen  -> coordinates Y =0
Left screen -> coordinates X =0


Toasts  -> displays a msg at the bottom of screen, short/long is for the amount of time it appears for

Textview = class, textview = references the class
GesturDetector = class, detector = references the class
instantiating = you have minecraft on your computer but you haven't opened it yet to interact with it yet
object is the program, the only way for me to interact with the program is to open an instance of it


textview.setText() -> Message at top of the screen
*/


//right-click the MainActivity and generate, implement, and it will generate the overrides
//Must have all the overrides it requires else it won't work
public class MainActivity extends Activity implements OnGestureListener, ScaleGestureDetector.OnScaleGestureListener{

    //Class Reference -> we need to have a name reference to a class
    TextView textview;
    GestureDetector detector;

    //create Global variables - those that are used in several overrides
    boolean isDriving = false;
    boolean isForward = false;

    float LastKnownVelocityX;
    float LastKnownVelocityY;

    //Subroutine - Find direction
    public void FindDirection()
    {
        //Negatives = upwards/left, Positives = Downward/right
        //Sets up wordage //Negatives = upwards/left, Positives = Downward/right
        if (LastKnownVelocityY > 0 && LastKnownVelocityX > 0)
        {
            textview.setText("\nStatus: Reverse and Right");
        }
        else if (LastKnownVelocityY > 0 && LastKnownVelocityX < 0)
        {
            textview.setText("\nStatus: Reverse and Left");
        }
        else if (LastKnownVelocityY < 0 && LastKnownVelocityX > 0)
        {
            textview.setText("\nStatus: Forward and Right");
        }
        else if (LastKnownVelocityY < 0 && LastKnownVelocityX < 0)
        {
            textview.setText("\nStatus: Forward and Left");
        }
        else
        {
            textview.setText("\nStatus: Driving unknown direction!!!!");
        }
    }




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



    //Message at top of the screen = textview.setText()
    //Message at the bottom of the screen = Toast.makeText().show()
    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        //toggles our state
        if (LastKnownVelocityX > 0 || LastKnownVelocityY > 0)
        {
            isDriving = !isDriving;
        }

        //Subroutine: Changes text at top of screen with proper direction
        FindDirection();

        //When car is moving
        if (isDriving && (LastKnownVelocityX > 0 || LastKnownVelocityY > 0))
        {
            //textview.setText("\nStatus: Driving in last known direction");
            Toast.makeText(getApplicationContext(), "Start driving forwards\n\nLast known velocity X= " + LastKnownVelocityX + "\n" +
                    "Last known velocity Y= "+ LastKnownVelocityY, Toast.LENGTH_SHORT).show();
        }
        //when car is idle
        else if (!isDriving)
        {
            textview.setText("\nStatus: Idle");
        }

        //create float variables and set them to get the x/y coordinates
        float touchX = event.getX();
        float touchY = event.getY();

        //prints current touch coordinates
        Toast.makeText(getApplicationContext(), "Touch x: " + touchX + "\nTouch y: " + touchY, Toast.LENGTH_SHORT).show();

        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)  {

        //controls the reverse and switching to driving forwards
        if (!isForward && event.getAction() == android.view.MotionEvent.ACTION_UP)
        {
            //if not forward, then make it forward when we touch screen
            isForward = true;

            if (isDriving)
            {
                textview.setText("Status: Driving forwards");
                Toast.makeText(getApplicationContext(), "Stop driving backwards, starting driving forwards", Toast.LENGTH_SHORT).show();
            }
        }


        //returns -> stops the override, the brackets are part of the structure to separate areas
        return detector.onTouchEvent(event);
    }


    @Override
    public void onLongPress(MotionEvent e) {
        isForward = false;

        if (isDriving)
        {
            textview.setText("RC Robot\nStatus: Driving backwards");
            Toast.makeText(getApplicationContext(), "Start driving backwards", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Not currently driving", Toast.LENGTH_SHORT).show();
        }
    }


    //how sensitive you want it to react
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float CurrentVelocityX, float CurrentVelocityY) {

        //sets sensitivity (note: the F sets it to a float)
        boolean swipedLeft, swipedRight, swipedUp, swipedDown, hasSwiped;

        //Negatives = upwards/left, Positives = Downward/right
        //Sets threshold for velocity swipe must be at to be registered
        swipedLeft = (CurrentVelocityX < -1500F);
        swipedRight = (CurrentVelocityX > 1500F);
        swipedUp = (CurrentVelocityY < -1500F);
        swipedDown = (CurrentVelocityY > 1500F);

        //define variable to acknowledge that any swipe has occurred
        hasSwiped = swipedLeft || swipedRight || swipedUp || swipedDown;

        //define new string variable
        String addText ="";

        // += is the add itself operator, will display "Swiped Left Swiped Up)
        if (swipedUp)
            addText += "Swiped Up ";
        if (swipedDown)
            addText += "Swiped Down ";
        if (swipedLeft)
            addText += "Swiped Left ";
        if (swipedRight)
            addText += "Swiped Right ";
        if (hasSwiped)
            Toast.makeText(getApplicationContext(), addText, Toast.LENGTH_SHORT).show();



        //see the values
        Toast.makeText(getApplicationContext(), "Swipe Velocity x =" + CurrentVelocityX + "\nSwipe  Velocity y =" + CurrentVelocityY, Toast.LENGTH_SHORT).show();

        //store the swipe velocity for future use
        LastKnownVelocityX = CurrentVelocityX;
        LastKnownVelocityY = CurrentVelocityY;

        //Subroutine: Changes text at top of screen with proper direction
        FindDirection();



        return true;
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }




    //NOT USED
    @Override
    public boolean onDown(MotionEvent e) {
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
