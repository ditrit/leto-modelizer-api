package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.persistence.model.LibraryTemplate;
import com.ditrit.letomodelizerapi.persistence.model.UserLibraryTemplateView;

import java.util.function.Function;

/**
 * A function that converts a UserLibraryTemplateView object into a LibraryTemplate object.
 * Utilizes a BeanMapper to copy properties from the UserLibraryTemplateView to a new LibraryTemplate instance,
 * then sets the library template's ID and library ID based on the values from the UserLibraryTemplateView.
 *
 * <p>This function facilitates the transformation of view representations of library templates into their
 * corresponding domain model instances, enabling easy data transfer and manipulation within the application.
 */
public class UserLibraryTemplateViewToLibraryTemplateFunction implements Function<UserLibraryTemplateView,
        LibraryTemplate> {
    @Override
    public LibraryTemplate apply(final UserLibraryTemplateView userLibraryTemplateView) {
        LibraryTemplate libraryTemplate = new BeanMapper<>(LibraryTemplate.class).apply(userLibraryTemplateView);

        libraryTemplate.setId(userLibraryTemplateView.getLibraryTemplateId());
        libraryTemplate.setLibraryId(userLibraryTemplateView.getLibraryId());

        return libraryTemplate;
    }
}
