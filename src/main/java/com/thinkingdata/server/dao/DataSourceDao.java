package com.thinkingdata.server.dao;

import com.thinkingdata.server.model.DataSourceEntity;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/12/20 5:44 PM
 */
@Repository
public interface DataSourceDao {
    public Integer addDataSource(DataSourceEntity dataSourceEntity);

    public Integer updateDataSource(DataSourceEntity dataSourceEntity);

    public Integer deleteDataSource(Integer id);

    public DataSourceEntity getDataSource(Integer id);


    public List<DataSourceEntity> dataSourceList(Map<String, Object> paramMap);

}
