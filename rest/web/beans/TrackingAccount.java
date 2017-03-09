package pro.smartum.reptracker.gateway.web.beans;

/**
 * User: Sergey Valuy
 
 */
public class TrackingAccount {

    private Long id;
    private String trackingCode;

    public TrackingAccount() {
    }

    public TrackingAccount(Long id, String trackingCode) {
        this.id = id;
        this.trackingCode = trackingCode;
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
}
