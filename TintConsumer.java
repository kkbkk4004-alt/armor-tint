package com.example.armortint;

import net.minecraft.client.render.VertexConsumer;

/** Passes everything through, but multiplies vertex colors by the current armor tint. */
public final class TintConsumer implements VertexConsumer {
	private final VertexConsumer delegate;

	public TintConsumer(VertexConsumer delegate) {
		this.delegate = delegate;
	}

	@Override
	public VertexConsumer vertex(float x, float y, float z) {
		delegate.vertex(x, y, z);
		return this;
	}

	@Override
	public VertexConsumer color(int red, int green, int blue, int alpha) {
		if (ArmorTint.active) {
			float k = Math.max(0f, Math.min(1f, ArmorTintConfig.get().strength));
			red = solid(red, ArmorTint.r, k);
			green = solid(green, ArmorTint.g, k);
			blue = solid(blue, ArmorTint.b, k);
		}
		delegate.color(red, green, blue, alpha);
		return this;
	}

	/** Pushes the armor toward the warning color but keeps its shading, so it stays vivid on dark armor. */
	private static int solid(int c, int tint, float k) {
		float shaded = tint * (0.45f + 0.55f * c / 255f);
		return Math.max(0, Math.min(255, Math.round(c * (1f - k) + shaded * k)));
	}

	@Override
	public VertexConsumer texture(float u, float v) {
		delegate.texture(u, v);
		return this;
	}

	@Override
	public VertexConsumer overlay(int u, int v) {
		delegate.overlay(u, v);
		return this;
	}

	@Override
	public VertexConsumer light(int u, int v) {
		delegate.light(u, v);
		return this;
	}

	@Override
	public VertexConsumer normal(float x, float y, float z) {
		delegate.normal(x, y, z);
		return this;
	}
}
