package com.github.ellivr.sakmasak.network;

import com.github.ellivr.sakmasak.utils.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by ref on 2017/09/01.
 */

public interface RecipeInterface {
    @GET("baking.json")
    Call<ArrayList<Recipe>> getRecipes();
}
