package beans.factory;

import java.lang.reflect.Method;

import beans.BeanDefinition;
import beans.BeanReference;
import beans.PropertyValue;

//可自动装配内容的BeanFactory

public class AutowireCapableBeanFactory extends AbstractBeanFactory{
	
	
	@Override
	protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {
		
		for(PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValues()) {
			Object value = propertyValue.getValue();
			
			if(value instanceof BeanReference) {
				BeanReference beanReference = (BeanReference)value;
				value = getBean(beanReference.getName());
			}
			
			//调用setXXX方法设置属性
			
			Method method = bean.getClass().getDeclaredMethod(
					"set" + propertyValue.getName().substring(0, 1).toUpperCase() + propertyValue.getName().substring(1)
					, value.getClass());
			boolean b = method.isAccessible();
			method.setAccessible(true);
			method.invoke(bean, value);
			method.setAccessible(b);
		}
		
	}

}
