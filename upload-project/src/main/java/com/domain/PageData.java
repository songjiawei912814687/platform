package com.domain;


import com.alibaba.druid.proxy.jdbc.ClobProxyImpl;
import com.alibaba.fastjson.JSON;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.*;


/**
 * 说明：参数封装Map
 * 创建人：lee
 * 修改时间：2018-1-4
 */
public class PageData extends HashMap implements Map {

    private static final long serialVersionUID = 1L;

    private Integer rows = 10;
    private Integer pageIndex = 1;

    Map map = null;
    HttpServletRequest request;

    public PageData(HttpServletRequest request) {
        this.request = request;
        Map properties = request.getParameterMap();
        Map returnMap = new HashMap();
        Iterator entries = properties.entrySet().iterator();
        Entry entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Entry) entries.next();
            name = (String) entry.getKey();
            if ("Param".equals(name)) {
                String[] valuess = (String[]) entry.getValue();

                for (int i = 0; i < valuess.length; i++) {
                    Map mapType = JSON.parseObject(valuess[i], Map.class);
                    for (Object obj : mapType.keySet()) {
                        String str = EscapeToolBox.unescape((String) mapType.get(obj));
                        returnMap.put(obj, str);
                        System.out.println("key为：" + obj + "值为：" + str);
                    }
                }

            }
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            returnMap.put(name, value);
        }
        map = returnMap;
        if (this.containsKey("rows")) {
            this.setRows(this.GetParameterInt("rows"));
        }
        if (this.containsKey("page")) {
            this.setPageIndex(this.GetParameterInt("page"));
        }

    }

    public PageData() {
        map = new HashMap();
    }

    @Override
    public Object get(Object key) {
        Object obj = null;
        if (map.get(key) instanceof Object[]) {
            Object[] arr = (Object[]) map.get(key);
            obj = request == null ? arr : (request.getParameter((String) key) == null ? arr : arr[0]);
        } else {
            obj = map.get(key);
        }
        return obj;
    }

    public String getString(Object key) {
        return (String) get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object put(Object key, Object value) {
        if (value instanceof ClobProxyImpl) {            //读取oracle Clob类型数据
            try {
                ClobProxyImpl cpi = (ClobProxyImpl) value;
                Reader is = cpi.getCharacterStream();    //获取流
                BufferedReader br = new BufferedReader(is);
                String str = br.readLine();
                StringBuffer sb = new StringBuffer();
                while (str != null) {                        //循环读取数据拼接到字符串
                    sb.append(str);
                    sb.append("\n");
                    str = br.readLine();
                }
                value = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    public void clear() {
        map.clear();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public Set entrySet() {
        return map.entrySet();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Set keySet() {
        return map.keySet();
    }

    @SuppressWarnings("unchecked")
    public void putAll(Map t) {
        map.putAll(t);
    }

    public int size() {

        return map.size();
    }

    public Collection values() {
        return map.values();
    }

    public int GetParameterInt(String ParameterKey) {
        String value = "-1";
        int result = 0;
        if (map != null && map.size() > 0) {
            value = (String) map.get(ParameterKey);
        }
        result = Integer.parseInt(value);
        return result;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }
}
