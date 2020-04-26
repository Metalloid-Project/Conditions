# Conditions

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.metalloid-project/metalloid-expected-conditions/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.metalloid-project/metalloid-expected-conditions)

Maven dependency:
```
<!-- https://mvnrepository.com/artifact/com.github.metalloid-project/conditions -->
<dependency>
    <groupId>com.github.metalloid-project</groupId>
    <artifactId>metalloid-expected-conditions</artifactId>
    <version>2.2.0</version>
</dependency>
```

Compiled with `Java 1.8`

Already contains dependency:
```
<dependency>
	<groupId>com.github.metalloid-project</groupId>
	<artifactId>metalloid-utils</artifactId>
	<version>2.2.0</version>
</dependency>
```

The library allows you to use `@ExpectedCondition` annotation on `WebElement` or custom `Control` to achieve expected state of a Page Object at initialization.

In order to create your own conditions, all you have to do is create a class and extend `Conditions.class`.

Read more details with examples in `Wiki` sections.
