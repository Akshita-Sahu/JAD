# JAD JFR Backend - Java Flight Recorder 

 Spring Boot 3.5.3  JFR (Java Flight Recorder) ， RESTful API 。

## 

### 
- **JFR **:  JMC 8.3.1  JFR 
- ****:  17+ 
- ****: 、、
- ****: 
- **RESTful API**:  REST API 

### 
- **CPU **: CPU 、CPU 、
- ****: 、
- **I/O **: 、
- ****: 、、
- ****: 、
- ****: 、CPU 

### 
- ****: ，
- ****: ，
- ****: 
- ****:  H2  MySQL 

## 

### 
- **Spring Boot 3.5.3**: ， Java 17+
- **Java 17**: ， Java 
- **Spring Data JPA**: 
- **H2/MySQL**: （/）
- **JMC 8.3.1**: Java Mission Control，JFR 
- **Lombok**: 
- **Maven**: 

### 
- `org.openjdk.jmc:flightrecorder` - JFR 
- `org.openjdk.jmc:common` - JMC 
- `org.openjdk.jmc:flightrecorder.rules` - JFR 
- `org.springframework.boot:spring-boot-starter-web` - Web 
- `org.springframework.boot:spring-boot-starter-data-jpa` - 

## 

```
jad-jfr-backend/
├── src/main/java/org/example/jfranalyzerbackend/
│   ├── config/              # 
│   │   ├── JADConfig.java    # JAD 
│   │   ├── CorsConfig.java      # 
│   │   └── Result.java          # 
│   ├── controller/          # REST 
│   │   ├── FileController.java      #  API
│   │   └── JFRAnalysisController.java # JFR  API
│   ├── service/             # 
│   │   ├── FileService.java         # 
│   │   ├── JFRAnalysisService.java  # JFR 
│   │   ├── JFRAnalyzer.java        # JFR 
│   │   └── impl/                   # 
│   ├── extractor/           # JFR 
│   │   ├── Extractor.java          # 
│   │   ├── EventVisitor.java       # 
│   │   ├── JFRAnalysisContext.java # 
│   │   ├── *Extractor.java         # 
│   │   └── PerfDimensionFactory.java # 
│   ├── entity/              # 
│   │   ├── shared/              # 
│   │   ├── FileEntity.java       # 
│   │   └── ProfileDimension.java # 
│   ├── model/               # 
│   │   ├── AnalysisResult.java    # 
│   │   ├── FlameGraph.java        # 
│   │   ├── jfr/                  # JFR 
│   │   └── symbol/               # 
│   ├── repository/          # 
│   ├── enums/              # 
│   ├── exception/          # 
│   ├── request/            # 
│   ├── vo/                 # 
│   ├── util/               # 
│   └── JfrAnalyzerBackendApplication.java # 
└── src/main/resources/
    └── application.yml      # 
```

##  

### 
- **Java 17+**: ， Java 17 
- **Maven 3.6+**: 
- ****:  4GB+， JFR 

### （）
 H2 ， MySQL：

```yaml
# application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/jad_jfr
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    properties:
      hibernate.dialect: org.hibernate.dialect.MySQLDialect
```

### 
```bash
# 
mvn spring-boot:run

# 
mvn clean package
java -jar target/jad-jfr-backend-4.0.5.jar
```

### 
- : `http://localhost:8200`
- H2 : `http://localhost:8200/h2-console`
- API : `http://localhost:8200/api/files` ()

### 
```yaml
# 
jad:
  jfr-storage-path: ${user.home}/jad-jfr-storage  # JFR 

spring:
  servlet:
    multipart:
      max-file-size: 1GB        # 
      max-request-size: 1GB     # 
  server:
    port: 8200                  # 
```

##  

### 

1. ：
```java
@Component
public class CustomExtractor extends Extractor {
    @Override
    public String getDimensionName() {
        return "CUSTOM_DIMENSION";
    }
    
    @Override
    public void extract(RecordedEvent event, JFRAnalysisContext context) {
        // 
    }
}
```

2.  `PerfDimensionFactory` ：
```java
public static PerfDimension createCustomDimension() {
    return new PerfDimension("CUSTOM_DIMENSION", "", "ms");
}
```

### 
 H2 ， MySQL：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/jad_jfr
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
```


## 

：

- **[Java Mission Control (JMC)](https://github.com/openjdk/jmc)** - Oracle  Java 
- **[JProfiler](https://www.ej-technologies.com/products/jprofiler/overview.html)** -  Java 
- **[VisualVM](https://visualvm.github.io/)** -  Java 
- **[FlameGraph](https://github.com/brendangregg/FlameGraph)** - 
- **[Jifa](https://github.com/eclipse-jifa/jifa)** - Java 

## 

### 
- [Spring Boot ](https://spring.io/projects/spring-boot)
- [Spring Data JPA ](https://spring.io/projects/spring-data-jpa)
- [Java Mission Control ](https://github.com/openjdk/jmc)
- [JFR ](https://openjdk.org/projects/jdk/8/)

### 
- [Java Flight Recorder ](https://docs.oracle.com/en/java/javase/11/jfr/)
- [JMC ](https://www.oracle.com/java/technologies/javase/jmc.html)



