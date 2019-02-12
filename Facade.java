/**
 * 外观模式的应用：
 * 假设想看一出电影大片，但看之前要准备一大堆操作
 */

 popper.on();
 popper.pop();
 lights.dim(10);
 screen.down();
 projector.on();
 projector.setInput(dvd);
 projector.wideScreenMode();
 amp.on();
 amp.setDvd(dvd);
 amp.setSurroundSound();
 amp.setVolume(5);
 dvd.on();
 dvd.play(movie);

//当你想关闭时，又是一堆操作

//我们使用组合的方式将各个子系统汇集起来，然后实现统一个的接口

public class HomeTheaterFacade {
    Amplifier amp;
    Tuner tuner;
    DvdPlayer dvd;
    CdPlayer cd;
    Projector projector;
    TheaterLights lights;
    Screen screen;
    PopcornPopper popper;

    public HomeTheaterFacade(Amplifier amp,Tuner tuner,DvdPlayer dvd,CdPlayer cd,Projector projector,Screen screen,TheaterLights lights,PopcornPopper popper) {
        this.amp = amp;
        this.tuner = tuner;
        this.dvd = dvd;
        this.cd = cd;
        this.projector = projector;
        this.screen = screen;
        this.lights = lights;
        this.popper = popper;
    }

    public void watchMovie(String movie) {
        System.out.println("Get ready to watch a movie ... ");
        popper.on();
        popper.pop();
        lights.dim(10);
        screen.down();
        projector.on();
        projector.setInput(dvd);
        projector.wideScreenMode();
        amp.on();
        amp.setDvd(dvd);
        amp.setSurroundSound();
        amp.setVolume(5);
        dvd.on();
        dvd.play(movie);
    }

    public void endMovie() {
        System.out.println("Shutting movie theater down ... ");
        popper.off();
        lights.on();
        screen.up();
        projector.off();
        amp.off();
        dvd.stop();
        dvd.eject();
        dvd.off();
    }
}

//外观模式提供了一个统一的接口，用来访问子系统中的一群接口。外观定义了一个高层接口，让子系统更容易使用。也可以理解为是一个包装了多个被适配者的适配器。

//外观模式和适配器模式的设计原则是，最少知识原则（又叫“墨忒耳法则”），只和你的密友谈话，不要让太多的类耦合在一起，避免修改系统的一部分而影响到其它部分。

//避免耦合过多类的示例

public float getTemp1() {
    Thermometer thermometer = station.getThermometer();
    return thermometer.getTemperature();
}

public float getTemp2() {
    return station.getTemperature();
}

//总结，当需要简化并统一一个很大的接口或者一群复杂的接口时，使用外观，从一个复杂的子系统中解耦。

