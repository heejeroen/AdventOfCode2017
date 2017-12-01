package nl.jeroennijs.adventofcode2017;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;



class Utils {
    static String[] readLines(final String fileName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader("src/nl/jeroennijs/adventofcode2017/input/" + fileName))) {
            return reader.lines().collect(Collectors.toList()).toArray(new String[0]);
        }
    }

    static MessageDigest getMd5Digest() {
        try {
            return MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Could not initialize MessageDigest: " + e);
        }
    }

    static String getMd5Hash(final MessageDigest md, final String hashInput) {
        try {
            final byte[] digest = md.digest(hashInput.getBytes("UTF-8"));
            return DatatypeConverter.printHexBinary(digest);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
