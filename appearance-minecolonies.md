# MineColonies Appearance Plan

## Status and implementation

The MineColonies appearance is implemented as this separate optional NeoForge
add-on for Minecraft 1.21.1. Server-side colony selection, deterministic visual
properties, generic Customers property persistence, delegated client
rendering, configuration, unit tests, clean builds, and development-client
startup have been completed.

MineColonies provides public colony, citizen-style, and model-type interfaces,
making this integration safer than the Millénaire integration. Its renderer is
still an internal implementation class and requires a MineColonies citizen
entity rather than an arbitrary Customers entity.

## How MineColonies groups appearances

MineColonies separates two concepts:

* A colony's structure pack controls its buildings.
* A colony's citizen texture style controls how its citizens look.

The appearance must use `IColony.getTextureStyleId()`. It must not infer the
citizen style from the structure pack, biome, or region.

The Minecraft 1.21.1 branch currently includes these built-in citizen styles:

* `default`
* `eastasian`
* `hellenic`
* `medieval`
* `modern`
* `nether`
* `nordic`
* `undead`

Resource packs can provide more style folders under
`textures/entity/citizen`. The implementation must support the colony's actual
style string and must not restrict selection to the eight built-in styles.

MineColonies also groups citizens by role-specific model types. Important
groups include:

* General population: settler and citizen
* Social classes: noble and aristocrat
* Logistics: courier
* Production professions: builder, miner, forester, farmer, fisherman,
  crafter, blacksmith, cook, baker, smelter, dyer, glassblower, carpenter,
  alchemist, and others
* Guards: archer and knight
* Children and students
* Specialist and expansion roles such as healer, teacher, beekeeper,
  netherworker, druid, florist, enchanter, planter, and mechanist

Each registered model type supplies male and female models and a texture
selection policy. General citizen, settler, noble, aristocrat, student, and
child models currently have multiple base texture indices. Most job models
have one texture index for each gender. MineColonies also applies saved texture
suffix variants.

## Player-facing behavior

Register one spawner option named `MineColonies - Local Colony`.

When a Customer or Supplier spawns with this option enabled:

1. Ask `IColonyManager` for the colony containing the spawner position.
2. If no colony contains it, find the nearest colony within a bounded,
   configurable distance. Start with 128 blocks and validate the distance in
   playtesting.
3. Read and save the colony's citizen texture style in the
   `minecolonies.style` additional property.
4. Use the existing variation seed to select gender, role family, texture
   index, and texture suffix deterministically. Persist those selections in
   `minecolonies.model_type`, `minecolonies.gender`,
   `minecolonies.texture_index`, and `minecolonies.texture_suffix`.
5. If no suitable colony exists, make the appearance inapplicable so another
   enabled appearance or Default can be selected.

The saved style must remain stable if the entity leaves the colony. Changing a
colony's style should affect newly spawned Customers and Suppliers, not rewrite
the appearances of already spawned entities.

### Customer role pool

Customers should look like residents without falsely presenting them as
working MineColonies employees.

Use this default weighted pool:

* Citizen: common
* Settler: common
* Noble: uncommon
* Aristocrat: rare

Do not use child, student, guard, or job-specific uniforms by default. They
suggest age, authority, or employment that the Customer does not have.

An advanced add-on configuration may allow pack authors to replace this pool
with any model types registered through MineColonies'
`IModelTypeRegistry`. Missing or unsupported model types must be ignored with a
warning and a civilian fallback.

### Supplier role pool

Use the courier model as the preferred Supplier role because it visually
communicates delivery and logistics. Use citizen and settler as fallbacks.

An advanced configuration may allow production roles such as crafter,
blacksmith, cook, baker, farmer, and carpenter. Guards and children should
remain excluded from the default Supplier pool.

## Colony matching

Use the public MineColonies API:

* `IColonyManager.getInstance()`
* `IColonyManager.getIColony(Level, BlockPos)` for a containing colony
* `IColonyManager.getClosestIColony(Level, BlockPos)` for a fallback
* `IColony.getCenter()` to enforce the add-on's maximum fallback distance
* `IColony.getTextureStyleId()` for the citizen style

Run this lookup on the logical server during appearance selection. Client
colony views include only data known to the local player and are not reliable
for persistent appearance selection.

Do not repeatedly query colonies while rendering. Read the synchronized
additional properties selected once when the entity spawned.

## Rendering approach

Use MineColonies' public model registry where possible:

* `IModelTypeRegistry`
* `IModelType`
* `ISimpleModelType`
* `ModModelTypes`

The main challenge is that `IModelType.getTexture` and its male and female
models are typed for `AbstractEntityCitizen`. MineColonies'
`RenderBipedCitizen` is also an internal core renderer.

The implemented renderer uses a lightweight `EntityCitizen` render proxy and
delegates to `RenderBipedCitizen`, following Customers' existing MCA proxy
pattern. It does not copy MineColonies model or renderer source into Customers.

The proxy must synchronize position, rotation, movement, equipment, pose,
damage animation, visibility, glowing state, name, and Customers sitting state.
It must never be added to the world or saved.

Child and student models use the Customers client appearance sitting offset to
move their render 0.5 blocks higher and 0.125 blocks forward relative to body
yaw while seated. This is a visual adjustment and does not move the shared
Customer seat entity. Their overhead wanted-item display uses the client
appearance name-tag offset hook to render 0.75 blocks lower at all times.

Use MineColonies textures directly from its resource namespace. Check that the
selected resource exists and fall back to the same model's `default` style
texture when a custom style is incomplete.

Do not use the internal `TextureReloadListener.TEXTURE_PACKS` list. If style
discovery is needed for diagnostics, scan the client resource manager under
`textures/entity/citizen` in the add-on. Normal selection needs only the style
saved from the colony.

The MineColonies Halloween override needs an explicit decision during the
prototype. Prefer matching MineColonies' displayed citizens when its holiday
feature is enabled, but do not depend on internal cached fields to do so.

Use vanilla villager sounds initially unless MineColonies exposes a stable
sound policy suitable for non-citizen entities.

## Customers appearance API work

Customers exposes the context needed by this add-on through
`CustomersVillager`:

* `getCustomersLevel()` provides the logical level.
* `getCustomersSpawnerPosition()` provides the originating spawner position.
* `getAdditionalProperties()` provides immutable, synchronized,
  appearance-specific state.

The registered appearance keeps the single stable ID
`customers_minecolonies_appearance:local_colony`. When Customers selects it,
`CustomersVillagerAppearance.getAdditionalProperties(CustomersVillager)`
resolves the local colony and all persistent visual choices on the logical
server. Customers persists and synchronizes the returned key-value pairs
generically.

Renderers must read the style, model type, gender, texture index, and texture
suffix from those properties. Style strings and other externally sourced
values must be validated before use in resource paths. Unknown or malformed
values fall back to safe civilian defaults.

## Add-on structure

Create a separately published NeoForge mod with required dependencies on:

* Customers
* Architectury API
* MineColonies
* MineColonies' normal required dependencies

Use mod ID `customers_minecolonies_appearance`, Maven group
`com.vikingkittens.mc.customersaddon`, and Java package
`com.vikingkittens.mc.customersaddon.appearance.minecolonies`.

The main Customers JAR should not depend on MineColonies, Structurize, BlockUI,
or other MineColonies libraries.

Keep server colony/style resolution separate from client renderer code. Do not
redistribute MineColonies textures, models, or other assets.

Because MineColonies is GPLv3, review the add-on's license before publishing.
A separately distributed GPL-compatible add-on provides a clearer boundary
than embedding the integration in the Apache-licensed Customers JAR.

## Configuration

Register a NeoForge common config and expose it through the standard mod config
screen. The implemented common config contains:

* Maximum nearby-colony distance, default `128`.
* Enable noble Customers, default `true`.
* Enable aristocrat Customers, default `true`.
* Enable job-specific Customer models, default `false`.
* Enable production-role Suppliers, default `false`.
* Enable guard models, default `false`.
* Enable child and student models, default `false`.

Each optional group is filtered from the loaded model registry. Missing model
types are ignored with a warning, and empty pools fall back to civilian models.

## Compatibility risks

* The colony and model registries are public API, but binary stability across
  MineColonies releases is not guaranteed.
* Some public API interfaces currently refer directly to NeoForge event types,
  so this is not a loader-neutral dependency.
* `RenderBipedCitizen`, its layers, model initialization, and texture reload
  listener are internal implementation classes.
* `IModelType.getTexture` requires an `AbstractEntityCitizen`.
* Resource packs can add incomplete styles or omit textures for particular
  roles and genders.
* MineColonies' custom player/supporter textures belong to individual citizen
  records and should not be copied to Customers.
* Holiday rendering can temporarily override the colony's selected style.
* MineColonies updates frequently, so tested dependency ranges should be
  conservative.

The integration should compile primarily against `com.minecolonies.api`.
Every use of a `core` or `apiimp` class must be isolated, documented, and
covered by a production-client compatibility test.

## Test plan

### Automated tests

* A containing colony is preferred over a merely nearby colony.
* A nearest colony outside the configured radius is rejected.
* No nearby colony makes the appearance inapplicable.
* The colony's texture style, not its structure pack, is saved.
* Civilian and Supplier role weighting is deterministic for a variation seed.
* Excluded roles never enter default pools.
* Unknown roles and missing custom-style textures fall back safely.
* Style, model, gender, texture-index, and suffix properties survive Customers
  save/load.
* Only the registered local-colony appearance appears in spawner choices.

Tests should call the production resolver and selector code rather than
reimplementing their behavior in test-only helpers.

### Manual tests

* Test all eight built-in citizen styles.
* Test a resource pack that adds another citizen style.
* Change a colony style and verify only newly spawned villagers use it.
* Test the four default Customer role models and courier Suppliers.
* Test both genders and all texture suffix variants.
* Test incomplete style packs and missing role textures.
* Test equipment, name tags, overhead wanted items, movement, damage, death,
  invisibility, glowing, and sitting.
* Test inside a colony, just outside its boundary, between two colonies, and
  with no colony nearby.
* Test dedicated server and production NeoForge client builds.

## Built-in versus separate add-on

### Built into Customers

Pros:

* Simplest installation and configuration for players.
* Direct access to internal Customers renderer and persistence code.
* Similar presentation to the built-in MCA appearance.

Cons:

* Adds a NeoForge-only dependency path to a multi-loader project.
* MineColonies brings a large and frequently changing dependency graph.
* Internal rendering changes could delay unrelated Customers releases.
* Combining GPLv3 integration code into the Apache-licensed main project needs
  careful licensing review.
* Other Customers versions and loaders would carry code they cannot use.

### Separate add-on

Pros:

* Can pin MineColonies versions and release independently.
* Keeps loader-specific and GPL-related code outside the main JAR.
* Avoids dependencies for players who do not use MineColonies.
* Serves as a real-world validation of Customers' appearance extensibility.

Cons:

* Players install and update another mod.
* Requires the contextual-selection extension in Customers.
* Adds a separately tested and published artifact.

Outcome: this project implements the recommended separate MineColonies
appearance add-on. It also validates the contextual appearance API with a
public colony lookup and an upstream render-proxy integration.

## Research sources

* [MineColonies on CurseForge](https://www.curseforge.com/minecraft/mc-mods/minecolonies)
* [MineColonies Minecraft 1.21 branch](https://github.com/ldtteam/MineColonies/tree/version/1.21)
* [IColonyManager](https://github.com/ldtteam/MineColonies/blob/version/1.21/src/main/java/com/minecolonies/api/colony/IColonyManager.java)
* [IColony](https://github.com/ldtteam/MineColonies/blob/version/1.21/src/main/java/com/minecolonies/api/colony/IColony.java)
* [IModelType](https://github.com/ldtteam/MineColonies/blob/version/1.21/src/main/java/com/minecolonies/api/client/render/modeltype/IModelType.java)
* [ISimpleModelType](https://github.com/ldtteam/MineColonies/blob/version/1.21/src/main/java/com/minecolonies/api/client/render/modeltype/ISimpleModelType.java)
* [MineColonies license](https://github.com/ldtteam/MineColonies/blob/version/1.21/LICENSE)
