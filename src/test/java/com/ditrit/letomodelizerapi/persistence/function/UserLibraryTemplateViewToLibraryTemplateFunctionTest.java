package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.model.library.LibraryTemplateType;
import com.ditrit.letomodelizerapi.persistence.model.LibraryTemplate;
import com.ditrit.letomodelizerapi.persistence.model.UserLibraryTemplateView;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
@DisplayName("Test class: UserLibraryTemplateViewToLibraryTemplateFunction")
class UserLibraryTemplateViewToLibraryTemplateFunctionTest {

    @Test
    @DisplayName("Test apply: should transform UserLibraryTemplateView to LibraryTemplate")
    void testApply() {
        UserLibraryTemplateViewToLibraryTemplateFunction mapper = new UserLibraryTemplateViewToLibraryTemplateFunction();

        LibraryTemplate expectedLibraryTemplate = new LibraryTemplate();
        expectedLibraryTemplate.setId(UUID.randomUUID());
        expectedLibraryTemplate.setLibraryId(UUID.randomUUID());
        expectedLibraryTemplate.setName("name");
        expectedLibraryTemplate.setIcon("icon");
        expectedLibraryTemplate.setDescription("description");
        expectedLibraryTemplate.setDocumentationUrl("documentationUrl");
        expectedLibraryTemplate.setFiles(List.of("file1"));
        expectedLibraryTemplate.setPlugins(List.of());
        expectedLibraryTemplate.setSchemas(List.of("schema1"));
        expectedLibraryTemplate.setBasePath("basePath");
        expectedLibraryTemplate.setType(LibraryTemplateType.COMPONENT);

        UserLibraryTemplateView view = new UserLibraryTemplateView();
        view.setId("id");
        view.setLibraryTemplateId(expectedLibraryTemplate.getId());
        view.setLibraryId(expectedLibraryTemplate.getLibraryId());
        view.setUserId(UUID.randomUUID());
        view.setName("name");
        view.setIcon("icon");
        view.setDescription("description");
        view.setDocumentationUrl("documentationUrl");
        view.setFiles(List.of("file1"));
        view.setPlugins(List.of());
        view.setSchemas(List.of("schema1"));
        view.setBasePath("basePath");
        view.setType(LibraryTemplateType.COMPONENT);

        assertEquals(expectedLibraryTemplate, mapper.apply(view));
    }
}
