package ioc;

import java.util.Set;

public interface Container {
	
	//根据Class获取Bean
	<T> T getBean(Class<T> clazz);
	
	//根据名称获取Bean
	<T> T getBean(String name);
	
	//注册一个Bean到容器中
    Object registerBean(Object bean);
	
    //注册一个Bean到容器中
	Object registerBean(Class<?> clazz);
	
	//注册一个带名称的Bean到容器中
	Object registerBean(String name, Object bean);
	
	//删除一个bean
	void removeBean(Class<?> clazz);
	
	//根据名称删除一个bean
	void removeBean(String name);
	
	//返回所有bean对象名称
	Set<String> getBeanNames();
	
	//初始化装配
	void initWired();

}
