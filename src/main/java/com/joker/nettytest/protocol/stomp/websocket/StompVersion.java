package com.joker.nettytest.protocol.stomp.websocket;

import java.util.ArrayList;
import java.util.List;

import io.netty.util.AttributeKey;
import io.netty.util.internal.StringUtil;

public enum StompVersion {
    
    STOMP_V11("1.1", "v11.stomp"),
    STOMP_V12("1.2", "v12.stomp"),
    STOMP_V13("1.3", "v13.stomp");

    public static final AttributeKey<StompVersion> CHANNEL_ATTRIBUTE_KEY = AttributeKey.valueOf("stomp_version");

    public static final String SUB_PROTOCOLS;

    static {
        List<String> subProtocols = new ArrayList<>(values().length);
        for (StompVersion stompVersion : values()) {
            subProtocols.add(stompVersion.subProtocol);
        }

        SUB_PROTOCOLS = StringUtil.join(",", subProtocols).toString();
    }

    private final String version;
    private final String subProtocol;

    StompVersion(String version, String subProtocol) {
        this.version = version;
        this.subProtocol = subProtocol;
    }

    public String version() {
        return version;
    }

    public String subProtocol() {
        return subProtocol;
    }

    public static StompVersion findBySubProtocol(String subProtocol) {
        if (subProtocol != null) {
            for (StompVersion stompVersion : values()) {
                if (stompVersion.subProtocol.equals(subProtocol)) {
                    return stompVersion;
                }
            }
        }
        throw new IllegalArgumentException("Not found StompVersion for '" + subProtocol + "'");
    }
}
