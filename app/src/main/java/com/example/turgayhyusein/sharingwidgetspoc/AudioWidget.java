package com.example.turgayhyusein.sharingwidgetspoc;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class AudioWidget extends AppWidgetProvider {

    public static final String TAG = AudioWidget.class.getSimpleName();

    private static final int INTENT_FLAGS = PendingIntent.FLAG_UPDATE_CURRENT;
    private static final int REQUEST_CODE = 0;

    static void updateAppWidget(final Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        // Create an Intent to launch ExampleActivity
        //Intent intent = new Intent(context, RecordingAudioActivity.class);
        //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,	intent, 0);

        Intent startIntent = new Intent(context, RecordingAudioService.class);
        startIntent.setAction("START");
        Intent stopIntent = new Intent(context, RecordingAudioService.class);
        stopIntent.setAction("STOP");

        PendingIntent startPendingIntent = PendingIntent.getService(context, REQUEST_CODE, startIntent, INTENT_FLAGS);
        PendingIntent stopPendingIntent = PendingIntent.getService(context, REQUEST_CODE, stopIntent, INTENT_FLAGS);

        // Get the layout for the App Widget and attach an on-click listener to the button
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.audio_widget);
        views.setOnClickPendingIntent(R.id.button_start_widget, startPendingIntent);
        views.setOnClickPendingIntent(R.id.button_stop_widget, stopPendingIntent);

        // Tell the AppWidgetManager to perform an update on the current app widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}