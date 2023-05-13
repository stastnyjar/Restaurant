package restaurant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

class Dish implements Comparable<Dish>, Serializable{
    private String title;
    private BigDecimal price;
    private int preparationTime;
    private String category;
    private ArrayList<String> fileNames;
    
    public Dish(String title, BigDecimal price, int preparationTime){
        this.title = title;
        this.price = price;
        this.preparationTime = preparationTime;
        fileNames = new ArrayList<>();
        fileNames.add("blank");
    }
    public String getTitle() {
        return title;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public int getPreparationTime() {
        return preparationTime;
    }
    public String getCategory() {
        return category;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public void setPreparationTime(int preparationTime) {
        this.preparationTime = preparationTime;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void addFileName(String fileName){
        if(fileNames.contains("blank")){
            fileNames.remove("blank");
        }
        fileNames.add(fileName);
    }
    public void removeFileName(String fileName){
        if(fileNames.size() > 1){
            fileNames.remove(fileName);
        }else{
            System.out.println("Není možné odebrat všechny fotografie.");
        }
    }
    public void clearFileNames(){
        fileNames.clear();
    }
    @Override
    public int compareTo(Dish dish2) {
        return this.getCategory().compareTo(dish2.getCategory());
    }
}
