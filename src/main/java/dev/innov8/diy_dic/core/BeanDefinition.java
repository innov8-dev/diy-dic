package dev.innov8.diy_dic.core;

public interface BeanDefinition {

    /**
     * Used to obtain this bean's name.
     *
     * @return the name of this bean
     */
    String getBeanName();

    /**
     * Used to obtain a Class reference of this bean's type.
     *
     * @return the type of this bean
     */
    Class<?> getBeanType();

    /**
     * Used to obtain a String representation of the simple name of this bean's type.
     *
     * @return the simple name of the bean's type
     */
    String getBeanTypeName();

    /**
     * Used to determine whether or not this bean can be instantiated.
     *
     * @return true if this bean is an abstract class, interface, or annotation type; otherwise false
     */
    boolean isAbstractType();

    /**
     * Used to get an enumerated value representing the scope of this bean.
     *
     * @return an enum value representing the scope of this bean
     */
    BeanScope getBeanScope();

    /**
     * Used to determine whether or not requests for this bean should result in a single shared instance
     * being returned.
     *
     * @return true if this bean is a singleton; otherwise false
     */
    boolean isSingleton();

    /**
     * Used to determine whether or not requests for this bean should result in a new instance
     * being returned.
     *
     * @return true if this bean is a prototype; otherwise false
     */
    boolean isPrototype();

    /**
     * Used to indicate the other beans within the current context that this bean is dependent upon.
     *
     * @param dependencyBeanNames the names of the beans that this bean depends upon
     */
    void setDependencies(String... dependencyBeanNames);

    /**
     * Used to retrieve an array of the names of this bean's dependencies
     *
     * @return an array of String representing the names of this bean's dependencies
     */
    String[] getDependencies();

}
