package com.github.metalloid.webdriver.utils.conditions;

import com.github.metalloid.pagefactory.controls.Control;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.List;
import java.util.regex.Pattern;

public class ControlConditions {

    public static <T extends Control> ExpectedCondition<T> presenceOfControl(final T control) {
        return driver -> {
            if (control.exists()) {
                return control;
            } else {
                return null;
            }
        };
    }

    public static <T extends Control> ExpectedCondition<Boolean> isDisplayed(final T control) {
        return driver -> control.isDisplayed();
    }

    public static <T extends Control> ExpectedCondition<Boolean> isInvisible(final T control) {
        return driver -> !control.isDisplayed();
    }

    public static <T extends Control> ExpectedCondition<Boolean> textToBePresentInControl(final T control, final String text) {
        return driver -> {
            String controlText = control.getText();
            return controlText.contains(text);
        };
    }

    public static <T extends Control> ExpectedCondition<Boolean> textToBePresentInControlValue(final T control, final String text) {
        return driver -> {
            String elementText = control.element().getAttribute("value");
            if (elementText != null) {
                return elementText.contains(text);
            }
            return false;
        };
    }

    public static <T extends Control> ExpectedCondition<T> controlToBeClickable(final T control) {
        return driver -> {
            T element = presenceOfControl(control).apply(driver);
            if (element != null && element.element().isEnabled()) {
                return element;
            }
            return null;
        };
    }

    public static <T extends Control> ExpectedCondition<List<T>> visbilityOfAllControls(final List<T> controls) {
        return driver -> {
            for (T control : controls) {
                if (!control.isDisplayed()) {
                    return null;
                }
            }
            return controls.size() > 0 ? controls : null;
        };
    }

    public static <T extends Control> ExpectedCondition<List<T>> presenceOfAllControls(final List<T> controls) {
        return driver -> {
            for (T control : controls) {
                if (!control.exists()) {
                    return null;
                }
            }
            return controls.size() > 0 ? controls : null;
        };
    }

    public static <T extends Control> ExpectedCondition<Boolean> attributeToBe(final T control, final String attribute, final String value) {
        return driver -> {
            String currentValue = control.element().getAttribute(attribute);
            if (currentValue == null||currentValue.isEmpty()) {
                currentValue = control.element().getCssValue(attribute);
            }
            return value.equals(currentValue);
        };
    }

    public static <T extends Control> ExpectedCondition<Boolean> textMatches(final T control, final Pattern pattern) {
        return driver -> {
            try {
                String currentValue = control.getText();
                return pattern.matcher(currentValue).find();
            } catch (Exception e) {
                return false;
            }
        };
    }
}
