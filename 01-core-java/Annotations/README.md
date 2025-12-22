# Annotations

Annotations, a form of metadata, provide data about a program that is not part of the program itself. Annotations have no direct effect on the operation of the code they annotate.

Annotations have a number of uses, among them:

* **Information for the compiler** — Annotations can be used by the compiler to detect errors or suppress warnings.
* **Compile-time and deployment-time processing** — Software tools can process annotation information to generate code, XML files, and so forth.
* **Runtime processing** — Some annotations are available to be examined at runtime.
  
This lesson explains where annotations can be used, how to apply annotations, what predefined annotation types are available in the Java Platform, Standard Edition (Java SE API), how type annotations can be used in conjunction with pluggable type systems to write code with stronger type checking, and how to implement repeating annotations.

## Annotations Basics
### The Format of an Annotation
In its simplest form, an annotation looks like the following:

```java
@Entity
```
The at sign character (@) indicates to the compiler that what follows is an annotation. In the following example, the annotation's name is Override:

```java
@Override
void mySuperMethod() { ... }
```

The annotation can include elements, which can be named or unnamed, and there are values for those elements:
```java
@Author(
   name = "Benjamin Franklin",
   date = "3/27/2003"
)
class MyClass { ... }
```

or

```java
@SuppressWarnings(value = "unchecked")
void myMethod() { ... }
```

If there is just one element named value, then the name can be omitted, as in:
```java
@SuppressWarnings("unchecked")
void myMethod() { ... }
```

If the annotation has no elements, then the parentheses can be omitted, as shown in the previous @Override example.

It is also possible to use multiple annotations on the same declaration:

```java
@Author(name = "Jane Doe")
@EBook
class MyClass { ... }
```
If the annotations have the same type, then this is called a repeating annotation:

```java
@Author(name = "Jane Doe")
@Author(name = "John Smith")
class MyClass { ... }
```

Repeating annotations are supported as of the Java SE 8 release.

The annotation type can be one of the types that are defined in the java.lang or java.lang.annotation packages of the Java SE API. In the previous examples, Override and SuppressWarnings are predefined Java annotations. It is also possible to define your own annotation type. The Author and Ebook annotations in the previous example are custom annotation types.

#### Where Annotations Can Be Used
Annotations can be applied to declarations: declarations of classes, fields, methods, and other program elements. When used on a declaration, each annotation often appears, by convention, on its own line.

* As of the Java SE 8 release, annotations can also be applied to the use of types. Here are some examples:
    ```java
    Class instance creation expression:
        new @Interned MyObject();
    ```

* Type cast:
    ```java
    myString = (@NonNull String) str;
    ```
* implements clause:
    ```java
    class UnmodifiableList<T> implements
        @Readonly List<@Readonly T> { ... }
    ```
* Thrown exception declaration:
    ```java
    void monitorTemperature() throws
        @Critical TemperatureException { ... }
    ```

This form of annotation is called a type annotation. For more information, see Type Annotations and Pluggable Type Systems.

## Declaring an Annotation Type
Many annotations replace comments in code.

Suppose that a software group traditionally starts the body of every class with comments providing important information:

```java
public class Generation3List extends Generation2List {

   // Author: John Doe
   // Date: 3/17/2002
   // Current revision: 6
   // Last modified: 4/12/2004
   // By: Jane Doe
   // Reviewers: Alice, Bill, Cindy

   // class code goes here

}
```

To add this same metadata with an annotation, you must first define the annotation type. The syntax for doing this is:

```java
@interface ClassPreamble {
   String author();
   String date();
   int currentRevision() default 1;
   String lastModified() default "N/A";
   String lastModifiedBy() default "N/A";
   // Note use of array
   String[] reviewers();
}
```

The annotation type definition looks similar to an interface definition where the keyword interface is preceded by the at sign (@) (@ = AT, as in annotation type). Annotation types are a form of interface, which will be covered in a later lesson. For the moment, you do not need to understand interfaces.

The body of the previous annotation definition contains annotation type element declarations, which look a lot like methods. Note that they can define optional default values.

After the annotation type is defined, you can use annotations of that type, with the values filled in, like this:

```java
@ClassPreamble (
   author = "John Doe",
   date = "3/17/2002",
   currentRevision = 6,
   lastModified = "4/12/2004",
   lastModifiedBy = "Jane Doe",
   // Note array notation
   reviewers = {"Alice", "Bob", "Cindy"}
)
public class Generation3List extends Generation2List {

// class code goes here

}
```

> Note: To make the information in @ClassPreamble appear in Javadoc-generated documentation, you must annotate the @ClassPreamble definition with the @Documented annotation:
> 
> ```java
> // import this to use @Documented
> import java.lang.annotation.*;
> 
> @Documented
> @interface ClassPreamble {
> 
>    // Annotation element definitions
>    
> }
> ```

## Predefined Annotation Types
