package com.devlab74.foodrecipes.requests;

import android.arch.lifecycle.LiveData;

import com.devlab74.foodrecipes.requests.responses.ApiResponse;
import com.devlab74.foodrecipes.requests.responses.RecipeResponse;
import com.devlab74.foodrecipes.requests.responses.RecipeSearchResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    // SEARCH RECIPES
    @GET("search")
    LiveData<ApiResponse<RecipeSearchResponse>> searchRecipes(
            @Query("app_id") String appId,
            @Query("app_key") String appKey,
            @Query("q") String query,
            @Query("from") int from,
            @Query("to") int to
    );

    // GET RECIPE
    @GET("search")
    LiveData<ApiResponse<RecipeResponse>> getRecipe(
            @Query("app_id") String appId,
            @Query("app_key") String appKey,
            @Query("r") String recipeId
    );

}
