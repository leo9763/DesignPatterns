/**
 * 适配器模式的应用：
 * 鸭子中有绿头鸭、红头鸭等，现在还缺一点鸭子的品种，想用火鸡冒充鸭子。
 */

 public interface Duck {
     public void quack();
     public void fly();
 }

 //绿头鸭
 public class MallardDuck implements Duck {
     public void quack() {
         System.out.println("Quack");
     }

     public void fly() {
         System.out.println("I'm flying");
     }
 }

 //火鸡
 public interface Turkey {
     public void gobble();
     public void fly();
 }

 public class WildTurkey implements Turkey {
     public void gobble() {
        System.out.println("Gobble gobble");
     }

     public void fly() {
        System.out.println("I'm flying a short distance");
     }
 }

 //适配器
 public class TurkeyAdapter implements Duck {
     Turkey turkey;

     public TurkeyAdapter(Turkey turkey) {
         this.turkey = turkey;
     }

     public void quack() {
         turkey.gobble();
     }

     public void fly() {
         for (int i=0; i < 5; i++) {
            turkey.fly();
         }
     }
 }

 //客户通过目标接口调用适配器的方法对适配器发出请求，适配器使用被适配者接口把请求转换成被适配者的一个或多个调用接口，客户接收到调用的结果，但并未察觉这一切是适配器做起转换作用。

 //适配器模式是将一个类的接口，转换成客户期望的另一个接口。适配器让原本接口不兼容的类可以合作无间。

 //总结，当需要使用现有的类而其接口不符合需要时，使用适配器。
 
 //延伸，适配器模式有两种形式，对象适配和类适配，类适配其实就是用(java)多重继承实现，效果一样但不够灵活。和装饰者比较，适配器是包装了一个对象并改变它的接口，装饰者是将对象包装起来后增加新行为和责任。