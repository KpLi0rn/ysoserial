public class Demo {
    public static void main(String[] args) {
        String input = "cbc:kPH+bIxk5D2deZiIxcaaaA==&./demo.ser";
        int pos = input.indexOf("&");
        System.out.println(input.substring(0,pos));

        System.out.println(input.substring(pos+1));
    }
}
