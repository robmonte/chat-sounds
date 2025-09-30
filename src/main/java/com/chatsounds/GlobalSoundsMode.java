package com.chatsounds;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GlobalSoundsMode
{
    ON("On"),
    OFF("Off");

    private final String name;

    @Override
    public String toString()
    {
        return name;
    }
}
