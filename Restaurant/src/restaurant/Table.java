package restaurant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Table implements Serializable{
    private int number;
    private ArrayList<Order> pendingOrders;
    private ArrayList<Order> fulfiledOrders;
    private String note;
    
    public Table(int number){
        this.number = number;
        pendingOrders = new ArrayList<>();
        fulfiledOrders = new ArrayList<>();
    }
    public ArrayList<Order> getPendingOrders() {
        return pendingOrders;
    }
    public ArrayList<Order> getFulfiledOrders() {
        return fulfiledOrders;
    }
    public void setNote(String note) {
        this.note = note;
    }
    public void order(Dish dish, int amount, int waiterNumber) throws DishException{
        if(Restaurant.isOnMenu(dish)){
            pendingOrders.add(new Order(number, dish, amount, waiterNumber));
        }else{
            throw new DishException("Toto jídlo není v menu.");
        }
    }
    public void deliver(Dish dish) throws DishException{
        Order order = null;
        for(Order o: pendingOrders){
            if(o.getDish().equals(dish)){
                order = o;
            }
        }
        if(order != null){
            pendingOrders.remove(order);
            fulfiledOrders.add(order);
            Restaurant.addFulfiledOrder(order);
        }else{
            throw new DishException("Toto jídlo se tento stůl neobjednal.");
        }
    }
    public boolean cancelOrder(Dish dish){
        Order order = null;
        for(Order o: pendingOrders){
            if(o.getDish().equals(dish)){
                order = o;
            }
        }
        if(order != null){
            pendingOrders.remove(order);
            return true;
        }else{
            return false;
        }
    }
    public BigDecimal finish() throws DishException{
        if(!pendingOrders.isEmpty()){
            throw new DishException("Zatím nebyly splněny všechny objednávky.");
        }
        BigDecimal total = BigDecimal.valueOf(0);
        for(Order order: fulfiledOrders){
            total.add(order.getPrice());
        }
        fulfiledOrders.clear();
        if(note != null){
            Restaurant.addNote(note);
            note = null;
        }
        return total;
    }
    public void writeOutOrders(){
        String tableNumber = number<10?" " + number:"" + number;
        System.out.println("** Objednávky pro stůl č. " + tableNumber + " **");
        System.out.println("****");
        int orderNumber = 1;
        for(Order order: fulfiledOrders){
            System.out.println(orderNumber + ". " + order.getDish().getTitle() + " " + order.getAmount() + "x (" + order.getPrice() + 
                    " Kč):\t" + order.getOrderedTime().format(DateTimeFormatter.ofPattern("hh:mm")) + "-" +
                    order.getFulfilmentTime().format(DateTimeFormatter.ofPattern("hh:mm")) + "\t" + "číšník č. " + order.getWaiterNumber());
        }
    }
}
