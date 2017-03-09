package pro.smartum.reptracker.gateway.web.beans;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import pro.smartum.reptracker.gateway.dao.entities.Gender;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

/**
 * @author Sergey Valuy
 * 
 */
public class User {

    private Long id;

    @NotNull
    @Size(min = 1)
    private String name;

    private Date createDate;

    private String country;

    private String postalCode;

    @NotNull
    private Integer maxTrackingAccountCount;

    private Integer maxAuthLevel;

    private Gender gender;

    @Min(1900)
    @Max(2012)
    private Integer birthYear;

    private String primaryLanguage;

    private Set<String> emailAddresses;

    private Set<String> trackingCodes;

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public Integer getMaxTrackingAccountCount() {
        return maxTrackingAccountCount;
    }

    public void setMaxTrackingAccountCount(Integer maxTrackingAccountCount) {
        this.maxTrackingAccountCount = maxTrackingAccountCount;
    }

    public Integer getMaxAuthLevel() {
        return maxAuthLevel;
    }

    public void setMaxAuthLevel(Integer maxAuthLevel) {
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

    public String getPrimaryLanguage() {
        return primaryLanguage;
    }

    public void setPrimaryLanguage(String primaryLanguage) {
        this.primaryLanguage = primaryLanguage;
    }

    public Set<String> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(Set<String> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public Set<String> getTrackingCodes() {
        return trackingCodes;
    }

    public void setTrackingCodes(Set<String> trackingCodes) {
        this.trackingCodes = trackingCodes;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
