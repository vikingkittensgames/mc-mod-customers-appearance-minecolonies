package com.vikingkittens.mc.customersaddon.appearance.minecolonies;

import java.util.Objects;
import java.util.Optional;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.IColonyManager;

import com.vikingkittens.mc.customers.appearance.CustomersVillager;

final class MineColoniesColonyResolver {
    static final int DEFAULT_MAX_DISTANCE = 128;

    private MineColoniesColonyResolver() {}

    static Optional<String> resolve(CustomersVillager villager, int maxDistance) {
        BlockPos position = villager.getCustomersSpawnerPosition();
        if (!(villager.getCustomersLevel() instanceof ServerLevel level) || position == null) {
            return Optional.empty();
        }

        IColonyManager manager = IColonyManager.getInstance();
        return resolve(position, maxDistance, new ColonyLookup() {
            @Override
            public Optional<ColonyCandidate> containing() {
                return candidate(manager.getIColony(level, position));
            }

            @Override
            public Optional<ColonyCandidate> closest() {
                return candidate(manager.getClosestIColony(level, position));
            }
        });
    }

    static Optional<String> resolve(BlockPos position, int maxDistance, ColonyLookup lookup) {
        Optional<ColonyCandidate> containing = lookup.containing();
        if (containing.isPresent()) {
            return Optional.of(containing.orElseThrow().textureStyle());
        }

        long distance = maxDistance;
        double maximumDistanceSquared = distance * distance;
        return lookup.closest()
                .filter(candidate -> position.distSqr(candidate.center()) <= maximumDistanceSquared)
                .map(ColonyCandidate::textureStyle);
    }

    private static Optional<ColonyCandidate> candidate(IColony colony) {
        return Optional.ofNullable(colony)
                .map(value -> new ColonyCandidate(
                        value.getCenter(),
                        Objects.requireNonNullElse(value.getTextureStyleId(), "default")
                ));
    }

    interface ColonyLookup {
        Optional<ColonyCandidate> containing();

        Optional<ColonyCandidate> closest();
    }

    record ColonyCandidate(BlockPos center, String textureStyle) {}
}
