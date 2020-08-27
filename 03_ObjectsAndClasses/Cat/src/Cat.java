
public class Cat
{
    private static int catCount = 0;

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
        catCount++;
    }

    public static int getCount(){ return catCount; }

    public boolean isLive() { return (weight >= minWeight) && (weight <= maxWeight); }

    public void meow()
    {
        if (isLive()) {
            weight = weight - 1;
            System.out.println("Meow");
            if (!isLive()) catCount--;
        }
    }

    public void feed(Double amount)
    {
        if (isLive()) {
            feedingWeight += amount;
            weight = weight + amount;
            if (!isLive()) catCount--;
        }
    }

    public void drink(Double amount)
    {
        if (isLive()) {
            feedingWeight += amount;
            weight = weight + amount;
            if (!isLive()) catCount--;
        }
    }

    public void pee()
    {
        if (isLive()) {
            weight -= 100.0;
            System.out.println("burble-burble-burble..");
            if (!isLive()) catCount--;
        }
    }

    public Double getWeight() { return weight; }

    public Double getFeedingWeight() { return feedingWeight; }

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