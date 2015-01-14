/*
Add method to button

    public void sendCustomNotification(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://developer.android.com/reference/android/app/Notification.html"));
        CustomNotification notify = new CustomNotification(intent, this.getApplication());
        notify.builderNotification("Ticker...", R.layout.example_notification, R.layout.example_notification_expanded);
        notify.send();
    }
}

* */

package com.fd.alertplaces;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

/**
 * Created by rfrosch on 03/01/2015.
 */
public class CustomNotification {

    public CustomNotification(Intent intent, Application application) {
        this.intent = intent;
        this.application = application;
    }

    private Notification notification;
    private final Intent intent;
    private final Application application;
    private String ticker;
    private int smallIcon = R.drawable.ic_stat_notification;
    private int largeIcon = R.drawable.ic_launcher;
    private boolean autoCancel = true;
    private int icon;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public int getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
    }

    public int getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(int largeIcon) {
        this.largeIcon = largeIcon;
    }

    public boolean isAutoCancel() {
        return autoCancel;
    }

    public void setAutoCancel(boolean autoCancel) {
        this.autoCancel = autoCancel;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }


    public void builderNotification(String ticker, int layout_notification, int layout_notification_expanded) {

        this.ticker = ticker;
        this.smallIcon = R.drawable.ic_stat_custom;

        // BEGIN_INCLUDE(notificationCompat)
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this.application);
        // END_INCLUDE(notificationCompat)

        // BEGIN_INCLUDE(intent)
        //Create Intent to launch this Activity again if the example_notification is clicked.
        PendingIntent intent = PendingIntent.getActivity(this.application, 0, this.intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent);
        // END_INCLUDE(intent)

        // BEGIN_INCLUDE(ticker)
        // Sets the ticker text
        builder.setTicker(getTicker());

        // Sets the small icon for the ticker
        builder.setSmallIcon(getSmallIcon());
        // END_INCLUDE(ticker)

        // BEGIN_INCLUDE(buildNotification)
        // Cancel the example_notification when clicked
        builder.setAutoCancel(true);

        // Build the example_notification
        notification = builder.build();
        // END_INCLUDE(buildNotification)

        // BEGIN_INCLUDE(customLayout)
        // Inflate the example_notification layout as RemoteViews
        RemoteViews contentView = new RemoteViews(this.application.getPackageName(), layout_notification);

        /* Workaround: Need to set the content view here directly on the example_notification.
         * NotificationCompatBuilder contains a bug that prevents this from working on platform
         * versions HoneyComb.
         * See https://code.google.com/p/android/issues/detail?id=30495
         */
        notification.contentView = contentView;

        // Add a big content view to the example_notification if supported.
        // Support for expanded notifications was added in API level 16.
        // (The normal contentView is shown when the example_notification is collapsed, when expanded the
        // big content view set here is displayed.)
        if (Build.VERSION.SDK_INT >= 16) {
            // Inflate and set the layout for the expanded example_notification view
            RemoteViews expandedView =
                    new RemoteViews(this.application.getPackageName(), layout_notification_expanded);
            notification.bigContentView = expandedView;
        }
        // END_INCLUDE(customLayout)
    }

    public void send() {
        // START_INCLUDE(notify)
        // Use the NotificationManager to show the example_notification
        NotificationManager nm = (NotificationManager) this.application.getApplicationContext().getSystemService(this.application.getApplicationContext().NOTIFICATION_SERVICE);
        nm.notify(0, notification);
        // END_INCLUDE(notify)
    }

}
