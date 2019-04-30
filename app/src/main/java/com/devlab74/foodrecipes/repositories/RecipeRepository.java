package com.devlab74.foodrecipes.repositories;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.devlab74.foodrecipes.AppExecutors;
import com.devlab74.foodrecipes.models.Recipe;
import com.devlab74.foodrecipes.models.Recipes;
import com.devlab74.foodrecipes.persistence.RecipeDao;
import com.devlab74.foodrecipes.persistence.RecipeDatabase;
import com.devlab74.foodrecipes.requests.ServiceGenerator;
import com.devlab74.foodrecipes.requests.responses.ApiResponse;
import com.devlab74.foodrecipes.requests.responses.RecipeSearchResponse;
import com.devlab74.foodrecipes.util.Constants;
import com.devlab74.foodrecipes.util.NetworkBoundResource;
import com.devlab74.foodrecipes.util.Resource;

import java.util.ArrayList;
import java.util.List;

public class RecipeRepository {

    private static final String TAG = "RecipeRepository";

    private static RecipeRepository instance;
    private RecipeDao recipeDao;

    public static RecipeRepository getInstance(Context context) {
        if (instance == null) {
            instance = new RecipeRepository(context);
        }
        return instance;
    }

    public RecipeRepository(Context context) {
        recipeDao = RecipeDatabase.getInstance(context).getRecipeDao();
    }

    public LiveData<Resource<List<Recipe>>> searchRecipesApi(final String query, final int pageNumber) {
        return new NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(AppExecutors.getInstance()) {
            @Override
            protected void saveCallResult(@NonNull RecipeSearchResponse item) {
                if (item.getRecipes() != null) {
                    Recipe[] recipes = new Recipe[item.getRecipes().size()];
                    List<Recipe> mRecipes = new ArrayList<>();
                    for (int i = 0; i < item.getRecipes().size(); i++) {
                        mRecipes.add(item.getRecipes().get(i).getRecipe());
                    }
                    Log.d(TAG, "saveCallResult: Im into search recipes. Recipes array size is: " + recipes.length);
                    Log.d(TAG, "saveCallResult: Single recipe check: recipe label: " + item.getRecipes().get(0).getRecipe().getLabel());

                    int index = 0;
                    for (long rowId : recipeDao.insertRecipes((Recipe[])(mRecipes.toArray(recipes)))) {
                        if (rowId == -1) {
                            Log.d(TAG, "saveCallResult: CONFLICT... this recipe is already in the cache");
                            recipeDao.updateRecipe(
                                    recipes[index].getUri(),
                                    recipes[index].getLabel(),
                                    recipes[index].getImage(),
                                    recipes[index].getSource(),
                                    recipes[index].getCalories()
                            );
                        }
                        index++;
                    }
                }
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Recipe> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Recipe>> loadFromDb() {
                return recipeDao.searchRecipes(query, pageNumber);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RecipeSearchResponse>> createCall() {
                return ServiceGenerator.getRecipeApi().searchRecipes(Constants.APP_ID, Constants.APP_KEY, query, 0, 29);
            }
        }.getAsLiveData();
    }
}
