/**
 * 装饰者模式的应用：
 * 计算有n种调料可添加的饮品的价格
 */

//方案一，为每种调料都创建一个对应的布尔值，用于在计价时判断是否算上对应调料的价格。

//此方案就不实现出来了，因为太多的if-else。（若不使用该方案，也应该不至于对每个调料创建一个子类来解决if-else多的问题吧）

//方案二，将不同的调料定为不同的装饰者，将饮品定为被装饰者，根据需要可包裹多层的装饰(即添加多种调料)
public abstract class Beverage { //当然也可以是接口
    String description = "Unknown beverage";

    public String getDescription() {
        return description;
    }

    public abstract double cost();
}

public abstract class CondimentDecorator extends Beverage {
    public abstract String getDescription();
}

//基础饮品（第一层被装饰的）
public class Espresso extends Beverage {
    public Espresso() {
        description = "Espresso";
    }

    public double cost() {
        return 1.99;
    }
}

public class HouseBlend extends Beverage {
    public HouseBlend() {
        description = "HouseBlend";
    }

    public double cost() {
        return 0.89;
    }
}

//调料（/饮品，第n层被装饰的，也是装饰第n-1层的）
public class Mocha extends CondimentDecorator {
    Beverage beverage;

    public Mocha(Beverage beverage) {
        this.beverage = beverage;
    }

    public String getDescription() {
        return beverage.getDescription() + ", Mocha";
    }

    public double cost() {
        return 0.20 + beverage.cost();
    }
}

//装饰者可以在被装饰者的行为前面或/与后面加上自己的行为，甚至将被装饰者的行为整个取代掉，而达到特定的目的。
//可以使用无数个装饰者去包装一个组件，因为装饰者一般对组件的客户是透明的，除非客户依赖的组件是具体类型，当然简单的只包一层也是可以的（不一定要使用上面例子中的嵌套同类的实例对象在类中）。

//总结，装饰者模式动态地将责任附加到对象上，若要扩展功能，装饰者提供了比继承更有弹性的替代方案。这是一个符合对扩展开放，对修改关闭原则的模式。