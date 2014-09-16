package edu.colorado.fis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

public class OrcidAction {

    public static final String FIS_ID                    = "fisId";
    public static final String ACTION_DESC               = "actionDesc";
    public static final String UPDATE_BY                 = "updateBy";
    public static final String NOTES                     = "notes";
    public static final String ACTION_DESC_EMAIL_EXISTS  = "Create ORCID failed - email already exists";
    public static final String ACTION_DESC_ORCID_CREATED = "ORCID iD created";
    private String orcidActionId;
    private String fisId;
    private String actionDesc;
    private String updateBy;
    private Date updateDate;
    private String notes;

    private Log log = LogFactory.getLog(getClass());


    public String getOrcidActionId() {
        return orcidActionId;
    }

    public void setOrcidActionId(String orcidActionId) {
        this.orcidActionId = orcidActionId;
    }

    public String getFisId() {
        return fisId;
    }

    public void setFisId(String fisId) {
        this.fisId = fisId;
    }

    public String getActionDesc() {
        return actionDesc;
    }

    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public java.util.Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("Orcid Action - FIS Id => " + fisId
                + "; orcidActionId => " + orcidActionId
                + "; actionDesc => " + actionDesc
                + "; updateBy => " + updateBy
                + "; updateDate => " + updateDate
                + "; notes => " + notes
        );
        return buf.toString();
    }
}
