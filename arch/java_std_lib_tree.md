# Java Standard Library (JDK) Package Tree
```scheme
java
 ├── lang        (core language classes – always imported)
 │    ├── Object
 │    ├── String
 │    ├── System
 │    ├── Math
 │    ├── Integer, Double, Boolean, Character
 │    ├── Thread, Runnable
 │    └── Exception, RuntimeException
 │
 ├── util        (utilities, collections, helpers)
 │    ├── ArrayList, LinkedList
 │    ├── HashMap, HashSet, TreeMap
 │    ├── Date, Calendar
 │    ├── Scanner
 │    └── Random
 │
 ├── io          (input/output)
 │    ├── File
 │    ├── FileReader, FileWriter
 │    ├── BufferedReader, BufferedWriter
 │    ├── InputStream, OutputStream
 │    └── PrintWriter
 │
 ├── time        (modern date/time API – since Java 8)
 │    ├── LocalDate
 │    ├── LocalDateTime
 │    ├── Duration
 │    └── Period
 │
 ├── sql         (databases / JDBC)
 │    ├── Connection
 │    ├── ResultSet
 │    ├── Statement, PreparedStatement
 │    └── SQLException
 │
 ├── net         (networking)
 │    ├── URL
 │    ├── Socket
 │    └── HttpURLConnection
 │
 └── xxxxx
      (many other packages like `javax.swing` for GUI, `javax.xml` for XML, etc.)
```