/**
 * 组合模式的应用：
 * 基于上一节的迭代器模式中的菜单例子，假设要历遍菜单的菜单项中，可能还有子菜单，继续打印包括子菜单里的所有菜单项。
 *  */

//定义一个菜单项和子菜单的抽象类，可以作为个别对象和对象组合。
public abstract class MenuComponent1 {
    public void add(MenuComponent1 menuComponent) { throw new UnsupportedOperationException();}
    public void remove(MenuComponent1 menuComponent) { throw new UnsupportedOperationException();}
    public MenuComponent1 getChild(int i) { throw new UnsupportedOperationException();}
    public String getName() { throw new UnsupportedOperationException();}
    public String getDescription() { throw new UnsupportedOperationException();}
    public String getPrice() { throw new UnsupportedOperationException();}
    public String isVegetarian() { throw new UnsupportedOperationException();}
    public void print() { throw new UnsupportedOperationException();}
}

public class MenuItem1 extends MenuComponent1 {
    //...

    public void print() {
        System.out.print(" "+getName());
        if (isVegetarian()) {
            System.out.print("(v)");
        }
        System.out.print(", "+getPrice());
        System.out.print("  -- "+getDescription());
    }
}

public class Menu1 extends MenuComponent1 {
    ArrayList menuComponents = new ArrayList();
    String name;
    String description;

    public Menu(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public void add(MenuComponent1 menuComponent) { menuComponent.add(menuComponent);}
    public void remove(MenuComponent1 menuComponent) { menuComponent.remove(menuComponent);}
    public MenuComponent1 getChild(int i) { return (MenuComponent)menuComponent.get(i);}
    public String getName() { return name; }
    public String getDescription() { return description; }
    public void print() {
        System.out.print("\n "+getName());
        System.out.print(", "+getDescription()); 
        System.out.print("----------------"); 
        Iterator iterator = menuComponents.iterator();
        while(iterator.hasNext()) {
            MenuComponent1 menuComponent = (MenuComponent)iterator.next();
            menuComponent.print(); //如果元素是(子)菜单，则产生递归效果
        }
    }
}

public class Waitress {
    MenuComponent1 allMenus;
    public Waitress(MenuComponent1 allMenus) { this.allMenus = allMenus; }
    public void printMenu() { allMenus.print(); }
}

public class MenuTestDrive {
    public static void main(String args[]) {
        MenuComponent1 pancakeHouseMenu = new Menu1("Pancake House Menu","Breakfast");
        MenuComponent1 dinerMenu = new Menu1("Diner Menu","Lunch");
        MenuComponent1 cafeMenu = new Menu1("Cafe Menu","Dinner");
        MenuComponent1 dessertMenu = new Menu1("Dessert Menu","Dessert of course");

        MenuComponent1 allMenus = new Menu1("All Menus","All menus combined");
        allMenus.add(pancakeHouseMenu);
        allMenus.add(dinerMenu);
        allMenus.add(cafeMenu);

        dinerMenu.add(new MenuItem1("Pasta","Spaghetti with Marinara Sauce, and a slice of sourdough bread",true,3.89));
        dinerMenu.add(dessertMenu);

        dessertMenu.add(new MenuItem1("Apple Pie","Apple pie with a flakey crust, topped with vanilla ice cream",true,1.59));

        Waitress waitress = new Waitress((allMenus));
        waitress.printMenu();
    }
}

//上面使用了树状结构设计菜单组件MenuComponent，Menu类是组件的其中一个具体实现，并可以往上面添加菜单组件MenuComponent，实现嵌套存储。

//上面的实现中没有提供遍历的元素，如果要提供出来则需要做一套这个树的迭代器。
//假设现在要找出所有素食菜品，做一下改造

public abstract class MenuComponent2 {
    //...

    public Iterator createIterator() {};
}

public class Menu2 extends MenuComponent2 {
    //...

    public Iterator createIterator() {
        return new CompositeIterator(menuComponents.iterator());
    };
}


public class MenuItem2 extends MenuComponent2 {
    //...

    public Iterator createIterator() {
        return new NullIterator(); // 为了保持一致性和安全性，提供空迭代器给外部遍历时进行假调用（因为外部只认菜单组件的接口，而不会理会具体实现是否支持）
    };
}

public class NullIterator implements Iterator {
    public Object next() {
        return null;
    }
    public boolean hasNext() {
        return false;
    }
    public void remove() {
        throw new UnsupportedOperationException();
    }
}

//树(组合)迭代器
public class CompositeIterator implements Iterator {
    Stack stack = new Stack(); //用来存放整个组合里面的所有迭代器
    public CompositeIterator(Iterator iterator) {
        stack.push(iterator);
    }
    public Object next() {
        if (hasNext()) {
            Iterator iterator = (Iterator) stack.peek(); //使用栈顶的迭代器历遍，一个迭代器历遍完后hasNext内会自动更新栈顶的迭代器
            MenuComponent component = (MenuComponent) iterator.next();
            if (component instanceof Menu) {
                stack.push(component.createIterator()); //将子菜单的迭代器推入栈
            }
            return component;
        } else {
            return null;
        }
    }
    public boolean hasNext() {
        if (stack.empty()) {
            return false;
        } else {
            Iterator iterator = (Iterator) stack.peek();
            if (!iterator.hasNext()) {
                stack.pop();
                return hasNext(); //递归
            } else {
                return true;
            }
        }
    }
    public void remove() {
        throw new UnsupportedOperationException();
    }
}

public class Waitress2 {
    //...

    public void printVegetarianMenu() {
        Iterator iterator = allMenus.createIterator();
        while(iterator.hasNext()) {
            MenuComponent2 menuComponent2 = (MenuComponent2)iterator.next();
            try {
                if (menuComponent2.isVegetarian()) {
                    menuComponent2.print();
                }
            } catch (UnsupportedOperationException e) { }
        }
    }
}

 //组合模式允许将对象组合成树形结构来表现“整体/部分”层次结构。组合能让客户以一致的方式处理个别对象以及对象组合。使用组合结构，我们能把相同的操作应用在组合和个别对象上，即忽略对象组合和个别对象之间的差别。
 //组合模式以单一责任设计原则换取了透明性，但失去了相对的安全性，即是调用者可能会对一个元素做出不恰当或没意义的操作方法调用，这个是典型的折衷案例。
 //延伸：组件还可以有指向父节点的指针，方便反向游走；有顺序的子节点，增删节点方法需额外设计；缓存起需要历遍计算的结果，节省开支。
 //回顾：策略-封装可互换的行为，并使用委托决定使用哪一个；适配器-改变一个或多个类的接口；迭代器-提供一个方式来遍历集合，而无须暴露集合的实现；外观-简化一群类的接口；组合-客户可以将对象的集合以及个别的对象一视同仁；观察者-当某个状态改变时，允许一群对象能被通知到。