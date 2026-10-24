# Potion methods

Methods for reading and adding potion effects on living entities, with the potion named by a string. Use them when a script picks the potion while the game runs, for example from a list of ids. When the potion is fixed in the script, CraftTweaker's `<potion:...>` brackets do the same job without Zenworks.

Potion ids are registry names such as `minecraft:haste` or `potioncore:diamond_skin`. An id without a namespace means `minecraft:`, and case does not matter.

Wrong usage writes one error to `crafttweaker.log`, which CraftTweaker also shows in the chat of players who are online. The same error from the same script line is written once per game session (it can appear again only after more than 1,024 other errors).

## addPotionEffectById

**Added in:** 1.0.0

```zenscript
entity.addPotionEffectById(potionId as string, durationTicks as int, amplifier as int) as bool
```

- **Called on:** `crafttweaker.entity.IEntityLivingBase`, which includes players and mobs.
- **Does:** gives the entity the potion effect for `durationTicks` ticks (20 ticks are one second) at level `amplifier + 1`, with particles shown. When the entity already has that potion, vanilla rules decide: a higher level replaces it, the same level with a longer duration extends it, and anything else leaves the existing effect as it is.
- **Returns:** `true` when the entity's effect of that potion changed (added, or a new level or duration); `false` when nothing changed.
- **Changes:** the entity's active potion effects, on the server.
- **Valid inputs:**
  - `potionId`: the id of a registered potion.
  - `durationTicks`: 1 or more.
  - `amplifier`: 0 to 127 (0 is level I). Higher values are refused because Minecraft saves the level in one signed byte, so they would turn into negative levels after a reload.
- **Invalid inputs:** an unknown id, a duration below 1, or an amplifier outside 0 to 127 changes nothing, returns `false`, and writes one error, for example `[Zenworks] addPotionEffectById: unknown potion id "minecraft:hast" (test.zs:3)`.
- **Other `false` results, without an error:** the call ran on the client side; the game refused the effect for this entity (for example Regeneration on an undead mob, or any effect on the Wither); or the existing effect was kept.
- **Requires:** CraftTweaker. No other mod, except the mod that adds the potion.
- **Runs on:** server side. On the client side it does nothing and returns `false`.
- **Added in:** 1.0.0.
- **Limits and cost:** a valid call is one registry lookup; fine in events and tick handlers. A wrong call also reads the call stack to find the script line, so fix wrong calls rather than leaving them in a tick handler. Instant potions such as `minecraft:instant_health` apply on every tick of their duration, so give them a duration of 1.

**Example:**

```zenscript example=potions-add-haste
val added as bool = player.addPotionEffectById("minecraft:haste", 200, 1);
print("addPotionEffectById haste returned " ~ added);
val haste as crafttweaker.potions.IPotionEffect = player.getActivePotionEffect(<potion:minecraft:haste>);
if (isNull(haste)) {
    print("haste: none");
} else {
    print("haste: amplifier " ~ haste.amplifier ~ ", duration " ~ haste.duration);
}
```

**Existing-equivalent check (2026-10-22, CraftTweaker 4.1.20.715; also checked: LootTweaker, GameStages, BlockDropsTweaker, roidtweaker, wizardryspelltweaker, ctsetbonus, jawms-integrations from the target pack, none relevant):**

- Result: convenience helper.
- Existing alternatives:
  - A fixed potion: `entity.addPotionEffect(<potion:minecraft:haste>.makePotionEffect(200, 1));`
  - A potion chosen while the game runs: keep the choices as brackets in an array, `val buffs = [<potion:minecraft:speed>, <potion:minecraft:haste>] as crafttweaker.potions.IPotion[];`, and pick from it.
- Benefit: the potion can come from a string, such as a config value, a list built elsewhere, or another mod's id that may not be installed, which brackets cannot express (a bracket is resolved when the script compiles, and CraftTweaker has no method that finds a potion by a string). It also refuses wrong values with a clear error, does nothing on the client side, and reports whether anything changed.

**Example of wrong usage** (shows the error lines; each appears once per game session):

```zenscript example=potions-add-wrong-usage
print("wrong id returned " ~ player.addPotionEffectById("minecraft:hast", 20, 0));
print("wrong amplifier returned " ~ player.addPotionEffectById("minecraft:haste", 20, 128));
```

## getPotionAmplifierById

**Added in:** 1.0.0

```zenscript
entity.getPotionAmplifierById(potionId as string) as int
```

- **Called on:** `crafttweaker.entity.IEntityLivingBase`, which includes players and mobs.
- **Does:** reads the level of the entity's active effect of that potion.
- **Returns:** the amplifier (0 is level I), or `-1` when the entity does not have the effect.
- **Changes:** nothing.
- **Valid inputs:** `potionId`: the id of a registered potion.
- **Invalid inputs:** an unknown id returns `-1` and writes one error, for example `[Zenworks] getPotionAmplifierById: unknown potion id "minecraft:hast" (test.zs:3)`.
- **Other `-1` results, without an error:** the entity does not have the effect.
- **Requires:** CraftTweaker. No other mod, except the mod that adds the potion.
- **Runs on:** both sides. It returns what that side knows; the server's answer is the one to trust, because the client may not know other entities' effects.
- **Added in:** 1.0.0.
- **Limits and cost:** a valid call is one registry lookup and one map read; fine in events and tick handlers. An effect given a level above 128 some other way (for example `/effect`, which allows up to 255) reads back as a negative number after a reload and on the client, and level 256 (amplifier 255) reads `-1` like an absent effect.

**Example:**

```zenscript example=potions-amplifier
player.addPotionEffectById("minecraft:absorption", 200, 1);
print("getPotionAmplifierById absorption returned " ~ player.getPotionAmplifierById("minecraft:absorption"));
val absorption as crafttweaker.potions.IPotionEffect = player.getActivePotionEffect(<potion:minecraft:absorption>);
if (isNull(absorption)) {
    print("CraftTweaker reads absorption: none");
} else {
    print("CraftTweaker reads absorption amplifier " ~ absorption.amplifier);
}
print("getPotionAmplifierById levitation returned " ~ player.getPotionAmplifierById("minecraft:levitation"));
```

**Example of wrong usage:**

```zenscript example=potions-amplifier-wrong-usage
print("wrong id amplifier read returned " ~ player.getPotionAmplifierById("minecraft:hast"));
```

**Existing-equivalent check (2026-10-22, CraftTweaker 4.1.20.715; also checked: LootTweaker, GameStages, BlockDropsTweaker, roidtweaker, wizardryspelltweaker, ctsetbonus, jawms-integrations from the target pack, none relevant):**

- Result: convenience helper.
- Existing alternative: `val effect = entity.getActivePotionEffect(<potion:minecraft:absorption>);` then `isNull(effect)` and `effect.amplifier`.
- Benefit: the potion can come from a string chosen while the game runs, and the absent case is one documented value instead of a null check.
