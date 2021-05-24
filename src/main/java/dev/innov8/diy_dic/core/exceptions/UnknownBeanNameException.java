package dev.innov8.diy_dic.core.exceptions;

public class UnknownBeanNameException extends RuntimeException {

    public UnknownBeanNameException(String beanName) {
        super("There was no bean found with the name: " + beanName);
    }
}
