package com.newcoin.api.demo.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JacksonUtil {
	
    private JacksonUtil() {
        throw new AssertionError();
    }

    private static ObjectMapper mapper = new ObjectMapper();

    static {
        //反序列化时忽略在 JSON 中存在但 Java 对象不存在的属性,否则会抛出异常
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //自定义日期格式 yyyy-MM-dd HH:mm:ss
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 将 javaBean 转换为 JSON
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 JSON 转换为 自定义的复杂类型
     * 示例：List<Map<String,User>> listMap = Jackson.toObj(jsonStr, new TypeReference<List<Map<String,User>>>(){});
     * @param json
     * @param valueTypeRef
     * @return
     */
    public static <T> T toObj(String json, TypeReference valueTypeRef) {
        try {
            return  mapper.readValue(json, valueTypeRef);
        } catch (IOException e) {
        	e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 JSON 转换为 javaBean
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T jsonToObj(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (Exception e) {
        	e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 JSON 转换为 List<T> ,T不能带泛型
     * 示例：List<T> list = JacksonUtil.jsonToList(jsonStr, ArrayList.class, T.class);
     *
     * @param jsonStr         待转化的json串
     * @param collectionClazz 集合类型
     * @param elementClazz    对象类型
     * @return
     * @since 2.5 NOTE: was briefly deprecated for 2.6
     */
    public static <T> List<T> jsonToList(String jsonStr, Class<?> collectionClazz, Class<T> elementClazz) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClazz, elementClazz);
        try {
            return mapper.readValue(jsonStr, javaType);
        } catch (Exception e) {
        	e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 JSON 转换为 List<T>
     * 使用new TypeReference<List<T>>() {} 来构造返回类型
     *
     * @param jsonStr
     * @param clazz
     * @return
     */
    public static <T> List<T> jsonToList(String jsonStr, Class<T> clazz) {
        try {
            List<Map<String, Object>> list = mapper.readValue(jsonStr, new TypeReference<List<T>>() {});
            List<T> result = new ArrayList<T>();
            for (Map<String, Object> map : list) {
                result.add(mapToObj(map, clazz));
            }
            return result;
        } catch (Exception e) {
        	e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 JSON 转换为 Map<K,V> ,且K、V不能带泛型
     * 示例：Map<K,V> map = JacksonUtil.jsonToMap(jsonStr, HashMap.class, K.class, V.class);
     *
     * @param jsonStr         待转化的json串
     * @param collectionClazz 集合类型
     * @param kClazz1         K 对象类型
     * @param kClazz2         V 对象类型
     * @return
     * @since 2.5 NOTE: was briefly deprecated for 2.6
     */
    public static <K, V> Map<K, V> jsonToMap(String jsonStr, Class<?> collectionClazz, Class<K> kClazz1, Class<V> kClazz2) {
        JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClazz, kClazz1, kClazz2);
        try {
            return mapper.readValue(jsonStr, javaType);
        } catch (Exception e) {
        	e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 JSON 转换为 Map<String,T>
     *
     * @param jsonStr
     * @param clazz
     * @return
     */
    public static <T> Map<String, T> jsonToMap(String jsonStr, Class<T> clazz) {
        try {
            Map<String, Map<String, Object>> map = mapper.readValue(jsonStr, new TypeReference<Map<String, T>>() {});
            Map<String, T> result = new HashMap<String, T>();
            for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
                result.put(entry.getKey(), mapToObj(entry.getValue(), clazz));
            }
            return result;
        } catch (Exception e) {
        	e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 Map 转换为 javaBean
     *
     * @param map
     * @param clazz
     * @return
     */
    public static <T> T mapToObj(Map map, Class<T> clazz) {
        return mapper.convertValue(map, clazz);
    }

}

