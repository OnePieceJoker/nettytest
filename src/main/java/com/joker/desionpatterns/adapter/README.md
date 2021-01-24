# 适配器模式(Adapter Pattern)

> Convert the interface of a class into another interface clients expect. Adapter lets classes work together that couldn't otherwise bacause of incompatible interface.
 (将一个类的接口变换成客户端所期待的另一种接口，从而使原本因接口不匹配而无法一起工作的两个类能够一起工作。)

## 通用代码模板

```java
// Target目标角色
public interface Target {
    public void request();
}

// Target目标角色的实现类
public class ConcreteTarget implements Target {
    public void request() {
        // do something
    }
}

// 源角色
public class Adaptee {
    public void doSomething() {
        // adaptee do something
    }
}

// 适配器
public class Adapter extends Adaptee implements Target {
    public void request() {
        super.doSomething();
    }
}

public class Client {
    public static void main(String[] args) {
        // 原有的业务逻辑
        Target target = new ConcreteTarget();
        target.request();
        // 适配后的新逻辑
        Target target2 = new Adapter();
        target2.request();
    }
}
```

## 优点

- 可以让两个不相关的类在一起运行

- 增加了类的透明性

- 提高了类的复用度

- 灵活性非常好

## 扩展：类适配器 && 对象适配器

类适配器：上述通用代码模板就是类适配器的实现模式，通过继承来进行适配

对象适配器：类关联的方式来进行适配，通过构造函数来进行适配
