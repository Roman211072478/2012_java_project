import java.io.*;
public class FoodMenuItem implements Serializable
{
    private static int idNum = 0;
    private int foodId;
    private String category;
    private String foodItem;
    private double price;

    /**
     * Constructor for objects of class FoodMenuItem
     */
    public FoodMenuItem()
    {
    }
    public FoodMenuItem(String f, String c, double p)
    {
        setCategory(c);
        foodItem = f;
        price = p;
        foodId = ++idNum;
    }

    public void setCategory(String c)
    {
        category = c;
    }
    public String getCategory(){
        return category;
    }
    public String getFoodItem(){
        return foodItem;
    }
    public double getPrice(){
        return price;
    }       
    public int getFoodId(){
        return foodId;
    }       
    public String toString(){
        return String.format("%-10d%-20s %-10s %.2f", foodId, foodItem, category, price);
    }       
}
