package com.fd.alertplaces;

import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;



/**
 * Created by frog on 05/01/15.
 */
public class GPSService extends Service {

    private GPSTracker gpsTracker;
    private double latitudeNow = -1;
    private double longitudeNow = -1;
    private Place placeToSending;
    private static final int DELAY = 15 * 1000;
    private CustomNotification notify;
    private Intent intentNotify;
    private String uri;
    private double radius;
    private ArrayList<Place> places;
    private boolean waitSending = false;
    private Settings settings;
    private ArrayList<String> hasNotifyNow;
    private DataPlace data;

    public GPSService() {
    }

    /** Called when the service is being created. */
    @Override
    public void onCreate() {

        gpsTracker = new GPSTracker(getApplicationContext(), "", "");
        places = new ArrayList<Place>();


        loadPlaces();

        //get longitudeNow and latitudeNow
        final Thread threadGetLatLong = new Thread() {
            public void run() {
                Log.d("Thread threadGetLatLong", "Stared");
                getLocationNow();
            }
        };
        threadGetLatLong.start();

        //check place to sending
        final Thread threadCheckPlaceToSending = new Thread() {
            public void run() {
                Log.d("Thread threadCheckPlaceToSending", "Started");
                checkPlaceToSending();
            }
        };
        threadCheckPlaceToSending.start();

        //checking if places in radius and send notify
        final Thread threadPlaceInRadius = new Thread() {
            public void run() {
                Log.d("Thread threadPlaceInRadius", "Started");
                checkPlaceInRadius();
            }
        };
        threadPlaceInRadius.start();



    }



    private void loadHasNotifyNow() {
        data = new DataPlace(getApplicationContext());
        hasNotifyNow = data.getPlacesHasNotify();
        Log.d("Refresh in hasNotifyNow from db", "");
        data.close();
        data = null;
    }


    private void getLocationNow() {
        long counter = 0;
        while (counter <= 100) {
            latitudeNow = gpsTracker.getLatitude();
            longitudeNow = gpsTracker.getLongitude();
            Log.d("Lat and Lng", Double.toString(latitudeNow) + Double.toString(longitudeNow));
            counter++;
            if (counter == 100)
                counter = 0;
            SystemClock.sleep(DELAY);
        }
    }

    private void checkPlaceInRadius() {

        long counter = 0;
        while (counter <= Long.MAX_VALUE) {
            if (!waitSending){

                //sync with checkPlaceToSending()
                if (placeToSending != null) {
                    data = new DataPlace(getApplicationContext());
                    if (!data.placeHasNotify(placeToSending.getName())) {
                        String millisecconds = String.valueOf(System.currentTimeMillis());
                        data.setHasNotify(placeToSending.getName(), millisecconds);
                        Log.d("Set has notify in db","Place " + placeToSending.getName() + " in " + millisecconds);
                        sendNotify();
                        Log.d("Nofiy sending","True");
                        waitSending = false;
                        placeToSending = null;
                        Log.d("Clean waitSending to false and placeToSending to null", " necessary to conditions in others thread");
                    }
                    data.close();
                    data = null;
                }
                counter++;
                if (counter == Long.MAX_VALUE)
                    counter = 0;
            }
            SystemClock.sleep(1000);
        }
    }

    private String getTimeNotify() {
        return Long.toString(System.currentTimeMillis());
    }


    private void sendNotify() {

        //stop checkPlaceToSending()
        waitSending = true;
        Log.d("Pause others threads","waitSending = true;");
/*
uri = "geo:" + Double.toString(latitudeNow) + "," +
                Double.toString(longitudeNow) + "?q=" + Double.toString(placeToSending.getLatitude()) + "," +
                Double.toString(placeToSending.getLongitude()) + "(" + placeToSending.getName()+")";
 */
        //sending notify
        uri = "geo:0,0?q=" + Double.toString(placeToSending.getLatitude()) + "," +
                Double.toString(placeToSending.getLongitude()) + "(" + placeToSending.getName()+")";
        intentNotify = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        notify = new CustomNotification(intentNotify, getApplication());
        notify.builderNotification(getString(R.string.sticker_notification), R.layout.notification, R.layout.notification_expanded);
        notify.send();
    }

    //check filter place configuring for user, and return too placeToSending
    private void checkPlaceToSending() {
        long counter = 0;
        while (!waitSending){
            for (int i = 0; i < places.size(); i++){
                data = new DataPlace(getApplicationContext());
                if (!data.placeHasNotify(places.get(i).getName())) {
                    //check in radius
                    if (gpsTracker.inRadius(latitudeNow, longitudeNow, places.get(i).getLatitude(), places.get(i).getLongitude(), 5000)) {
                        Log.d("Place " + places.get(i).getName() + " in radius", "True");
                        placeToSending = places.get(i);
                        Log.d("Set " + places.get(i).getName() + " to placeToSending", "True");
                        break;
                    }
                }
                data = null;
            }
            counter++;
            if (counter == Long.MAX_VALUE)
                counter = 0;
            SystemClock.sleep(500);
        }
    }

    //load places from settings
    private void loadPlaces() {
        DataPlace data = new DataPlace(getApplicationContext());
        places = data.getAllPlaces();
        data.close();
        data = null;
    }



    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
