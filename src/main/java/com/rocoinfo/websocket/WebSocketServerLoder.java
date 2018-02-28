package com.rocoinfo.websocket;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketConfig;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * <dl>
 * <dd>Description: websocket服务类，用于启动websocket服务</dd>
 * <dd>Company: 大城若谷信息技术有限公司</dd>
 * <dd>@date：2017/4/6 下午1:59</dd>
 * <dd>@author：Aaron</dd>
 * </dl>
 */
@Component
@Lazy(false)
public class WebSocketServerLoder {

    private Logger logger = LoggerFactory.getLogger(WebSocketServerLoder.class);

    @Value("${socket.io.port}")
    private int port;

    @Value("${profile}")
    private String profile;

    @PostConstruct
    public void load() {
        if (!"development".equals(profile)) {
            logger.info("WebSocket Init Start");
            this.initServer();
            logger.info("WebSocket Init End...");
        }

    }


    private void initServer() {
        Configuration config = new Configuration();
        config.setAllowCustomRequests(true);
        config.setPort(port);
        config.setTransports(Transport.WEBSOCKET, Transport.XHRPOLLING, Transport.FLASHSOCKET);
        // 设置Socket配置
        SocketConfig socketConfig = new SocketConfig();
        socketConfig.setTcpNoDelay(true);
        socketConfig.setTcpKeepAlive(true);
        config.setSocketConfig(socketConfig);
        config.setCloseTimeout(60);
        SocketIOServer server = new SocketIOServer(config);
        // 添加 默认事件 处理
        server.addListeners(new DefaultListeners());
        server.start();
        WebSocketServer.setServer(server);
    }
}
