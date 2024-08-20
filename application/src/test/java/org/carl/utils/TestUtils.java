package org.carl.utils;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestUtils {
    @Test
    public void testBeanToString() {
        Son son = new Son();
        son.name = "carl";
        son.age = 12;
        // System.out.println(beanToString(son));
        Tree tree = new Tree();
        tree.val = "root";
        tree.left = new Tree();
        tree.right = new Tree();
        tree.left.val = "left";
        tree.right.val = "right";
        System.out.println(beanToString(tree));
    }

    static class Father {
        String name;
    }

    static class Son extends Father {
        Integer age;
    }

    static class Tree {
        Tree right;
        Tree left;
        String val;

        @Override
        public String toString() {
            return "Tree{" +
                "right=" + right +
                ", left=" + left +
                ", val='" + val + '\'' +
                '}';
        }
    }

    public String beanToString(Object bean) {
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
        while (clazz != null && clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();

            for (Field field : fields) {

                field.setAccessible(true);
                Object value = null;
                try {
                    value = field.get(bean);
                } catch (IllegalAccessException e) {
                    //Irreversible error so Ignore
                }
                if (value == null) {
                    continue;
                }
                if (value instanceof Integer || value instanceof Float
                    || value instanceof Boolean) {
                    sb.append("\"").append(field.getName()).append("\"").append(": ").append(value)
                        .append(",");
                } else if (value instanceof String) {
                    sb.append("\"").append(field.getName()).append("\"").append(": ").append("\"")
                        .append(value).append("\"").append(",");
                } else {
                    sb.append("\"").append(field.getName()).append("\"").append(": ")
                        .append(beanToString(value)).append(",");
                }
            }

            clazz = clazz.getSuperclass();
        }
        sb.delete(sb.length() - 2, sb.length());
        sb.append("}");
        return sb.toString();
    }
}
