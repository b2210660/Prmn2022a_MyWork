import java.util.ArrayList;

public class Te {
    public static void main(String[] args) {
        ArrayList<String> str = new ArrayList<>();
        str.add("a");
        str.add("b");
        str.add("c");
        str.add("d");
        str.add("e");

        for(int i=0 ; i<str.size() ; i++){
            System.out.print(" " + i + ":" + str.get(i));
        }
        str.remove(2);
        for(int i=0 ; i<str.size() ; i++){
            System.out.print(" " + i + ":" + str.get(i));
        }
        str.add(3, "X");
        for(int i=0 ; i<str.size() ; i++){
            System.out.print(" " + i + ":" + str.get(i));
        }
    }
}
