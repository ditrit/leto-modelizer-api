package com.ditrit.letomodelizerapi.controller;

import com.ditrit.letomodelizerapi.helper.MockHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("unit")
@DisplayName("Test class: CurrentUserController")
class DefaultControllerTest extends MockHelper {
    class ControllerTest implements DefaultController {};

    @Test
    @DisplayName("Test getFilters: should return map of filters")
    void testGetFilters() {
        HashMap<String, String> map = new HashMap<>();
        assertEquals(map, new ControllerTest().getFilters(mockUriInfo()));
    }

    @Test
    @DisplayName("Test getStatus: should return 200 on complete response.")
    void testGetStatusComplete() {
        assertEquals(200, new ControllerTest().getStatus(new PageImpl<String>(List.of(), Pageable.ofSize(1), 1)));
    }

    @Test
    @DisplayName("Test getStatus: should return 206 on partial response.")
    void testGetStatusPartial() {
        assertEquals(206, new ControllerTest().getStatus(new PageImpl<String>(List.of(), Pageable.ofSize(1), 2)));

    }
}
