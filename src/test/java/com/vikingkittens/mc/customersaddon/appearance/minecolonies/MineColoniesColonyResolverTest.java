package com.vikingkittens.mc.customersaddon.appearance.minecolonies;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import net.minecraft.core.BlockPos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MineColoniesColonyResolverTest {
    private static final BlockPos POSITION = new BlockPos(0, 64, 0);

    @Test
    void containingColonyIsPreferredWithoutRequestingClosest() {
        TestLookup lookup = new TestLookup(
                Optional.of(new MineColoniesColonyResolver.ColonyCandidate(new BlockPos(500, 64, 0), "nordic")),
                Optional.of(new MineColoniesColonyResolver.ColonyCandidate(new BlockPos(1, 64, 0), "default"))
        );

        assertEquals(Optional.of("nordic"), MineColoniesColonyResolver.resolve(POSITION, 128, lookup));
        assertFalse(lookup.closestRequested);
    }

    @Test
    void closestColonyInsideRadiusIsAccepted() {
        TestLookup lookup = new TestLookup(
                Optional.empty(),
                Optional.of(new MineColoniesColonyResolver.ColonyCandidate(new BlockPos(64, 64, 0), "medieval"))
        );

        assertEquals(Optional.of("medieval"), MineColoniesColonyResolver.resolve(POSITION, 128, lookup));
        assertTrue(lookup.closestRequested);
    }

    @Test
    void closestColonyOutsideRadiusIsRejected() {
        TestLookup lookup = new TestLookup(
                Optional.empty(),
                Optional.of(new MineColoniesColonyResolver.ColonyCandidate(new BlockPos(129, 64, 0), "modern"))
        );

        assertEquals(Optional.empty(), MineColoniesColonyResolver.resolve(POSITION, 128, lookup));
    }

    @Test
    void noColonyReturnsEmpty() {
        assertEquals(
                Optional.empty(),
                MineColoniesColonyResolver.resolve(POSITION, 128, new TestLookup(Optional.empty(), Optional.empty()))
        );
    }

    private static final class TestLookup implements MineColoniesColonyResolver.ColonyLookup {
        private final Optional<MineColoniesColonyResolver.ColonyCandidate> containing;
        private final Optional<MineColoniesColonyResolver.ColonyCandidate> closest;
        private boolean closestRequested;

        private TestLookup(
                Optional<MineColoniesColonyResolver.ColonyCandidate> containing,
                Optional<MineColoniesColonyResolver.ColonyCandidate> closest
        ) {
            this.containing = containing;
            this.closest = closest;
        }

        @Override
        public Optional<MineColoniesColonyResolver.ColonyCandidate> containing() {
            return containing;
        }

        @Override
        public Optional<MineColoniesColonyResolver.ColonyCandidate> closest() {
            closestRequested = true;
            return closest;
        }
    }
}
