package com.github.ellivr.sakmasak.utils;

import android.content.Context;
import android.content.res.Resources;

import com.github.ellivr.sakmasak.R;

/**
 * Created by ref on 2017/08/31.
 */

public class GlobalVar {

    public final static class Const{

        public static final String BASE_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
        public static final String EXTRA_RECIPES = "com.github.ellivr.sakmasak.extra_recipes";
        public static final String EXTRA_STEPS = "com.github.ellivr.sakmasak.extra_steps";
        public static final String EXTRA_STEPS_POS = "com.github.ellivr.sakmasak.extra_steps_pos";
        public static final String EXTRA_INGREDIENTS_LIST = "com.github.ellivr.sakmasak.extra_ingredients_list";

        public static final String EXTRA_SCROLLPOS = "com.github.ellivr.sakmasak.extra_scrollpos";
        public static final String EXTRA_SELECTED_RECIPES = "com.github.ellivr.sakmasak.extra_selected_recipes";

        public static final String WIDGET_UPDATE_SERVICE = "com.github.ellivr.sakmasak.widget_update_service";

        public static final String TAG_FRAGMENT_DETAIL = "com.github.ellivr.sakmasak.tag_fragment_detail";
        public static final String TAG_FRAGMENT_STEP = "com.github.ellivr.sakmasak.tag_fragment_step";
    }

    public final static class Helper{

        public static String getFriendlyIngredient(Context context, Ingredients ingredient){
            if(ingredient == null){
                return null;
            }
            Resources resource = context.getResources();
            String output = ingredient.getQuantity() +
                    " " + getFriendlyMeasure(context, ingredient.getMeasure()) +
                    " " + resource.getString(R.string.of) +
                    " " + ingredient.getIngredient();
            return output;
        }


        public static String getFriendlyMeasure(Context context, String measure){
            Resources resource = context.getResources();
            String output = "";

            switch (measure){
                case "CUP":
                    output = resource.getString(R.string.cup);
                    break;
                case "TBLSP":
                    output = resource.getString(R.string.tblsp);
                    break;
                case "TSP":
                    output = resource.getString(R.string.tsp);
                    break;
                case "K":
                    output = resource.getString(R.string.kilo);
                    break;
                case "G":
                    output = resource.getString(R.string.gram);
                    break;
                case "OZ":
                    output = resource.getString(R.string.oz);
                    break;
                default:
                    output = resource.getString(R.string.unit);
                    break;
            }

            return output;
        }

    }
}
