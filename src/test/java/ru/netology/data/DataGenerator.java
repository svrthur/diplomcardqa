package ru.netology.data;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DataGenerator {
    private static Faker fakerEn = new Faker(new Locale("en"));
    private static Faker fakerRu = new Faker(new Locale("ru"));

    public static String getApprovedCardNumber() {
        return "4444 4444 4444 4441";
    }

    public static String getDeclinedCardNumber() {
        return "4444 4444 4444 4442";
    }

    public static String getApprovedMonth() {
        String randomMonth = String.valueOf(fakerEn.number().numberBetween(1, 13));
        if (randomMonth.length() == 1) {
            return "0" + randomMonth;
        }
        return randomMonth;
    }

    public static String getApprovedYear() {
        return LocalDate.now().plusYears(1).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getApprovedOwner() {
        return fakerEn.name().firstName().toUpperCase() + " " + fakerEn.name().lastName().toUpperCase();
    }

    public static String getApprovedCVV() {
        return fakerEn.number().digits(3);
    }

    public static String getEmptyCardNumber() {
        return "";
    }

    public static String getEmptyMonth() {
        return "";
    }

    public static String getEmptyYear() {
        return "";
    }

    public static String getEmptyOwner() {
        return "";
    }

    public static String getEmptyCVV() {
        return "";
    }

    public static String getCardNumberWithAllZeros() {
        return "0000 0000 0000 0000";
    }

    public static String getMonthWith00() {
        return "00";
    }

    public static String getYearWith00() {
        return "00";
    }

    public static String getCVVWith00() {
        return "000";
    }

    public static String getCardNumberWithLettersAndSymbols() {
        return "fhhb $*)@ jkjs уолц";
    }

    public static String getMonthWithLettersAndSymbols() {
        return "h*";
    }

    public static String getYearWithLettersAndSymbols() {
        return "Р?";
    }

    public static String getOwnerWithCyrillicLetters() {
        return fakerRu.name().firstName().toUpperCase() + " " + fakerRu.name().lastName().toUpperCase();
    }

    public static String getOwnerWithSymbols() {
        return "23%$### *^^";
    }

    public static String getCVVWithLettersAndSymbols() {
        return "*Ц?";
    }

    public static String getCardNumberNotFromRange() {
        return fakerEn.business().creditCardNumber();
    }

    public static String getCardNumberWithLessNumbers() {
        String creditCard = fakerEn.business().creditCardNumber();
        return creditCard.substring(0, creditCard.length() - 1);
    }

    public static String getMonthWithMoreThan12() {
        return "13";
    }

    public static String getMonthWith1Symbol() {
        return String.valueOf(fakerEn.number().numberBetween(1, 10));
    }

    public static String getYearLessThanCurrent() {
        return LocalDate.now().minusYears(1).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getYearWith1Symbol() {
        return String.valueOf(fakerEn.number().numberBetween(1, 10));
    }

    public static String getCVVWith2Symbols() {
        return fakerEn.number().digits(2);
    }

}
