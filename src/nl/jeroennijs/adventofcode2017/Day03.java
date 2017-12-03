package nl.jeroennijs.adventofcode2017;

public class Day03 {
    private static final int gridLength = 19;

    public static void main(String[] args) {
        System.out.println("Result for part 1: " + calculateDistance(368078)); // 371
        System.out.println("Result for part 2: " + calculateFirstValueLargerThan(368078)); // 369601
    }

    private static int calculateDistance(int stepLimit) {
        int stepsToWalk = 0;
        Grid grid = new Grid();
        while (grid.stepsWalked() < stepLimit) {
            stepsToWalk++;
            if (walkUntilStepLimit(grid, 1, 0, stepsToWalk, stepLimit)) break;
            if (walkUntilStepLimit(grid, 0, 1, stepsToWalk, stepLimit)) break;
            stepsToWalk++;
            if (walkUntilStepLimit(grid, -1, 0, stepsToWalk, stepLimit)) break;
            if (walkUntilStepLimit(grid, 0, -1, stepsToWalk, stepLimit)) break;
        }
        return grid.distanceToCenter();
    }

    private static boolean walkUntilStepLimit(Grid grid, int horizontal, int vertical, int stepsToWalk, int stepLimit) {
        for (int i = 0; i < stepsToWalk; i++) {
            if (grid.stepsWalked() == stepLimit) return true;
            grid.walk(horizontal, vertical);
        }
        return false;
    }

    private static int calculateFirstValueLargerThan(int limit) {
        Grid grid = new Grid();
        int stepsToWalk = 0;
        while (true) {
            stepsToWalk++;
            if (walkUntilValueLimit(grid, 1, 0, stepsToWalk, limit)) break;
            if (walkUntilValueLimit(grid, 0, 1, stepsToWalk, limit)) break;
            stepsToWalk++;
            if (walkUntilValueLimit(grid, -1, 0, stepsToWalk, limit)) break;
            if (walkUntilValueLimit(grid, 0, -1, stepsToWalk, limit)) break;
        }
        return grid.getCurrentValue();
    }

    private static boolean walkUntilValueLimit(Grid grid, int horizontal, int vertical, int stepsToWalk, int limit) {
        for (int i = 0; i < stepsToWalk; i++) {
            grid.walk(horizontal, vertical);
            if (grid.calculateAndSetCurrentValue() > limit) return true;
        }
        return false;
    }

    public static class Grid {
        private int x = 0;
        private int y = 0;
        private int[][] grid = new int[gridLength][gridLength];
        private int stepsWalked = 1;

        Grid() {
            setValue(1);
        }

        int stepsWalked() {
            return stepsWalked;
        }

        int distanceToCenter() {
            return Math.abs(x) + Math.abs(y);
        }

        int calculateAndSetCurrentValue() {
            int currentValue = getValue(x + 1, y)
                    + getValue(x + 1, y + 1)
                    + getValue(x, y + 1)
                    + getValue(x - 1, y + 1)
                    + getValue(x - 1, y)
                    + getValue(x - 1, y - 1)
                    + getValue(x, y - 1)
                    + getValue(x + 1, y - 1);
            return setValue(currentValue);
        }

        int getCurrentValue() {
            return getValue(x, y);
        }

        private int getValue(int newX, int newY) {
            return grid[gridLength / 2 + newY][gridLength / 2 + newX];
        }

        void walk(int horizontal, int vertical) {
            x += horizontal;
            y += vertical;
            stepsWalked++;
        }

        private int setValue(int value) {
            grid[gridLength / 2 + y][gridLength / 2 + x] = value;
            return value;
        }
    }
}
