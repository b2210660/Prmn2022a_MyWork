import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

public class ProductManager {
    Scanner input = new Scanner(System.in);
    private ArrayList<Product> products = new ArrayList<>(); //在庫
    private ArrayList<Product> cart = new ArrayList<>(); //カート内の商品群
    private ArrayList<ArrayList<Product>> sellerHistory = new ArrayList<>(); //販売履歴
    private ArrayList<Date> sellerDateHistory = new ArrayList<>(); //販売時刻履歴
    int addCartCnt = 0;
    int bill;
    public ProductManager(String[] data){ //商品を生成→ArrayListに追加
        for (String datum : data) {
            String[] splitted = datum.split(",");
            Product product = new Product(Integer.parseInt(splitted[0]), Integer.parseInt(splitted[1]), Integer.parseInt(splitted[2]), splitted[3], Integer.parseInt(splitted[4]));
            products.add(product);
        }
    }


    public void allStockView(){ //商品一覧
        System.out.println("ID  商品名   価格   在庫");
        for (int i=0 ; i< products.size() ; i++) {
            System.out.println(products.get(i).getId() + "   " + products.get(i).getName() +
                    "   " + products.get(i).getPrice() + "    " + products.get(i).getQty() + "個");
        }
    }


    public void allCartViewWithNumber(){ //カート修正のときのカート内一覧

        System.out.println("\n番号  商品名  選択数  残数");
        for (int i=0 ; i< cart.size() ; i++) {
            System.out.println((i + 1) + "    " + cart.get(i).getName() + "   " +
                    cart.get(i).getQty() + "個    " + products.get(cart.get(i).getId()).getQty() + "個");
        }
    }


    public void searchById(int id){ //商品検索
        if (id > products.size()  ||  id < 1) {
            System.out.println("!! IDが異常です !!");
        } else {
            System.out.println("\n*****検索結果*****");
            products.get(id - 1).view();
            System.out.println("*****************\n");
        }
    }


    public void autoStock(){ //自動在庫補充(最大在庫から判定)
        boolean changed = false;
        for (Product product : products) {
            int value = product.getMaxQty() / 2;
            if (product.getQty() < value) { //最大在庫の半数以下で自動入荷
                product.setQty(product.getQty() + value);
                System.out.println("\n" + product.getName() + "を" + value + "個入荷しました");
                changed = true;
            }
        }
        if(changed){
            System.out.println("*****変更後在庫*****");
            allStockView();
            System.out.println("******************");
        }
    }


    public void manualStock(int id, int adQty){ //手動在庫調整
        int YesNo = 1;
        int newStock = products.get(id-1).getQty() + adQty;
        if (id > products.size()  ||  id < 1){
            System.out.println("!! IDが異常です !!");
        } else if (newStock > products.get(id-1).getMaxQty()) { //最大在庫以上のときアラート
            System.out.print("!! 在庫過多になる可能性があります !!\n" +
                    "最大在庫数：" + products.get(id-1).getMaxQty() + "  変更後在庫：" + newStock +
                    "\n処理を実行しますか？　　1:Yes 9:No  -->");
            YesNo = input.nextInt();
        } else if (newStock < 0){
            System.out.print("!! 在庫が異常値になります !!\n" + "変更後在庫：" + newStock +
                    "\n処理を実行しますか？　　1:Yes 9:No  -->");
            YesNo = input.nextInt();
        }
        if(YesNo == 1){
            products.get(id-1).setQty(newStock);
            System.out.println("\n*****変更後在庫*****");
            products.get(id-1).view();
            System.out.println("*********************\n");
        }else if(YesNo == 9){
            System.out.println("在庫調整を中止します");
        }else{
            System.out.println("error\n在庫調整を中止します");
        }
    }


    public void addCart(int id, int buyQty){ //カートに追加
        if(id < 1  ||  id > products.size()) {
            System.out.println("!! IDが異常です !!");
        } else {
            int newQty = products.get(id - 1).getQty() - buyQty;
            if (newQty < 0) { //在庫以上の個数を指定したときのエラー文
                System.out.println("!! " + products.get(id - 1).getName() + "の在庫は" + products.get(id - 1).getQty() + "です !!");
            } else { //正常にカート追加＆在庫変更
                if (newQty == 0) {
                    System.out.println(products.get(id - 1).getName() + "の在庫が0になりました");
                }
                Product boughtProduct = new Product(buyQty,
                        products.get(id - 1).getMaxQty(),
                        products.get(id - 1).getPrice(),
                        products.get(id - 1).getName(), id - 1);
                cart.add(boughtProduct);
                products.get(id - 1).setQty(newQty);
                addCartCnt++;
            }
        }
    }


    public void fixCart(int id, int fixedQty){
        if (id > cart.size()  ||  id < 1) { //IDがカートの範囲からはみ出しているとき
            System.out.println("!! IDが異常です !!");
        } else {
            int increase = fixedQty - cart.get(id-1).getQty();
            if (increase > (products.get(cart.get(id-1).getId()).getQty())){
                System.out.println("!! 在庫が不足しています !!");
            } else if (fixedQty < 0){
                System.out.println("!! 個数が異常です !!");
            } else if (fixedQty == 0){
                products.get(cart.get(id-1).getId()).setQty(products.get(cart.get(id-1).getId()).getQty() - increase);
                cart.remove(id-1);
            } else {
                products.get(cart.get(id-1).getId()).setQty(products.get(cart.get(id-1).getId()).getQty() - increase);
                cart.get(id-1).setQty(fixedQty);
            }
        }
    }


    public void summarizeCart() { //Cart内のproductに重複がある場合に修正する
        ArrayList<Integer> idsInCart = new ArrayList<>(); //カート内の各IDの項目の数（2< で重複）
        ArrayList<Integer> qtysInCart = new ArrayList<>();
        ArrayList<Product> newCart = new ArrayList<>();
        for(int i=0 ; i<products.size() ; i++){
            idsInCart.add(0);
        }
        for(int i=0 ; i<cart.size() ; i++){ //カート内の各IDの項目の数を登録
            int tmp = idsInCart.get(cart.get(i).getId());
            idsInCart.set(cart.get(i).getId(), tmp+1);
        }
        for(int i=0 ; i<idsInCart.size() ; i++){ //カート内のproductの各qtyをまとめる
            int tmp = 0;
            if(idsInCart.get(i) > 1){ //ID=iに重複あり
                for(int j=0 ; j<cart.size() ; j++){
                    if(cart.get(j).getId() == i){
                        tmp += cart.get(j).getQty();
                    }
                }
            } else if (idsInCart.get(i) == 0){ //カート内になし
            } else if (idsInCart.get(i) == 1){ //ID=iに重複なし
                for(int j=0 ; j<cart.size() ; j++){
                    if(cart.get(j).getId() == i){
                        tmp = cart.get(j).getQty();
                    }
                }
            }
            qtysInCart.add(tmp);
        }
        for(int i=0 ; i<qtysInCart.size() ; i++){
            if(qtysInCart.get(i) != 0){ //カート内に存在するID
                for(int j=0 ; j<cart.size() ; j++){
                    if(cart.get(j).getId() == i){
                        Product product = new Product(qtysInCart.get(i), cart.get(j).getMaxQty(), cart.get(j).getPrice(), cart.get(j).getName(), i);
                        newCart.add(product);
                        break;
                    }
                }
            }
        }
        cart = newCart;
    }



    public void canselCart(){
        for(int i=0 ; i<cart.size() ; i++){
            for(int j=0 ; j<products.size() ; j++){
                if(Objects.equals(cart.get(i).getName(), products.get(j).getName())) {
                    products.get(j).setQty(products.get(j).getQty() + cart.get(i).getQty());
                }
            }
        }
        cart.clear();
    }



    public void clearCart(){
        cart.clear();
    }


    public int allCartView(){ //カート内productの一覧
        if(cart.size() == 0){
            System.out.println("\n!! カートに商品が入っていません !!");
            return 0;
        }else {
            int sum = 0;
            System.out.println("\n商品名   価格    個数   金額");
            for (Product product : cart) {
                System.out.println(product.getName() + "   " + product.getPrice() + "円   " +
                        product.getQty() + "個   " + product.getPrice() * product.getQty() + "円");
                sum += product.getPrice() * product.getQty();
            }
            System.out.println("会計：" + sum + "円");
            bill = sum;
            return 1;
        }
    }


    public boolean cashing(int paid){ //決済
        if((paid-bill) < 0){ //金額不足
            System.out.println("!! 金額が" + (bill-paid) + "円不足しています !!");
            return false;
        }else if((paid-bill) == 0){ //釣銭なし
            System.out.println("決済が完了しました");
            return true;
        }else{
            System.out.println("釣銭：" + (paid-bill) + "円\n決済が完了しました");
            return true;
        }
    }


    public void addSellerHistory(){
        ArrayList<Product> tmpCart = new ArrayList<>();
        tmpCart.addAll(cart);
        sellerHistory.add(tmpCart);
        sellerDateHistory.add(getDate());
    }


    public void viewSellerHistory(){
        if(sellerHistory.size() == 0) {
            System.out.println("\n!! 販売履歴がありません !!");
        } else {
            for (int i=0 ; i<sellerHistory.size() ; i++) {
                System.out.println("\n***" + sellerDateHistory.get(i) + "***\n商品名  個数");
                for (int j=0 ; j<sellerHistory.get(i).size() ; j++) {
                    System.out.println(sellerHistory.get(i).get(j).getName() + "  " +
                            sellerHistory.get(i).get(j).getQty());
                }
            }
            System.out.println("**********************************");
        }
        System.out.println("");
    }


    public Date getDate(){
        Date date = new Date();
        return date;
    }


}
