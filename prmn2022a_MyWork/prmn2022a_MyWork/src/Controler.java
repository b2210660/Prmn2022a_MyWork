import java.util.Scanner;

public class Controler {
    Scanner input = new Scanner(System.in);
    private ProductManager pm;

    public Controler(ProductManager pm) {
        this.pm = pm;

    }


    public void control(){
        loop1: while(true){
            pm.allStockView();
            System.out.print("0:戻る  1:手動在庫調整  2:商品検索  3:販売実績  -->");
            int control1 = input.nextInt();
            if(control1 == 1){
                System.out.print("ID:");
                int a = input.nextInt();
                System.out.print("補充個数:");
                int b = input.nextInt();
                pm.manualStock(a, b);
            } else if (control1 == 2){
                loop2: while(true) {
                    System.out.print("0:戻る  1:ID検索  -->");
                    int control2 = input.nextInt();
                    if(control2 == 1){
                        System.out.println("ID:");
                        int a = input.nextInt();
                        pm.searchWithId(a);
                        break loop2;
                    } else if (control2 == 0){
                        break loop2;
                    } else {
                        System.out.println("error");
                    }
                }
            } else if (control1 == 3){
                pm.viewSellerHistory();
            } else if (control1 == 0) {
                break;
            } else {
                System.out.println("error");
            }
        }
    }
}
