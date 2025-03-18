package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class MainPage {
    private String host = "http://localhost:8080";
    private SelenideElement heading = $(".heading_size_l");
    private SelenideElement buyButton = $(byText("Купить")).parent().parent();
    private SelenideElement buyInCreditButton = $(byText("Купить в кредит"));

    public MainPage() {
        open(host);
        heading.shouldBe(visible);
    }

    public DebitPage goToDebitPage() {
        buyButton.click();
        return new DebitPage();
    }

    public CreditPage goToCreditPage() {
        buyInCreditButton.click();
        return new CreditPage();
    }
}
