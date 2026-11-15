package com.example.armortint.mixin;

import com.example.armortint.ArmorTint;
import com.example.armortint.TintProvider;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * renderArmor draws one armor piece (helmet, chestplate, ...) of one player.
 * We note which piece it is, work out its tint from its durability, and make
 * every vertex written during the call go through the tinting wrapper.
 */
@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin {

	@Inject(method = "renderArmor", at = @At("HEAD"))
	private void armortint$begin(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack,
								 EquipmentSlot slot, int light, BipedEntityRenderState state, CallbackInfo ci) {
		ArmorTint.begin(stack);
	}

	@ModifyVariable(method = "renderArmor", at = @At("HEAD"), argsOnly = true, ordinal = 0)
	private VertexConsumerProvider armortint$wrap(VertexConsumerProvider original) {
		return new TintProvider(original);
	}

	@Inject(method = "renderArmor", at = @At("RETURN"))
	private void armortint$end(MatrixStack matrices, VertexConsumerProvider vertexConsumers, ItemStack stack,
							   EquipmentSlot slot, int light, BipedEntityRenderState state, CallbackInfo ci) {
		ArmorTint.end();
	}
}
