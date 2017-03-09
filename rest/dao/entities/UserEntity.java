package pro.smartum.reptracker.gateway.dao.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * @author Sergey Valuy
 * 
 */
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @JoinColumn(name = "country_id")
    @ManyToOne
    private CountryEntity country;

    @Column
    private String postalCode;

    @Column(nullable = false)
    private int maxTrackingAccountCount;

    @Column(nullable = false)
    private int maxAuthLevel;

    @Enumerated(EnumType.STRING)
    @Column
    private Gender gender;

    @Column
    private Integer birthYear;

    @JoinColumn(name = "language_id")
    @ManyToOne
    private LanguageEntity primaryLanguage;

    @OneToMany(mappedBy = "user")
    private Set<EmailAddressEntity> emails;

    @OneToMany(mappedBy = "user")
    private Set<TrackingAccountEntity> trackingAccounts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public CountryEntity getCountry() {
        return country;
    }

    public void setCountry(CountryEntity country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public int getMaxTrackingAccountCount() {
        return maxTrackingAccountCount;
    }

    public void setMaxTrackingAccountCount(int maxTrackingAccountCount) {
        this.maxTrackingAccountCount = maxTrackingAccountCount;
    }

    public int getMaxAuthLevel() {
        return maxAuthLevel;
    }

    public void setMaxAuthLevel(int maxAuthLevel) {
        this.maxAuthLevel = maxAuthLevel;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public LanguageEntity getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(LanguageEntity primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    public Set<EmailAddressEntity> getEmails() {
        return emails;
    }

    public void setEmails(Set<EmailAddressEntity> emails) {
        this.emails = emails;
    }

    public Set<TrackingAccountEntity> getTrackingAccounts() {
        return trackingAccounts;
    }

    public void setTrackingAccounts(Set<TrackingAccountEntity> trackingAccounts) {
        this.trackingAccounts = trackingAccounts;
    }
}
