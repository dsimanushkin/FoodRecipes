package com.devlab74.foodrecipes.util;

import android.util.Log;

import com.devlab74.foodrecipes.models.Recipe;

import java.util.List;

public class Testing {

    public static void printRecipes(List<Recipe> recipes, String TAG) {
        for (Recipe recipe : recipes) {
            Log.d(TAG, "printRecipes: " + recipe.getLabel());
        }
    }

}
