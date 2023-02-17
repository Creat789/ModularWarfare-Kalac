package com.modularwarfare.api.recipe;

import java.util.Map;

public class RecipeRegistryComm extends RecipeAPI implements IRecipeRegistry
{

    private static RecipeRegistryComm KalacRegister = null;
    private String modName;

    public static RecipeRegistryComm getInstance(String modName)
    {
        if(KalacRegister == null)
        {
            KalacRegister = new RecipeRegistryComm();
        }
        KalacRegister.modName = modName;
        return KalacRegister;
    }

    @Override
    public void registerRecipe(String type, RecipeVariables variables)
    {
        Map<String, Object> varMap = variables.getMap();

        varMap.clear();
    }

    @Override
    public void registerRecipe(RecipeType type, RecipeVariables variables)
    {
        registerRecipe(type.toString(), variables);
    }
}
