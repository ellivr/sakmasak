package com.github.ellivr.sakmasak;

import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.ellivr.sakmasak.utils.GlobalVar;
import com.github.ellivr.sakmasak.utils.Steps;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnDataPass {

    private boolean mTwoPane;
    Bundle arguments;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getSupportFragmentManager();

            setContentView(R.layout.activity_recipe_detail);

            arguments = new Bundle();
            arguments.putParcelable(GlobalVar.Const.EXTRA_RECIPES, getIntent().getParcelableExtra(GlobalVar.Const.EXTRA_RECIPES));

            //Place the RecipeDetail Fragment, but first try to see if we already created one before
            RecipeDetailFragment recipeDetailFragment = (RecipeDetailFragment) fragmentManager.findFragmentByTag(GlobalVar.Const.TAG_FRAGMENT_DETAIL);
            if(recipeDetailFragment == null){
                //Looks like we haven't created it yet, let's create this fragment
                recipeDetailFragment = new RecipeDetailFragment();
                recipeDetailFragment.setArguments(arguments);

                final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.recipe_detail_container, recipeDetailFragment, GlobalVar.Const.TAG_FRAGMENT_DETAIL);
                transaction.addToBackStack(null);
                transaction.commit();
            }

        //This device should be dual pane if it has step_detail_container on its layout
            if (findViewById(R.id.step_detail_container) != null) {
                mTwoPane = true;
                //So we put additional StepDetail Fragment right beside it
                //But first, check if we already created one before
                StepDetailFragment stepDetailFragment = (StepDetailFragment) fragmentManager.findFragmentByTag(GlobalVar.Const.TAG_FRAGMENT_STEP);
                if(stepDetailFragment == null){
                    //Looks like we haven't created it yet, let's create this fragment
                    stepDetailFragment = new StepDetailFragment();
                    Bundle additionalArguments = new Bundle();
                        additionalArguments.putInt(GlobalVar.Const.EXTRA_STEPS_POS, 0);
                        additionalArguments.putParcelableArrayList(GlobalVar.Const.EXTRA_STEPS, getIntent().getParcelableArrayListExtra(GlobalVar.Const.EXTRA_STEPS));
                    stepDetailFragment.setArguments(additionalArguments);

                    final FragmentTransaction additionalTransaction = getSupportFragmentManager().beginTransaction();
                    additionalTransaction.replace(R.id.step_detail_container, stepDetailFragment, GlobalVar.Const.TAG_FRAGMENT_STEP);
                    additionalTransaction.addToBackStack(null);
                    additionalTransaction.commit();
                }

            }else{
                //This device should be single pane
                mTwoPane = false;
            }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int count = getFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onDataPass(ArrayList<Steps> steps, int pos) {
        if(mTwoPane == false){
            //if it's a single pane, we make new intent to display that step detail
            Intent intent = new Intent(this, StepDetailActivity.class)
                    .putExtra(GlobalVar.Const.EXTRA_STEPS_POS, pos)
                    .putParcelableArrayListExtra(GlobalVar.Const.EXTRA_STEPS, steps);
            startActivity(intent);
        }
        else{
            //if it's a dual pane, we don't make new intent, instead we do this
            Bundle arguments = new Bundle();
            arguments.putInt(GlobalVar.Const.EXTRA_STEPS_POS, pos);
            arguments.putParcelableArrayList(GlobalVar.Const.EXTRA_STEPS, steps);

            StepDetailFragment fragment = new StepDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_detail_container, fragment, GlobalVar.Const.TAG_FRAGMENT_STEP)
                    .commit();
        }
    }
}
