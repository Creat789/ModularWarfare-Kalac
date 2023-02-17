package com.modularwarfare.api.recipe;

import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class RecipeVariables
{

    private HashMap<String, Object> variables = new HashMap<>();

    public Map<String, Object> getMap()
    {
        return variables;
    }

    /**
     * Adds a value to the recipe variables
     *
     * @param variable The type of variable
     * @param object   The object to bind to the variable
     * @return RecipeVariables instance
     * @see {@link RecipeVariables} for required variables
     */
    @Deprecated
    public RecipeVariables addValue(String variable, Object object)
    {
        variables.put(variable, object);
        return this;
    }

    /**
     * Sets the input ItemStack for the recipe<br>
     * <br>
     * Valid for recipe type(s):<br>
     *
     * @param input The input ItemStack for the recipe
     * @return Instance of RecipeVariables
     */
    public RecipeVariables setInput(ItemStack input)
    {
        variables.put("input", input);
        return this;
    }

    /**
     * Sets the output ItemStack for the recipe<br>
     * <br>
     * Valid for recipe type(s):<br>
     *
     * @param output The output ItemStack for the recipe
     * @return Instance of RecipeVariables
     */
    public RecipeVariables setOutput(ItemStack output)
    {
        variables.put("output", output);
        return this;
    }

    /**
     * Valid for recipe type(s): BLENDER
     *
     * @return Instance of RecipeVariables
     */
    public RecipeVariables setName(String name)
    {
        variables.put("name", name);
        return this;
    }

    /**
     * Valid for recipe type(s): BLENDER
     *
     * @param heal The amount of hearts to heal
     * @return Instance of RecipeVariables
     */
    public RecipeVariables setHeal(int heal)
    {
        variables.put("heal", heal);
        return this;
    }

    /**
     * Valid for recipe type(s): BLENDER
     *
     * @param ingredients The required ingredients for the blender recipe. Must be an ItemStack array.
     * @return Instance of RecipeVariables
     */
    public RecipeVariables setIngredients(ItemStack... ingredients)
    {
        variables.put("ingredients", ingredients);
        return this;
    }

    /**
     * Valid for recipe type(s): BLENDER
     *
     * @return Instance of RecipeVariables
     */
    public RecipeVariables setColour(int[] rgb)
    {
        variables.put("colour", rgb);
        return this;
    }
}
