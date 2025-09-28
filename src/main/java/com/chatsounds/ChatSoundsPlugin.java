package com.chatsounds;

import com.google.inject.Provides;
import jaco.mp3.player.MP3Player;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import javax.inject.Inject;
import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.RuneLite;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
	name = "Chat Sounds"
)
public class ChatSoundsPlugin extends Plugin
{
	private static final String DEFAULT_FILE_NAME = "cs_default.mp3";
	private static final String PUBLIC_FILE_NAME = "cs_public.mp3";
	private static final String PRIVATE_FILE_NAME = "cs_private.mp3";
	private static final String CHAT_CHANNEL_FILE_NAME = "cs_chat_channel.mp3";
	private static final String CLAN_FILE_NAME = "cs_clan.mp3";
	private static final String CLAN_BROADCAST_FILE_NAME = "cs_clan_broadcast.mp3";
	private static final String CLAN_GUEST_FILE_NAME = "cs_clan_guest.mp3";
	private static final String CLAN_GUEST_BROADCAST_FILE_NAME = "cs_clan_guest_broadcast.mp3";
	private static final String GIM_FILE_NAME = "cs_gim.mp3";
	private static final String GIM_BROADCAST_FILE_NAME = "cs_gim_broadcast.mp3";

	private static final String HAS_JOINED = " has joined.";
	private static final String HAS_LEFT = " has left.";
	private static final String CS_CHAT_CHANNEL_MSG_1 = "Attempting to join chat-channel...".toLowerCase();
	private static final String CS_CHAT_CHANNEL_MSG_2 = "Now talking in chat-channel ".toLowerCase();
	private static final String CS_CHAT_CHANNEL_MSG_3 = "To talk, start each line of chat with the / symbol.".toLowerCase();
	private static final String CS_CLAN_MSG = "To talk in your clan's channel, start each line of chat with // or /c.".toLowerCase();
	private static final String CS_CLAN_GUEST_MSG_1 = "You are now a guest of ".toLowerCase();
	private static final String CS_CLAN_GUEST_MSG_2 = "To talk, start each line of chat with /// or /gc.".toLowerCase();
	private static final String CS_GIM_MSG = "To talk in your Ironman Group's channel, start each line of chat with //// or /g.".toLowerCase();

	private static final File CS_DIR = new File(RuneLite.RUNELITE_DIR.getPath() + File.separator + "chat-sounds");
	private static final File CS_DEFAULT = new File(CS_DIR, DEFAULT_FILE_NAME);
	private static final File CS_PUBLIC = new File(CS_DIR, PUBLIC_FILE_NAME);
	private static final File CS_PRIVATE = new File(CS_DIR, PRIVATE_FILE_NAME);
	private static final File CS_CHAT_CHANNEL = new File(CS_DIR, CHAT_CHANNEL_FILE_NAME);
	private static final File CS_CLAN = new File(CS_DIR, CLAN_FILE_NAME);
	private static final File CS_CLAN_BROADCAST = new File(CS_DIR, CLAN_BROADCAST_FILE_NAME);
	private static final File CS_CLAN_GUEST = new File(CS_DIR, CLAN_GUEST_FILE_NAME);
	private static final File CS_CLAN_GUEST_BROADCAST = new File(CS_DIR, CLAN_GUEST_BROADCAST_FILE_NAME);
	private static final File CS_GIM = new File(CS_DIR, GIM_FILE_NAME);
	private static final File CS_GIM_BROADCAST = new File(CS_DIR, GIM_BROADCAST_FILE_NAME);
	private static final File[] CS_FILES = new File[]{
		CS_DEFAULT,
		CS_PUBLIC,
		CS_PRIVATE,
		CS_CHAT_CHANNEL,
		CS_CLAN,
		CS_CLAN_BROADCAST,
		CS_CLAN_GUEST,
		CS_CLAN_GUEST_BROADCAST,
		CS_GIM,
		CS_GIM_BROADCAST
	};

	@Inject
	private Client client;

	@Inject
	private ChatSoundsConfig config;

	@Override
	protected void startUp()
	{
		initSoundFiles();
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		Player player = client.getLocalPlayer();
		String playerName = player.getName() != null ? player.getName() : "";
		String eventName = Text.sanitize(event.getName());
		if (player == null || client.getGameState() != GameState.LOGGED_IN ||
				eventName.equalsIgnoreCase(Text.sanitize(playerName))) {
			return;
		}

		String msg = Text.standardize(event.getMessage());
		ChatMessageType type = event.getType();
		switch (type) {
			case MODCHAT:
			case PUBLICCHAT:
				if (shouldAlertOnPlayer(config.publicIgnorePlayersList(), eventName)) {
					return;
				}
				playSound(config.publicChat(), CS_PUBLIC, config.publicVolume());
				break;

			case MODPRIVATECHAT:
			case PRIVATECHAT:
				if (shouldAlertOnPlayer(config.privateIgnorePlayersList(), eventName)) {
					return;
				}
				playSound(config.privateChat(), CS_PRIVATE, config.privateVolume());
				break;

			case FRIENDSCHAT:
				if (shouldAlertOnPlayer(config.chatChannelIgnorePlayersList(), eventName)) {
					return;
				}
				playSound(config.chatChannel(), CS_CHAT_CHANNEL, config.chatChannelVolume());
				break;

			case FRIENDSCHATNOTIFICATION:
				if (shouldAlertOnJoinOrLeft(msg, config.chatChannelIgnoreJoinLeave()) &&
						!msg.equals(CS_CHAT_CHANNEL_MSG_1) && !msg.startsWith(CS_CHAT_CHANNEL_MSG_2) &&
						!msg.equals(CS_CHAT_CHANNEL_MSG_3)) {
					playSound(config.chatChannel(), CS_CHAT_CHANNEL, config.chatChannelVolume());
				}
				break;

			case CLAN_CHAT:
				if (shouldAlertOnPlayer(config.clanIgnorePlayersList(), eventName)) {
					return;
				}
				playSound(config.clanChat(), CS_CLAN, config.clanVolume());
				break;

			case CLAN_MESSAGE:
				if (shouldAlertOnJoinOrLeft(msg, config.clanIgnoreJoinLeave()) && !msg.equals(CS_CLAN_MSG)) {
					playSound(config.clanBroadcast(), CS_CLAN_BROADCAST, config.clanVolume());
				}
				break;

			case CLAN_GUEST_CHAT:
				if (shouldAlertOnPlayer(config.guestClanIgnorePlayersList(), eventName)) {
					return;
				}
				playSound(config.guestClanChat(), CS_CLAN_GUEST, config.guestClanVolume());
				break;

			case CLAN_GUEST_MESSAGE:
				if (shouldAlertOnJoinOrLeft(msg, config.guestClanIgnoreJoinLeave()) &&
						!msg.startsWith(CS_CLAN_GUEST_MSG_1) && !msg.endsWith(CS_CLAN_GUEST_MSG_2)) {
					playSound(config.guestClanBroadcast(), CS_CLAN_GUEST_BROADCAST, config.guestClanVolume());
				}
				break;

			case CLAN_GIM_CHAT:
				if (shouldAlertOnPlayer(config.groupIronIgnorePlayersList(), eventName)) {
					return;
				}
				playSound(config.gimChat(), CS_GIM, config.groupIronVolume());
				break;

			case CLAN_GIM_MESSAGE:
				if (!msg.equals(CS_GIM_MSG)) {
					playSound(config.gimBroadcast(), CS_GIM_BROADCAST, config.groupIronVolume());
				}
				break;
		}
	}

	// Returns true if the message is from an ignored player in the chat's type.
	private boolean shouldAlertOnPlayer(String ignoreList, String eventName) {
		List<String> ignorePlayersList = Text.fromCSV(ignoreList.trim().toLowerCase());

        return ignorePlayersList.contains(eventName.toLowerCase());
    }

	// Returns false if it is not a "join" or "left" message. If it is one, returns true if ignore is
	// not checked, false if it is checked.
	private boolean shouldAlertOnJoinOrLeft(String text, boolean ignore) {
		if (!text.endsWith(HAS_JOINED) && !text.endsWith(HAS_LEFT)) {
			return false;
		}

        return !ignore;
    }

	private void initSoundFiles()
	{
		if (!CS_DIR.exists())
		{
			boolean dirsResult = CS_DIR.mkdirs();
			if (!dirsResult) {
				log.info("Failed to create directory " + CS_DIR);
			}
		}

		for (File f : CS_FILES)
		{
			try
			{
				if (f.exists()) {
					continue;
				}
				InputStream stream = ChatSoundsPlugin.class.getClassLoader().getResourceAsStream(DEFAULT_FILE_NAME);
				OutputStream out = new FileOutputStream(f);
				byte[] buffer = new byte[8 * 1024];
				int bytesRead;
				while ((bytesRead = stream.read(buffer)) != -1) {
					out.write(buffer, 0, bytesRead);
				}
				out.close();
				stream.close();
			}  catch (Exception e) {
				log.info("ChatSoundsPlugin - " + e + ": " + f);
			}
		}
	}

	private void playSound(ChatSoundsMode mode, File f, int volume)
	{
		if (mode == ChatSoundsMode.OFF || !f.exists())
		{
			return;
		}

		//this is invoked later on the Event Dispatch Thread due to MP3Player's initialization
		SwingUtilities.invokeLater(() ->
			{
				MP3Player mp3Player = new MP3Player(CS_DEFAULT);
				if (mode == ChatSoundsMode.CUSTOM)
				{
					mp3Player = new MP3Player(f);
				}
				mp3Player.setVolume(volume);
				mp3Player.play();
			});
	}

	@Provides
	ChatSoundsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ChatSoundsConfig.class);
	}
}
