package com.vikingkittens.mc.customersaddon.appearance.minecolonies.client;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import com.vikingkittens.mc.customers.client.appearance.CustomersVillagerClientAppearances;
import com.vikingkittens.mc.customersaddon.appearance.minecolonies.CustomersMineColoniesAppearanceMod;
import com.vikingkittens.mc.customersaddon.appearance.minecolonies.MineColoniesAppearance;

@EventBusSubscriber(modid = CustomersMineColoniesAppearanceMod.MOD_ID, value = Dist.CLIENT)
public final class MineColoniesClientEvents {
    private MineColoniesClientEvents() {}

    @SubscribeEvent
    public static void registerAppearance(EntityRenderersEvent.RegisterRenderers event) {
        CustomersVillagerClientAppearances.register(
                MineColoniesAppearance.ID,
                MineColoniesClientAppearance::new
        );
    }
}
