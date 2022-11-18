package com.thinkingdata.server.controller;

import com.thinkingdata.server.model.DataSourceEntity;
import com.thinkingdata.server.service.DataSourceService;
import com.thinkingdata.tools.aop.WebLog;
import com.thinkingdata.tools.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/12/27 4:37 PM
 */
@RestController
@RequestMapping("/dataSource")
public class DataSourceController {
    @Autowired
    private DataSourceService dataSourceService;

    @RequestMapping(value = "/addDataSource", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了新增数据源接口")
    public ResponseData<Object> addDataSource(@RequestBody DataSourceEntity dataSourceEntity) {
        return dataSourceService.addDataSource(dataSourceEntity);
    }

    @RequestMapping(value = "/updateDataSource", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了修改数据源接口")
    public ResponseData<Object> updDataSource(@RequestBody DataSourceEntity dataSourceEntity) {
        return dataSourceService.updateDataSource(dataSourceEntity);
    }

    @RequestMapping(value = "/deleteDataSource", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了删除数据源接口")
    public ResponseData<Object> deleteDataSource(@RequestBody Map<String, Integer> map) {
        return dataSourceService.deleteDataSource(map.get("id"));
    }

    @RequestMapping(value = "/getDataSource", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了获取数据源接口")
    public ResponseData<Object> getDataSource(@RequestBody Map<String, Integer> map) {
        return dataSourceService.getDataSource(map.get("id"));
    }

    @RequestMapping(value = "/dataSourceList", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了获取数据源列表接口")
    public ResponseData<Object> dataSourceList(@RequestBody Map<String, Object> paramMap) {
        return dataSourceService.dataSourceList(paramMap);
    }

    @RequestMapping(value = "/typeList", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了获取查询条件接口")
    public ResponseData<Object> typeList() {
        return dataSourceService.typeList();
    }

    @RequestMapping(value = "/dataSourceListForScript", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了获取数据源下拉选项接口")
    public ResponseData<Object> dataSourceListForScript(@RequestBody Map<String, Object> paramMap) {
        return dataSourceService.dataSourceListForScript(paramMap);
    }

    @RequestMapping(value = "/tableList", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了获取数据源库列表接口")
    public ResponseData<Object> tableList(@RequestBody Map<String, Integer> map) {
        return dataSourceService.tableList(map.get("id"));
    }

}
