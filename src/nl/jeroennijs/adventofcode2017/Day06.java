package nl.jeroennijs.adventofcode2017;

import java.util.*;



/**
 * This works, but it needs some refactoring.
 */
public class Day06 {
    public static void main(String[] args) {
        final int[] initialBanks = { 0, 5, 10, 0, 11, 14, 13, 4, 11, 8, 8, 7, 1, 4, 12, 11 };

        System.out.println("Redistributions for part 1: " + calculateRedistributions1(initialBanks, new HashSet<>())); // 7864
        System.out.println("Repeat redistributions for part 2: " + calculateRedistributions2(initialBanks, new HashSet<>())); // 1695
    }

    private static int calculateRedistributions1(int[] banks, Set<Integer> hashes) {
        int nrOfRedistributions = 0;

        do {
            redistributeBlocks(banks);
            nrOfRedistributions++;
        } while (hashes.add(Arrays.hashCode(banks)));

        return nrOfRedistributions;
    }

    private static int calculateRedistributions2(int[] banks, Set<Integer> hashes) {

        while (true) {
            redistributeBlocks(banks);
            Integer hash = Arrays.hashCode(banks);
            if (!hashes.add(hash)) {
                return calculateRedistributionsLoop(banks, hash);
            }
        }
    }

    private static int calculateRedistributionsLoop(int[] banks, Integer hashToDetect) {
        int nrOfRedistributions = 0;

        do {
            redistributeBlocks(banks);
            nrOfRedistributions++;
        } while (!hashToDetect.equals(Arrays.hashCode(banks)));

        return nrOfRedistributions;
    }

    private static void redistributeBlocks(int[] banks) {
        final int bankWithHighestNrOfBlocks = getBankWithHighestNrOfBlocks(banks);
        final int nrOfBanks = banks.length;
        final int blocksToRedistribute = banks[bankWithHighestNrOfBlocks];
        final int extraBlocksPerBank = blocksToRedistribute / nrOfBanks;
        final int remainingBlocks = blocksToRedistribute % nrOfBanks;

        for (int i = 0; i < banks.length; i++) {
            banks[i] = (i == bankWithHighestNrOfBlocks) ? extraBlocksPerBank - 1 : banks[i] + extraBlocksPerBank;
            banks[i] += calculateRemainingBlocks(i, bankWithHighestNrOfBlocks, remainingBlocks, nrOfBanks);
        }
    }

    private static int calculateRemainingBlocks(int i, int bankWithHighestNrOfBlocks, int remainingBlocks, int nrOfBanks) {
        final int offset = (i < bankWithHighestNrOfBlocks ? nrOfBanks : 0) - bankWithHighestNrOfBlocks;
        return i + offset <= remainingBlocks ? 1 : 0;
    }

    private static int getBankWithHighestNrOfBlocks(int[] banks) {
        int max = 0;
        int maxIndex = 0;
        for (int i = 0; i < banks.length; i++) {
            if (banks[i] > max) {
                max = banks[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}
