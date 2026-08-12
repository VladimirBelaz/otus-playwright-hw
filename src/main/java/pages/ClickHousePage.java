package pages;

import com.google.inject.Inject;
import com.microsoft.playwright.Page;
import pages.components.TeacherPopup;
import pages.components.TeachersComponent;

public class ClickHousePage extends BasePage {

    private static final String PATH = "/lessons/clickhouse/";

    private final TeachersComponent teachersComponent;
    private final TeacherPopup teacherPopup;

    @Inject
    public ClickHousePage(Page page, TeachersComponent teachersComponent, TeacherPopup teacherPopup) {
        super(page);
        this.teachersComponent = teachersComponent;
        this.teacherPopup = teacherPopup;
    }

    public ClickHousePage open() {
        open(PATH);
        return this;
    }

    public String getUrl() {
        return getCurrentUrl();
    }

    public TeachersComponent teachers() {
        return teachersComponent;
    }

    public TeacherPopup popup() {
        return teacherPopup;
    }
}