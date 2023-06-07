
package restaurant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class Restaurant {
    private static ArrayList<Dish> dishList;
    private static ArrayList<Dish> menu;
    private static ArrayList<Order> pendingOrders;
    private static ArrayList<Order> fulfiledOrders;
    private static ArrayList<String> notes;

    
    public static void addToDishList(Dish dish){
        dishList.add(dish);
    }
    public static void removeFromDishList(Dish dish){
        dishList.remove(dish);
    }
    public static void addToMenu(Dish dish) throws DishException{
        if(dishList.contains(dish)){
            menu.add(dish);
        }else{
            throw new DishException("Toto jídlo není v repertoáru.");
        }
    }
    public static void addToMenu(int index){
        menu.add(dishList.get(index));
    }
    public static void removeFromMenu(Dish dish){
        menu.remove(dish);
    }
    public static void clearMenu(){
        menu.clear();
    }
    public static boolean isOnMenu(Dish dish){
        return menu.contains(dish);
    }
    public static void addFulfiledOrder(Order order){
        fulfiledOrders.add(order);
    }
    public static int getPendingOrdersNumber(){
        return pendingOrders.size();
    }
    public static ArrayList<Order> sortOrdersByTime(){
        Collections.sort(pendingOrders);
        return pendingOrders;
    }
    public static ArrayList<Order> sortOrdersByWaiter(){
        Collections.sort(pendingOrders, new OrderTimeComparator());
        return pendingOrders;
    }
    public static double getAverageFulfilmentTime(LocalTime from, LocalTime to){
        int orders = 0;
        int sum = 0;
        try{
            for(Order order: fulfiledOrders){
                if(order.getOrderedTime().isAfter(from) && order.getFulfilmentTime().isBefore(to)){
                    orders++;
                    int time = (int)order.getOrderedTime().until(order.getFulfilmentTime(), ChronoUnit.SECONDS);
                    sum += time;
                }
            }
            return sum/orders;
        }catch(Exception e){//pro případ, že si tento den ještě nikdo nic neobjednal (dělení nulou)
            return 0;
        }
    }
    public static void order(Dish dish, int table, int amount, int waiterNumber) throws DishException{
        if(Restaurant.isOnMenu(dish)){
            pendingOrders.add(new Order(table, dish, amount, waiterNumber));
        }else{
            throw new DishException("Toto jídlo není v menu.");
        }
    }
    public static void finishOrder(Order order){
        if(pendingOrders.contains(order)){
            order.deliver();
            pendingOrders.remove(order);
            fulfiledOrders.add(order);
        }
    }
    public static void cancelOrder(Order order){
        if(pendingOrders.contains(order)){
            pendingOrders.remove(order);
        }
    }
    public static HashSet<Dish> getTodaysDishes(){
        HashSet<Dish> dishes = new HashSet<>();
        for(Order order: fulfiledOrders){
            dishes.add(order.getDish());
        }
        return dishes;
    }
    public static void writeOutOrders(int tableNumber){
        System.out.println("** Objednávky pro stůl č. " + tableNumber + " **");
        System.out.println("****");
        int orderNumber = 1;
        for(Order order: fulfiledOrders){
            if(order.getTable() == tableNumber){
                System.out.println(orderNumber + ". " + order.getDish().getTitle() + " " + order.getAmount() + "x (" + order.getPrice() + 
                        " Kč):\t" + order.getOrderedTime().format(DateTimeFormatter.ofPattern("hh:mm")) + "-" +
                        order.getFulfilmentTime().format(DateTimeFormatter.ofPattern("hh:mm")) + "\t" + "číšník č. " + order.getWaiterNumber());
            }
        }
    }
    public static void getOrdersPerWaiters(int waiters){
        for(int i = 1; i<=waiters; i++){
            int pending = 0;
            int fulfiled = 0;
            BigDecimal total = BigDecimal.ZERO;
            for(Order order: pendingOrders){
                if(order.getWaiterNumber() == i){
                    pending++;
                    total = total.add(order.getPrice());
                }
            }
            for(Order order: fulfiledOrders){
                if(order.getWaiterNumber() == i){
                    fulfiled++;
                    total = total.add(order.getPrice());
                }
            }
            if(!total.equals(BigDecimal.ZERO)){
                System.out.println("Číšník " + i + ": " + total + " Kč (" + (pending+fulfiled) + " objednávek, z toho " + fulfiled + " dokončených.");
            }   
        }
    }
    public static void saveData(){
        try{
            //pouze dočasný adresář, v praxi bude změněn na adresář na serveru
            FileOutputStream fos = new FileOutputStream(new File("restaurace.txt"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dishList);
            oos.writeObject(menu);
            oos.writeObject(pendingOrders);
            oos.writeObject(fulfiledOrders);
            oos.writeObject(notes);
            oos.close();
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }
    public static void loadData(){
        try{
            FileInputStream fis = new FileInputStream(new File("restaurace.txt"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            dishList = ((ArrayList<Dish>)ois.readObject());
            menu = ((ArrayList<Dish>) ois.readObject());
            pendingOrders = ((ArrayList<Order>)ois.readObject());
            fulfiledOrders = ((ArrayList<Order>)ois.readObject());
            notes = ((ArrayList<String>)ois.readObject());
            ois.close();
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }
    public static void clearData(){
        try{
            File file = new File("restaurace.txt");
            file.delete();
        }catch(Exception ex){
            System.err.println(ex.getMessage());
        }
    }
    public static void main(String[] args) {
        dishList = new ArrayList<>();
        menu = new ArrayList<>();
        pendingOrders = new ArrayList<>();
        fulfiledOrders = new ArrayList<>();
        notes = new ArrayList<>();
        loadData();
        dishList.add(new Dish("Kuřecí řízek obalovaný 150 g", BigDecimal.valueOf(145), 15));
        dishList.add(new Dish("Hranolky 150 g", BigDecimal.valueOf(45), 10));
        dishList.add(new Dish("Pstruh na víně 200 g", BigDecimal.valueOf(235), 20));
        dishList.add(new Dish("Pivo Staropramen 11°, 500 ml", BigDecimal.valueOf(50), 5));
        dishList.add(new Dish("Coca Cola 250 ml", BigDecimal.valueOf(45), 5));
        dishList.add(new Dish("Džus Cappy pomeranč 250 ml", BigDecimal.valueOf(45), 5));
        menu.add(dishList.get(0));
        menu.add(dishList.get(2));
        menu.add(dishList.get(3));
        menu.add(dishList.get(4));
        menu.add(dishList.get(5));
        try{
            order(menu.get(2), 15, 2, 3);
            order(menu.get(0), 15, 1, 1);
            order(menu.get(1), 15, 1, 1);
            order(menu.get(1), 2, 1, 2);
            order(menu.get(4), 2, 1, 2);
        }catch(DishException e){
            System.err.println(e.getMessage());
        }
        System.out.println("Objednávek celkem: " + getPendingOrdersNumber());
        System.out.println("Objednávky:");
        for(Order o: sortOrdersByWaiter()){
            System.out.println(o.getDish().getTitle() + ", objednáno u stolu " + o.getTable());
        }
        writeOutOrders(2);
        writeOutOrders(15);
        getOrdersPerWaiters(3);
        saveData();
    }
}
