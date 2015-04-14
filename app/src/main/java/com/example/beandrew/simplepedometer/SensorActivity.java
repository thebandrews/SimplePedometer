/* Copyright (C) 4/12/2015 Ben Andrews ben.andrews@gmail.com
 * All Rights Reserved.
 */

package com.example.beandrew.simplepedometer;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.*;

import java.util.Arrays;


public class SensorActivity extends ActionBarActivity implements SensorEventListener {

    //
    // Member variables
    //
    private final String TAG="SensorApp";
    final int GRAPH_BUFFER_SIZE=1000;

    private SensorManager sensorManager;
    private MedianFIlter medianFilter;
    private StepCounter stepCounter;

    Button button1;
    Button button2;
    ProgressBar progressBar;
    TextView stepView;
    XYPlot plot;
    SimpleXYSeries /* series1, series2, */ series3, series4;

    //
    // Member functions
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        //
        // Setup UI elements
        //
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        stepView = (TextView) findViewById(R.id.stepView);
        plot = (XYPlot) findViewById(R.id.sensorXYPlot);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        //
        // Setup sensor manager and filter
        //
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        medianFilter = new MedianFIlter(25);
        stepCounter = new StepCounter(200, (float)-0.9);

        Number[] series1Numbers = {};

        // Turn the above arrays into XYSeries':
//        series1 = new SimpleXYSeries(
//                Arrays.asList(series1Numbers),           // SimpleXYSeries takes a List so turn our array into a List
//                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,  // Y_VALS_ONLY means use the element index as the x value
//                "Sensor 1");                             // Set the display title of the series
//
//        series2 = new SimpleXYSeries(
//                Arrays.asList(series1Numbers),           // SimpleXYSeries takes a List so turn our array into a List
//                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,  // Y_VALS_ONLY means use the element index as the x value
//                "Sensor 2");                             // Set the display title of the series

        series3 = new SimpleXYSeries(
                Arrays.asList(series1Numbers),           // SimpleXYSeries takes a List so turn our array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,  // Y_VALS_ONLY means use the element index as the x value
                "Sensor 3");                             // Set the display title of the series

        series4 = new SimpleXYSeries(
                Arrays.asList(series1Numbers),              // SimpleXYSeries takes a List so turn our array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,     // Y_VALS_ONLY means use the element index as the x value
                "Median Filter");                           // Set the display title of the series

        // Create a formatter to use for drawing a series using LineAndPointRenderer
        // and configure it from xml:
//        LineAndPointFormatter series1Format = new LineAndPointFormatter(Color.RED, null, null, null);
//        LineAndPointFormatter series2Format = new LineAndPointFormatter(Color.GREEN, null, null, null);
        LineAndPointFormatter series3Format = new LineAndPointFormatter(Color.BLUE, null, null, null);
        LineAndPointFormatter series4Format = new LineAndPointFormatter(Color.CYAN, null, null, null);

        // add a new series' to the xyplot:
//        plot.addSeries(series1, series1Format);
//        plot.addSeries(series2, series2Format);
        plot.addSeries(series3, series3Format);
        plot.addSeries(series4, series4Format);
    }

    @Override
    protected void onPause() {
        // unregister listener
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d(TAG,"Resumed!");
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sensor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;

        // Save the values from the three axes into their corresponding variables
//        float x = values[0];
//        float y = values[1];
        float z = values[2];

        if(series3.size() > GRAPH_BUFFER_SIZE) {
//            series1.removeFirst();
//            series2.removeFirst();
            series3.removeFirst();
            series4.removeFirst();
        }

//        series1.addLast(null,x);
//        series2.addLast(null,y);
        series3.addLast(null,z);
        series4.addLast(null, stepCounter.InputData(medianFilter.ApplyFilter(z)));

        String stepCountString = Integer.toString(stepCounter.Steps);
        stepView.setText(stepCountString);
        progressBar.setProgress(stepCounter.Steps % 100);
        plot.redraw();
    }

    public void handleButton1Click(View v)
    {
        Log.d(TAG,"Handle button 1 click");
        stepCounter.ResetSteps();
    }

    public void handleButton2Click(View v)
    {
        Log.d(TAG,"Handle button 2 click");

        if(plot.getVisibility() == View.VISIBLE){
            plot.setVisibility(View.INVISIBLE);
            button2.setText("Show Chart");
        } else {
            plot.setVisibility(View.VISIBLE);
            button2.setText("Hide Chart");
        }

    }
}
