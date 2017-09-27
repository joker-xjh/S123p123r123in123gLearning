package context;

import beans.BeanDefinition;
import beans.factory.AbstractBeanFactory;
import beans.factory.AutowireCapableBeanFactory;
import beans.io.ResourceLoader;
import beans.xml.XmlBeanDefinitionReader;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext{
	
	private String location;
	
	public ClassPathXmlApplicationContext(String location) throws Exception {
		this(new AutowireCapableBeanFactory(), location);
	}
	

	public ClassPathXmlApplicationContext(AbstractBeanFactory beanFactory, String location) throws Exception {
		super(beanFactory);
		this.location = location;
		refresh();
	}

	@Override
	protected void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception {
		XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(new ResourceLoader());
		xmlBeanDefinitionReader.loadBeanDefinitions(location);
		for(String key: xmlBeanDefinitionReader.getRegistry().keySet()) {
			BeanDefinition beanDefinition = xmlBeanDefinitionReader.getRegistry().get(key);
			beanFactory.registerBeanDefinition(key, beanDefinition);
		}
	}

}
