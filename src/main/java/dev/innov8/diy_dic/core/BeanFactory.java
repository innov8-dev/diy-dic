package dev.innov8.diy_dic.core;

import dev.innov8.diy_dic.core.exceptions.BeanTypeMismatchException;
import dev.innov8.diy_dic.core.exceptions.UnknownBeanNameException;

/**
 * Serves as a base contract for interfacing with a dependency injection container.
 *
 * Attribution: this code was heavily inspired from the creators of the Spring Framework
 * which this codebase is an oma
 */
public interface BeanFactory {

    /**
     * Used to determine if this factory is aware of a bean with the provided name
     *
     * @param beanName the name of the bean under inquiry
     * @return true if the factory has a registered bean with the provided name; otherwise false
     */
    boolean containsBean(String beanName);

    /**
     * Used to retrieve a reference of the bean whose type matches the provided value.
     *
     * @param requiredBeanType a Class reference of the type of the required bean
     * @param <T> the type of the desired bean
     * @return an instance (shared or individual based on scope) of the requested bean type
     * @throws dev.innov8.diy_dic.core.exceptions.UnknownBeanTypeException if there is no bean of the indicated type
     */
    <T> T getBean(Class<T> requiredBeanType);

    /**
     * Used to retrieve a reference of the bean whose name and type matches the provided values.
     *
     * @param beanName the name of the required bean
     * @param requiredBeanType a Class reference of the type of the required bean
     * @param <T> the type of the desired bean
     * @return an instance (shared or individual based on scope) of the requested bean type
     * @throws UnknownBeanNameException if there is no bean found with the provided name
     * @throws BeanTypeMismatchException if the provided type is not congruent with bean's actual type
     */
    <T> T getBean(String beanName, Class<T> requiredBeanType) throws UnknownBeanNameException, BeanTypeMismatchException;

    /**
     * Used to retrieve a Class reference of the type for the bean whose name matches the provided value.
     *
     * @param beanName the name of the bean under inquiry
     * @return the
     * @throws UnknownBeanNameException if there is no bean found with the provided name
     */
    Class<?> getBeanClass(String beanName) throws UnknownBeanNameException;

    /**
     * Used to retrieve the bean scope of the bean whose name matches the provided value;
     * @param beanName the name of the bean under inquiry
     * @return a String value representing the bean scope of the bean under inquiry
     * @throws UnknownBeanNameException if there is no bean found with the provided name
     */
    String getBeanScope(String beanName) throws UnknownBeanNameException;

    /**
     * Used to determine if the bean with the given name is scoped as a prototype (new instance provided on request).
     *
     * @param beanName the name of the bean under inquiry
     * @return true if the bean with the matching name is scoped as a prototype
     */
    boolean isPrototype(String beanName);

    /**
     * Used to determine if the bean with the given name is scoped as a prototype (shared reference provided on
     * request).
     *
     * @param beanName the name of the bean under inquiry
     * @return true if the bean with the matching name is scoped as a singleton
     */
    boolean isSingleton(String beanName);

}
