package com.tristian.necronbossfight.utils;


import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;
import net.minecraft.server.v1_7_R4.ChatSerializer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.spigotmc.ProtocolInjector;
import org.bukkit.entity.Player;

public class TitleAPI
{
    private static Class<?> packetClass;
    private static Class<?> nmsChatSerializer;
    private static int VERSION;

    public static void sendTitle(final Player p, final String title, final TitleColor color) {
        if (getVersion(p) < VERSION) {
            return;
        }
        final String raw = String.format("{\"text\":\"\",\"extra\":[{\"text\":\"%s\",\"color\":\"%s\"}]}", title, color.getName());
        try {
            final Object handle = ReflectionUtils.getNMSHandle(p);
            final Object connection = ReflectionUtils.getField(handle.getClass(), "playerConnection").get(handle);
            final Object serialized = ReflectionUtils.getMethod(nmsChatSerializer, "a", String.class).invoke(null, raw);
            final Object packet = ProtocolInjector.PacketTitle.class.getConstructor(ProtocolInjector.PacketTitle.Action.class, ReflectionUtils.getNMSClass("IChatBaseComponent")).newInstance(ProtocolInjector.PacketTitle.Action.TITLE, serialized);
            ReflectionUtils.getMethod(connection.getClass(), "sendPacket", packetClass).invoke(connection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendSubTitle(final Player p, final String subtitle, final TitleColor color) {
        if (getVersion(p) < VERSION) {
            return;
        }
        final String raw = String.format("{\"text\":\"\",\"extra\":[{\"text\":\"%s\",\"color\":\"%s\"}]}", subtitle, color.getName());
        try {
            final Object handle = ReflectionUtils.getNMSHandle(p);
            final Object connection = ReflectionUtils.getField(handle.getClass(), "playerConnection").get(handle);
            final Object serialized = ReflectionUtils.getMethod(nmsChatSerializer, "a", String.class).invoke(null, raw);
            final Object packet = ProtocolInjector.PacketTitle.class.getConstructor(ProtocolInjector.PacketTitle.Action.class, ReflectionUtils.getNMSClass("IChatBaseComponent")).newInstance(ProtocolInjector.PacketTitle.Action.SUBTITLE, serialized);
            ReflectionUtils.getMethod(connection.getClass(), "sendPacket", packetClass).invoke(connection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendTimings(final Player p, final int fadeIn, final int stay, final int fadeOut) {
        if (getVersion(p) < VERSION) {
            return;
        }
        try {
            final Object handle = ReflectionUtils.getNMSHandle(p);
            final Object connection = ReflectionUtils.getField(handle.getClass(), "playerConnection").get(handle);
            final Object packet = ProtocolInjector.PacketTitle.class.getConstructor(ProtocolInjector.PacketTitle.Action.class, Integer.TYPE, Integer.TYPE, Integer.TYPE).newInstance(ProtocolInjector.PacketTitle.Action.TIMES, fadeIn, stay, fadeOut);
            ReflectionUtils.getMethod(connection.getClass(), "sendPacket", packetClass).invoke(connection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendActionBar(final Player player, final String message) {
        final CraftPlayer p = (CraftPlayer)player;
        if (p.getHandle().playerConnection.networkManager.getVersion() != 47) {
            return;
        }
        final IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + message + "\"}");
        final PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, true);
        p.getHandle().playerConnection.sendPacket((Packet)ppoc);
    }

    public static void reset(final Player p) {
        if (getVersion(p) < VERSION) {
            return;
        }
        try {
            final Object handle = ReflectionUtils.getNMSHandle(p);
            final Object connection = ReflectionUtils.getField(handle.getClass(), "playerConnection").get(handle);
            final Object packet = ProtocolInjector.PacketTitle.class.getConstructor(ProtocolInjector.PacketTitle.Action.class).newInstance(ProtocolInjector.PacketTitle.Action.RESET);
            ReflectionUtils.getMethod(connection.getClass(), "sendPacket", packetClass).invoke(connection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void clear(final Player p) {
        if (getVersion(p) < VERSION) {
            return;
        }
        try {
            final Object handle = ReflectionUtils.getNMSHandle(p);
            final Object connection = ReflectionUtils.getField(handle.getClass(), "playerConnection").get(handle);
            final Object packet = ProtocolInjector.PacketTitle.class.getConstructor(ProtocolInjector.PacketTitle.Action.class).newInstance(ProtocolInjector.PacketTitle.Action.CLEAR);
            ReflectionUtils.getMethod(connection.getClass(), "sendPacket", packetClass).invoke(connection, packet);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getVersion(final Player p) {
        try {
            final Object handle = ReflectionUtils.getNMSHandle(p);
            final Object connection = ReflectionUtils.getField(handle.getClass(), "playerConnection").get(handle);
            final Object network = ReflectionUtils.getField(connection.getClass(), "networkManager").get(connection);
            final Object version = ReflectionUtils.getMethod(network.getClass(), "getVersion").invoke(network, new Object[0]);
            return (int)version;
        }
        catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    static {
        packetClass = ReflectionUtils.getNMSClass("Packet");
        nmsChatSerializer = ReflectionUtils.getNMSClass("ChatSerializer");
        VERSION = 47;
    }

    public enum TitleColor
    {
        RED,
        GREEN,
        BLUE,
        YELLOW,
        GOLD,
        AQUA,
        GRAY;

        public String getName() {
            return this.name().toLowerCase();
        }
    }
}


