interface FanState {
    void pressButton(Fan fan);
}

//Context class
class Fan {
    private FanState state;

    public Fan() {
        //default state then first it will go to low speed class phir low speed se high
        // speed phir high speed se off state jayega
        this.state = new OffState();
    }

    public void setState(FanState state) {
        this.state = state;
    }

    public void pressButton() {
        state.pressButton(this);
    }
}

class OffState implements FanState {

    @Override
    public void pressButton(Fan fan) {
        System.out.println("Fan is turned ON to LOW speed.");
        fan.setState(new LowSpeedState());
    }
}
class LowSpeedState implements FanState{

    @Override
    public void pressButton(Fan fan) {
        System.out.println("Fan is turned to HIGH speed.");
        fan.setState(new HighSpeedState());
    }
}

class HighSpeedState implements FanState {

    @Override
    public void pressButton(Fan fan) {
        System.out.println("Fan is turned OFF.");
        fan.setState(new OffState());
    }
}
