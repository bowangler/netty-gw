package cn.hots.gw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author TIT
 * @version 1.0
 * @description: TODO
 * @date 2025/4/15 18:23
 */
@Data
@AllArgsConstructor
public class Port52003Message {
    private String header;
    private String body;
}
