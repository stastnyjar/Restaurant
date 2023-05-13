package restaurant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.ArrayList;

public class Order implements Comparable<Order>, Serializable{
    private int table;
    private Dish dish;
    private int amount;
    private int waiterNumber;
    private LocalTime orderedTime;
    private LocalTime fulfilmentTime;
    
    public Order(int table, Dish dish, int amount, int waiterNumber){
        this.table = table;
        this.dish = dish;
        this.amount = amount;
        this.waiterNumber = waiterNumber;
        orderedTime = LocalTime.now();
    }
    
    public void deliver(){
        fulfilmentTime = LocalTime.now();
    }
    public int getTable() {
        return table;
    }
    public Dish getDish() {
        return dish;
    }
    public int getAmount() {
        return amount;
    }
    public int getWaiterNumber() {
        return waiterNumber;
    }
    public BigDecimal getPrice(){
        return dish.getPrice().multiply(BigDecimal.valueOf(amount));
    }
    public LocalTime getOrderedTime() {
        return orderedTime;
    }
    public LocalTime getFulfilmentTime() {
        return fulfilmentTime;
    }
    public LocalTime getExpectedFulfilmentTime(){
        return orderedTime.plusMinutes(dish.getPreparationTime());
    }
    @Override
    public int compareTo(Order o) {
        return Integer.valueOf(this.waiterNumber).compareTo(Integer.valueOf(o.getWaiterNumber()));
    }
}
