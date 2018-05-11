package com.kk.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
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
    public static final String ARG_RECIPE_ID = "recipe_id";

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

        if (getArguments() != null && getArguments().containsKey(ARG_RECIPE)) {
            mRecipe = Parcels.unwrap(getArguments().getParcelable(ARG_RECIPE));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
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
        DividerItemDecoration itemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new IngredientsRecyclerViewAdapter(this, ingredients, false));
    }

    private void setupStepsRecyclerView(RecyclerView recyclerView, List<Recipe.Step> steps) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        DividerItemDecoration itemDecoration =
                new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.HORIZONTAL);
        recyclerView.addItemDecoration(itemDecoration);
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
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Recipe.Step step = (Recipe.Step) view.getTag();
                Context context = view.getContext();
                Intent intent = new Intent(context, RecipeStepActivity.class);
                intent.putExtra(RecipeStepFragment.ARG_RECIPE_STEP, Parcels.wrap(step));
                intent.putExtra(RecipeStepFragment.ARG_RECIPE_ID, mRecipe.getId());
                context.startActivity(intent);
            }
        };

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
            holder.mShortDescriptionTextView.setText(String.valueOf(step.getShortDescription()));
            holder.mDescriptionTextView.setText(String.valueOf(step.getDescription()));
            holder.itemView.setTag(mSteps.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mSteps.size();
        }

        class StepViewHolder extends RecyclerView.ViewHolder {
            final TextView mShortDescriptionTextView;
            final TextView mDescriptionTextView;

            StepViewHolder(View itemView) {
                super(itemView);
                mShortDescriptionTextView = itemView.findViewById(R.id.step_short_description_tv);
                mDescriptionTextView = itemView.findViewById(R.id.step_description_tv);
            }
        }

    }
}
