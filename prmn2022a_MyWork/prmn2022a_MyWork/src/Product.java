public class Product {
    private int qty;
    private int maxQty;
    private int price;
    private String name;
    private int id;

    public Product(int qty, int maxQty, int price, String name, int id){
        this.qty = qty;
        this.maxQty = maxQty;
        this.price = price;
        this.name = name;
        this.id = id;
    }

    public int getQty(){return qty;}
    public void setQty(int qty){this.qty = qty;}

    public int getMaxQty(){return maxQty;}

    public int getPrice(){return price;}
    public void setPrice(int price){this.price = price;}

    public String getName(){return name;}

    public int getId(){return id;}

    public void view(){
        System.out.println(name + "    " + price + "円  " + qty + "個  " + "最大在庫数" + maxQty);
    }

}
