package com.example.getway.netty;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用户 requestId 映射 Channel
    private static final ConcurrentHashMap<String, Channel> requestIdToChannel = new ConcurrentHashMap<>();

    // Channel 映射回 requestId（方便断开连接时移除）
    private static final ConcurrentHashMap<Channel, String> channelToRequestId = new ConcurrentHashMap<>();

    public static boolean exists(String requestId) {
        return requestIdToChannel.containsKey(requestId);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        log.info("新客户端连接: {}", ctx.channel().id().asShortText());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        String requestId = channelToRequestId.remove(channel);
        if (requestId != null) {
            requestIdToChannel.remove(requestId);
        }
        log.info("客户端断开: {}，解绑 requestId: {}", channel.id().asShortText(), requestId);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        String text = frame.text();

        try {
            JSONObject json = JSON.parseObject(text);
            String type = json.getString("type");

            if ("getRequestId".equals(type)) {
                // 客户端请求 requestId

                Channel channel = ctx.channel();
                String requestId = UUID.randomUUID().toString();

                requestIdToChannel.put(requestId, channel);
                channelToRequestId.put(channel, requestId);
                System.out.println("现在channel");
                System.out.println(requestIdToChannel);
                // 回复前端绑定结果
                String response = JSON.toJSONString(Map.of("type", "bindAck", "requestId", requestId));
                channel.writeAndFlush(new TextWebSocketFrame(response));
            } else {
                // 其他业务消息可在这里处理
                ctx.channel().writeAndFlush(new TextWebSocketFrame("收到未知 type 消息: " + type));
            }
        } catch (Exception e) {
            ctx.channel().writeAndFlush(new TextWebSocketFrame("服务器无法解析你的消息"));
        }
    }

    // 外部调用：通过 requestId 推送消息
    public static void sendMessage(String requestId, String message) {
        Channel channel = requestIdToChannel.get(requestId);
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(new TextWebSocketFrame(message));
        } else {
            log.warn("推送失败: {}", requestId);
        }
    }
}
