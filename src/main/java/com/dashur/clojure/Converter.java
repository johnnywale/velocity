package com.dashur.clojure;

import clojure.lang.IRecord;
import clojure.lang.Keyword;

import java.util.Map;
import java.util.regex.Pattern;

public class Converter {
    private IRecord entity;

    public static Object convertRecord(Map map) throws Exception {
        if (map instanceof IRecord) {
            Converter converter = new Converter();
            converter.setEntity((IRecord) map);
            return converter;
        } else {
            return map;
        }
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(IRecord entity) {
        this.entity = entity;
    }

    public String toString() {
        return entity.toString();
    }

    public Object get(String o) {
        String[] data = o.split(Pattern.quote("."));
        Map m = (Map) entity;
        Object finalResult = null;
        for (String x : data) {
            Keyword key = Keyword.intern(x);
            finalResult = m.get(key);
            if (finalResult instanceof Map) {
                m = (Map) finalResult;
            }
        }
        if (finalResult != null && finalResult instanceof IRecord) {
            Converter converter = new Converter();
            converter.setEntity((IRecord) finalResult);
            return converter;
        } else {
            return finalResult;
        }
     }
}