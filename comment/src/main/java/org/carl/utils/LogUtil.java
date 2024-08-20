package org.carl.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LogUtil {
    public static String beanToString(Object[] bean) {
        StringBuilder sb = new StringBuilder();
        for (Object o : bean) {
            sb.append(beanToString(o));
        }
        return sb.toString();
    }

    public static String beanToString(Object bean) {
        if (bean instanceof String) {
            return bean.toString();
        }

        try {
            Method aMethod = bean.getClass().getDeclaredMethod("toString");
            return (String) aMethod.invoke(bean);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            //Irreversible error so Ignore
        }

        StringBuilder sb = new StringBuilder();
        Class<?> clazz = bean.getClass();
        sb.append("{");
        try {
            while (clazz != null && clazz != Object.class) {
                Field[] fields = clazz.getDeclaredFields();

                for (Field field : fields) {
                    try {
                        field.setAccessible(true);
                    } catch (InaccessibleObjectException e) {
                        System.out.println(e.getMessage());
                        continue;
                    }

                    Object value = null;
                    try {
                        value = field.get(bean);
                    } catch (IllegalAccessException e) {
                        //Irreversible error so Ignore
                        continue;
                    }
                    if (value == null) {
                        continue;
                    }
                    if (value instanceof Integer || value instanceof Float
                        || value instanceof Boolean) {
                        sb.append("\"").append(field.getName()).append("\"").append(": ")
                            .append(value)
                            .append(",");
                    } else if (value instanceof String) {
                        sb.append("\"").append(field.getName()).append("\"").append(": ")
                            .append("\"")
                            .append(value).append("\"").append(",");
                    } else if (value instanceof Object[]) {
                        //FIX: module base is
                        sb.append("\"").append(field.getName()).append("\"").append(": ")
                            .append(beanToString((Object[]) value)).append(",");
                    } else {
                        sb.append("\"").append(field.getName()).append("\"").append(": ")
                            .append(value).append(",");
                        // sb.append("\"").append(field.getName()).append("\"").append(": ")
                        //     .append(beanToString(value)).append(",");
                    }
                }

                clazz = clazz.getSuperclass();
            }
        } catch (Exception e) {
            sb.append(" ");
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("}");
        return sb.toString();
    }
}
