package nl.jeroennijs.adventofcode2017;

import java.io.IOException;

public class Day09 {
    private static int garbage = 0;

    public static void main(String[] args) throws IOException {
        System.out.println("Score for part 1: " + calculateScore(Utils.readLines("day09.txt").get(0), 0, 0)); // 12897
        System.out.println("Score for part 2: " + garbage); // 7031
    }

    private static int calculateScore(String input, int currentIndex, int score) {
        if (input.isEmpty() || input.equals("}")) {
            return score + 1;
        }
        if (input.startsWith("<")) {
            return calculateScore(removeGarbage(input), currentIndex, score);
        }
        if (input.startsWith("{")) {
            return calculateScore(input.substring(1), currentIndex + 1, score);
        }
        if (input.startsWith("},")) {
            return score + currentIndex + calculateScore(input.substring(2), currentIndex - 1, score);
        }
        if (input.startsWith("}")) {
            return score + currentIndex + calculateScore(input.substring(1), currentIndex - 1, score);
        }
        return calculateScore(input.substring(1), currentIndex, score);
    }

    private static String removeGarbage(String input) {
        int index = 1;
        while (true) {
            if (input.charAt(index) == '!') {
                index = index + 2;
                continue;
            }
            if (input.charAt(index) == '>') {
                if (index < input.length() && input.charAt(index + 1) == ',') {
                    return input.substring(index + 2);
                }
                return input.substring(index + 1);
            }
            garbage++;
            index++;
        }
    }
}
