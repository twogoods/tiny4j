package com.tg.tiny4j.core.ioc.beans.reader;

import com.tg.tiny4j.commons.utils.Validate;
import com.tg.tiny4j.core.ioc.beans.BeanDefinition;
import com.tg.tiny4j.core.ioc.beans.BeanPropertyValue;
import com.tg.tiny4j.core.ioc.beans.BeanReference;
import com.tg.tiny4j.core.ioc.exception.BeanDefinitionException;
import com.tg.tiny4j.core.ioc.resource.ResourceLoad;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.List;

/**
 * Created by twogoods on 16/10/25.
 */
public class XmlBeanDefinitionReader extends AnnotationBeanDefinitionReader {
    private static Logger log = LoggerFactory.getLogger(XmlBeanDefinitionReader.class);

    private String location;

    public XmlBeanDefinitionReader(String location) {
        this.location = location;
    }

    @Override
    public void loadResource() throws Exception {
        log.debug("XmlBeanDefinitionReader load...");
        InputStream inputStream = ResourceLoad.getInstance().loadResource(location).getInputStream();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();
            Document doc = docBuilder.parse(inputStream);
            Element root = doc.getDocumentElement();
            initConfig(root);
            //解析xml里配置的bean
            praseBeanDefinitions(root);
            //开始扫描注解的bean
            super.loadResource();
        } finally {
            inputStream.close();
        }
    }

    @Override
    public BeanDefinition handleIntegrationAnnotation(Class clazz) throws Exception {
        return null;
    }

    /**
     * config默认在classpath下取
     *
     * @param root
     */
    private void initConfig(Element root) throws Exception {
        NodeList propertieslist = root.getElementsByTagName("configs");
        if (propertieslist.getLength() != 1) {
            throw new BeanDefinitionException("In XML, <configs></configs> should be unique.");
        }
        if (!(propertieslist.item(0) instanceof Element)) {
            return;
        }
        Element configRoot = (Element) propertieslist.item(0);
        //配置文件
        NodeList propertyFiles = configRoot.getElementsByTagName("property-file");
        if (propertyFiles.getLength() > 0) {
            String filesPath = propertyFiles.item(0).getTextContent();
            if (StringUtils.isEmpty(filesPath)) {
                return;
            }
            String[] pathArr = filesPath.split(",");
            for (String filePath : pathArr) {
                loadConfig(filePath);
            }
        } else {
            //默认取application.properties
            loadConfig("application.properties");
        }
        //设置扫描的包
        NodeList packageFiles = configRoot.getElementsByTagName("component-scan");
        if (packageFiles.getLength() > 0) {
            String packageNames = packageFiles.item(0).getTextContent();
            packageNames = getConfigValue(PlaceholderPraser.prase(packageNames));
            if (StringUtils.isEmpty(packageNames)) {
                return;
            }
            String[] packageArr = packageNames.split(",");
            List<String> packages = PackageChecker.deDuplicatePackage(packageArr);
            setScanPackages(packages);
        }
    }

    private void praseBeanDefinitions(Element root) throws Exception {
        NodeList list = root.getElementsByTagName("bean");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                readBean(ele);
            }
        }
    }

    private void readBean(Element ele) throws Exception {
        String className = ele.getAttribute("class");
        if (Validate.isEmpty(className)) {
            throw new BeanDefinitionException("Bean definition need 'class' property");
        }
        String id = ele.getAttribute("id");
        if (Validate.isEmpty(id)) {
            id = getBeanNameByClassName(className);
        }
        BeanDefinition beanDefinition = new BeanDefinition(id, className);
        processProperty(ele, beanDefinition);
        if (getRegisterBeans().putIfAbsent(id, beanDefinition) != null) {
            throw new BeanDefinitionException(String.format("bean is duplicate,bean name is '%s'", id));
        }
    }


    private void processProperty(Element ele, BeanDefinition beanDefinition) throws Exception {
        NodeList propertyNode = ele.getElementsByTagName("property");
        for (int i = 0; i < propertyNode.getLength(); i++) {
            Node node = propertyNode.item(i);
            if (node instanceof Element) {
                Element propertyEle = (Element) node;
                String name = propertyEle.getAttribute("name");
                if (Validate.isEmpty(name)) {
                    throw new BeanDefinitionException("Bean definition <property> need 'name' property");
                }
                String value = propertyEle.getAttribute("value");
                String ref = propertyEle.getAttribute("ref");
                if (!Validate.isEmpty(value)) {
                    beanDefinition.addProperty(new BeanPropertyValue(name, value));
                } else if (!Validate.isEmpty(ref)) {
                    BeanReference beanReference = new BeanReference(ref);
                    beanDefinition.addProperty(new BeanPropertyValue(name, beanReference));
                } else {
                    throw new BeanDefinitionException("Bean definition <property> need 'name' property");
                }
            }
        }
    }
}
