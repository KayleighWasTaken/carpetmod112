package carpet.helpers;

import carpet.CarpetSettings;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.UUID;

public class CMBFilter {
    private static Map<UUID, HashSet<BlockPos>> locationMap = new HashMap<UUID, HashSet<BlockPos>>();
    private static HashSet<UUID> enabledSet = new HashSet<UUID>();

    public static Boolean addLocation (EntityPlayer player, BlockPos pos) {
        UUID uuid = player.getUniqueID();
        if (!locationMap.containsKey(uuid)) {
            locationMap.put(uuid, new HashSet<BlockPos>());
            enabledSet.add(uuid);
        }

        return locationMap.get(uuid).add(pos);
    }

    public static Boolean removeLocation (EntityPlayer player, BlockPos pos) {
        UUID uuid = player.getUniqueID();

        if (!locationMap.containsKey(uuid)) {
            return false;
        }

        return locationMap.get(uuid).remove(pos);
    }

    public static Boolean enableForPlayer (EntityPlayer player) {
        return enabledSet.add(player.getUniqueID());
    }

    public static Boolean disableForPlayer (EntityPlayer player) {
        return enabledSet.remove(player.getUniqueID());
    }

    public static Boolean sendFeedback (ICommandSender sender, EntityPlayer player) {
        UUID uuid = player.getUniqueID();
        if (!CarpetSettings.CMBFilter || !enabledSet.contains(uuid)) {
            return sender.sendCommandFeedback();
        }

        BlockPos pos = sender.getPosition();
        if (locationMap.get(uuid).contains(pos)) {
            return true;
        }

        return false;
    }
}
