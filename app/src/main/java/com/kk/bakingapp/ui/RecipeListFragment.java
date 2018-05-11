package com.kk.bakingapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kk.bakingapp.R;
import com.kk.bakingapp.data.Recipe;
import com.kk.bakingapp.idle.SimpleIdlingResource;
import com.kk.bakingapp.util.Drawables;
import com.kk.bakingapp.util.Jsons;
import com.kk.bakingapp.util.Recipes;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeListFragment extends Fragment {

    private static final String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private static OnRecipeClickListener mCallback;

    @BindView(R.id.recipe_list_rv)
    RecyclerView mRecyclerView;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    public RecipeListFragment() {
        // Mandatory empty constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        setCallback(context);
    }

    private void setCallback(Context context) {
        try {
            mCallback = (OnRecipeClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnRecipeClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, rootView);
        fetchRecipes(RECIPES_URL, getIdlingResource());
        return rootView;
    }

    private void fetchRecipes(String url, @Nullable final SimpleIdlingResource idlingResource) {
        setIdleState(idlingResource, false);

        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    List<Recipe> recipes = Jsons.fromJsonArrayToObjects(response, Recipe.class);
                    Recipes.setRecipes(recipes);
                    setupRecyclerView(mRecyclerView, recipes);
                    setIdleState(idlingResource, true);
                },
                Timber::e);
        queue.add(jsonObjectRequest);
    }

    private void setIdleState(@Nullable SimpleIdlingResource idlingResource, boolean isIdleNow) {
        if (idlingResource != null) {
            idlingResource.setIdleState(isIdleNow);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, @NonNull List<Recipe> recipes) {
        recyclerView.setAdapter(new RecipeRecyclerViewAdapter(this, recipes, false));
    }

    public SimpleIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    interface OnRecipeClickListener {
        void onRecipeSelected(Recipe recipe);
    }

    public static class RecipeRecyclerViewAdapter
            extends RecyclerView.Adapter<RecipeRecyclerViewAdapter.RecipeViewHolder> {

        private final RecipeListFragment mParentFragment;
        private final List<Recipe> mRecipes;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe recipe = (Recipe) view.getTag();
                mCallback.onRecipeSelected(recipe);
            }
        };

        RecipeRecyclerViewAdapter(RecipeListFragment parent,
                                  List<Recipe> items,
                                  boolean twoPane) {
            mRecipes = items;
            mParentFragment = parent;
            mTwoPane = twoPane;
        }

        @NonNull
        @Override
        public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recipe_list_item, parent, false);
            return new RecipeViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecipeViewHolder holder, int position) {
            holder.mRecipeTextView.setText(mRecipes.get(position).getName());
            setImageResource(holder, position);

            holder.itemView.setTag(mRecipes.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        private void setImageResource(@NonNull RecipeViewHolder holder, int position) {
            int imageResource = Drawables.getRecipeImageResource(position);
            holder.mRecipeImageView.setImageResource(imageResource);
        }

        @Override
        public int getItemCount() {
            return mRecipes.size();
        }

        class RecipeViewHolder extends RecyclerView.ViewHolder {
            final TextView mRecipeTextView;
            final ImageView mRecipeImageView;

            RecipeViewHolder(View view) {
                super(view);
                mRecipeTextView = view.findViewById(R.id.recipe_card_tv);
                mRecipeImageView = view.findViewById(R.id.recipe_card_iv);
            }
        }
    }

}