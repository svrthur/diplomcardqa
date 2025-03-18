package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.data.DBConnector;
import ru.netology.data.DataGenerator;
import ru.netology.page.DebitPage;
import ru.netology.page.MainPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DebitCardTest {

    private MainPage mainPage;
    private DebitPage debitPage;

    @BeforeAll
    static void setUp() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDown() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    void setUpMainPage() {
        mainPage = new MainPage();
    }

    @SneakyThrows
    @Test
    //Вручную проходит
    @DisplayName("1. Покупка с оплатой дебетовой картой со статусом APPROVED: отправка формы в введенными во все поля валидными данными")
    void shouldPassWithApprovedDebitCard() {
        debitPage = mainPage.goToDebitPage();
        DBConnector.PaymentData beforeRequest = DBConnector.getLastPaymentData("payment_entity");
        debitPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        debitPage.checkIfSuccess();
        DBConnector.PaymentData afterRequest = DBConnector.getLastPaymentData("payment_entity");
        assertNotEquals(beforeRequest.getId(), afterRequest.getId());
        assertEquals("APPROVED", afterRequest.getStatus());
    }

    @SneakyThrows
    @Test
    //Вручную не проходит, уведомление об успешной операции
    @DisplayName("2. Покупка по карте со статусом DECLINED")
    void shouldFailWithDeclinedDebitCard() {
        debitPage = mainPage.goToDebitPage();
        DBConnector.PaymentData beforeRequest = DBConnector.getLastPaymentData("payment_entity");
        debitPage.fillInCardInfo(DataGenerator.getDeclinedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        debitPage.checkIfFail();
        DBConnector.PaymentData afterRequest = DBConnector.getLastPaymentData("payment_entity");
        assertNotEquals(beforeRequest.getId(), afterRequest.getId());
        assertEquals("DECLINED", afterRequest.getStatus());
    }

    @Test
    //Вручную не проходит, тексты уведомлений должны быть иными
    @DisplayName("3. Отправка пустой формы")
    void shouldThrowAllVerificationErrorsDebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardInfo(DataGenerator.getEmptyCardNumber(), DataGenerator.getEmptyMonth(), DataGenerator.getEmptyYear(), DataGenerator.getEmptyOwner(), DataGenerator.getEmptyCVV());
        debitPage.checkCardNumberText("Поле обязательно для заполнения");
        debitPage.checkMonthText("Поле обязательно для заполнения");
        debitPage.checkYearText("Поле обязательно для заполнения");
        debitPage.checkOwnerText("Поле обязательно для заполнения");
        debitPage.checkCVVText("Поле обязательно для заполнения");
    }

    @Test
    //Вручную проходит
    @DisplayName("4. Номер карты из 16 цифр, отличный от 4444 4444 4444 4441 или 4444 4444 4444 4442")
    void shouldFailWithDebitCardNotFromList() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardInfo(DataGenerator.getCardNumberNotFromRange(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        debitPage.checkIfFail();
    }

    @Test
    //Вручную проходит
    @DisplayName("5. Номер карты из 16 нулей")
    void shouldFailWithDebitCardWithAllZeros() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardInfo(DataGenerator.getCardNumberWithAllZeros(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        debitPage.checkIfFail();
    }

    @Test
    //Вручную проходит
    @DisplayName("6. Номер карты, состоящий из менее чем 16 цифр")
    void shouldThrowWrongFormatCardNumberVerificationErrorDebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardInfo(DataGenerator.getCardNumberWithLessNumbers(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        debitPage.checkCardNumberText("Неверный формат");
    }

    @Test
    //Вручную не проходит, текст уведомления должен быть иной
    @DisplayName("7. Пустой номер карты")
    void shouldThrowEmptyCardNumberVerificationErrorDebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardInfo(DataGenerator.getEmptyCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        debitPage.checkCardNumberText("Поле обязательно для заполнения");
    }

    @Test
    //Вручную проходит
    @DisplayName("8. В поле 'Номер карты' введены буквы и специальные символы")
    void shouldNotEnterLettersAndSymbolsInCardNumberDebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardNumber(DataGenerator.getCardNumberWithLettersAndSymbols());
        debitPage.emptyCardNumberInField();
    }

    @Test
    //Вручную проходит
    @DisplayName("9. Месяц, больше 12")
    void shouldThrowInvalidExpDateMonthVerificationErrorWithMoreThan12DebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getMonthWithMoreThan12(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        debitPage.checkMonthText("Неверно указан срок действия карты");
    }

    @Test
    //Вручную проходит
    @DisplayName("10. Месяц из 1 цифры")
    void shouldThrowWrongFormatMonthVerificationErrorDebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getMonthWith1Symbol(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        debitPage.checkMonthText("Неверный формат");
    }

    @Test
    //Вручную не проходит, текст уведомления должен быть иной
    @DisplayName("11. Пустое поле 'Месяц'")
    void shouldThrowEmptyMonthVerificationErrorDebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getEmptyMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        debitPage.checkMonthText("Поле обязательно для заполнения");
    }

    @Test
    //Вручную не проходит, форма отправляется без ошибок
    @DisplayName("12. Месяц из двух нулей")
    void shouldThrowInvalidExpDateMonthVerificationErrorWith00DebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getMonthWith00(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        debitPage.checkMonthText("Неверно указан срок действия карты");
    }

    @Test
    //Вручную проходит
    @DisplayName("13. В поле 'Месяц' введены буквы и специальные символы")
    void shouldNotEnterLettersAndSymbolsInMonthDebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInMonth(DataGenerator.getMonthWithLettersAndSymbols());
        debitPage.emptyMonthInField();
    }

    @Test
    //Вручную проходит
    @DisplayName("14. Год меньше текущего")
    void shouldThrowInvalidExpDateYearVerificationErrorLessThanCurrentDebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getYearLessThanCurrent(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        debitPage.checkYearText("Истёк срок действия карты");
    }

    @Test
    //Вручную проходит
    @DisplayName("15. Год из 1 цифры")
    void shouldThrowWrongFormatYearVerificationErrorDebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getYearWith1Symbol(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        debitPage.checkYearText("Неверный формат");
    }

    @Test
    //Вручную не проходит, текст уведомления должен быть иной
    @DisplayName("16. Пустое поле 'Год'")
    void shouldThrowEmptyYearVerificationErrorDebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getEmptyYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        debitPage.checkYearText("Поле обязательно для заполнения");
    }

    @Test
    //Вручную проходит
    @DisplayName("17. Год из двух нулей")
    void shouldThrowInvalidExpDateYearVerificationErrorWith00DebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getYearWith00(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        debitPage.checkYearText("Истёк срок действия карты");
    }
    @Test
    //Вручную проходит
    @DisplayName("18. В поле 'Год' введены буквы и специальные символы")
    void shouldNotEnterLettersAndSymbolsInYearDebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInYear(DataGenerator.getYearWithLettersAndSymbols());
        debitPage.emptyYearInField();
    }

    @Test
    //Вручную не проходит, поле принимает любые значения
    @DisplayName("19. В поле 'Владелец' введены кириллические символы")
    void shouldThrowWrongFormatOwnerVerificationErrorDebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getOwnerWithCyrillicLetters(), DataGenerator.getApprovedCVV());
        debitPage.checkOwnerText("Неверный формат");
    }

    @Test
    //Вручную не проходит, поле принимает любые значения
    @DisplayName("20. В поле 'Владелец' введены цифры и специальные символы кроме дефиса, пробела и апострофа")
    void shouldNotEnterLettersAndSymbolsInOwnerDebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInOwner(DataGenerator.getOwnerWithSymbols());
        debitPage.emptyCardOwnerInField();
    }

    @Test
    //Вручную проходит
    @DisplayName("21. Пустое поле 'Владелец'")
    void shouldThrowEmptyOwnerVerificationErrorDebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getEmptyOwner(), DataGenerator.getApprovedCVV());
        debitPage.checkOwnerText("Поле обязательно для заполнения");
    }

    @Test
    //Вручную проходит
    @DisplayName("22. Поле 'CVC/CVV' из 1 или 2 цифр")
    void shouldThrowWrongFormatCVVVerificationErrorDebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getCVVWith2Symbols());
        debitPage.checkCVVText("Неверный формат");
    }

    @Test
    //Вручную не проходит, текст уведомления должен быть иной
    @DisplayName("23. Пустое поле 'CVC/CVV'")
    void shouldThrowEmptyCVVVerificationErrorDebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getEmptyCVV());
        debitPage.checkCVVText("Поле обязательно для заполнения");
    }

    @Test
    //Вручную не проходит, форма отправляется без ошибок
    @DisplayName("24. Поле 'CVC/CVV' из трех нулей")
    void shouldThrowInvalidCVVVerificationErrorDebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getCVVWith00());
        debitPage.checkCVVText("Неверное значение");
    }

    @Test
    //Вручную проходит
    @DisplayName("25. В поле 'CVC/CVV' введены буквы и специальные символы")
    void shouldNotEnterLettersAndSymbolsInCVVDebitCard() {
        debitPage = mainPage.goToDebitPage();
        debitPage.fillInCVV(DataGenerator.getCVVWithLettersAndSymbols());
        debitPage.emptyCVVInField();
    }
}
