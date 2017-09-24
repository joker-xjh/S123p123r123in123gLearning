package context;

import java.util.List;

import beans.BeanPostProcessor;
import beans.factory.AbstractBeanFactory;

public abstract class AbstractApplicationContext implements ApplicationContext{
	
	protected AbstractBeanFactory beanFactory;
	
	
	public AbstractApplicationContext(AbstractBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
	
	protected void refresh() throws Exception {
		loadBeanDefinitions(beanFactory);	
		registerBeanPostProcessors(beanFactory);
	}
	
	
	protected abstract void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception; 
	
	
	public Object getBean(String name) throws Exception {
		return beanFactory.getBean(name);
	}
	
	protected void registerBeanPostProcessors(AbstractBeanFactory abstractBeanFactory) throws Exception {
		List<Object> beanPostProcessors = abstractBeanFactory.getBeansForType(BeanPostProcessor.class);
		for(Object bean : beanPostProcessors) {
			abstractBeanFactory.addBeanPostProcessor((BeanPostProcessor)bean);
		}
	}

}
