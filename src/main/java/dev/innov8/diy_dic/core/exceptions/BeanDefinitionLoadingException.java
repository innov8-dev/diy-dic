package dev.innov8.diy_dic.core.exceptions;

public class BeanDefinitionLoadingException extends RuntimeException {

    public BeanDefinitionLoadingException(String msg) {
        super(msg);
    }

    public BeanDefinitionLoadingException(Throwable t) {
        super("There was an exception thrown while attempting to load bean definitions.", t);
    }
}
