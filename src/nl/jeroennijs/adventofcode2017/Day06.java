package nl.jeroennijs.adventofcode2017;

import java.security.MessageDigest;
import java.util.*;



/**
 * This works, but it needs some refactoring.
 */
public class Day06 {
    public static void main(String[] args) {
        final int[] initialBanks = { 0, 5, 10, 0, 11, 14, 13, 4, 11, 8, 8, 7, 1, 4, 12, 11 };

        System.out.println("Redistributions for part 1: " + calculateRedistributions1(initialBanks)); // 7864
        System.out.println("Repeat redistributions for part 2: " + calculateRedistributions2(initialBanks)); // 1695
    }

    private static int calculateRedistributions1(int[] initialBanks) {
        final MessageDigest digest = Utils.getMd5Digest();

        int[] banks = initialBanks;
        int nrOfRedistributions = 0;
        Set<String> hashes = new HashSet<>();

        while (true) {
            int bankWithHighestNrOfBlocks = getBankWithHighestNrOfBlocks(banks);
            banks = redistributeBlocks(banks, bankWithHighestNrOfBlocks);
            nrOfRedistributions++;
            if (!hashes.add(Utils.getMd5Hash(digest, printArray(banks)))) {
                return nrOfRedistributions;
            }
        }
    }

    private static int calculateRedistributions2(int[] initialBanks) {
        final MessageDigest digest = Utils.getMd5Digest();

        int[] banks = initialBanks;
        Set<String> hashes = new HashSet<>();
        boolean loopDetected = false;
        int nrLoop = 0;
        String hashToDetect = "";

        while (true) {
            int bankWithHighestNrOfBlocks = getBankWithHighestNrOfBlocks(banks);
            banks = redistributeBlocks(banks, bankWithHighestNrOfBlocks);
            String hash = Utils.getMd5Hash(digest, printArray(banks));
            if (loopDetected) {
                nrLoop++;
                if (hash.equals(hashToDetect)) {
                    return nrLoop;
                }
            } else {
                if (!hashes.add(hash)) {
                    hashToDetect = hash;
                    loopDetected = true;
                }
            }
        }
    }

    private static String printArray(int[] banks) {
        StringBuilder result = new StringBuilder();
        for (int bank : banks) {
            result.append(bank);
            result.append(" ");
        }
        return result.toString();
    }

    private static int[] redistributeBlocks(int[] banks, int bankWithHighestNrOfBlocks) {
        final int nrOfBanks = banks.length;
        final int[] updatedBanks = new int[nrOfBanks];
        final int blocksToRedistribute = banks[bankWithHighestNrOfBlocks];
        final int extraBlocksPerBank = blocksToRedistribute / nrOfBanks;
        final int remainingBlocks = blocksToRedistribute % nrOfBanks;

        for (int i = 0; i < banks.length; i++) {
            updatedBanks[i] = (i == bankWithHighestNrOfBlocks) ? extraBlocksPerBank - 1 : banks[i] + extraBlocksPerBank;
            updatedBanks[i] += calculateRemainingBlocks(i, bankWithHighestNrOfBlocks, remainingBlocks, nrOfBanks);
        }

        return updatedBanks;
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
