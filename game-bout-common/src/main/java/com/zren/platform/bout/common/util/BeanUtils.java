package com.zren.platform.bout.common.util;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class BeanUtils {
	public static Class<?> classForName(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Object objectForName(String className) {
		return newInstance(classForName(className));
	}

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

	public static Field[] getFields(Object o) {
		return getFields(o.getClass());
	}

	public static Field getField(Class<?> c, String f) {
		try {
			return c.getField(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Field getField(Object c, String f) {
		return getField(c.getClass(), f);
	}

	public static Field[] getFields(Class<?> c) {
		return c.getDeclaredFields();
	}

	public static Method[] getMethods(Object o) {
		return getMethods(o.getClass());
	}

	public static Method[] getMethods(Class<?> c) {
		return c.getDeclaredMethods();
	}

	public static Method getMethod(Class<?> c, String method, Class<?>[] types) {
		try {
			return c.getMethod(method, types);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void copyFields(Object src, Object target) throws Exception {
		Field[] srcFields = src.getClass().getDeclaredFields();
		Field[] targetFields = target.getClass().getDeclaredFields();
		for (Field targetField : targetFields) {
			for (Field srcField : srcFields) {
				if (targetField.getName().equals(srcField.getName())) {
					targetField.setAccessible(true);
					srcField.setAccessible(true);
					targetField.set(target, Convert.parseTo(srcField.get(src), targetField.getType()));
					break;
				}
			}
		}
	}

	public static Method getMethod(Object c, String method) {
		return getMethod(c.getClass(), method);
	}

	public static Method getMethod(Class<?> c, String method) {
		try {
			Method[] ms = c.getMethods();
			for (Method m : ms) {
				if (m.getName().equals(method)) {
					return m;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Method getMethod(Object c, String method, Class<?>[] types) {
		return getMethod(c.getClass(), method, types);
	}

	public static Field findAttribute(Object obj, String name) throws NoSuchFieldException {
		Class<?> clazz = obj.getClass();
		Field field = null;
		while (field == null && clazz != Object.class) {
			try {
				field = clazz.getDeclaredField(name);
			} catch (NoSuchFieldException e) {
			}
			if (field == null) {
				clazz = clazz.getSuperclass();
			}
		}
		if (field == null) {
			throw new NoSuchFieldException(name);
		}
		return field;
	}

	public static Object getAttribute(Object obj, String name) throws Exception {
		Field field = BeanUtils.findAttribute(obj, name);
		field.setAccessible(true);
		return field.get(obj);
	}

	public static void setAttribute(Object obj, String name, Object value) throws Exception {
		Field field = BeanUtils.findAttribute(obj, name);
		field.setAccessible(true);
		field.set(obj, Convert.parseTo(value, field.getType()));
	}

	public static Object getProperty(Object obj, String name) throws Exception {
		PropertyDescriptor pd = new PropertyDescriptor(name, obj.getClass());
		return pd.getReadMethod().invoke(obj);
	}

	public static void setProperty(Object obj, String name, Object value) throws Exception {
		PropertyDescriptor pd = new PropertyDescriptor(name, obj.getClass());
		pd.getWriteMethod().invoke(obj, Convert.parseTo(value, pd.getPropertyType()));
	}

	public static Object copy(Object source) throws Exception {
		Object target = source.getClass().newInstance();
		BeanUtils.copy(source, target);
		return target;
	}

	public static <T> void copy(T source, T target) {
		Field[] arr = source.getClass().getDeclaredFields();
		try {
			for (int i = 0; i < arr.length; ++i) {
				Field field = arr[i];
				field.setAccessible(true);
				field.set(target, field.get(source));
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	public static void map2obj(Map<String, Object> map, Object obj) throws Exception {
		Field[] fields = obj.getClass().getDeclaredFields();
		for(Field field:fields) {
			//过滤枚举类型
			if(field.getType().equals(Enum.class)) {
				continue;
			}
			field.setAccessible(true);
			if(map.get(field.getName())==null) {
				continue;
			}
			field.set(obj, Convert.parseTo(map.get(field.getName()),field.getType()));
		}
	}

	public static Map<String, Object> obj2map(Object obj) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Field[] arr = obj.getClass().getDeclaredFields();
		for (int i = 0; i < arr.length; ++i) {
			Field field = arr[i];
			field.setAccessible(true);
			map.put(field.getName(), field.get(obj));
		}
		return map;
	}

	public static Class<?> getEntityClass(Object obj, int index) {
		return (Class<?>) ((ParameterizedType) obj.getClass().getGenericSuperclass()).getActualTypeArguments()[index];
	}

	public static Class<?>[] getClasses(Object... objects) {
		if (objects != null) {
			Class<?>[] classes = new Class<?>[objects.length];
			for (int i = 0; i < objects.length; ++i) {
				classes[i] = objects[i].getClass();
			}
			return classes;
		}
		return new Class<?>[0];
	}

	public static final List<Class<?>> getClasses(String packageName) throws ClassNotFoundException, IOException {
		return getClasses(packageName, false);
	}

	public static final List<Class<?>> getClasses(String packageName, boolean iterative)
			throws ClassNotFoundException, IOException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Enumeration<URL> resources = classLoader.getResources(packageName.replace('.', '/'));
		List<File> dirs = new Vector<File>();
		while (resources.hasMoreElements()) {
			URL resource = resources.nextElement();
			dirs.add(new File(resource.getFile()));
		}
		List<Class<?>> classes = new Vector<Class<?>>();
		for (File directory : dirs) {
			classes.addAll(findClasses(directory, packageName, iterative));
		}
		return classes;
	}

	private static final List<Class<?>> findClasses(File directory, String packageName, boolean iterative)
			throws ClassNotFoundException {
		List<Class<?>> classes = new Vector<Class<?>>();
		if (!directory.exists()) {
			return classes;
		}
		File[] files = directory.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				if (iterative) {
					classes.addAll(findClasses(file, packageName + "." + file.getName(), iterative));
				}
			} else if (file.getName().endsWith(".class")) {
				classes.add(
						Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
			}
		}
		return classes;
	}

	/**
	 * 从map列表中获取指定 的key
	 * 
	 * @param mapList
	 * @param key
	 * @return
	 */
	public static final List<Object> pickup(List<Map<String, Object>> mapList, String key) {
		List<Object> list = new ArrayList<Object>();
		for (Map<String, Object> map : mapList) {
			if (!Strings.isNullOrEmpty(map.get(key))) {
				list.add(map.get(key));
			}
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public static final <T>List<T> pickup1(List<Map<String,Object>> mapList,String key){
		List<T> list = new ArrayList<T>();
		for(Map<String,Object> map:mapList) {
			list.add((T)map.get(key));
		}
		return list;
	}
	
	/**
	 * 从对象列列表里边提取指定的key
	 * @param ObjectList
	 * @param key
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static final <T>List<T> pickupObject(List<?> ObjectList,String key){
		List<T> list = new ArrayList<T>();
		String k=Strings.ucfirst(key);
		for (Object o:ObjectList) {
			Method method=null;
			try {
				method = o.getClass().getMethod("get"+k);
				Object temp=method.invoke(o);
				if(temp!=null) {
					list.add((T)temp);
				}
			} catch (NoSuchMethodException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return list;
	}

	/**
	 * 合并mapList
	 * 
	 * @param mapList1
	 * @param mapList2
	 * @param key
	 *            依据的key
	 * @return
	 */
	public static final List<Map<String, Object>> merge(List<Map<String, Object>> mapList1,
			List<Map<String, Object>> mapList2, String key) {
		List<Map<String, Object>> list = new ArrayList<>();
		for (Map<String, Object> m : mapList1) {
			for (Map<String, Object> mm : mapList2) {
				if (m.get(key).equals(mm.get(key))) {
					m.putAll(mm);
					break;
				}
			}
			list.add(m);
		}
		return list;
	}
	
	/**
	 * 合并对应的属性值到map中
	 * @param mapList
	 * @param objects
	 * @param key
	 * @param targetKey
	 * @param targetDataFiled
	 */
	public static final void merge(List<Map<String, Object>> mapList,List<?> objects,String key,String targetKey,String targetDataFiled,String sourceKey){
		if(objects==null||objects==null) {
			return;
		}
		String k=Strings.ucfirst(targetKey);
		String kf=Strings.ucfirst(targetDataFiled);
		for(Map<String,Object> m:mapList) {
			if(m.get(key)==null) {
				continue;
			}
			for(Object o:objects) {
				Method method=null;
				try {
					method = o.getClass().getMethod("get"+k);
					Object temp=method.invoke(o);
					if(temp!=null&&temp.equals(m.get(key))) {
						method=o.getClass().getMethod("get"+kf);
						m.put(sourceKey, method.invoke(o));
						break;
					}
				} catch (NoSuchMethodException | SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	public static final Object getValue(String key,Object v,String TargetKey,List<?> list) {
		if(v==null) {
			return null;
		}
		if(list==null||list.size()<1) {
			return null;
		}
		String k=Strings.ucfirst(TargetKey);
		String kk=Strings.ucfirst(key);
		for(Object o:list) {
			Method method=null;
			try {
				method = o.getClass().getMethod("get"+kk);
				Object temp=method.invoke(o);
				if(temp.equals(v)) {
					method=o.getClass().getMethod("get"+ k);
					return method.invoke(o);
				}
			} catch (NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static final Object getMapValue(String key,Object v,String TargetKey,List<Map<String,Object>> list) {
		if(v==null) {
			return null;
		}
		if(list==null||list.size()<1) {
			return null;
		}
		for(Map<String,Object> o:list) {
			if(v.equals(o.get(key))) {
				return o.get(TargetKey);
			}
		}
		return null;
	}
		
}
