package com.devlab74.foodrecipes.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
import com.devlab74.foodrecipes.R;
import com.devlab74.foodrecipes.models.Recipe;

public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    TextView label, source, calories;
    AppCompatImageView image;
    OnRecipeListener onRecipeListener;
    RequestManager requestManager;

    ViewPreloadSizeProvider viewPreloadSizeProvider;

    public RecipeViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener, RequestManager requestManager, ViewPreloadSizeProvider preloadSizeProvider) {
        super(itemView);
        this.onRecipeListener = onRecipeListener;
        this.requestManager = requestManager;
        viewPreloadSizeProvider = preloadSizeProvider;

        label = itemView.findViewById(R.id.recipe_label);
        source = itemView.findViewById(R.id.recipe_source);
        calories = itemView.findViewById(R.id.recipe_calories);
        image = itemView.findViewById(R.id.recipe_image);

        itemView.setOnClickListener(this);
    }

    public void onBind(Recipe recipe) {
        requestManager
                .load(recipe.getImage())
                .into(image);

        label.setText(recipe.getLabel());
        source.setText(recipe.getSource());
        calories.setText(String.valueOf(Math.round(recipe.getCalories())));
        viewPreloadSizeProvider.setView(image);
    }

    @Override
    public void onClick(View view) {
        onRecipeListener.onRecipeClick(getAdapterPosition());
    }
}
