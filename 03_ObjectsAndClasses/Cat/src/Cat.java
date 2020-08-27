
public class Cat
{
    public static final int EYES_COUNT = 2;
    public static final double MIN_WEIGHT = 1000.0;
    public static final double MAX_WEIGHT = 9000.0;

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
        minWeight = MIN_WEIGHT;
        maxWeight = MAX_WEIGHT;
        feedingWeight = 0.0;
        catCount++;
    }

    public static int getCount(){ return catCount; }

    public boolean isAlive() { return (weight >= minWeight) && (weight <= maxWeight); }

    public void meow()
    {
        if (isAlive()) {
            weight = weight - 1;
            System.out.println("Meow");
            if (!isAlive()) catCount--;
        }
    }

    public void feed(Double amount)
    {
        if (isAlive()) {
            feedingWeight += amount;
            weight = weight + amount;
            if (!isAlive()) catCount--;
        }
    }

    public void drink(Double amount)
    {
        if (isAlive()) {
            feedingWeight += amount;
            weight = weight + amount;
            if (!isAlive()) catCount--;
        }
    }

    public void pee()
    {
        if (isAlive()) {
            weight -= 100.0;
            System.out.println("burble-burble-burble..");
            if (!isAlive()) catCount--;
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