package org.ncmao.myspring.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigManager {
    private static Map<String, Bean> map = new HashMap<String, Bean>();


    public static Map<String, Bean> getConfig(String path) {
        SAXReader saxReader = new SAXReader();
        InputStream inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(path);
        Document document = null;
        try {
            document = saxReader.read(inputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        String xpath="//bean";
        List<Element> list = document.selectNodes(xpath);
        if (list != null) {
            for (Element element : list) {
                Bean bean = new Bean();
                String name = element.attributeValue("name");
                bean.setName(name);
                bean.setClassName(element.attributeValue("class"));
                String scope = element.attributeValue("scope");
                if (scope != null) {
                    bean.setScope(scope);
                }
                List<Element> childElements = element.elements();
                if (childElements != null) {
                    for (Element childElement : childElements) {
                        Property property = new Property();
                        property.setName(childElement.attributeValue("name"));
                        property.setValue(childElement.attributeValue("value"));
                        property.setRef(childElement.attributeValue("ref"));
                        bean.getProperties().add(property);
                    }

                }
                map.put(name, bean);
            }

        }
        return map;
    }

}
