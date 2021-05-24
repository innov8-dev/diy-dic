package dev.innov8.diy_dic.core.exceptions;

public class BeanTypeMismatchException extends RuntimeException {
    public BeanTypeMismatchException(String beanName, String providedTypeName, String actualTypeName) {
        super("Cannot provide bean with name," + beanName + " , due to type mismatch. Provided bean type: " +
                providedTypeName + ", actual bean type: " + actualTypeName);
    }
}
