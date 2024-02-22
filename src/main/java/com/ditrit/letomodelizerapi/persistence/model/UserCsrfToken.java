package com.ditrit.letomodelizerapi.persistence.model;

import com.ditrit.letomodelizerapi.persistence.specification.filter.FilterType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Entity representing a CSRF (Cross-Site Request Forgery) token associated with a user.
 * This class maps to the 'csrf_tokens' table in the database, storing CSRF tokens that are used to protect
 * against CSRF attacks by ensuring that each request made to the server is from a trusted source.
 */
@Entity
@Table(name = "csrf_tokens")
@EqualsAndHashCode(callSuper = true)
@Data
public class UserCsrfToken extends AbstractEntity {

    /**
     * Default constructor for the UserCsrfToken class.
     * Initializes a new instance of the UserCsrfToken without setting any properties. This constructor
     * is typically used by frameworks that require a no-argument constructor for instantiation.
     */
    public UserCsrfToken() {

    }

    /**
     * Constructs a new UserCsrfToken with a specified expiration timeout.
     * This constructor initializes the UserCsrfToken and sets its expiration date to a future time based on
     * the provided timeout value. The expiration date is calculated by adding the timeout (in seconds) to the
     * current time.
     *
     * @param timeout the amount of time (in seconds) until the token expires from the current moment.
     */
    public UserCsrfToken(final long timeout) {
        this.setExpirationDate(Timestamp.from(Instant.now().plus(timeout, ChronoUnit.SECONDS)));
    }

    /**
     * Internal id.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cst_id")
    @FilterType(type = FilterType.Type.NUMBER)
    private Long id;

    /**
     * The login identifier of the user to whom the CSRF token is associated.
     */
    @Column(name = "login")
    @FilterType(type = FilterType.Type.TEXT)
    private String login;

    /**
     * The CSRF token value.
     */
    @Column(name = "token")
    private String token;

    /**
     * The expiration date of this token.
     */
    @Column(name = "expiration_date")
    private Timestamp expirationDate;

    /**
     * Set insertDate before persisting in repository.
     */
   @PrePersist
    public void prePersist() {
        this.setInsertDate(Timestamp.valueOf(LocalDateTime.now()));
    }
}
