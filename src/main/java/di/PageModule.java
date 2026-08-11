package di;

import com.google.inject.AbstractModule;
import pages.ClickHousePage;
import pages.components.TeacherPopup;
import pages.components.TeachersComponent;

public class PageModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ClickHousePage.class);
        bind(TeachersComponent.class);
        bind(TeacherPopup.class);
    }
}