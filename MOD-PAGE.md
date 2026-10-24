Zenworks is my collection of ZenScript utility methods for [CraftTweaker](https://www.curseforge.com/minecraft/mc-mods/crafttweaker). I add a method when one of my own scripts needs it, so the methods are a mixed set and do not follow one theme. Other script authors may find them useful too. The current methods work with potion effects and nearby players.

Zenworks adds no items, blocks, or recipes, and changes nothing until a script calls one of its methods.

<span style="color:#d6a100">**AI usage disclaimer:** This mod was developed with AI-agent assistance using [this agent workflow](https://github.com/mahghuuuls/minecraft-1.12.2-mod-agent-workflow). The project owner reviewed the work during development.</span>

**Methods**

- **addPotionEffectById:** gives a living entity a potion effect named by a string, such as `"minecraft:haste"`, so a script can pick the potion while the game runs. Returns whether the effect changed.
- **getPotionAmplifierById:** reads the level of an entity's potion effect by its id, or `-1` when the entity does not have it.
- **getPlayersInRadius:** finds the living players within a distance of any entity.
- **asPlayer:** turns an entity into a player for player methods, or gives `null` when it is not a player.

A wrong argument, such as an unknown potion id, writes one clear error to `crafttweaker.log` that names the method, the value, and the script line.

**Documentation**

The [method catalog](https://github.com/mahghuuuls/zenworks/blob/main/docs/methods/index.md) lists every method with its exact signature and a working example, and shows the CraftTweaker way for needs CraftTweaker already meets.

**Installation**

Install Zenworks and CraftTweaker on both the client and the server.

**Source:** [github.com/mahghuuuls/zenworks](https://github.com/mahghuuuls/zenworks)
