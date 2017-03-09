package pro.smartum.reptracker.gateway.dao.entities;

import org.hibernate.annotations.Index;

import javax.persistence.*;

/**
 * User: Sergey Valuy
 
 */
@Entity
@Table(name = "event_types")
public class EventTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Index(name = "UNIQUE_TYPE_NAME")
    @Column(unique = true, nullable = false)
    private String typeName;

    @Index(name = "UNIQUE_TYPE_CODE")
    @Column(unique = true, nullable = false)
    private Integer typeCode;

    public EventTypeEntity() {
    }

    public EventTypeEntity(String typeName, Integer typeCode) {
        this.typeName = typeName;
        this.typeCode = typeCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(Integer typeCode) {
        this.typeCode = typeCode;
    }
}
