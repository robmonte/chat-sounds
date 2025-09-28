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
			name = "Public chat",
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
			name = "Private chat",
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
			position = 12,
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
			position = 13,
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
			position = 14,
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
			name = "Clan chat",
			description = "Settings for clan chats.",
			position = 15
	)
	String clanChatList = "clanChatList";

	@ConfigItem(
		keyName = "clanChat",
		name = "Clan Chat",
		description = "The sound effect to use when receiving a clan chat message.",
		position = 16,
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
		position = 17,
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
			position = 18,
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
			position = 19,
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
			position = 20,
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
			name = "Guest clan chat",
			description = "Settings for guest clan chats.",
			position = 21
	)
	String guestClanList = "guestClanList";

	@ConfigItem(
			keyName = "guestClanChat",
			name = "Guest Clan Chat",
			description = "The sound effect to use when receiving a guest clan chat message.",
			position = 6,
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
			position = 22,
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
			position = 23,
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
			position = 24,
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
			position = 25,
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
			name = "Group ironman chat",
			description = "Settings for group ironman chats.",
			position = 26
	)
	String groupList = "groupList";

	@ConfigItem(
		keyName = "gimChat",
		name = "Group Ironman Chat",
		description = "The sound effect to use when receiving a group ironman chat message.",
		position = 27,
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
		position = 28,
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
			position = 29,
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
			position = 31,
			section = groupList
	)
	default String groupIronIgnorePlayersList()
	{
		return "";
	}
}
