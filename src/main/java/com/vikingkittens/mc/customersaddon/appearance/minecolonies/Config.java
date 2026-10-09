package com.vikingkittens.mc.customersaddon.appearance.minecolonies;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue MAX_NEARBY_COLONY_DISTANCE = BUILDER
            .translation("config.customers_minecolonies_appearance.max_nearby_colony_distance")
            .defineInRange("maxNearbyColonyDistance", 128, 0, 4096);
    public static final ModConfigSpec.BooleanValue ENABLE_NOBLE_CUSTOMERS = BUILDER
            .translation("config.customers_minecolonies_appearance.enable_noble_customers")
            .define("enableNobleCustomers", true);
    public static final ModConfigSpec.BooleanValue ENABLE_ARISTOCRAT_CUSTOMERS = BUILDER
            .translation("config.customers_minecolonies_appearance.enable_aristocrat_customers")
            .define("enableAristocratCustomers", true);
    public static final ModConfigSpec.BooleanValue ENABLE_JOB_SPECIFIC_CUSTOMERS = BUILDER
            .translation("config.customers_minecolonies_appearance.enable_job_specific_customers")
            .define("enableJobSpecificCustomers", false);
    public static final ModConfigSpec.BooleanValue ENABLE_PRODUCTION_ROLE_SUPPLIERS = BUILDER
            .translation("config.customers_minecolonies_appearance.enable_production_role_suppliers")
            .define("enableProductionRoleSuppliers", false);
    public static final ModConfigSpec.BooleanValue ENABLE_GUARD_MODELS = BUILDER
            .translation("config.customers_minecolonies_appearance.enable_guard_models")
            .define("enableGuardModels", false);
    public static final ModConfigSpec.BooleanValue ENABLE_CHILD_AND_STUDENT_MODELS = BUILDER
            .translation("config.customers_minecolonies_appearance.enable_child_and_student_models")
            .define("enableChildAndStudentModels", false);

    public static final ModConfigSpec SPEC = BUILDER.build();

    private Config() {}

    static MineColoniesAppearanceProperties.SelectionOptions selectionOptions() {
        return new MineColoniesAppearanceProperties.SelectionOptions(
                ENABLE_NOBLE_CUSTOMERS.get(),
                ENABLE_ARISTOCRAT_CUSTOMERS.get(),
                ENABLE_JOB_SPECIFIC_CUSTOMERS.get(),
                ENABLE_PRODUCTION_ROLE_SUPPLIERS.get(),
                ENABLE_GUARD_MODELS.get(),
                ENABLE_CHILD_AND_STUDENT_MODELS.get()
        );
    }
}
