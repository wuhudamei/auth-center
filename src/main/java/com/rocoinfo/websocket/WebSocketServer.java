package com.rocoinfo.websocket;

import com.corundumstudio.socketio.SocketIOServer;

/**
 * <dl>
 * <dd>Description: 持有SocketIOServer对象</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/4/6 下午4:02</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
public class WebSocketServer {

    private WebSocketServer() {
    }

    private static SocketIOServer server = null;

    /**
     * 获取SocketIOServer
     *
     * @return
     */
    public static SocketIOServer getServer() {
        return server;
    }

    /**
     * 设置SocketIOServer
     *
     * @return
     */
    public static void setServer(SocketIOServer server) {
        WebSocketServer.server = server;
    }
}
