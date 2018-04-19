package com.kk.bakingapp.data;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Parcel
public class Ingredient {
    double quantity;
    Measure measure;
    @SerializedName("ingredient")
    String name;

    public Ingredient() {
        // Mandatory empty constructor
    }

    public Ingredient(double quantity, Measure measure, String name) {
        this.quantity = quantity;
        this.measure = measure;
        this.name = name;
    }
}
