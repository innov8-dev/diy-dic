package dev.innov8.demo;

import dev.innov8.diy_dic.core.BeanScope;
import dev.innov8.diy_dic.core.annotations.Autowired;
import dev.innov8.diy_dic.core.annotations.Component;
import dev.innov8.diy_dic.core.annotations.Scope;

@Component
@Scope(BeanScope.PROTOTYPE)
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

    public DemoRepository getDemoRepository() {
        return demoRepository;
    }
}
