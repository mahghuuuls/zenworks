# Guide for coding agents

Read this before writing ZenScript that uses Zenworks.

1. **Find methods in the catalog.** The index of every Zenworks method, by exact name, is `docs/methods/index.md`. Each name links to its entry in `docs/methods/<group>.md` (for example `docs/methods/potions.md`). The index also lists needs that CraftTweaker itself already meets, with the CraftTweaker member to use.
2. **Copy the exact signature.** Each Zenworks method entry starts with the exact ZenScript signature and the type it is called on; each CraftTweaker equivalent entry shows the CraftTweaker form to use. Use that signature as written: method name, argument order, argument types, and return type.
3. **Check what the method needs.** Each Zenworks method entry lists the mods it requires, the side it runs on, and the release that added it. Use only methods added in the Zenworks version the pack has installed or an earlier one.
4. **Do not invent signatures.** If a method is not in the index, Zenworks does not have it. Do not guess a name, an overload, or an argument from another mod. Say that the method does not exist, or use the CraftTweaker member the catalog names.
