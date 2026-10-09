package com.vikingkittens.mc.customersaddon.appearance.minecolonies.client;

import java.util.Map;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;

import com.vikingkittens.mc.customers.appearance.CustomersVillager;
import com.vikingkittens.mc.customers.client.appearance.CustomersVillagerClientAppearance;
import com.vikingkittens.mc.customersaddon.appearance.minecolonies.MineColoniesAppearanceProperties;

final class MineColoniesClientAppearance implements CustomersVillagerClientAppearance {
    private final MineColoniesRenderer<?> renderer;

    MineColoniesClientAppearance(EntityRendererProvider.Context context) {
        renderer = new MineColoniesRenderer<>(context);
    }

    @Override
    public MobRenderer<?, ?> getRenderer(CustomersVillager villager) {
        return renderer;
    }

    @Override
    public float getNameTagOffset(CustomersVillager villager) {
        return overheadItemOffset(villager.getAdditionalProperties());
    }

    static float overheadItemOffset(Map<String, String> properties) {
        String modelType = properties.get(MineColoniesAppearanceProperties.MODEL_TYPE);
        return MineColoniesSittingOffset.isYoungModel(modelType) ? -0.75F : 0.0F;
    }

    @Override
    public Vec3 getSittingOffset(CustomersVillager villager) {
        return sittingOffset(villager);
    }

    static Vec3 sittingOffset(CustomersVillager villager) {
        float bodyYaw = villager instanceof Mob mob ? mob.yBodyRot : 0.0F;
        return sittingOffset(villager.getAdditionalProperties(), bodyYaw);
    }

    static Vec3 sittingOffset(Map<String, String> properties, float bodyYaw) {
        return MineColoniesSittingOffset.forModel(properties.get(MineColoniesAppearanceProperties.MODEL_TYPE), bodyYaw);
    }
}
