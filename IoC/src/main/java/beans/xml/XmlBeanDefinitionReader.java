package beans.xml;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import beans.AbstractBeanDefinitionReader;
import beans.BeanDefinition;
import beans.BeanReference;
import beans.PropertyValue;
import beans.io.ResourceLoader;

public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader{

	public XmlBeanDefinitionReader(ResourceLoader resourceLoader) {
		super(resourceLoader);
	}

	public void loadBeanDefinitions(String location) throws Exception {
		InputStream in = getResourceLoader().getResource(location).getInputStream();
		doLoadBeanDefinitions(in);
	}
	
	protected void doLoadBeanDefinitions(InputStream in) throws Exception{
		in = new BufferedInputStream(in);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(in);
		registerBeanDefinitions(document);
		in.close();
	}
	
	protected void registerBeanDefinitions(Document document) {
		Element root = document.getDocumentElement();
		parseBeanDefinitions(root);
	}
	
	
	protected void parseBeanDefinitions(Element root) {
		NodeList nodeList = root.getChildNodes();
		for(int i=0; i<nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if(node instanceof Element) {
				Element element = (Element)node;
				processBeanDefinition(element);
			}
		}
	}
	
	protected void processBeanDefinition(Element bean) {
		String name = bean.getAttribute("id");
		String className = bean.getAttribute("class");
		BeanDefinition beanDefinition = new BeanDefinition();
		processProperty(bean, beanDefinition);
		beanDefinition.setBeanClassName(className);
		getRegistry().put(name, beanDefinition);
	}
	
	private void processProperty(Element bean, BeanDefinition beanDefinition) {
		NodeList nodeList = bean.getElementsByTagName("property");
		for(int i=0; i<nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if(node instanceof Element) {
				Element element = (Element) node;
				String name = element.getAttribute("name");
				String value = element.getAttribute("value");
				if(value != null ) {
					beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, value));
				}
				else {
					String ref = element.getAttribute("ref");
					if(ref == null || ref.length() ==0) {
						throw new IllegalArgumentException("Configuration problem: <property> element for property '"
								+ name + "' must specify a ref or value");
					}
					BeanReference beanReference = new BeanReference(ref);
					beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, beanReference));
				}
			}
		}
	}

}
