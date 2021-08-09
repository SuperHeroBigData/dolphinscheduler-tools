package iquantex.com.dolphinscheduler.pojo;

/**
 * @author mujp
 */
public class ProcessDefinition {
    private Long id;
    private String name;
    private int releaseState;
    private int projectId;
    private int userId;
    private String processDefinitionJson;
    private String description="";
    private String globalParams;
    private String locations;
    private String connects;
    private int tenantId;

    public ProcessDefinition() {
    }

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

    public int getReleaseState() {
        return releaseState;
    }

    public void setReleaseState(int releaseState) {
        this.releaseState = releaseState;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getProcessDefinitionJson() {
        return processDefinitionJson;
    }

    public void setProcessDefinitionJson(String processDefinitionJson) {
        this.processDefinitionJson = processDefinitionJson;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGlobalParams() {
        return globalParams;
    }

    public void setGlobalParams(String globalParams) {
        this.globalParams = globalParams;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getConnects() {
        return connects;
    }

    public void setConnects(String connects) {
        this.connects = connects;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "ProcessDefinition{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", releaseState=" + releaseState +
                ", projectId=" + projectId +
                ", userId=" + userId +
                ", processDefinitionJson='" + processDefinitionJson + '\'' +
                ", description='" + description + '\'' +
                ", globalParams='" + globalParams + '\'' +
                ", locations='" + locations + '\'' +
                ", connects='" + connects + '\'' +
                ", tenantId=" + tenantId +
                '}';
    }
}
