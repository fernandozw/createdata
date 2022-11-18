package com.thinkingdata.server.service;

import com.thinkingdata.server.dao.MethodDao;
import com.thinkingdata.server.dao.ParamDao;
import com.thinkingdata.server.dao.ProjectDao;
import com.thinkingdata.server.dao.ScriptDao;
import com.thinkingdata.server.model.MethodEntity;
import com.thinkingdata.server.model.ParamEntity;
import com.thinkingdata.server.model.ScriptEntity;
import com.thinkingdata.tools.execution.Actuator;
import com.thinkingdata.tools.formater.JavaFormat;
import com.thinkingdata.tools.response.ResponseData;
import com.thinkingdata.tools.response.ResponseDataUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author Fernando Zhu
 * @version 1.0
 * @date 2021/09/10 2:28 PM
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@Service
public class ScriptService {
    private final static String JAVA_FIX = ".java";

    private final static Map<String, Object> PYTHON = new HashMap<String, Object>() {
        {
            put("label", "python");
            put("value", "python");
        }
    };
    private final static Map<String, Object> JAVA = new HashMap<String, Object>() {
        {
            put("label", "java");
            put("value", "java");
        }
    };

    private final static List<Object> TYPE_LIST = Arrays.asList(JAVA, PYTHON);
    private final static Map<String, Object> UN_CONFIG = new HashMap<String, Object>() {
        {
            put("label", "未完成");
            put("value", "0");
        }
    };
    private final static Map<String, Object> CONFIG = new HashMap<String, Object>() {
        {
            put("label", "已完成");
            put("value", "1");
        }
    };

    private final static List<Object> IS_DONE_LIST = Arrays.asList(UN_CONFIG, CONFIG);

    private final static Map<String, Object> IS_DONE_MAP = new HashMap<String, Object>() {
        {
            put("1", CONFIG);

            put("0", UN_CONFIG);
        }
    };
    @Autowired
    private Actuator actuator;
    @Autowired
    private JavaFormat javaFormat;
    @Autowired
    private ScriptDao scriptDao;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private MethodDao methodDao;
    @Autowired
    private ParamDao paramDao;

    /**
     * 新增脚本
     *
     * @param scriptEntity 脚本实例
     * @return 返回新增结果
     */
    public ResponseData<Object> addScript(ScriptEntity scriptEntity) {
        ResponseData<Object> responseData;
        if (actuator.getMethods(scriptEntity.getContent()).size() > 0) {
            scriptEntity.setContent(javaFormat.formJava(scriptEntity.getContent()));
            responseData = ResponseDataUtils.buildAddSuccess(scriptDao.addScript(scriptEntity));
        } else {
            responseData = ResponseDataUtils.buildAddFail("脚本可执行方法不能为空!");
        }
        return responseData;
    }

    /***
     * 编辑脚本
     * @param scriptEntity 脚本实体
     * @return 返回编辑结果
     */
    public ResponseData<Object> updateScript(ScriptEntity scriptEntity) {

        ResponseData<Object> responseData;
        if (actuator.getMethods(scriptEntity.getContent()).size() > 0) {
            if (scriptEntity.getContent() != "") {
                scriptEntity.setContent(javaFormat.formJava(scriptEntity.getContent()));
                Integer code = scriptIsChange(scriptEntity);
                if (code != 0) {
                    scriptEntity.setIsDone(0);
                    responseData = ResponseDataUtils.buildUpdateSuccess("脚本内容有改动,请重新配置方法、参数信息!");
                } else {
                    responseData = ResponseDataUtils.buildUpdateSuccess("脚本可执行方法没有变动，无需重新配置!");
                }
            } else {
                responseData = ResponseDataUtils.buildUpdateSuccess("脚本可执行方法没有变动，无需重新配置!");
            }
            scriptDao.updateScript(scriptEntity);
        } else {
            responseData = ResponseDataUtils.buildUpdateFail("脚本可执行方法不能为空!");
        }
        return responseData;
    }

    /***
     * 根据脚本id获取脚本
     * @param id 脚本id
     * @return 返回脚本实体
     */
    public ResponseData<Object> getScriptById(Integer id) {
        ResponseData<Object> responseData;
        responseData = ResponseDataUtils.buildSuccess(scriptDao.getScriptById(id));
        return responseData;
    }


    /**
     * 删除脚本、方法、参数三表的关联数据
     *
     * @param id 脚本id
     * @return 返回删除结果
     */
    public ResponseData<Object> deleteScript(Integer id) {
        ResponseData<Object> responseData;
        responseData = ResponseDataUtils.buildDeleteSuccess(scriptDao.deleteScript(id));
        return responseData;
    }

    /**
     * 根据条件查询脚本列表并分页
     *
     * @param paramMap 查询参数
     * @return 返回脚本列表
     */
    public ResponseData<Object> scriptList(Map<String, Object> paramMap) {
        ResponseData<Object> responseData;
        Integer total = scriptDao.scriptTotal(paramMap);
        Integer pageSize = (Integer) paramMap.get("pageSize");
        Integer start = (Integer) paramMap.get("pageNum") * pageSize - pageSize;
        paramMap.put("start", start);
        paramMap.put("end", pageSize);
        List<ScriptEntity> scriptList = scriptDao.scriptList(paramMap);
        Map<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("total", total);
        dataMap.put("scriptList", scriptList);
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }

    /**
     * 提供给脚本新增、编辑时使用的项目下拉列表
     *
     * @return 返回项目下拉列表
     */
    public ResponseData<Object> selectList() {
        ResponseData<Object> responseData;
        Map<String, Object> dataMap = new HashMap<>();
        List<Map<String, Object>> selectList = projectDao.projectSelectList();
        Map<String, Map<String, Object>> selectMap = selectList.stream().collect(
                Collectors.toMap(s -> String.valueOf(s.get("value")), s -> s));
        dataMap.put("selectList", selectList);
        dataMap.put("isDoneList", IS_DONE_LIST);
        dataMap.put("selectMap", selectMap);
        dataMap.put("isDoneMap", IS_DONE_MAP);
        dataMap.put("typeList", TYPE_LIST);
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }

    /**
     * 格式化java代码
     *
     * @param map 源代码
     * @return 返回格式化后的代码
     */
    public ResponseData<Object> formatScript(Map<String, Object> map) throws IOException {
        ResponseData<Object> responseData;
        String className = actuator.getClassName(map.get("script").toString()) + JAVA_FIX;
        Map<String, Object> dataMap = new HashMap<>();
        String target = javaFormat.formJava(map.get("script").toString());
        List<String> errorList = javaFormat.check(className, target);
        dataMap.put("script", target);
        dataMap.put("errorList", errorList);
        responseData = ResponseDataUtils.buildSuccess(dataMap);
        return responseData;
    }

    public Integer scriptIsChange(ScriptEntity scriptEntity) {

        Integer code = 0;
        Map<String, Map<String, List<String>>> map = actuator.getParamMap(scriptEntity.getContent());
        Map<String, Map<String, List<String>>> mapOld = actuator.getParamMap(scriptDao.getScriptById(scriptEntity.getId()).getContent());
        Set<String> keySet = map.keySet();
        List<MethodEntity> methodEntityList = methodDao.getMethodByScriptId(scriptEntity.getId());
        List<String> methodList = methodEntityList.stream().map(MethodEntity::getMethodName).collect(Collectors.toList());
        // 判断编辑后的脚本的交集与多余的方法
        for (String key : keySet) {
            if (methodList.contains(key)) {
                // 找出匹配的方法对象
                MethodEntity methodEntity = methodEntityList.stream().
                        filter(method -> method.getMethodName().equals(key)).
                        findAny().orElse(null);
                List<ParamEntity> paramEntityList = paramDao.getParamsByMethodId(methodEntity.getId());
                List<String> paramList = paramEntityList.stream().map(ParamEntity::getParamName).collect(Collectors.toList());
                List<String> paramTypeList = paramEntityList.stream().map(ParamEntity::getParamType).collect(Collectors.toList());
                List<String> paramListNew = map.get(key).get("paramNameList");
                List<String> paramTypeListNew = map.get(key).get("paramTypeList");
                // 判断参数列表和参数类型是否完全一样
                if (compare(paramListNew, paramList) && compare(paramTypeListNew, paramTypeList)) {
                    if (!map.get(key).get("returnTypeList").get(0).equals(mapOld.get(key).get("returnTypeList").get(0))) {
                        paramDao.deleteByMethodId(methodEntity.getId());
                        methodDao.deleteMethodById(methodEntity.getId());
                        code = 2;
                    }
                } else {
                    paramDao.deleteByMethodId(methodEntity.getId());
                    methodDao.deleteMethodById(methodEntity.getId());
                    code = 2;
                }

            } else {
                code = 1;
            }
        }
        // 判断原脚本中的多余可执行方法，留到最后判断，不需要修改脚本的配置状态
        for (MethodEntity entity : methodEntityList) {
            if (!keySet.contains(entity.getMethodName())) {
                paramDao.deleteByMethodId(entity.getId());
                methodDao.deleteMethodById(entity.getId());
            }
        }
        return code;
    }

    public <T extends Comparable<T>> boolean compare(List<T> a, List<T> b) {
        if (a.size() != b.size())
            return false;
        Collections.sort(a);
        Collections.sort(b);
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i)))
                return false;
        }
        return true;
    }

    @Test
    public void test1() {
        String script = "import java.sql.*;\n" +
                "import redis.clients.jedis.Jedis;\n" +
                "public class CreateData {\n" +
                "    private Statement mysql_promotion(){\n" +
                "        Statement statement = null;\n" +
                "        try{\n" +
                "            Connection connection = DriverManager.getConnection(\"jdbc:mysql://rm-bp1b4agx5254bs1u4.mysql.rds.aliyuncs.com:3306/promotion\",\"miao_pmt_trw\",\"osfS28whIH6c9LUZ\");\n" +
                "            statement = connection.createStatement();\n" +
                "        }\n" +
                "        catch(Exception e){\n" +
                "        }\n" +
                "        return statement;\n" +
                "    }\n" +
                "    public Object setHrbp(String hrbpName, String hrbpDomain, Integer id) throws SQLException {\n" +
                "        String sql = \"update candidates set hrbp_name ='\" + hrbpName + \"',hrbp_domain='\" + hrbpDomain + \"' where id=\" + id + \"\";\n" +
                "        Integer rs = mysql_promotion().executeUpdate(sql);\n" +
                "        return rs;\n" +
                "    }\n" +
                "    public Integer setCoeStatus(String status, Integer id) throws SQLException {\n" +
                "        String sql = \"update appraise_coes set confirm_status='\" + status + \"' where id = \" + id;\n" +
                "        Integer rs = mysql_promotion().executeUpdate(sql);\n" +
                "        return rs;\n" +
                "    }\n" +
                "}\n";
        Integer id = 102;
        ScriptEntity scriptEntity = new ScriptEntity();
        scriptEntity.setId(id);
        scriptEntity.setContent(script);
        System.out.println(scriptIsChange(scriptEntity));
    }
}

