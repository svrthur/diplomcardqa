package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class CreditPage extends CardPage {

    public CreditPage() {
        super();
        SelenideElement howToPayChoiceHeading = $$("h3").find(text("Кредит"));
        howToPayChoiceHeading.shouldHave(exactText("Кредит по данным карты"));
    }
}
