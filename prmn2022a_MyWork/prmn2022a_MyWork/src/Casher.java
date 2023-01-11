import java.util.Scanner;

public class Casher {
    Scanner input = new Scanner(System.in);
    private ProductManager pm;

    public Casher(ProductManager pm) {
        this.pm = pm;

    }

    public void cash() {
        loop1 : while (true) {
            System.out.println("");
            pm.allStockView();
            System.out.print("0:戻る　1:カートに追加  2:会計確認  -->");
            int control1 = input.nextInt();
            if (control1 == 1) {
                System.out.print("商品ID：");
                int a = input.nextInt();
                System.out.print("購入数：");
                int b = input.nextInt();
                pm.addCart(a, b);
            } else if (control1 == 2) {
                loop2 : while(true) {
                    pm.summarizeCart();
                    if(pm.allCartView() == 0){
                        break;
                    }
                    System.out.print("0:戻る　1:支払いに進む　2:カート内変更　9:キャンセル　-->");
                    int control2 = input.nextInt();
                    if(control2 == 1){
                        loop3 : while(true) {
                            System.out.print("支払い額を入力(0:戻る　<0:キャンセル)  -->");
                            int control3 = input.nextInt();
                            if (control3 == 0) {
                                break;
                            } else if (control3 < 0) {
                                control1 = 0;
                                break loop2;
                            } else {
                                if(pm.cashing(control3)){
                                    pm.addSellerHistory();
                                    break loop1;
                                }
                            }
                        }
                    }else if(control2 == 0){
                        break;
                    }else if(control2 == 2){
                        pm.allCartViewWithNumber();
                        System.out.print("商品番号：");
                        int a = input.nextInt();
                        System.out.print("購入個数：");
                        int b = input.nextInt();
                        pm.fixCart(a, b);
                    }else if(control2 == 9){
                        control1 = 0;
                        break;
                    }else{
                        System.out.println("error");
                    }
                }
            }
            if (control1 == 0) { //カート内のクリア
                pm.canselCart();
                break;
            } else if (control1 != 1  &&  control1 != 2) {
                System.out.println("error");
            }
        }
        pm.clearCart();
    }
}