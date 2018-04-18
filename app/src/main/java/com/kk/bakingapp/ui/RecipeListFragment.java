package com.kk.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
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
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.kk.bakingapp.R;
import com.kk.bakingapp.data.Recipe;
import com.kk.bakingapp.util.Jsons;

import org.json.JSONArray;
import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class RecipeListFragment extends Fragment {


    private static final String RECIPES_URL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    @BindView(R.id.recipe_list)
    RecyclerView mRecyclerView;

    public RecipeListFragment() {
        // Mandatory empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, rootView);
        fetchRecipes(RECIPES_URL);
        return rootView;
    }

    private void fetchRecipes(String url) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Timber.d(response.toString());
                        List<Recipe> recipes = Jsons.fromJsonArrayToObjects(response, Recipe.class);
                        setupRecyclerView(mRecyclerView, recipes);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Timber.e(error);
                    }
                });
        queue.add(jsonObjectRequest);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, @NonNull List<Recipe> recipes) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, recipes, false));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.RecipeViewHolder> {

        private final RecipeListFragment mParentFragment;
        private final List<Recipe> mRecipes;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe recipe = (Recipe) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(RecipeDetailFragment.ARG_RECIPE, Parcels.wrap(recipe));
                    RecipeDetailFragment fragment = new RecipeDetailFragment();
                    fragment.setArguments(arguments);
                    mParentFragment.getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.recipe_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra(RecipeDetailFragment.ARG_RECIPE, Parcels.wrap(recipe));
                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(RecipeListFragment parent,
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
            int imageResource;
            switch (position) {
                case 0:
                    imageResource = R.drawable.nutella_pie;
                    break;
                case 1:
                    imageResource = R.drawable.brownies;
                    break;
                case 2:
                    imageResource = R.drawable.yellow_cake;
                    break;
                case 3:
                    imageResource = R.drawable.cheesecake;
                    break;
                default:
                    imageResource = R.drawable.unknown;
                    break;
            }
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