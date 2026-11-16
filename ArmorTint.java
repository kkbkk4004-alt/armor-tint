package com.example.armortint;

import net.minecraft.item.ItemStack;

/**
 * Holds the color multiplier for the armor piece that is being drawn right now.
 * The render thread draws one piece at a time, so plain static fields are enough.
 */
public final class ArmorTint {
	public static boolean active = false;
	public static int r = 255, g = 255, b = 255;

	private ArmorTint() {}

	/** Called when drawing of one armor piece starts. */
	public static void begin(ItemStack stack) {
		active = false;
		ArmorTintConfig cfg = ArmorTintConfig.get();
		if (!cfg.enabled || stack == null || stack.isEmpty() || !stack.isDamageable()) return;

		int max = stack.getMaxDamage();
		if (max <= 0) return;
		float left = 1f - stack.getDamage() / (float) max; // 1 = new, 0 = about to break
		float start = Math.max(0.05f, Math.min(1f, cfg.startFraction));
		if (left > start) return;

		// colors along the way: none -> yellow -> orange -> red
		float[][] stops = {
				{start, 255, 255, 255},
				{start * 0.70f, 255, 225, 80},
				{start * 0.40f, 255, 140, 50},
				{start * 0.10f, 255, 50, 50}
		};
		float fr, fg, fb;
		if (left >= stops[1][0]) {
			float[] c = lerp(stops[0], stops[1], left);
			fr = c[0]; fg = c[1]; fb = c[2];
		} else if (left >= stops[2][0]) {
			float[] c = lerp(stops[1], stops[2], left);
			fr = c[0]; fg = c[1]; fb = c[2];
		} else if (left >= stops[3][0]) {
			float[] c = lerp(stops[2], stops[3], left);
			fr = c[0]; fg = c[1]; fb = c[2];
		} else {
			fr = stops[3][1]; fg = stops[3][2]; fb = stops[3][3];
		}

		float brightness = 1f;
		if (cfg.flash && left <= 0.10f) { // almost broken: blink
			brightness = 0.7f + 0.3f * (float) Math.sin(System.currentTimeMillis() / 110.0);
		}
		r = Math.round(fr * brightness);
		g = Math.round(fg * brightness);
		b = Math.round(fb * brightness);
		active = true;
	}

	/** Called when drawing of that piece is done. */
	public static void end() {
		active = false;
	}

	/** Blend between two stops {fraction, r, g, b} for the durability fraction `left`. */
	private static float[] lerp(float[] from, float[] to, float left) {
		float span = from[0] - to[0];
		float t = span <= 0 ? 1f : (from[0] - left) / span;
		t = Math.max(0f, Math.min(1f, t));
		return new float[]{
				from[1] + (to[1] - from[1]) * t,
				from[2] + (to[2] - from[2]) * t,
				from[3] + (to[3] - from[3]) * t
		};
	}
}
