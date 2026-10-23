package com.mahghuuuls.zenworks;

import net.minecraftforge.fml.common.Mod;

/**
 * Declares the mod and its CraftTweaker requirement. Zenworks has no lifecycle code: CraftTweaker
 * finds the ZenScript expansions through their {@code @ZenRegister} annotation, and nothing runs
 * unless a script calls a method.
 */
@Mod(
        modid = Tags.MOD_ID,
        name = Tags.MOD_NAME,
        version = Tags.VERSION,
        dependencies = "required-after:crafttweaker@[4.1.20,);"
)
public class ZenworksMod {
}
