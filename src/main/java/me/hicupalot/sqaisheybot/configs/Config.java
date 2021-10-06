package me.hicupalot.sqaisheybot.configs;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    public static final String SQAISHEY_DISCORD = "894562763697963059";
    public static final String QUACKTOPIA = "298176792492244992";
    public static final String SQAISHEY_DISCORD_LOG="894564012824617001";
    public static final String QT_DISCORD_LOG = "736633672949825591";
    public static final String TESTING_SERVER = "325893724678782979";
    //-----------------------------------------------------------------//
    private static final Dotenv dotenv = Dotenv.load();
    public static String get(String key) {
        return dotenv.get(key.toUpperCase());
        }
}

