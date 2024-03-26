package com.ditrit.letomodelizerapi.persistence.function;

import com.ditrit.letomodelizerapi.persistence.model.AccessControl;
import com.ditrit.letomodelizerapi.persistence.model.UserAccessControlView;

import java.util.function.Function;

/**
 * A function that converts a UserAccessControlView object into an AccessControl object.
 * This class implements the Java Function interface, providing an implementation for the apply method.
 * The purpose of this function is to take an object of type UserAccessControlView and transform it into an object of
 * type AccessControl, typically used for converting data models across different layers of an application.
 */
public class UserAccesControlViewToAccessControlFunction implements Function<UserAccessControlView, AccessControl> {

    @Override
    public AccessControl apply(final UserAccessControlView userAccessControlView) {
        AccessControl accessControl = new AccessControl();

        accessControl.setId(userAccessControlView.getAccessControlId());
        accessControl.setName(userAccessControlView.getName());
        accessControl.setType(userAccessControlView.getType());

        return accessControl;
    }
}
