package dev.innov8.diy_dic.core.exceptions;

public class BeanCreationException extends RuntimeException {

    public BeanCreationException(String beanName) {
        super("A problem occurred while trying to create bean with name: " + beanName);
    }

    public BeanCreationException(String beanName, Throwable t) {
        super("A problem occurred while trying to create bean with name: " + beanName, t);
    }
}
