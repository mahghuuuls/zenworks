package com.mahghuuuls.zenworks.crafttweaker;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RepeatFilterTest {

    @Test
    void sameKeyIsReportedOnce() {
        RepeatFilter filter = new RepeatFilter(10);
        assertTrue(filter.shouldReport("a"));
        for (int i = 0; i < 200; i++) {
            assertFalse(filter.shouldReport("a"));
        }
    }

    @Test
    void differentKeysAreEachReported() {
        RepeatFilter filter = new RepeatFilter(10);
        assertTrue(filter.shouldReport("a"));
        assertTrue(filter.shouldReport("b"));
        assertFalse(filter.shouldReport("a"));
    }

    @Test
    void repeatingKeyStaysSuppressedWhileManyOtherKeysPass() {
        RepeatFilter filter = new RepeatFilter(3);
        assertTrue(filter.shouldReport("constant"));
        for (int i = 0; i < 100; i++) {
            assertTrue(filter.shouldReport("changing-" + i));
            assertFalse(filter.shouldReport("constant"));
        }
    }

    @Test
    void keyUnseenForCapacityOtherKeysIsForgotten() {
        RepeatFilter filter = new RepeatFilter(2);
        assertTrue(filter.shouldReport("a"));
        assertTrue(filter.shouldReport("b"));
        assertTrue(filter.shouldReport("c"));
        assertTrue(filter.shouldReport("a"));
        assertFalse(filter.shouldReport("a"));
        assertFalse(filter.shouldReport("c"));
    }
}
