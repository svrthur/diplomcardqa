package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$$;

public class DebitPage extends CardPage {
    public DebitPage() {
        super();
        SelenideElement howToPayChoiceHeading = $$("h3").find(text("Оплата"));
        howToPayChoiceHeading.shouldHave(exactText("Оплата по карте"));
    }

}