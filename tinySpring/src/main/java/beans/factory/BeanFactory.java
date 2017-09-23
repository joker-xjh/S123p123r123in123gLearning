package beans.factory;

//bean的容器，工厂

public interface BeanFactory {
	
	//获取bean的实例对象
    Object getBean(String name) throws Exception;

}
