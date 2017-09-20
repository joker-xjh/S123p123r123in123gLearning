package ioc;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unchecked")
public class SimpleContainer implements Container {
	
	private Map<String, Object> beans;
	private Map<String, String> beanKeys;
	
	
	public SimpleContainer() {
		beanKeys = new ConcurrentHashMap<String, String>();
		beans = new ConcurrentHashMap<String, Object>();
	}
	

	public <T> T getBean(Class<T> clazz) {
		String className = clazz.getName();
		Object object = beans.get(className);
		if(object != null) {
			return (T)object;
		}
		return null;
	}

	public <T> T getBean(String name) {
		String className = beanKeys.get(name);
		Object object = beans.get(className);
		if(object != null)
			return (T)object;
		return null;
	}

	public Object registerBean(Object bean) {
		String className = bean.getClass().getName();
		beanKeys.put(className, className);
		beans.put(className, bean);
		return bean;
	}

	public Object registerBean(Class<?> clazz) {
		Object object = ReflectUtil.newInstance(clazz);
		String className = clazz.getName();
		beanKeys.put(className, className);
		beans.put(className, object);
		return object;
	}

	public Object registerBean(String name, Object bean) {
		String className = bean.getClass().getName();
		beanKeys.put(name, className);
		beans.put(className, bean);
		return bean;
	}

	public void removeBean(Class<?> clazz) {
		String className = clazz.getName();
		beanKeys.remove(className);
		beans.remove(className);
	}

	public void removeBean(String name) {
		String className = beanKeys.get(name);
		beans.remove(className);
		beanKeys.remove(name);
	}

	public Set<String> getBeanNames() {
		return beanKeys.keySet();
	}

	public void initWired() {
		for(String key : beans.keySet()) {
			Object bean = beans.get(key);
			injection(bean);
		}
	}
	
	public void injection(Object object) {
		Field[] fields = object.getClass().getDeclaredFields();
		for(Field field : fields) {
			Autowired autowired = field.getAnnotation(Autowired.class);
			if(autowired != null) {
				Object autoWiredField = null;
				String name = autowired.name();
				if(!name.equals("")) {
					String className = beanKeys.get(name);
					if(className != null && !className.equals("")) {
						autoWiredField = beans.get(className);
					}
					if(autoWiredField == null)
						throw new RuntimeException("Unable to load " + name);
				}
				else {
					if(autowired.value() == Class.class)
						autoWiredField = recursiveAssembly(field.getType());
					else {
						autoWiredField = this.getBean(autowired.value());
						if (null == autoWiredField) {
			            	autoWiredField = recursiveAssembly(autowired.value());
			            }
					}
				}
				
				 if (null == autoWiredField) {
			         throw new RuntimeException("Unable to load " + field.getType().getCanonicalName());
			     }
				 
				 boolean accessible = field.isAccessible();
			     field.setAccessible(true);
			     try {
					field.set(object, autoWiredField);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			     field.setAccessible(accessible);
				 
			}
			
			 
			
			
		}
	}
	
	
	
	
	
	private Object recursiveAssembly(Class<?> clazz) {
		if(clazz != null)
			return registerBean(clazz);
		return null;
	}
	
	
	
	
	
	

}
