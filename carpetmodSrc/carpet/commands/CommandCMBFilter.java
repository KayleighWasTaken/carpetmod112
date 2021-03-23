package carpet.commands;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

import carpet.CarpetSettings;
import carpet.helpers.CMBFilter;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandCMBFilter extends CommandCarpetBase
{
    public String getUsage(ICommandSender sender) {
        return "Usage: cmbfilter add/remove <x> <y> <z>; cmbfilter enable/disale";
    }

    public String getName() {
        return "cmbfilter";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (!command_enabled("CMBFilter", sender)
                || sender.getCommandSenderEntity() == null
                || !(sender.getCommandSenderEntity() instanceof EntityPlayer)) {
            return;
        }

        if (args.length < 1) {
            throw new WrongUsageException(getUsage(sender));
        }

        EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
        switch (args[0]) {
            case "add":
            case "remove":
                if (args.length != 4) {
                    throw new WrongUsageException(getUsage(sender));
                }

                BlockPos blockPos = parseBlockPos(sender, args, 1, false);

                if ("add".equalsIgnoreCase(args[0])) {
                    if (CMBFilter.addLocation(player, blockPos)) {
                        msg(sender, new TextComponentString("Added location"));
                        return;
                    }
                    msg(sender, new TextComponentString("Location already exist"));
                } else {
                    if (CMBFilter.removeLocation(player, blockPos)) {
                        msg(sender, new TextComponentString("Removed location"));
                        return;
                    }
                    msg(sender, new TextComponentString("Location doesn't exist"));
                }
                break;
            case "enable":
                if (CMBFilter.enableForPlayer(player)) {
                    msg(sender, new TextComponentString("Enabled command block output filtering"));
                    return;
                }
                msg(sender, new TextComponentString("Command block filtering is already enabled"));
                break;
            case "disable":
                if (CMBFilter.disableForPlayer(player)) {
                    msg(sender, new TextComponentString("Disabled command block output filtering"));
                    return;
                }
                msg(sender, new TextComponentString("Command block output filtering is already disabled"));
                break;
            default:
                throw new WrongUsageException(getUsage(sender));
        }
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        if (!CarpetSettings.CMBFilter)
        {
            return Collections.<String>emptyList();
        }

        if (args.length == 1) {
            return getListOfStringsMatchingLastWord(args, new String[] {"add", "remove", "disable", "enable"});
        } else if (args.length > 1 && args.length <= 4) {
            if ("add".equalsIgnoreCase(args[0]) || "remove".equalsIgnoreCase(args[0])){
                return getTabCompletionCoordinate(args, 1, pos);
            }
        }
        return Collections.<String>emptyList();
    }
}
