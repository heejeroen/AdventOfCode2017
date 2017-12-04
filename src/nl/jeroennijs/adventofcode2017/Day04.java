package nl.jeroennijs.adventofcode2017;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;

import static nl.jeroennijs.adventofcode2017.Utils.readLines;



public class Day04 {
    public static void main(String[] args) throws IOException {
        System.out.println("Total for part 1: " + calculateTotal(Function.identity())); // 477
        System.out.println("Total for part 2: " + calculateTotal(Day04::sorted)); // 167
    }

    private static long calculateTotal(Function<String, String> transformer) throws IOException {
        return readLines("day04.txt").stream()
            .filter(line -> isValidPassword(line, transformer))
            .count();
    }

    private static boolean isValidPassword(String line, Function<String, String> transformer) {
        final Set<String> uniqueWords = new HashSet<>();
        return Arrays.stream(line.split(" "))
            .allMatch(word -> uniqueWords.add(transformer.apply(word)));
    }

    private static String sorted(String word) {
        final char[] chars = word.toCharArray();
        Arrays.sort(chars);
        return String.valueOf(chars);
    }
}
