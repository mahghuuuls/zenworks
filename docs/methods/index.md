# Method index

Every ZenScript method Zenworks adds, by exact name. Entries marked unreleased are not in a published release yet.

Examples in the catalog run inside an event handler where `player` is the `crafttweaker.player.IPlayer` who acted and `target` is a `crafttweaker.entity.IEntity`, as in `events.onPlayerInteractEntity`, where they are `event.player` and `event.target`.

Rules for example code:

- It runs as the body of a function, so it has no `import` or `function` lines; use full type names such as `crafttweaker.potions.IPotion`.
- It may run on the client side as well as the server side, so it must be safe there: Zenworks methods that change the world do nothing on the client, but plain CraftTweaker calls may not.
- It prints what it did with `print`, so the result can be read in `crafttweaker.log`.

| Method | Called on | Group | Status |
| --- | --- | --- | --- |
| [`addPotionEffectById`](potions.md#addpotioneffectbyid) | `IEntityLivingBase` | Potions | Unreleased |
| [`getPotionAmplifierById`](potions.md#getpotionamplifierbyid) | `IEntityLivingBase` | Potions | Unreleased |
| [`getPlayersInRadius`](entities.md#getplayersinradius) | `IEntity` | Entities | Unreleased |
| [`asPlayer`](entities.md#asplayer) | `IEntity` | Entities | Unreleased |

## CraftTweaker equivalents

Needs that CraftTweaker 4.1.20 already meets; the catalog shows how.

| CraftTweaker member | Called on | Replaces (MahZenUtils) | Entry |
| --- | --- | --- | --- |
| `getAttribute` | `IEntityLivingBase` | `getAttributeValue` | [entities.md](entities.md#getattribute) |
| `isSneaking` | `IEntity` | `PlayerUtils.isSneaking` | [entities.md](entities.md#issneaking) |
| `isActiveItemStackBlocking` | `IEntityLivingBase` | `isBlocking` | [entities.md](entities.md#isactiveitemstackblocking) |
| `absorptionAmount` | `IEntityLivingBase` | `getTrueAbsorptionAmount` | [entities.md](entities.md#absorptionamount) |
| `health` (as `health / maxHealth`) | `IEntityLivingBase` | `getHealthPercentage` | [entities.md](entities.md#health) |
| `attackEntityFrom` (with `IDamageSource.createEntityDamage`) | `IEntity` | `dealCustomDamage` | [entities.md](entities.md#attackentityfrom) |

See also [the migration page](../migration-from-mahzenutils.md) and [the agent guide](../agent-guide.md).
