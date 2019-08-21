package com.github.metalloid.webdriver.utils.conditions;

import com.github.metalloid.pagefactory.controls.Control;
import com.github.metalloid.webdriver.utils.Wait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ConditionEvaluator {

	public static void evaluate(WebDriver driver, Object page) {
		Field[] fields = page.getClass().getDeclaredFields();
		for (Field field : fields) {
			ExpectedCondition annotation = field.getAnnotation(ExpectedCondition.class);
			if (annotation != null) {
				Class<? extends Condition> conditionClass = annotation.condition();

				WebElement elementInstance = null;

				Object variable;

				try {
					variable = field.get(page);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(String.format("Cannot get instance of field: %s IllegalAccessException: %s", field.getName(), e.getMessage()));
				}
				if (WebElement.class.isAssignableFrom(variable.getClass())) {
					elementInstance = (WebElement) variable;
				} else if (Control.class.isAssignableFrom(variable.getClass())) {
					elementInstance = ((Control) variable).element();
				}

				Condition condition;
				try {
					condition = conditionClass
							.getConstructor(WebDriver.class, WebElement.class)
							.newInstance(driver, elementInstance);
				} catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
					throw new RuntimeException("Cannot get the constructor of Condition.class!");
				}

				Wait wait = new Wait(driver);
				wait.until(annotation.timeout(), condition.getExpectedCondition());
			}
		}
	}
}
