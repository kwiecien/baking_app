package com.kk.bakingapp.data;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
class Ingredient {
    private final double quantity;
    private final Measure measure;
    @SerializedName("ingredient")
    private final String name;

    public Ingredient(double quantity, Measure measure, String name) {
        this.quantity = quantity;
        this.measure = measure;
        this.name = name;
    }
}
