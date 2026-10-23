package com.mahghuuuls.zenworks.rules;

/**
 * Rules for finding things around an entity: the accepted radius and the distance test. A radius
 * is a sphere around the entity's position, not a box.
 */
public final class RadiusRules {

    /** Large enough for any area effect a script needs, small enough to keep the search cheap. */
    public static final double MAX_RADIUS = 64.0;

    private RadiusRules() {
    }

    /** True for a finite radius above 0 and at most {@link #MAX_RADIUS}; false for NaN. */
    public static boolean isValidRadius(double radius) {
        return radius > 0.0 && radius <= MAX_RADIUS;
    }

    /** True when the offset (dx, dy, dz) is at most {@code radius} long. */
    public static boolean isWithin(double dx, double dy, double dz, double radius) {
        return dx * dx + dy * dy + dz * dz <= radius * radius;
    }
}
