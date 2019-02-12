/**
 * 工厂模式的应用：
 * 创建不同类型的披萨对象
 */

//方案一，直接new出具体类型的对象

public class PizzaStore1 {
    Pizza orderPizza(String type) {
        Pizza pizza;

        if (type.equals("cheese")) {
            pizza = new CheesePizza();
        } else if (type.equals("clam")) {
            pizza = new ClamPizza();
        }
        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();
        return pizza;
    }
}

//方案二，将具体(直接耦合、实现)的、会变化的创建对象部分抽离，包装进一个类，这常被称为“静态工厂”（或叫“简单工厂”）。它的缺点是不能通过继承来改变创建方法的行为。
public class PizzaStore2 {
    SimplePizzaFactory factory;

    public PizzaStore(SimplePizzaFactory factory) {
        this.factory = factory;
    }

    Pizza orderPizza(String type) {
        Pizza pizza = factory.createPizza(type);    

        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();
        return pizza;
    }
}

public class SimplePizzaFactory {
    public Pizza createPizza(String type) {
        Pizza pizza = null;
        if (type.equals("cheese")) {
            pizza = new CheesePizza();
        } else if (type.equals("clam")) {
            pizza = new ClamPizza();
        }
        return pizza;
    }
}

//通过方案二封装出来的创建披萨简单工厂，将具体的实现解耦出来只依赖接口了，其它分店也可以复用了，修改要创建的类型时也更封闭。

//假若PizzaStore开始搞加盟店，不同的加盟店会根据当地的口味调整提供Pizza的种类，在简单工厂中再根据多一个地区标识去判断就会越来越复杂，耦合越来越严重。

//方案三，使用“工厂方法”，来监控创建的披萨质量（当然还是可以继承方案二中的简单工厂，提供不同的子类工厂给PizzaStore来创建，这也是工厂方法，但PizzaStore就无法过多地干预创建的过程）
public abstract class PizzaStore3 {
    public Pizza orderPizza(String type) {
        Pizza pizza;
        pizza = createPizza(type);
        pizza.prepare();
        pizza.bake();
        pizza.cut();
        pizza.box();
        return pizza;
    }
    abstract Pizza createPizza(String type);
}

public class NYPizzaStore3 extends PizzaStore3 {
    Pizza createPizza(String type) {
        if (type.equals("cheese")) {
            return new NYStyleCheesePizza();
        } else if (type.equals("clam")) {
            return new NYStyleClamPizza();
        }
    }
}

public class ChicagoPizzaStore3 extends PizzaStore3 {
    Pizza createPizza(String type) {
        if (type.equals("cheese")) {
            return new ChicagoStyleCheesePizza();
        } else if (type.equals("clam")) {
            return new ChicagoStyleClamPizza();
        }
    }
}

//工厂方法(模式)通过让子类决定该创建的对象是什么，达到对象创建的过程封装的目的，即是定义出一个创建对象的接口，但由子类决定要实例化的类是哪一个，让类把实例化推迟到子类。

//这里还涉及一个涉及原则“反依赖倒置原则”，可以按下面三点方针去遵循：1）变量不可以持有具体类的引用；2）不要让类派生自具体类；3）不要覆盖基类中已实现的方法。

//上面的方案二和方案三中的工厂或者工厂方法中，都还是依赖了具体类(即 NY or Chicago 这两个地区种类的披萨)，并不符合“反依赖倒置原则”，若未来除cheese和clam外还多了3种披萨，那这两个地区的披萨总和就从4个增至10个

//方案四，优化工厂中的依赖关系
public interface PizzaIngredientFactory {
    public Dough createDough();
    public Sauce createSauce();
}

public class NYPizzaIngredientFactory implements PizzaIngredientFactory {
    public Dough createDough() {
        return new ThinCrustDough();
    }
    public Sauce createSauce() {
        return new MarinaraSauce();
    }
}

public class ChicagoPizzaIngredientFactory implements PizzaIngredientFactory {
    public Dough createDough() {
        return new ThickCrustDough();
    }
    public Sauce createSauce() {
        return new PlumTomatoSauce();
    }
}

public abstract class Pizza {
    String name;
    Dough dough;
    Sauce sauce;

    abstract void prepare();

    //... 其它方法
}

public class CheesePizza extends Pizza {
    PizzaIngredientFactory ingredientFactory;

    public CheesePizza(PizzaIngredientFactory ingredientFactory) {
        this.ingredientFactory = ingredientFactory;
    }

    void prepare() {
        //抽象工厂在此起关键作用了
        dough = ingredientFactory.createDough();
        sauce = ingredientFactory.createSauce();
    }
    //... 其它方法
}

public class ClamPizza extends Pizza {
    PizzaIngredientFactory ingredientFactory;

    public ClamPizza(PizzaIngredientFactory ingredientFactory) {
        this.ingredientFactory = ingredientFactory;
    }

    void prepare() {
        dough = ingredientFactory.createDough();
        sauce = ingredientFactory.createSauce();
    }
    //... 其它方法
}

public class NYPizzaStore4 extends PizzaStore3 {
    protected Pizza createPizza(String type) {
        Pizza pizza = null;
        PizzaIngredientFactory ingredientFactory = new NYPizzaIngredientFactory();

        if (type.equals("cheese")) {
            //（间接）应用抽象工厂（传给产品来使用）
            pizza = new CheesePizza(ingredientFactory);
        } else if (type.equals("clam")) {
            pizza = new ClamPizza(ingredientFactory);
        }
        return pizza;
    }
}

public class ChicagoPizzaStore4 extends PizzaStore3 {
    protected Pizza createPizza(String type) {
        Pizza pizza = null;
        PizzaIngredientFactory ingredientFactory = new ChicagoPizzaIngredientFactory();

        if (type.equals("cheese")) {
            pizza = new CheesePizza(ingredientFactory);
        } else if (type.equals("clam")) {
            pizza = new ClamPizza(ingredientFactory);
        }
        return pizza;
    }
}

//这个就是“抽象工厂”，通过采取对象的组合的方式实现（工厂与产品组合，产品使用不同的工厂创建配件族），现在即使披萨的基本种类增至5个，也不至于要另外为2个不同地区同时新增6(2x3)种地区性披萨，也就是说类的增量只有基本种类的3个新披萨。

//同时可以看到，NYPizzaIngredientFactory和ChicagoPizzaIngredientFactory里其实都用到了“工厂方法模式”，只是方法不止一个，是包括了创建这个产品的家族的多个方法。

//抽象工厂模式提供一个接口，用于创建相关或者依赖对象的家族，而不需要明确指定具体类。