package mattwalsh.hebtest;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumUtil {

    public static String getChecksum(byte[] imageData) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(imageData);
            return DatatypeConverter.printHexBinary(md.digest()).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("this is very bad");
        }
    }

}
