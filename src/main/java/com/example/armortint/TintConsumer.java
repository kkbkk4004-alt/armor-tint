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
			red = red * ArmorTint.r / 255;
			green = green * ArmorTint.g / 255;
			blue = blue * ArmorTint.b / 255;
		}
		delegate.color(red, green, blue, alpha);
		return this;
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
