package pro.smartum.reptracker.gateway.web.beans;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.format.annotation.DateTimeFormat;

public class Event {

    private Long id;

    private Date createDate;

    @NotNull
    private Integer eventTypeCode;

    @Size(max = 100)
    private String eventDescription;

    private String siteEventIdentifier;

    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private Date siteEventDate;

    private Integer affiliateRelationStrength;

    private EventParticipant consumer;

    private EventParticipant business;

    private EventParticipant affiliate;

    public Event() {
    }

    public Event(Integer eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

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

    public Integer getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(Integer eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getSiteEventIdentifier() {
        return siteEventIdentifier;
    }

    public void setSiteEventIdentifier(String siteEventIdentifier) {
        this.siteEventIdentifier = siteEventIdentifier;
    }

    public Integer getAffiliateRelationStrength() {
        return affiliateRelationStrength;
    }

    public void setAffiliateRelationStrength(Integer affiliateRelationStrength) {
        this.affiliateRelationStrength = affiliateRelationStrength;
    }

    public EventParticipant getConsumer() {
        return consumer;
    }

    public void setConsumer(EventParticipant consumer) {
        this.consumer = consumer;
    }

    public EventParticipant getBusiness() {
        return business;
    }

    public void setBusiness(EventParticipant business) {
        this.business = business;
    }

    public EventParticipant getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(EventParticipant affiliate) {
        this.affiliate = affiliate;
    }

    public Date getSiteEventDate() {
        return siteEventDate;
    }

    public void setSiteEventDate(Date siteEventDate) {
        this.siteEventDate = siteEventDate;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
