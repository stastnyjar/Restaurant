package restaurant;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

class Dish implements Comparable<Dish>, Serializable{
    private String title;
    private BigDecimal price;
    private int preparationTime;
    private String category;
    private ArrayList<String> photos;
    
    public Dish(String title, BigDecimal price, int preparationTime){
        this.title = title;
        this.price = price;
        this.preparationTime = preparationTime;
        photos = new ArrayList<>();
        photos.add("blank");
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
    public void addPhoto(String fileName){
        if(photos.contains("blank")){
            photos.remove("blank");
        }
        photos.add(fileName);
    }
    public void removePhoto(String fileName){
        if(photos.size() > 1){
            photos.remove(fileName);
        }else{
            System.out.println("Není možné odebrat všechny fotografie.");
        }
    }
    public void clearPhotos(){
        photos.clear();
    }
    @Override
    public int compareTo(Dish dish2) {
        return this.getCategory().compareTo(dish2.getCategory());
    }
}
