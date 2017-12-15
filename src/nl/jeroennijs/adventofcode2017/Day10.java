package nl.jeroennijs.adventofcode2017;

import java.util.Arrays;
import java.util.stream.Collectors;



public class Day10 {
    private static final String[] hexChar = new String[] { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    public static void main(String[] args) {
        Rope rope1 = new Rope(256);
        int[] lengths1 = new int[] { 70, 66, 255, 2, 48, 0, 54, 48, 80, 141, 244, 254, 160, 108, 1, 41 };
        rope1.twist(lengths1);
        System.out.println("Result for step 1: " + rope1.segments[0] * rope1.segments[1]); // 7888

        Rope rope2 = new Rope(256);
        String hash = rope2.calculateHash(asString(lengths1));
        System.out.println("Result for step 2: " + hash); // decdf7d377879877173b7f2fb131cf1b
    }

    private static String asString(int[] lengths) {
        return String.join(",", Arrays.stream(lengths)
            .mapToObj(String::valueOf)
            .collect(Collectors.toList()));
    }

    private static String toHex(int[] hashes) {
        StringBuilder result = new StringBuilder();
        for (int hash : hashes) {
            result.append(hexChar[hash / 16]).append(hexChar[hash % 16]);
        }
        return result.toString();
    }

    private static int[] toAscii(String input) {
        int[] result = new int[input.length()];
        for (int i = 0; i < input.length(); i++) {
            result[i] = input.charAt(i);
        }
        return result;
    }

    static class Rope {
        int[] segments;
        int index = 0;
        int skip = 0;

        Rope(int size) {
            segments = new int[size];
            for (int i = 0; i < size; i++) {
                segments[i] = i;
            }
        }

        String calculateHash(String input) {
            int[] lengths = ArrayUtilities.concatenate(toAscii(input), new int[] { 17, 31, 73, 47, 23 });
            for (int i = 0; i < 64; i++) {
                twist(lengths);
            }
            return toHex(getDenseHashes());
        }

        void twist(int[] lengths) {
            for (int length : lengths) {
                ArrayUtilities.replace(segments, index, ArrayUtilities.reverse(ArrayUtilities.extract(segments, index, length)));
                index = (index + length + skip) % segments.length;
                skip++;
            }
        }

        private int[] getDenseHashes() {
            final int nrOfHashes = segments.length / 16;
            int[] hashes = new int[nrOfHashes];
            for (int i = 0; i < nrOfHashes; i++) {
                hashes[i] = segments[i * 16];
                for (int j = 1; j < 16; j++) {
                    hashes[i] = hashes[i] ^ segments[i * 16 + j];
                }
            }
            return hashes;
        }
    }

    private static class ArrayUtilities {

        private static int[] reverse(int[] source) {
            int[] reversed = new int[source.length];
            for (int i = 0; i < source.length; i++) {
                reversed[source.length - 1 - i] = source[i];
            }
            return reversed;
        }

        private static void replace(int[] source, int startIndex, int[] replacement) {
            for (int j = 0; j < replacement.length; j++) {
                source[(j + startIndex) % source.length] = replacement[j];
            }
        }

        private static int[] extract(int[] source, int startIndex, int length) {
            int[] extract = new int[length];
            for (int i = 0; i < length; i++) {
                extract[i] = source[(i + startIndex) % source.length];
            }
            return extract;
        }

        private static int[] concatenate(int[] a, int[] b) {
            int[] result = new int[a.length + b.length];
            System.arraycopy(a, 0, result, 0, a.length);
            System.arraycopy(b, 0, result, a.length, b.length);
            return result;
        }
    }
}
