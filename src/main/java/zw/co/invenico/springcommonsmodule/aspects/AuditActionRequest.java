package zw.co.invenico.springcommonsmodule.aspects;

import lombok.Data;

@Data
public class AuditActionRequest {

    private String resource;

    private String action;

    public AuditActionRequest(String resource, String action) {
        this.resource = resource;
        this.action = action;
    }
}
