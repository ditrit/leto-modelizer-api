package com.ditrit.letomodelizerapi.persistence.specification;

import com.ditrit.letomodelizerapi.model.accesscontrol.AccessControlType;
import com.ditrit.letomodelizerapi.model.permission.ActionPermission;
import com.ditrit.letomodelizerapi.model.permission.EntityPermission;
import com.ditrit.letomodelizerapi.persistence.model.User;
import io.github.zorin95670.predicate.StringPredicateFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
@DisplayName("Test class: CustomSpringQueryFilterSpecification")
class CustomSpringQueryFilterSpecificationTest {

    @Test
    @DisplayName("Test getPredicateFilter: should return AccessControlTypePredicateFilter")
    void testGetPredicateFilterReturnsAccessControlTypePredicateFilter() {
        var specification = new CustomSpringQueryFilterSpecification<User>(User.class, null);

        assertEquals(
                AccessControlTypePredicateFilter.class,
                specification.getPredicateFilter(AccessControlType.class, "name", "value")
                        .getClass());
    }

    @Test
    @DisplayName("Test getPredicateFilter: should return EntityPermissionPredicateFilter")
    void testGetPredicateFilterReturnsEntityPermissionPredicateFilter() {
        var specification = new CustomSpringQueryFilterSpecification<User>(User.class, null);

        assertEquals(
                EntityPermissionPredicateFilter.class,
                specification.getPredicateFilter(EntityPermission.class, "name", "value")
                        .getClass());
    }

    @Test
    @DisplayName("Test getPredicateFilter: should return ActionPermissionPredicateFilter")
    void testGetPredicateFilterReturnsActionPermissionPredicateFilter() {
        var specification = new CustomSpringQueryFilterSpecification<User>(User.class, null);

        assertEquals(
                ActionPermissionPredicateFilter.class,
                specification.getPredicateFilter(ActionPermission.class, "name", "value")
                        .getClass());
    }

    @Test
    @DisplayName("Test getPredicateFilter: should return StringPredicateFilter")
    void testGetPredicateFilterReturnsStringPredicateFilter() {
        var specification = new CustomSpringQueryFilterSpecification<User>(User.class, null);

        assertEquals(
                StringPredicateFilter.class,
                specification.getPredicateFilter(String.class, "name", "value")
                        .getClass());
    }
}
