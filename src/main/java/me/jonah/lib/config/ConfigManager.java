package me.jonah.lib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Jonah Bray
 */
@SuppressWarnings("all")
public class ConfigManager {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public <T> Config<T> loadConfig(String name, T defaults) {
        return new Config<T>(name, gson, defaults);
    }

    public Gson getGson() {
        return gson;
    }
}


