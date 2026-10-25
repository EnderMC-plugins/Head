package fr.maxlego08.head.save;

import fr.maxlego08.head.HeadPlugin;
import fr.maxlego08.head.api.enums.HeadCategory;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class Config {

    public static boolean enableDebug = true;
    public static boolean enableDebugTime = false;

    public static Map<HeadCategory, String> categoryNames = new HashMap<>();

    public static String headInventoryName;
    public static String searchInventoryName;
    public static String paginationInventoryName;
    public static ItemConfiguration headItem;
    public static ItemConfiguration paginateItem;
    public static ItemConfiguration refreshItem;
    public static ItemConfiguration informationItem;
    public static ItemConfiguration searchItem;
    public static String backItemName;
    public static String pageItemName;

    public static String playersOnlineInventoryName;
    public static String playersOfflineInventoryName;
    public static ItemConfiguration playersOnlineItem;
    public static ItemConfiguration playersOfflineItem;
    public static ItemConfiguration playerHeadItem;
    public static ItemConfiguration previousPageItem;
    public static ItemConfiguration nextPageItem;

    /**
     * static Singleton instance.
     */
    private static volatile Config instance;


    /**
     * Private constructor for singleton.
     */
    private Config() {
    }

    /**
     * Return a singleton instance of Config.
     */
    public static Config getInstance() {
        // Double lock for thread safety.
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config();
                }
            }
        }
        return instance;
    }

    public void loadConfiguration(HeadPlugin plugin) {
        FileConfiguration configuration = plugin.getConfig();

        Lang lang = new Lang(plugin, configuration.getString("language", "en"));
        lang.applyMessages();

        for (HeadCategory headCategory : HeadCategory.values()) {
            String categoryName = lang.getString("category." + headCategory.name().toLowerCase());
            categoryNames.put(headCategory, categoryName == null ? headCategory.getName() : categoryName);
        }

        headInventoryName = lang.getString("inventory.heads.name");
        headItem = new ItemConfiguration(lang.getString("inventory.heads.item.name"), lang.getStringList("inventory.heads.item.lore"));
        refreshItem = new ItemConfiguration(lang.getString("inventory.heads.refresh.name"), lang.getStringList("inventory.heads.refresh.lore"));
        informationItem = new ItemConfiguration(lang.getString("inventory.heads.informations.name"), lang.getStringList("inventory.heads.informations.lore"));

        paginationInventoryName = lang.getString("inventory.pagination.name");
        paginateItem = new ItemConfiguration(lang.getString("inventory.pagination.item.name"), lang.getStringList("inventory.pagination.item.lore"));
        searchItem = new ItemConfiguration(lang.getString("inventory.pagination.search.name"), lang.getStringList("inventory.pagination.search.lore"));

        searchInventoryName = lang.getString("inventory.search.name");

        backItemName = lang.getString("inventory.back");
        pageItemName = lang.getString("inventory.page");

        playersOnlineInventoryName = lang.getString("inventory.players.online.title");
        playersOfflineInventoryName = lang.getString("inventory.players.offline.title");
        playersOnlineItem = new ItemConfiguration(lang.getString("inventory.players.online.name"), lang.getStringList("inventory.players.online.lore"));
        playersOfflineItem = new ItemConfiguration(lang.getString("inventory.players.offline.name"), lang.getStringList("inventory.players.offline.lore"));
        playerHeadItem = new ItemConfiguration(lang.getString("inventory.players.head.name"), lang.getStringList("inventory.players.head.lore"));
        previousPageItem = new ItemConfiguration(lang.getString("inventory.players.previous.name"), lang.getStringList("inventory.players.previous.lore"));
        nextPageItem = new ItemConfiguration(lang.getString("inventory.players.next.name"), lang.getStringList("inventory.players.next.lore"));
    }
}
