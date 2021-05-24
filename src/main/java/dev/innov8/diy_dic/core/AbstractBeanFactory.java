package dev.innov8.diy_dic.core;

import dev.innov8.diy_dic.core.BeanDefinition;
import dev.innov8.diy_dic.core.BeanFactory;
import dev.innov8.diy_dic.core.SimpleBeanDefinition;
import dev.innov8.diy_dic.core.annotations.Component;
import dev.innov8.diy_dic.core.exceptions.BeanCreationException;
import dev.innov8.diy_dic.core.exceptions.BeanDefinitionLoadingException;
import dev.innov8.diy_dic.core.util.Reflector;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractBeanFactory implements BeanFactory {

    protected final Map<String, BeanDefinition> beanDefinitionMap = new Hashtable<>();
    protected final Map<String, Object> createdBeans = new Hashtable<>();

    protected void loadBeanDefinitions(String basePackage) {
        try {

            Reflector.getClassesInPackageWithConstraints(basePackage, aClass -> aClass.isAnnotationPresent(Component.class))
                    .stream()
                    .map(SimpleBeanDefinition::new)
                    .forEach(beanDef -> beanDefinitionMap.put(beanDef.getBeanName(), beanDef));

        } catch (MalformedURLException | ClassNotFoundException e) {
            throw new BeanDefinitionLoadingException(e);
        }
    }

    protected void instantiateSingletons() {
        beanDefinitionMap.values()
                .stream()
                .filter(BeanDefinition::isSingleton)
                .forEach(beanDef -> createdBeans.put(beanDef.getBeanName(), this.getBean(beanDef.getBeanName(), beanDef.getBeanType())));
    }

    @SuppressWarnings("unchecked")
    protected <T> T createInstance(BeanDefinition beanDefinition, Class<?>[] dependencyBeanTypes, Object[] dependencyBeans) {
        String beanName = beanDefinition.getBeanName();
        try {
            Constructor<?> beanConstructor = beanDefinition.getBeanType().getConstructor(dependencyBeanTypes);
            T beanInstance = (T) beanConstructor.newInstance(dependencyBeans);
            createdBeans.put(beanName, beanInstance);
            return beanInstance;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new BeanCreationException(beanName, e);
        }
    }

    protected Class<?>[] getDependencyBeanTypes(BeanDefinition[] dependencyBeanDefinitions) {
        return Arrays.stream(dependencyBeanDefinitions)
                .map(BeanDefinition::getBeanType)
                .toArray(Class<?>[]::new);
    }

    protected BeanDefinition[] getDependencyBeanDefinitions(BeanDefinition dependentBeanDefinition) {
        return Arrays.stream(dependentBeanDefinition.getDependencies())
                     .map(beanDefinitionMap::get)
                     .toArray(BeanDefinition[]::new);
    }

    protected Object[] getDependencyBeanInstances(BeanDefinition[] dependencyBeanDefinitions) {
        return Arrays.stream(dependencyBeanDefinitions)
                     .map(depBeanDef -> {
                         String depBeanName = depBeanDef.getBeanName();
                         if (createdBeans.containsKey(depBeanName)) {
                             return createdBeans.get(depBeanDef.getBeanName());
                         }
                         Object depBean = getBean(depBeanName, depBeanDef.getBeanType());
                         createdBeans.put(depBeanName, depBean);
                         return depBean;
                     })
                     .toArray();
    }


}
