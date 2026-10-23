package com.mahghuuuls.zenworks.rules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RadiusRulesTest {

    @Test
    void radiusIsAboveZeroAndAtMost64() {
        assertFalse(RadiusRules.isValidRadius(0.0));
        assertFalse(RadiusRules.isValidRadius(-1.0));
        assertTrue(RadiusRules.isValidRadius(0.001));
        assertTrue(RadiusRules.isValidRadius(64.0));
        assertFalse(RadiusRules.isValidRadius(64.0001));
    }

    @Test
    void radiusThatIsNotANumberOrInfiniteIsRefused() {
        assertFalse(RadiusRules.isValidRadius(Double.NaN));
        assertFalse(RadiusRules.isValidRadius(Double.POSITIVE_INFINITY));
        assertFalse(RadiusRules.isValidRadius(Double.NEGATIVE_INFINITY));
    }

    @Test
    void distanceIsASphereNotABox() {
        assertTrue(RadiusRules.isWithin(3.0, 0.0, 0.0, 5.0));
        assertFalse(RadiusRules.isWithin(3.0, 0.0, 0.0, 2.0));
        // Inside a 5-block box but 6.9 blocks away: outside the sphere.
        assertFalse(RadiusRules.isWithin(4.0, 4.0, 4.0, 5.0));
    }

    @Test
    void exactlyOnTheRadiusCounts() {
        assertTrue(RadiusRules.isWithin(3.0, 4.0, 0.0, 5.0));
        assertFalse(RadiusRules.isWithin(3.0, 4.0, 0.0001, 5.0));
        assertTrue(RadiusRules.isWithin(0.0, 0.0, 0.0, 0.001));
    }
}
