package com.kk.bakingapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.bakingapp.R;
import com.kk.bakingapp.data.Ingredient;
import com.kk.bakingapp.data.Recipe;
import com.kk.bakingapp.util.Drawables;

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

    @BindView(R.id.ingredients_rv)
    RecyclerView mIngredientsRecyclerView;
    @BindView(R.id.steps_rv)
    RecyclerView mStepsRecyclerView;

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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
        ButterKnife.bind(this, rootView);
        setupToolbar();
        setupIngredientsRecyclerView(mIngredientsRecyclerView, mRecipe.getIngredients());
        setupStepsRecyclerView(mStepsRecyclerView, mRecipe.getSteps());
        return rootView;
    }

    private void setupToolbar() {
        CollapsingToolbarLayout appBarLayout = getActivity().findViewById(R.id.recipe_detail_toolbar_layout);
        ImageView mToolbarImageView = getActivity().findViewById(R.id.recipe_toolbar_iv);
        if (appBarLayout != null) {
            appBarLayout.setTitle(mRecipe.getName());
            mToolbarImageView.setImageResource(Drawables.getRecipeImageResource((int) mRecipe.getId() - 1));
        }
    }

    private void setupIngredientsRecyclerView(@NonNull RecyclerView recyclerView, List<Ingredient> ingredients) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new IngredientsRecyclerViewAdapter(this, ingredients, false));
    }

    private void setupStepsRecyclerView(RecyclerView recyclerView, List<Recipe.Step> steps) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new StepsRecyclerViewAdapter(this, steps, false));
    }

    private class IngredientsRecyclerViewAdapter extends RecyclerView.Adapter<IngredientsRecyclerViewAdapter.IngredientViewHolder> {
        private final RecipeDetailFragment mRecipeDetailFragment;
        private final List<Ingredient> mIngredients;
        private final boolean mTwoPane;

        IngredientsRecyclerViewAdapter(RecipeDetailFragment recipeDetailFragment, List<Ingredient> ingredients, boolean twoPane) {
            mRecipeDetailFragment = recipeDetailFragment;
            mIngredients = ingredients;
            mTwoPane = twoPane;
        }

        @NonNull
        @Override
        public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ingredient_list_item, parent, false);
            return new IngredientViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
            Ingredient ingredient = mIngredients.get(position);
            holder.mQuantityTextView.setText(String.valueOf(ingredient.getQuantity()));
            holder.mMeasureTextView.setText(String.valueOf(ingredient.getMeasure()));
            holder.mNameTextView.setText(ingredient.getName());
        }

        @Override
        public int getItemCount() {
            return mIngredients.size();
        }

        class IngredientViewHolder extends RecyclerView.ViewHolder {
            final TextView mQuantityTextView;
            final TextView mMeasureTextView;
            final TextView mNameTextView;

            IngredientViewHolder(View itemView) {
                super(itemView);
                mQuantityTextView = itemView.findViewById(R.id.ingredient_quantity_tv);
                mMeasureTextView = itemView.findViewById(R.id.ingredient_measure_tv);
                mNameTextView = itemView.findViewById(R.id.ingredient_name_tv);
            }
        }

    }

    private class StepsRecyclerViewAdapter extends RecyclerView.Adapter<StepsRecyclerViewAdapter.StepViewHolder> {
        private final RecipeDetailFragment mRecipeDetailFragment;
        private final List<Recipe.Step> mSteps;
        private final boolean mTwoPane;

        StepsRecyclerViewAdapter(RecipeDetailFragment recipeDetailFragment, List<Recipe.Step> steps, boolean twoPane) {
            mRecipeDetailFragment = recipeDetailFragment;
            mSteps = steps;
            mTwoPane = twoPane;
        }

        @NonNull
        @Override
        public StepViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.step_list_item, parent, false);
            return new StepViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StepViewHolder holder, int position) {
            Recipe.Step step = mSteps.get(position);
            holder.mShortDescription.setText(String.valueOf(step.getShortDescription()));
            holder.mDescription.setText(String.valueOf(step.getDescription()));
        }

        @Override
        public int getItemCount() {
            return mSteps.size();
        }

        class StepViewHolder extends RecyclerView.ViewHolder {
            final TextView mShortDescription;
            final TextView mDescription;

            StepViewHolder(View itemView) {
                super(itemView);
                mShortDescription = itemView.findViewById(R.id.step_short_description_tv);
                mDescription = itemView.findViewById(R.id.step_description_tv);
            }
        }

    }
}
