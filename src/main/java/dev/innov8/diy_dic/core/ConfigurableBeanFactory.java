package dev.innov8.diy_dic.core;

import dev.innov8.diy_dic.core.exceptions.UnknownBeanNameException;

/**
 * Serves as a base contract for interfacing with a dependency injection container.
 *
 * Attribution: this code was heavily inspired from the creators of the Spring Framework
 * which this codebase is an oma
 */
public interface ConfigurableBeanFactory {

    /**
     * Used to set the classloader which will be used by this factory to load bean classes into memory.
     * If no class loader is set, then the current thread context's classloader will be used.
     *
     * @param beanClassLoader the classloader to be used by this factory
     */
    void setBeanClassLoader(ClassLoader beanClassLoader);

    /**
     * Used to register a dependency to a bean, which indicates to the container that the dependent bean
     * should be destroyed prior to the dependency.
     *
     * @param dependentBeanName the bean for which a dependency is being registed
     * @param dependencyBeanName the bean which is a dependency
     */
    void registerDependentBean(String dependentBeanName, String dependencyBeanName);

    /**
     * Used to retrieve an array of the bean names for the bean whose name matches the provided value
     *
     * @param beanName
     * @return a String array of bean names that the provided bean name depends on
     * @throws UnknownBeanNameException if the provided bean name value is unknown to this factory
     */
    String[] getDependentBeanNames(String beanName) throws UnknownBeanNameException;

    /**
     * Destroy the given bean instance (usually a prototype instance obtained from this factory) according to its
     * bean definition. Any exception that arises during destruction should be caught and logged instead of propagated
     * to the caller of this method.
     *
     * @param beanName the name of the bean to be destroyed
     * @param beanInstance the bean instance to destroy
     */
    void destroyBean(String beanName, Object beanInstance);

}
