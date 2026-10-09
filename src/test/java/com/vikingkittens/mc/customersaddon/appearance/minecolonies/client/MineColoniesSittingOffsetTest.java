package com.vikingkittens.mc.customersaddon.appearance.minecolonies.client;

import java.util.Map;

import org.junit.jupiter.api.Test;

import net.minecraft.world.phys.Vec3;

import com.vikingkittens.mc.customersaddon.appearance.minecolonies.MineColoniesAppearanceProperties;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MineColoniesSittingOffsetTest {
    private static final double TOLERANCE = 0.000001D;

    @Test
    void adultModelsKeepTheDefaultSittingPosition() {
        assertEquals(Vec3.ZERO, MineColoniesSittingOffset.forModel("minecolonies:citizen", 0.0F));
    }

    @Test
    void childAndStudentModelsSitHigherAndForward() {
        assertOffset(new Vec3(0.0D, 0.5D, 0.125D), MineColoniesSittingOffset.forModel("minecolonies:child", 0.0F));
        assertOffset(
                new Vec3(-0.125D, 0.5D, 0.0D),
                MineColoniesSittingOffset.forModel("minecolonies:student", 90.0F)
        );
    }

    @Test
    void forwardOffsetFollowsEveryCardinalBodyYaw() {
        assertOffset(
                new Vec3(0.0D, 0.5D, -0.125D),
                MineColoniesSittingOffset.forModel("minecolonies:child", 180.0F)
        );
        assertOffset(
                new Vec3(0.125D, 0.5D, 0.0D),
                MineColoniesSittingOffset.forModel("minecolonies:child", 270.0F)
        );
    }

    @Test
    void clientAppearanceReadsThePersistedModelProperty() {
        assertOffset(
                new Vec3(-0.125D, 0.5D, 0.0D),
                MineColoniesClientAppearance.sittingOffset(
                        Map.of(MineColoniesAppearanceProperties.MODEL_TYPE, "minecolonies:student"),
                        90.0F
                )
        );
    }

    @Test
    void childAndStudentOverheadItemsRenderLower() {
        assertEquals(
                -0.75F,
                MineColoniesClientAppearance.overheadItemOffset(Map.of(
                        MineColoniesAppearanceProperties.MODEL_TYPE,
                        "minecolonies:child"
                ))
        );
        assertEquals(
                -0.75F,
                MineColoniesClientAppearance.overheadItemOffset(Map.of(
                        MineColoniesAppearanceProperties.MODEL_TYPE,
                        "minecolonies:student"
                ))
        );
    }

    @Test
    void adultOverheadItemsKeepTheDefaultHeight() {
        assertEquals(
                0.0F,
                MineColoniesClientAppearance.overheadItemOffset(Map.of(
                        MineColoniesAppearanceProperties.MODEL_TYPE,
                        "minecolonies:citizen"
                ))
        );
    }

    private static void assertOffset(Vec3 expected, Vec3 actual) {
        assertEquals(expected.x, actual.x, TOLERANCE);
        assertEquals(expected.y, actual.y, TOLERANCE);
        assertEquals(expected.z, actual.z, TOLERANCE);
    }
}
