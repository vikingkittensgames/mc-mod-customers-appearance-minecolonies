package com.vikingkittens.mc.customersaddon.appearance.minecolonies.client;

import net.minecraft.world.phys.Vec3;

final class MineColoniesSittingOffset {
    private static final double YOUNG_VERTICAL_OFFSET = 0.5D;
    private static final double YOUNG_FORWARD_OFFSET = 0.125D;

    private MineColoniesSittingOffset() {}

    static Vec3 forModel(String modelType, float bodyYaw) {
        if (!isYoungModel(modelType)) {
            return Vec3.ZERO;
        }

        double yawRadians = Math.toRadians(bodyYaw);
        return new Vec3(
                -Math.sin(yawRadians) * YOUNG_FORWARD_OFFSET,
                YOUNG_VERTICAL_OFFSET,
                Math.cos(yawRadians) * YOUNG_FORWARD_OFFSET
        );
    }

    static boolean isYoungModel(String modelType) {
        return "minecolonies:child".equals(modelType) || "minecolonies:student".equals(modelType);
    }
}
