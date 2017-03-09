package pro.smartum.reptracker.gateway.dao.entities;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "external_users", uniqueConstraints = {@UniqueConstraint(columnNames = {"parent_partner_id", "userIdentifier"})})
public class ExternalUserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column
    private String userIdentifier;

    @Column
    private String userEmail;

    @Column
    private String emailHash;

    @Column
    private Integer emailValidationLevel;

    @OneToMany(mappedBy = "externalUser")
    private Set<EventParticipantEntity> eventParticipants;

    @ManyToOne(optional = false)
    @JoinColumn(name = "parent_partner_id")
    private PartnerEntity parentPartner;

    public ExternalUserEntity() {
    }

    public ExternalUserEntity(String userIdentifier, String userEmail, Integer emailValidationLevel) {
        this.userIdentifier = userIdentifier;
        this.userEmail = userEmail;
        this.emailValidationLevel = emailValidationLevel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Integer getEmailValidationLevel() {
        return emailValidationLevel;
    }

    public void setEmailValidationLevel(Integer emailValidationLevel) {
        this.emailValidationLevel = emailValidationLevel;
    }

    public Set<EventParticipantEntity> getEventParticipants() {
        return eventParticipants;
    }

    public void setEventParticipants(Set<EventParticipantEntity> eventParticipants) {
        this.eventParticipants = eventParticipants;
    }

    public PartnerEntity getParentPartner() {
        return parentPartner;
    }

    public void setParentPartner(PartnerEntity parentPartner) {
        this.parentPartner = parentPartner;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getEmailHash() {
        return emailHash;
    }

    public void setEmailHash(String emailHash) {
        this.emailHash = emailHash;
    }
}
