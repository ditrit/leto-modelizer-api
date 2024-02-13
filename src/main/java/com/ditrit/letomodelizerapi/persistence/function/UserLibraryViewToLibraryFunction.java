package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.persistence.model.Library;
import com.ditrit.letomodelizerapi.persistence.model.UserLibraryView;

import java.util.function.Function;

/**
 * A function that converts a UserLibraryView object into a Library object.
 * Utilizes a BeanMapper to copy properties from the UserLibraryView to a new Library instance,
 * then sets the library ID based on the values from the UserLibraryView.
 *
 * <p>This function facilitates the transformation of view representations of libraries into their
 * corresponding domain model instances, enabling easy data transfer and manipulation within the application.
 */
public class UserLibraryViewToLibraryFunction implements Function<UserLibraryView,
        Library> {
    @Override
    public Library apply(final UserLibraryView userLibraryView) {
        Library library = new BeanMapper<>(Library.class).apply(userLibraryView);

        library.setId(userLibraryView.getLibraryId());

        return library;
    }
}
