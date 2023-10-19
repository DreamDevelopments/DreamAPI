package com.github.dreamdevelopments.dreamapi.utils;

public interface Value {

    Value ZERO = new Static(0);
    Value ONE = new Static(1);

    int get();

    record Static(int value) implements Value {
        public int get() {
            return value;
        }

    }

    record Random(int minimum, int maximum) implements Value {
        private static final java.util.Random random = new java.util.Random();

        public int get() {
            return random.nextInt(this.minimum, this.maximum);
        }
    }
}
