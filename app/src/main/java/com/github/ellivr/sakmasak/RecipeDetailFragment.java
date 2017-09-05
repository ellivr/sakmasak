package com.github.ellivr.sakmasak;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.ellivr.sakmasak.adapters.RecipeStepsAdapter;
import com.github.ellivr.sakmasak.utils.GlobalVar;
import com.github.ellivr.sakmasak.utils.Ingredients;
import com.github.ellivr.sakmasak.utils.Recipe;
import com.github.ellivr.sakmasak.utils.Steps;
import com.github.ellivr.sakmasak.widget.UpdateWidgetService;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment implements RecipeStepsAdapter.Callbacks {

    Recipe selectedRecipe;
    RecipeStepsAdapter mRecipeStepsAdapter;
    Bundle args;

    @BindView(R.id.detail_ingredients)
    TextView detailIngredientsTextview;
    @BindView(R.id.detail_steps)
    RecyclerView detailStepsView;
    @BindView(R.id.detail_title)
    TextView detailTitleView;
    @BindView(R.id.recipe_detail_scrollview)
    ScrollView scrollView;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    OnDataPass mOnDataPass;
    public interface OnDataPass{
        public void onDataPass(ArrayList<Steps> step, int pos);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);
        args = getArguments();
        setHasOptionsMenu(true);

        if(savedInstanceState != null){
            args = savedInstanceState.getBundle(GlobalVar.Const.EXTRA_RECIPES);
        }
        //Check if we have scrollview position saved in our instance
        if (savedInstanceState != null && savedInstanceState.containsKey(GlobalVar.Const.EXTRA_SCROLLPOS)) {
            //Grab it
            final int[] position = savedInstanceState.getIntArray(GlobalVar.Const.EXTRA_SCROLLPOS);
            if(position != null)
                scrollView.post(new Runnable() {
                    public void run() {
                        //And get back to where we were
                        scrollView.scrollTo(position[0], position[1]);
                    }
                });
        }

        if (args == null) {
           // rootView.setVisibility(View.INVISIBLE);
            return rootView;
        }
        else{
            selectedRecipe = args.getParcelable(GlobalVar.Const.EXTRA_RECIPES);
            if (selectedRecipe != null) {
                detailTitleView.setText(String.format(getString(R.string.detail_title), selectedRecipe.getName()));
                for(int i=0;i<selectedRecipe.getIngredients().length;i++){
                    Ingredients temp = selectedRecipe.getIngredients()[i];
                    detailIngredientsTextview.append(GlobalVar.Helper.getFriendlyIngredient(getContext(), temp) + "\n");
                }

                mRecipeStepsAdapter = new RecipeStepsAdapter(new ArrayList<Steps>(), this, getContext());
                mRecipeStepsAdapter.add(new ArrayList<>(Arrays.asList(selectedRecipe.getSteps())));
                detailStepsView.setNestedScrollingEnabled(false);
                detailStepsView.setAdapter(mRecipeStepsAdapter);
                LinearLayoutManager myLayout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                detailStepsView.setLayoutManager(myLayout);
                rootView.setVisibility(View.VISIBLE);
                Log.v("TEST", "Done" + selectedRecipe.getName());

                //update widget
                UpdateWidget();
            }
        }


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        UpdateWidget();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnDataPass = (OnDataPass) context;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(GlobalVar.Const.EXTRA_RECIPES, args);
        //As well as the current scrollview position
        outState.putIntArray(GlobalVar.Const.EXTRA_SCROLLPOS, new int[]{ scrollView.getScrollX(), scrollView.getScrollY()});
    }

    //User has clicked our steps adapter, tell the activity to show the step detail
    @Override
    public void toStepDetail(ArrayList<Steps> step, int position) {
        mOnDataPass.onDataPass(step, position);
    }

    void UpdateWidget(){
        if (selectedRecipe != null) {
            ArrayList<String> widgetIngredients = new ArrayList<>();
            widgetIngredients.add(String.format(getString(R.string.detail_title), selectedRecipe.getName()) + getString(R.string.colon));
            for(int i=0;i<selectedRecipe.getIngredients().length;i++){
                Ingredients temp = selectedRecipe.getIngredients()[i];
                widgetIngredients.add(String.format(getString(R.string.dash), GlobalVar.Helper.getFriendlyIngredient(getContext(), temp)));
            }
            if(widgetIngredients.size() > 0){
                UpdateWidgetService.startWidgetService(getContext(),widgetIngredients);
            }
        }
    }
}
