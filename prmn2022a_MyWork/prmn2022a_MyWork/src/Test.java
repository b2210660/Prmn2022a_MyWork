import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        try {
            Scanner input = new Scanner(System.in);
            FileManager fileManager = new FileManager("products.txt");
            String[] data = fileManager.getAsArray();
            ProductManager pm = new ProductManager(data);
            Casher casher = new Casher(pm);

            while(true){ //ホーム
                System.out.print("1:レジ  2:商品管理  3:終了  -->");
                int control1 = input.nextInt();
                if(control1 == 1){
                    casher.cash();
                }else if(control1 == 2){
                    System.out.println("未実装");
                }else if(control1 == 3){
                    break;
                }else{
                    System.out.println("error");
                }
            }

        } catch (IllegalArgumentException e1) {
            System.out.println("File Error");
        } catch (IOException e2) {
            System.out.println("error");
        } catch (InputMismatchException e3){
            System.out.println("Input Mismatch");
        } finally {
            System.out.println("終了します");
        }

    }

}