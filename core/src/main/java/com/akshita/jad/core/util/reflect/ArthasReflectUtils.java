package com.akshita.jad.core.util.reflect;

import com.akshita.jad.core.util.JADCheckUtils;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *  Created by vlinux on 15/5/18.
 */
public class JADReflectUtils {

    /**
     * packageClass
     *
     * @param packname 
     * @return 
     * <p>
     *  http://www.oschina.net/code/snippet_129830_8767</p>
     */
    public static Set<Class<?>> getClasses(final ClassLoader loader, final String packname) {

        // class
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        // 
        //  
        String packageName = packname;
        String packageDirName = packageName.replace('.', '/');
        //  things
        Enumeration<URL> dirs;
        try {
//            dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
            dirs = loader.getResources(packageDirName);
            // 
            while (dirs.hasMoreElements()) {
                // 
                URL url = dirs.nextElement();
                // 
                String protocol = url.getProtocol();
                // 
                if ("file".equals(protocol)) {
//					System.err.println("file");
                    // 
                    String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                    //  
                    findAndAddClassesInPackageByFile(packageName, filePath,
                            true, classes);
                } else if ("jar".equals(protocol)) {
                    // jar
                    // JarFile
//					System.err.println("jar");
                    JarFile jar;
                    try {
                        // jar
                        jar = ((JarURLConnection) url.openConnection())
                                .getJarFile();
                        // jar 
                        Enumeration<JarEntry> entries = jar.entries();
                        // 
                        while (entries.hasMoreElements()) {
                            // jar  jar META-INF
                            JarEntry entry = entries.nextElement();
                            String name = entry.getName();
                            // /
                            if (name.charAt(0) == '/') {
                                // 
                                name = name.substring(1);
                            }
                            // 
                            if (name.startsWith(packageDirName)) {
                                int idx = name.lastIndexOf('/');
                                // "/" 
                                if (idx != -1) {
                                    //  "/""."
                                    packageName = name.substring(0, idx)
                                            .replace('/', '.');
                                }
                                // .class 
                                if (name.endsWith(".class") && !entry.isDirectory()) {
                                    // ".class" 
                                    String className = name.substring(
                                            packageName.length() + 1,
                                            name.length() - 6);
                                    try {
                                        // classes
                                        classes.add(Class
                                                .forName(packageName + '.'
                                                        + className));
                                    } catch (ClassNotFoundException e) {
                                        // log
                                        // .error(" .class");
//											e.printStackTrace();
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        // log.error("jar");
//						e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
//			e.printStackTrace();
        }

        return classes;
    }

    /**
     * Class
     * <p/>
     * <p>
     *  http://www.oschina.net/code/snippet_129830_8767</p>
     */
    private static void findAndAddClassesInPackageByFile(String packageName,
                                                         String packagePath, final boolean recursive, Set<Class<?>> classes) {
        //  File
        File dir = new File(packagePath);
        //  
        if (!dir.exists() || !dir.isDirectory()) {
            // log.warn(" " + packageName + " ");
            return;
        }
        //   
        File[] dirfiles = dir.listFiles(new FileFilter() {
            //  () .class(java)
            @Override
            public boolean accept(File file) {
                return (recursive && file.isDirectory())
                        || (file.getName().endsWith(".class"));
            }
        });
        if (dirfiles != null) {
            // 
            for (File file : dirfiles) {
                //  
                if (file.isDirectory()) {
                    findAndAddClassesInPackageByFile(
                            packageName + "." + file.getName(),
                            file.getAbsolutePath(), recursive, classes);
                } else {
                    // java .class 
                    String className = file.getName().substring(0,
                            file.getName().length() - 6);
                    try {
                        // 
                        // classes.add(Class.forName(packageName + '.' +
                        // className));
                        // ，forName，static，classLoaderload
                        classes.add(Thread.currentThread().getContextClassLoader()
                                .loadClass(packageName + '.' + className));
                    } catch (ClassNotFoundException e) {
                        // log.error(" .class");
                        //                    e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 
     *
     * @param field  
     * @param value  
     * @param target 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException   
     */
    public static void set(Field field, Object value, Object target) throws IllegalArgumentException, IllegalAccessException {
        final boolean isAccessible = field.isAccessible();
        try {
            field.setAccessible(true);
            field.set(target, value);
        } finally {
            field.setAccessible(isAccessible);
        }
    }

    /**
     * (、)
     *
     * @param clazz 
     * @return 
     */
    public static Set<Field> getFields(Class<?> clazz) {
        final Set<Field> fields = new LinkedHashSet<Field>();
        final Class<?> parentClazz = clazz.getSuperclass();
        Collections.addAll(fields, clazz.getDeclaredFields());
        if (null != parentClazz) {
            fields.addAll(getFields(parentClazz));
        }
        return fields;
    }

    /**
     * 
     *
     * @param clazz 
     * @param name  
     * @return 
     */
    public static Field getField(Class<?> clazz, String name) {
        for (Field field : getFields(clazz)) {
            if (JADCheckUtils.isEquals(field.getName(), name)) {
                return field;
            }
        }//for
        return null;
    }

    /**
     * 
     *
     * @param <T>
     * @param target 
     * @param field  
     * @return 
     * @throws IllegalArgumentException 
     * @throws IllegalAccessException   
     */
    public static <T> T getFieldValueByField(Object target, Field field) throws IllegalArgumentException, IllegalAccessException {
        final boolean isAccessible = field.isAccessible();
        try {
            field.setAccessible(true);
            //noinspection unchecked
            return (T) field.get(target);
        } finally {
            field.setAccessible(isAccessible);
        }
    }

    /**
     * ，9：8（）
     *
     * @param t     
     * @param value 
     * @return 
     */
    @SuppressWarnings("unchecked")
    public static <T> T valueOf(Class<T> t, String value) {
        if (JADCheckUtils.isIn(t, int.class, Integer.class)) {
            return (T) Integer.valueOf(value);
        } else if (JADCheckUtils.isIn(t, long.class, Long.class)) {
            return (T) Long.valueOf(value);
        } else if (JADCheckUtils.isIn(t, double.class, Double.class)) {
            return (T) Double.valueOf(value);
        } else if (JADCheckUtils.isIn(t, float.class, Float.class)) {
            return (T) Float.valueOf(value);
        } else if (JADCheckUtils.isIn(t, char.class, Character.class)) {
            return (T) Character.valueOf(value.charAt(0));
        } else if (JADCheckUtils.isIn(t, byte.class, Byte.class)) {
            return (T) Byte.valueOf(value);
        } else if (JADCheckUtils.isIn(t, boolean.class, Boolean.class)) {
            return (T) Boolean.valueOf(value);
        } else if (JADCheckUtils.isIn(t, short.class, Short.class)) {
            return (T) Short.valueOf(value);
        } else if (JADCheckUtils.isIn(t, String.class)) {
            return (T) value;
        } else {
            return null;
        }
    }


    /**
     * 
     *
     * @param targetClassLoader classloader
     * @param className         
     * @param classByteArray    
     * @return 
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Class<?> defineClass(
            final ClassLoader targetClassLoader,
            final String className,
            final byte[] classByteArray) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        final Method defineClassMethod = ClassLoader.class.getDeclaredMethod(
                "defineClass",
                String.class,
                byte[].class,
                int.class,
                int.class
        );

        synchronized (defineClassMethod) {
            final boolean acc = defineClassMethod.isAccessible();
            try {
                defineClassMethod.setAccessible(true);
                return (Class<?>) defineClassMethod.invoke(
                        targetClassLoader,
                        className,
                        classByteArray,
                        0,
                        classByteArray.length
                );
            } finally {
                defineClassMethod.setAccessible(acc);
            }
        }


    }

}
