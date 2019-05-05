package com.devlab74.foodrecipes.util;

public class Constants {

    public static final String[] DEFAULT_SEARCH_CATEGORIES = {"Breakfast", "Lunch", "Dinner", "Beverages", "Appetizers", "Soups", "Salads", "Desserts", "Sauces"};

    public static final String[] DEFAULT_SEARCH_CATEGORY_IMAGES = {
            "breakfast",
            "lunch",
            "dinner",
            "beverages",
            "appetizers",
            "soups",
            "salads",
            "desserts",
            "sauces"
    };

    // To get App ID and App Key register for free on edamam.com
    public static final String BASE_URL = "https://api.edamam.com";
    public static final String APP_ID = "05799ae5";
    public static final String APP_KEY = "315ca702e93082f266e45a0b29f1807f";

    public static final int CONNECTION_TIMEOUT = 10;
    public static final int READ_TIMEOUT = 3;
    public static final int WRITE_TIMEOUT = 3;
}