package com.vikingkittens.mc.customersaddon.appearance.minecolonies.client;

import java.util.Map;
import java.util.WeakHashMap;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

import com.minecolonies.core.client.render.RenderBipedCitizen;

import com.vikingkittens.mc.customers.appearance.CustomersVillager;

final class MineColoniesRenderer<T extends Mob & CustomersVillager> extends MobRenderer<T, PlayerModel<T>> {
    private final RenderBipedCitizen delegate;
    private final Map<T, CachedProxy> proxies = new WeakHashMap<>();

    MineColoniesRenderer(EntityRendererProvider.Context context) {
        super(context, new PlayerModel<>(context.bakeLayer(ModelLayers.PLAYER), false), 0.5F);
        delegate = new RenderBipedCitizen(context);
    }

    @Override
    public void render(
            T entity,
            float entityYaw,
            float partialTick,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight
    ) {
        delegate.render(proxy(entity), entityYaw, partialTick, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return delegate.getTextureLocation(proxy(entity));
    }

    private MineColoniesCitizenProxy proxy(T entity) {
        Map<String, String> properties = entity.getAdditionalProperties();
        CachedProxy cached = proxies.get(entity);
        if (cached == null || !cached.properties().equals(properties)) {
            cached = new CachedProxy(properties, new MineColoniesCitizenProxy(entity.level(), properties));
            proxies.put(entity, cached);
        }
        cached.proxy().syncFrom(entity, entity);
        return cached.proxy();
    }

    private record CachedProxy(Map<String, String> properties, MineColoniesCitizenProxy proxy) {
        private CachedProxy {
            properties = Map.copyOf(properties);
        }
    }
}
