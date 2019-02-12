/**
 * 命令模式的应用：
 * 使用多功能的遥控器打开一个电灯
 */

public interface Command {
    public void execute();
}

public class LightOnCommand implements Command {
    Light light;

    public LigitOnCommand(Light light) {
        this.light = light;
    }

    public void execute() {
        light.on();
    }
}

public class SimpleRemoteControl {
    Command slot;

    public SimpleRemoteControl() { }

    public void setCommand(Command command) {
        slot = command;
    }

    public void buttonWasPressed() {
        slot.execute();
    }
}

public class RemoteControlTest {
    public static void main(String[] args) {
        SimpleRemoteControl remote = new SimpleRemoteControl();
        Light light = new Light();
        LightOnCommand lightOn = new LightOnCommand(light);

        remote.setCommand(lightOn);
        remote.buttonWasPressed();
    }
}

//遥控器是调用者，电灯是接收者。创建一个命令并将接收者传给命令，然后将命令传给调用者。这样，将发出请求的对象和执行请求的对象解耦，通过命令对象沟通。

//一个命令对象通过在特定接收者上绑定一组动作来封装一个请求，其它对象不知道究竟哪个接收者进行了哪些动作，只知道调用execute方法，请求的目的就能达到。

//总结，命令模式将“请求”封装成对象，以便使用不同的请求、队列或者日志来参数化其它对象，命令模式也支持可撤销的操作。