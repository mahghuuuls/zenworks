# Migrating from MahZenUtils

Zenworks replaces the general utilities of MahZenUtils 0.0.4. It does not keep their names or signatures. Each old method below has one replacement: a Zenworks method, or a CraftTweaker member that already does the job.

The stack, cooldown, and mark methods of MahZenUtils (`Stacks`, `addStacks`, `getStacks`, `startCooldown`, `onCooldown`, `markEntity`, `getMarkedEntities`, and the rest) are not covered by Zenworks. They move to Statusworks.

Zenworks is meant to be installed next to MahZenUtils while scripts move over: no Zenworks method has the name of a MahZenUtils method.

| MahZenUtils 0.0.4 | Replacement | Kind |
| --- | --- | --- |
| `entity.asIPlayer()` | [`entity.asPlayer()`](methods/entities.md#asplayer) | Zenworks method |
| `entity.applyPotionEffect(id, ticks, amplifier)` | [`entity.addPotionEffectById(id, ticks, amplifier)`](methods/potions.md#addpotioneffectbyid) | Zenworks method |
| `entity.getPotionAmplifier(id)` | [`entity.getPotionAmplifierById(id)`](methods/potions.md#getpotionamplifierbyid) | Zenworks method |
| `entity.getNearbyPlayers(radius)` | [`entity.getPlayersInRadius(radius)`](methods/entities.md#getplayersinradius) | Zenworks method |
| `entity.getAttributeValue(name, default)` | [`entity.getAttribute(name)`](methods/entities.md#getattribute) | CraftTweaker |
| `PlayerUtils.isSneaking(player)` | [`player.isSneaking`](methods/entities.md#issneaking) | CraftTweaker |
| `entity.isBlocking()` | [`entity.isActiveItemStackBlocking()`](methods/entities.md#isactiveitemstackblocking) | CraftTweaker |
| `entity.getTrueAbsorptionAmount()` | [`entity.absorptionAmount`](methods/entities.md#absorptionamount) | CraftTweaker |
| `entity.getHealthPercentage()` | [`entity.health / entity.maxHealth`](methods/entities.md#health) | CraftTweaker |
| `target.dealCustomDamage(attacker, amount, type)` | [`target.attackEntityFrom(IDamageSource.createEntityDamage(type, attacker), amount)`](methods/entities.md#attackentityfrom) | CraftTweaker |

## Before and after

`asIPlayer`:

```zenscript
// before
val attacker as IPlayer = attackerEntity.asIPlayer();
// after
val attacker as IPlayer = attackerEntity.asPlayer();
```

`asPlayer` also works on the client side, where `asIPlayer` returned `null`.

`applyPotionEffect`:

```zenscript
// before
attacker.applyPotionEffect("minecraft:haste", 200, 1);
// after
attacker.addPotionEffectById("minecraft:haste", 200, 1);
```

Both methods do nothing on the client side and both log an unknown id. The new method also returns `true` when the effect changed and `false` otherwise, refuses amplifiers above 127 and durations below 1, names the script line in its error, and writes each error once per game session.

`getPotionAmplifier`:

```zenscript
// before
val amp = player.getPotionAmplifier("minecraft:absorption");
// after
val amp = player.getPotionAmplifierById("minecraft:absorption");
```

Both return `-1` when the effect is absent. For an unknown id the old method returned `-1` silently; the new one also writes an error.

`getNearbyPlayers`:

```zenscript
// before
val nearbyPlayers = deadEntity.getNearbyPlayers(6.0);
// after
val nearbyPlayers = deadEntity.getPlayersInRadius(6.0);
```

Differences: the new method measures from the entity's position to each player's position (a sphere); the old one grew the entity's hitbox into a box and caught any player whose hitbox touched it, so it reached a little further. The new one leaves dead players out and includes spectators, which the old one left out. It works on any entity, not only living ones, and accepts a radius above 0 and at most 64.

`getAttributeValue`:

```zenscript
// before
val toughness = attacker.getAttributeValue("generic.armorToughness", 0.0);
// after
val instance = attacker.getAttribute("generic.armorToughness");
val toughness as double = isNull(instance) ? 0.0 : instance.attributeValue;
```

`PlayerUtils.isSneaking`:

```zenscript
// before
if (PlayerUtils.isSneaking(player)) { ... }
// after
if (player.isSneaking) { ... }
```

Also delete `import mods.mahzenutils.PlayerUtils;` from the script, or it stops compiling once MahZenUtils is removed.

`isBlocking`:

```zenscript
// before
if (ally.isBlocking()) { ... }
// after
if (ally.isActiveItemStackBlocking()) { ... }
```

`getTrueAbsorptionAmount`:

```zenscript
// before
val absorption = attacker.getTrueAbsorptionAmount();
// after
val absorption = attacker.absorptionAmount;
```

`getHealthPercentage`:

```zenscript
// before
if (defender.getHealthPercentage() <= 0.3) { ... }
// after
if (defender.health / defender.maxHealth <= 0.3) { ... }
```

`dealCustomDamage`:

```zenscript
// before
livingEnemy.dealCustomDamage(attacker, splashAmount, "ruby_splash");
// after
livingEnemy.attackEntityFrom(crafttweaker.damage.IDamageSource.createEntityDamage("ruby_splash", attacker), splashAmount);
```

Call it on the server side, as before.
