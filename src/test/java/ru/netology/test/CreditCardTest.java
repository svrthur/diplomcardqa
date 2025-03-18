package ru.netology.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import ru.netology.data.DBConnector;
import ru.netology.data.DataGenerator;
import ru.netology.page.CreditPage;
import ru.netology.page.MainPage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CreditCardTest {
    private MainPage mainPage;
    private CreditPage creditPage;

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
    @DisplayName("26. Покупка с оплатой в кредит по карте со статусом APPROVED: отправка формы с введенными во всем поля валидными данными")
    void shouldPassWithApprovedCardInCredit() {
        creditPage = mainPage.goToCreditPage();
        DBConnector.PaymentData beforeRequest = DBConnector.getLastPaymentData("credit_request_entity");
        creditPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        creditPage.checkIfSuccess();
        DBConnector.PaymentData afterRequest = DBConnector.getLastPaymentData("credit_request_entity");
        assertNotEquals(beforeRequest.getId(), afterRequest.getId());
        assertEquals("APPROVED", afterRequest.getStatus());
    }

    @SneakyThrows
    @Test
    //Вручную не проходит, уведомление об успешной операции
    @DisplayName("27. Покупка по карте со статусом DECLINED")
    void shouldFailWithDeclinedCardInCredit() {
        creditPage = mainPage.goToCreditPage();
        DBConnector.PaymentData beforeRequest = DBConnector.getLastPaymentData("credit_request_entity");
        creditPage.fillInCardInfo(DataGenerator.getDeclinedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        creditPage.checkIfFail();
        DBConnector.PaymentData afterRequest = DBConnector.getLastPaymentData("credit_request_entity");
        assertNotEquals(beforeRequest.getId(), afterRequest.getId());
        assertEquals("DECLINED", afterRequest.getStatus());
    }

    @Test
    //Вручную не проходит, тексты уведомлений должны быть иными
    @DisplayName("28. Отправка пустой формы")
    void shouldThrowAllVerificationErrorsInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardInfo(DataGenerator.getEmptyCardNumber(), DataGenerator.getEmptyMonth(), DataGenerator.getEmptyYear(), DataGenerator.getEmptyOwner(), DataGenerator.getEmptyCVV());
        creditPage.checkCardNumberText("Поле обязательно для заполнения");
        creditPage.checkMonthText("Поле обязательно для заполнения");
        creditPage.checkYearText("Поле обязательно для заполнения");
        creditPage.checkOwnerText("Поле обязательно для заполнения");
        creditPage.checkCVVText("Поле обязательно для заполнения");
    }

    @Test
    //Вручную проходит
    @DisplayName("29. Номер карты из 16 цифр, отличный от 4444 4444 4444 4441 или 4444 4444 4444 4442")
    void shouldFailWithCardNotFromListInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardInfo(DataGenerator.getCardNumberNotFromRange(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        creditPage.checkIfFail();
    }

    @Test
    //Вручную проходит
    @DisplayName("30. Номер карты из 16 нулей")
    void shouldFailWithCardWithAllZerosInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardInfo(DataGenerator.getCardNumberWithAllZeros(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        creditPage.checkIfFail();
    }

    @Test
    //Вручную проходит
    @DisplayName("31. Номер карты, состоящий из менее чем 16 цифр")
    void shouldThrowWrongFormatCardNumberVerificationErrorInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardInfo(DataGenerator.getCardNumberWithLessNumbers(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        creditPage.checkCardNumberText("Неверный формат");
    }

    @Test
    //Вручную не проходит, текст уведомления должен быть иной
    @DisplayName("32. Пустой номер карты")
    void shouldThrowEmptyCardNumberVerificationErrorInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardInfo(DataGenerator.getEmptyCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        creditPage.checkCardNumberText("Поле обязательно для заполнения");
    }

    @Test
    //Вручную проходит
    @DisplayName("33. В поле 'Номер карты' введены буквы и специальные символы")
    void shouldNotEnterLettersAndSymbolsInCardNumberInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardNumber(DataGenerator.getCardNumberWithLettersAndSymbols());
        creditPage.emptyCardNumberInField();
    }

    @Test
    //Вручную проходит
    @DisplayName("34. Месяц, больше 12")
    void shouldThrowInvalidExpDateMonthVerificationErrorWithMoreThan12InCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getMonthWithMoreThan12(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        creditPage.checkMonthText("Неверно указан срок действия карты");
    }

    @Test
    //Вручную проходит
    @DisplayName("35. Месяц из 1 цифры")
    void shouldThrowWrongFormatMonthVerificationErrorInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getMonthWith1Symbol(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        creditPage.checkMonthText("Неверный формат");
    }

    @Test
    //Вручную не проходит, текст уведомления должен быть иной
    @DisplayName("36. Пустое поле 'Месяц'")
    void shouldThrowEmptyMonthVerificationErrorInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getEmptyMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        creditPage.checkMonthText("Поле обязательно для заполнения");
    }

    @Test
    //Вручную не проходит, форма отправляется без ошибок
    @DisplayName("37. Месяц из двух нулей")
    void shouldThrowInvalidExpDateMonthVerificationErrorWith00InCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getMonthWith00(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        creditPage.checkMonthText("Неверно указан срок действия карты");
    }

    @Test
    //Вручную проходит
    @DisplayName("38. В поле 'Месяц' введены буквы и специальные символы")
    void shouldNotEnterLettersAndSymbolsInMonthInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInMonth(DataGenerator.getMonthWithLettersAndSymbols());
        creditPage.emptyMonthInField();
    }

    @Test
    //Вручную проходит
    @DisplayName("39. Год меньше текущего")
    void shouldThrowInvalidExpDateYearVerificationErrorInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getYearLessThanCurrent(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        creditPage.checkYearText("Истёк срок действия карты");
    }

    @Test
    //Вручную проходит
    @DisplayName("40. Год из 1 цифры")
    void shouldThrowWrongFormatYearVerificationErrorInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getYearWith1Symbol(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        creditPage.checkYearText("Неверный формат");
    }

    @Test
    //Вручную не проходит, текст уведомления должен быть иной
    @DisplayName("41. Пустое поле 'Год'")
    void shouldThrowEmptyYearVerificationErrorInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getEmptyYear(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        creditPage.checkYearText("Поле обязательно для заполнения");
    }

    @Test
    //Вручную проходит
    @DisplayName("42. Год из двух нулей")
    void shouldThrowInvalidExpDateYearVerificationErrorWith00InCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getYearWith00(), DataGenerator.getApprovedOwner(), DataGenerator.getApprovedCVV());
        creditPage.checkYearText("Истёк срок действия карты");
    }

    @Test
    //Вручную проходит
    @DisplayName("43. В поле 'Год' введены буквы и специальные символы")
    void shouldNotEnterLettersAndSymbolsInYearInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInYear(DataGenerator.getYearWithLettersAndSymbols());
        creditPage.emptyYearInField();
    }

    @Test
    //Вручную не проходит, поле принимает любые значения
    @DisplayName("44. В поле 'Владелец' введены кириллические символы")
    void shouldThrowWrongFormatOwnerVerificationErrorInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getOwnerWithCyrillicLetters(), DataGenerator.getApprovedCVV());
        creditPage.checkOwnerText("Неверный формат");
    }

    @Test
    //Вручную не проходит, поле принимает любые значения
    @DisplayName("45. В поле 'Владелец' введены цифры и специальные символы кроме дефиса, пробела и апострофа")
    void shouldNotEnterLettersAndSymbolsInOwnerInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInOwner(DataGenerator.getOwnerWithSymbols());
        creditPage.emptyCardOwnerInField();
    }

    @Test
    //Вручную проходит
    @DisplayName("46. Пустое поле 'Владелец'")
    void shouldThrowEmptyOwnerVerificationErrorInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getEmptyOwner(), DataGenerator.getApprovedCVV());
        creditPage.checkOwnerText("Поле обязательно для заполнения");
    }

    @Test
    //Вручную проходит
    @DisplayName("47. Поле 'CVC/CVV' из 1 или 2 цифр")
    void shouldThrowWrongFormatCVVVerificationErrorInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getCVVWith2Symbols());
        creditPage.checkCVVText("Неверный формат");
    }

    @Test
    //Вручную не проходит, текст уведомления должен быть иной
    @DisplayName("48. Пустое поле 'CVC/CVV'")
    void shouldThrowEmptyCVVVerificationErrorInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getEmptyCVV());
        creditPage.checkCVVText("Поле обязательно для заполнения");
    }

    @Test
    //Вручную не проходит, форма отправляется без ошибок
    @DisplayName("49. Поле 'CVC/CVV' из трех нулей")
    void shouldThrowInvalidCVVVerificationErrorInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCardInfo(DataGenerator.getApprovedCardNumber(), DataGenerator.getApprovedMonth(), DataGenerator.getApprovedYear(), DataGenerator.getApprovedOwner(), DataGenerator.getCVVWith00());
        creditPage.checkCVVText("Неверное значение");
    }

    @Test
    //Вручную проходит
    @DisplayName("50. В поле 'CVC/CVV' введены буквы и специальные символы")
    void shouldNotEnterLettersAndSymbolsInCVVInCredit() {
        creditPage = mainPage.goToCreditPage();
        creditPage.fillInCVV(DataGenerator.getCVVWithLettersAndSymbols());
        creditPage.emptyCVVInField();
    }
}
