package com.vikingkittens.mc.customersaddon.appearance.minecolonies;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = CustomersMineColoniesAppearanceMod.MOD_ID, dist = Dist.CLIENT)
public final class CustomersMineColoniesAppearanceClient {
    public CustomersMineColoniesAppearanceClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
