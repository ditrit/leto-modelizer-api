/**
 * Provides the classes necessary for converting various types used in the Ditrit LetoModelizer API's persistence layer.
 * This package primarily includes custom JPA attribute converters that facilitate the conversion between
 * database column representations and Java entity attributes. These converters are essential for handling
 * specialized data types or custom mappings that are not natively supported by the JPA implementation.
 *
 * <p>Converters in this package are typically used in entity classes with the {@code @Convert} annotation
 * to specify the conversion to be performed on a particular entity attribute.</p>
 *
 * <p>Example usage:</p>
 * <pre>
 *     {@code
 *     @Entity
 *     public class ExampleEntity {
 *         // ... other fields ...
 *
 *         @Convert(converter = AccessControlTypeConverter.class)
 *         private AccessControlType accessControlType;
 *         // ... other fields ...
 *     }
 *     }
 * </pre>
 *
 * @since 1.x
 */
package com.ditrit.letomodelizerapi.persistence.converter;
