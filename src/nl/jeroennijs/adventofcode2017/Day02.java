package nl.jeroennijs.adventofcode2017;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

import static nl.jeroennijs.adventofcode2017.Utils.readLines;

public class Day02 {
    public static void main(String[] args) throws IOException {
        System.out.println("Checksum for part 1: " + calculateChecksum(Day02::calculateLineTotalForPart1)); // 45158
        System.out.println("Checksum for part 2: " + calculateChecksum(Day02::calculateLineTotalForPart2)); // 294
    }

    private static int calculateChecksum(ToIntFunction<String[]> calculateLineTotal) throws IOException {
        return Arrays.stream(readLines("day02.txt"))
                .map(line -> line.split("\t"))
                .mapToInt(calculateLineTotal)
                .sum();
    }

    private static int calculateLineTotalForPart1(String[] stringNumbers) {
        int max = asNumbers(stringNumbers).max().orElse(0);
        int min = asNumbers(stringNumbers).min().orElse(0);
        return max - min;
    }

    private static int calculateLineTotalForPart2(String[] stringNumbers) {
        int[] numbers = asNumbers(stringNumbers).toArray();
        for (int i = 0; i < numbers.length; i++) {
            for (int j = 0; j < numbers.length; j++) {
                if (i == j) continue;
                if (numbers[i] % numbers[j] == 0) return numbers[i] / numbers[j];
            }
        }
        return 0;
    }

    private static IntStream asNumbers(String[] stringNumbers) {
        return Arrays.stream(stringNumbers).mapToInt(Integer::valueOf);
    }
}
