package com.rylinaux.herochatbridge.bukkit.listeners;

import com.dthielke.herochat.Channel;
import com.dthielke.herochat.Herochat;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import com.rylinaux.herochatbridge.bukkit.HeroChatBridgeBukkit;
import com.rylinaux.herochatbridge.bukkit.utilities.MessageFormatter;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class HeroChatPluginMessageListener implements PluginMessageListener {

    private final HeroChatBridgeBukkit plugin;

    public HeroChatPluginMessageListener(HeroChatBridgeBukkit plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(String header, Player player, byte[] bytes) {

        if (!(header.equalsIgnoreCase(HeroChatBridgeBukkit.CHANNEL))) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);

        String channelName = in.readUTF();

        if (plugin.isIgnored(channelName)) {
            return;
        }

        Channel channel = Herochat.getChannelManager().getChannel(channelName);

        if (channel == null || !Herochat.getChannelManager().hasChannel(channelName)) {
            Bukkit.getServer().getLogger().log(Level.SEVERE, String.format("HeroChat channel %s does not exist - check your configuration.", channelName));
            return;
        }

        String playerName = in.readUTF();
        String message = in.readUTF();
        String world = in.readUTF();
        String fromServer = in.readUTF();

        channel.sendRawMessage(MessageFormatter.format(playerName, message, fromServer, world, channel));

    }

}
