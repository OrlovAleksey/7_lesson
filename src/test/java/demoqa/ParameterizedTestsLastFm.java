package demoqa;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class ParameterizedTestsLastFm {
    @BeforeAll
    static void setUp() {
        Configuration.holdBrowserOpen = true;
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
}