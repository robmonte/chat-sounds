package com.chatsounds;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChatSoundsMode
{
	DEFAULT("Default"),
	CUSTOM("Custom"),
	OFF("Off");

	private final String name;

	@Override
	public String toString()
	{
		return name;
	}
}
