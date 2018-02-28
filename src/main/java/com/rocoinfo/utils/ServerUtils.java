package com.rocoinfo.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * <dl>
 * <dd>Description: </dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/7/4 上午11:43</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public final class ServerUtils {

    private static final String LOCAL_HOST = "127.0.0.1";

    private ServerUtils() {
    }

    /**
     * 获取服务器ip
     *
     * @return
     */
    public static String getServerIp() {
        String SERVER_IP = LOCAL_HOST;
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip;
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();

                if(ni.getInetAddresses().hasMoreElements()){
                    ip = ni.getInetAddresses().nextElement();
                    SERVER_IP = ip.getHostAddress();
                    if (!ip.isSiteLocalAddress() && !ip.isLoopbackAddress()
                            && !ip.getHostAddress().contains(":")) {
                        SERVER_IP = ip.getHostAddress();
                        break;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return SERVER_IP;
    }
}
