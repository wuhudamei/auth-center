package cn.damei.websocket;

import cn.damei.utils.JsonUtils;
import cn.damei.utils.cache.SocketIOClientCache;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.annotation.*;
import org.apache.commons.collections.MapUtils;

import java.util.Map;


public class DefaultListeners {

    @OnConnect
    public void onConnectHandler(SocketIOClient client) {
    }

    @OnMessage
    public void onMessageHandler(SocketIOClient client, String data, AckRequest ackRequest) {
    }

    @OnJsonObject
    public void onJsonObjectHandler(SocketIOClient client, Object data, AckRequest ackRequest) {
    }


    @OnEvent("req")
    public void onRecUUIDHandler(SocketIOClient client, Object data, AckRequest ackRequest) {
        if (data != null) {
            // 获取参数
            Map<String, Object> params = null;
            if (data instanceof String) {
                params = JsonUtils.fromJsonAsMap(data.toString(), String.class, Object.class);
            } else if (data instanceof Map) {
                params = (Map<String, Object>) data;
            } else {
                params = (Map<String, Object>) data;
            }

            if (MapUtils.isNotEmpty(params)) {
                // 获取前台传递的uuid
                String uuid = String.valueOf(params.get("uuid"));
                SocketIOClientCache.put(uuid, client);
                return;
            }
        }

        client.sendEvent("req", JsonUtils.toJson(cn.damei.utils.MapUtils.of("code", "0", "message", "二维码已经失效,请刷新页面。")));
        client.disconnect();
    }


    @OnDisconnect
    public void onDisconnectHandler(SocketIOClient client) {
    }
}
