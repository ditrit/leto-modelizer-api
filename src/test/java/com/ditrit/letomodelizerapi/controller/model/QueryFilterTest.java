package com.ditrit.letomodelizerapi.controller.model;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

@Tag("unit")
@DisplayName("Test class: QueryFilter")
public class QueryFilterTest {

    @Test
    @DisplayName("Test getComputedPage: should return valid page")
    public void testGetComputedPage() {
        QueryFilter queryFilter = new QueryFilter();

        queryFilter.setPage(null);
        assertEquals(0, queryFilter.getComputedPage());

        queryFilter.setPage(-1);
        assertEquals(0, queryFilter.getComputedPage());

        queryFilter.setPage(2);
        assertEquals(2, queryFilter.getComputedPage());
    }

    @Test
    @DisplayName("Test getComputedPageSize: should return valid page size")
    public void testGetComputedPageSize() {
        QueryFilter queryFilter = new QueryFilter();

        queryFilter.setCount(null);
        assertEquals(10, queryFilter.getComputedPageSize());

        queryFilter.setCount(-1);
        assertEquals(1, queryFilter.getComputedPageSize());

        queryFilter.setCount(201);
        assertEquals(200, queryFilter.getComputedPageSize());

        queryFilter.setCount(45);
        assertEquals(45, queryFilter.getComputedPageSize());
    }

    @Test
    @DisplayName("Test isAscendantSort: should return valid sort direction")
    public void testIsAscendantSort() {
        QueryFilter queryFilter = new QueryFilter();

        queryFilter.setSort(null);
        assertFalse(queryFilter.isAscendantSort());

        queryFilter.setSort("desc");
        assertFalse(queryFilter.isAscendantSort());

        queryFilter.setSort("test");
        assertFalse(queryFilter.isAscendantSort());

        queryFilter.setSort("asc");
        assertTrue(queryFilter.isAscendantSort());

        queryFilter.setSort("ASC");
        assertTrue(queryFilter.isAscendantSort());
    }

    @Test
    @DisplayName("Test getDirection: should return valid sort direction")
    public void testGetDirection() {
        QueryFilter queryFilter = new QueryFilter();

        queryFilter.setSort(null);
        assertEquals(Sort.Direction.ASC, queryFilter.getDirection(true));
        queryFilter.setSort("");
        assertEquals(Sort.Direction.ASC, queryFilter.getDirection(true));
        queryFilter.setSort(" ");
        assertEquals(Sort.Direction.ASC, queryFilter.getDirection(true));

        queryFilter.setSort(null);
        assertEquals(Sort.Direction.DESC, queryFilter.getDirection(false));
        queryFilter.setSort("");
        assertEquals(Sort.Direction.DESC, queryFilter.getDirection(false));
        queryFilter.setSort(" ");
        assertEquals(Sort.Direction.DESC, queryFilter.getDirection(false));

        queryFilter.setSort("asc");
        assertEquals(Sort.Direction.ASC, queryFilter.getDirection(true));
        assertEquals(Sort.Direction.ASC, queryFilter.getDirection(false));

        queryFilter.setSort("desc");
        assertEquals(Sort.Direction.DESC, queryFilter.getDirection(true));
        assertEquals(Sort.Direction.DESC, queryFilter.getDirection(false));
    }

    @Test
    @DisplayName("Test getPageable: should return Pageable with default sort")
    public void testGetPageable() {
        QueryFilter queryFilter = new QueryFilter();

        Pageable pageable = queryFilter.getPageable(true, "test1", "test2");
        assertNotNull(pageable);
        assertEquals(0, pageable.getPageNumber());
        assertEquals(10, pageable.getPageSize());
        assertEquals(2, pageable.getSort().toList().size());
        assertEquals("test1", pageable.getSort().toList().get(0).getProperty());
        assertEquals(Sort.Direction.ASC, pageable.getSort().toList().get(0).getDirection());
        assertEquals("test2", pageable.getSort().toList().get(1).getProperty());
        assertEquals(Sort.Direction.ASC, pageable.getSort().toList().get(1).getDirection());
    }

    @Test
    @DisplayName("Test getPageable: should return Pageable with wanted sort")
    public void testGetPageableWithSort() {
        QueryFilter queryFilter = new QueryFilter();
        queryFilter.setPage(1);
        queryFilter.setCount(5);
        queryFilter.setSort("desc");
        queryFilter.setOrder("order");

        Pageable pageable = queryFilter.getPageable(true, "test1", "test2");
        assertNotNull(pageable);
        assertEquals(1, pageable.getPageNumber());
        assertEquals(5, pageable.getPageSize());
        assertEquals(1, pageable.getSort().toList().size());
        assertEquals("order", pageable.getSort().toList().getFirst().getProperty());
        assertEquals(Sort.Direction.DESC, pageable.getSort().toList().getFirst().getDirection());
    }
}
