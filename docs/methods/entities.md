# Entity methods

Methods on any entity: finding players around it and turning it into a player.

## getPlayersInRadius

**Added in:** 1.0.0

```zenscript
entity.getPlayersInRadius(radius as double) as crafttweaker.player.IPlayer[]
```

- **Called on:** `crafttweaker.entity.IEntity`, so also on living entities and players.
- **Does:** finds the living players in the entity's world whose position is at most `radius` blocks from the entity's position. The area is a sphere around the entity, not a box. When the entity is itself a living player, it is in the result. Players in creative and spectator mode count; dead players do not.
- **Returns:** an array of players; empty when no player is in range. Never null.
- **Changes:** nothing.
- **Valid inputs:** `radius`: above 0 and at most 64.
- **Invalid inputs:** a radius of 0 or less, above 64, or not a number returns an empty array and writes one error, for example `[Zenworks] getPlayersInRadius: radius must be above 0 and at most 64 "100.0" (test.zs:3)`. CraftTweaker also shows errors in the chat of players who are online. The same error from the same script line is written once per game session (it can appear again only after more than 1,024 other errors).
- **Requires:** CraftTweaker.
- **Runs on:** both sides. It returns what that side knows; the server's answer is the one to trust, because the client only knows players near it.
- **Added in:** 1.0.0.
- **Limits and cost:** not for every tick on many entities: each call goes through every player in the entity's world, so the cost grows with the number of players in that world, not with the radius. Fine in events.

**Example** (on the clicked entity, with a radius that includes a player 3 blocks away and one that does not):

```zenscript example=entities-players-in-radius
val ox as double = player.x - target.x;
val oy as double = player.y - target.y;
val oz as double = player.z - target.z;
print("the acting player is at squared distance " ~ (ox * ox + oy * oy + oz * oz) ~ " from the entity");
for radius in [5.0, 2.0] as double[] {
    val found as crafttweaker.player.IPlayer[] = target.getPlayersInRadius(radius);
    print("getPlayersInRadius " ~ radius ~ " found " ~ found.length ~ " players");
    for p in found {
        val dx as double = p.x - target.x;
        val dy as double = p.y - target.y;
        val dz as double = p.z - target.z;
        print("  " ~ p.name ~ " at squared distance " ~ (dx * dx + dy * dy + dz * dz));
    }
}
```

**Existing-equivalent check (2026-10-22, CraftTweaker 4.1.20.715; also checked: LootTweaker, GameStages, BlockDropsTweaker, roidtweaker, wizardryspelltweaker, ctsetbonus, jawms-integrations from the target pack, none relevant):**

- Result: convenience helper.
- Existing alternative: `entity.world.getEntitiesInArea(start, end)` with two `crafttweaker.util.Position3f` corners, then a loop that keeps entities that are players and converts them, which needs `asPlayer` below because ZenScript cannot cast an entity to a player.
- Benefit: one call instead of building a box, filtering, and converting; a true distance instead of a box; dead players left out; a checked radius.

**Example** (the player finds themself; a radius above 64 is wrong usage):

```zenscript example=entities-players-self-and-wrong-radius
print("getPlayersInRadius 1.0 around the player found " ~ player.getPlayersInRadius(1.0).length ~ " players");
print("getPlayersInRadius 100.0 found " ~ target.getPlayersInRadius(100.0).length ~ " players");
```

## asPlayer

**Added in:** 1.0.0

```zenscript
entity.asPlayer() as crafttweaker.player.IPlayer
```

- **Called on:** `crafttweaker.entity.IEntity`, so also on living entities and players.
- **Does:** gives the same entity as a player, so player methods can be used on it.
- **Returns:** the player, or `null` when the entity is not a player.
- **Changes:** nothing.
- **Valid inputs:** any entity.
- **Invalid inputs:** none; a non-player gives `null`, without an error.
- **Requires:** CraftTweaker.
- **Runs on:** both sides.
- **Added in:** 1.0.0.
- **Limits and cost:** one type check; fine anywhere.

**Example:**

```zenscript example=entities-as-player
val clicked as crafttweaker.player.IPlayer = target.asPlayer();
print("asPlayer on the clicked entity: " ~ (isNull(clicked) ? "null" : clicked.name));
val living as crafttweaker.entity.IEntityLivingBase = player;
print("asPlayer on a living entity that is a player: " ~ living.asPlayer().name);
print("asPlayer on a player: " ~ player.asPlayer().name);
```

**Existing-equivalent check (2026-10-23, CraftTweaker 4.1.20.715):**

- Result: missing capability.
- Checked: `entity instanceof crafttweaker.player.IPlayer` works, but the cast `entity as crafttweaker.player.IPlayer` does not compile (`Cannot cast ZenTypeNative: crafttweaker.entity.IEntity to ZenTypeNative: crafttweaker.player.IPlayer`), so without this method a script cannot use player methods on an entity it got from an event.

## CraftTweaker equivalents

These needs are already met by CraftTweaker 4.1.20 itself, so Zenworks adds no method for them. Each entry shows the CraftTweaker way.

Existing-equivalent check for all entries below (2026-10-22, CraftTweaker 4.1.20.715, read in its source; also checked: LootTweaker, GameStages, BlockDropsTweaker, roidtweaker, wizardryspelltweaker, ctsetbonus, jawms-integrations from the target pack): result existing equivalent.

### getAttribute

- **CraftTweaker way:** `entity.getAttribute(name)` on `IEntityLivingBase` returns a `crafttweaker.entity.AttributeInstance`, or `null` when the entity has no attribute of that name; its `attributeValue` is the value with all modifiers.
- **Runs on:** both sides; the server's value is the one to trust.

```zenscript example=equivalents-attribute
val toughness as crafttweaker.entity.AttributeInstance = player.getAttribute("generic.armorToughness");
print("armorToughness: " ~ (isNull(toughness) ? "missing" : "" ~ toughness.attributeValue));
val unknown as crafttweaker.entity.AttributeInstance = player.getAttribute("zenworks.example.none");
print("unknown attribute: " ~ (isNull(unknown) ? "missing" : "" ~ unknown.attributeValue));
```

### isSneaking

- **CraftTweaker way:** the `isSneaking` getter on any `IEntity`.
- **Runs on:** both sides.

```zenscript example=equivalents-sneaking
print("isSneaking: " ~ player.isSneaking);
```

### isActiveItemStackBlocking

- **CraftTweaker way:** `entity.isActiveItemStackBlocking()` on `IEntityLivingBase`: true once the entity has held up a blocking item, such as a shield, for about 5 ticks.
- **Runs on:** both sides.

```zenscript example=equivalents-blocking
print("isActiveItemStackBlocking: " ~ player.isActiveItemStackBlocking());
```

### absorptionAmount

- **CraftTweaker way:** the `absorptionAmount` getter on `IEntityLivingBase`: the extra hearts from absorption, in health points.
- **Runs on:** both sides.

```zenscript example=equivalents-absorption
print("absorptionAmount: " ~ player.absorptionAmount);
```

### health

- **CraftTweaker way:** `entity.health / entity.maxHealth` on `IEntityLivingBase`, a value from 0 to 1. Forge keeps maximum health above 0, so the division is safe.
- **Runs on:** both sides.

```zenscript example=equivalents-health
print("health " ~ player.health ~ " of " ~ player.maxHealth ~ " is " ~ (player.health / player.maxHealth));
```

### attackEntityFrom

- **CraftTweaker way:** `target.attackEntityFrom(crafttweaker.damage.IDamageSource.createEntityDamage(type, attacker), amount)` on any `IEntity`. It builds the same kind of damage source the old method built: a named damage type caused by the attacker. Returns true when the damage was dealt.
- **Runs on:** call it on the server; on the client it deals no damage and returns false. Creative players take no damage.

```zenscript example=equivalents-damage
val hit as bool = target.attackEntityFrom(crafttweaker.damage.IDamageSource.createEntityDamage("zenworks_example", player), 1.0);
print("attackEntityFrom 1.0 returned " ~ hit);
```
