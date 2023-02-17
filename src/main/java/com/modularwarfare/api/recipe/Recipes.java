package com.modularwarfare.api.recipe;

import java.util.ArrayList;

public class Recipes
{
    // Used for sending to clients
    public static ArrayList<String> recipeData = new ArrayList<>();

    /**
     * Recipes registered from the Config
     */


    public static void updateDataList()
    {

    }

    public static ArrayList<RecipeData> getRecipes(String type)
    {

        return new ArrayList<>();
    }

    public static void addCommRecipesToLocal()
    {

    }

    public static void clearLocalRecipes()
    {

    }

    public static void clearRemoteRecipes()
    {

    }

    public static void clearCommRecipes()
    {

    }

    public static void clearAll()
    {
        clearLocalRecipes();
        clearRemoteRecipes();
        clearCommRecipes();
    }
}
