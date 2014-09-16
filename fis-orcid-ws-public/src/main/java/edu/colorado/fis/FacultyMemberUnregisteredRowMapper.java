package edu.colorado.fis;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FacultyMemberUnregisteredRowMapper implements RowMapper {
	
	public Object mapRow(ResultSet rs, int rowNum) 
		throws SQLException {
		FacultyMember facultyMember = new FacultyMember();
		facultyMember.setFisId(rs.getString("fis_id"));
        facultyMember.setSubmissionEmail(rs.getString("submission_email"));
        facultyMember.setSubmissionFirstName(rs.getString("submission_first_name"));
        facultyMember.setSubmissionLastName(rs.getString("submission_last_name"));
        facultyMember.setSubmissionNameType(rs.getString("submission_name_type"));
        return facultyMember;
	}
}
