package dev.innov8.diy_dic.core;

public enum BeanScope {

    SINGLETON("singleton"),
    PROTOTYPE("prototype");

    String name;

    BeanScope(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
