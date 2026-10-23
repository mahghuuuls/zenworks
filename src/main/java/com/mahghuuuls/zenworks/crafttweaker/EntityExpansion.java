package com.mahghuuuls.zenworks.crafttweaker;

import com.mahghuuuls.zenworks.rules.RadiusRules;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.entity.IEntity;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.player.IPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import stanhebben.zenscript.annotations.ZenExpansion;
import stanhebben.zenscript.annotations.ZenMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Methods on every entity. Declared on {@code IEntity}, so they are also callable on living
 * entities and players.
 */
@ZenRegister
@ZenExpansion("crafttweaker.entity.IEntity")
public final class EntityExpansion {

    private static final IPlayer[] NO_PLAYERS = new IPlayer[0];

    private EntityExpansion() {
    }

    /**
     * Returns the living players in the entity's world whose position is at most {@code radius}
     * blocks from the entity's position, including the entity itself when it is a living player.
     * Never null. Reads whatever this side knows. A radius that is not above 0 and at most 64 is
     * reported and returns an empty array.
     */
    @ZenMethod
    public static IPlayer[] getPlayersInRadius(IEntity entity, double radius) {
        if (!RadiusRules.isValidRadius(radius)) {
            UsageReporter.report("getPlayersInRadius", "radius must be above 0 and at most " + (int) RadiusRules.MAX_RADIUS, radius);
            return NO_PLAYERS;
        }
        Entity center = CraftTweakerMC.getEntity(entity);
        if (center == null || center.world == null) {
            return NO_PLAYERS;
        }
        // The world's player list is short, so scanning it costs less than an area search.
        List<IPlayer> found = new ArrayList<>();
        for (EntityPlayer player : center.world.playerEntities) {
            if (player.isEntityAlive()
                    && RadiusRules.isWithin(player.posX - center.posX, player.posY - center.posY, player.posZ - center.posZ, radius)) {
                found.add(CraftTweakerMC.getIPlayer(player));
            }
        }
        return found.toArray(NO_PLAYERS);
    }

    /**
     * Returns the entity as an {@code IPlayer}, or null when it is not a player. ZenScript cannot
     * cast an {@code IEntity} to {@code IPlayer} itself.
     */
    @ZenMethod
    public static IPlayer asPlayer(IEntity entity) {
        Entity internal = CraftTweakerMC.getEntity(entity);
        return internal instanceof EntityPlayer ? CraftTweakerMC.getIPlayer((EntityPlayer) internal) : null;
    }
}
