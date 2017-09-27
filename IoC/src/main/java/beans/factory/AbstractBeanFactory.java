package beans.factory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import beans.BeanDefinition;
import beans.BeanPostProcessor;

public abstract class AbstractBeanFactory implements BeanFactory{
	
	private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
	
	private List<String> beanDefinitionNames = new ArrayList<String>();
	
	private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();
	
	
	public Object getBean(String name) throws Exception {
		BeanDefinition beanDefinition = beanDefinitionMap.get(name);
		if(beanDefinition == null) {
			throw new IllegalArgumentException("No bean named " + name + " is defined");
		}
		
		Object bean = beanDefinition.getBean();
		if(bean == null) {
			bean = doCreateBean(beanDefinition);
			bean = initializeBean(bean, name);
			beanDefinition.setBean(bean);
		}
		return bean;
	}
	
	
	
	protected Object initializeBean(Object bean, String name) throws Exception{
		for(BeanPostProcessor beanPostProcessor : beanPostProcessors) {
			bean = beanPostProcessor.postProcessBeforeInitialization(bean, name);
		}
		for(BeanPostProcessor beanPostProcessor : beanPostProcessors) {
			bean = beanPostProcessor.postProcessAfterInitialization(bean, name);
		}
		return bean;
	}
	
	public Object doCreateBean(BeanDefinition beanDefinition) throws Exception {
		Object bean = createBeanInstance(beanDefinition);
		beanDefinition.setBean(bean);
		applyPropertyValues(bean, beanDefinition);
		return bean;
	}
	
	
	
	protected Object createBeanInstance(BeanDefinition beanDefinition) throws Exception {
		return beanDefinition.getBeanClass().newInstance();
	}
	
	public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) throws Exception {
		beanDefinitionMap.put(beanName, beanDefinition);
		beanDefinitionNames.add(beanName);
	}
	
	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) throws Exception {
		beanPostProcessors.add(beanPostProcessor);
	}
	
	protected abstract void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception;
	
	public List<Object> getBeansForType(Class<?> type) throws Exception {
		List<Object> beans = new ArrayList<Object>();
		for(String beanName : beanDefinitionNames) {
			if(type.isAssignableFrom(beanDefinitionMap.get(beanName).getBeanClass())) {
				beans.add(beanDefinitionMap.get(beanName).getBean());
			}
		}
		return beans;
	}
	
	

}
