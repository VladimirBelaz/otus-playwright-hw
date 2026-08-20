package pages.components;

import com.google.inject.Inject;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Mouse;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class TeachersComponent {

    private final Page page;
    private final Locator section;
    private final Locator slider;
    private final Locator teachers;

    @Inject
    public TeachersComponent(Page page) {
        this.page = page;

        this.section = page.locator("section")
                .filter(new Locator.FilterOptions()
                        .setHasText("Преподаватели"))
                .first();

        this.slider = section.locator(".swiper").first();

        this.teachers = section.locator(".swiper-slide");
    }

    public boolean isDisplayed() {
        return section.isVisible();
    }

    public int getTeachersCount() {
        return teachers.count();
    }

    public boolean hasTeacher(String teacherName) {
        return teachers
                .filter(new Locator.FilterOptions()
                        .setHasText(teacherName))
                .count() > 0;
    }

    public String getTeacherName(int index) {
        return teachers.nth(index)
                .locator("p")
                .first()
                .innerText()
                .trim();
    }

    public String getTeacherPosition(String teacherName) {
        return findTeacher(teacherName)
                .locator("p")
                .nth(1)
                .innerText()
                .trim();
    }

    public void dragToNextTeacher() {
        slider.scrollIntoViewIfNeeded();

        Locator activeSlide = section.locator(".swiper-slide-active");

        activeSlide.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(15_000));

        double startX = activeSlide.boundingBox().x + activeSlide.boundingBox().width / 2;
        double startY = activeSlide.boundingBox().y + activeSlide.boundingBox().height / 2;

        page.mouse().move(startX, startY);
        page.mouse().down();

        page.mouse().move(startX - 350, startY, new Mouse.MoveOptions().setSteps(10));

        page.mouse().up();

        page.waitForTimeout(500);
    }

    public int getActiveSlideIndex() {
        String index = section.locator(".swiper-slide-active")
                .getAttribute("data-swiper-slide-index");

        if (index == null) {
            throw new IllegalStateException("У активного слайда отсутствует data-swiper-slide-index");
        }

        return Integer.parseInt(index);
    }

    public String getActiveTeacherName() {
        return section.locator(".swiper-slide-active")
                .locator("p")
                .first()
                .innerText()
                .trim();
    }

    public boolean hasActiveSlideChanged(int previousIndex) {
        return getActiveSlideIndex() != previousIndex;
    }

    public void clickTeacher(String teacherName) {
        findTeacher(teacherName).click();
        page.locator("div.sc-13monb3-2.sc-1craaz7-0:has(button.sc-15oy5d9-0):visible")
                .first()
                .waitFor(new Locator.WaitForOptions().setState(WaitForSelectorState.VISIBLE).setTimeout(15_000));
    }

    private Locator findTeacher(String teacherName) {
        return teachers.filter(new Locator.FilterOptions().setHasText(teacherName)).first();
    }
}

