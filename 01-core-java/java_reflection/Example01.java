import java.lang.reflect.Method;

public class Example01 {

    public class MyClass {

        final private String message;

        public MyClass(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public static void main(String[] args) {

        Method[] methods = MyClass.class.getMethods();

        for (Method method : methods) {
            System.out.println("method = " + method.getName());
        }

    }
}
