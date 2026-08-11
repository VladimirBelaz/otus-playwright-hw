package pages.components;

import com.google.inject.Inject;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitForSelectorState;

public class TeacherPopup {

    private final Locator popup;
    private final Locator activeTeacher;
    private final Locator nextButton;
    private final Locator previousButton;
    private final Locator closeButton;

    @Inject
    public TeacherPopup(Page page) {
        this.popup = page.locator(
                "div.sc-13monb3-2.sc-1craaz7-0:has(button.sc-15oy5d9-0):visible"
        ).first();

        this.activeTeacher = popup
                .locator(".swiper-slide-active")
                .locator("h3.sc-1x9oq14-0.jmLQpp");

        this.nextButton = popup.locator(
                "button.sc-az0z8y-0.sc-az0z8y-5"
        );

        this.previousButton = popup.locator(
                "button.sc-az0z8y-0.sc-az0z8y-4"
        );

        this.closeButton = popup.locator(
                "button.sc-15oy5d9-0"
        );
    }

    public void waitForOpen() {
        popup.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(15_000));
    }

    public boolean isOpen() {
        return popup.isVisible();
    }

    public String getTeacherName() {
        activeTeacher.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(15_000));

        return activeTeacher.innerText().trim();
    }

    public void clickNext() {
        nextButton.click();

        waitForTeacherChange();
    }

    public void clickPrevious() {
        previousButton.click();

        waitForTeacherChange();
    }

    public void close() {
        closeButton.click();

        popup.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.HIDDEN)
                .setTimeout(15_000));
    }

    private void waitForTeacherChange() {
        pageWaitForPopupContent();
    }

    private void pageWaitForPopupContent() {
        activeTeacher.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE)
                .setTimeout(15_000));
    }
}