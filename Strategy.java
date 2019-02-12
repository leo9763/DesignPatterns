/**
 * 策略模式的应用：
 * 已知有父类鸭子和它的一些子类，如青头鸭、橡皮鸭等，起初实现需求时，他们都还没有飞翔功能，现在想对能飞翔的子类添加飞方法实现。
 */

//利用继承实现
public abstract class Duck1 {
    public void quack() {System.out.println("quack");}

    public void swim() {System.out.println("swim");}

    public abstract void display();

    public void fly() {System.out.println("fly");};
}

public class MallardDuck1 extends Duck1 { //青头鸭
    public void display() {System.out.println("MallardDuck display");}
}

public class RedheadDuck1 extends Duck1 { //红头鸭
    public void display() {System.out.println("RedheadDuck display");}
}

public class RubberDuck1 extends Duck1 { //橡皮鸭
    public void display() {System.out.println("RubberDuck display");}

    @Override
    public void quack() {System.out.println("squeak");}
}

public class DecoyDuck1 extends Duck1 { //诱利鸭
    public void display() {System.out.println("RubberDuck display");}

    @Override
    public void quack() { }
}

//从上面的情况看，要添加一个fly方法，不适合直接在父类Duck中通过继承的方式实现而让子类获得此方法，因为并非所有子类鸭子都会飞。若为使大部分能飞的鸭子子类可以复用fly方法而硬要在父类中添加，则一些不会飞鸭子子类需要重写fly方法改为什么都不做。同时发现，已经实现了的quack方法也是与fly一样的情况。

//利用接口改进
public interface FlyBehavior {
    public void fly();
}

public interface QuackBehavior {
    public void quack();
}

public abstract class Duck2 {
    public void swim() {System.out.println("swim");}

    public abstract void display();
}

public class MallardDuck2 extends Duck2 implements FlyBehavior, QuackBehavior  { //青头鸭
    public void display() {System.out.println("MallardDuck display");}

    public void fly() {System.out.println("MallardDuck fly");};
    public void quack() {System.out.println("MallardDuck quack");};
}

public class RedheadDuck2 extends Duck2 implements FlyBehavior, QuackBehavior { //红头鸭
    public void display() {System.out.println("RedheadDuck display");}

    public void fly() {System.out.println("RedheadDuck fly");};
    public void quack() {System.out.println("RedheadDuck quack");};
}

public class RubberDuck2 extends Duck2 implements QuackBehavior { //橡皮鸭
    public void display() {System.out.println("RubberDuck display");}

    public void quack() {System.out.println("RubberDuck quack");};
}

public class DecoyDuck2 extends Duck2 { //诱利鸭
    public void display() {System.out.println("DecoyDuck display");}
}

//这样修改后，会发现重复的代码很多，像fly和quack接口方法，若子类很多且大部分都实现了fly或quack接口的时候（即使有部分的实现不一样），在需要调整fly或quack的实现时要改动到的地方就会变得很多了。

//利用组合的形式改进
public interface FlyBehavior {
    public void fly();
}

public class FlyWithWings implements FlyBehavior {
    public void fly() {System.out.println("Fly with wings");}
}

public class FlyNoWay implements FlyBehavior {
    public void fly() {System.out.println("Can't fly");}
}

public interface QuackBehavior {
    public void quack();
}

public class Qucak implements QuackBehavior {
    public void quack() {System.out.println("Quack");}
}

public class Squeak implements QuackBehavior {
    public void quack() {System.out.println("Squeak");}
}

public class MuteQuack implements QuackBehavior {
    public void quack() {System.out.println("Silence");}
}

public abstract class Duck3 {

    FlyBehavior flyBehavior;
    QuackBehavior quackBehavior;

    void performFly() {
        flyBehavior.fly();
    }

    void performQuack() {
        quackBehavior.quack();
    }

    public void swim() {System.out.println("swim");}

    public abstract void display();
}

public class MallardDuck3 extends Duck3 { //青头鸭
    public MallardDuck3() {
        quackBehavior = new Qucak();
        flyBehavior = new FlyWithWings();
    }

    public void display() {System.out.println("MallardDuck display");}
}

public class RedheadDuck3 extends Duck { //红头鸭
    public RedheadDuck3() {
        quackBehavior = new Qucak();
        flyBehavior = new FlyWithWings();
    }

    public void display() {System.out.println("RedheadDuck display");}
}

public class RubberDuck3 extends Duck { //橡皮鸭
    public RubberDuck3() {
        quackBehavior = new Squeak();
        flyBehavior = new FlyNoWay();
    }

    public void display() {System.out.println("RubberDuck display");}
}

public class DecoyDuck3 extends Duck { //诱利鸭
    public DecoyDuck3() {
        quackBehavior = new MuteQuack();
        flyBehavior = new FlyNoWay();
    }

    public void display() {System.out.println("DecoyDuck display");}
}
//现在，飞翔和啼叫的动作委托了两个抽象的实例变量去处理。而且在Duck中添加两个实例变量的set方法，更是可以在运行时动态地设定这两种行为。

//总结出3个设计原则: 找出变化之处独立出来，封装变化；针对接口编程而不要针对实现编程（即是对方法进行抽离抽象成接口后使用，若在父类或子类中直接实现后使用就会因太过于通用而失去了灵活性）；多组合，少继承。

// 策略模式，是一套算法族，分别封装起来，让它们之间可以互相替换，此模式让算法的变化独立于使用算法的客户。