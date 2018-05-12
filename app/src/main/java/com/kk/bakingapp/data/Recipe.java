package com.kk.bakingapp.data;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

@Parcel
@ToString
@Getter
public class Recipe {

    long id;
    String name;
    List<Ingredient> ingredients;
    List<Step> steps;
    int servings;
    String image;

    public Recipe() {
        // Mandatory empty constructor
    }

    public Recipe(long id, String name, List<Ingredient> ingredients, List<Step> steps, int servings, String image) {
        this.id = id;
        this.name = name;
        this.ingredients = ingredients;
        this.steps = steps;
        this.servings = servings;
        this.image = image;
    }

    @Parcel
    @ToString
    @Getter
    public static class Step {
        long id;
        String shortDescription;
        String description;
        @SerializedName("videoURL")
        String videoUrl;
        @SerializedName("thumbnailURL")
        String thumbnailUrl;

        public Step() {
            // Mandatory empty constructor
        }

        public Step(long id, String shortDescription, String description, String videoUrl, String thumbnailUrl) {
            this.id = id;
            this.shortDescription = shortDescription;
            this.description = description;
            this.videoUrl = videoUrl;
            this.thumbnailUrl = thumbnailUrl;
        }
    }
}
