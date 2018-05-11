package com.kk.bakingapp.data;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public enum Measure {
    @SerializedName("CUP")
    CUP,
    @SerializedName("TBLSP")
    TBLSP,
    @SerializedName("TSP")
    TSP,
    @SerializedName("G")
    G,
    @SerializedName("K")
    K,
    @SerializedName("OZ")
    OZ,
    @SerializedName("UNIT")
    UNIT
}
