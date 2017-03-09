package pro.smartum.reptracker.gateway.dao.entities;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Sergey Valuy
 * 
 */
@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "event_type_id")
    private EventTypeEntity eventType;

    @Column
    private String siteEventIdentifier;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private Date siteEventDate;

    @Column(length = 100)
    private String eventDescription;

    @Column
    private Integer affiliateRelationStrength;

    @JoinColumn(name = "consumer_id")
    @ManyToOne(optional = false)
    private EventParticipantEntity consumer;

    @JoinColumn(name = "business_id")
    @ManyToOne(optional = false)
    private EventParticipantEntity business;

    @JoinColumn(name = "affiliate_id")
    @ManyToOne
    private EventParticipantEntity affiliate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public EventTypeEntity getEventType() {
        return eventType;
    }

    public void setEventType(EventTypeEntity eventType) {
        this.eventType = eventType;
    }

    public String getSiteEventIdentifier() {
        return siteEventIdentifier;
    }

    public void setSiteEventIdentifier(String siteEventIdentifier) {
        this.siteEventIdentifier = siteEventIdentifier;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Integer getAffiliateRelationStrength() {
        return affiliateRelationStrength;
    }

    public void setAffiliateRelationStrength(Integer affiliateRelationStrength) {
        this.affiliateRelationStrength = affiliateRelationStrength;
    }

    public EventParticipantEntity getConsumer() {
        return consumer;
    }

    public void setConsumer(EventParticipantEntity consumer) {
        this.consumer = consumer;
    }

    public EventParticipantEntity getBusiness() {
        return business;
    }

    public void setBusiness(EventParticipantEntity business) {
        this.business = business;
    }

    public EventParticipantEntity getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(EventParticipantEntity affiliate) {
        this.affiliate = affiliate;
    }

    public Date getSiteEventDate() {
        return siteEventDate;
    }

    public void setSiteEventDate(Date siteEventDate) {
        this.siteEventDate = siteEventDate;
    }
}
