package dev.innov8.demo;

import dev.innov8.diy_dic.core.annotations.Autowired;
import dev.innov8.diy_dic.core.annotations.Component;

@Component
public class DemoService {

    private DemoRepository demoRepository;

    @Autowired
    public DemoService(DemoRepository demoRepository) {
        this.demoRepository = demoRepository;
    }

    public void test() {
        System.out.println("In DemoService#test");
        demoRepository.test();
    }

}
