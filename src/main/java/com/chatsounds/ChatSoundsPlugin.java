package com.chatsounds;

import com.google.inject.Provides;
import jaco.mp3.player.MP3Player;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

@Slf4j
@PluginDescriptor(
	name = "Chat Sounds"
)
public class ChatSoundsPlugin extends Plugin
{
	private static final File CS_DIR = new File(RuneLite.RUNELITE_DIR.getPath() + File.separator + "chat-sounds");
	private static final File CS_DEFAULT = new File(CS_DIR, "cs_default.mp3");
	private static final File CS_PUBLIC = new File(CS_DIR, "cs_public.mp3");
	private static final File CS_PRIVATE = new File(CS_DIR, "cs_private.mp3");
	private static final File CS_CHAT_CHANNEL = new File(CS_DIR,"cs_chat_channel.mp3");
	private static final File CS_CLAN = new File(CS_DIR, "cs_clan.mp3");
	private static final File CS_CLAN_BROADCAST = new File(CS_DIR, "cs_clan_broadcast.mp3");
	private static final File[] CS_FILES = new File[]{
		CS_DEFAULT,
		CS_PUBLIC,
		CS_PRIVATE,
		CS_CHAT_CHANNEL,
		CS_CLAN,
		CS_CLAN_BROADCAST
	};
	private final URL SOUND = this.getClass().getResource("/cs_default.mp3");

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
		if (player == null ||
			client.getGameState() != GameState.LOGGED_IN ||
			event.getName().equals(player.getName()))
		{
			return;
		}

		ChatMessageType type = event.getType();
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
				playSound(config.clanBroadcast(), CS_CLAN_BROADCAST);
				break;
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
				Path src = Paths.get(SOUND.toURI());
				Path dest = Paths.get(f.getPath());
				Files.copy(src, dest);
			} catch (IOException e)
			{
				log.debug("ChatSoundsPlugin - Unhandled IO: " + f);
			} catch (URISyntaxException u)
			{
				log.debug("ChatSoundsPlugin - Unhandled URISyntax: " + f);
			}
		}
	}

	private void playSound(ChatSoundsMode mode, File f)
	{
		if (mode == ChatSoundsMode.OFF)
		{
			return;
		}

		//this is invoked later on the Event Dispatch Thread due to MP3Player's initialization
		SwingUtilities.invokeLater(() ->
			{
				MP3Player mp3Player = new MP3Player(CS_DEFAULT);
				if (mode == ChatSoundsMode.DEFAULT)
				{
					mp3Player.setVolume(config.volume());
					mp3Player.play();
				} else if (mode == ChatSoundsMode.CUSTOM)
				{
					mp3Player = new MP3Player(f);
					mp3Player.setVolume(config.volume());
					mp3Player.play();
				}
			});
	}

	@Provides
	ChatSoundsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ChatSoundsConfig.class);
	}
}
