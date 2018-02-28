package com.rocoinfo.utils;

import com.rocoinfo.utils.des.Digests;
import com.rocoinfo.utils.des.Encodes;

import java.util.UUID;

/**
 * <dl>
 * <dd>Description: 使用sha1算法对传入的字符串进行签名</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/5 11:20</dd>
 * <dd>@author：Kong</dd>
 * </dl>
 */
public class Md5GeneratorUtils {

    /**
     * 获取UUID字符串
     *
     * @return 返回UUID字符串
     */
    private static String getUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成唯一字符串，使用encodeBase62编码
     *
     * @return 返回唯一字符串
     */
    public static String generatorAll() {
        byte[] output = Digests.sha1(getUUID().getBytes());
        return Encodes.encodeBase62(output);
    }

}
