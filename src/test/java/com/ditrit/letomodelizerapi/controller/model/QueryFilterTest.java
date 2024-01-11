package com.ditrit.letomodelizerapi.controller.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@DisplayName("Test class: QueryFilter")
class QueryFilterTest {


    @Test
    @DisplayName("Test getPagination: should return default pagination.")
    void testGetPaginationDefault() {
        QueryFilter filter = new QueryFilter();
        Pageable pageable = filter.getPagination();

        assertNotNull(pageable);
        assertNotNull(pageable.getSort());
        assertFalse(pageable.getSort().isSorted());
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
    }

    @Test
    @DisplayName("Test getPagination: should return pagination with given sort.")
    void testGetPaginationGivenSort() {
        QueryFilter filter = new QueryFilter();
        filter.setSort("desc");
        filter.setOrder("id");
        Pageable pageable = filter.getPagination();

        assertNotNull(pageable);
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertNotNull(pageable.getSort());
        assertEquals("id: DESC", pageable.getSort().toString());

        filter = new QueryFilter();
        filter.setSort("asc");
        filter.setOrder("id");
        pageable = filter.getPagination();

        assertNotNull(pageable);
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertNotNull(pageable.getSort());
        assertEquals("id: ASC", pageable.getSort().toString());
    }

    @Test
    @DisplayName("Test getPagination: should return pagination with given limit.")
    void testGetPaginationGivenLimit() {
        QueryFilter filter = new QueryFilter();
        filter.setCount(100);
        filter.setPage(50);
        Pageable pageable = filter.getPagination();

        assertNotNull(pageable);
        assertNotNull(pageable.getSort());
        assertFalse(pageable.getSort().isSorted());
        assertEquals(50, pageable.getPageNumber());
        assertEquals(100, pageable.getPageSize());
    }
}
