package com.github.ellivr.sakmasak.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ellivr.sakmasak.R;
import com.github.ellivr.sakmasak.utils.Recipe;
import com.github.ellivr.sakmasak.utils.Steps;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ref on 2017/09/01.
 */

public class RecipeStepsAdapter extends RecyclerView.Adapter<RecipeStepsAdapter.ViewHolder> {

    private final ArrayList<Steps> mSteps;
    private Context mContext;

    private final Callbacks mCallbacks;
    public interface Callbacks {
        void toStepDetail(ArrayList<Steps> steps, int position);
    }

    public RecipeStepsAdapter(ArrayList<Steps> steps, Callbacks callbacks, Context context) {
        mCallbacks = callbacks;
        mSteps = steps;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_stepcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Steps step = mSteps.get(position);

        holder.mDetailStep.setText(step.getShortDescription());
        holder.mDetailStep.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.toStepDetail(mSteps, holder.getAdapterPosition());
            }
        });
//        holder.mRecipe = recipe;
//        holder.mRecipeName.setText(recipe.getName());
//        holder.mRecipeServings.setText(String.format(mContext.getResources().getString(R.string.recipe_serving_amount), String.valueOf(recipe.getServings())));
//
//        holder.mImageView.setOnClickListener(new ImageView.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mCallbacks.toRecipeDetail(recipe, holder.getAdapterPosition());
//            }
//        });
////        holder.mAuthorView.setText(review.getAuthor());
    }


    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        @BindView(R.id.detail_step)
        TextView mDetailStep;

        Steps mStep;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }

    public void add(ArrayList<Steps> steps) {
        mSteps.clear();
        mSteps.addAll(steps);
        notifyDataSetChanged();
    }

    public ArrayList<Steps> getSteps() {
        return mSteps;
    }
}