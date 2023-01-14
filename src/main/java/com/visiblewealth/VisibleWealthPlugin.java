package com.visiblewealth;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.kit.KitType;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.task.Schedule;
import net.runelite.client.ui.overlay.OverlayManager;

import java.time.temporal.ChronoUnit;

@Slf4j
@PluginDescriptor(
	name = "Visible Wealth",
	description = "Displays the total value of the items worn by visible players",
	tags = {"equipment", "items", "worth", "value"}
)
public class VisibleWealthPlugin extends Plugin
{

	@Getter
	private long totalWealth;

	@Inject
	private Client client;

	@Inject
	private VisibleWealthConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private VisibleWealthOverlay overlay;

	@Inject
	private ItemManager itemManager;

	@Override
	protected void startUp() throws Exception
	{
		calculateTotalWealth();
		overlayManager.add(overlay);
	}

	@Schedule(period = 1, unit = ChronoUnit.SECONDS)
	public void scanPlayers()
	{
		calculateTotalWealth();
	}

	private void calculateTotalWealth()
	{
		totalWealth = 0;

		if (client == null)
		{
			return;
		}

		for (Player player : client.getPlayers())
		{
			if (player == null)
			{
				continue;
			}

			if (player == client.getLocalPlayer() && !config.includeSelf())
			{
				continue;
			}

			PlayerComposition playerComposition = player.getPlayerComposition();
			if (playerComposition == null)
			{
				continue;
			}

			for (KitType kitType : KitType.values())
			{
				int itemId = playerComposition.getEquipmentId(kitType);
				if (itemId == -1)
				{
					continue;
				}

				int price = itemManager.getItemPrice(itemId);
				totalWealth += price;
			}
		}
	}

	@Override
	protected void shutDown() throws Exception
	{
		totalWealth = 0;
		overlayManager.remove(overlay);
	}

	@Provides
	VisibleWealthConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(VisibleWealthConfig.class);
	}
}
