package com.github.ellivr.sakmasak.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.github.ellivr.sakmasak.R;
import com.github.ellivr.sakmasak.RecipeDetailActivity;

import java.util.ArrayList;

import static com.github.ellivr.sakmasak.utils.GlobalVar.Const.EXTRA_INGREDIENTS_LIST;

/**
 * Implementation of App Widget functionality.
 */
public class MyWidgetProvider extends AppWidgetProvider {

    static ArrayList<String> ingredients = new ArrayList<>();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

    }

    @Override
    public void onEnabled(Context context) {

    }

    @Override
    public void onDisabled(Context context) {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, MyWidgetProvider.class));

        final String action = intent.getAction();
        if (action.equals("com.github.ellivr.sakmasak.APPWIDGET_UPDATE")) {
            try{
                ingredients = intent.getExtras().getStringArrayList(EXTRA_INGREDIENTS_LIST);
                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_gridview);

                MyWidgetProvider.updateBakingWidgets(context, appWidgetManager, appWidgetIds);
                super.onReceive(context, intent);
            }
            catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    public static void updateBakingWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            Intent  appIntent = new Intent(context, RecipeDetailActivity.class);
                    appIntent.addCategory(Intent.ACTION_MAIN);
                    appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    appIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT|Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_gridview, appPendingIntent);

            Intent intent = new Intent(context, MyRemoteViewsService.class);
            views.setRemoteAdapter(R.id.widget_gridview, intent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

}
