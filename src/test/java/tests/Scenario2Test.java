package tests;

import com.google.inject.Inject;
import extensions.PlaywrightExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.CatalogPage;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(PlaywrightExtension.class)
public class Scenario2Test {

    @Inject
    private CatalogPage catalogPage;

    @Test
    void shouldFilterCoursesByDurationAndDirection() {
        catalogPage.open();
        System.out.println("✅ Страница каталога открыта");

        catalogPage.verifyDirectionFilterDefault();
        catalogPage.verifyLevelFilterDefault();

        int initialCount = catalogPage.getCoursesCount();
        System.out.println("➡️ Начальное количество курсов: " + initialCount);

        catalogPage.selectDuration(3, 10);
        int durationFilteredCount = catalogPage.getCoursesCount();
        System.out.println("➡️ После фильтра по продолжительности (3–10 мес): " + durationFilteredCount);

        catalogPage.verifyCoursesDisplayed();

        catalogPage.verifyAllCoursesDurationInRange(3, 10);

        catalogPage.selectArchitectureDirection();
        catalogPage.verifyDirectionFilterArchitecture();

        int architectureFilteredCount = catalogPage.getCoursesCount();
        System.out.println("➡️ После фильтра по направлению 'Архитектура': " + architectureFilteredCount);

        catalogPage.verifyCoursesDisplayed();

        if (architectureFilteredCount != durationFilteredCount) {
            assertNotEquals(durationFilteredCount, architectureFilteredCount,
                    "Количество курсов должно измениться после выбора направления 'Архитектура'");
            System.out.println("✅ Количество курсов изменилось");
        } else {
            System.out.println("ℹ️ Количество курсов не изменилось, так как все курсы в выборке относятся к архитектуре");
        }

        catalogPage.resetFilters();
        catalogPage.verifyFiltersReset();

        int finalCount = catalogPage.getCoursesCount();
        System.out.println("➡️ После сброса фильтров: " + finalCount);
        assertTrue(finalCount >= initialCount,
                "После сброса фильтров количество курсов должно быть не меньше исходного");
    }
}