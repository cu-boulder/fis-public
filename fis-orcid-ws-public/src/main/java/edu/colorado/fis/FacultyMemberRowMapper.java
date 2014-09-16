package edu.colorado.fis;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FacultyMemberRowMapper implements RowMapper {
	
	public Object mapRow(ResultSet rs, int rowNum) 
		throws SQLException {
		FacultyMember facultyMember = new FacultyMember();
		facultyMember.setFisId(rs.getString("fis_id"));
		facultyMember.setOrcidId(rs.getString("orcid_id"));
        facultyMember.setOrcidRecordId(rs.getString("orcid_record_id"));
        facultyMember.setFisOrcidSource(rs.getString("fis_orcid_source"));
        facultyMember.setSubmissionEmail(rs.getString("submission_email"));
        facultyMember.setSubmissionFirstName(rs.getString("submission_first_name"));
        facultyMember.setSubmissionLastName(rs.getString("submission_last_name"));
        facultyMember.setSubmissionNameType(rs.getString("submission_name_type"));
        facultyMember.setOrcidGivenNames(rs.getString("orcid_given_names"));
        facultyMember.setOrcidFamilyName(rs.getString("orcid_family_name"));
        facultyMember.setOrcidEmail(rs.getString("orcid_email"));
        facultyMember.setOrcidCreationMethod(rs.getString("orcid_creation_method"));
        facultyMember.setOrcidSubmissionDate(rs.getTimestamp("orcid_submission_date"));
        facultyMember.setOrcidCompletionDate(rs.getTimestamp("orcid_completion_date"));
        facultyMember.setOrcidLastModifiedDate(rs.getTimestamp("orcid_last_modified_date"));
        facultyMember.setOrcidClaimed(rs.getString("orcid_claimed"));
        facultyMember.setOrcidDeprecated(rs.getString("orcid_deprecated"));
        facultyMember.setOrcidDeactivationDate(rs.getTimestamp("orcid_deactivation_date"));
        facultyMember.setOrcidPrimaryRecord(rs.getString("orcid_primary_record"));
        facultyMember.setUpdateBy(rs.getString("update_by"));
        facultyMember.setUpdateDate(rs.getTimestamp("update_date"));
        facultyMember.setNotes(rs.getString("notes"));
        return facultyMember;
	}
}
