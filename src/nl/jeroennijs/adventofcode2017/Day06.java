package nl.jeroennijs.adventofcode2017;

import java.security.MessageDigest;
import java.util.HashSet;
import java.util.Set;



/**
 * This works, but it needs some serious refactoring.
 */
public class Day06 {
    public static void main(String[] args) {
        System.out.println("Redistributions for part 1: " + calculateRedistributions(false)); // 7864
        System.out.println("Repeat redistributions for part 2: " + calculateRedistributions(true)); // 1695
    }

    private static int calculateRedistributions(boolean findLoop) {
        final MessageDigest digest = Utils.getMd5Digest();

        int[] banks = new int[] { 0, 5, 10, 0, 11, 14, 13, 4, 11, 8, 8, 7, 1, 4, 12, 11 };
        int nrOfRedistributions = 0;
        Set<String> hashes = new HashSet<>();
        boolean loopDetected = false;
        int nrLoop = 0;
        String hashToDetect = "";

        System.out.println(nrOfRedistributions + " = " + printArray(banks));

        while (true) {
            int bankWithHighestNrOfBlocks = getBankWithHighestNrOfBlocks(banks);
            banks = redistributeBlocks(banks, bankWithHighestNrOfBlocks);
            nrOfRedistributions++;
            if (loopDetected) {
                nrLoop++;
            }
            String hash = Utils.getMd5Hash(digest, printArray(banks));
            System.out.println(nrOfRedistributions + " = " + printArray(banks) + " (hash =" + hash + ")");
            if (!findLoop) {
                if (!hashes.add(hash)) {
                    return nrOfRedistributions;
                }
            } else {
                if (!loopDetected) {
                    if (!hashes.add(hash)) {
                        hashToDetect = hash;
                        loopDetected = true;
                    }
                } else {
                    if (hash.equals(hashToDetect)) {
                        return nrLoop;
                    }
                }
            }
        }
    }

    private static String printArray(int[] banks) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < banks.length; i++) {
            result.append(banks[i]);
            result.append(" ");
        }
        return result.toString();
    }

    private static int[] redistributeBlocks(int[] banks, int bankWithHighestNrOfBlocks) {
        int nrOfBanks = banks.length;
        int[] updatedBanks = new int[nrOfBanks];
        int blocksToRedistribute = banks[bankWithHighestNrOfBlocks];
        final int extraBlocksPerBank = blocksToRedistribute / nrOfBanks;
        final int remainingBlocks = blocksToRedistribute % nrOfBanks;
        for (int i = 0; i < banks.length; i++) {
            if (i == bankWithHighestNrOfBlocks) {
                updatedBanks[i] = extraBlocksPerBank - 1 + calculateRemainingBlocks(i, bankWithHighestNrOfBlocks, remainingBlocks, nrOfBanks);
            } else {
                updatedBanks[i] = banks[i] + extraBlocksPerBank + calculateRemainingBlocks(i, bankWithHighestNrOfBlocks, remainingBlocks, nrOfBanks);
            }
        }
        return updatedBanks;
    }

    private static int calculateRemainingBlocks(int i, int bankWithHighestNrOfBlocks, int remainingBlocks, int nrOfBanks) {
        if (i + ((i < bankWithHighestNrOfBlocks) ? nrOfBanks : 0) - bankWithHighestNrOfBlocks <= remainingBlocks) {
            return 1;
        }
        return 0;
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
