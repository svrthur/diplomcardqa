package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$$;

public class CardPage {

    private SelenideElement cardNumberField = $$(".input__inner").findBy(text("Номер карты")).$("input");
    private SelenideElement monthField = $$(".input__inner").findBy(text("Месяц")).$("input");
    private SelenideElement yearField = $$(".input__inner").findBy(text("Год")).$("input");
    private SelenideElement cardOwnerField = $$(".input__inner").findBy(text("Владелец")).$("input");
    private SelenideElement cvvField = $$(".input__inner").findBy(text("CVC/CVV")).$("input");
    private SelenideElement sendRequestButton = $$("button").findBy(text("Продолжить"));

    private SelenideElement successOperationNotification = $$(".notification__content").findBy(text("Операция одобрена Банком."));
    private SelenideElement failOperationNotification = $$(".notification__content").findBy(text("Ошибка! Банк отказал в проведении операции."));

    private SelenideElement verificationErrorNumber = $$(".input__inner").findBy(text("Номер карты")).$(".input__sub");
    private SelenideElement verificationErrorMonth = $$(".input__inner").findBy(text("Месяц")).$(".input__sub");
    private SelenideElement verificationErrorYear = $$(".input__inner").findBy(text("Год")).$(".input__sub");
    private SelenideElement verificationErrorOwner = $$(".input__inner").findBy(text("Владелец")).$(".input__sub");
    private SelenideElement verificationErrorCVV = $$(".input__inner").findBy(text("CVC/CVV")).$(".input__sub");


    public void fillInCardInfo(String cardNumber, String month, String year, String cardOwner, String cvv) {
        cardNumberField.sendKeys(cardNumber);
        monthField.sendKeys(month);
        yearField.sendKeys(year);
        cardOwnerField.sendKeys(cardOwner);
        cvvField.sendKeys(cvv);
        sendRequestButton.click();
    }

    public void fillInCardNumber(String cardNumber) {
        cardNumberField.sendKeys(cardNumber);
    }

    public void fillInMonth(String month) {
        monthField.sendKeys(month);
    }

    public void fillInYear(String year) {
        yearField.sendKeys(year);
    }

    public void fillInOwner(String owner) {
        cardOwnerField.sendKeys(owner);
    }

    public void fillInCVV(String CVV) {
        cvvField.sendKeys(CVV);
    }

    public void checkIfSuccess() {
        successOperationNotification.shouldBe(Condition.visible, Duration.ofSeconds(20));
    }

    public void checkIfFail() {
        failOperationNotification.shouldBe(Condition.visible, Duration.ofSeconds(20));
    }

    public void checkCardNumberText(String text) {
        verificationErrorNumber.shouldHave(text(text), Duration.ofSeconds(3)).shouldBe(visible);
    }

    public void checkMonthText(String text) {
        verificationErrorMonth.shouldHave(text(text), Duration.ofSeconds(3)).shouldBe(visible);
    }

    public void checkYearText(String text) {
        verificationErrorYear.shouldHave(text(text), Duration.ofSeconds(3)).shouldBe(visible);
    }

    public void checkOwnerText(String text) {
        verificationErrorOwner.shouldHave(text(text), Duration.ofSeconds(3)).shouldBe(visible);
    }

    public void checkCVVText(String text) {
        verificationErrorCVV.shouldHave(text(text), Duration.ofSeconds(3)).shouldBe(visible);
    }

    public void emptyCardNumberInField() {
        cardNumberField.shouldBe(empty);
    }

    public void emptyMonthInField() {
        monthField.shouldBe(empty);
    }

    public void emptyYearInField() {
        yearField.shouldBe(empty);
    }

    public void emptyCardOwnerInField() {
        cardOwnerField.shouldBe(empty);
    }

    public void emptyCVVInField() {
        cvvField.shouldBe(empty);
    }
}
