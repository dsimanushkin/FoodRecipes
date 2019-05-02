package com.devlab74.foodrecipes.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.devlab74.foodrecipes.R;
import com.devlab74.foodrecipes.models.Recipe;
import com.devlab74.foodrecipes.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class RecipeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int CATEGORY_TYPE = 1;
    private static final int RECIPE_TYPE = 2;

    private List<Recipe> mRecipes;
    private OnRecipeListener onRecipeListener;
    private RequestManager requestManager;

    public RecipeRecyclerAdapter(OnRecipeListener onRecipeListener, RequestManager requestManager) {
        this.onRecipeListener = onRecipeListener;
        this.requestManager = requestManager;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = null;
        switch (i) {
            case CATEGORY_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_category_list_item, viewGroup, false);
                return new CategoryViewHolder(view, onRecipeListener, requestManager);
            }
            case RECIPE_TYPE: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recipe_list_item, viewGroup, false);
                return new RecipeViewHolder(view, onRecipeListener, requestManager);
            }
            default: {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_recipe_list_item, viewGroup, false);
                return new RecipeViewHolder(view, onRecipeListener, requestManager);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mRecipes.get(position).getCalories() == -1) {
            return CATEGORY_TYPE;
        } else {
            return RECIPE_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        int itemViewType = getItemViewType(i);
        if (itemViewType == CATEGORY_TYPE) {
            ((CategoryViewHolder)viewHolder).onBind(mRecipes.get(i));
        } else if (itemViewType == RECIPE_TYPE) {
            ((RecipeViewHolder)viewHolder).onBind(mRecipes.get(i));
        }

    }

    @Override
    public int getItemCount() {
        if (mRecipes != null) {
            return mRecipes.size();
        }
        return 0;
    }

    public void displaySearchCategories() {
        List<Recipe> categories = new ArrayList<>();
        for (int i = 0; i < Constants.DEFAULT_SEARCH_CATEGORIES.length; i++) {
            Recipe recipe = new Recipe();
            recipe.setLabel(Constants.DEFAULT_SEARCH_CATEGORIES[i]);
            recipe.setImage(Constants.DEFAULT_SEARCH_CATEGORY_IMAGES[i]);
            recipe.setCalories(-1);
            categories.add(recipe);
        }
        mRecipes = categories;
        notifyDataSetChanged();
    }

    public void setRecipes(List<Recipe> mRecipes) {
        this.mRecipes = mRecipes;
        notifyDataSetChanged();
    }
}
