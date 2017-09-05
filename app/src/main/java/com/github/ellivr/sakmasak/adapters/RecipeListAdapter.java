package com.github.ellivr.sakmasak.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ellivr.sakmasak.R;
import com.github.ellivr.sakmasak.utils.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ref on 2017/09/01.
 */

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.ViewHolder> {

    private final ArrayList<Recipe> mRecipes;
    private Context mContext;

    private final Callbacks mCallbacks;
    public interface Callbacks {
        void toRecipeDetail(Recipe recipe, int position);
    }

    public RecipeListAdapter(ArrayList<Recipe> recipes, Callbacks callbacks, Context context) {
        mCallbacks = callbacks;
        mRecipes = recipes;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_recipecard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Recipe recipe = mRecipes.get(position);

        holder.mRecipe = recipe;
        holder.mRecipeName.setText(recipe.getName());
        holder.mRecipeServings.setText(String.format(mContext.getResources().getString(R.string.recipe_serving_amount), String.valueOf(recipe.getServings())));

        if(recipe.getImage() != null && recipe.getImage() != ""){
            //If the recipe has an image, let's display it
            Picasso.with(mContext).load(recipe.getImage()).into(holder.mImageView);
        }

        holder.mImageView.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallbacks.toRecipeDetail(recipe, holder.getAdapterPosition());
            }
        });
//        holder.mAuthorView.setText(review.getAuthor());
    }


    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        @BindView(R.id.recipe_name)
        TextView mRecipeName;
        @BindView(R.id.recipe_servings)
        TextView mRecipeServings;
        @BindView(R.id.recipe_image)
        ImageView mImageView;

        Recipe mRecipe;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
        }
    }

    public void add(List<Recipe> recipes) {
        mRecipes.clear();
        mRecipes.addAll(recipes);
        notifyDataSetChanged();
    }

    public ArrayList<Recipe> getRecipes() {
        return mRecipes;
    }
}
