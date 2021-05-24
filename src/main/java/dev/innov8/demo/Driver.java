package dev.innov8.demo;

import dev.innov8.diy_dic.core.BeanFactory;
import dev.innov8.diy_dic.core.SimpleBeanFactory;

public class Driver {

    public static void main(String[] args) {
        BeanFactory beanFactory = new SimpleBeanFactory("dev.innov8.demo");
        DemoModel demoModel = beanFactory.getBean("demoModel", DemoModel.class);
        DemoModel demoModel2 = beanFactory.getBean("demoModel", DemoModel.class);

        System.out.println(demoModel);
        System.out.println(demoModel2);
        System.out.println(demoModel == demoModel2);

        DemoController demoController = beanFactory.getBean("demoController", DemoController.class);
        demoController.test();

        DemoController demoController2 = beanFactory.getBean("demoController", DemoController.class);
        System.out.println(demoController == demoController2);

    }

}
