/**
 * 迭代器模式的应用：
 * 假设有西餐和煎饼两种菜单，需要招待员打印出这两种菜单的所有项目。
 *  */

//方案1：暴露集合给外部进行遍历
public class MenuItem {
    String name;
    String description;
    boolean vegetarian;
    double price;
    public MenuItem(String name, String description, boolean vegetarian, double price) {
        this.name = name;
        this.description = description;
        this.vegetarian = vegetarian;
        this.price = price;
    }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getPrice() { return price; }
    public String isVegetarian() { return vegetarian; }
}

public class PancakeHouseMenu1 { //煎饼菜单
    ArrayList menuItems;
    public PancakeHouseMenu() {
        menuItems = new ArrayList();
        addItem("K&B's Pancake Breakfast","Pancakes with scrambled eggs, and toast",true,2.99);
        addItem("Regular Pancake Breakfast","Pancakes with fried eggs, sausage",false,2.99);
        addItem("Blueberry Pancakes","Pancakes made with fresh blueberries",true,3.49);
        addItem("Waffles","Walfles, with yoiur choice of blueberries or strawberries",true,3.59);
    }
    public void addItem(String name, String description, boolean vegetarian, double price) {
        MenuItem menuItem = new MenuItem(name, description, vegetarian, price);
        menuItems.add(menuItem);
    }
    public ArrayList getMenuItems() {
        return menuItems;
    }
}

public class DinerMenu1 { //西餐菜单
    static final int MAX_ITEMS = 6;
    int numberOfItems;
    MenuItem[] menuItems;
    public DinerMenu() {
        menuItems = new MenuItem[MAX_ITEMS];
        addItem("Vegetarian BLT","(Fakin') Bacon with lettuce & tomato on whole wheat",true,2.99);
        addItem("BLT","Bacon with lettuce & tomato on whole wheat",false,2.99);
        addItem("Soup of the day","Soup of the day, with a side of potato salad",false,3.29);
        addItem("Hotdog","A hot dog, with saurkraut, relish, onions, topped with cheese",false,3.05);
    }
    public void addItem(String name, String description, boolean vegetarian, double price) {
        MenuItem menuItem = new MenuItem(name, description, vegetarian, price);
        if (numberOfItems >= MAX_ITEMS) {
            System.err.println("Sorry, menu is full! Can't add item to menu");
        } else {
            menuItems[numberOfItems] = menuItem;
            numberOfItems += 1;
        }
    }
    public MenuItem[] getMenuItems() {
        return menuItems;
    }
}

public class Waitress1 { //招待员
    public void printMenu() {
        PancakeHouseMenu1 pancakeHouseMenu = new PancakeHouseMenu1();
        ArrayList breakfastItems = pancakeHouseMenu.getMenuItems();

        DinerMenu1 dinerMenu = new DinerMenu1();
        MenuItem[] lunchItems = dinerMenu.getMenuItems();

        //需要用两个for循环分别遍历两种不同类型的菜单项集合(菜单)
        for (int i = 0; i < breakfastItems.size(); i++) {
            MenuItem menuItem = (MenuItem)breakfastItems.get(i);
            System.out.print(menuItem.getName()+" ");
            System.out.println(menuItem.getPrice()+" ");
            System.out.println(menuItem.getDescription());
        }
        for (int j = 0; j < lunchItems.length; j++) {
            MenuItem menuItem = lunchItems[j];
            System.out.print(menuItem.getName()+" ");
            System.out.println(menuItem.getPrice()+" ");
            System.out.println(menuItem.getDescription());
        }
    }
}

//从上面的方案1看，招待员的打印菜单实现中带有两次循环，且捆绑于具体的集合类型

//方案2：利用迭代器改进。
//为了让招待员更方便地遍历菜单，需要定义一个迭代器接口，然后分别实现煎饼和西餐两种具体的迭代器。这样招待员使用迭代器遍历时就可以调用统一的接口而不用关心这两种菜单内的差异处理，循环代码也因此变成只有一个。
public interface Iterator {
    boolean hasNext();
    Object next();
}

public class DinerMenuIterator2 implements Iterator {
    MenuItem[] items;
    int position = 0;
    public DinerMenuIterator(MenuItem[] items) {
        this.items = items;
    }
    public Object next() {
        MenuItem menuItem = items[position];
        position += 1;
        return menuItem;
    }
    public boolean hasNext() {
        if (position >= items.length || items[position] == null) {
            return false;
        } else {
            return true;
        }
    }
}

// 同理，实现 PancakeHouseMenuIterator2。

public class DinerMenu2 {
    //...

    //去掉getMenuItems方法，因为不再以暴露出内部的实现给外部进行遍历，改为提供封装好遍历逻辑的迭代器给外部直接使用
    public Iterator createIterator() {
        return new DinerMenuIterator2(menuItems);
    }
}

// 同理，为 PancakeHouseMenu2 添加 createIterator 方法。

public class Waitress2 {
    PancakeHouseMenu2 pancakeHouseMenu;
    DinerMenu2 dinerMenu;
    public Waitress(PancakeHouseMenu2 pancakeHouseMenu, DinerMenu2 dinerMenu) {
        this.pancakeHouseMenu = pancakeHouseMenu;
        this.dinerMenu = dinerMenu;
    }
    public void printMenu() {
        Iterator pancakeIterator = pancakeHouseMenu.createIterator();
        Iterator dinerIterator = dinerMenu.createIterator();
        System.out.println("Menu\n---\nBreakfast");
        printMenu(pancakeIterator);
        System.out.println("\nLunch");
        printMenu(dinerIterator);
    }
    private void printMenu(Iterator iterator) {
        while (iterator.hasNext()) {
            MenuItem menuItem = (MenuItem)iterator.next();
            System.out.print(menuItem.getName()+", ");
            System.out.print(menuItem.getPrice()+" -- ");
            System.out.print(menuItem.getDescription());
        }
    }
}

//从上面看，方案2中，招待员仍然捆绑于两个具体的菜单类，我们应该做得更针对接口编程，解耦招待员和具体菜单的关系。

//方案3：利用java.util.Iterator。
//系统本身有迭代器相关接口和已实现的类可直接利用，同时本次抽象出菜单接口供招待员使用，解耦招待员和煎饼西餐两个具体菜单的关系。
public interface Menu {
    public Iterator createIterator();
}

public class PancakeHouseMenu3 implements Menu { //煎饼菜单
    ArrayList menuItems;
    public PancakeHouseMenu() {
        menuItems = new ArrayList();
        addItem("K&B's Pancake Breakfast","Pancakes with scrambled eggs, and toast",true,2.99);
        addItem("Regular Pancake Breakfast","Pancakes with fried eggs, sausage",false,2.99);
        addItem("Blueberry Pancakes","Pancakes made with fresh blueberries",true,3.49);
        addItem("Waffles","Walfles, with yoiur choice of blueberries or strawberries",true,3.59);
    }
    public void addItem(String name, String description, boolean vegetarian, double price) {
        MenuItem menuItem = new MenuItem(name, description, vegetarian, price);
        menuItems.add(menuItem);
    }
    public Iterator createIterator() {
        return menuItems.iterator(); //因ArrayList支持Iterator接口，故直接免去 PancakeHouseMenuIterator3 的实现。
    }
}

public class DinerMenu3 implements Menu {
    //...

    public Iterator createIterator() {
        return new DinerMenuIterator3(menuItems); //因数组list（MenuItems[]）不支持Iterator接口，故仍需实现 DinerMenuIterator3
    }
}

public class DinerMenuIterator3 implements Iterator {
    //...

    //还需实现 java.util.Iterator 的remove
    public void remove() {
        if (position <= 0) {
            throw new IllegalStateException("You can't remove an item until you've done at least one next()");
        }
        if (items[position-1] != null) {
            for (int i = position-1; i < (items.length-1); i++) {
                items[i] = items[i+1];
            }
            items[items.length-1] = null;
        }
    }
}

public class Waitress3 {
    Menu pancakeHouseMenu;
    Menu dinerMenu;
    public Waitress(Menu pancakeHouseMenu, Menu dinerMenu) {
        this.pancakeHouseMenu = pancakeHouseMenu;
        this.dinerMenu = dinerMenu;
    }
    
}

//添加咖啡菜单（演示Hashtable的Iterator）
public class CafeMenu implements Menu {
    Hashtable menuItems = new Hashtable();
    public CafeMenu() {}
    public void addItem(String name, String description, boolean vegetarian, double price) {
        MenuItem menuItem = new MenuItem(name, description, vegetarian, price);
        menuItems.put(menuItem.getName(), menuItem);
    }
    public Iterator createIterator() {
        return MenuItems.values().iterator();
    }
}

//再优化一下招待员，使他不用在每次增减菜单时都要改到内部的代码。这也是得益于Menu接口的诞生
public class Waitress4 {
    ArrayList menus;
    public Waitress(ArrayList menus) {
        this.menus = menus;
    }
    public void printMenu() {
        Iterator menuIterator = menus.iterator();
        while(menuIterator.hasNext()) {
            Menu menu = (Menu)menuIterator.next();
            printMenu(menu.createIterator());
        }
    }
    private void printMenu(Iterator iterator) {
        //...
    }
}

//总结，迭代器模式提供一种方法顺序访问一个聚合对象中的各个元素，而又不暴露其内部的表示。把游走的任务放在迭代器上，而不是聚合，简化了聚合的接口和实现，让责任各得其所。
//延伸，类的每个责任都有改变的潜在区域，超过一个责任，意味着超过一个改变的区域，应尽量让每个类保存单一责任，即一个类应该只有一个引起变化的原因。