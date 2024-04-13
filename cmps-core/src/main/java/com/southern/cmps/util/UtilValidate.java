
package com.southern.cmps.util;

import java.util.Collection;
import java.util.Map;

public class UtilValidate {

	@SuppressWarnings("unchecked")
	public static boolean isEmpty(Object value) {
		
		if (value == null)
			return true;
		else{
			if (value instanceof String)
				return isEmpty((String)value);
			if (value instanceof Collection)
				return isEmpty((Collection<? extends Object>) value);
			if (value instanceof Map)
				return isEmpty((Map<? extends Object, ? extends Object>) value);
			if (value instanceof CharSequence)
				return isEmpty((CharSequence) value);
			if (value instanceof Boolean || value instanceof Number || value instanceof Character || value instanceof java.sql.Timestamp)
				return false;
		}
		return false;
	}
	
	
	public static boolean isEmpty(String s) {
		return ((s == null) || (s.trim().length() == 0) || ("null".equalsIgnoreCase(s)));
	}
	
	public static <E> boolean isEmpty(Collection<E> c) {
		return (c == null || c.isEmpty());
	}
	
	public static <K, E> boolean isEmpty(Map<K, E> m) {
		return ((m == null) || (m.size() == 0));
	}

	public static boolean isEmpty(CharSequence c) {
		return ((c == null) || (c.length() == 0));
	}
}
