package com.devlab74.foodrecipes.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Arrays;

@Entity(tableName = "recipes")
public class Recipe implements Parcelable {

    @PrimaryKey
    @NonNull
    private String uri;

    @ColumnInfo(name = "label")
    private String label;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "source")
    private String source;

    @ColumnInfo(name = "dietLabels")
    private String[] dietLabels;

    @ColumnInfo(name = "healthLabels")
    private String[] healthLabels;

    @ColumnInfo(name = "cautions")
    private String[] cautions;

    @ColumnInfo(name = "ingredientLines")
    private String[] ingredientLines;

    @ColumnInfo(name = "calories")
    private float calories;
    // private TotalDaily[] totalDaily;

    @ColumnInfo(name = "timestamp")
    private int timestamp;

    public Recipe(String uri, String label, String image, String source, String[] dietLabels, String[] healthLabels, String[] cautions, String[] ingredientLines, float calories, int timestamp) {
        this.uri = uri;
        this.label = label;
        this.image = image;
        this.source = source;
        this.dietLabels = dietLabels;
        this.healthLabels = healthLabels;
        this.cautions = cautions;
        this.ingredientLines = ingredientLines;
        this.calories = calories;
        this.timestamp = timestamp;
    }

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        uri = in.readString();
        label = in.readString();
        image = in.readString();
        source = in.readString();
        dietLabels = in.createStringArray();
        healthLabels = in.createStringArray();
        cautions = in.createStringArray();
        ingredientLines = in.createStringArray();
        calories = in.readFloat();
        timestamp = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(uri);
        dest.writeString(label);
        dest.writeString(image);
        dest.writeString(source);
        dest.writeStringArray(dietLabels);
        dest.writeStringArray(healthLabels);
        dest.writeStringArray(cautions);
        dest.writeStringArray(ingredientLines);
        dest.writeFloat(calories);
        dest.writeInt(timestamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String[] getDietLabels() {
        return dietLabels;
    }

    public void setDietLabels(String[] dietLabels) {
        this.dietLabels = dietLabels;
    }

    public String[] getHealthLabels() {
        return healthLabels;
    }

    public void setHealthLabels(String[] healthLabels) {
        this.healthLabels = healthLabels;
    }

    public String[] getCautions() {
        return cautions;
    }

    public void setCautions(String[] cautions) {
        this.cautions = cautions;
    }

    public String[] getIngredientLines() {
        return ingredientLines;
    }

    public void setIngredientLines(String[] ingredientLines) {
        this.ingredientLines = ingredientLines;
    }

    public float getCalories() {
        return calories;
    }

    public void setCalories(float calories) {
        this.calories = calories;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "uri='" + uri + '\'' +
                ", label='" + label + '\'' +
                ", image='" + image + '\'' +
                ", source='" + source + '\'' +
                ", dietLabels=" + Arrays.toString(dietLabels) +
                ", healthLabels=" + Arrays.toString(healthLabels) +
                ", cautions=" + Arrays.toString(cautions) +
                ", ingredientLines=" + Arrays.toString(ingredientLines) +
                ", calories=" + calories +
                ", timestamp=" + timestamp +
                '}';
    }
}
