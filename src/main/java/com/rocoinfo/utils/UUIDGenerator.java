package com.rocoinfo.utils;

import java.util.UUID;

/**
 * <dl>
 * <dd>Description: uuid生成工具</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/6/5 上午11:31</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class UUIDGenerator {

    /**
     * 生成唯一uuid
     *
     * @return
     */
    public static String generateValue() {
        return generateValue(UUID.randomUUID().toString());
    }

    /**
     * 生成uuid
     *
     * @param param
     * @return
     */
    private static String generateValue(String param) {
        return UUID.fromString(UUID.nameUUIDFromBytes(param.getBytes()).toString()).toString().replace("-", "");
    }
}
