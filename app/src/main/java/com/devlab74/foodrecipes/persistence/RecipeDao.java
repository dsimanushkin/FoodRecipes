package com.devlab74.foodrecipes.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.devlab74.foodrecipes.models.Recipe;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.IGNORE;
import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface RecipeDao {

    @Insert(onConflict = IGNORE)
    long[] insertRecipes(Recipe... recipe);

    @Insert(onConflict = REPLACE)
    void insertRecipe(Recipe recipe);

    @Query("UPDATE recipes SET label = :label, image = :image, source = :source, calories = :calories WHERE uri = :uri")
    void updateRecipe(String uri, String label, String image, String source, float calories);

    @Query("SELECT * FROM recipes WHERE label LIKE '%' || :query || '%' OR ingredientLines LIKE '%' || :query || '%' ORDER BY calories DESC LIMIT (:pageNumber * 30)")
    LiveData<List<Recipe>> searchRecipes(String query, int pageNumber);

    @Query("SELECT * FROM recipes WHERE uri = :uri")
    LiveData<Recipe> getRecipe(String uri);

}
