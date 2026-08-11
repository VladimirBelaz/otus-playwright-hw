package pages;

import com.google.inject.Inject;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.WaitUntilState;
import pages.components.TeacherPopup;
import pages.components.TeachersComponent;

public class ClickHousePage {

    private static final String URL =
            "https://otus.ru/lessons/clickhouse/";

    private final Page page;
    private final TeachersComponent teachersComponent;
    private final TeacherPopup teacherPopup;

    @Inject
    public ClickHousePage(
            Page page,
            TeachersComponent teachersComponent,
            TeacherPopup teacherPopup
    ) {
        this.page = page;
        this.teachersComponent = teachersComponent;
        this.teacherPopup = teacherPopup;
    }

    public ClickHousePage open() {
        page.navigate(
                URL,
                new Page.NavigateOptions()
                        .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
        );

        return this;
    }

    public String getUrl() {
        return page.url();
    }

    public TeachersComponent teachers() {
        return teachersComponent;
    }

    public TeacherPopup popup() {
        return teacherPopup;
    }
}