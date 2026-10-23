package com.mahghuuuls.zenworks.rules;

import java.util.Locale;

/**
 * Argument rules for the potion methods: the accepted duration and amplifier ranges and how a
 * script's potion id is turned into a registry name. Plain Java so the limits can be unit-tested.
 */
public final class PotionRules {

    public static final int MIN_DURATION_TICKS = 1;
    public static final int MIN_AMPLIFIER = 0;
    /**
     * Minecraft saves and sends the amplifier as a signed byte, so 128 and above come back as
     * negative levels after a reload and on the client.
     */
    public static final int MAX_AMPLIFIER = 127;

    private static final String DEFAULT_NAMESPACE = "minecraft";

    private PotionRules() {
    }

    public static boolean isValidDuration(int durationTicks) {
        return durationTicks >= MIN_DURATION_TICKS;
    }

    public static boolean isValidAmplifier(int amplifier) {
        return amplifier >= MIN_AMPLIFIER && amplifier <= MAX_AMPLIFIER;
    }

    /**
     * Returns the registry name for a script's potion id, or null when the id cannot be one. An id
     * without a namespace means {@code minecraft:}; registry names are lowercase, so the id is too.
     */
    public static String normalizeId(String potionId) {
        if (potionId == null) {
            return null;
        }
        String id = potionId.trim().toLowerCase(Locale.ROOT);
        int colon = id.indexOf(':');
        if (colon < 0) {
            return id.isEmpty() ? null : DEFAULT_NAMESPACE + ":" + id;
        }
        boolean oneColon = colon == id.lastIndexOf(':');
        boolean bothParts = colon > 0 && colon < id.length() - 1;
        return oneColon && bothParts ? id : null;
    }
}
