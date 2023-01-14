package com.visiblewealth;

import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.util.QuantityFormatter;
import javax.inject.Inject;
import java.awt.*;

public class VisibleWealthOverlay extends OverlayPanel
{

	private final VisibleWealthPlugin plugin;

	@Inject
	private VisibleWealthOverlay(VisibleWealthPlugin plugin)
	{
		super(plugin);
		this.plugin = plugin;
		setPosition(OverlayPosition.ABOVE_CHATBOX_RIGHT);
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		long price = plugin.getTotalWealth();

		final String titleText = "Visible Wealth";
		panelComponent.getChildren().add(TitleComponent.builder()
			.text(titleText)
			.color(Color.WHITE)
			.build());

		final String priceText = QuantityFormatter.quantityToStackSize(price);
		panelComponent.getChildren().add(TitleComponent.builder()
			.text(priceText)
			.color(Color.YELLOW)
			.build());

		int width = Math.max(graphics.getFontMetrics().stringWidth(priceText) + 10,
			graphics.getFontMetrics().stringWidth(titleText) + 10);

		panelComponent.setPreferredSize(new Dimension(width, 0));

		return super.render(graphics);
	}
}
