package fr.maxlego08.head.inventory.inventories;

import fr.maxlego08.head.HeadPlugin;
import fr.maxlego08.head.exceptions.InventoryOpenException;
import fr.maxlego08.head.inventory.VInventory;
import fr.maxlego08.head.save.Config;
import fr.maxlego08.head.zcore.enums.EnumInventory;
import fr.maxlego08.head.zcore.enums.Message;
import fr.maxlego08.head.zcore.utils.inventory.InventoryResult;
import fr.maxlego08.head.zcore.utils.inventory.Pagination;
import fr.maxlego08.head.zcore.utils.nms.NmsVersion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Displays the heads of the online or offline players, args[0] is true for the online players
 */
public class InventoryPlayers extends VInventory {

    /**
     * The 7x4 slots inside the lime glass border
     */
    private static final int[] SLOTS = {
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    @Override
    public InventoryResult openInventory(HeadPlugin plugin, Player player, int page, Object... args) throws InventoryOpenException {

        boolean online = !(args.length > 0 && Boolean.FALSE.equals(args[0]));

        List<OfflinePlayer> players = online ? new ArrayList<OfflinePlayer>(Bukkit.getOnlinePlayers()) : this.getOfflinePlayers();
        players.sort(Comparator.comparing(OfflinePlayer::getName, String.CASE_INSENSITIVE_ORDER));

        int maxPage = Math.max(1, (players.size() + SLOTS.length - 1) / SLOTS.length);
        int currentPage = Math.min(Math.max(page, 1), maxPage);

        String inventoryName = online ? Config.playersOnlineInventoryName : Config.playersOfflineInventoryName;
        createInventory(color(getMessage(inventoryName, "%count%", format(players.size()))), 54);

        ItemStack border = NmsVersion.nmsVersion.isNewMaterial() ? new ItemStack(Material.LIME_STAINED_GLASS_PANE) : new ItemStack(getMaterial(160), 1, (byte) 5);
        ItemMeta borderMeta = border.getItemMeta();
        borderMeta.setDisplayName(" ");
        border.setItemMeta(borderMeta);
        for (int slot = 0; slot < 54; slot++) {
            if (slot < 9 || slot >= 45 || slot % 9 == 0 || slot % 9 == 8) addItem(slot, border);
        }

        // Green for the online players, red for the offline players
        ItemStack switchItem;
        if (online) {
            switchItem = NmsVersion.nmsVersion.isNewMaterial() ? new ItemStack(Material.LIME_WOOL) : new ItemStack(getMaterial(35), 1, (byte) 5);
            Config.playersOnlineItem.apply(switchItem);
        } else {
            switchItem = NmsVersion.nmsVersion.isNewMaterial() ? new ItemStack(Material.RED_WOOL) : new ItemStack(getMaterial(35), 1, (byte) 14);
            Config.playersOfflineItem.apply(switchItem);
        }
        addItem(4, switchItem).setClick(event -> createInventory(plugin, player, EnumInventory.PLAYERS, 1, !online));

        List<OfflinePlayer> pagePlayers = new Pagination<OfflinePlayer>().paginate(players, SLOTS.length, currentPage);
        for (int index = 0; index < pagePlayers.size(); index++) {

            OfflinePlayer target = pagePlayers.get(index);

            ItemStack itemStack = playerHead(target);
            Config.playerHeadItem.apply(itemStack, "%player%", target.getName());
            addItem(SLOTS[index], itemStack).setClick(event -> {
                give(player, playerHead(target));
                message(player, Message.GIVE_HEAD, "%name%", target.getName());
            });
        }

        if (currentPage > 1) {
            ItemStack previous = new ItemStack(Material.ARROW);
            Config.previousPageItem.apply(previous, "%page%", currentPage, "%max%", maxPage);
            addItem(48, previous).setClick(event -> createInventory(plugin, player, EnumInventory.PLAYERS, currentPage - 1, online));
        }

        if (currentPage < maxPage) {
            ItemStack next = new ItemStack(Material.ARROW);
            Config.nextPageItem.apply(next, "%page%", currentPage, "%max%", maxPage);
            addItem(50, next).setClick(event -> createInventory(plugin, player, EnumInventory.PLAYERS, currentPage + 1, online));
        }

        return InventoryResult.SUCCESS;
    }

    /**
     * All players who have ever joined the server and are not online
     */
    private List<OfflinePlayer> getOfflinePlayers() {
        List<OfflinePlayer> players = new ArrayList<>();
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            if (!offlinePlayer.isOnline() && offlinePlayer.getName() != null) players.add(offlinePlayer);
        }
        return players;
    }
}
