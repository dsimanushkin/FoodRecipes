package com.devlab74.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.ViewPreloadSizeProvider;
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

        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));
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
                                    mAdapter.displayOnlyLoading();
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
        mRecyclerView.smoothScrollToPosition(0);
        mRecipeListViewModel.searchRecipesApi(query, 1, 1, 30);
        mSearchView.clearFocus();
    }

    private RequestManager initGlide() {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.white_background)
                .error(R.drawable.white_background);
        return Glide.with(this)
                .setDefaultRequestOptions(requestOptions);
    }

    private void initRecyclerView() {
        ViewPreloadSizeProvider<String> viewPreloader = new ViewPreloadSizeProvider<>();
        mAdapter = new RecipeRecyclerAdapter(this, initGlide(), viewPreloader);
        VerticalSpaceItemDecorator itemDecorator = new VerticalSpaceItemDecorator(30);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerViewPreloader<String> preloader = new RecyclerViewPreloader<String>(Glide.with(this), mAdapter, viewPreloader, 30);
        mRecyclerView.addOnScrollListener(preloader);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!mRecyclerView.canScrollVertically(1) && mRecipeListViewModel.getViewState().getValue() == RecipeListViewModel.ViewState.RECIPES) {
                    mRecipeListViewModel.searchNextPage();
                }
            }
        });
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
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("recipe", mAdapter.getSelectedRecipe(position));
        startActivity(intent);
    }

    @Override
    public void onCategoryClick(String category) {
        searchRecipesApi(category);
    }

    @Override
    public void onBackPressed() {
        if (mRecipeListViewModel.getViewState().getValue() == RecipeListViewModel.ViewState.CATEGORIES) {
            super.onBackPressed();
        } else {
            mRecipeListViewModel.cancelSearchRequest();
            mRecipeListViewModel.setViewCategories();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_categories) {
            mRecipeListViewModel.setViewCategories();
        }
        return super.onOptionsItemSelected(item);
    }
}
