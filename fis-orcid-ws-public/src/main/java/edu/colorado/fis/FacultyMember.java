package edu.colorado.fis;


import edu.colorado.orcid.mvc.OrcidProfile;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FacultyMember {

    public static final String ORCID_RECORD_ID          = "orcidRecordId";
    public static final String FIS_ID                   = "fisId";
    public static final String ORCID_ID                 = "orcidId";
    public static final String FIS_ORCID_SOURCE         = "fisOrcidSource";
    public static final String SUBMISSION_EMAIL         = "submissionEmail";
    public static final String SUBMISSION_FIRST_NAME    = "submissionFirstName";
    public static final String SUBMISSION_LAST_NAME     = "submissionLastName";
    public static final String SUBMISSION_NAME_TYPE     = "submissionNameType";
    public static final String ORCID_GIVEN_NAMES        = "orcidGivenNames";
    public static final String ORCID_FAMILY_NAME        = "orcidFamilyName";
    public static final String ORCID_EMAIL              = "orcidEmail";
    public static final String ORCID_CREATION_METHOD    = "orcidCreationMethod";
    public static final String ORCID_SUBMISSION_DATE    = "orcidSubmissionDate";
    public static final String ORCID_COMPLETION_DATE    = "orcidCompletionDate";
    public static final String ORCID_LAST_MODIFIED_DATE = "orcidLastModifiedDate";
    public static final String ORCID_CLAIMED            = "orcidClaimed";
    public static final String ORCID_DEACTIVATION_DATE  = "orcidDeactivationDate";
    public static final String ORCID_PRIMARY_RECORD     = "orcidPrimaryRecord";
    public static final String UPDATE_BY                = "updateBy";
    private String fisId;
	private String orcidId;
    private String orcidRecordId;
    private String fisOrcidSource;
    private String submissionEmail;
    private String submissionFirstName;
    private String submissionLastName;
    private String submissionNameType;
    private String orcidGivenNames;
    private String orcidFamilyName;
    private String orcidEmail;
    private String orcidCreationMethod;
    private java.util.Date orcidSubmissionDate;
    private java.util.Date orcidCompletionDate;
    private java.util.Date orcidLastModifiedDate;
    private String orcidClaimed;
    private String orcidDeprecated;
    private java.util.Date orcidDeactivationDate;
    private String orcidPrimaryRecord;
    private String updateBy;
    private java.util.Date updateDate;
    private String notes;

    public static String FIS_ORCID_SOURCE_ORCID = "O";
    public static String FIS_ORCID_SOURCE_FACULTY_MEMBER = "F";
    public static String SUBMISSION_NAME_TYPE_PREFERRED = "P";
    public static String SUBMISSION_NAME_TYPE_HR = "H";
    public static String ORCID_CREATION_METHOD_WEBSITE = "Website";
    public static String ORCID_CREATION_METHOD_API = "API";
    public static String ORCID_CLAIMED_TRUE = "T";
    public static String ORCID_CLAIMED_FALSE = "F";
    public static String ORCID_DEPRECATED_TRUE = "T";
    public static String ORCID_DEPRECATED_FALSE = "F";
    public static String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    private SimpleDateFormat formatter = new SimpleDateFormat(FacultyMember.DATE_FORMAT);
    private Log log = LogFactory.getLog(getClass());

    public void mergeOrcidProfile(OrcidProfile profile) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(FacultyMember.DATE_FORMAT);
        this.setOrcidGivenNames((String) profile.get(OrcidProfile.GIVEN_NAMES));
        this.setOrcidFamilyName((String) profile.get(OrcidProfile.FAMILY_NAME));
        this.setOrcidEmail((String) profile.get(OrcidProfile.EMAIL));
        this.setOrcidCreationMethod((String) profile.get(OrcidProfile.CREATION_METHOD));
        this.setOrcidSubmissionDate(parseDate((String) profile.get(OrcidProfile.SUBMISSION_DATE)));
        this.setOrcidCompletionDate(parseDate((String) profile.get(OrcidProfile.COMPLETION_DATE)));
        this.setOrcidLastModifiedDate(parseDate((String) profile.get(OrcidProfile.LAST_MODIFICATION_DATE)));
        this.setOrcidClaimed(convertTrueFalse((String) profile.get(OrcidProfile.CLAIMED)));
        this.setOrcidDeactivationDate(parseDate((String) profile.get(OrcidProfile.DEACTIVATION_DATE)));
    }

    private Date parseDate(String stringDate) throws ParseException {
        if (stringDate == null || stringDate.isEmpty()) {
            return null;
        }

        return formatter.parse(stringDate);
    }

    private String convertTrueFalse(String inString) throws ParseException {
        if (inString == null || inString.isEmpty()) {
            return null;
        }

        if (inString.equalsIgnoreCase("True")){
            return "T";
        }

        if (inString.equalsIgnoreCase("False")){
            return "F";
        }

        String msg = "Unknown True or False value - Setting value null for " + this.toString();
        log.error(msg);
        throw new ParseException(msg, 0);
    }

    public String getFisId() {
		return fisId;
	}

	public void setFisId(String fisId) {
		this.fisId = fisId;
	}

	public String getOrcidId() {
		return orcidId;
	}

	public void setOrcidId(String orcidId) {
		this.orcidId = orcidId;
	}

    public String getOrcidRecordId() {
        return orcidRecordId;
    }

    public void setOrcidRecordId(String orcidRecordId) {
        this.orcidRecordId = orcidRecordId;
    }

    public String getFisOrcidSource() {
        return fisOrcidSource;
    }

    public void setFisOrcidSource(String fisOrcidSource) {
        this.fisOrcidSource = fisOrcidSource;
    }

    public String getSubmissionEmail() {
        return submissionEmail;
    }

    public void setSubmissionEmail(String submissionEmail) {
        this.submissionEmail = submissionEmail;
    }

    public String getSubmissionFirstName() {
        return submissionFirstName;
    }

    public void setSubmissionFirstName(String submissionFirstName) {
        this.submissionFirstName = submissionFirstName;
    }

    public String getSubmissionLastName() {
        return submissionLastName;
    }

    public void setSubmissionLastName(String submissionLastName) {
        this.submissionLastName = submissionLastName;
    }

    public String getSubmissionNameType() {
        return submissionNameType;
    }

    public void setSubmissionNameType(String submissionNameType) {
        this.submissionNameType = submissionNameType;
    }

    public String getOrcidGivenNames() {
        return orcidGivenNames;
    }

    public void setOrcidGivenNames(String orcidGivenNames) {
        this.orcidGivenNames = orcidGivenNames;
    }

    public String getOrcidFamilyName() {
        return orcidFamilyName;
    }

    public void setOrcidFamilyName(String orcidFamilyName) {
        this.orcidFamilyName = orcidFamilyName;
    }

    public String getOrcidEmail() {
        return orcidEmail;
    }

    public void setOrcidEmail(String orcidEmail) {
        this.orcidEmail = orcidEmail;
    }

    public String getOrcidCreationMethod() {
        return orcidCreationMethod;
    }

    public void setOrcidCreationMethod(String orcidCreationMethod) {
        this.orcidCreationMethod = orcidCreationMethod;
    }

    public java.util.Date getOrcidSubmissionDate() {
        return orcidSubmissionDate;
    }

    public void setOrcidSubmissionDate(Date orcidSubmissionDate) {
        this.orcidSubmissionDate = orcidSubmissionDate;
    }

    public java.util.Date getOrcidCompletionDate() {
        return orcidCompletionDate;
    }

    public void setOrcidCompletionDate(Date orcidCompletionDate) {
        this.orcidCompletionDate = orcidCompletionDate;
    }

    public java.util.Date getOrcidLastModifiedDate() {
        return orcidLastModifiedDate;
    }

    public void setOrcidLastModifiedDate(Date orcidLastModifiedDate) {
        this.orcidLastModifiedDate = orcidLastModifiedDate;
    }

    public String getOrcidClaimed() {
        return orcidClaimed;
    }

    public void setOrcidClaimed(String orcidClaimed) {
        this.orcidClaimed = orcidClaimed;
    }

    public String getOrcidDeprecated() {
        return orcidDeprecated;
    }

    public void setOrcidDeprecated(String orcidDeprecated) {
        this.orcidDeprecated = orcidDeprecated;
    }

    public java.util.Date getOrcidDeactivationDate() {
        return orcidDeactivationDate;
    }

    public void setOrcidDeactivationDate(Date orcidDeactivationDate) {
        this.orcidDeactivationDate = orcidDeactivationDate;
    }

    public String getOrcidPrimaryRecord() {
        return orcidPrimaryRecord;
    }

    public void setOrcidPrimaryRecord(String orcidPrimaryRecord) {
        this.orcidPrimaryRecord = orcidPrimaryRecord;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public java.util.Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /* (non-Javadoc)
      * @see java.lang.Object#hashCode()
      */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fisId == null) ? 0 : fisId.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FacultyMember other = (FacultyMember) obj;
		if (fisId == null) {
			if (other.fisId != null)
				return false;
		} else if (!fisId.equals(other.fisId))
			return false;
		return true;
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Faculty Member - FIS Id => " + fisId
				+ "; ORCID iD => " + orcidId
                + "; orcidRecordId => " + orcidRecordId
                + "; fisOrcidSource => " + fisOrcidSource
                + "; submissionEmail => " + submissionEmail
                + "; submissionFirstName => " + submissionFirstName
                + "; submissionLastName => " + submissionLastName
                + "; submissionNameType => " + submissionNameType
                + "; orcidGivenNames => " + orcidGivenNames
                + "; orcidFamilyName => " + orcidFamilyName
                + "; orcidEmail => " + orcidEmail
                + "; orcidCreationMethod => " + orcidCreationMethod
                + "; orcidSubmissionDate => " + orcidSubmissionDate
                + "; orcidCompletionDate => " + orcidCompletionDate
                + "; orcidLastModifiedDate => " + orcidLastModifiedDate
                + "; orcidClaimed => " + orcidClaimed
                + "; orcidDeprecated => " + orcidDeprecated
                + "; orcidDeactivationDate => " + orcidDeactivationDate
                + "; orcidPrimaryRecord => " + orcidPrimaryRecord
                + "; updateBy => " + updateBy
                + "; updateDate => " + updateDate
                + "; notes => " + notes
        );
		return buf.toString();
	}


}
