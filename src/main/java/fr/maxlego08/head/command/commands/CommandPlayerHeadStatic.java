package fr.maxlego08.head.command.commands;

import fr.maxlego08.head.HeadPlugin;
import fr.maxlego08.head.command.VCommand;
import fr.maxlego08.head.zcore.enums.Message;
import fr.maxlego08.head.zcore.enums.Permission;
import fr.maxlego08.head.zcore.utils.PlayerSkin;
import fr.maxlego08.head.zcore.utils.commands.CommandType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

/**
 * Like /skull, but the skin is saved inside the head and does not change anymore
 */
public class CommandPlayerHeadStatic extends VCommand {

    public CommandPlayerHeadStatic(HeadPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.HEAD_PLAYER_HEAD_STATIC);
        this.onlyPlayers();
        this.addRequireArg("player");
    }

    @Override
    protected CommandType perform(HeadPlugin plugin) {

        // The command object is shared, so we keep the values before going async
        Player player = this.player;
        String name = this.argAsString(0);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(name);
            String playerName = offlinePlayer.getName() == null ? name : offlinePlayer.getName();
            String skinUrl = PlayerSkin.getSkinUrl(offlinePlayer);

            Bukkit.getScheduler().runTask(plugin, () -> {
                if (skinUrl == null) {
                    message(player, Message.SKIN_NOT_FOUND, "%name%", playerName);
                    return;
                }
                give(player, staticPlayerHead(playerName, skinUrl));
                message(player, Message.GIVE_HEAD, "%name%", playerName);
            });
        });

        return CommandType.SUCCESS;
    }

}
