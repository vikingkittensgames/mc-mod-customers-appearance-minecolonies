package com.vikingkittens.mc.customersaddon.appearance.minecolonies;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.vikingkittens.mc.customers.appearance.CustomersVillagerType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MineColoniesAppearancePropertiesTest {
    private static final Set<String> CUSTOMER_MODELS = Set.of(
            "minecolonies:citizen",
            "minecolonies:settler",
            "minecolonies:noble",
            "minecolonies:aristocrat"
    );

    @Test
    void customerSelectionIsDeterministic() {
        Map<String, String> first = MineColoniesAppearanceProperties.create(
                "nordic",
                CustomersVillagerType.CUSTOMER_NORMAL,
                0.625F
        );

        assertEquals(
                first,
                MineColoniesAppearanceProperties.create("nordic", CustomersVillagerType.CUSTOMER_NORMAL, 0.625F)
        );
    }

    @Test
    void customerRolesStayWithinTheDefaultWeightedPool() {
        for (int index = 0; index < 100; index++) {
            Map<String, String> properties = MineColoniesAppearanceProperties.create(
                    "default",
                    CustomersVillagerType.CUSTOMER_CASUAL,
                    index / 100.0F
            );

            assertTrue(CUSTOMER_MODELS.contains(properties.get(MineColoniesAppearanceProperties.MODEL_TYPE)));
        }
    }

    @Test
    void defaultCustomerPoolUsesDocumentedWeights() {
        assertEquals(
                Map.of(
                        "minecolonies:citizen",
                        4L,
                        "minecolonies:settler",
                        4L,
                        "minecolonies:noble",
                        2L,
                        "minecolonies:aristocrat",
                        1L
                ),
                MineColoniesAppearanceProperties.modelsFor(
                                CustomersVillagerType.CUSTOMER_NORMAL,
                                MineColoniesAppearanceProperties.SelectionOptions.DEFAULT
                        ).stream()
                        .collect(Collectors.groupingBy(model -> model, Collectors.counting()))
        );
    }

    @Test
    void defaultSupplierPoolPrefersCourierAndIncludesCivilianFallbacks() {
        assertEquals(
                Map.of("minecolonies:courier", 4L, "minecolonies:citizen", 1L, "minecolonies:settler", 1L),
                MineColoniesAppearanceProperties.modelsFor(
                                CustomersVillagerType.SUPPLIER,
                                MineColoniesAppearanceProperties.SelectionOptions.DEFAULT
                        ).stream()
                        .collect(Collectors.groupingBy(model -> model, Collectors.counting()))
        );
    }

    @Test
    void invalidStyleFallsBackToDefault() {
        Map<String, String> properties = MineColoniesAppearanceProperties.create(
                "../Invalid Style",
                CustomersVillagerType.CUSTOMER_IMPATIENT,
                0.5F
        );

        assertEquals("default", properties.get(MineColoniesAppearanceProperties.STYLE));
    }

    @Test
    void everyPersistedRenderingPropertyIsPresent() {
        Map<String, String> properties = MineColoniesAppearanceProperties.create(
                "eastasian",
                CustomersVillagerType.CUSTOMER_NORMAL,
                0.875F
        );

        assertEquals(
                Set.of(
                        MineColoniesAppearanceProperties.STYLE,
                        MineColoniesAppearanceProperties.MODEL_TYPE,
                        MineColoniesAppearanceProperties.GENDER,
                        MineColoniesAppearanceProperties.TEXTURE_INDEX,
                        MineColoniesAppearanceProperties.TEXTURE_SUFFIX
                ),
                properties.keySet()
        );
    }

    @Test
    void optionalModelsAreDisabledByDefault() {
        assertEquals(
                Set.of("minecolonies:citizen", "minecolonies:settler", "minecolonies:noble", "minecolonies:aristocrat"),
                Set.copyOf(MineColoniesAppearanceProperties.modelsFor(
                        CustomersVillagerType.CUSTOMER_NORMAL,
                        MineColoniesAppearanceProperties.SelectionOptions.DEFAULT
                ))
        );
    }

    @Test
    void eachOptionalCustomerCategoryCanBeEnabled() {
        MineColoniesAppearanceProperties.SelectionOptions options =
                new MineColoniesAppearanceProperties.SelectionOptions(
                true,
                true,
                true,
                false,
                true,
                true
        );
        Set<String> models = Set.copyOf(MineColoniesAppearanceProperties.modelsFor(
                CustomersVillagerType.CUSTOMER_NORMAL,
                options
        ));

        assertTrue(models.contains("minecolonies:builder"));
        assertTrue(models.contains("minecolonies:archer"));
        assertTrue(models.contains("minecolonies:knight"));
        assertTrue(models.contains("minecolonies:child"));
        assertTrue(models.contains("minecolonies:student"));
    }

    @Test
    void productionSupplierRolesCanBeEnabled() {
        MineColoniesAppearanceProperties.SelectionOptions options =
                new MineColoniesAppearanceProperties.SelectionOptions(
                true,
                true,
                false,
                true,
                false,
                false
        );
        Set<String> models = Set.copyOf(MineColoniesAppearanceProperties.modelsFor(
                CustomersVillagerType.SUPPLIER,
                options
        ));

        assertTrue(models.contains("minecolonies:crafter"));
        assertTrue(models.contains("minecolonies:blacksmith"));
        assertTrue(models.contains("minecolonies:cook"));
        assertTrue(models.contains("minecolonies:baker"));
        assertTrue(models.contains("minecolonies:farmer"));
        assertTrue(models.contains("minecolonies:carpenter"));
    }

    @Test
    void nobleAndAristocratCustomersCanBeDisabledIndependently() {
        MineColoniesAppearanceProperties.SelectionOptions options =
                new MineColoniesAppearanceProperties.SelectionOptions(
                false,
                false,
                false,
                false,
                false,
                false
        );

        assertEquals(
                Set.of("minecolonies:citizen", "minecolonies:settler"),
                Set.copyOf(MineColoniesAppearanceProperties.modelsFor(CustomersVillagerType.CUSTOMER_CASUAL, options))
        );
    }
}
