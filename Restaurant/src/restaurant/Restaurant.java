
package restaurant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class Restaurant {
    private static ArrayList<Dish> dishList;
    private static ArrayList<Dish> menu;
    private static ArrayList<Table> tables;
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
    public static void addPhoto(String filename, Dish dish) throws DishException{
        if(dishList.contains(dish)){
            dish.addFileName(filename);
        }else{
            throw new DishException("Toto jídlo není v repertoáru.");
        }
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
    public static void addNote(String note){
        notes.add(note);
    }
    public static int getPendingOrdersNumber(){
        return getAllPendingOrders().size();
    }
    public static ArrayList<Order> getAllPendingOrders(){
        ArrayList<Order> allOrders = new ArrayList<>();
        for(Table t: tables){
            allOrders.addAll(t.getPendingOrders());
        }
        return allOrders;
    }
    public static ArrayList<Order> getAllCurrentOrders(){
        ArrayList<Order> allOrders = new ArrayList<>();
        for(Table t: tables){
            allOrders.addAll(t.getPendingOrders());
            allOrders.addAll(t.getFulfiledOrders());
        }
        return allOrders;
    }
    public static ArrayList<Order> sortOrdersByWaiter(){
        ArrayList<Order> allOrders = getAllPendingOrders();
        Collections.sort(allOrders);
        return allOrders;
    }
    public static ArrayList<Order> sortOrdersByTime(){
        ArrayList<Order> allOrders = getAllPendingOrders();
        Collections.sort(allOrders, new OrderTimeComparator());
        return allOrders;
    }
    public static double getAverageFulfilmentTime(LocalTime from, LocalTime to){
        int orders = 0;
        int sum = 0;
        try{
            for(Order order: fulfiledOrders){
                if(order.getOrderedTime().isAfter(from) && order.getFulfilmentTime().isBefore(to)){
                    orders++;
                    int time = (int)order.getOrderedTime().until(to, ChronoUnit.SECONDS);
                    sum += time;
                }
            }
            return sum/orders;
        }catch(Exception e){//pro případ, že si tento den ještě nikdo nic neobjednal (dělení nulou)
            return 0;
        }
    }
    public static HashSet<Dish> getTodaysDishes(){
        HashSet<Dish> dishes = new HashSet<>();
        for(Order order: fulfiledOrders){
            dishes.add(order.getDish());
        }
        return dishes;
    }
    public static void saveData(){
        try{
            //pouze dočasný adresář, v praxi bude změněn na adresář na serveru
            FileOutputStream fos = new FileOutputStream("C:\\Users\\jstas\\OneDrive\\Dokumenty\\restaurace.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(dishList);
            oos.writeObject(menu);
            oos.writeObject(tables);
            oos.writeObject(fulfiledOrders);
            oos.writeObject(notes);
            oos.close();
        }catch(Exception ex){
            System.err.println("Ukládání selhalo");
        }
    }
    public static void loadData(){
        try{
            FileInputStream fis = new FileInputStream("C:\\Users\\jstas\\OneDrive\\Dokumenty\\restaurace.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            dishList = ((ArrayList<Dish>)ois.readObject());
            menu = ((ArrayList<Dish>) ois.readObject());
            tables = ((ArrayList<Table>)ois.readObject());
            fulfiledOrders = ((ArrayList<Order>)ois.readObject());
            notes = ((ArrayList<String>)ois.readObject());
            ois.close();
        }catch(Exception ex){
            System.err.println("Načítání selhalo");
        }
    }
    public static void clearData(){
        try{
            File file = new File("C:\\Users\\jstas\\OneDrive\\Dokumenty\\restaurace.txt");
            file.delete();
        }catch(Exception ex){
            System.err.println("Soubor nebyl nalezen.");
        }
    }
    public static void main(String[] args) {
        dishList = new ArrayList<>();
        menu = new ArrayList<>();
        tables = new ArrayList<>();
        fulfiledOrders = new ArrayList<>();
        notes = new ArrayList<>();
        for(int i = 0; i<16; i++){
            /*stůl 0 není myšlen k používání a ve frontendu nebude přístupný, slouží pouze k tomu, aby
            indexy stolů v seznamu odpovídaly jejich číslům*/
            tables.add(new Table(i));
        }
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
            tables.get(15).order(menu.get(2), 2, 3);
            tables.get(15).order(menu.get(0), 1, 1);
            tables.get(15).order(menu.get(1), 1, 1);
            tables.get(2).order(menu.get(1), 1, 2);
            tables.get(2).order(menu.get(4), 1, 2);
        }catch(DishException e){
            System.err.println(e.getMessage());
        }
        System.out.println("Objednávek celkem: " + getPendingOrdersNumber());
        System.out.println("Objednávky:");
        for(Order o: sortOrdersByWaiter()){
            System.out.println(o.getDish().getTitle() + ", objednáno u stolu " + o.getTable());
        }
        saveData();
    }
}
