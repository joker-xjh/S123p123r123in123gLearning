package context;

import java.util.Map.Entry;

import beans.BeanDefinition;
import beans.factory.AbstractBeanFactory;
import beans.factory.AutowireCapableBeanFactory;
import beans.io.ResourceLoader;
import beans.xml.XmlBeanDefinitionReader;

public class ClassPathXmlApplicationContext extends AbstractApplicationContext{
	
	private String configuration;
	
	public ClassPathXmlApplicationContext(String configuration) {
		this(configuration, new AutowireCapableBeanFactory());
	}

	public ClassPathXmlApplicationContext(String configuration, AbstractBeanFactory beanFactory) {
		super(beanFactory);
		this.configuration = configuration;
	}

	@Override
	protected void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception {
		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(new ResourceLoader());
		reader.loadBeanDefinitions(configuration);
		for(Entry<String, BeanDefinition> entry : reader.getRegistry().entrySet()) {
			beanFactory.registerBeanDefinition(entry.getKey(), entry.getValue());
		}
	}

}
