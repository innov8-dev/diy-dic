package dev.innov8.diy_dic.core;

import dev.innov8.diy_dic.core.annotations.Autowired;
import dev.innov8.diy_dic.core.annotations.Component;
import dev.innov8.diy_dic.core.annotations.Scope;
import dev.innov8.diy_dic.core.exceptions.BeanDefinitionLoadingException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleBeanDefinition implements BeanDefinition {

    private String beanName;
    private final Class<?> beanTypeDefinition;
    private final String beanTypeName;
    private boolean isAbstract;
    private String[] dependencyBeanNames;
    private BeanScope beanScope = BeanScope.SINGLETON;

    public SimpleBeanDefinition(Class<?> beanTypeDefinition) {

        this.beanTypeDefinition = beanTypeDefinition;
        this.beanTypeName = beanTypeDefinition.getName();

        setBeanName();
        setBeanScope();
        setIsAbstract();
        extractDependencyNames();

    }

    @Override
    public String getBeanName() {
        return this.beanName;
    }

    @Override
    public Class<?> getBeanType() {
        return beanTypeDefinition;
    }

    @Override
    public String getBeanTypeName() {
        return beanTypeName;
    }

    @Override
    public boolean isAbstractType() {
        return isAbstract;
    }

    @Override
    public BeanScope getBeanScope() {
        return beanScope;
    }

    @Override
    public boolean isSingleton() {
        return beanScope == BeanScope.SINGLETON;
    }

    @Override
    public boolean isPrototype() {
        return beanScope == BeanScope.PROTOTYPE;
    }

    @Override
    public void setDependencies(String... dependencyBeanNames) {
        this.dependencyBeanNames = dependencyBeanNames;
    }

    @Override
    public String[] getDependencies() {
        return dependencyBeanNames;
    }

    private void setBeanName() {
        if (beanTypeDefinition.isAnnotationPresent(Component.class)) {
            String annoValue = beanTypeDefinition.getAnnotation(Component.class).beanName();
            if (annoValue.equals("")) {
                String simpleBeanName = beanTypeDefinition.getSimpleName();
                this.beanName = simpleBeanName.substring(0, 1).toLowerCase() + simpleBeanName.substring(1);
            } else {
                this.beanName = annoValue;
            }
        } else {
            throw new BeanDefinitionLoadingException("Could not load bean definition from non-Component type");
        }
    }

    private void setBeanScope() {
        if (beanTypeDefinition.isAnnotationPresent(Scope.class)) {
            beanScope = beanTypeDefinition.getAnnotation(Scope.class).value();
        }
    }

    private void setIsAbstract() {
        isAbstract = Modifier.isAbstract(beanTypeDefinition.getModifiers());
    }

    private void extractDependencyNames() {
        List<Constructor<?>> constructors = Arrays.stream(beanTypeDefinition.getConstructors())
                                                          .filter(beanConstructor -> beanConstructor.isAnnotationPresent(Autowired.class))
                                                          .collect(Collectors.toList());

        if (constructors.size() > 1) {
            throw new BeanDefinitionLoadingException("Found more than one constructor annotated with @Autowired, when only one was expected for bean with name: " + beanName);
        } else if (constructors.size() == 0) {
            this.dependencyBeanNames = new String[0];
            return;
        }

        Constructor<?> autowireConstructor = constructors.get(0);
        this.dependencyBeanNames = Arrays.stream(autowireConstructor.getParameterTypes())
                                          .filter(paramType -> paramType.isAnnotationPresent(Component.class))
                                          .map(componentType -> {
                                              String annoValue = componentType.getAnnotation(Component.class).beanName();
                                              if (annoValue.equals("")) {
                                                  String simpleBeanName = componentType.getSimpleName();
                                                  return simpleBeanName.substring(0, 1).toLowerCase() + simpleBeanName.substring(1);
                                              } else {
                                                  return annoValue;
                                              }
                                          })
                                          .toArray(String[]::new);
    }

    @Override
    public String toString() {
        return "SimpleBeanDefinition{" +
                "beanName='" + beanName + '\'' +
                ", beanTypeDefinition=" + beanTypeDefinition +
                ", beanTypeName='" + beanTypeName + '\'' +
                ", isAbstract=" + isAbstract +
                ", dependencyBeanNames=" + Arrays.toString(dependencyBeanNames) +
                ", beanScope=" + beanScope +
                '}';
    }
}
