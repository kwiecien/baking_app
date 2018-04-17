package com.kk.bakingapp.data;

import lombok.Getter;

@Getter
class Ingredient {
    private final double mQuantity;
    private final Measure mMeasure;
    private final String mName;

    public Ingredient(double quantity, Measure measure, String name) {
        mQuantity = quantity;
        mMeasure = measure;
        mName = name;
    }
}
