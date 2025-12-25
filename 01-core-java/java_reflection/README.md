Java Reflection makes it possible to inspect classes, interfaces, fields and methods at runtime, without knowing the names of the classes, methods etc. at compile time. It is also possible to instantiate new objects, invoke methods and get/set field values using reflection.

Java Reflection is quite powerful and can be very useful. For instance, Java Reflection can be used to map properties in JSON files to getter / setter methods in Java objects, like Jackson, GSON, Boon etc. does. Or, Reflection can be used to map the column names of a JDBC ResultSet to getter / setter methods in a Java object.

This tutorial will get into Java reflection in depth. It will explain the basics of Java Reflection including how to work with arrays, annotations, generics and dynamic proxies, and do dynamic class loading and reloading.

It will also show you how to do more specific Java Reflection tasks, like reading all getter methods of a class, or accessing private fields and methods of a class.

This Java Reflection tutorial will also clear up some of the confusion out there about what Generics information is available at runtime. Some people claim that all Generics information is lost at runtime. This is not true.

This tutorial describes the version of Java Reflection found in Java 8.

Java Reflection Example:
```java
Method[] methods = MyObject.class.getMethods();

for(Method method : methods){
    System.out.println("method = " + method.getName());
}
```
[Example01.java](./Example01.java)

This example obtains the Class object from the class called MyObject. Using the class object the example gets a list of the methods in that class, iterates the methods and print out their names.

