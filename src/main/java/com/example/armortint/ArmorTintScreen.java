package com.example.armortint;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

/** Settings screen (press H). Changes apply immediately and are saved. */
public class ArmorTintScreen extends Screen {
	private ButtonWidget enabledButton;
	private ButtonWidget flashButton;

	public ArmorTintScreen() {
		super(Text.literal("Armor Tint"));
	}

	@Override
	protected void init() {
		ArmorTintConfig cfg = ArmorTintConfig.get();
		int cx = this.width / 2;
		int top = this.height / 2 - 60;

		enabledButton = ButtonWidget.builder(Text.empty(), b -> {
			cfg.enabled = !cfg.enabled;
			cfg.save();
			refresh();
		}).dimensions(cx - 100, top, 200, 20).build();
		addDrawableChild(enabledButton);

		addDrawableChild(new SliderWidget(cx - 100, top + 26, 200, 20, Text.empty(), cfg.strength) {
			{ updateMessage(); }

			@Override
			protected void updateMessage() {
				setMessage(Text.literal("Color strength: " + Math.round(value * 100) + "%"));
			}

			@Override
			protected void applyValue() {
				cfg.strength = (float) value;
				cfg.save();
			}
		});

		// slider 0..1 maps to "starts when 10%..100% durability is left"
		double startPos = (Math.max(0.1f, Math.min(1f, cfg.startFraction)) - 0.1) / 0.9;
		addDrawableChild(new SliderWidget(cx - 100, top + 52, 200, 20, Text.empty(), startPos) {
			{ updateMessage(); }

			private int percent() {
				return (int) Math.round((0.1 + value * 0.9) * 100);
			}

			@Override
			protected void updateMessage() {
				setMessage(Text.literal("Starts at: " + percent() + "% durability left"));
			}

			@Override
			protected void applyValue() {
				cfg.startFraction = percent() / 100f;
				cfg.save();
			}
		});

		flashButton = ButtonWidget.builder(Text.empty(), b -> {
			cfg.flash = !cfg.flash;
			cfg.save();
			refresh();
		}).dimensions(cx - 100, top + 78, 200, 20).build();
		addDrawableChild(flashButton);

		addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> close())
				.dimensions(cx - 100, top + 108, 200, 20).build());

		refresh();
	}

	private void refresh() {
		ArmorTintConfig cfg = ArmorTintConfig.get();
		enabledButton.setMessage(Text.literal("Armor Tint: " + (cfg.enabled ? "ON" : "OFF")));
		flashButton.setMessage(Text.literal("Blink when almost broken: " + (cfg.flash ? "ON" : "OFF")));
	}

	@Override
	public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
		super.render(ctx, mouseX, mouseY, delta);
		ctx.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, this.height / 2 - 78, 0xFFFFFF);
	}
}
