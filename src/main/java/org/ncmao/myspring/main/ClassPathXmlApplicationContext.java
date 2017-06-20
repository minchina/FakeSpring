package org.ncmao.myspring.main;

import org.apache.commons.beanutils.BeanUtils;
import org.ncmao.myspring.config.Bean;
import org.ncmao.myspring.config.ConfigManager;
import org.ncmao.myspring.config.Property;
import org.ncmao.myspring.main.BeanFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ClassPathXmlApplicationContext implements BeanFactory {

    private Map<String, Bean> map;

    private Map<String, Object> context = new HashMap<String, Object>();

    public ClassPathXmlApplicationContext(String path) {
        map = ConfigManager.getConfig(path);

        for (Map.Entry<String, Bean> en : map.entrySet()) {
            String beanName = en.getKey();
            Bean bean = en.getValue();

            Object existBean = context.get(beanName);
            if (existBean == null && "singleton".equals(bean.getScope())) {
                Object newObject = createBean(bean);
                context.put(beanName, newObject);
            }
        }
    }


    private Object createBean(Bean bean) {
        Class clazz = null;
        try {
             clazz = Class.forName(bean.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Object beanObj;

        try {
            beanObj = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("without no paramater constructor");
        }
        if (!bean.getProperties().isEmpty()) {
            for (Property property : bean.getProperties()) {
                String name = property.getName();
                String value = property.getValue();
                String ref = property.getRef();
                if (value != null) {
                    Map<String, String> parmMap = new HashMap<String, String>();
                    parmMap.put(name, value);
                    try {
                        BeanUtils.populate(beanObj, parmMap);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                if (ref != null) {
                    Object existBean = context.get(property.getRef());
                    if (existBean == null) {
                        existBean = createBean(map.get(property.getRef()));
                        if (map.get(property.getRef()).getScope().equals("singleton")) {
                            context.put(property.getRef(), existBean);
                        }
                    }

                    try {
                        BeanUtils.setProperty(beanObj, name, existBean);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }

                }
            }

        }
        return beanObj;
    }

    public Object getBean(String name) {
        Object bean = context.get(name);
        if (bean == null) {
            bean = createBean(map.get(name));
        }

        return bean;
    }
}
