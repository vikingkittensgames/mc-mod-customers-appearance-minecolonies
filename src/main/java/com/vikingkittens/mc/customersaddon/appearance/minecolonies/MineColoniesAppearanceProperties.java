package com.vikingkittens.mc.customersaddon.appearance.minecolonies;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import com.vikingkittens.mc.customers.appearance.CustomersVillagerType;

public final class MineColoniesAppearanceProperties {
    public static final String STYLE = "minecolonies.style";
    public static final String MODEL_TYPE = "minecolonies.model_type";
    public static final String GENDER = "minecolonies.gender";
    public static final String TEXTURE_INDEX = "minecolonies.texture_index";
    public static final String TEXTURE_SUFFIX = "minecolonies.texture_suffix";

    private static final Pattern VALID_STYLE = Pattern.compile("[a-z0-9_-]+(?:/[a-z0-9_-]+)*");
    private static final List<String> BASE_CUSTOMER_MODELS = List.of(
            "minecolonies:citizen",
            "minecolonies:citizen",
            "minecolonies:citizen",
            "minecolonies:citizen",
            "minecolonies:settler",
            "minecolonies:settler",
            "minecolonies:settler",
            "minecolonies:settler"
    );
    private static final List<String> JOB_MODELS = List.of(
            "minecolonies:builder",
            "minecolonies:miner",
            "minecolonies:forester",
            "minecolonies:farmer",
            "minecolonies:fisherman",
            "minecolonies:crafter",
            "minecolonies:blacksmith",
            "minecolonies:cook",
            "minecolonies:baker",
            "minecolonies:smelter",
            "minecolonies:dyer",
            "minecolonies:glassblower",
            "minecolonies:carpenter",
            "minecolonies:alchemist",
            "minecolonies:healer",
            "minecolonies:teacher",
            "minecolonies:beekeeper",
            "minecolonies:netherworker",
            "minecolonies:druid",
            "minecolonies:florist",
            "minecolonies:enchanter",
            "minecolonies:planter",
            "minecolonies:mechanist"
    );
    private static final List<String> PRODUCTION_SUPPLIER_MODELS = List.of(
            "minecolonies:crafter",
            "minecolonies:blacksmith",
            "minecolonies:cook",
            "minecolonies:baker",
            "minecolonies:farmer",
            "minecolonies:carpenter"
    );
    private static final List<String> GUARD_MODELS = List.of("minecolonies:archer", "minecolonies:knight");
    private static final List<String> YOUNG_MODELS = List.of("minecolonies:child", "minecolonies:student");
    private static final Set<String> MULTI_TEXTURE_MODELS = Set.of(
            "minecolonies:citizen",
            "minecolonies:settler",
            "minecolonies:noble",
            "minecolonies:aristocrat",
            "minecolonies:child",
            "minecolonies:student"
    );
    private static final List<String> GENDERS = List.of("male", "female");
    private static final List<String> TEXTURE_SUFFIXES = List.of("a", "b", "d", "w");

    private MineColoniesAppearanceProperties() {}

    static Map<String, String> create(String style, CustomersVillagerType villagerType, float variationSeed) {
        return create(style, villagerType, variationSeed, SelectionOptions.DEFAULT);
    }

    static Map<String, String> create(
            String style,
            CustomersVillagerType villagerType,
            float variationSeed,
            SelectionOptions options
    ) {
        String modelType = select(modelsFor(villagerType, options), variationSeed, 0x3C6EF372);
        int textureCount = MULTI_TEXTURE_MODELS.contains(modelType) ? 3 : 1;
        return Map.of(
                STYLE,
                validStyle(style),
                MODEL_TYPE,
                modelType,
                GENDER,
                select(GENDERS, variationSeed, 0x1BF5A9D7),
                TEXTURE_INDEX,
                Integer.toString(selectIndex(variationSeed, 0x5A17C9E3, textureCount) + 1),
                TEXTURE_SUFFIX,
                select(TEXTURE_SUFFIXES, variationSeed, 0x72E4B1C5)
        );
    }

    static List<String> modelsFor(CustomersVillagerType villagerType, SelectionOptions options) {
        List<String> models = new ArrayList<>();
        if (villagerType == CustomersVillagerType.SUPPLIER) {
            models.addAll(List.of(
                    "minecolonies:courier",
                    "minecolonies:courier",
                    "minecolonies:courier",
                    "minecolonies:courier",
                    "minecolonies:citizen",
                    "minecolonies:settler"
            ));
            if (options.productionRoleSuppliers()) {
                models.addAll(PRODUCTION_SUPPLIER_MODELS);
            }
        } else {
            models.addAll(BASE_CUSTOMER_MODELS);
            if (options.nobleCustomers()) {
                models.addAll(List.of("minecolonies:noble", "minecolonies:noble"));
            }
            if (options.aristocratCustomers()) {
                models.add("minecolonies:aristocrat");
            }
            if (options.jobSpecificCustomers()) {
                models.addAll(JOB_MODELS);
            }
        }
        if (options.guardModels()) {
            models.addAll(GUARD_MODELS);
        }
        if (options.childAndStudentModels()) {
            models.addAll(YOUNG_MODELS);
        }
        return List.copyOf(models);
    }

    private static String validStyle(String style) {
        return style != null && VALID_STYLE.matcher(style).matches() ? style : "default";
    }

    private static String select(List<String> values, float variationSeed, int salt) {
        return values.get(selectIndex(variationSeed, salt, values.size()));
    }

    private static int selectIndex(float variationSeed, int salt, int size) {
        int mixed = Float.floatToIntBits(variationSeed) ^ salt;
        mixed ^= mixed >>> 16;
        mixed *= 0x7FEB352D;
        mixed ^= mixed >>> 15;
        return Math.floorMod(mixed, size);
    }

    record SelectionOptions(
            boolean nobleCustomers,
            boolean aristocratCustomers,
            boolean jobSpecificCustomers,
            boolean productionRoleSuppliers,
            boolean guardModels,
            boolean childAndStudentModels
    ) {
        static final SelectionOptions DEFAULT = new SelectionOptions(true, true, false, false, false, false);
    }
}
