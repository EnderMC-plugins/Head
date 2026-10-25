package fr.maxlego08.head.save;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fr.maxlego08.head.zcore.enums.Message;
import fr.maxlego08.head.zcore.logger.Logger;
import fr.maxlego08.head.zcore.utils.ZUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Loads the texts of the selected lang/xx.json file.
 * Missing keys fall back to the file of the same language inside the jar, then to lang/en.json inside the jar.
 */
public class Lang extends ZUtils {

    /**
     * Languages that are shipped inside the jar
     */
    public static final List<String> LANGUAGES = Arrays.asList("en", "de", "fr");

    private static final Gson GSON = new Gson();

    private final List<JsonObject> sources = new ArrayList<>();

    public Lang(JavaPlugin plugin, String language) {

        for (String lang : LANGUAGES) {
            if (!new File(plugin.getDataFolder(), "lang/" + lang + ".json").exists()) {
                plugin.saveResource("lang/" + lang + ".json", false);
            }
        }

        File file = new File(plugin.getDataFolder(), "lang/" + language + ".json");
        if (!file.exists()) {
            Logger.info("Language file lang/" + language + ".json not found, using lang/en.json", Logger.LogType.WARNING);
            language = "en";
            file = new File(plugin.getDataFolder(), "lang/en.json");
        }

        try (Reader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            this.addSource(reader);
        } catch (Exception exception) {
            Logger.info("Unable to read lang/" + language + ".json: " + exception.getMessage(), Logger.LogType.ERROR);
        }

        this.addResource(plugin, "lang/" + language + ".json");
        if (!language.equals("en")) {
            this.addResource(plugin, "lang/en.json");
        }
    }

    private void addResource(JavaPlugin plugin, String path) {
        InputStream inputStream = plugin.getResource(path);
        if (inputStream == null) return;

        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            this.addSource(reader);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private void addSource(Reader reader) {
        JsonObject jsonObject = GSON.fromJson(reader, JsonObject.class);
        if (jsonObject != null) this.sources.add(jsonObject);
    }

    /**
     * Returns the first element found for the path, like "inventory.heads.name"
     */
    private JsonElement get(String path) {
        for (JsonObject source : this.sources) {
            JsonElement element = source;
            for (String key : path.split("\\.")) {
                element = element != null && element.isJsonObject() ? element.getAsJsonObject().get(key) : null;
            }
            if (element != null && !element.isJsonNull()) return element;
        }
        return null;
    }

    public String getString(String path) {
        JsonElement element = this.get(path);
        return element != null && element.isJsonPrimitive() ? element.getAsString() : null;
    }

    public List<String> getStringList(String path) {
        JsonElement element = this.get(path);
        List<String> list = new ArrayList<>();
        if (element == null) return list;

        if (element.isJsonArray()) {
            element.getAsJsonArray().forEach(line -> list.add(line.getAsString()));
        } else if (element.isJsonPrimitive()) {
            list.add(element.getAsString());
        }
        return list;
    }

    /**
     * Copies the texts of the "messages" section into the {@link Message} enum
     */
    public void applyMessages() {
        for (Message message : Message.values()) {

            String path = "messages." + message.name().toLowerCase();
            if (this.get(path) == null) continue;

            List<String> lines = this.color(this.getStringList(path));
            if (lines.size() > 1) {
                message.setMessages(lines);
                message.setMessage(null);
            } else {
                message.setMessage(lines.isEmpty() ? "" : lines.get(0));
                message.setMessages(null);
            }
        }
    }
}
