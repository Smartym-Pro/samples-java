package pro.smartum.reptracker.gateway.dao.entities;

import javax.persistence.*;

/**
 * @author Sergey Valuy
 * 
 */
@Entity
@Table(name = "event_participants")
public class EventParticipantEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JoinColumn(name = "tracking_account_id")
    @ManyToOne
    private TrackingAccountEntity trackingAccount;

    @JoinColumn(name = "external_user_id")
    @ManyToOne
    private ExternalUserEntity externalUser;

    @Column(nullable = false)
    private boolean approved;

    public EventParticipantEntity() {
    }

    public EventParticipantEntity(TrackingAccountEntity trackingAccount, ExternalUserEntity externalUser) {
        this.trackingAccount = trackingAccount;
        this.externalUser = externalUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TrackingAccountEntity getTrackingAccount() {
        return trackingAccount;
    }

    public void setTrackingAccount(TrackingAccountEntity trackingAccount) {
        this.trackingAccount = trackingAccount;
    }

    public ExternalUserEntity getExternalUser() {
        return externalUser;
    }

    public void setExternalUser(ExternalUserEntity externalUser) {
        this.externalUser = externalUser;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
