package cn.damei.utils;

import cn.damei.utils.des.Digests;
import cn.damei.utils.des.Encodes;

import java.util.UUID;


public class Md5GeneratorUtils {


    private static String getUUID() {
        return UUID.randomUUID().toString();
    }


    public static String generatorAll() {
        byte[] output = Digests.sha1(getUUID().getBytes());
        return Encodes.encodeBase62(output);
    }

}
