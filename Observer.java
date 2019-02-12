/**
 * 观察者模式的应用：
 * 气象站将数据传输到显示装置上显示
 */

//方案一，错误的示范
public class WeatherData1 {
    public void measurementsChanged() {
        float temp = getTemperature();
        float pressure = getPressure();

        displayer1.update(temp, pressure);
        displayer2.update(temp, pressure);
    }
}

//这样就耦合了具体的对象displayer1和displayer2了

//方案二，应用观察者
public interface Subject {
    public void registerObserver(Observer o);
    public void removeObserver(Observer o);
    public void notifyObservers();
}

public interface Observer {
    public void update(float temp, float pressure);
}

public interface DisplayElement {
    public void display();
}

public class WeatherData2 implements Subject {
    private ArrayList observers;
    private float temperature;
    private float pressure;

    public WeatherData() {
        observers = new ArrayList();
    }

    public void registerObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        int i = observers.indexOf(o);
        if (i >=0) {
            observers.remove(i);
        }
    }

    public void notifyObservers() {
        for (int i = 0; i < observers.size(); i++) {
            Observer observer = (Observer)observers.get(i);
            observer.update(temperature, pressure);
        }
    }

    public void measurementsChanged() {
        notifyObservers();
    }

    public void setMeasurements(float temperature, float pressure) {
        this.temperature = temperature;
        this.pressure = pressure;
        measurementsChanged();
    }
}

public class Displayer1 implements Observer, DisplayElement {
    private float temperature;
    private Subject weatherData;

    public Displayer1(Subject weatherData) {
        this.weatherData = weatherData;
        weatherData.registerObserver(this);
    }

    public void update(float temperature, float pressure) {
        this.temperature = temperature;
        display();
    }

    public void display() {
        //... 显示的实现
    }
}

//方案三，利用系统内置观察方式
//import java.util.Observable;
//import java.util.Observer;

public class WeatherData3 implements Observable {
    private float temperature;
    private float pressure;

    public WeatherData() { } //不再需要记住观察者的数据结构

    public void measurementsChanged() {
        setChanged(); //通知观察者前先标记状态改变了（系统API）
        notifyObservers(); //通知观察者（系统API）
    }

    public void setMeasurements(float temperature, float pressure) {
        this.temperature = temperature;
        this.pressure = pressure;
        measurementsChanged();
    }
}

public class Displayer2 implements Observer, DisplayElement {
    private float temperature;
    Observable observable;

    public Displayer1(Observable observable) {
        this.observable = observable;
        observable.addObserver(this);
    }

    public void update(Observer obs, Object arg) {
        if (obs instanceof WeatherData3) {
            WeatherData3 weatherData = (WeatherData3)obs;
            this.temperature = weatherData.getTemperature();
            display();
        }
    }

    public void display() {
        //... 显示的实现
    }
}

//可见系统内置的观察方式，已经帮我们定义了注册观察者、通知观察者及其触发的更新方法。只是特别需要注意setChanged()这个方法需要在状态改变后先调用，后再调用notifyObservers()。

 //总结，观察者模式定义了对象之间的一对多依赖，这样一来，当一个对象改变状态时，它的所有依赖者都会收到通知并自动更新。