package zw.co.invenico.springcommonsmodule.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Sheldon
 * @created 28/06/2020 - 1:39 PM
 */

@MappedSuperclass
@Data
@NoArgsConstructor
public abstract class BaseEntity implements Serializable {

    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "en_ZW", timezone = "Africa/Harare")
    private Date dateCreated;

    @UpdateTimestamp
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", locale = "en_ZW", timezone = "Africa/Harare")
    private Date dateModified = new Date();

    @Version
    @Column(nullable = false)
    private Long version;

    @Column
    private boolean deleted;
}
