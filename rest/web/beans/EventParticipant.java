package pro.smartum.reptracker.gateway.web.beans;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class EventParticipant {

    private Long id;

    private String trackingCode;

    private String externalUserIdentifier;

    private boolean approved;

    public EventParticipant() {
    }

    public EventParticipant(String trackingCode, String externalUserIdentifier) {
        this.trackingCode = trackingCode;
        this.externalUserIdentifier = externalUserIdentifier;
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

    public String getExternalUserIdentifier() {
        return externalUserIdentifier;
    }

    public void setExternalUserIdentifier(String externalUserIdentifier) {
        this.externalUserIdentifier = externalUserIdentifier;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
