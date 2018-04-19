package com.kk.bakingapp.util;

import com.kk.bakingapp.R;

public class Drawables {

    private Drawables() {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static int getRecipeImageResource(int id) {
        int imageResource;
        switch (id) {
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
        return imageResource;
    }

}
