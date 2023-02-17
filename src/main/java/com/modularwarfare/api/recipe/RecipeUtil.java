package com.modularwarfare.api.recipe;


import java.util.ArrayList;

public class RecipeUtil
{
    public static void printRequired(String type, String desc, String modName)
    {

    }

    public static void printUnknownType(Parser parser, int num, String desc)
    {

        printVariables(parser, "");
    }

    public static void printMissing(Parser parser, int num, String missingKey, String desc)
    {

        printVariables(parser, "");
    }

    public static void printReport(Parser parser, int num, String erroredKey, String desc)
    {

        printVariables(parser, erroredKey);
    }

    private static void printVariables(Parser parser, String erroredKey)
    {
        for(String key : parser.getMap().keySet())
        {
            if(!key.equalsIgnoreCase("type"))
            {
                String pre = "- " + key + ": " + parser.getValue(key, null);
                if(key.equalsIgnoreCase(erroredKey))
                {
                    pre += " (Error)";
                }
            }
        }
    }

    private static ArrayList<String> getRequiredVariablesList(String type)
    {
        ArrayList<String> vars = new ArrayList<>();
        return vars;
    }
}
