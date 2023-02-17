package com.modularwarfare.api.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;

public class RecipeRegistryRemote extends RecipeAPI
{
    private static RecipeRegistryRemote KalacRegister = null;

    public static RecipeRegistryRemote getInstance()
    {
        if(KalacRegister == null)
        {
            KalacRegister = new RecipeRegistryRemote();
        }
        return KalacRegister;
    }


    public static ItemStack[] parseIngredients(Parser parser, String ingredients, int num)
    {
        ArrayList<ItemStack> list = new ArrayList<>();
        String[] ingredientData = ingredients.split("/");

        if(ingredientData.length == 0)
        {
            return null;
        }

        int length = ingredientData.length > 4 ? 4 : ingredientData.length;
        for(int i = 0; i < length; i++)
        {
            String[] itemData = ingredientData[i].split(":");
            String itemName = itemData[0] + ":" + itemData[1];
            String itemAmount = "1";
            String itemMetadata = "0";

            if(itemData.length > 2)
            {
                itemAmount = itemData[2];
                if(itemData.length > 3)
                {
                    itemMetadata = itemData[3];
                }
            }
            Item item = Item.getByNameOrId(itemName);

            int i_amount;
            try
            {
                i_amount = Integer.parseInt(itemAmount);
            }
            catch(NumberFormatException e)
            {

                return null;
            }

            int i_metadata;
            try
            {
                i_metadata = Integer.parseInt(itemMetadata);
            }
            catch(NumberFormatException e)
            {
                return null;
            }

            list.add(new ItemStack(item, i_amount, i_metadata));
        }
        return list.toArray(new ItemStack[0]);
    }

    public static int[] parseColour(Parser parser, String colour, int num)
    {
        String[] rgb = colour.split("-");
        if(rgb.length == 3)
        {
            String r = rgb[0];
            String g = rgb[1];
            String b = rgb[2];

            int red;
            try
            {
                red = Integer.parseInt(r);
            }
            catch(NumberFormatException e)
            {

                return null;
            }

            int green;
            try
            {
                green = Integer.parseInt(g);
            }
            catch(NumberFormatException e)
            {

                return null;
            }

            int blue;
            try
            {
                blue = Integer.parseInt(b);
            }
            catch(NumberFormatException e)
            {

                return null;
            }

            return new int[]{red, green, blue};
        }
        else
        {

            return null;
        }
    }

    public static String parseSpaces(String name)
    {
        return name.replaceAll("_", " ");
    }

    public static String parseFormatting(String name)
    {
        name = name.replaceAll("&0", TextFormatting.BLACK.toString());
        name = name.replaceAll("&1", TextFormatting.DARK_BLUE.toString());
        name = name.replaceAll("&2", TextFormatting.DARK_GREEN.toString());
        name = name.replaceAll("&3", TextFormatting.DARK_AQUA.toString());
        name = name.replaceAll("&4", TextFormatting.DARK_RED.toString());
        name = name.replaceAll("&5", TextFormatting.DARK_PURPLE.toString());
        name = name.replaceAll("&6", TextFormatting.GOLD.toString());
        name = name.replaceAll("&7", TextFormatting.GRAY.toString());
        name = name.replaceAll("&8", TextFormatting.DARK_GRAY.toString());
        name = name.replaceAll("&9", TextFormatting.BLUE.toString());
        name = name.replaceAll("&a", TextFormatting.GREEN.toString());
        name = name.replaceAll("&b", TextFormatting.AQUA.toString());
        name = name.replaceAll("&c", TextFormatting.RED.toString());
        name = name.replaceAll("&d", TextFormatting.LIGHT_PURPLE.toString());
        name = name.replaceAll("&e", TextFormatting.YELLOW.toString());
        name = name.replaceAll("&f", TextFormatting.WHITE.toString());
        name = name.replaceAll("&k", TextFormatting.OBFUSCATED.toString());
        name = name.replaceAll("&l", TextFormatting.BOLD.toString());
        name = name.replaceAll("&m", TextFormatting.STRIKETHROUGH.toString());
        name = name.replaceAll("&n", TextFormatting.UNDERLINE.toString());
        name = name.replaceAll("&o", TextFormatting.ITALIC.toString());
        name = name.replaceAll("&r", TextFormatting.RESET.toString());
        return name;
    }

}
