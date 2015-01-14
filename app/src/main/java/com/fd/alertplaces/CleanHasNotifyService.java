package com.fd.alertplaces;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by frog on 07/01/15.
 */
public class CleanHasNotifyService extends Service {


    private DataPlace data;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    /** Called when the service is being created. */
    @Override
    public void onCreate() {
        new Thread(){
            public void run(){
                data = new DataPlace(getApplicationContext());
                ArrayList<String> listPlaces = data.getPlacesHasNotify();
                ArrayList<String> listMillis = new ArrayList<String>();
                for (String name : listPlaces){
                    listMillis.add(data.getMilliseconds(name));
                }
                while (true){
                    if (listPlaces.size() > 0) {
                        for (int i = 0; i < listPlaces.size(); i++) {
                            if(!listMillis.get(i).equals("")) {
                                if (isPlusTime(Long.parseLong(listMillis.get(i)))) {
                                    Log.d("Check isPlusTime " + listPlaces.get(i), "True");
                                    data.setNoNotify(listPlaces.get(i));
                                    Log.d("Set to NO notify in db ", listPlaces.get(i));
                                }
                            }
                        }
                    }
                    data = null;

                    SystemClock.sleep(500);
                }

            }
        }.start();

    }
    private boolean isPlusTime(Long time) {
        long calc = System.currentTimeMillis() - time;
        //to seconds
        calc = calc / 1000;


        //2 horas in minutes = 120
        return (Long.parseLong("7200") > calc) ? false : true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
