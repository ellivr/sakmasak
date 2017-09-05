package com.github.ellivr.sakmasak.network;

import com.github.ellivr.sakmasak.utils.GlobalVar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ref on 2017/09/01.
 */

public class RecipeClient {
    static RecipeInterface recipeInterface;

    public static RecipeInterface retrieve() {

        Gson gson = new GsonBuilder().create();
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        recipeInterface = new Retrofit
                .Builder()
                .baseUrl(GlobalVar.Const.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .callFactory(httpClientBuilder.build())
                .build().create(RecipeInterface.class);

        return recipeInterface;
    }
}
