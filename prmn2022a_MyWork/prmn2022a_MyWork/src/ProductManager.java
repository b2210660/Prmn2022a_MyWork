import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class ProductManager {
    Scanner input = new Scanner(System.in);
    private ArrayList<Product> products = new ArrayList<>();
    private ArrayList<Product> cart = new ArrayList<>();
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
        System.out.println("ID  商品名　　価格　　在庫");
        for (int i=0 ; i< products.size() ; i++) {
            System.out.print(products.get(i).getId() + "  ");
            products.get(i).view();
        }
    }

    public void allCartViewWithNumber(){
        System.out.println("番号  商品名　　価格　　在庫");
        for (int i=0 ; i< cart.size() ; i++) {
            System.out.print((i + 1) + "   ");
            cart.get(i).view();
        }
    }

    public void search(int id){ //商品検索
        products.get(id-1).view();
    }

    public void autoStock(){ //自動在庫補充(最大在庫から判定)
        boolean changed = false;
        for (Product product : products) {
            int value = product.getMaxQty() / 2;
            if (product.getQty() < value) { //最大在庫の半数以下で自動入荷
                product.setQty(product.getQty() + value);
                System.out.println(product.getName() + "を" + value + "個入荷しました");
                changed = true;
            }
        }
        if(changed){
            System.out.println("*****変更後在庫*****");
            allStockView();
        }
    }

    public void manualStock(int id, int adQty){ //手動在庫補充
        int YesNo = 1;
        int newStock = products.get(id-1).getQty() + adQty;
        if(newStock > products.get(id-1).getMaxQty()) { //最大在庫以上のときアラート
            System.out.print("!! 在庫過多になる可能性があります !!\n" +
                    "最大在庫数：" + products.get(id-1).getMaxQty() + "  変更後在庫：" + newStock +
                    "\n入荷を実行しますか？　　1:Yes 9:No  -->");
            YesNo = input.nextInt();
        }
        if(YesNo == 1){
            products.get(id-1).setQty(newStock);
            System.out.println("*****変更後在庫*****");
            products.get(id-1).view();
        }else if(YesNo == 9){
            System.out.println("手動入荷を中止します");
        }else{
            System.out.println("error\n手動入荷を中止します");
        }
    }

    public void addCart(int id, int buyQty){ //カートに追加
        int newQty = products.get(id-1).getQty() - buyQty;
        if(newQty < 0) { //在庫以上の個数を指定したときのエラー文
            System.out.println("!! " + products.get(id-1).getName() + "の在庫は" + products.get(id-1).getQty() + "です !!");
        }else { //正常にカート追加＆在庫変更
            if (newQty == 0) {
                System.out.println(products.get(id-1).getName() + "の在庫が0になりました");
            }
            Product boughtProduct = new Product(buyQty,
                    products.get(id-1).getMaxQty(),
                    products.get(id-1).getPrice(),
                    products.get(id-1).getName(), id-1);
            cart.add(boughtProduct);
            products.get(id-1).setQty(newQty);
            addCartCnt++;
        }
    }

    public void fixCart(int id, int fixQty){ //カートの修正（削除）
        int newQty = cart.get(id-1).getQty() - fixQty; //新たなカート内個数
        if(newQty < 0){ //個数以上に削除しようとしたときのエラー文
            System.out.println("!! " + cart.get(id-1).getName() + "の個数は" + cart.get(id-1).getQty() + "です !!");
        } else if (fixQty > products.get(id).getQty()){ //在庫以上に追加しようとしたときのエラー文
            System.out.println("!! " + products.get(id-1).getName() + "の在庫は残り" + products.get(id-1).getQty() + "です !!");
        } else { //正常に変更
            if (newQty != 0) {
                Product fixedProduct = new Product(newQty,
                        cart.get(id - 1).getMaxQty(),
                        cart.get(id - 1).getPrice(),
                        cart.get(id - 1).getName(), id - 1);
                cart.set(id - 1, fixedProduct);
            } else { //カートから項目ごと削除
                cart.remove(id - 1);
            }
            products.get(id-1).setQty(products.get(id-1).getQty() + fixQty);
        }
    }

    public void fixingCart(int id, int fixedQty){
        int tmp = -1;
        if (id > (cart.size())  ||  id < 0) { //IDがカートの範囲からはみ出しているとき
            System.out.println("!! 商品番号は0~" + cart.size() + "から入力してください !!");
        } else {
            int difQty = fixedQty - cart.get(id - 1).getQty(); //カートのQtyの増加量
            for (int i = 0; i < products.size(); i++) {
                if (cart.get(id - 1).getId() == products.get(i).getId()) {
                    tmp = i;
                    break;
                }
            }
            if (fixedQty < 0 || fixedQty > (cart.get(id - 1).getQty() + products.get(tmp).getQty())) { //fixedQty異常
                System.out.println("!! 購入個数は0~" + (cart.get(id - 1).getQty() + products.get(tmp).getQty()) + "から入力してください !!");
            } else {
                if (fixedQty == 0) {
                    cart.remove(id - 1);
                } else {
                    Product fixedProduct = new Product(fixedQty,
                            cart.get(id - 1).getMaxQty(),
                            cart.get(id - 1).getPrice(),
                            cart.get(id - 1).getName(), id - 1);
                    cart.set(id - 1, fixedProduct);
                }
                products.get(tmp + 1).setQty(products.get(tmp + 1).getQty() - difQty);
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

    public int allCartView(){ //カート内productの一覧
        if(cart.size() == 0){
            System.out.println("!! カートに商品が入っていません !!");
            return 0;
        }else {
            int sum = 0;
            System.out.println("商品名　　価格　　個数   金額");
            for (Product product : cart) {
                System.out.println(product.getName() + "   " + product.getPrice() + "円  " +
                        product.getQty() + "個   " + product.getPrice() * product.getQty());
                sum += product.getPrice() * product.getQty();
            }
            System.out.println("\n会計：" + sum + "円");
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


}
