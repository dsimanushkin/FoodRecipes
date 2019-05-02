package com.devlab74.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.devlab74.foodrecipes.adapters.OnRecipeListener;
import com.devlab74.foodrecipes.adapters.RecipeRecyclerAdapter;
import com.devlab74.foodrecipes.models.Recipe;
import com.devlab74.foodrecipes.util.Resource;
import com.devlab74.foodrecipes.util.VerticalSpaceItemDecorator;
import com.devlab74.foodrecipes.viewmodels.RecipeListViewModel;

import java.util.List;

import static com.devlab74.foodrecipes.viewmodels.RecipeListViewModel.QUERY_EXHAUSTED;

public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    private static final String TAG = "RecipeListActivity";

    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mAdapter;
    private RecipeListViewModel mRecipeListViewModel;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        mRecyclerView = findViewById(R.id.recipe_list);
        mSearchView = findViewById(R.id.search_view);

        initRecyclerView();
        subscribeObservers();
        initSearchView();
    }

    private void subscribeObservers() {
        mRecipeListViewModel.getRecipes().observe(this, new Observer<Resource<List<Recipe>>>() {
            @Override
            public void onChanged(@Nullable Resource<List<Recipe>> listResource) {
                if (listResource != null) {
                    Log.d(TAG, "onChanged: status: " + listResource.status);

                    if (listResource.data != null) {
                        switch (listResource.status) {
                            case LOADING: {
                                if (mRecipeListViewModel.getPageNumber() > 1) {
                                    mAdapter.displayLoading();
                                } else {
                                    mAdapter.displayOnltLoading();
                                }
                                break;
                            }
                            case ERROR: {
                                Log.e(TAG, "onChanged: cannot refresh the cache");
                                Log.e(TAG, "onChanged: ERROR message: " + listResource.message);
                                Log.e(TAG, "onChanged: status: ERROR, #recipes: " + listResource.data.size());
                                mAdapter.hideLoading();
                                mAdapter.setRecipes(listResource.data);
                                Toast.makeText(RecipeListActivity.this, listResource.message, Toast.LENGTH_SHORT).show();
                                if (listResource.message.equals(QUERY_EXHAUSTED)) {
                                    mAdapter.setQueryExhausted();
                                }
                                break;
                            }
                            case SUCCESS: {
                                Log.d(TAG, "onChanged: cache has been refreshed.");
                                Log.d(TAG, "onChanged: status: SUCCESS, #recipes:" + listResource.data.size());
                                mAdapter.hideLoading();
                                mAdapter.setRecipes(listResource.data);
                                break;
                            }
                        }
                    }
                }
            }
        });

        mRecipeListViewModel.getViewState().observe(this, new Observer<RecipeListViewModel.ViewState>() {
            @Override
            public void onChanged(@Nullable RecipeListViewModel.ViewState viewState) {
                if (viewState != null) {
                    switch (viewState) {
                        case CATEGORIES: {
                            displaySearchCategories();
                            break;
                        }
                    }
                }
            }
        });
    }

    private void searchRecipesApi(String query) {
        mRecipeListViewModel.searchRecipesApi(query, 1, 0, 29);
    }

    private RequestManager initGlide() {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);
        return Glide.with(this)
                .setDefaultRequestOptions(requestOptions);
    }

    private void initRecyclerView() {
        mAdapter = new RecipeRecyclerAdapter(this, initGlide());
        VerticalSpaceItemDecorator itemDecorator = new VerticalSpaceItemDecorator(30);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initSearchView() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchRecipesApi(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void displaySearchCategories() {
        mAdapter.displaySearchCategories();
    }

    @Override
    public void onRecipeClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {
        searchRecipesApi(category);
    }
}
