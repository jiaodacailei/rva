package com.ruoyi.rva.framework.domain;

import com.ruoyi.rva.framework.util.RvaJsonUtils;
import com.ruoyi.rva.framework.util.RvaUtils;

import java.util.*;

public class RvaMap<K, V> extends LinkedHashMap<K, V> {

    public RvaMap() {
        super();
    }

    public RvaMap(K k, V v) {
        super();
        put(k, v);
    }

    public RvaMap(Map<K, V> map) {
        super(map);
    }

    public String getString(K key) {
        return getString(key, null);
    }

    public String getString(K key, String defaultValue) {
        V val = this.get(key);
        if (val == null || "".equals(val)) {
            return defaultValue;
        }
        return val.toString();
    }

    public Integer getInt(K key) {
        return RvaUtils.getInt(this.get(key));
    }

    public Integer getInt(K key, Integer defaultVal) {
        return RvaUtils.getInt(this.get(key), defaultVal);
    }

    public Boolean isEmpty(K key) {
        return RvaUtils.isEmpty(this.get(key));
    }

    public Boolean isNotEmpty(K key) {
        return !isEmpty(key);
    }

    public Boolean equals(K key, Object val) {
        V v = get(key);
        if (v == null) {
            return false;
        }
        return v.equals(val);
    }

    private List<K> rvaKeys = new ArrayList<>();

    public V put(K key, V value) {
        if (!rvaKeys.contains(key)) {
            rvaKeys.add(key);
        }
        return super.put(key, value);
    }

    public List<K> getRvaKeys() {
        return rvaKeys;
    }

    public RvaMap<K, V> rvaPut(K key, V value) {
        put(key, value);
        return this;
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public Boolean rvaEquals(K key, V value) {
        V v = get(key);
        if (v == null) {
            return false;
        }
        return v.equals(value);
    }

    public RvaMap<K, V> rvaPutByPrefix(String prefix, V value) {
        for (K k : this.keySet()) {
            if (k.toString().endsWith(prefix)) {
                put(k, value);
            }
        }
        return this;
    }

    public RvaMap<K, V> rvaPutAll(Map<K, V> map) {
        putAll(map);
        return this;
    }

    public Long getLong(K key) {
        return RvaUtils.getLong(this.get(key));
    }

    public Long getLong(K key, Long defaultVal) {
        return RvaUtils.getLong(this.get(key), defaultVal);
    }

    public Boolean getBoolean(K key, Boolean defaultVal) {
        return RvaUtils.getBoolean(this.get(key), defaultVal);
    }

    public String[] getStrings(K key) {
        String val = getString(key);
        return val.split(",");
    }

    public List<String> getStringList(K key) {
        String val = getString(key);
        if (RvaUtils.isEmpty(val)) {
            return new ArrayList<String>();
        }
        return Arrays.asList(val.split(","));
    }

    public String getStringBySuffix(String suffix) {
        for (K key : this.keySet()) {
            if (key.toString().endsWith(suffix)) {
                return getString(key);
            }
        }
        return null;
    }

    public String getStringByPrefix(String prefix) {
        for (K key : this.keySet()) {
            if (key.toString().startsWith(prefix)) {
                return getString(key);
            }
        }
        return null;
    }

    public List getList(K key) {
        List list = (List) get(key);
        if (list == null) {
            List v = new ArrayList();
            this.rvaPut(key, (V) v);
            return v;
        }
        return list;
    }

    public List<Map> getMapList(K key) {
        List<Map> list = (List) get(key);
        if (list == null) {
            List<Map> v = new ArrayList();
            this.rvaPut(key, (V) v);
            return v;
        }
        return list;
    }

    public RvaMap getMap(K key) {
        Object val = get(key);
        Map map = null;
        if (val != null) {
            if (val instanceof Map) {
                map = (Map) get(key);
            } else if (val instanceof String) {
                if (!val.toString().equals("")) {
                    map = RvaJsonUtils.readAsType(val.toString(), Map.class);
                }
            }
        }
        RvaMap v = new RvaMap();
        if (map != null) {
            v.rvaPutAll(map);
        }
        this.rvaPut(key, (V) v);
        return v;
    }

    public void addToList(K key, Object obj) {
        List list = (List) get(key);
        if (list == null) {
            list = new ArrayList();
            super.put(key, (V) list);
        }
        list.add(obj);
    }

    public List<Map> parseList(K key) {
        String string = getString(key);
        return RvaJsonUtils.readAsList(string, Map.class);
    }

    public Double getDoule(K key) {
        return RvaUtils.getDouble(this.get(key));
    }
}
