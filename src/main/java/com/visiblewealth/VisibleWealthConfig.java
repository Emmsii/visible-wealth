package com.visiblewealth;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("visibleWealth")
public interface VisibleWealthConfig extends Config
{
	@ConfigItem(
		keyName = "includeSelf",
		name = "Include Self",
		description = "Include your own equipment when calculating visible wealth"
	)
	default boolean includeSelf()
	{
		return false;
	}
}
