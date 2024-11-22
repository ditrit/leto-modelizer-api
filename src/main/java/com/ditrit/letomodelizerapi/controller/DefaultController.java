package com.ditrit.letomodelizerapi.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;

import java.time.Duration;

/**
 * Interface defining default controller behaviors. This interface provides common utility
 * methods that can be used across different controllers. These default methods include
 * extracting filters from query parameters and determining the HTTP status based on
 * the pagination details of a resource.
 */
public interface DefaultController {

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
        int maxAgeInSeconds = Integer.parseInt(maxAge);

        return CacheControl
                .maxAge(Duration.ofSeconds(maxAgeInSeconds))
                .cachePrivate()
                .mustRevalidate();
    }
}
