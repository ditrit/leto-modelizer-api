package com.ditrit.letomodelizerapi.controller;

import jakarta.ws.rs.core.CacheControl;
import jakarta.ws.rs.core.UriInfo;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Interface defining default controller behaviors. This interface provides common utility
 * methods that can be used across different controllers. These default methods include
 * extracting filters from query parameters and determining the HTTP status based on
 * the pagination details of a resource.
 */
public interface DefaultController {
    /**
     * Extracts filter parameters from the URI query parameters.
     * This method is useful for parsing query parameters into a map, where each
     * parameter name is mapped to its corresponding value.
     *
     * @param uriInfo URI information containing the query parameters.
     * @return A map of query parameter names to their respective single value,
     *         extracted from the provided {@link UriInfo}.
     */
    default Map<String, String> getFilters(final UriInfo uriInfo) {
        return uriInfo.getQueryParameters().entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().get(0)))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    /**
     * Determines the appropriate HTTP status code based on the pagination details
     * of a resource. This method is used to return different statuses depending on
     * whether the content being returned is partial or complete.
     *
     * @param <T> The type of the content in the paginated resources.
     * @param resources The paginated resources to evaluate.
     * @return {@link HttpStatus.PARTIAL_CONTENT} if the resource has more than one page,
     *         otherwise {@link HttpStatus.OK}.
     */
    default <T> int getStatus(final Page<T> resources) {
        if (resources.getTotalPages() > 1) {
            return HttpStatus.PARTIAL_CONTENT.value();
        }
        return HttpStatus.OK.value();
    }

    /**
     * Generates a CacheControl object configured with specific caching strategies based on the provided maximum age.
     * This method sets up caching policies that are suitable for content that can be cached privately by user agents
     * but must be revalidated once the max age expires. It allows for a balance between efficient caching and ensuring
     * that users receive the most up-to-date content when necessary.
     *
     * @param maxAge the maximum age, in seconds, that the content should be considered fresh. After this period,
     *               caches must check back with the origin server to confirm that the content is still current.
     * @return a CacheControl object configured with the specified maximum age and additional caching strategies,
     *         including setting the response to be private, enabling must-revalidate, and disabling no-cache and
     *         no-store.
     */
    default CacheControl getCacheControl(String maxAge) {
        CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(false);
        cacheControl.setNoStore(false);
        cacheControl.setPrivate(true);
        cacheControl.setMustRevalidate(true);
        cacheControl.setMaxAge(Integer.valueOf(maxAge));

        return cacheControl;
    }
}
