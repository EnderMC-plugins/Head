package fr.maxlego08.head.command.commands;

import fr.maxlego08.head.HeadPlugin;
import fr.maxlego08.head.command.VCommand;
import fr.maxlego08.head.zcore.enums.EnumInventory;
import fr.maxlego08.head.zcore.enums.Message;
import fr.maxlego08.head.zcore.enums.Permission;
import fr.maxlego08.head.zcore.utils.commands.CommandType;

public class CommandHeadPlayers extends VCommand {

    public CommandHeadPlayers(HeadPlugin plugin) {
        super(plugin);
        this.setPermission(Permission.HEAD_PLAYERS);
        this.addSubCommand("players");
        this.setDescription(Message.DESCRIPTION_PLAYERS);
        this.onlyPlayers();
    }

    @Override
    protected CommandType perform(HeadPlugin plugin) {

        createInventory(plugin, this.player, EnumInventory.PLAYERS, 1, true);
        return CommandType.SUCCESS;
    }

}
