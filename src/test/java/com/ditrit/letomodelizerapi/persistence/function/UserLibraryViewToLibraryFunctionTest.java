package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.persistence.model.Library;
import com.ditrit.letomodelizerapi.persistence.model.UserLibraryView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
@DisplayName("Test class: UserLibraryViewToLibraryFunction")
class UserLibraryViewToLibraryFunctionTest {

    @Test
    @DisplayName("Test apply: should transform UserLibraryView to Library")
    void testApply() {
        UserLibraryViewToLibraryFunction mapper = new UserLibraryViewToLibraryFunction();

        Library expectedLibrary = new Library();
        expectedLibrary.setId(UUID.randomUUID());
        expectedLibrary.setName("name");
        expectedLibrary.setIcon("icon");
        expectedLibrary.setDescription("description");
        expectedLibrary.setMaintainer("maintainer");
        expectedLibrary.setVersion("version");
        expectedLibrary.setDocumentationUrl("documentationUrl");

        UserLibraryView view = new UserLibraryView();
        view.setDescription("description");
        view.setId("id");
        view.setLibraryId(expectedLibrary.getId());
        view.setUserId(UUID.randomUUID());
        view.setName("name");
        view.setIcon("icon");
        view.setDescription("description");
        view.setMaintainer("maintainer");
        view.setVersion("version");
        view.setDocumentationUrl("documentationUrl");

        assertEquals(expectedLibrary, mapper.apply(view));
    }
}
