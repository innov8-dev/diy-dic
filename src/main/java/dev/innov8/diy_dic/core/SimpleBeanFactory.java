package dev.innov8.diy_dic.core;

import dev.innov8.diy_dic.core.exceptions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class SimpleBeanFactory extends AbstractBeanFactory {

    public SimpleBeanFactory(String basePackage) {
        loadBeanDefinitions(basePackage);
        instantiateSingletons();
    }

    @Override
    public boolean containsBean(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredBeanType) throws IllegalArgumentException, UnknownBeanNameException, AmbiguousBeanRequestException {

        if (requiredBeanType == null) {
            throw new IllegalArgumentException();
        }

        List<BeanDefinition> matchingBeanDefs = beanDefinitionMap.values()
                                                                  .stream()
                                                                  .filter(beanDef -> beanDef.getBeanType().equals(requiredBeanType))
                                                                  .collect(Collectors.toList());

        if (matchingBeanDefs.size() == 0) {
            throw new UnknownBeanTypeException();
        } else if (matchingBeanDefs.size() != 1) {
            List<Class<?>> matchingBeanTypes = matchingBeanDefs.stream()
                                                               .map(BeanDefinition::getBeanType)
                                                               .collect(Collectors.toList());

            throw new AmbiguousBeanRequestException("type", requiredBeanType.getName(), matchingBeanTypes);
        }

        BeanDefinition matchingBeanDef = matchingBeanDefs.get(0);
        return (T) getBean(matchingBeanDef.getBeanName(), matchingBeanDef.getBeanType());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getBean(String beanName, Class<T> requiredBeanType) throws IllegalArgumentException, UnknownBeanNameException, BeanTypeMismatchException {

        if (beanName == null || beanName.trim().equals("") || requiredBeanType == null) {
            throw new IllegalArgumentException();
        }

        BeanDefinition beanDef = beanDefinitionMap.get(beanName);

        if (beanDef == null) {
            throw new UnknownBeanNameException(beanName);
        }

        if (!beanDef.getBeanType().equals(requiredBeanType)) {
            throw new BeanTypeMismatchException(beanName, requiredBeanType.getName(), beanDef.getBeanType().getName());
        }

        if (createdBeans.containsKey(beanName) && beanDef.getBeanScope() == BeanScope.SINGLETON) {
            return (T) createdBeans.get(beanName);
        }

        BeanDefinition[] dependencyBeanDefs = getDependencyBeanDefinitions(beanDef);
        Class<?>[] dependencyBeanTypes = getDependencyBeanTypes(dependencyBeanDefs);
        Object[] dependencyBeans = getDependencyBeanInstances(dependencyBeanDefs);

        return this.createInstance(beanDef, dependencyBeanTypes, dependencyBeans);

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

}
