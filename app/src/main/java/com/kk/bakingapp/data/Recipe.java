package com.kk.bakingapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Getter;

@Getter
public class Recipe {

    private final long id;
    private final String name;
    private final List<Ingredient> ingredients;
    private final List<Step> steps;
    private final int servings;
    private final String image;

    public Recipe(long id, String name, List<Ingredient> ingredients, List<Step> steps, int servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    @Getter
    public class Step {
        private final long id;
        private final String shortDescription;
        private final String description;
        @SerializedName("videoURL")
        private final String videoUrl;
        @SerializedName("thumbnailURL")
        private final String thumbnailUrl;

        public Step(long id, String shortDescription, String description, String videoUrl, String thumbnailUrl) {
            this.id = id;
            this.shortDescription = shortDescription;
            this.description = description;
            this.videoUrl = videoUrl;
            this.thumbnailUrl = thumbnailUrl;
        }
    }
}
