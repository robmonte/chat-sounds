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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
	private static final String CS_CLAN_MSG = "To talk in your clan's channel, start each line of chat with // or /c.";
	private static final String CS_GIM_MSG = "To talk in your Ironman Group's channel, start each line of chat with //// or /g.";
	private static final String CS_CLAN_GUEST_MSG = "Attempting to reconnect to guest channel automatically...";
	private static final Pattern CS_CLAN_GUEST_PATTERN = Pattern.compile("You are now a guest of [0-9A-Za-z ]*.<br>To talk, start each line of chat with \\/\\/\\/ or \\/gc.");
	private static final File CS_DIR = new File(RuneLite.RUNELITE_DIR.getPath() + File.separator + "chat-sounds");
	private static final File CS_DEFAULT = new File(CS_DIR, "cs_default.wav");
	private static final File CS_PUBLIC = new File(CS_DIR, "cs_public.wav");
	private static final File CS_PRIVATE = new File(CS_DIR, "cs_private.wav");
	private static final File CS_CHAT_CHANNEL = new File(CS_DIR,"cs_chat_channel.wav");
	private static final File CS_CLAN = new File(CS_DIR, "cs_clan.wav");
	private static final File CS_CLAN_BROADCAST = new File(CS_DIR, "cs_clan_broadcast.wav");
	private static final File CS_GIM = new File(CS_DIR, "cs_gim.wav");
	private static final File CS_GIM_BROADCAST = new File(CS_DIR, "cs_gim_broadcast.wav");
	private static final File CS_CLAN_GUEST = new File(CS_DIR, "cs_clan_guest.wav");
	private static final File CS_CLAN_GUEST_SYSTEM = new File(CS_DIR, "cs_clan_guest_system.wav");
	private static final File CS_TRADE_REQUEST = new File(CS_DIR, "cs_trade_req.wav");
	private static final File CS_DUEL_REQUEST = new File(CS_DIR, "cs_duel_req.wav");
	private static final File[] CS_FILES = new File[]{
			CS_DEFAULT,
			CS_PUBLIC,
			CS_PRIVATE,
			CS_CHAT_CHANNEL,
			CS_CLAN,
			CS_CLAN_BROADCAST,
			CS_GIM,
			CS_GIM_BROADCAST,
			CS_CLAN_GUEST,
			CS_CLAN_GUEST_SYSTEM,
			CS_TRADE_REQUEST,
			CS_DUEL_REQUEST
	};
	private final AudioPlayer audioPlayer = new AudioPlayer();

	private List<String> ignoredPlayers = new CopyOnWriteArrayList<>();

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
		ignoredPlayers = null;
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage)
	{
		Player player = client.getLocalPlayer();
		String cleanName = Text.sanitize(chatMessage.getName());
		if (player == null ||
			client.getGameState() != GameState.LOGGED_IN ||
			cleanName.equals(player.getName()) ||
			ignoredPlayers.contains(cleanName))
		{
			return;
		}

		ChatMessageType type = chatMessage.getType();
		String msg = chatMessage.getMessage();
		switch (type)
		{
			case MODCHAT:
			case PUBLICCHAT:
				playSound(config.publicChat(), CS_PUBLIC);
				break;
			case MODPRIVATECHAT:
			case PRIVATECHAT:
				playSound(config.privateChat(), CS_PRIVATE);
				break;
			case FRIENDSCHAT:
				playSound(config.chatChannel(), CS_CHAT_CHANNEL);
				break;
			case CLAN_CHAT:
				playSound(config.clanChat(), CS_CLAN);
				break;
			case CLAN_MESSAGE:
				if (!msg.equals(CS_CLAN_MSG)) {
					playSound(config.clanBroadcast(), CS_CLAN_BROADCAST);
				}
				break;
			case CLAN_GIM_CHAT:
				playSound(config.gimChat(), CS_GIM);
				break;
			case CLAN_GIM_MESSAGE:
				if (!msg.equals(CS_GIM_MSG)) {
					playSound(config.gimBroadcast(), CS_CLAN_BROADCAST);
				}
				break;
			case CLAN_GUEST_CHAT:
				playSound(config.clanGuestChat(), CS_CLAN_GUEST);
				break;
			case CLAN_GUEST_MESSAGE:
				Matcher m = CS_CLAN_GUEST_PATTERN.matcher(msg);
				if (m.find() || msg.equals(CS_CLAN_GUEST_MSG))
				{
					break;
				}
				playSound(config.clanGuestSystemMessage(), CS_CLAN_GUEST_SYSTEM);
				break;
			case TRADEREQ:
				playSound(config.tradeRequest(), CS_TRADE_REQUEST);
				break;
			case CHALREQ_TRADE:
				playSound(config.duelRequest(), CS_DUEL_REQUEST);
				break;
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged configChanged)
	{
		if (configChanged.getGroup().equals(ChatSoundsConfig.GROUP) && configChanged.getKey().equals(ChatSoundsConfig.PLAYER_IGNORE_LIST))
		{
			updateLists();
		}
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

	/*
	private void playSound(ChatSoundsMode mode, File f)
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
				mp3Player.setVolume(config.volume());
				mp3Player.play();
			});
	}
	 */

	private void playSound(ChatSoundsMode mode, File f)
	{
		if (mode == ChatSoundsMode.OFF || !f.exists())
		{
			return;
		}

		try
		{
			f = mode.equals(ChatSoundsMode.DEFAULT) ? CS_DEFAULT : f;
			audioPlayer.play(f, linearTodB(config.volume()));
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
		ignoredPlayers = Text.fromCSV(config.playerIgnoreList());
	}

	@Provides
	ChatSoundsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ChatSoundsConfig.class);
	}
}
