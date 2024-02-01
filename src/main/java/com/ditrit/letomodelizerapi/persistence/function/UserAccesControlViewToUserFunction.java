package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.persistence.model.User;
import com.ditrit.letomodelizerapi.persistence.model.UserAccessControlView;

import java.util.function.Function;

/**
 * A function that converts a UserAccessControlView object into a User object.
 * This class implements the Function interface, providing a single method apply that takes a UserAccessControlView
 * object and returns a corresponding User object.
 * The conversion involves copying relevant data from the UserAccessControlView to the User.
 */
public class UserAccesControlViewToUserFunction implements Function<UserAccessControlView, User> {

    @Override
    public User apply(final UserAccessControlView userAccessControlView) {
        User user = new User();

        user.setId(userAccessControlView.getUserId());
        user.setEmail(userAccessControlView.getEmail());
        user.setLogin(userAccessControlView.getLogin());
        user.setName(userAccessControlView.getUserName());

        return user;
    }
}
