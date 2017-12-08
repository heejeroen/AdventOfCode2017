package nl.jeroennijs.adventofcode2017;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Day08 {
    private static final Pattern PATTERN = Pattern.compile("([a-z]+) (inc|dec) (-?[0-9]+) if ([a-z]+) ([=!<>]+) (-?[0-9]+)");

    public static void main(String[] args) throws IOException {
        final Map<String, Integer> registers = new HashMap<>();
        int highestValueDuringExecution = executeInstructions(registers, "day08.txt");
        int highestValueAtEnd = determineHighestValue(registers);
        System.out.println("Highest value at end: " + highestValueAtEnd); // 5966
        System.out.println("Highest value during execution: " + highestValueDuringExecution); // 6347
    }

    private static int executeInstructions(final Map<String, Integer> registers, String fileName) throws IOException {
        return Utils.readLines(fileName).stream()
            .map(PATTERN::matcher)
            .filter(Matcher::matches)
            .filter(matcher -> checkCondition(registers.getOrDefault(matcher.group(4), 0), matcher.group(5), Integer.valueOf(matcher.group(6))))
            .map(matcher -> executeInstruction(registers, matcher.group(1), Integer.valueOf(matcher.group(3)), "dec".equals(matcher.group(2))))
            .max(Integer::compareTo).orElse(0);
    }

    private static Integer executeInstruction(Map<String, Integer> registers, String registerToModify, Integer value, boolean isDecrease) {
        registers.put(registerToModify, registers.getOrDefault(registerToModify, 0) + (isDecrease ? -value : value));
        return determineHighestValue(registers);
    }

    private static boolean checkCondition(Integer value, String conditionOperand, Integer conditionValue) {
        switch (conditionOperand) {
            case "<":
                return value < conditionValue;
            case ">":
                return value > conditionValue;
            case "<=":
                return value <= conditionValue;
            case ">=":
                return value >= conditionValue;
            case "==":
                return value.equals(conditionValue);
            case "!=":
                return !value.equals(conditionValue);
            default:
                return false;
        }
    }

    private static Integer determineHighestValue(Map<String, Integer> registers) {
        return registers.values().stream().max(Integer::compareTo).orElse(0);
    }
}
