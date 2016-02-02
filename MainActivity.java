package com.industries.falcon.rcrobot;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


/*NOTES:
\n      -> new line
||      -> logical OR
&&      -> logical AND
==      -> match, comparison of the values
Top screen  -> coordinates Y =0
Left screen -> coordinates X =0


Toasts  -> displays a msg at the bottom of screen, short/long is for the amount of time it appears for


textview.setText() -> Message at top of the screen
*/


//right-click the MainActivity and generate, implement, and it will generate the overrides
//Must have all the overrides it requires else it won't work
public class MainActivity extends Activity implements OnGestureListener{

    //Textview = class, textview = references the class
    //GesturDetector = class, detector = references the class
    //instantiating = you have minecraft on your computer but you haven't opened it yet to interact with it yet
    //object is the program, the only way for me to interact with the program is to open an instance of it
    TextView textview;
    GestureDetector detector;

    //create booleans for start up
    boolean isDriving = false;
    boolean isForward = false;


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
        isDriving = !isDriving;

        if (isDriving)
        {
            textview.setText("\nStatus: Driving");
            Toast.makeText(getApplicationContext(), "Start driving forwards", Toast.LENGTH_SHORT).show();
        }
        else if (!isDriving)
        {
            textview.setText("\nStatus: Idle");
            Toast.makeText(getApplicationContext(), "Stop driving forwards", Toast.LENGTH_SHORT).show();
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
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        //sets sensitivity (note: the F sets it to a float)
        boolean swipedLeft, swipedRight, swipedUp, swipedDown, hasSwiped;
        swipedLeft = (velocityX < -2000F);
        swipedRight = (velocityX > 2000F);
        swipedUp = (velocityY < -800F);
        swipedDown = (velocityY > 800F);

        hasSwiped = swipedLeft || swipedRight || swipedUp || swipedDown;

        //define new string variable
        String addText ="";

        // += is the add itself operator, will display "Swiped Left Swiped Up)
        if (swipedLeft)
            addText += "Swiped Left ";
        if (swipedRight)
            addText += "Swiped Right ";
        if (swipedUp)
            addText += "Swiped Up ";
        if (swipedDown)
            addText += "Swiped Down ";
        if (hasSwiped)
            Toast.makeText(getApplicationContext(), addText, Toast.LENGTH_SHORT).show();

        //see the values
        Toast.makeText(getApplicationContext(), "Swipe Velocity x =" + velocityX + "\nSwipe  Velocity y =" + velocityY, Toast.LENGTH_SHORT).show();

        return true;
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
