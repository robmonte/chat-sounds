package com.chatsounds;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Range;
import net.runelite.client.config.Units;

@ConfigGroup("chatSounds")
public interface ChatSoundsConfig extends Config
{
	/**************
	* PUBLIC CHAT *
 	**************/
	@ConfigSection(
			name = "Public Chat",
			description = "Settings for public chat.",
			position = 0
	)
	String publicList = "publicList";

	@ConfigItem(
		keyName = "publicChat",
		name = "Public Chat",
		description = "The sound effect to use when receiving a public chat message.",
		position = 1,
		section = publicList
	)
	default ChatSoundsMode publicChat()
	{
		return ChatSoundsMode.OFF;
	}

	@Range(
			max = 100
	)
	@Units(
			value = "%"
	)
	@ConfigItem(
			keyName = "publicVolume",
			name = "Volume",
			description = "Sets the volume of the chat message sound effect.",
			position = 2,
			section = publicList
	)
	default int publicVolume()
	{
		return 100;
	}

	@ConfigItem(
			keyName = "publicIgnorePlayers",
			name = "Ignore player names",
			description = "A comma-separated list of player names to never play a sound for.",
			position = 4,
			section = publicList
	)
	default String publicIgnorePlayersList()
	{
		return "";
	}

	/***************
	* PRIVATE CHAT *
	****************/
	@ConfigSection(
			name = "Private Chat",
			description = "Settings for private chat.",
			position = 5
	)
	String privateList = "privateList";

	@ConfigItem(
		keyName = "privateChat",
		name = "Private Chat",
		description = "The sound effect to use when receiving a private chat message.",
		position = 6,
		section = privateList
	)
	default ChatSoundsMode privateChat()
	{
		return ChatSoundsMode.DEFAULT;
	}

	@Range(
			max = 100
	)
	@Units(
			value = "%"
	)
	@ConfigItem(
			keyName = "privateVolume",
			name = "Volume",
			description = "Sets the volume of the chat message sound effect.",
			position = 7,
			section = privateList
	)
	default int privateVolume()
	{
		return 100;
	}

	@ConfigItem(
			keyName = "privateIgnorePlayers",
			name = "Ignore player names",
			description = "A comma-separated list of player names to never play a sound for.",
			position = 9,
			section = privateList
	)
	default String privateIgnorePlayersList()
	{
		return "";
	}

	/****************
 	* CHAT-CHANNELS *
	****************/
	@ConfigSection(
			name = "Chat-channel",
			description = "Settings for chat-channels.",
			position = 10
	)
	String chatChannelList = "chatChannelList";

	@ConfigItem(
		keyName = "chatChannel",
		name = "Chat-channel",
		description = "The sound effect to use when receiving a chat-channel chat message.",
		position = 11,
		section = chatChannelList
	)
	default ChatSoundsMode chatChannel()
	{
		return ChatSoundsMode.OFF;
	}

	@ConfigItem(
			keyName = "chatChannelBroadcast",
			name = "Chat-channel Broadcast",
			description = "The sound effect to use when receiving a chat-channel broadcast message.",
			position = 12,
			section = chatChannelList
	)
	default ChatSoundsMode chatChannelBroadcast()
	{
		return ChatSoundsMode.OFF;
	}

	@Range(
			max = 100
	)
	@Units(
			value = "%"
	)
	@ConfigItem(
			keyName = "chatChannelVolume",
			name = "Volume",
			description = "Sets the volume of the chat message sound effect.",
			position = 13,
			section = chatChannelList
	)
	default int chatChannelVolume()
	{
		return 100;
	}

	@ConfigItem(
			keyName = "chatChannelIgnoreJoinLeave",
			name = "Ignore joined/left messages",
			description = "Do not play a sound for users joining or leaving chats.",
			position = 14,
			section = chatChannelList
	)
	default boolean chatChannelIgnoreJoinLeave()
	{
		return false;
	}

	@ConfigItem(
			keyName = "chatChannelIgnorePlayers",
			name = "Ignore player names",
			description = "A comma-separated list of player names to never play a sound for.",
			position = 15,
			section = chatChannelList
	)
	default String chatChannelIgnorePlayersList()
	{
		return "";
	}

	/*************
 	* CLAN CHATS *
 	*************/
	@ConfigSection(
			name = "Clan Chat",
			description = "Settings for clan chats.",
			position = 16
	)
	String clanChatList = "clanChatList";

	@ConfigItem(
		keyName = "clanChat",
		name = "Clan Chat",
		description = "The sound effect to use when receiving a clan chat message.",
		position = 17,
		section = clanChatList
	)
	default ChatSoundsMode clanChat()
	{
		return ChatSoundsMode.OFF;
	}

	@ConfigItem(
		keyName = "clanBroadcast",
		name = "Clan Broadcast",
		description = "The sound effect to use when receiving a clan broadcast message.",
		position = 18,
		section = clanChatList
	)
	default ChatSoundsMode clanBroadcast()
	{
		return ChatSoundsMode.OFF;
	}

	@Range(
			max = 100
	)
	@Units(
			value = "%"
	)
	@ConfigItem(
			keyName = "clanVolume",
			name = "Volume",
			description = "Sets the volume of the chat message sound effect.",
			position = 19,
			section = clanChatList
	)
	default int clanVolume()
	{
		return 100;
	}

	@ConfigItem(
			keyName = "clanIgnoreJoinLeave",
			name = "Ignore joined/left messages",
			description = "Do not play a sound for users joining or leaving chats.",
			position = 20,
			section = clanChatList
	)
	default boolean clanIgnoreJoinLeave()
	{
		return false;
	}

	@ConfigItem(
			keyName = "clanIgnorePlayers",
			name = "Ignore player names",
			description = "A comma-separated list of player names to never play a sound for.",
			position = 21,
			section = clanChatList
	)
	default String clanIgnorePlayersList()
	{
		return "";
	}

	/**************
 	* GUEST CLANS *
	**************/
	@ConfigSection(
			name = "Guest Clan Chat",
			description = "Settings for guest clan chats.",
			position = 22
	)
	String guestClanList = "guestClanList";

	@ConfigItem(
			keyName = "guestClanChat",
			name = "Guest Clan Chat",
			description = "The sound effect to use when receiving a guest clan chat message.",
			position = 23,
			section = guestClanList
	)
	default ChatSoundsMode guestClanChat()
	{
		return ChatSoundsMode.OFF;
	}

	@ConfigItem(
			keyName = "guestClanBroadcast",
			name = "Guest Clan Broadcast",
			description = "The sound effect to use when receiving a guest clan broadcast message.",
			position = 24,
			section = guestClanList
	)
	default ChatSoundsMode guestClanBroadcast()
	{
		return ChatSoundsMode.OFF;
	}

	@Range(
			max = 100
	)
	@Units(
			value = "%"
	)
	@ConfigItem(
			keyName = "guestClanChannelVolume",
			name = "Volume",
			description = "Sets the volume of the chat message sound effect.",
			position = 25,
			section = guestClanList
	)
	default int guestClanVolume()
	{
		return 100;
	}

	@ConfigItem(
			keyName = "guestClanIgnoreJoinLeave",
			name = "Ignore joined/left messages",
			description = "Do not play a sound for users joining or leaving chats.",
			position = 26,
			section = guestClanList
	)
	default boolean guestClanIgnoreJoinLeave()
	{
		return false;
	}

	@ConfigItem(
			keyName = "guestClanIgnorePlayers",
			name = "Ignore player names",
			description = "A comma-separated list of player names to never play a sound for.",
			position = 27,
			section = guestClanList
	)
	default String guestClanIgnorePlayersList()
	{
		return "";
	}

	/****************
 	* GROUP IRONMAN *
	****************/
	@ConfigSection(
			name = "Group Ironman Chat",
			description = "Settings for group ironman chats.",
			position = 28
	)
	String groupList = "groupList";

	@ConfigItem(
		keyName = "gimChat",
		name = "Group Ironman Chat",
		description = "The sound effect to use when receiving a group ironman chat message.",
		position = 29,
		section = groupList
	)
	default ChatSoundsMode gimChat()
	{
		return ChatSoundsMode.OFF;
	}

	@ConfigItem(
		keyName = "gimBroadcast",
		name = "Group Ironman Broadcast",
		description = "The sound effect to use when receiving a group ironman broadcast message.",
		position = 30,
		section = groupList
	)
	default ChatSoundsMode gimBroadcast()
	{
		return ChatSoundsMode.OFF;
	}

	@Range(
			max = 100
	)
	@Units(
			value = "%"
	)
	@ConfigItem(
			keyName = "groupIronVolume",
			name = "Volume",
			description = "Sets the volume of the chat message sound effect.",
			position = 31,
			section = groupList
	)
	default int groupIronVolume()
	{
		return 100;
	}

	@ConfigItem(
			keyName = "groupIronIgnorePlayers",
			name = "Ignore player names",
			description = "A comma-separated list of player names to never play a sound for.",
			position = 32,
			section = groupList
	)
	default String groupIronIgnorePlayersList()
	{
		return "";
	}

	/*********
	* TRADES *
	*********/
	@ConfigSection(
			name = "Trade Requests",
			description = "Settings for trade requests.",
			position = 33
	)
	String tradeList = "tradeList";

	@ConfigItem(
			keyName = "tradeRequest",
			name = "Trade Request",
			description = "The sound effect to use when receiving a trade request.",
			position = 34,
			section = tradeList
	)
	default ChatSoundsMode tradeRequest()
	{
		return ChatSoundsMode.OFF;
	}

	@Range(
			max = 100
	)
	@Units(
			value = "%"
	)
	@ConfigItem(
			keyName = "tradeVolume",
			name = "Trade Volume",
			description = "Sets the volume of the chat message sound effect.",
			position = 35,
			section = tradeList
	)
	default int tradeVolume()
	{
		return 100;
	}

	@ConfigItem(
			keyName = "tradeIgnorePlayers",
			name = "Ignore player names",
			description = "A comma-separated list of player names to never play a sound for.",
			position = 36,
			section = tradeList
	)
	default String tradeIgnorePlayersList()
	{
		return "";
	}

	/********
	* DUELS *
	********/
	@ConfigSection(
			name = "Duel Requests",
			description = "Settings for duel requests.",
			position = 37
	)
	String duelList = "duelList";

	@ConfigItem(
			keyName = "duelRequest",
			name = "Duel Request",
			description = "The sound effect to use when receiving a duel request.",
			position = 38,
			section = duelList
	)
	default ChatSoundsMode duelRequest()
	{
		return ChatSoundsMode.OFF;
	}

	@Range(
			max = 100
	)
	@Units(
			value = "%"
	)
	@ConfigItem(
			keyName = "duelVolume",
			name = "Duel Volume",
			description = "Sets the volume of the chat message sound effect.",
			position = 39,
			section = duelList
	)
	default int duelVolume()
	{
		return 100;
	}

	@ConfigItem(
			keyName = "duelIgnorePlayers",
			name = "Ignore player names",
			description = "A comma-separated list of player names to never play a sound for.",
			position = 40,
			section = duelList
	)
	default String duelIgnorePlayersList()
	{
		return "";
	}
}
