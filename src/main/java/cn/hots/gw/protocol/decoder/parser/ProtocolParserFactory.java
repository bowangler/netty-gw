package cn.hots.gw.protocol.decoder.parser;

import cn.hots.gw.protocol.decoder.parser.impl.Port52001Parser;
import cn.hots.gw.protocol.decoder.parser.impl.Port52002Parser;
import cn.hots.gw.protocol.decoder.parser.impl.Port52003Parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


/**
 * @author TIT
 * @version 1.0
 * @description: TODO
 * @date 2025/4/16 16:18
 */
public class ProtocolParserFactory {
    private static final Map<Integer, ProtocolParser> PARSERS = new HashMap<>();

    static {
        PARSERS.put(52001, new Port52001Parser());
        PARSERS.put(52002, new Port52002Parser());
        PARSERS.put(52003, new Port52003Parser());
    }

    public static ProtocolParser getParser(int port) {
        return Optional.ofNullable(PARSERS.get(port))
                .orElseThrow(() -> new IllegalArgumentException("Unsupported port: " + port));
    }

    // 动态注册新协议
    public static void registerParser(int port, ProtocolParser parser) {
        PARSERS.put(port, parser);
    }
}
