package me.glicz.inventoryapi.nms.v1_18_R1;

import me.glicz.inventoryapi.nms.NativeVersion;
import me.glicz.inventoryapi.nms.v1_17_R1.v1_17_R1_NMS;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@NativeVersion(major = 1, minor = 18)
public class v1_18_R1_NMS extends v1_17_R1_NMS {
    public v1_18_R1_NMS(JavaPlugin plugin) {
        super(plugin);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected ServerPlayer getServerPlayer(Player player) {
        return MinecraftServer.getServer().getPlayerList().getPlayer(player.getUniqueId());
    }

    @Override
    protected void sendPacket(Player player, Object packetObj) {
        if (!(packetObj instanceof Packet<?> packet))
            throw new IllegalArgumentException("Provided packetObj should be an instance of Packet class!");
        getConnection(player).send(packet);
    }
}
