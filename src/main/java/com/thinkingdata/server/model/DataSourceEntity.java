package com.thinkingdata.server.model;

import lombok.Data;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/12/20 5:19 PM
 */
@Data
public class DataSourceEntity {
    // 数据库自增id
    private Integer id;
    // 名称
    private String name;
    // 数据源类型
    private String type;
    // url地址
    private String url;
    // 端口号
    private Integer port;
    // 用户名
    private String user;
    // 密码
    private String password;
    // table列表，逗号分割
    private String tables;
    // 创建时间
    private String addTime;
    // 修改时间
    private String updateTime;

}
