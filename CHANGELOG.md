# Changelog

## [1.0.0] - 2026-10-24

### Added

- `addPotionEffectById(potionId, durationTicks, amplifier)` on living entities: adds a potion effect named by a string; returns whether the effect changed; amplifier 0 to 127.
- `getPotionAmplifierById(potionId)` on living entities: the level of an active effect, or `-1` when absent.
- `getPlayersInRadius(radius)` on any entity: living players within a sphere of radius above 0 and at most 64.
- `asPlayer()` on any entity: the entity as a player, or `null`.
- Wrong arguments write one error to `crafttweaker.log` naming the method, the value, and the script line, once per game session.
- Method catalog with examples and a guide for coding agents, under `docs/`.

### Compatibility

- Requires CraftTweaker 4.1.20 or newer; tested with 4.1.20.715.
- Install on client and server.
