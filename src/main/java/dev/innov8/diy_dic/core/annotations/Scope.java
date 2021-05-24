package dev.innov8.diy_dic.core.annotations;

import dev.innov8.diy_dic.core.BeanScope;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
    BeanScope value() default BeanScope.SINGLETON;
}
