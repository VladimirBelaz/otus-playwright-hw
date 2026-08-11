package tests;

import com.google.inject.Inject;
import extensions.PlaywrightExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.BusinessPage;

@ExtendWith(PlaywrightExtension.class)
public class Scenario3Test {

    @Inject
    private BusinessPage businessPage;

    @Test
    void shouldOpenBusinessCoursePageAndNavigateToCatalog() {
        businessPage.open();

        businessPage.clickDetailsButton();

        businessPage.verifyBusinessCoursePageOpened();

        businessPage.verifyDirectionsDisplayed();

        String firstDirectionName = businessPage.getDirectionItems().first().textContent().trim();

        businessPage.clickFirstDirection();

        businessPage.verifyCatalogOpenedWithCategory(firstDirectionName);
    }
}