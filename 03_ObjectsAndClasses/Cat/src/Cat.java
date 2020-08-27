
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
    private Color color;

    public void setOriginWeight(double originWeight) { this.originWeight = originWeight; }
    public double getOriginWeight() { return originWeight; }

    public void setWeight(double weight) { this.weight = weight; }
    public double getWeight() { return weight; }

    public void setFeedingWeight(double feedingWeight) { this.feedingWeight = feedingWeight; }
    public double getFeedingWeight() { return feedingWeight; }

    public void setMinWeight(double minWeight) { this.minWeight = minWeight; }
    public double getMinWeight() { return minWeight; }

    public void setMaxWeight(double maxWeight) { this.maxWeight = maxWeight; }
    public double getMaxWeight() { return maxWeight; }

    public void setColor(Color color) { this.color = color; }
    public Color getColor() { return color; }

    public Cat()
    {
        weight = 1500.0 + 3000.0 * Math.random();
        originWeight = weight;
        minWeight = MIN_WEIGHT;
        maxWeight = MAX_WEIGHT;
        feedingWeight = 0.0;
        catCount++;
    }

    public Cat(double weight)
    {
        this.weight = weight;
        originWeight = weight;
        minWeight = MIN_WEIGHT;
        maxWeight = MAX_WEIGHT;
        feedingWeight = 0.0;
        catCount++;
    }

    public Cat(Cat cat)
    {
        this.originWeight = cat.originWeight;
        this.weight = cat.weight;
        this.feedingWeight = cat.feedingWeight;
        this.minWeight = cat.minWeight;
        this.maxWeight = cat.maxWeight;
        setColor(cat.getColor());
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