import java.io.BufferedReader;

/**
 * 模板方法模式的应用：
 * 假设有茶和咖啡两个类，它们在冲泡的过程中，有部分步骤的方法实现是重复的，现在需要进行优化抽取出公共的代码。
 *  */

public class Coffee1 {
    void prepareRecipe() {
        boilWater();
        brewCoffeeGrinds();
        pourInCup();
        addSugarAndMilk();
    }
    void boilWater() {System.out.println("Boiling water");}
    void brewCoffeeGrinds() {System.out.println("Dripping Coffee through filter");}
    void pourInCup() {System.out.println("Pouring into cup");}
    void addSugarAndMilk() {System.out.println("Adding Sugar and Milk");}
}

public class Tea1 {
    void prepareRecipe() {
        boilWater();
        steepTeaBag();
        pourInCup();
        addLemon();
    }
    void boilWater() {System.out.println("Boiling water");}
    void steepTeaBag() {System.out.println("Steeping the tea");}
    void pourInCup() {System.out.println("Pouring into cup");}
    void addLemon() {System.out.println("Adding Lemon");}
}

//创建一个咖啡因饮料的基类，将咖啡和茶的公共方法抽取到此基类中，它们各自实现自己特有的步骤方法
public abstract class CaffeineBeverage2 {
    void prepareRecipe() {
        boilWater();
        pourInCup();
    }
    public void boilWater() {System.out.println("Boiling water");}
    public void pourInCup() {System.out.println("Pouring into cup");}
}

public class Coffee2 extends CaffeineBeverage2 {
    void prepareRecipe() {
        boilWater();
        brewCoffeeGrinds();
        pourInCup();
        addSugarAndMilk();
    }
    void brewCoffeeGrinds() {System.out.println("Dripping Coffee through filter");}
    void addSugarAndMilk() {System.out.println("Adding Sugar and Milk");}
}

public class Tea2 extends CaffeineBeverage2 {
    void prepareRecipe() {
        boilWater();
        steepTeaBag();
        pourInCup();
        addLemon();
    }
    void steepTeaBag() {System.out.println("Steeping the tea");}
    void addLemon() {System.out.println("Adding Lemon");}
}

//此时，会发现prepareRecipe并没有完全抽取到基类，因为子类的prepareRecipe实现里面是包含子类特有的步骤方法，基类中的prepareRecipe其实并没什么实质的意义，只是包含上基类知道的公共步骤方法，具有真正意义的只能靠子类重写的prepareRecipe。
//这时，问题又来了，子类重写的prepareRecipe方法里还是存在重复的代码，就是boilWater和pourInCup这两个步骤，而且如果prepareRecipe其中的实现变动（例如步骤顺序等改变）那要改的地方就很多了（每一个子类里），需要再进一步抽象prepareRecipe。

//抽象特性的步骤方法
public abstract class CaffeineBeverage3 {
    void prepareRecipe() {
        boilWater();
        brew();
        pourInCup();
        addCondiments();
    }
    public void boilWater() {System.out.println("Boiling water");}
    public void pourInCup() {System.out.println("Pouring into cup");}

    abstract void brew();
    abstract void addCondiments();
}

public class Coffee3 extends CaffeineBeverage3 {
    public void brew() {System.out.println("Dripping Coffee through filter");}
    public void addCondiments() {System.out.println("Adding Sugar and Milk");}
}

public class Tea3 extends CaffeineBeverage3 {
    public void brew() {System.out.println("Steeping the tea");}
    public void addCondiments() {System.out.println("Adding Lemon");}
}

//抽象特性的步骤方法后，prepareRecipe可以完全抽取到基类中实现了，而基类也不用关注两个抽象步骤方法brew和addCondiments的具体实现。

//这里咖啡和茶经过两次的方法抽取后，算法原本在这两个子类中控制的，改由基类咖啡因饮料主导，从而可以保护和约束这个算法的框架，即是基类更专注于算法本身，子类提供完整的实现。

//这个例子中是在继承的关系上演示的，实际情况可能会有变种，例如算法本身并不在基类上，而是在另外一个独立类上实现，完整的实现通过接口（iOS上对应的是协议）去获取。

//最后，关于这个例子中子类实现的完整步骤方法，有时候子类可能想要动态控制是否执行这个步骤，所以设计者在模板算法中对于可选执行的步骤，应该提供钩子hook给子类去自行决定是否执行（基类或算法所在的类上可以实现默认或空的钩子方法，等子类选择重写），如下例
public abstract class CaffeineBeverageWithHook {
    void prepareRecipe() {
        boilWater();
        brew();
        pourInCup();
        if (customerWantsCondiments()) {
            addCondiments();
        }
    }
    abstract void brew();
    abstract void addCondiments();

    public void boilWater() {System.out.println("Boiling water");}
    public void pourInCup() {System.out.println("Pouring into cup");}

    boolean customerWantsCondiments() {
        return true;
    }
}

public class CoffeeWithHook extends CaffeineBeverageWithHook {
    public void brew() {System.out.println("Dripping Coffee through filter");}
    public void addCondiments() {System.out.println("Adding Sugar and Milk");}

    public boolean customerWantsCondiments() {
        String answer = getUserInput();
        if (answer.toLowerCase().startsWith("y")) {
            return true;
        } else {
            return false;
        }
    }

    private String getUserInput() {
        String answer = null;
        System.out.print("Would you like milk and sugar with your coffee (y/n)?");
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        try {
            answer = in.readLine();
        } catch (IOException ioe) {
            System.err.println("IO error trying to read your answer");
        }
        if (answer == null) {
            return "no";
        }
        return answer;
    }
}

//一般，都是底层组件（具体实现者）挂钩到高层组件（抽象执行者）上，

//另外需要注意一点，算法里的步骤切割得太细就会让子类实现的步骤方法麻烦复杂，但太粗太少就会比较没有弹性，这个需要折衷考虑。
//总结一下，模板方法模式其实是定义了一个算法的大纲，定义了算法的步骤，并由它的子类或接口实现者定义其中的某些步骤的内容，它也是一种代码复用的技巧，当中的抽象类可以定义具体方法、抽象方法和钩子，为防止子类改变模板方法，可以将模板方法声明为final。
//对比一下，模板方法模式和策略模式有点相似，但是策略模式通过组合委托对象的形式是可以动态更换算法中的实现步骤，不过算法的封装因为散落在子类而没有绝对的控制权，会较容易出现重复代码。而工厂方法则是模板方法的一种特殊版本。