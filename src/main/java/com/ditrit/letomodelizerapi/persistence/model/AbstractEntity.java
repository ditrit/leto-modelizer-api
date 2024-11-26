package com.ditrit.letomodelizerapi.persistence.model;

import io.github.zorin95670.predicate.FilterType;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Abstract entity with default fields (InsertDate and UpdateDate).
 */
@MappedSuperclass
@Data
public class AbstractEntity {

    /**
     * The creation date of this entity.
     */
    @Column(name = "insert_date", updatable = false)
    @FilterType(type = Date.class)
    private Timestamp insertDate;

    /**
     * The last update date of this entity.
     */
    @Column(name = "update_date")
    @FilterType(type = Date.class)
    @Version
    private Timestamp updateDate;
}
