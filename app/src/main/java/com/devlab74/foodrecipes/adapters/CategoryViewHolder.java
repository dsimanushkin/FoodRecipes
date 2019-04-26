package com.devlab74.foodrecipes.adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.devlab74.foodrecipes.R;
import com.devlab74.foodrecipes.models.Recipe;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    CircleImageView categoryImage;
    TextView categoryTitle;
    OnRecipeListener onRecipeListener;
    RequestManager requestManager;

    public CategoryViewHolder(@NonNull View itemView, OnRecipeListener onRecipeListener, RequestManager requestManager) {
        super(itemView);

        this.onRecipeListener = onRecipeListener;
        this.requestManager = requestManager;

        categoryImage = itemView.findViewById(R.id.category_image);
        categoryTitle = itemView.findViewById(R.id.category_title);

        itemView.setOnClickListener(this);
    }

    public void onBind(Recipe recipe) {
        Uri path = Uri.parse("android.resource://com.devlab74.foodrecipes/drawable/" + recipe.getImage());
        requestManager
                .load(path)
                .into(categoryImage);

        categoryTitle.setText(recipe.getLabel());
    }

    @Override
    public void onClick(View view) {
        onRecipeListener.onCategoryClick(categoryTitle.getText().toString());
    }
}
