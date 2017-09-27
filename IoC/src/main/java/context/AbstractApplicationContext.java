package context;

import java.util.List;

import beans.BeanPostProcessor;
import beans.factory.AbstractBeanFactory;

public abstract class AbstractApplicationContext implements ApplicationContext{
	
	protected AbstractBeanFactory beanFactory;
	
	public AbstractApplicationContext(AbstractBeanFactory beanFactory){
		this.beanFactory = beanFactory;
		
	}
	
	public void refresh() throws Exception{
		loadBeanDefinitions(beanFactory);
		registerBeanPostProcessors(beanFactory);
	}
	
	public void registerBeanPostProcessors(AbstractBeanFactory beanFactory) throws Exception{
		List<Object> beanPostProcessors = beanFactory.getBeansForType(BeanPostProcessor.class);
		for(Object bean : beanPostProcessors)
			beanFactory.addBeanPostProcessor((BeanPostProcessor)bean);
	}
	
	protected abstract void loadBeanDefinitions(AbstractBeanFactory beanFactory) throws Exception;

	public Object getBean(String name) throws Exception {
		return beanFactory.getBean(name);
	}

}
