abstract class Vehicle{
    private String name;
    private String color;
    public Vehicle(String name, String color){
        System.out.println("Vehicle Constructor");
        this.name = name;
        this.color = color;
    }
    public String getName(){
        return name;
    }
    public String getColor() {
        return color;
    }
}
class Bike extends Vehicle{
    public Bike(String name, String color){
        super(name, color);
        System.out.println("Bike Constructor");

    }
}

public class Testss  {

    public static void main(String[] args) {
        Vehicle v = new Bike("Pulsar", "Black");
        System.out.println(v.getName() + " " + v.getColor());
    }
}