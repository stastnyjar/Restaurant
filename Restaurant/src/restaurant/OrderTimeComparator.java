package restaurant;

import java.util.Comparator;

public class OrderTimeComparator implements Comparator<Order>{
    @Override
    public int compare(Order first, Order second){
        
        return Integer.valueOf(first.getWaiterNumber()).compareTo(Integer.valueOf(second.getWaiterNumber()));
    }
}
