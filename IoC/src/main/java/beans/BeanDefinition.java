package beans;

//bean的内容及元数据，保存在BeanFactory中，包装bean的实体

public class BeanDefinition {
	
	private Object bean;
	
	private Class<?> beanClass;
	
	private String beanClassName;
	
	private PropertyValues propertyValues;
	
	public BeanDefinition() {
		propertyValues = new PropertyValues();
	}
	
	public void setBean(Object bean) {
		this.bean = bean;
	}
	
	public Class<?> getBeanClass() {
		return beanClass;
	}
	
	public void setBeanClass(Class<?> beanClass) {
		this.beanClass = beanClass;
	}
	
	public Object getBean() {
		return bean;
	}
	
	public void setPropertyValues(PropertyValues propertyValues) {
		this.propertyValues = propertyValues;
	}
	
	public void setBeanClassName(String beanClassName) {
		this.beanClassName = beanClassName;
		try {
			this.beanClass = Class.forName(beanClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public String getBeanClassName() {
		return beanClassName;
	}
	
	public PropertyValues getPropertyValues() {
		return propertyValues;
	}
	

}
