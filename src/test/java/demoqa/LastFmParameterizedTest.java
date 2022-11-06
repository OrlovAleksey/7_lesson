package demoqa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import demoqa.data.Locale;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class LastFmParameterizedTest {
    @BeforeAll
    static void setUp() {
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1540x1080";
    }
    @ValueSource(strings = {"Tame Impala", "Oasis"})
    @ParameterizedTest(name = "Проверка наличия исполнителя для поиска по тексту: {0}")
    void lastfmSearchTest(String testData) {
        open("https://www.last.fm/");
        $(".masthead-search-toggle").click();
        $("#masthead-search-field").setValue(testData).pressEnter();
        $(".grid-items-item-details").shouldHave(text(testData));
    }

    @CsvSource(value = {
            "Tame Impala, Currents",
            "Oasis, Definitely Maybe"
    })
    @ParameterizedTest(name = "Проверка наличия альбома для исполнителя {0}")
    void lastfmSearchTestAlbum(String Artist, String Album) {
        open("https://www.last.fm/");
        $(".masthead-search-toggle").click();
        $("#masthead-search-field").setValue(Artist).pressEnter();
        $(".grid-items-item-details").shouldHave(text(Artist));
        $$(".secondary-nav-item-link").findBy(text("Albums")).click();
        $(".album-results").shouldHave(text(Album));
    }

    static Stream<Arguments> lastfmLocaleTest(){
        return Stream.of(
                Arguments.of(List.of("Live","Musik","Charts", "Events", "Präsentationen"), Locale.Deutsch),
                Arguments.of(List.of("Live", "Musica", "Classifiche", "Eventi", "In evidenza"), Locale.Italiano),
                Arguments.of(List.of("Live", "Музыка", "Чарты", "События", "Избранное"), Locale.Русский)
        );
    }
    @MethodSource
    @ParameterizedTest
    void lastfmLocaleTest (List<String> buttonTexts, Locale locale){
        open("https://www.last.fm/");
        $(".footer-bottom").scrollIntoView(false);
        $$(".footer-language-form").findBy(text(locale.name())).click();
        $$(".masthead-nav-item").filter(visible).shouldHave(CollectionCondition.texts(buttonTexts));
    }
}