package dev.innov8.demo;

import dev.innov8.diy_dic.core.annotations.Autowired;
import dev.innov8.diy_dic.core.annotations.Component;

@Component
public class DemoController {

    private DemoService demoService;

    @Autowired
    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    public void test() {
        System.out.println("In DemoController#test");
        demoService.test();
    }

    public DemoService getDemoService() {
        return demoService;
    }

}
