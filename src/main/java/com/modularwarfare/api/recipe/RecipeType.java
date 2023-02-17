package com.modularwarfare.api.recipe;

public enum RecipeType
{
    PARTS("parts");

    private String name;

    RecipeType(String name)
    {
        this.name = name;
    }

    public String toString()
    {
        return name;
    }
}