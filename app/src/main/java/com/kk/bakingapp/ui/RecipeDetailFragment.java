package com.kk.bakingapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kk.bakingapp.R;
import com.kk.bakingapp.data.Ingredient;
import com.kk.bakingapp.data.Recipe;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link MainActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {

    public static final String ARG_RECIPE = "recipe";
    @BindView(R.id.steps_rv)
    RecyclerView mRecyclerView;
    private Recipe mRecipe;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
        // Mandatory empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_RECIPE)) {
            mRecipe = Parcels.unwrap(getArguments().getParcelable(ARG_RECIPE));

            Activity activity = getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mRecipe.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
        ButterKnife.bind(this, rootView);
        setupRecyclerView(mRecyclerView, mRecipe.getIngredients());
        return rootView;
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, List<Ingredient> ingredients) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new StepRecyclerViewAdapter(this, ingredients, false));
    }

    private class StepRecyclerViewAdapter extends RecyclerView.Adapter<StepRecyclerViewAdapter.StepViewHolder> {
        private final RecipeDetailFragment mRecipeDetailFragment;
        private final List<Ingredient> mIngredients;
        private final boolean mTwoPane;

        StepRecyclerViewAdapter(RecipeDetailFragment recipeDetailFragment, List<Ingredient> ingredients, boolean twoPane) {
            mRecipeDetailFragment = recipeDetailFragment;
            mIngredients = ingredients;
            mTwoPane = twoPane;
        }

        @NonNull
        @Override
        public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_list_item, parent, false);
            return new StepViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
            Ingredient ingredient = mIngredients.get(position);
            holder.mQuantityTextView.setText(String.valueOf(ingredient.getQuantity()));
            holder.mMeasureTextView.setText(String.valueOf(ingredient.getMeasure()));
            holder.mNameTextView.setText(ingredient.getName());
        }

        @Override
        public int getItemCount() {
            return mIngredients.size();
        }

        class StepViewHolder extends RecyclerView.ViewHolder {
            final TextView mQuantityTextView;
            final TextView mMeasureTextView;
            final TextView mNameTextView;

            StepViewHolder(View itemView) {
                super(itemView);
                mQuantityTextView = itemView.findViewById(R.id.ingredient_quantity_tv);
                mMeasureTextView = itemView.findViewById(R.id.ingredient_measure_tv);
                mNameTextView = itemView.findViewById(R.id.ingredient_name_tv);
            }
        }

    }
}
