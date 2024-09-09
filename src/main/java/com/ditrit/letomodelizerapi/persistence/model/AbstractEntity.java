package com.ditrit.letomodelizerapi.persistence.model;


import com.ditrit.letomodelizerapi.persistence.specification.filter.FilterType;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Data;

import java.sql.Timestamp;

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
    @FilterType(type = FilterType.Type.DATE)
    private Timestamp insertDate;

    /**
     * The last update date of this entity.
     */
    @Column(name = "update_date")
    @FilterType(type = FilterType.Type.DATE)
    @Version
    private Timestamp updateDate;
}
