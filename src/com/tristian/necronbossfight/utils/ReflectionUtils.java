package com.tristian.necronbossfight.utils;


import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReflectionUtils {
    private static Map<String, Field> cachedFields;
    private static Map<String, Method> cachedMethods;
    private static String nmsVersion;
    private static Class<?> entityLivingClass;
    private static Class<?> genericAttributesClass;
    private static Class<?> iattributeInstanceClass;
    private static Class<?> attributeInstanceClass;


    public static Field getField(final Class<?> clazz, final String field) {
        try {
            final Field cached = ReflectionUtils.cachedFields.get(clazz.getName() + ":" + field);
            if (cached != null) {
                return cached;
            }
            final Field f = clazz.getDeclaredField(field);
            f.setAccessible(true);
            ReflectionUtils.cachedFields.put(clazz.getName() + ":" + field, f);
            return f;
        } catch (Throwable $ex) {
            return null;
        }
    }

    public static Method getMethod(final Class<?> clazz, final String field) {
        try {
            final Method cached = ReflectionUtils.cachedMethods.get(clazz.getName() + ":" + field);
            if (cached != null) {
                return cached;
            }
            final Method f = clazz.getDeclaredMethod(field, new Class[0]);
            f.setAccessible(true);
            ReflectionUtils.cachedMethods.put(clazz.getName() + ":" + field, f);
            return f;
        } catch (Throwable $ex) {
            return null;
        }
    }

    public static Object invokeMethod(final Method method, final Object instance, final Object... args) {
        try {
            return method.invoke(instance, args);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object invokeMethod(final Method method, final Object instance) {
        try {
            return method.invoke(instance);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Method getMethod(final Class<?> clazz, final String field, final Class<?>... paramaters) {
        try {
            final Method cached = ReflectionUtils.cachedMethods.get(clazz.getName() + ":" + field);
            if (cached != null) {
                return cached;
            }
            final Method f = clazz.getDeclaredMethod(field, paramaters);
            f.setAccessible(true);
            ReflectionUtils.cachedMethods.put(clazz.getName() + ":" + field, f);
            return f;
        } catch (Throwable $ex) {
            return null;
        }

    }

    public static void setObject(final Object object, final String fieldName, final Object value) {
        try {
            final Field field = getField(object.getClass(), fieldName);
            setObject(object, field, value);
        } catch (Throwable $ex) {
            return;
        }
    }

    public static void setObject(final Object object, final Field field, final Object value) {
        try {
            if (field != null) {
                field.set(object, value);
            }
        } catch (Throwable $ex) {
            return;
        }
    }


    public static Object callReflectionMethod(final Object instance, final Class<?> clazz, final String method) {
        return callReflectionMethod(instance, clazz, method, null);
    }

    public static Object callReflectionMethod(final Object instance, final String method) {
        if (instance == null) {
            throw new NullPointerException("instance");
        }
        return callReflectionMethod(instance, instance.getClass(), method, null);
    }

    public static Object callReflectionMethod(final Object instance, final String method, final ParamBuilder builder) {
        if (instance == null) {
            throw new NullPointerException("instance");
        }
        return callReflectionMethod(instance, instance.getClass(), method, builder);
    }

    public static Object callReflectionMethod(final Object instance, final Class<?> clazz, final String method, final ParamBuilder builder) {
        try {
            if (builder != null) {
                final Class<?>[] params = (Class<?>[]) new Class[builder.getParameters().size()];
                final Object[] objcs = new Object[builder.getValidObjectCount()];
                int index = 0;
                for (final RParam param : builder.getParameters()) {
                    final int i = index++;
                    params[i] = param.getMethodClass();
                    objcs[i] = param.getInstance();
                }
                final Method m = getMethod(clazz, method, params);
                return m.invoke(instance, objcs);
            }
            final Method j = getMethod(clazz, method);
            return j.invoke(instance);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getNMSVersion() {
        return ReflectionUtils.nmsVersion;
    }

    public static Class<?> getNMSClass(final String className) {
        final String fullName = "net.minecraft.server." + getNMSVersion() + className;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Class<?> getOBCClass(final String className) {
        final String fullName = "org.bukkit.craftbukkit." + getNMSVersion() + className;
        Class<?> clazz = null;
        try {
            clazz = Class.forName(fullName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clazz;
    }

    public static Object getNMSHandle(final Object obj) {
        try {
            return getMethod(obj.getClass(), "getHandle", new Class[0]).invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getEntityLivingClass() {
        return ReflectionUtils.entityLivingClass;
    }

    public static Class<?> getGenericAttributesClass() {
        return ReflectionUtils.genericAttributesClass;
    }

    public static Class<?> getIattributeInstanceClass() {
        return ReflectionUtils.iattributeInstanceClass;
    }

    public static Class<?> getAttributeInstanceClass() {
        return ReflectionUtils.attributeInstanceClass;
    }

    static {
        ReflectionUtils.cachedFields = new HashMap<String, Field>();
        ReflectionUtils.cachedMethods = new HashMap<String, Method>();
        final String name = Bukkit.getServer().getClass().getPackage().getName();
        ReflectionUtils.nmsVersion = name.substring(name.lastIndexOf(46) + 1) + ".";
        ReflectionUtils.entityLivingClass = getNMSClass("EntityLiving");
        ReflectionUtils.attributeInstanceClass = getNMSClass("AttributeInstance");
        ReflectionUtils.genericAttributesClass = getNMSClass("GenericAttributes");
        ReflectionUtils.iattributeInstanceClass = getNMSClass("IAttribute");

    }


    public static void setNull(final Object object, final String fieldName) {
        try {
            final Field f = object.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);
            System.out.println("Value Before: " + f.get(object));
            f.set(object, null);
            System.out.println("Value: " + f.get(object));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setObject(final Class<?> clazz, final Object instance, final Object objectToSet, final String fieldName) {
        try {
            final Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(instance, objectToSet);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object getObject(final Class<?> clazz, final Object instance, final String fieldName) {
        try {
            final Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(instance);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object getObject(final Class<?> clazz, final String fieldName) {
        try {
            final Field f = clazz.getDeclaredField(fieldName);
            f.setAccessible(true);
            return f.get(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static class ParamBuilder {
        private List<RParam> parameters;
        private int validObjectCount;

        public ParamBuilder() {
            this.parameters = new LinkedList<>();
            this.validObjectCount = 0;
        }

        public ParamBuilder(final Class<?> clazz, final Object obj) {
            this.parameters = new LinkedList<>();
            this.validObjectCount = 0;
            this.add(clazz, obj);
        }

        public ParamBuilder add(final RParam param) {
            this.parameters.add(param);
            if (param != null) {
                ++this.validObjectCount;
            }
            return this;
        }

        public ParamBuilder add(final Class<?> clazz, final Object param) {
            this.add(new RParam(clazz, param));
            return this;
        }

        public List<RParam> getParameters() {
            return this.parameters;
        }

        public int getValidObjectCount() {
            return this.validObjectCount;
        }

        public void setValidObjectCount(final int validObjectCount) {
            this.validObjectCount = validObjectCount;
        }
    }

    public static class RParam {
        private Class<?> methodClass;
        private Object instance;

        public RParam(final Class<?> clazz) {
            this.methodClass = clazz;
        }

        public RParam(final Class<?> methodClass, final Object instance) {
            this.methodClass = methodClass;
            this.instance = instance;
        }

        public Class<?> getMethodClass() {
            return this.methodClass;
        }

        public Object getInstance() {
            return this.instance;
        }
    }


}


