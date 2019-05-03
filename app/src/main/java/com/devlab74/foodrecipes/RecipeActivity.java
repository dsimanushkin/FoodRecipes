package com.devlab74.foodrecipes;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.devlab74.foodrecipes.models.Recipe;
import com.devlab74.foodrecipes.viewmodels.RecipeViewModel;

public class RecipeActivity extends BaseActivity {

    private static final String TAG = "RecipeActivity";

    private RecipeViewModel mRecipeViewModel;

    private AppCompatImageView mRecipeImage;
    private TextView mRecipeLabel,mRecipeCalories, mRecipeServings, mRecipeIngredientsTitle;
    private LinearLayout mRecipeDietList, mRecipeHealthList, mRecipeCautionsList, mRecipeIngredientsList, mRecipeContainer;
    private RelativeLayout mRecipeDietContainer, mRecipeHealthContainer, mRecipeCautionsContainer, mRecipeIngredientsContainer;
    private ScrollView mParent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        mRecipeViewModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        mRecipeImage = findViewById(R.id.recipe_image);
        mRecipeLabel = findViewById(R.id.recipe_label);
        mRecipeCalories = findViewById(R.id.recipe_calories);
        mRecipeServings = findViewById(R.id.recipe_servings);
        mRecipeIngredientsTitle = findViewById(R.id.ingredients_title);
        mRecipeDietList = findViewById(R.id.diet_list);
        mRecipeHealthList = findViewById(R.id.health_list);
        mRecipeIngredientsList = findViewById(R.id.ingredients_list);
        mRecipeContainer = findViewById(R.id.container);
        mRecipeDietContainer = findViewById(R.id.diet_container);
        mRecipeHealthContainer = findViewById(R.id.health_container);
        mRecipeCautionsContainer = findViewById(R.id.cautions_container);
        mRecipeIngredientsContainer = findViewById(R.id.ingredients_container);
        mParent = findViewById(R.id.parent);

        getIncomingIntent();
    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("recipe")) {
            Recipe recipe = getIntent().getParcelableExtra("recipe");
            Log.d(TAG, "getIncomingIntent: " + recipe.toString());
        }
    }
}