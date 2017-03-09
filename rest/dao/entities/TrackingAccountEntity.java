package pro.smartum.reptracker.gateway.dao.entities;

import org.hibernate.annotations.Index;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Sergey Valuy
 * 
 */
@Entity
@Table(name = "tracking_accounts")
public class TrackingAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Index(name = "UNIQUE_TRACKING_CODE")
    @Column(nullable = false, unique = true)
    private String trackingCode;

    @JoinColumn(name = "user_id")
    @ManyToOne(optional = false)
    private UserEntity user;

    @OneToMany(mappedBy = "trackingAccount")
    private Set<EventParticipantEntity> eventParticipants;

    public TrackingAccountEntity() {
    }

    public TrackingAccountEntity(String trackingCode, UserEntity user) {
        this.trackingCode = trackingCode;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackingCode() {
        return trackingCode;
    }

    public void setTrackingCode(String trackingCode) {
        this.trackingCode = trackingCode;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Set<EventParticipantEntity> getEventParticipants() {
        return eventParticipants;
    }

    public void setEventParticipants(Set<EventParticipantEntity> eventParticipants) {
        this.eventParticipants = eventParticipants;
    }
}
