public class Array {

    public static void main(String[] args) {

        int[] arr1 = new int[5];
        arr1[0] = 1;
        arr1[1] = 2;
        arr1[2] = 3;
        arr1[3] = 4;
        arr1[4] = 5;

        System.out.println(arr1[0]);
        System.out.println(arr1[1]);
        System.out.println(arr1[2]);
        System.out.println(arr1[3]);
        System.out.println(arr1[4]);

        String[][] name = {
            {"John", "Paul", "George", "Ringo"},
            {"John", "Paul", "George", "Ringo"},
            {"John", "Paul", "George", "Ringo"}
        };

        System.out.println(name[0][0]);
        System.out.println(name[0][1]);
        System.out.println(name[0][2]);
        System.out.println(name[0][3]);
    }
}