package com.modularwarfare;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class ModConfig {

    public transient static ModConfig INSTANCE;

    //general
    public General general = new General();
    public static class General {
        public boolean customInventory = true;
        public boolean prototype_pack_extraction = true;
        public boolean animated_pack_extraction = true;

        public boolean modified_pack_server_kick = true;
        public boolean drop_extra_slots_on_death = true;
    }

    //shots
    public Shots shots = new Shots();
    public static class Shots {
        public boolean shot_break_glass = false;
        public boolean knockback_entity_damage = false;
        public float maxShooterPing = 1000;
    }

    //guns
    public Guns guns = new Guns();
    public static class Guns {
        public boolean guns_interaction_hand = true;

        public List<String> anim_guns_show_default_objects = Arrays.asList("ammoModel",
                "leftArmModel", "leftArmLayerModel",
                "leftArmSlimModel", "leftArmLayerSlimModel",
                "rightArmModel", "rightArmLayerModel",
                "rightArmSlimModel", "rightArmLayerSlimModel",
                "flashModel","sprint_righthand","sprint_lefthand",
                "selector_semi","selector_full","selector_brust",
                "bulletModel");
    }

    //drops
    public Drops drops = new Drops();
    public static class Drops {
        public boolean advanced_drops_models = true;
        public int drops_despawn_time = 120;
        public boolean advanced_drops_models_everything = false;
    }

    //hud ui
    public Hud hud = new Hud();
    public static class Hud {
        public boolean hitmarkers = true;
        public boolean enable_crosshair = true;
        public boolean dynamic_crosshair = true;
        public boolean ammo_count = true;
        public boolean snap_fade_hit = true;
        public boolean isDynamicFov = false;
        public boolean ads_blur=false;
        public float handDepthRange = 0.7f;
        public float eraseScopeDepth=1f;
        public int shadersColorTexID=0;
    }

    //walk sounds
    public Walk walks_sounds = new Walk();
    public static class Walk {
        public boolean walk_sounds = true;
        public float volume = 0.3f;
    }

    //casings
    public Casings casings_drops = new Casings();
    public static class Casings {
        public int despawn_time = 10;
    }

    public KillFeed killFeed = new KillFeed();
    public static class KillFeed {
        public boolean enableKillFeed = true;
        public boolean sendDefaultKillMessage = false;
        public int messageDuration = 10;
        public List<String> messageList = Arrays.asList("&a{killer} &dkilled &c{victim}", "&a{killer} &fdestroyed &c{victim}", "&a{killer} &fshot &c{victim}");
    }

    public boolean model_optimization = true;
    public boolean gun_break = true;

    public boolean debug_hits_message = false;
    public boolean dev_mode = true;

    public String version = ModularWarfare.MOD_VERSION;

    public ModConfig(File configFile) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            if (configFile.exists()) {
                JsonReader jsonReader = new JsonReader(new FileReader(configFile));
                ModConfig config = gson.fromJson(jsonReader, ModConfig.class);
                System.out.println("Comparing version " + config.version + " to " + ModularWarfare.MOD_VERSION);
                if (config.version == null || !config.version.matches(ModularWarfare.MOD_VERSION)) {
                    try (Writer writer = new OutputStreamWriter(new FileOutputStream(configFile),"UTF-8")) {
                        gson.toJson(this, writer);
                    }
                    INSTANCE = this;
                } else {
                    INSTANCE = config;
                }
            } else {
                try (Writer writer = new OutputStreamWriter(new FileOutputStream(configFile),"UTF-8")) {
                    gson.toJson(this, writer);
                }
                INSTANCE = this;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}