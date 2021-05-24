package dev.innov8.diy_dic.core;

import dev.innov8.diy_dic.core.annotations.Component;
import dev.innov8.diy_dic.core.exceptions.*;
import dev.innov8.diy_dic.core.util.Reflector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.stream.Collectors;

public class SimpleBeanFactory implements BeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new Hashtable<>();
    private final Map<String, Object> createdBeans = new Hashtable<>();

    public SimpleBeanFactory(String basePackage) {
        loadBeanDefinitions(basePackage);
        instantiateSingletons();
    }

    @Override
    public boolean containsBean(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public <T> T getBean(Class<T> requiredBeanType) {
        List<Class<?>> matchingBeanTypes = beanDefinitionMap.values()
                                                            .stream()
                                                            .map(BeanDefinition::getBeanType)
                                                            .filter(beanType -> beanType.equals(requiredBeanType))
                                                            .collect(Collectors.toList());

        if (matchingBeanTypes.size() == 0) {
            throw new UnknownBeanTypeException();
        } else if (matchingBeanTypes.size() != 1) {
            throw new AmbiguousBeanRequestException("type", requiredBeanType.getName(), matchingBeanTypes);
        }

        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(String beanName, Class<T> requiredBeanType) throws UnknownBeanNameException, BeanTypeMismatchException {
        BeanDefinition beanDef = beanDefinitionMap.get(beanName);

        if (beanDef == null) {
            throw new UnknownBeanNameException(beanName);
        }

        if (!beanDef.getBeanType().equals(requiredBeanType)) {
            throw new BeanTypeMismatchException(beanName, requiredBeanType.getName(), beanDef.getBeanType().getName());
        }

        if (createdBeans.containsKey(beanName)) {
            return (T) createdBeans.get(beanName);
        }

        Class<?>[] dependencyBeanTypes = Arrays.stream(beanDef.getDependencies())
                                               .map(beanDefinitionMap::get)
                                               .map(BeanDefinition::getBeanType)
                                               .toArray(Class<?>[]::new);

        Object[] dependencyBeans = Arrays.stream(beanDef.getDependencies())
                                         .map(beanDefinitionMap::get)
                                         .map(dependencyBeanDef -> {
                                             if (createdBeans.containsKey(dependencyBeanDef.getBeanName())) {
                                                 return createdBeans.get(dependencyBeanDef.getBeanName());
                                             } else {
                                                 System.out.println(dependencyBeanDef);
                                                 createdBeans.put(dependencyBeanDef.getBeanName(), getBean(dependencyBeanDef.getBeanName(), dependencyBeanDef.getBeanType()));
                                                 return createdBeans.get(dependencyBeanDef.getBeanName());                                             }
                                         })
                                         .toArray();

        try {
            Constructor<?> beanConstructor = beanDef.getBeanType().getConstructor(dependencyBeanTypes);
            T beanInstance = (T) beanConstructor.newInstance(dependencyBeans);
            createdBeans.put(beanName, beanInstance);
            return beanInstance;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BeanCreationException(beanName, e);
        }

    }

    @Override
    public Class<?> getBeanClass(String beanName) throws UnknownBeanNameException {
        return beanDefinitionMap.get(beanName).getBeanType();
    }

    @Override
    public String getBeanScope(String beanName) throws UnknownBeanNameException {
        return beanDefinitionMap.get(beanName).getBeanScope().toString();
    }

    @Override
    public boolean isPrototype(String beanName) {
        return beanDefinitionMap.get(beanName).isPrototype();
    }

    @Override
    public boolean isSingleton(String beanName) {
        return beanDefinitionMap.get(beanName).isSingleton();
    }

    private void loadBeanDefinitions(String basePackage) {
        try {

            Reflector.getClassesInPackageWithConstraints(basePackage, aClass -> aClass.isAnnotationPresent(Component.class))
                     .stream()
                     .map(SimpleBeanDefinition::new)
                     .forEach(beanDef -> beanDefinitionMap.put(beanDef.getBeanName(), beanDef));

        } catch (MalformedURLException | ClassNotFoundException e) {
            throw new BeanDefinitionLoadingException(e);
        }
    }

    private void instantiateSingletons() {
        beanDefinitionMap.values()
                         .stream()
                         .filter(BeanDefinition::isSingleton)
                         .forEach(beanDef -> createdBeans.put(beanDef.getBeanName(), this.getBean(beanDef.getBeanName(), beanDef.getBeanType())));
    }

}
