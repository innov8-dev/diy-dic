package dev.innov8.demo;

import dev.innov8.diy_dic.core.BeanFactory;
import dev.innov8.diy_dic.core.SimpleBeanFactory;

public class Driver {

    public static void main(String[] args) {
        BeanFactory beanFactory = new SimpleBeanFactory("dev.innov8.demo");
        DemoModel demoModel = beanFactory.getBean("demoModel", DemoModel.class);
        DemoModel demoModel2 = beanFactory.getBean("demoModel", DemoModel.class);

        // shows that objects are created
        System.out.println(demoModel);

        // false, because DemoModel scoped as a prototype
        System.out.println(demoModel == demoModel2);

        // shows that dependencies are successfully wired in
        DemoController demoController = beanFactory.getBean("demoController", DemoController.class);
        demoController.test();

        // both are true, because DemoController is scoped as a singleton
        DemoController demoController2 = beanFactory.getBean("demoController", DemoController.class);
        System.out.println(demoController == demoController2);
        System.out.println(demoController.getDemoService() == demoController2.getDemoService());

        // false, because DemoService is scoped as a prototype
        DemoService demoService = beanFactory.getBean(DemoService.class);
        System.out.println(demoService == demoController.getDemoService());

        // true, because DemoRepository is scoped as a singleton
        DemoRepository demoRepository = beanFactory.getBean(DemoRepository.class);
        System.out.println(demoController.getDemoService().getDemoRepository() == demoRepository);

    }

}
