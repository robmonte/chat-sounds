package com.chatsounds;

import com.google.inject.Provides;
//import jaco.mp3.player.MP3Player;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.inject.Inject;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
//import javax.swing.SwingUtilities;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.RuneLite;
import net.runelite.client.audio.AudioPlayer;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.util.Text;

@Slf4j
@PluginDescriptor(
	name = "Chat Sounds"
)
public class ChatSoundsPlugin extends Plugin
{
	private static final String HAS_JOINED = " has joined.".toLowerCase();;
	private static final String HAS_LEFT = " has left.".toLowerCase();;
	private static final String CS_CHAT_CHANNEL_MSG_1 = "Attempting to join chat-channel...".toLowerCase();
	private static final String CS_CHAT_CHANNEL_MSG_2 = "Now talking in chat-channel ".toLowerCase();
	private static final String CS_CHAT_CHANNEL_MSG_3 = "To talk, start each line of chat with the / symbol.".toLowerCase();
	private static final String CS_CLAN_MSG = "To talk in your clan's channel, start each line of chat with // or /c.".toLowerCase();
	private static final String CS_CLAN_GUEST_MSG_1 = "You are now a guest of ".toLowerCase();
	private static final String CS_CLAN_GUEST_MSG_2 = "To talk, start each line of chat with /// or /gc.".toLowerCase();
	private static final String CS_CLAN_GUEST_MSG_3 = "Attempting to reconnect to guest channel automatically...";
	private static final String CS_GIM_MSG = "To talk in your Ironman Group's channel, start each line of chat with //// or /g.".toLowerCase();

	private static final File CS_DIR = new File(RuneLite.RUNELITE_DIR.getPath() + File.separator + "chat-sounds");
	private static final File CS_DEFAULT = new File(CS_DIR, "cs_default.wav");
	private static final File CS_PUBLIC = new File(CS_DIR, "cs_public.wav");
	private static final File CS_PRIVATE = new File(CS_DIR, "cs_private.wav");
	private static final File CS_CHAT_CHANNEL = new File(CS_DIR,"cs_chat_channel.wav");
	private static final File CS_CHAT_CHANNEL_BROADCAST = new File(CS_DIR, "cs_chat_channel_broadcast.wave");
	private static final File CS_CLAN = new File(CS_DIR, "cs_clan.wav");
	private static final File CS_CLAN_BROADCAST = new File(CS_DIR, "cs_clan_broadcast.wav");
	private static final File CS_CLAN_GUEST = new File(CS_DIR, "cs_clan_guest.wav");
	private static final File CS_CLAN_GUEST_BROADCAST = new File(CS_DIR, "cs_clan_guest_broadcast.wav");
	private static final File CS_GIM = new File(CS_DIR, "cs_gim.wav");
	private static final File CS_GIM_BROADCAST = new File(CS_DIR, "cs_gim_broadcast.wav");
	private static final File CS_TRADE_REQUEST = new File(CS_DIR, "cs_trade_req.wav");
	private static final File CS_DUEL_REQUEST = new File(CS_DIR, "cs_duel_req.wav");
	private static final File[] CS_FILES = new File[]{
		CS_DEFAULT,
		CS_PUBLIC,
		CS_PRIVATE,
		CS_CHAT_CHANNEL,
		CS_CHAT_CHANNEL_BROADCAST,
		CS_CLAN,
		CS_CLAN_BROADCAST,
		CS_CLAN_GUEST,
		CS_CLAN_GUEST_BROADCAST,
		CS_GIM,
		CS_GIM_BROADCAST,
		CS_TRADE_REQUEST,
		CS_DUEL_REQUEST
	};

	private final AudioPlayer audioPlayer = new AudioPlayer();
	private List<String> publicIgnored = new CopyOnWriteArrayList<>();
	private List<String> privateIgnored = new CopyOnWriteArrayList<>();
	private List<String> chatChannelIgnored = new CopyOnWriteArrayList<>();
	private List<String> clanIgnored = new CopyOnWriteArrayList<>();
	private List<String> guestClanIgnored = new CopyOnWriteArrayList<>();
	private List<String> gimIgnored = new CopyOnWriteArrayList<>();
	private List<String> tradeIgnored = new CopyOnWriteArrayList<>();
	private List<String> duelIgnored = new CopyOnWriteArrayList<>();

	@Inject
	private Client client;

	@Inject
	private ChatSoundsConfig config;

	@Override
	protected void startUp()
	{
		initSoundFiles();
		updateLists();
	}

	@Override
	protected void shutDown()
	{
		publicIgnored = null;
		privateIgnored = null;
		chatChannelIgnored = null;
		clanIgnored = null;
		guestClanIgnored = null;
		gimIgnored = null;
		tradeIgnored = null;
		duelIgnored = null;
	}


	@Subscribe
	public void onChatMessage(ChatMessage chatMessage)
	{
		Player player = client.getLocalPlayer();
		String playerName = player.getName() != null ? player.getName() : "";
		String cleanName = Text.sanitize(chatMessage.getName());
		if (player == null ||
				client.getGameState() != GameState.LOGGED_IN ||
				cleanName.equalsIgnoreCase(Text.sanitize(playerName))) {
			return;
		}

		ChatMessageType type = chatMessage.getType();
		String msg = Text.standardize(chatMessage.getMessage());
		switch (type) {
			case MODCHAT:
			case PUBLICCHAT:
				if (shouldIgnorePlayer(publicIgnored, cleanName)) {
					return;
				}
				playSound(config.publicChat(), CS_PUBLIC, config.publicVolume());
				break;

			case MODPRIVATECHAT:
			case PRIVATECHAT:
				if (shouldIgnorePlayer(privateIgnored, cleanName)) {
					return;
				}
				playSound(config.privateChat(), CS_PRIVATE, config.privateVolume());
				break;

			case FRIENDSCHAT:
				if (shouldIgnorePlayer(chatChannelIgnored, cleanName)) {
					return;
				}
				playSound(config.chatChannel(), CS_CHAT_CHANNEL, config.chatChannelVolume());
				break;

			case FRIENDSCHATNOTIFICATION:
				if (shouldAlertOnJoinOrLeft(msg, config.chatChannelIgnoreJoinLeave()) &&
						!msg.equals(CS_CHAT_CHANNEL_MSG_1) && !msg.startsWith(CS_CHAT_CHANNEL_MSG_2) &&
						!msg.equals(CS_CHAT_CHANNEL_MSG_3)) {
					playSound(config.chatChannelBroadcast(), CS_CHAT_CHANNEL_BROADCAST, config.chatChannelVolume());
				}
				break;

			case CLAN_CHAT:
				if (shouldIgnorePlayer(clanIgnored, cleanName)) {
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
				if (shouldIgnorePlayer(guestClanIgnored, cleanName)) {
					return;
				}
				playSound(config.guestClanChat(), CS_CLAN_GUEST, config.guestClanVolume());
				break;

			case CLAN_GUEST_MESSAGE:
				if (shouldAlertOnJoinOrLeft(msg, config.guestClanIgnoreJoinLeave()) &&
						!msg.startsWith(CS_CLAN_GUEST_MSG_1) && !msg.endsWith(CS_CLAN_GUEST_MSG_2) &&
						!msg.equals(CS_CLAN_GUEST_MSG_3)) {
					playSound(config.guestClanBroadcast(), CS_CLAN_GUEST_BROADCAST, config.guestClanVolume());
				}
				break;

			case CLAN_GIM_CHAT:
				if (shouldIgnorePlayer(gimIgnored, cleanName)) {
					return;
				}
				playSound(config.gimChat(), CS_GIM, config.groupIronVolume());
				break;

			case CLAN_GIM_MESSAGE:
				if (!msg.equals(CS_GIM_MSG)) {
					playSound(config.gimBroadcast(), CS_GIM_BROADCAST, config.groupIronVolume());
				}
				break;

			case TRADEREQ:
				if (shouldIgnorePlayer(tradeIgnored, cleanName)) {
					return;
				}
				playSound(config.tradeRequest(), CS_TRADE_REQUEST, config.tradeVolume());
				break;

			case CHALREQ_TRADE:
				if (shouldIgnorePlayer(duelIgnored, cleanName)) {
					return;
				}
				playSound(config.duelRequest(), CS_DUEL_REQUEST, config.duelVolume());
				break;
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged configChanged)
	{
		if (configChanged.getGroup().equals("chatSounds"))
		{
			updateLists();
		}
	}

	// Returns true if the message is from an ignored player in the chat's type.
	private boolean shouldIgnorePlayer(List<String> ignoreList, String name) {
        return ignoreList.contains(name.toLowerCase());
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
			CS_DIR.mkdirs();
		}

		for (File f : CS_FILES)
		{
			try
			{
				if (f.exists()) {
					continue;
				}
				InputStream stream = ChatSoundsPlugin.class.getClassLoader().getResourceAsStream("cs_default.wav");
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

		try
		{
			f = mode.equals(ChatSoundsMode.DEFAULT) ? CS_DEFAULT : f;
			audioPlayer.play(f, linearTodB(volume));
		}
		catch (LineUnavailableException | UnsupportedAudioFileException | IOException e)
		{
			log.warn("ChatSoundsPlugin::playSound() error!", e);
		}
	}

	private float linearTodB(int linearVolume)
	{
		return 20.0f * (float) Math.log10(linearVolume/100.0f);
	}

	private void updateLists()
	{
		publicIgnored = Text.fromCSV(config.publicIgnorePlayersList());
		privateIgnored = Text.fromCSV(config.privateIgnorePlayersList());
		chatChannelIgnored = Text.fromCSV(config.chatChannelIgnorePlayersList());
		clanIgnored = Text.fromCSV(config.clanIgnorePlayersList());
		guestClanIgnored = Text.fromCSV(config.guestClanIgnorePlayersList());
		gimIgnored = Text.fromCSV(config.groupIronIgnorePlayersList());
		tradeIgnored = Text.fromCSV(config.tradeIgnorePlayersList());
		duelIgnored = Text.fromCSV(config.duelIgnorePlayersList());
	}

	@Provides
	ChatSoundsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ChatSoundsConfig.class);
	}
}
