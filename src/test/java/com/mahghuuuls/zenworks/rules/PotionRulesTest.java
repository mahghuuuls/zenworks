package com.mahghuuuls.zenworks.rules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PotionRulesTest {

    @Test
    void durationStartsAtOneTick() {
        assertFalse(PotionRules.isValidDuration(0));
        assertFalse(PotionRules.isValidDuration(-1));
        assertTrue(PotionRules.isValidDuration(1));
        assertTrue(PotionRules.isValidDuration(Integer.MAX_VALUE));
    }

    @Test
    void amplifierIsZeroTo127() {
        assertFalse(PotionRules.isValidAmplifier(-1));
        assertTrue(PotionRules.isValidAmplifier(0));
        assertTrue(PotionRules.isValidAmplifier(127));
        assertFalse(PotionRules.isValidAmplifier(128));
    }

    @Test
    void idWithoutNamespaceMeansMinecraft() {
        assertEquals("minecraft:haste", PotionRules.normalizeId("haste"));
        assertEquals(PotionRules.normalizeId("minecraft:haste"), PotionRules.normalizeId("haste"));
    }

    @Test
    void idKeepsItsNamespaceAndIsLowercased() {
        assertEquals("potioncore:diamond_skin", PotionRules.normalizeId("potioncore:diamond_skin"));
        assertEquals("minecraft:haste", PotionRules.normalizeId(" Minecraft:HASTE "));
    }

    @Test
    void idThatCannotBeARegistryNameIsRejected() {
        assertNull(PotionRules.normalizeId(null));
        assertNull(PotionRules.normalizeId(""));
        assertNull(PotionRules.normalizeId("   "));
        assertNull(PotionRules.normalizeId(":haste"));
        assertNull(PotionRules.normalizeId("minecraft:"));
        assertNull(PotionRules.normalizeId("a:b:c"));
    }
}
