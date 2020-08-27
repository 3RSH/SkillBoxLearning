
public class Cat
{
    private double originWeight;
    private double weight;
    private double feedingWeight;

    private double minWeight;
    private double maxWeight;

    public Cat()
    {
        weight = 1500.0 + 3000.0 * Math.random();
        originWeight = weight;
        minWeight = 1000.0;
        maxWeight = 9000.0;
        feedingWeight = 0.0;

    }

    public void meow()
    {
        weight = weight - 1;
        System.out.println("Meow");
    }

    public void feed(Double amount)
    {
        feedingWeight += amount;
        weight = weight + amount;
    }

    public void drink(Double amount)
    {
        feedingWeight += amount;
        weight = weight + amount;
    }

    public void pee()
    {
        weight -= 100.0;
        System.out.println("burble-burble-burble..");
    }

    public Double getWeight()
    {
        return weight;
    }

    public Double getFeedingWeight() {return feedingWeight;}

    public String getStatus()
    {
        if(weight < minWeight) {
            return "Dead";
        }
        else if(weight > maxWeight) {
            return "Exploded";
        }
        else if(weight > originWeight) {
            return "Sleeping";
        }
        else {
            return "Playing";
        }
    }


}