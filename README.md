# Conditions

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.metalloid-project/conditions/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.metalloid-project/conditions)

Maven dependency:
```
<!-- https://mvnrepository.com/artifact/com.github.metalloid-project/conditions -->
<dependency>
    <groupId>com.github.metalloid-project</groupId>
    <artifactId>conditions</artifactId>
    <version>1.0.2</version>
</dependency>
```

Compiled with `Java 1.8`

Already contains dependency:
```
<dependency>
	<groupId>com.github.metalloid-project</groupId>
	<artifactId>webdriver-utils</artifactId>
	<version>1.0.2</version>
</dependency>
```

The library allows you to use @ExpectedCondition annotation on `WebElement` or custom `Control` to achieve expected state of a Page Object at initialization.

In order to create your own conditions, all you have to do is create a class and extend Conditions.class :)

Read more details with examples in `Wiki` sections.
