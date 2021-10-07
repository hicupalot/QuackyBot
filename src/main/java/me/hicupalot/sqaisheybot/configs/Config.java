package me.hicupalot.sqaisheybot.configs;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.*;

public class Config {

    private final String token;
    private final String sqaisheyDiscord;
    private final String quacktopiaDiscord;
    private final String sqaisheyDiscordLog;
    private final String quacktopiaDiscordLog;
    private final String sqlUsername;
    private final String sqlPassword;
    private final String sqlDatabase;
    private final Integer sqlPort;

    public Config(String token, String sqaisheyDiscord, String quacktopiaDiscord, String sqaisheyDiscordLog, String quacktopiaDiscordLog, String sqlUsername, String sqlPassword, String sqlDatabase, Integer sqlPort) {

        this.token = token;
        this.sqaisheyDiscord = sqaisheyDiscord;
        this.quacktopiaDiscord = quacktopiaDiscord;
        this.sqaisheyDiscordLog = sqaisheyDiscordLog;
        this.quacktopiaDiscordLog = quacktopiaDiscordLog;
        this.sqlUsername = sqlUsername;
        this.sqlPassword = sqlPassword;
        this.sqlDatabase = sqlDatabase;
        this.sqlPort = sqlPort;

    }

    public static Config loadConfig(String name, Class<?> clazz) throws IOException {

        File file = new File(name);
        if (!file.exists()) {
            file.createNewFile();
            InputStream from = Thread.currentThread().getContextClassLoader().getResourceAsStream(name);
            try (InputStream input = from) {
                try (FileOutputStream output = new FileOutputStream(file)) {
                    byte[] b = new byte[8192];
                    int length;
                    while ((length = input.read(b)) > 0)
                        output.write(b, 0, length);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        JsonReader jsonReader = new JsonReader(new FileReader(file));
        Gson gson = new Gson();
        return gson.fromJson(jsonReader, clazz);

    }

    public static void saveConfig(String name, Object object) {
        try {
            Gson gson = new Gson();
            FileWriter fileWriter = new FileWriter(name);
            gson.toJson(object, fileWriter);
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getToken() {
        return token;
    }

    public String getSqaisheyDiscord() {
        return sqaisheyDiscord;
    }

    public String getQuacktopiaDiscord() {
        return quacktopiaDiscord;
    }

    public String getSqaisheyDiscordLog() {
        return sqaisheyDiscordLog;
    }

    public String getQuacktopiaDiscordLog() {
        return quacktopiaDiscordLog;
    }

    public String getSqlUsername() {
        return sqlUsername;
    }

    public String getSqlPassword() {
        return sqlPassword;
    }

    public String getSqlDatabase() {
        return sqlDatabase;
    }

    public Integer getSqlPort() {
        return sqlPort;
    }

}
