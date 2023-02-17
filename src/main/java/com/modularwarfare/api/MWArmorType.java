package com.modularwarfare.api;

import com.google.gson.annotations.SerializedName;

public enum MWArmorType {

    @SerializedName("head") Head,
    @SerializedName("chest") Chest,
    @SerializedName("legs") Legs,
    @SerializedName("feet") Feet,
    @SerializedName("vest") Vest(1),
    @SerializedName("suits") Suits(2),
    @SerializedName("mask") Mask(5);

    int[] validSlots;

    private MWArmorType(int ... validSlots) {
        this.validSlots = validSlots;
    }

    public boolean hasSlot(int slot) {
        for (int s:validSlots) {
            if (s == slot) return true;
        }
        return false;
    }

    public int[] getValidSlots() {
        return validSlots;
    }

    public static boolean isVanilla(MWArmorType type)
    {
        return type == Head || type == Chest || type == Legs || type == Feet;
    }

}