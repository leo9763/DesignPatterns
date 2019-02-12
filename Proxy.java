/**
 * 代理模式的应用：
 * 对糖果机的销售情况做监控
 */

 //首先解释一下，这里的代理和Objective-C中所说的代理（Delegate）是有区别的，在下面的延伸部分再做详细说明。

 //服务端的实现
import java.rmi.*;
import java.io.*;
import java.rmi.server.*;

public interface GumballMachineRemote extends Remote {
    public int getCount() throws RemoteException; //因为是远程操作，所以得声明异常
    public String getLocation() throws RemoteException;
    public State getState() throws RemoteException;
}

public interface State extends Serializable { //只有扩展了序列化接口才可以在网络上传送
    public void insertQuarter();
    public void ejectQuarter();
    public void turnCrank();
    public void dispense();
}

public class NoQuarterState implements State {
    transient GumballMachine3 gumballMachine; //为了不让GumballMachine加入到序列化，使用transient关键字修饰各个State实现类中的该实例变量

    //... 其它方法的实现
}

//让GumballMachine继承UnicastRemoteObject，使其成为远程服务
public class GumballMachine3 extends UnicastRemoteObject implements GumballMachineRemote {
    public GumballMachine(String location, int numberGumballs) throws RemoteException {
        //...
    }

    //... GumballMachineRemote远程接口的实现，rmi系统会自动针对这些接口生成可被远程访问的方法
}

//客户端的实现
public class GumballMonitor {
    GumballMachineRemote machine;

    public GumballMonitor(GumballMachineRemote machine) {
        this.machine = machine;
    }

    public void report() {
        try {
            System.out.println(machine.getLocation());
            System.out.println(machine.getCount());
            System.out.println(machine.getState());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

//测试程序
public class test1 {
    void test() throws Exception {
        try {
            GumballMachineRemote machine = (GumballMachineRemote) Naming.lookup("rmi://xx.xx.com/gumballmachine");
            GumballMonitor monitor = new GumballMonitor(machine);
            monitor.report();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//这里，GumballMachineRemote就是一个远程代理。

//总结，代理模式为另一个对象提供一个替身或占位符以访问这个对象，以便控制或管理访问。

//延伸，代理还有很多变体，例如虚拟代理（控制访问实例化开销大的对象）、保护代理（基于调用者控制对象方法的访问）、防火墙代理、缓存代理等。
//代理模式与装饰者模式的区别，装饰者模式只能装饰点缀，不会实例化任何对象，它是为对象加上行为，而代理则是通过创建代理对象控制访问。
//Objective-C中的代理是指实现一个协议（接口）的对象，通过委托这个对象去执行协议中的方法，而无需关系该对象是什么，但它并没有是谁的替身或占位符的意思，也不是用于控制访问。

//Java内置的代理支持可建立动态代理(反射)，将调用分配到所选的处理器，下面再提供一个用动态代理的实现保护代理例子

public interface PersonBean {
    void setName(String name);//想要控制别人不能设置，自己能设置
    void setHotOrNotRating(int rating);//想要控制自己不能设置，别人能设置
}

public class PersonBeanImpl implements PersonBean {
    String name;
    int rating;

    public void setName(String name) {
        this.name = name;
    }

    public void setHotOrNotRating(int rating) {
        this.rating += rating;
    }
}

//import java.lang.reflect.*
public class OwnerInvocationHandler implements InvocationHandler {
    PersonBean person;

    public OwnerInvocationHandler(PersonBean person) {
        this.person = person;
    }

    //动态代理的任何接口方法被调用时，会调用该invoke方法
    public Object invoke(Object proxy, Method method, Object[] args) throws IllegalAccessException {
        try {
            if (method.getName().startWith("get")) {
                return method.invoke(person,args);
            } else if (method.getName().equals("setHotOrNotRating")) {
                throw new IllegalAccessException();
            } else if (method.getName().startWith("set")) {
                return method.invoke(person,args);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

public class NoOwnerInvocationHandler implements InvocationHandler {
    PersonBean person;

    public OwnerInvocationHandler(PersonBean person) {
        this.person = person;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws IllegalAccessException {
        try {
            if (method.getName().startWith("get")) {
                return method.invoke(person,args);
            } else if (method.getName().equals("setHotOrNotRating")) {
                return method.invoke(person,args); //与OwnerInvocationHandler相反
            } else if (method.getName().startWith("set")) {
                throw new IllegalAccessException(); //与OwnerInvocationHandler相反
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

//这样就无需额外定义代理类，在使用时通过下面的Java内置代理支持方法获取动态的代理
public class test2 {
    PersonBean getOwnerProxy(PersonBean person) {
        return (PersonBean) Proxy.newProxyInstance(person.getClass().getClassLoader(),person.getClass().getInterfaces(),new OwnerInvocationHandler(person));
    }
    PersonBean getNoOwnerProxy(PersonBean person) {
        return (PersonBean) Proxy.newProxyInstance(person.getClass().getClassLoader(),person.getClass().getInterfaces(),new NoOwnerInvocationHandler(person));
    }
}


