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
		if(beanDefinition == null)
			throw new IllegalArgumentException("No bean named "+name +" is defined");
		Object bean = beanDefinition.getBean();
		if(bean == null) {
			bean = doCreateBean(beanDefinition);
			bean = initializeBean(bean, name);
			beanDefinition.setBean(bean);
		}
		return bean;
	}
	
	
	//初始化bean，即做一些初始化的工作
	protected Object initializeBean(Object bean, String name) throws Exception{
		for(BeanPostProcessor beanPostProcessor : beanPostProcessors) {
			bean = beanPostProcessor.postProcessBeforeInitialization(bean, name);
		}
		
		for(BeanPostProcessor beanPostProcessor : beanPostProcessors) {
			bean = beanPostProcessor.postProcessAfterInitialization(bean, name);
		}
		
		return bean;
	}
	
	
	//创建bean的实例
	protected Object createBeanInstance(BeanDefinition beanDefinition) throws Exception {
		return beanDefinition.getBeanClass().newInstance();
	}
	
	
	////注册bean，即将类名和类的定义保存到内存（map对象）中
	public void registerBeanDefinition(String name, BeanDefinition beanDefinition) throws Exception {
		beanDefinitionMap.put(name, beanDefinition);
		//备份
		beanDefinitionNames.add(name);
	}
	
	
	//创建bean
	protected Object doCreateBean(BeanDefinition beanDefinition) throws Exception {
		Object bean = createBeanInstance(beanDefinition);
		beanDefinition.setBean(bean);
		applyPropertyValues(bean, beanDefinition);
		return bean;
	}
	
	
	//设置bean的引用的实例对象
	protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {
		
	}
	
	
	//根据类型返回beans
	public List<Object> getBeansForType(Class<?> type) throws Exception {
		List<Object> beans = new ArrayList<Object>();
		for(String beanName : beanDefinitionNames) {
			if(type.isAssignableFrom(beanDefinitionMap.get(beanName).getBeanClass())) {
				beans.add(getBean(beanName));
			}
		}
		return beans;
	}
	
	
	public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) throws Exception {
		this.beanPostProcessors.add(beanPostProcessor);
	}
	
	
	
	
	

}
