package nl.jeroennijs.adventofcode2017;

import java.math.BigInteger;
import java.util.*;



public class Day14 {
    private static final String INPUT = "nbysizxe";
    private static final String EMPTYROW = emptyRow();
    private static final int GRIDSIZE = 128;

    public static void main(String[] args) {
        final List<String> grid = createGrid();
        int used = grid.stream()
            .mapToInt(row -> row.replaceAll("0", "").length())
            .sum();
        System.out.println("Squares used: " + used); // 8216

        int groups = countGroups(grid);
        System.out.println("Groups found: " + groups); // 1139
    }

    private static int countGroups(List<String> grid) {
        int[][] groupGrid = createGroupGrid(grid);
        deduplicate(groupGrid);
        int count = countGroups(groupGrid);
        return count;
    }

    private static void deduplicate(int[][] grid) {
        for (int y = 0; y < GRIDSIZE; y++) {
            for (int x = 0; x < GRIDSIZE; x++) {
                if (x > 0) {
                    replaceValue(grid, grid[y][x], grid[y][x - 1]);
                }
                if (y < GRIDSIZE - 1) {
                    replaceValue(grid, grid[y][x], grid[y + 1][x]);
                }
            }
        }
    }

    private static void replaceValue(int[][] grid, int value, int replace) {
        if (value != 0 && replace != 0 && value != replace) {
            for (int y = 0; y < GRIDSIZE; y++) {
                for (int x = 0; x < GRIDSIZE; x++) {
                    if (grid[y][x] == value) {
                        grid[y][x] = replace;
                    }
                }
            }
        }
    }

    private static int[][] createGroupGrid(List<String> grid) {
        int[][] groupGrid = new int[GRIDSIZE][GRIDSIZE];
        int highestGroupInFirstRow = createFirstRow(groupGrid, grid.get(0));
        return createOtherRows(groupGrid, grid, highestGroupInFirstRow + 1);
    }

    private static int createFirstRow(int[][] groupGrid, String row) {
        int group = 1;
        if (row.charAt(0) == '1') {
            groupGrid[0][0] = group;
        }
        for (int x = 1; x < GRIDSIZE; x++) {
            if (row.charAt(x) == '1') {
                if (row.charAt(x - 1) == '1') {
                    groupGrid[0][x] = groupGrid[0][x - 1];
                } else {
                    group++;
                    groupGrid[0][x] = group;
                }

            }
        }
        return group;
    }

    private static int[][] createOtherRows(int[][] groupGrid, List<String> grid, int startGroup) {
        int group = startGroup;
        for (int y = 1; y < GRIDSIZE; y++) {
            String row = grid.get(y);
            for (int x = 0; x < GRIDSIZE; x++) {
                if (row.charAt(x) == '1') {
                    final int valueAbove = groupGrid[y - 1][x];
                    if (valueAbove != 0) {
                        groupGrid[y][x] = valueAbove;
                    } else if (x == 0) {
                        groupGrid[y][x] = group;
                    } else {
                        final int valueLeft = groupGrid[y][x - 1];
                        if (valueLeft != 0) {
                            groupGrid[y][x] = valueLeft;
                        } else {
                            group++;
                            groupGrid[y][x] = group;
                        }
                    }

                }
            }
            group++;
        }
        return groupGrid;
    }

    private static int countGroups(int[][] groupGrid) {
        Set<Integer> groupsCounted = new HashSet<>();
        for (int i = 0; i < GRIDSIZE; i++) {
            for (int j = 0; j < GRIDSIZE; j++) {
                final int cell = groupGrid[i][j];
                if (cell != 0) {
                    groupsCounted.add(cell);
                    String value = String.valueOf(cell);
                    System.out.print("    ".substring(0, 4 - value.length()) + value);
                } else {
                    System.out.print("    ");
                }
            }
            System.out.println();
        }
        return groupsCounted.size();
    }

    private static List<String> createGrid() {
        final List<String> grid = new ArrayList<>(GRIDSIZE);
        for (int i = 0; i < GRIDSIZE; i++) {
            grid.add(toBinary(new Day10.Rope(256).calculateHash(INPUT + "-" + i)));
        }
        return grid;
    }

    private static String toBinary(String hex) {
        return leftPadWithZero(new BigInteger(hex, 16).toString(2));
    }

    private static String emptyRow() {
        StringBuilder row = new StringBuilder();
        for (int i = 0; i < GRIDSIZE; i++) {
            row.append("0");
        }
        return row.toString();
    }

    private static String leftPadWithZero(String value) {
        return EMPTYROW.substring(0, GRIDSIZE - value.length()) + value;
    }
}
