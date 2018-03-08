package cn.damei.utils;


import cn.damei.utils.des.Digests;
import cn.damei.utils.des.Encodes;


public class PasswordUtil {
    private static final int SALT_SIZE = 8;
    public static final String HASH_ALGORITHM = "SHA-1";
    public static final int HASH_ITERATIONS = 1024;


    public static String entryptUserPassword(String password, String salt) {
        return hashPassword(password, salt);
    }

    public static String generateSalt() {
        byte[] salt = Digests.generateSalt(SALT_SIZE);
        return Encodes.encodeHex(salt);
    }


    public static String hashPassword(String plainPassword, String salt) {
        byte[] hashPassword = Digests.sha1(plainPassword.getBytes(), Encodes.decodeHex(salt), HASH_ITERATIONS);
        return Encodes.encodeHex(hashPassword);
    }

}
