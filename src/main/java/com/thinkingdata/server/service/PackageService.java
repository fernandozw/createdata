package com.thinkingdata.server.service;

import com.thinkingdata.tools.response.ResponseData;
import com.thinkingdata.tools.response.ResponseDataUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/12/08 4:20 PM
 */

@Service
public class PackageService {
    // ext 目录路径
    @Value(value = "${javaPath.extDir}")
    private String extJars;

    /**
     * 获取拓展包的名称列表
     *
     * @return 返回拓展包名称列表
     */
    public ResponseData<Object> getFileList() {
        ResponseData<Object> response;
        File dir = new File(extJars);
        File[] array = dir.listFiles();
        List<String> fileList = new ArrayList<String>();
        for (File file : array) {
            fileList.add(file.getName());
        }
        response = ResponseDataUtils.buildSuccess(fileList);
        return response;
    }

    /**
     * 上传文件至指定目录
     *
     * @param file     二进制流文件
     * @param fileName 文件名称
     * @return 返回上传结果
     * @throws Exception
     */
    public ResponseData<Object> uploadFile(byte[] file, String fileName) throws Exception {
        ResponseData<Object> response;
        // 判断文件夹是否存在,不存在就新建
        File folder = new File(extJars);
        if (!folder.exists() && !folder.isDirectory()) {
            folder.mkdirs();
        }
        File ifFile = new File(extJars + File.separator + fileName);
        if (ifFile.exists()) {
            response = ResponseDataUtils.buildError("jar包【" + fileName + "】已存在!");
        } else {
            //写文件
            FileOutputStream out = new FileOutputStream(extJars + File.separator + fileName);
            out.write(file);
            out.close();
            response = ResponseDataUtils.buildSuccess("jar包【" + fileName + "】上传成功！");
        }
        return response;
    }


    public ResponseData<Object> deleteFile(List<String> fileList) {
        ResponseData<Object> response = null;
        List<String> deleteList = new ArrayList<String>();
        for (String fileName : fileList) {
            File file = new File(extJars + fileName);
            if (file.exists()) {
                if (file.isFile()) {
                    file.delete();
                    deleteList.add("删除jar包【" + fileName + "】成功！");
                }

            } else {
                deleteList.add("jar包【" + fileName + "】不存在！");
            }
        }
        response = ResponseDataUtils.buildSuccess(deleteList);

        return response;
    }


}

