package com.github.ellivr.sakmasak.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.github.ellivr.sakmasak.R;

import java.util.List;


/**
 * Created by ref on 2017/09/02.
 */

public class MyRemoteViewsService extends RemoteViewsService {
    List<String> ingredients;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new MyRemoteViewsFactory(this.getApplicationContext());
    }

    class MyRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            ingredients = MyWidgetProvider.ingredients;
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return ingredients.size();
        }

        Context mContext = null;
        MyRemoteViewsFactory(Context context) {
            mContext = context;

        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_widget);

            views.setTextViewText(R.id.item_ingredient, ingredients.get(i));

            Intent fillInIntent = new Intent();

            views.setOnClickFillInIntent(R.id.item_ingredient, fillInIntent);

            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
