package cn.hots.gw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.sql.DataSourceDefinition;

/**
 * @author TIT
 * @version 1.0
 * @description: TODO
 * @date 2025/4/15 18:23
 */
@Data
@AllArgsConstructor
public class Port52001Message {
    private byte[] header;
    private String body;
}
