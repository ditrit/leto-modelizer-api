package com.ditrit.letomodelizerapi.controller.model;

import com.ditrit.letomodelizerapi.config.Constants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.github.zorin95670.query.ISpringQueryFilter;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Optional;

/**
 * Class to manage query option for controller.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@NoArgsConstructor
public class QueryFilter implements ISpringQueryFilter {

    /**
     * The current page.
     */
    @Min(0)
    private Integer page = 0;

    /**
     * The maximum size of resources.
     */
    @Min(1)
    @Max(Constants.MAXIMUM_RESOURCE_SIZE)
    private Integer count = Constants.DEFAULT_RESOURCE_SIZE;

    /**
     * Name of the field to sort.
     */
    private String order;

    /**
     * Type of sort. Can be 'asc' or 'desc'.
     */
    @Pattern(regexp = "asc|desc")
    private String sort;

    @Override
    public int getComputedPage() {
        return Math.max(Optional.ofNullable(page).orElse(0), 0);
    }

    @Override
    public int getComputedPageSize() {
        return Math.clamp(Optional.ofNullable(getCount())
                .orElse(Constants.DEFAULT_RESOURCE_SIZE),
                Constants.MINIMUM_RESOURCE_SIZE,
                Constants.MAXIMUM_RESOURCE_SIZE);
    }

    @Override
    public boolean isAscendantSort() {
        return "asc".equalsIgnoreCase(sort);
    }

    /**
     * Determines the {@link Sort.Direction} (ascending or descending) based on the provided sorting order.
     *
     * <p>This method first checks if a custom sort order has been specified. If so, it uses that order.
     * Otherwise, it defaults to ascending or descending based on the provided {@code isAscending} flag.
     *
     * @param isAscending a boolean flag indicating whether the sorting order should be ascending.
     *                    If no custom sort order is provided, this flag is used to determine the direction.
     * @return {@link Sort.Direction#ASC} for ascending order, or {@link Sort.Direction#DESC} for descending order.
     */
    public Sort.Direction getDirection(final boolean isAscending) {
        if (StringUtils.isNotBlank(sort)) {
            if (isAscendantSort()) {
                return Sort.Direction.ASC;
            }
            return Sort.Direction.DESC;
        }

        if (isAscending) {
            return Sort.Direction.ASC;
        }

        return Sort.Direction.DESC;
    }

    /**
     * Constructs a {@link Pageable} object based on the provided sorting order and the current pagination parameters.
     *
     * <p>This method creates a {@link PageRequest} with sorting directions and the provided field names.
     * If a custom sorting order is already set, it is used; otherwise, the method uses the provided
     * {@code isAsc} flag to determine the sorting direction.
     *
     * @param isAsc a boolean flag indicating whether the sorting should be ascending or descending.
     *              If {@code true}, the sorting will be ascending; if {@code false}, descending.
     * @param orders a variable number of string parameters representing the field names to sort by.
     * @return a {@link Pageable} object with the appropriate page size, page number, and sort order.
     */
    public Pageable getPageable(final boolean isAsc, final String... orders) {
        Sort currentSort = this.getOrderBy(null);

        if (sort != null) {
            return PageRequest.of(this.getComputedPage(), this.getComputedPageSize(), currentSort);
        }

        return PageRequest.of(
                this.getComputedPage(),
                this.getComputedPageSize(),
                Sort.by(getDirection(isAsc), orders)
        );
    }
}
