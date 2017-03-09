package pro.smartum.reptracker.gateway.web.beans;

/**
 * @author Sergey Valuy
 * 
 */
public class Partner {

    private Long id;
    private String name;
    private String apiSecretCode;
    private String url;
    private boolean superPartner;

    public Partner() {
    }

    public Partner(String name, String apiSecretCode, String url, boolean superPartner) {
        this.name = name;
        this.apiSecretCode = apiSecretCode;
        this.url = url;
        this.superPartner = superPartner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApiSecretCode() {
        return apiSecretCode;
    }

    public void setApiSecretCode(String apiSecretCode) {
        this.apiSecretCode = apiSecretCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSuperPartner() {
        return superPartner;
    }

    public void setSuperPartner(boolean superPartner) {
        this.superPartner = superPartner;
    }
}
