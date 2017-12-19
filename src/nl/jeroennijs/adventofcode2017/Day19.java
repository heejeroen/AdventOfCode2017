package nl.jeroennijs.adventofcode2017;

import java.io.IOException;



public class Day19 {
    public enum GridDirection {
        N, S, E, W
    }

    public static void main(String[] args) throws IOException {
        char[][] grid = readGrid("day19.txt", 200, 200);
        String lettersVisited = travel(new Grid(grid, 1, 0, GridDirection.S), false);
        System.out.println("Letters visited: " + lettersVisited); // RYLONKEWB

        String steps = travel(new Grid(grid, 1, 0, GridDirection.S), true);
        System.out.println("Steps taken: " + steps); // 16016
    }

    private static char[][] readGrid(String fileName, int gridSizeX, int gridSizeY) throws IOException {
        char[][] grid = new char[gridSizeY][gridSizeX];
        int y = 0;
        for (String line : Utils.readLines(fileName)) {
            for (int x = 0; x < line.length(); x++) {
                grid[y][x] = line.charAt(x);
            }
            y++;
        }
        return grid;
    }

    private static String travel(Grid grid, boolean countSteps) {
        StringBuilder lettersVisited = new StringBuilder();
        int steps = 1;
        while (true) {
            char nextCell = grid.walk();
            if (isLetter(nextCell)) {
                lettersVisited.append(nextCell);
            } else if (nextCell == '+') {
                handleCrossing(grid);
            } else if (nextCell != '|' && nextCell != '-') {
                return countSteps ? String.valueOf(steps) : lettersVisited.toString();
            }
            steps++;
        }
    }

    private static void handleCrossing(Grid grid) {
        if (grid.direction == GridDirection.N || grid.direction == GridDirection.S) {
            grid.direction = (grid.cellToLeft() == '-' || isLetter(grid.cellToLeft())) ? GridDirection.W : GridDirection.E;
        } else {
            grid.direction = (grid.cellAbove() == '|' || isLetter(grid.cellAbove())) ? GridDirection.N : GridDirection.S;
        }
    }

    private static boolean isLetter(char c) {
        return c >= 'A' && c <= 'Z';
    }

    static class Grid {
        final char[][] grid;
        int x;
        int y;
        GridDirection direction;

        Grid(char[][] grid, int startX, int startY, GridDirection startDirection) {
            this.grid = grid;
            this.x = startX;
            this.y = startY;
            this.direction = startDirection;
        }

        char currentCell() {
            return grid[y][x];
        }

        char cellToLeft() {
            return grid[y][x - 1];
        }

        char cellAbove() {
            return grid[y - 1][x];
        }

        char walk() {
            switch (direction) {
                case N:
                    y--;
                    break;
                case S:
                    y++;
                    break;
                case E:
                    x++;
                    break;
                case W:
                    x--;
            }
            return currentCell();
        }
    }
}
