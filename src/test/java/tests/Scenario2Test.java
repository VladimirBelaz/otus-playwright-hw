package tests;

import com.google.inject.Inject;
import extensions.PlaywrightExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.CatalogPage;

import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(PlaywrightExtension.class)
public class Scenario2Test {

    @Inject
    private CatalogPage catalogPage;

    @Test
    void shouldFilterCoursesByDurationAndDirection() {

        catalogPage.open();

        catalogPage.verifyDefaultFilters();

        assertFalse(catalogPage.getCourseCardsIds().isEmpty(), "В каталоге отсутствуют курсы");

        catalogPage.selectDuration(3, 10);

        catalogPage.verifyAllCoursesDurationInRange(3, 10);

        assertFalse(catalogPage.getCourseCardsIds().isEmpty(), "После фильтра по продолжительности каталог пуст");

        catalogPage.selectArchitectureDirection();

        catalogPage.verifyDirectionFilterArchitecture();

        catalogPage.verifyAllCoursesDurationInRange(3, 10);

        assertFalse(catalogPage.getCourseCardsIds().isEmpty(), "После применения двух фильтров каталог пуст"        );

        catalogPage.resetFilters();

        catalogPage.verifyFiltersReset();

        assertFalse(catalogPage.getCourseCardsIds().isEmpty(),"После сброса фильтров каталог пуст"
        );
    }
}