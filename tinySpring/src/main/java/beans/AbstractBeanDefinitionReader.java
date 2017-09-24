package beans;

import java.util.HashMap;
import java.util.Map;

import beans.io.ResourceLoader;

//从配置中读取BeanDefinition，抽象类

public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{
	
	private Map<String, BeanDefinition> registry;
	
	private ResourceLoader resourceLoader;
	
	public AbstractBeanDefinitionReader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
		registry = new HashMap<String, BeanDefinition>();
	}

	
	public Map<String, BeanDefinition> getRegistry() {
		return registry;
	}
	
	public ResourceLoader getResourceLoader() {
		return resourceLoader;
	}
	
}
