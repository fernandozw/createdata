package com.thinkingdata.server.service;

import com.thinkingdata.server.dao.DataSourceDao;
import com.thinkingdata.server.model.DataSourceEntity;
import com.thinkingdata.tools.response.ResponseData;
import com.thinkingdata.tools.response.ResponseDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/12/27 2:35 PM
 */
@Service
public class DataSourceService {
    @Autowired
    private DataSourceDao dataSourceDao;
    private final static Map<String, Object> MYSQL = new HashMap<String, Object>() {
        {
            put("label", "mysql");
            put("value", "mysql");
        }
    };
    private final static Map<String, Object> REDIS = new HashMap<String, Object>() {
        {
            put("label", "redis");
            put("value", "redis");
        }
    };
    private final static Map<String, Object> MONGO = new HashMap<String, Object>() {
        {
            put("label", "mongo");
            put("value", "mongo");
        }
    };


    private final static List<Object> TYPE_LIST = Arrays.asList(MYSQL, REDIS, MONGO);

    public ResponseData<Object> addDataSource(DataSourceEntity dataSourceEntity) {
        ResponseData<Object> responseData;
        dataSourceDao.addDataSource(dataSourceEntity);
        responseData = ResponseDataUtils.buildAddSuccess();
        return responseData;
    }

    public ResponseData<Object> updateDataSource(DataSourceEntity dataSourceEntity) {
        ResponseData<Object> responseData;
        dataSourceDao.updateDataSource(dataSourceEntity);
        responseData = ResponseDataUtils.buildUpdateSuccess();
        return responseData;
    }

    public ResponseData<Object> deleteDataSource(Integer id) {
        ResponseData<Object> responseData;
        dataSourceDao.deleteDataSource(id);
        responseData = ResponseDataUtils.buildDeleteSuccess();
        return responseData;
    }

    public ResponseData<Object> getDataSource(Integer id) {
        ResponseData<Object> responseData;
        responseData = ResponseDataUtils.buildSuccess(dataSourceDao.getDataSource(id));
        return responseData;
    }

    public ResponseData<Object> dataSourceList(Map<String, Object> paramMap) {
        ResponseData<Object> responseData;
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("dataSourceList", dataSourceDao.dataSourceList(paramMap));
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }

    public ResponseData<Object> dataSourceListForScript(Map<String,Object> paramMap) {
        ResponseData<Object> responseData;

        List<DataSourceEntity> dataSourceList = dataSourceDao.dataSourceList(paramMap);
        Map<String, Object> dataSourceMap = new HashMap<String, Object>();
        List<Object> selectList = new ArrayList<Object>();
        for (DataSourceEntity dataSourceEntity : dataSourceList) {
            Map<String, String> selectMap = new HashMap<String, String>();
            selectMap.put("label", dataSourceEntity.getName());
            selectMap.put("value", dataSourceEntity.getId().toString());
            dataSourceMap.put(dataSourceEntity.getId().toString(), dataSourceEntity);
            selectList.add(selectMap);

        }
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("selectList", selectList);
        dataMap.put("dataSourceMap", dataSourceMap);
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }

    public ResponseData<Object> tableList(Integer id) {
        ResponseData<Object> responseData;
        Map<String, Object> dataMap = new HashMap<String, Object>();
        DataSourceEntity dataSourceEntity = dataSourceDao.getDataSource(id);
        List<String> tables = Arrays.asList(dataSourceEntity.getTables().split(","));
        List<Object> tableList = new ArrayList<Object>();
        for (String table : tables) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("label", table);
            map.put("value", table);
            tableList.add(map);
        }
        dataMap.put("tableList", tableList);
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }

    public ResponseData<Object> typeList() {
        ResponseData<Object> responseData;
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("typeList", TYPE_LIST);
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }
}
