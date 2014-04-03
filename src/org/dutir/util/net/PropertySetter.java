/**
 * 
 */
package org.dutir.util.net;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;

/**
 * Try to call the object's setter method.
 * 
 * @author shutrazh
 * 
 */
public class PropertySetter {
	private Object object;

	private Class<?> clazz;

	public PropertySetter(Object object) {
		this.object = object;
		this.clazz = object.getClass();
	}

	public void setProperty(String property, Object value) {
		if (value == null) {
			return;
		}
		boolean setted = false;
		try {
			setProperty(property, value.toString());
			setted = true;
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		} catch (NoSuchMethodException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		}

		if (!setted) {
			try {
				setProperty(property, Integer.parseInt(value.toString()));
				setted = true;
			} catch (NumberFormatException e) {
			} catch (SecurityException e) {
			} catch (IllegalArgumentException e) {
			} catch (NoSuchMethodException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
		}

		if (!setted) {
			try {
				setProperty(property, Long.parseLong(value.toString()));
			} catch (NumberFormatException e) {
			} catch (SecurityException e) {
			} catch (IllegalArgumentException e) {
			} catch (NoSuchMethodException e) {
			} catch (IllegalAccessException e) {
			} catch (InvocationTargetException e) {
			}
		}
	}

	private void setProperty(String property, String value)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Method m = clazz.getMethod("set" + StringUtils.capitalize(property),
				String.class);
		m.invoke(object, value);
	}

	private void setProperty(String property, int value)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Method m = clazz.getMethod("set" + StringUtils.capitalize(property),
				int.class);
		m.invoke(object, value);
	}

	private void setProperty(String property, long value)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		Method m = clazz.getMethod("set" + StringUtils.capitalize(property),
				long.class);
		m.invoke(object, value);
	}
}
