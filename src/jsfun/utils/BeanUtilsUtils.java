package jsfun.utils;

import org.apache.commons.beanutils.BeanUtils;


public class BeanUtilsUtils {

	public static Object getProperty(Object bean, String name) {
		try {
			return BeanUtils.getProperty(bean, name);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void setProperty(Object bean, String name, Object value) {
		try {
			BeanUtils.setProperty(bean, name, value);
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
