package pro.smartum.reptracker.gateway.dao.entities;

import javax.persistence.*;

/**
 * @author Sergey Valuy
 * 
 */
@Entity
@Table(name = "referral_relations", uniqueConstraints =
        {@UniqueConstraint(columnNames = {"referrer_id", "referral_id", "partner_id", "depth", "eventTypeCode"})})
public class ReferralRelationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //relation parent
    @ManyToOne(optional = false)
    @JoinColumn(name = "referrer_id")
    private ExternalUserEntity referrer;

    //relation child
    @ManyToOne(optional = false)
    @JoinColumn(name = "referral_id")
    private ExternalUserEntity referral;

    //distance between parent and child nodes
    @Column(nullable = false)
    private int depth;

    @Column(nullable = false)
    private int eventTypeCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "partner_id")
    private PartnerEntity partner;

    public ReferralRelationEntity() {
    }

    public ReferralRelationEntity(ExternalUserEntity referrer, ExternalUserEntity referral, int depth, int eventTypeCode,
                                  PartnerEntity partner) {
        this.referrer = referrer;
        this.referral = referral;
        this.depth = depth;
        this.eventTypeCode = eventTypeCode;
        this.partner = partner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExternalUserEntity getReferrer() {
        return referrer;
    }

    public void setReferrer(ExternalUserEntity referrer) {
        this.referrer = referrer;
    }

    public ExternalUserEntity getReferral() {
        return referral;
    }

    public void setReferral(ExternalUserEntity referral) {
        this.referral = referral;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getEventTypeCode() {
        return eventTypeCode;
    }

    public void setEventTypeCode(int eventTypeCode) {
        this.eventTypeCode = eventTypeCode;
    }

    public PartnerEntity getPartner() {
        return partner;
    }

    public void setPartner(PartnerEntity partner) {
        this.partner = partner;
    }
}
