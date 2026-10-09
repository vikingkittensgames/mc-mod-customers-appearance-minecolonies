package com.vikingkittens.mc.customersaddon.appearance.minecolonies;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import com.vikingkittens.mc.customers.appearance.CustomersVillager;
import com.vikingkittens.mc.customers.appearance.CustomersVillagerAppearance;

public final class MineColoniesAppearance implements CustomersVillagerAppearance {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(
            CustomersMineColoniesAppearanceMod.MOD_ID,
            "local_colony"
    );

    private final Function<CustomersVillager, Optional<String>> styleResolver;
    private final Supplier<MineColoniesAppearanceProperties.SelectionOptions> selectionOptions;

    public MineColoniesAppearance() {
        this(
                villager -> MineColoniesColonyResolver.resolve(
                        villager,
                        Config.MAX_NEARBY_COLONY_DISTANCE.get()
                ),
                Config::selectionOptions
        );
    }

    MineColoniesAppearance(Function<CustomersVillager, Optional<String>> styleResolver) {
        this(styleResolver, () -> MineColoniesAppearanceProperties.SelectionOptions.DEFAULT);
    }

    MineColoniesAppearance(
            Function<CustomersVillager, Optional<String>> styleResolver,
            Supplier<MineColoniesAppearanceProperties.SelectionOptions> selectionOptions
    ) {
        this.styleResolver = styleResolver;
        this.selectionOptions = selectionOptions;
    }

    @Override
    public Component getName() {
        return Component.translatable("appearance.customers_minecolonies_appearance.local_colony");
    }

    @Override
    public boolean isApplicable(CustomersVillager villager) {
        return styleResolver.apply(villager).isPresent();
    }

    @Override
    public Map<String, String> getAdditionalProperties(CustomersVillager villager) {
        return styleResolver.apply(villager)
                .map(style -> MineColoniesAppearanceProperties.create(
                        style,
                        villager.getCustomersVillagerType(),
                        villager.getVariationSeed(),
                        selectionOptions.get()
                ))
                .orElseGet(Map::of);
    }
}
