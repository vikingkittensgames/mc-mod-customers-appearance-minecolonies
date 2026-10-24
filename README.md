# Customers MineColonies Appearance

Customers MineColonies Appearance is an optional NeoForge add-on for
[Customers](https://www.curseforge.com/minecraft/mc-mods/customers). It adds a
`MineColonies - Local Colony` appearance that makes spawned Customers and
Suppliers match the citizen texture style of the colony around their spawner.

The add-on uses MineColonies' own models and textures. It does not copy or
redistribute MineColonies assets.

## Documentation

Check out the full Customers documentation here:

https://vikingkittensgames.github.io/mc-mod-customers-appearance-minecolonies

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
