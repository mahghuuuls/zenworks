package com.mahghuuuls.zenworks.crafttweaker;

import com.mahghuuuls.zenworks.rules.PotionRules;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntityLivingBase;
import crafttweaker.api.minecraft.CraftTweakerMC;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

/**
 * Potion methods on every living entity, players included. Potions are named by a string so a
 * script can choose them while the game runs, which CraftTweaker's {@code <potion:...>} brackets
 * cannot do.
 */
@ZenRegister
@ZenExpansion("crafttweaker.entity.IEntityLivingBase")
public final class PotionExpansion {

    private PotionExpansion() {
    }

    /**
     * Adds the effect, with particles shown, and returns true when the entity's effect of that
     * potion changed: it was added, or its level or duration changed. Returns false without a
     * report when nothing changed: on the client side, where the server owns the entity's effects;
     * when the game refuses the effect for this entity; or when an existing effect wins the vanilla
     * combine. Wrong usage is reported and returns false.
     */
    @ZenMethod
    public static boolean addPotionEffectById(IEntityLivingBase entity, String potionId, int durationTicks, int amplifier) {
        String method = "addPotionEffectById";
        EntityLivingBase living = CraftTweakerMC.getEntityLivingBase(entity);
        if (living == null || living.world.isRemote) {
            return false;
        }
        if (!PotionRules.isValidDuration(durationTicks)) {
            UsageReporter.report(method, "duration below " + PotionRules.MIN_DURATION_TICKS + " tick", durationTicks);
            return false;
        }
        if (!PotionRules.isValidAmplifier(amplifier)) {
            UsageReporter.report(method, "amplifier outside " + PotionRules.MIN_AMPLIFIER + " to " + PotionRules.MAX_AMPLIFIER, amplifier);
            return false;
        }
        Potion potion = findPotion(method, potionId);
        if (potion == null) {
            return false;
        }
        // Vanilla combines into the existing effect object, so read its state before the call.
        PotionEffect before = living.getActivePotionEffect(potion);
        int beforeAmplifier = before == null ? -1 : before.getAmplifier();
        int beforeDuration = before == null ? -1 : before.getDuration();
        living.addPotionEffect(new PotionEffect(potion, durationTicks, amplifier, false, true));
        PotionEffect after = living.getActivePotionEffect(potion);
        return after != null && (after.getAmplifier() != beforeAmplifier || after.getDuration() != beforeDuration);
    }

    /**
     * Returns the amplifier of the entity's active effect of that potion, or -1 when the entity
     * does not have it. Reads whatever this side knows, so the server's answer is authoritative.
     * An unknown id is reported and returns -1.
     */
    @ZenMethod
    public static int getPotionAmplifierById(IEntityLivingBase entity, String potionId) {
        EntityLivingBase living = CraftTweakerMC.getEntityLivingBase(entity);
        Potion potion = findPotion("getPotionAmplifierById", potionId);
        if (living == null || potion == null) {
            return -1;
        }
        PotionEffect effect = living.getActivePotionEffect(potion);
        return effect == null ? -1 : effect.getAmplifier();
    }

    /** Looks up a script's potion id; reports and returns null when it names no potion. */
    private static Potion findPotion(String method, String potionId) {
        String registryName = PotionRules.normalizeId(potionId);
        Potion potion = registryName == null ? null : ForgeRegistries.POTIONS.getValue(new ResourceLocation(registryName));
        if (potion == null) {
            UsageReporter.report(method, "unknown potion id", potionId);
        }
        return potion;
    }
}
