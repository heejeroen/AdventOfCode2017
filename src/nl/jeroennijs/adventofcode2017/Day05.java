package nl.jeroennijs.adventofcode2017;

import java.io.IOException;
import java.util.function.Function;

import static nl.jeroennijs.adventofcode2017.Utils.readLines;



public class Day05 {
    public static void main(String[] args) throws IOException {
        System.out.println("Steps for part 1: " + calculateSteps(i -> i + 1)); // 394829
        System.out.println("Steps for part 2: " + calculateSteps(i -> i < 3 ? i + 1 : i - 1)); // 31150702
    }

    private static int calculateSteps(Function<Integer, Integer> modifyInstruction) throws IOException {
        final int[] instructions = readLines("day05.txt").stream()
            .mapToInt(Integer::valueOf)
            .toArray();
        int steps = 0;
        int index = 0;
        while (index < instructions.length) {
            int newIndex = index + instructions[index];
            instructions[index] = modifyInstruction.apply(instructions[index]);
            index = newIndex;
            steps++;
        }
        return steps;
    }
}
