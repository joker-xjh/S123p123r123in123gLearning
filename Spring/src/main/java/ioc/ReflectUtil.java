package ioc;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ReflectUtil {
	
	//新建对象
	public static Object newInstance(String className) {
		Object object = null;
		try {
			Class<?> clazz = Class.forName(className);
			object = clazz.newInstance();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return object;
	}
	
	//新建对象
	public static Object newInstance(Class<?> clazz) {
		try {
			return clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@SuppressWarnings("unchecked")
	public static <T> T cast(Object object, Class<T> type) {
		if(object == null)
			return null;
		if(!type.isAssignableFrom(object.getClass())) {
			if(is(type, int.class, Integer.class)) {
				object = Integer.parseInt(String.valueOf(object));
			}
			else if(is(type, long.class, Long.class)) {
				object = Long.parseLong(String.valueOf(object));
			}
			else if(is(type, double.class, Double.class)) {
				object = Double.parseDouble(String.valueOf(object));
			}
			else if(is(type, float.class, Float.class)) {
				object = Float.parseFloat(String.valueOf(object));
			}
			else if(is(type, boolean.class, Boolean.class)) {
				object = Boolean.parseBoolean(String.valueOf(object));
			}
			else if(is(type, String.class)) {
				object = String.valueOf(object);
			}
			
		}
		
		return (T)object;
	}
	
	
	/** 查找方法 */
	public static Method getMethodByName(Object classOrBean, String methodName) {
		Method method = null;
		if(classOrBean != null) {
			Class<?> clazz = null;
			if(classOrBean instanceof Class<?>) {
				clazz = (Class<?>) classOrBean;
			}
			else {
				clazz = classOrBean.getClass();
			}
			for(Method m : clazz.getMethods()) {
				if(m.getName().equals(methodName)) {
					method = m;
					break;
				}
			}
		}
		return method;
	}
	
	
	public static Method getMethodByName(Class<?> clazz, String methodName) {
		Method method = null;
		if(clazz != null) {
			for(Method m : clazz.getMethods()) {
				if(m.getName().equals(methodName)) {
					method = m;
					break;
				}
			}
		}
		return method;
	}
	
	
	/** 扫描包下面所有的类 */
	public static List<String> scanPackageClass(String packageName) {
		List<String> classNames = new ArrayList<String>();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource(packageName.replace('.', '/'));
		String protocol = url.getProtocol();
		if("file".equals(protocol)) {
			try {
				File[] files = new File(url.toURI()).listFiles();
				for(File file : files) {
					scanPackageClassInFile(packageName, file, classNames);
				}
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		}
		else if("jar".equals(protocol)) {
			try {
				JarFile jarFile = ((JarURLConnection)url.openConnection()).getJarFile();
				scanPackageClassInJar(jarFile, packageName, classNames);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return classNames;
	}
	
	
	
	
	/** 扫描文件夹下所有class文件 */
	private static void scanPackageClassInFile(String packageName, File file, List<String> classNames) {
		if(file.isFile() && file.getName().endsWith(".class")) {
			classNames.add(packageName+"."+file.getName().substring(0, file.getName().length()-6));
		}
		else if(file.isDirectory()) {
			packageName += "."+file.getName();
			for(File f:file.listFiles()) {
				scanPackageClassInFile(packageName, f, classNames);
			}
		}
	}
	
	
	//扫描jar里面的类
	private static void scanPackageClassInJar(JarFile jarFile, String packageName, List<String> classNames) {
		Enumeration<JarEntry> entries = jarFile.entries();
		while(entries.hasMoreElements()) {
			JarEntry jarEntry = entries.nextElement();
			String name = jarEntry.getName().replace('/', '.');
			if(name.startsWith(packageName) && name.endsWith(".class")) {
				classNames.add(name.substring(0, name.length()-6));
			}
		}
	}
	
	
	
	
	
	
	
	/** 对象是否其中一个 */
	public static boolean is(Object obj, Object... target) {
		if(obj != null && target!=null && target.length >0) {
			for(Object temp : target)
				if(obj.equals(temp))
					return true;
		}
		return false;
	}
	
	public static boolean isNot(Object obj, Object... mybe) {
		return !is(obj, mybe);
	}
	
	
	
	
	
	
	
	
	

}
