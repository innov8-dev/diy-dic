package dev.innov8.diy_dic.core.exceptions;

public class UnknownBeanTypeException extends RuntimeException {

    public UnknownBeanTypeException() {
        super("No such bean found using the provided information");
    }

}
