package com.github.ellivr.sakmasak;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.github.ellivr.sakmasak.adapters.RecipeListAdapter;
import com.github.ellivr.sakmasak.network.RecipeClient;
import com.github.ellivr.sakmasak.network.RecipeInterface;
import com.github.ellivr.sakmasak.utils.GlobalVar;
import com.github.ellivr.sakmasak.utils.Recipe;
import com.github.ellivr.sakmasak.utils.Steps;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecipeListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecipeListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeListFragment extends Fragment implements RecipeListAdapter.Callbacks {

    RecipeListAdapter mRecipeListAdapter;

    @BindView(R.id.recipe_grid)
    RecyclerView recipeGridView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, rootView);

        mRecipeListAdapter = new RecipeListAdapter(new ArrayList<Recipe>(), this, getContext());

        if(savedInstanceState == null){
            fetchRecipes();
        }
        else{
            List<Recipe> recipes = savedInstanceState.getParcelableArrayList(GlobalVar.Const.EXTRA_RECIPES);
            mRecipeListAdapter.add(recipes);
            syncRecipeGridView();
        }


        return rootView;
    }


    //Save needed instances
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        try{
            if(mRecipeListAdapter.getItemCount() > 0){
                outState.putParcelableArrayList(GlobalVar.Const.EXTRA_RECIPES, mRecipeListAdapter.getRecipes());
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    void fetchRecipes(){
        RecipeInterface recipeInterface = RecipeClient.retrieve();
        Call<ArrayList<Recipe>> recipes = recipeInterface.getRecipes();

        recipes.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                for(int a=0; a<response.body().size(); a++){
                    mRecipeListAdapter.add(response.body());
                    syncRecipeGridView();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Log.v("http fail: ", t.getMessage());
            }
        });
    }

    void syncRecipeGridView(){
        recipeGridView.setAdapter(mRecipeListAdapter);
        int numberOfColumns = 1;
        recipeGridView.setLayoutManager(new GridLayoutManager(getContext(), numberOfColumns));
        recipeGridView.setAdapter(mRecipeListAdapter);
    }

    @Override
    public void toRecipeDetail(Recipe recipe, int position) {

        Intent intent = new Intent(getContext(), RecipeDetailActivity.class)
                .putParcelableArrayListExtra(GlobalVar.Const.EXTRA_STEPS, new ArrayList<Steps>(Arrays.asList(recipe.getSteps())))
                .putExtra(GlobalVar.Const.EXTRA_RECIPES, recipe);
        startActivity(intent);
    }
}
