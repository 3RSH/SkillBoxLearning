
public class Cat
{
    public static final int EYES_COUNT = 2;
    public static final double MIN_WEIGHT = 1000.0;
    public static final double MAX_WEIGHT = 9000.0;

    private static int catCount = 0;

    private double originWeight;
    private double weight;
    private double feedingWeight;
    private Color color;

    public Cat()
    {
        weight = 1500.0 + 3000.0 * Math.random();
        originWeight = weight;
        feedingWeight = 0.0;
        catCount++;
    }

    public Cat(double weight)
    {
        this.weight = weight;
        originWeight = weight;
        feedingWeight = 0.0;
        catCount++;
        countCtrl();
    }

    public Cat(Cat cat)
    {
        this.originWeight = cat.originWeight;
        this.weight = cat.weight;
        this.feedingWeight = cat.feedingWeight;
        setColor(cat.getColor());
        catCount++;
        countCtrl();
    }

    public void setOriginWeight(double originWeight) { this.originWeight = originWeight; }
    public double getOriginWeight() { return originWeight; }

    public void setWeight(double weight) { this.weight = weight; }
    public double getWeight() { return weight; }

    public void setFeedingWeight(double feedingWeight) { this.feedingWeight = feedingWeight; }
    public double getFeedingWeight() { return feedingWeight; }

    public double getMinWeight() { return MIN_WEIGHT; }

    public double getMaxWeight() { return MAX_WEIGHT; }

    public void setColor(Color color) { this.color = color; }
    public Color getColor() { return color; }

    public static int getCount() { return catCount; }

    public void countCtrl()
    {
        if (!isAlive()) {
            catCount--;
        }
    }

    public boolean isAlive() { return (weight >= MIN_WEIGHT) && (weight <= MAX_WEIGHT); }

    public void meow()
    {
        if (isAlive()) {
            weight = weight - 1;
            System.out.println("Meow");
            countCtrl();
        }
    }

    public void feed(Double amount)
    {
        if (isAlive()) {
            feedingWeight += amount;
            weight = weight + amount;
            countCtrl();
        }
    }

    public void drink(Double amount)
    {
        feed(amount);
    }

    public void pee()
    {
        if (isAlive()) {
            weight -= 100.0;
            System.out.println("burble-burble-burble..");
            countCtrl();
        }
    }

    public String getStatus()
    {
        if(weight < MIN_WEIGHT) {
            return "Dead";
        }
        else if(weight > MAX_WEIGHT) {
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