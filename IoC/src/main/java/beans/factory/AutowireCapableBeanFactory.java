package beans.factory;

import java.lang.reflect.Method;

import beans.BeanDefinition;
import beans.BeanReference;
import beans.PropertyValue;

public class AutowireCapableBeanFactory extends AbstractBeanFactory{

	@Override
	protected void applyPropertyValues(Object bean, BeanDefinition beanDefinition) throws Exception {
		for(PropertyValue propertyValue : beanDefinition.getPropertyValues().getPropertyValues()) {
			String name = propertyValue.getName();
			Object value = propertyValue.getValue();
			Class<?> type = bean.getClass().getDeclaredField(propertyValue.getName()).getType();
			if(value instanceof BeanReference) {
				BeanReference beanReference = (BeanReference) value;
				value = getBean(beanReference.getName());
			}
			else {
				value = parseType(value, type);
			}
			Method method = beanDefinition.getClass().getDeclaredMethod(
					 "set" + name.substring(0, 1).toUpperCase() + name.substring(1)
					,type);
			boolean b = method.isAccessible();
			method.setAccessible(true);
			method.invoke(bean, value);
			method.setAccessible(b);
		}
		
	}
	
	protected Object parseType(Object value, Class<?> type) throws Exception{
		if(type.equals(int.class) || type.equals(Integer.class)) {
			return Integer.parseInt(value.toString());
		}
		else if(type.equals(Long.class) || type.equals(long.class)) {
			return Long.parseLong(value.toString());
		}
		else if(type.equals(double.class) || type.equals(Double.class)) {
			return Double.parseDouble(value.toString());
		}
		else if(type.equals(Boolean.class) || type.equals(boolean.class)) {
			return Boolean.parseBoolean(value.toString());
		}
		else if(type.equals(char.class) || type.equals(Character.class)) {
			return value.toString().charAt(0);
		}
		else if(type.equals(String.class)) {
			return value.toString();
		}
		return value;
	}

}
