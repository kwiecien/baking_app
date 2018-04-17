package com.kk.bakingapp.data;

import java.util.List;

import lombok.Getter;

@Getter
public class Recipe {

    private final String mName;
    private final List<Ingredient> mIngredients;
    private final List<Step> mSteps;

    public Recipe(String name, List<Ingredient> ingredients, List<Step> steps) {
        mName = name;
        mIngredients = ingredients;
        mSteps = steps;
    }

    @Getter
    public class Step {
        private final int mId;
        private final String mShortDescription;
        private final String mDescription;
        private final String mVideoUrl;
        private final String mThumbnailUrl;

        public Step(int id, String shortDescription, String description, String videoUrl, String thumbnailUrl) {
            mId = id;
            mShortDescription = shortDescription;
            mDescription = description;
            mVideoUrl = videoUrl;
            mThumbnailUrl = thumbnailUrl;
        }
    }
}
