# Method index

Every ZenScript method Zenworks adds, by exact name, with the release that added it.

Examples in the catalog run inside an event handler where `player` is the `crafttweaker.player.IPlayer` who acted and `target` is a `crafttweaker.entity.IEntity`, as in `events.onPlayerInteractEntity`, where they are `event.player` and `event.target`.

Rules for example code:

- It runs as the body of a function, so it has no `import` or `function` lines; use full type names such as `crafttweaker.potions.IPotion`.
- It may run on the client side as well as the server side, so it must be safe there: Zenworks methods that change the world do nothing on the client, but plain CraftTweaker calls may not.
- It prints what it did with `print`, so the result can be read in `crafttweaker.log`.

| Method | Called on | Group | Added in |
| --- | --- | --- | --- |
| [`addPotionEffectById`](potions.md#addpotioneffectbyid) | `IEntityLivingBase` | Potions | 1.0.0 |
| [`getPotionAmplifierById`](potions.md#getpotionamplifierbyid) | `IEntityLivingBase` | Potions | 1.0.0 |
| [`getPlayersInRadius`](entities.md#getplayersinradius) | `IEntity` | Entities | 1.0.0 |
| [`asPlayer`](entities.md#asplayer) | `IEntity` | Entities | 1.0.0 |

## CraftTweaker equivalents

Needs that CraftTweaker 4.1.20 already meets; the catalog shows how.

| CraftTweaker member | Called on | Entry |
| --- | --- | --- |
| `getAttribute` | `IEntityLivingBase` | [entities.md](entities.md#getattribute) |
| `isSneaking` | `IEntity` | [entities.md](entities.md#issneaking) |
| `isActiveItemStackBlocking` | `IEntityLivingBase` | [entities.md](entities.md#isactiveitemstackblocking) |
| `absorptionAmount` | `IEntityLivingBase` | [entities.md](entities.md#absorptionamount) |
| `health` (as `health / maxHealth`) | `IEntityLivingBase` | [entities.md](entities.md#health) |
| `attackEntityFrom` (with `IDamageSource.createEntityDamage`) | `IEntity` | [entities.md](entities.md#attackentityfrom) |

See also [the agent guide](../agent-guide.md).
