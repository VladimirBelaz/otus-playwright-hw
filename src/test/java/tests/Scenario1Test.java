package tests;

import com.google.inject.Inject;
import extensions.PlaywrightExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import pages.ClickHousePage;
import pages.components.TeacherPopup;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(PlaywrightExtension.class)
class Scenario1Test {

    @Inject
    private ClickHousePage clickHousePage;

    @Test
    void shouldScrollTeachersAndVerifyTeacherPopup() {
        clickHousePage.open();

        assertTrue(clickHousePage.teachers().isDisplayed(),"Секция 'Преподаватели' не отображается");

        assertTrue(clickHousePage.teachers().getTeachersCount() > 0,"В секции 'Преподаватели' нет преподавателей");

        int previousIndex = clickHousePage.teachers().getActiveSlideIndex();

        clickHousePage.teachers().dragToNextTeacher();

        assertTrue(clickHousePage.teachers().hasActiveSlideChanged(previousIndex),"Список преподавателей не прокрутился после drag");

        String activeTeacherName = clickHousePage.teachers().getActiveTeacherName();

        clickHousePage.teachers().clickTeacher(activeTeacherName);

        TeacherPopup popup = clickHousePage.popup();

        popup.waitForOpen();

        assertEquals(activeTeacherName, popup.getTeacherName(),"Открыт popup другого преподавателя");

        String firstPopupTeacherName = popup.getTeacherName();

        popup.clickNext();

        String secondPopupTeacherName = popup.getTeacherName();

        assertNotEquals(firstPopupTeacherName, secondPopupTeacherName, "После нажатия '>' преподаватель не изменился");

        popup.clickPrevious();

        assertEquals(firstPopupTeacherName, popup.getTeacherName(), "После нажатия '<' не открылся предыдущий преподаватель");

        popup.close();

        assertFalse(popup.isOpen(),"Popup не закрылся");
    }
}