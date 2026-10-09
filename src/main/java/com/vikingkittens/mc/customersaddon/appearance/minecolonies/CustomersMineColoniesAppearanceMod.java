package com.vikingkittens.mc.customersaddon.appearance.minecolonies;

import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

import com.vikingkittens.mc.customers.appearance.CustomersVillagerAppearanceRegistries;

@Mod(CustomersMineColoniesAppearanceMod.MOD_ID)
public final class CustomersMineColoniesAppearanceMod {
    public static final String MOD_ID = "customers_minecolonies_appearance";

    public CustomersMineColoniesAppearanceMod(ModContainer modContainer) {
        CustomersVillagerAppearanceRegistries.appearances().register(
                MineColoniesAppearance.ID,
                MineColoniesAppearance::new
        );
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
}
