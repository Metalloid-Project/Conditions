package com.github.metalloid.webdriver.utils.conditions;

import com.github.metalloid.logging.Logger;
import com.github.metalloid.pagefactory.controls.Control;
import com.github.metalloid.webdriver.utils.ListUtils;
import com.github.metalloid.webdriver.utils.Wait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConditionEvaluator {
	private static final Logger LOGGER = new Logger(ConditionEvaluator.class);

	public static void evaluate(WebDriver driver, Object page) {
		Field[] fields = page.getClass().getDeclaredFields();
		for (Field field : fields) {
			ExpectedCondition annotation = field.getAnnotation(ExpectedCondition.class);
			if (annotation != null) {
				Class<? extends Condition> conditionClass = annotation.condition();

				List<WebElement> webElementsToEvaluate = null;

				Object variable;

				try {
					variable = field.get(page);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(String.format("Cannot get instance of field: %s IllegalAccessException: %s", field.getName(), e.getMessage()));
				}
				if (isWebElement(variable)) {
					LOGGER.debug("Field [%s] under evaluation is a WebElement", field.getName());
					webElementsToEvaluate = Collections.singletonList((WebElement) variable);
				} else if (isControl(variable)) {
					LOGGER.debug("Field [%s] under evaluation is a Control", field.getName());
					webElementsToEvaluate = Collections.singletonList(((Control) variable).element());
				} else if (isList(variable)) {
					LOGGER.debug("Field [%s] under evaluation is a List<?>", field.getName());
					Class<?> genericClass = ListUtils.getListType(field);
					LOGGER.debug("Field [%s] is a List<?>. Generic argument is [%s]", field.getName(), genericClass.getSimpleName());
					if (isControl(genericClass)) {
						LOGGER.debug("Generic argument of List is Control");
						webElementsToEvaluate = ((List<Control>) variable).stream().map(Control::element).collect(Collectors.toList());
					} else if (isWebElement(genericClass)) {
						LOGGER.debug("Generic argument of List is WebElement");
						webElementsToEvaluate = ((List<WebElement>) variable);
					} else {
						LOGGER.debug("Nothing to evaluate");
						return;
					}
				}

				List<Condition> conditionsToEvaluate = webElementsToEvaluate.stream().map(x -> {
						try {
							return (Condition) conditionClass
									.getConstructor(WebDriver.class, WebElement.class)
									.newInstance(driver, x);
						} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
							throw new RuntimeException("Cannot get the constructor of Condition.class!");
						}
					}).collect(Collectors.toList());

				LOGGER.debug("Evaluating conditions for field: [%s]", field.getName());
				for (Condition condition : conditionsToEvaluate) {
					Wait wait = new Wait(driver);
					wait.until(annotation.timeout(), condition.getExpectedCondition());
				}
			}
		}
	}

	private static boolean isControl(Object variable) {
		return Control.class.isAssignableFrom(variable.getClass());
	}

	private static boolean isWebElement(Object variable) {
		return Control.class.isAssignableFrom(variable.getClass());
	}

	private static boolean isControl(Class<?> clazz) {
		Class<?> superclass = Objects.requireNonNull(clazz).getSuperclass();
		return superclass != null && superclass.isAssignableFrom(Control.class);
	}

	private static boolean isWebElement(Class<?> clazz) {
		return WebElement.class.equals(clazz);
	}

	private static boolean isList(Object variable) {
		return List.class.isAssignableFrom(variable.getClass());
	}
}
