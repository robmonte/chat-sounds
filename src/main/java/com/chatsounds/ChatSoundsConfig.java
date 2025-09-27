package com.chatsounds;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup(ChatSoundsConfig.GROUP)
public interface ChatSoundsConfig extends Config
{
	String GROUP = "chatSounds";
	String PLAYER_IGNORE_LIST = "playerIgnoreList";
	@Range(
			max = 100
	)
	@Units(
			value = "%"
	)
	@ConfigItem(
			keyName = "volume",
			name = "Volume",
			description = "Sets the volume of the chat message sound effect.",
			position = 1
	)
	default int volume()
	{
		return 100;
	}

	@ConfigItem(
		keyName = "publicChat",
		name = "Public Chat",
		description = "The sound effect to use when receiving a public chat message.",
		position = 2
	)
	default ChatSoundsMode publicChat()
	{
		return ChatSoundsMode.OFF;
	}

	@ConfigItem(
		keyName = "privateChat",
		name = "Private Chat",
		description = "The sound effect to use when receiving a private chat message.",
		position = 3
	)
	default ChatSoundsMode privateChat()
	{
		return ChatSoundsMode.DEFAULT;
	}

	@ConfigItem(
		keyName = "chatChannel",
		name = "Chat-channel",
		description = "The sound effect to use when receiving a chat-channel chat message.",
		position = 4
	)
	default ChatSoundsMode chatChannel()
	{
		return ChatSoundsMode.OFF;
	}

	@ConfigItem(
		keyName = "clanChat",
		name = "Clan Chat",
		description = "The sound effect to use when receiving a clan chat message.",
		position = 5
	)
	default ChatSoundsMode clanChat()
	{
		return ChatSoundsMode.OFF;
	}

	@ConfigItem(
		keyName = "clanBroadcast",
		name = "Clan Broadcast",
		description = "The sound effect to use when receiving a clan broadcast message.",
		position = 6
	)
	default ChatSoundsMode clanBroadcast()
	{
		return ChatSoundsMode.OFF;
	}

	@ConfigItem(
			keyName = "gimChat",
			name = "Group Ironman Chat",
			description = "The sound effect to use when receiving a group ironman chat message.",
			position = 7
	)
	default ChatSoundsMode gimChat()
	{
		return ChatSoundsMode.OFF;
	}

	@ConfigItem(
			keyName = "gimBroadcast",
			name = "Group Ironman Broadcast",
			description = "The sound effect to use when receiving a group ironman broadcast message.",
			position = 8
	)
	default ChatSoundsMode gimBroadcast()
	{
		return ChatSoundsMode.OFF;
	}

	@ConfigItem(
			keyName = "clanGuestChat",
			name = "Clan Guest Chat",
			description = "The sound effect to use when receiving a clan guest chat message.",
			position = 9
	)
	default ChatSoundsMode clanGuestChat()
	{
		return ChatSoundsMode.OFF;
	}

	@ConfigItem(
			keyName = "clanGuestSystemMessage",
			name = "Clan Guest System Message",
			description = "The sound effect to use when receiving a clan guest system message.",
			position = 10
	)
	default ChatSoundsMode clanGuestSystemMessage()
	{
		return ChatSoundsMode.OFF;
	}

	@ConfigItem(
			keyName = "tradeRequest",
			name = "Trade Request",
			description = "The sound effect to use when receiving a trade request.",
			position = 11
	)
	default ChatSoundsMode tradeRequest()
	{
		return ChatSoundsMode.OFF;
	}

	@ConfigItem(
			keyName = "duelRequest",
			name = "Duel Request",
			description = "The sound effect to use when receiving a duel request.",
			position = 12
	)
	default ChatSoundsMode duelRequest()
	{
		return ChatSoundsMode.OFF;
	}

	@ConfigItem(
			keyName = PLAYER_IGNORE_LIST,
			name = "Player ignore list",
			description = "Custom list of players to not trigger sounds for, separated by commas (,).",
			position = 13
	)
	default String playerIgnoreList()
	{
		return "";
	}
}
