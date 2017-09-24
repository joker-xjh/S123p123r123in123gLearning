package beans.xml;

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
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(in);
		registerBeanDefinitions(document);
		in.close();
	}
	
	protected void registerBeanDefinitions(Document document) throws Exception {
		Element root = document.getDocumentElement();
		parseBeanDefinitions(root);
	}
	
	protected void parseBeanDefinitions(Element root) throws Exception {
		NodeList list = root.getChildNodes();
		for(int i=0; i<list.getLength(); i++) {
			Node node = list.item(i);
			if(node instanceof Element) {
				Element element = (Element) node;
				processBeanDefinition(element);
			}
		}
	}
	
	protected void processBeanDefinition(Element element) throws Exception {
		String name = element.getAttribute("id");
		String className = element.getAttribute("class");
		BeanDefinition beanDefinition = new BeanDefinition();
		processProperty(element, beanDefinition);
		beanDefinition.setBeanClassName(className);
		getRegistry().put(name, beanDefinition);
	}
	
	protected void processProperty(Element element, BeanDefinition beanDefinition) throws Exception {
		NodeList list = element.getElementsByTagName("property");
		for(int i=0; i<list.getLength(); i++) {
			Node node = list.item(i);
			if(node instanceof Element) {
				Element property = (Element)node;
				String name = property.getAttribute("name");
				String value = property.getAttribute("value");
				if(value != null && value.length() > 0) {
					beanDefinition.getPropertyValues().addPropertyValue(new PropertyValue(name, value));
				}
				else {
					String ref = property.getAttribute("ref");
					if(ref==null || ref.length()==0) {
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
