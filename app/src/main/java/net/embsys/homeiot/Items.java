package net.embsys.homeiot;

public class Items {

    private String name;
    private String price;
    private String img;
    private String count;


    public Items(String name, String price, String count, String img) {
        this.name = name;
        this.price = price;
        this.count = count;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.img = count;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
