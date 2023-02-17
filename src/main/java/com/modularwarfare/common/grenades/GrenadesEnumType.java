package com.modularwarfare.common.grenades;

import com.google.gson.annotations.SerializedName;

public enum GrenadesEnumType {

    @SerializedName("frag") Frag("frag"),
    @SerializedName("smoke") Smoke("smoke"),
    @SerializedName("stun") Stun("stun"),
    @SerializedName("gas") Gas("gas"),
    @SerializedName("c4") C4("c4");


    public String typeName;

    GrenadesEnumType(String typeName) {
        this.typeName = typeName;
    }
}