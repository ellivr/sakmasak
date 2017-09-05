package com.github.ellivr.sakmasak.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import static com.github.ellivr.sakmasak.utils.GlobalVar.Const.EXTRA_INGREDIENTS_LIST;

/**
 * Created by ref on 2017/09/02.
 */

public class UpdateWidgetService extends IntentService {

    public UpdateWidgetService() {
        super("UpdateWidgetService");
    }

    public static void startWidgetService(Context context, ArrayList<String> ingredients) {
        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.putExtra(EXTRA_INGREDIENTS_LIST,ingredients);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                ArrayList<String> ingredients = intent.getExtras().getStringArrayList(EXTRA_INGREDIENTS_LIST);

                Intent newIntent = new Intent("com.github.ellivr.sakmasak.APPWIDGET_UPDATE");
                newIntent.setAction("com.github.ellivr.sakmasak.APPWIDGET_UPDATE");
                newIntent.putExtra(EXTRA_INGREDIENTS_LIST, ingredients);
                sendBroadcast(newIntent);
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }
}