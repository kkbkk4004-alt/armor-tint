package com.example.armortint;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;

/** Hands out vertex consumers that apply the current armor tint. */
public final class TintProvider implements VertexConsumerProvider {
	private final VertexConsumerProvider delegate;

	public TintProvider(VertexConsumerProvider delegate) {
		this.delegate = delegate;
	}

	@Override
	public VertexConsumer getBuffer(RenderLayer layer) {
		return new TintConsumer(delegate.getBuffer(layer));
	}
}
