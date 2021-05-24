package dev.innov8.diy_dic.core.util;

import dev.innov8.diy_dic.core.BeanDefinition;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Reflector {

    public static List<String> getClassNamesInPackage(String packageName) {

        List<String> classNames = new ArrayList<>();
        File packageDirectory = new File("target/classes/" + packageName.replace('.', '/'));

        for (File file : Objects.requireNonNull(packageDirectory.listFiles())) {
            if (file.isDirectory()) {
                classNames.addAll(getClassNamesInPackage(packageName + "." + file.getName()));
            } else if (file.getName().contains(".class")) {
                classNames.add(file.getName());
            }
        }

        return classNames;
    }

    public static List<Class<?>> getClassesInPackage(String packageName) throws MalformedURLException {

        final List<Class<?>> packageClasses = new ArrayList<>();
        List<String> classNames = getClassNamesInPackage(packageName);

        URLClassLoader ucl = new URLClassLoader(new URL[] { new File("target/classes/").toURI().toURL() });
        classNames.forEach(className -> {
            try {
                packageClasses.add(ucl.loadClass(packageName + "." + className.substring(0, className.length() - 6)));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });

        return packageClasses;

    }

    public static List<Class<?>> getClassesInPackageWithConstraints(String packageName, Predicate<Class<?>> predicate) throws MalformedURLException, ClassNotFoundException {
        List<Class<?>> packageClasses = getClassesInPackage(packageName);
        return packageClasses.stream().filter(predicate).collect(Collectors.toList());
    }

}
