package tests;

import com.google.inject.Inject;
import extensions.PlaywrightExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.BusinessPage;
import pages.CatalogPage;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@ExtendWith(PlaywrightExtension.class)
public class Scenario3Test {

    @Inject
    private BusinessPage businessPage;

    @Test
    void shouldOpenBusinessPageAndNavigateToCatalogAndCheckAllDirectionsFilter() {
        businessPage.open();

        CatalogPage catalogPage = businessPage.clickCatalogLink();

        assertThat(catalogPage.getDirectionFilterValue()).hasText("Все направления");
        System.out.println("Фильтр 'Все направления' выбран");
    }
}