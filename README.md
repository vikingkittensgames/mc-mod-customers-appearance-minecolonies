# Customers MineColonies Appearance

Customers MineColonies Appearance is an optional NeoForge add-on for
[Customers](https://www.curseforge.com/minecraft/mc-mods/customers). It adds a
`MineColonies - Local Colony` appearance that makes spawned Customers and
Suppliers match the citizen texture style of the colony around their spawner.

The add-on uses MineColonies' own models and textures. It does not copy or
redistribute MineColonies assets.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.230 or newer in the 21.1 line
- Customers 2.1.0 or newer
- Architectury API 13.0.8 or newer in the 13.0 line
- MineColonies 1.1.1368 for Minecraft 1.21.1
- MineColonies' normal required dependencies

This release is intentionally pinned to the MineColonies version it was tested
against. MineColonies changes frequently, including internal rendering code
used by this add-on.

## Player setup

1. Install Customers, Architectury API, MineColonies, its dependencies, and
   this add-on.
2. Place a Customer Spawner or Supplier Spawner inside or near a colony.
3. Open the spawner's appearance settings.
4. Enable `MineColonies - Local Colony`.

The containing colony is preferred. If the spawner is outside every colony,
the nearest colony is used only when its center is within the configured
distance. If no colony qualifies, Customers chooses another enabled applicable
appearance or Default.

The selected style and visual variation are stored on the spawned entity. They
remain stable when it leaves the colony, the colony changes style, or the world
is reloaded. A later colony style change affects newly spawned villagers.

## Appearance choices

By default, Customers use civilian roles with these relative weights:

| Model | Weight |
| --- | ---: |
| Citizen | 4 |
| Settler | 4 |
| Noble | 2 |
| Aristocrat | 1 |

Suppliers prefer the courier model with civilian fallbacks:

| Model | Weight |
| --- | ---: |
| Courier | 4 |
| Citizen | 1 |
| Settler | 1 |

Gender, base texture, and texture suffix are selected deterministically from
the Customer's existing variation seed. The add-on supports MineColonies'
built-in styles and resource-pack styles supplied by the selected colony.

## Configuration

The common config is available through NeoForge's mod configuration screen and
`config/customers_minecolonies_appearance-common.toml`.

| Setting | Default | Effect |
| --- | ---: | --- |
| Maximum nearby-colony distance | 128 | Maximum spherical distance from the spawner to a fallback colony center |
| Enable Noble Customers | true | Adds nobles to the Customer pool |
| Enable Aristocrat Customers | true | Adds aristocrats to the Customer pool |
| Enable job-specific Customers | false | Adds MineColonies profession models to Customers |
| Enable production-role Suppliers | false | Adds production professions to Suppliers |
| Enable guard models | false | Allows archer and knight models |
| Enable child and student models | false | Allows young models; disabled because they imply an age the entity does not have |

Unusual or misleading roles are opt-in. An invalid colony style is replaced by
MineColonies' `default` style, and missing visual properties fall back to a
civilian appearance. When child or student models are enabled, their smaller
models are raised and moved slightly forward while sitting so they align with
the seat rather than clipping into it. Their overhead wanted-item display is
also rendered 0.75 blocks lower to remain close to the smaller model's head.

## Technical behavior

The logical server resolves a colony through MineColonies' public colony API.
Customers persists the resulting style, model, gender, texture index, and
suffix as generic appearance properties and synchronizes them to clients.

On the client, this add-on creates an unspawned MineColonies citizen render
proxy and delegates to MineColonies' renderer. Proxies are weakly cached by the
real Customer or Supplier. They are never added to the level, ticked, or saved.
This retains upstream citizen models, textures, equipment rendering, movement,
damage animation, visibility, glowing, name tags, and sitting behavior.

Implementation and compatibility decisions are recorded in
[appearance-minecolonies.md](appearance-minecolonies.md).

## Building

First build the Customers NeoForge production JAR at
`../neoforge/build/libs/customers-neoforge-1.21.1-2.1.0.jar`, then run:

```text
gradlew spotlessCheck test build
```

`gradlew runClient` starts a development client containing Customers,
Architectury API, MineColonies, its dependencies, and this add-on.

## Compatibility and licensing

This add-on is a separate project so it can follow MineColonies releases
without coupling Customers itself to a NeoForge-only dependency graph. The
add-on is licensed under GPLv3. MineColonies is also GPLv3; refer to its project
for its full license and asset terms.

Report the exact Minecraft, NeoForge, Customers, MineColonies, and add-on
versions with compatibility issues.
