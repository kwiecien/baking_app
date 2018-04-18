package com.kk.bakingapp.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kk.bakingapp.R;
import com.kk.bakingapp.data.Recipe;

import org.parceler.Parcels;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link MainActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_RECIPE = "recipe";

    /**
     * The dummy content this fragment is presenting.
     */
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
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
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

        // Show the dummy content as text in a TextView.
        if (mRecipe != null) {
            ((TextView) rootView.findViewById(R.id.recipe_detail)).setText(mRecipe.getIngredients().toString());
        }

        return rootView;
    }
}
