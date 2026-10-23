package com.mahghuuuls.zenworks.crafttweaker;

import crafttweaker.CraftTweakerAPI;

/**
 * The single writer of wrong-usage messages. Each message names the method, the problem, the
 * value the script gave, and the script location, and goes to the CraftTweaker log as an error
 * (CraftTweaker also shows errors in the chat of players who are online). The same message from
 * the same script line is written once per game session, unless more than 1024 other messages
 * pushed it out of the filter's memory.
 */
final class UsageReporter {

    /** Longer values are cut so a huge script string cannot bloat the log or the filter. */
    private static final int MAX_VALUE_LENGTH = 64;

    private static final RepeatFilter FILTER = new RepeatFilter(1024);

    private UsageReporter() {
    }

    /** Reports {@code problem} for {@code value}, for example ("unknown potion id", "minecraft:hast"). */
    static void report(String method, String problem, Object value) {
        String location = CraftTweakerAPI.getScriptFileAndLine();
        String message = "[Zenworks] " + method + ": " + problem + " \"" + shorten(String.valueOf(value)) + "\" (" + location + ")";
        if (FILTER.shouldReport(message)) {
            CraftTweakerAPI.logError(message);
        }
    }

    private static String shorten(String value) {
        return value.length() <= MAX_VALUE_LENGTH ? value : value.substring(0, MAX_VALUE_LENGTH) + "...";
    }
}
