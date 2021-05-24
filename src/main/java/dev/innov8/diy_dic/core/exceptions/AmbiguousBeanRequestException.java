package dev.innov8.diy_dic.core.exceptions;

import java.util.List;

public class AmbiguousBeanRequestException extends RuntimeException {

    public AmbiguousBeanRequestException(String requestCriteria, String beanDetail, List<Class<?>> beans) {
        super(String.format("No unique bean with %s [%s] is defined: expected single matching bean but found %s: [%s]", requestCriteria, beanDetail, beans.size(), beans));
    }
}
