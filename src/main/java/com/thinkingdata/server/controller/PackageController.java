package com.thinkingdata.server.controller;

import com.thinkingdata.server.service.PackageService;
import com.thinkingdata.tools.aop.WebLog;
import com.thinkingdata.tools.response.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/12/08 4:23 PM
 */
@RestController
@RequestMapping("/package")
public class PackageController {
    @Autowired
    private PackageService packageService;

    @RequestMapping(value = "/jarList", method = {RequestMethod.GET}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了获取jar包列表接口")
    public ResponseData<Object> getJarList() {
        return packageService.getFileList();
    }

    @RequestMapping(value = "/deleteJar", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了删除jar包接口")
    public ResponseData<Object> deleteJar(@RequestBody Map<String, List<String>> map) {
        return packageService.deleteFile(map.get("fileList"));
    }

    @RequestMapping(value = "/uploadJar", method = {RequestMethod.POST}, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @WebLog(description = "调用了上传jar包接口")
    public ResponseData<Object> uploadJar(@RequestParam("file") MultipartFile file) throws Exception {
        //这里是拿到文件名
        String fileName = file.getOriginalFilename();
        return packageService.uploadFile(file.getBytes(), fileName);
    }

}
