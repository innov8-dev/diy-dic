package dev.innov8.demo;

import dev.innov8.diy_dic.core.BeanScope;
import dev.innov8.diy_dic.core.annotations.Component;
import dev.innov8.diy_dic.core.annotations.Scope;

@Component(beanName = "demoModel")
@Scope(BeanScope.PROTOTYPE)
public class DemoModel {

    private int id;
    private String testValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTestValue() {
        return testValue;
    }

    public void setTestValue(String testValue) {
        this.testValue = testValue;
    }

    @Override
    public String toString() {
        return "DemoModel{" +
                "id=" + id +
                ", testValue='" + testValue + '\'' +
                '}';
    }

}
