package cn.damei.utils;

import java.util.UUID;


public class UUIDGenerator {


    public static String generateValue() {
        return generateValue(UUID.randomUUID().toString());
    }


    private static String generateValue(String param) {
        return UUID.fromString(UUID.nameUUIDFromBytes(param.getBytes()).toString()).toString().replace("-", "");
    }
}
