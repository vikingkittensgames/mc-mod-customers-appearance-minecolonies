package com.vikingkittens.mc.customersaddon.appearance.minecolonies;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.vikingkittens.mc.customers.appearance.CustomersVillager;
import com.vikingkittens.mc.customers.appearance.CustomersVillagerType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MineColoniesAppearanceTest {
    @Test
    void appearanceIsApplicableWhenResolverFindsAColony() {
        MineColoniesAppearance appearance = new MineColoniesAppearance(villager -> Optional.of("nordic"));

        assertTrue(appearance.isApplicable(mock(CustomersVillager.class)));
    }

    @Test
    void appearanceIsNotApplicableWithoutAColony() {
        MineColoniesAppearance appearance = new MineColoniesAppearance(villager -> Optional.empty());

        assertFalse(appearance.isApplicable(mock(CustomersVillager.class)));
    }

    @Test
    void additionalPropertiesUseTheResolvedStyleAndVillagerState() {
        CustomersVillager villager = mock(CustomersVillager.class);
        when(villager.getCustomersVillagerType()).thenReturn(CustomersVillagerType.SUPPLIER);
        when(villager.getVariationSeed()).thenReturn(0.75F);
        MineColoniesAppearance appearance = new MineColoniesAppearance(ignored -> Optional.of("modern"));

        Map<String, String> properties = appearance.getAdditionalProperties(villager);

        assertEquals("modern", properties.get(MineColoniesAppearanceProperties.STYLE));
        assertEquals("minecolonies:courier", properties.get(MineColoniesAppearanceProperties.MODEL_TYPE));
    }
}
