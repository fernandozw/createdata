package com.thinkingdata.server.controller;

import com.thinkingdata.server.service.ExecuteService;
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
 * @date 2021/10/13 5:30 PM
 */
@RestController
@RequestMapping("/execute")
public class ExecuteController {
    @Autowired
    private ExecuteService executeService;

    @RequestMapping(value = "/executeMethod", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了通过脚本id执行方法接口")
    public ResponseData<Object> executeMethod(@RequestBody Map<String, Object> map) {
        return executeService.executeMethod(map);
    }

    @RequestMapping(value = "/executeMethodBySource", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了通过脚本字符串执行方法接口")
    public ResponseData<Object> executeMethodBySource(@RequestBody Map<String, Object> map) {
        return executeService.executeMethodBySource(map);
    }
}
