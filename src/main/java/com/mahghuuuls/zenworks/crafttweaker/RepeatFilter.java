package com.mahghuuuls.zenworks.crafttweaker;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Remembers which messages were already reported so each one is reported once. The memory is
 * bounded: past {@code capacity} keys it forgets the key seen least recently. A message that keeps
 * repeating stays recent and stays suppressed; only a message unseen for {@code capacity} other
 * messages can be reported again.
 */
final class RepeatFilter {

    private final Map<String, Boolean> seen;

    RepeatFilter(final int capacity) {
        this.seen = new LinkedHashMap<String, Boolean>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Boolean> eldest) {
                return size() > capacity;
            }
        };
    }

    /** True when {@code key} should be reported now. */
    synchronized boolean shouldReport(String key) {
        return seen.put(key, Boolean.TRUE) == null;
    }
}
