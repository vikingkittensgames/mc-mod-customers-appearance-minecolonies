package com.vikingkittens.mc.customersaddon.appearance.minecolonies.client;

import java.lang.ref.WeakReference;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

import com.minecolonies.api.client.render.modeltype.ModModelTypes;
import com.minecolonies.api.client.render.modeltype.registry.IModelTypeRegistry;
import com.minecolonies.api.entity.ModEntities;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.core.entity.citizen.EntityCitizen;

import com.vikingkittens.mc.customers.appearance.CustomersVillager;
import com.vikingkittens.mc.customers.client.appearance.CustomersVillagerRenderProxy;
import com.vikingkittens.mc.customersaddon.appearance.minecolonies.MineColoniesAppearanceProperties;

final class MineColoniesCitizenProxy extends EntityCitizen implements CustomersVillagerRenderProxy {
    private WeakReference<CustomersVillager> source = new WeakReference<>(null);
    private WeakReference<Mob> sourceEntity = new WeakReference<>(null);
    private int sourceTick = Integer.MIN_VALUE;

    MineColoniesCitizenProxy(Level level, Map<String, String> properties) {
        super(ModEntities.CITIZEN, level);
        applyProperties(properties);
    }

    void syncFrom(Mob entity, CustomersVillager villager) {
        source = new WeakReference<>(villager);
        sourceEntity = new WeakReference<>(entity);
        setPos(entity.getX(), entity.getY(), entity.getZ());
        setYRot(entity.getYRot());
        setXRot(entity.getXRot());
        yRotO = entity.yRotO;
        xRotO = entity.xRotO;
        yBodyRot = entity.yBodyRot;
        yBodyRotO = entity.yBodyRotO;
        yHeadRot = entity.yHeadRot;
        yHeadRotO = entity.yHeadRotO;
        tickCount = entity.tickCount;
        hurtTime = entity.hurtTime;
        deathTime = entity.deathTime;
        swinging = entity.swinging;
        swingingArm = entity.swingingArm;
        swingTime = entity.swingTime;
        oAttackAnim = entity.oAttackAnim;
        attackAnim = entity.attackAnim;
        if (sourceTick != entity.tickCount) {
            sourceTick = entity.tickCount;
            walkAnimation.update(entity.walkAnimation.speed(), 1.0F);
        }
        setPose(entity.getPose());
        setDeltaMovement(entity.getDeltaMovement());
        setCustomName(entity.getCustomName());
        setCustomNameVisible(entity.isCustomNameVisible());
        setInvisible(entity.isInvisible());
        setGlowingTag(entity.isCurrentlyGlowing());
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            setItemSlot(slot, entity.getItemBySlot(slot).copy());
        }
    }

    @Override
    public boolean isPassenger() {
        Mob entity = sourceEntity.get();
        return entity != null && entity.isPassenger();
    }

    @Override
    public @Nullable Entity getVehicle() {
        Mob entity = sourceEntity.get();
        return entity == null ? null : entity.getVehicle();
    }

    @Override
    public @Nullable CustomersVillager getCustomersVillagerSource() {
        return source.get();
    }

    @Override
    public boolean shouldRenderNameTag() {
        Mob entity = sourceEntity.get();
        return entity != null && entity.isCustomNameVisible();
    }

    private void applyProperties(Map<String, String> properties) {
        getEntityData().set(
                AbstractEntityCitizen.DATA_STYLE,
                properties.getOrDefault(MineColoniesAppearanceProperties.STYLE, "default")
        );
        setModelId(parseModel(properties.get(MineColoniesAppearanceProperties.MODEL_TYPE)));
        setFemale("female".equals(properties.get(MineColoniesAppearanceProperties.GENDER)));
        int textureIndex = parseTextureIndex(properties.get(MineColoniesAppearanceProperties.TEXTURE_INDEX));
        setTextureId(Math.max(0, textureIndex - 1));
        String suffix = properties.getOrDefault(MineColoniesAppearanceProperties.TEXTURE_SUFFIX, "b");
        getEntityData().set(AbstractEntityCitizen.DATA_TEXTURE_SUFFIX, "_" + suffix);
        setIsChild(MineColoniesSittingOffset.isYoungModel(getModelType().toString()));
    }

    private static ResourceLocation parseModel(String value) {
        ResourceLocation model = ResourceLocation.tryParse(value);
        return model == null || IModelTypeRegistry.getInstance().getModelType(model) == null
                ? ModModelTypes.CITIZEN_ID
                : model;
    }

    private static int parseTextureIndex(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return 1;
        }
    }

}
