package de.kaes3kuch3n.raytracer.utilities;

public class Consts {
    public static final double GAMMA = 2.2;
    public static final double SMALL_VALUE = 0.0000000001;

    public static class  Reflection {
        public static final int MAX_STEPS = 4;
        public static final double WEIGHT_MIN = 0.01;
    }

    public static class Refraction {
        public static final double AIR = 1.0;
        public static final double WATER = 1.3;
        public static final double GLASS = 1.5;
        public static final double DIAMOND = 1.8;

        public static final int MAX_STEPS = 5;
        public static final double WEIGHT_MIN = 0.01;

    }
}
