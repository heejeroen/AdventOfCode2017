package nl.jeroennijs.adventofcode2017;

import java.io.IOException;
import java.util.function.BiFunction;

import static nl.jeroennijs.adventofcode2017.Utils.readLines;



public class Day01 {
    public static void main(String[] args) throws IOException {
        System.out.println("Total for part 1: " + calculateTotal((i, length) -> (i + 1) % length)); // 1034
        System.out.println("Total for part 2: " + calculateTotal((i, length) -> (i + length / 2) % length)); // 1356
    }

    private static long calculateTotal(BiFunction<Integer, Integer, Integer> nextIndex) throws IOException {
        long total = 0L;
        final String line = readLines("day01.txt")[0];
        final int length = line.length();
        for (int i = 0; i < length; i++) {
            if (line.charAt(i) == line.charAt(nextIndex.apply(i, length)))
                total += Integer.valueOf(line.substring(i, i + 1));
        }
        return total;
    }
}
