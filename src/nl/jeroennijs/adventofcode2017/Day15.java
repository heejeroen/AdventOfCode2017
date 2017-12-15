package nl.jeroennijs.adventofcode2017;

public class Day15 {
    public static void main(String[] args) {
        int matches1 = countMatches(new Generator(512, 16807), new Generator(191, 48271), 40_000_000);
        System.out.println("Part 1 number of matches is " + matches1); // 567

        int matches2 = countMatches(new Generator(512, 16807, 4), new Generator(191, 48271, 8), 5_000_000);
        System.out.println("Part 2 number of matches is " + matches2); // 323
    }

    private static int countMatches(Generator generatorA, Generator generatorB, int iterations) {
        int matches = 0;
        for (int i = 0; i < iterations; i++) {
            if ((generatorA.next() & 0xFFFF) == (generatorB.next() & 0xFFFF))
                matches++;
        }
        return matches;
    }

    private static class Generator {
        private long factor;
        private long current;
        private long divider;
        private boolean useRegular = true;

        Generator(long start, long factor) {
            this(start, factor, 0L);
        }

        Generator(long start, long factor, long divider) {
            this.factor = factor;
            this.current = start;
            this.divider = divider;
            this.useRegular = (this.divider == 0L);
        }

        long next() {
            return useRegular ? nextRegular() : nextDividable();
        }

        private long nextRegular() {
            current = (current * factor) % 2147483647L;
            return current;
        }

        private long nextDividable() {
            while (true) {
                long value = nextRegular();
                if (value % divider == 0)
                    return value;
            }
        }
    }
}
