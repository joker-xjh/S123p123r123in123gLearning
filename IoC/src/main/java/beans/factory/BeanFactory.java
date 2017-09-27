package beans.factory;

//bean的容器，工厂

public interface BeanFactory {
	
	Object getBean(String name) throws Exception;

}
