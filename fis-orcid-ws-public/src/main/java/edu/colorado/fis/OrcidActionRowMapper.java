package edu.colorado.fis;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrcidActionRowMapper implements RowMapper {

    public Object mapRow(ResultSet rs, int rowNum)
            throws SQLException {
        OrcidAction orcidAction = new OrcidAction();
        orcidAction.setFisId(rs.getString("fis_id"));
        orcidAction.setOrcidActionId(rs.getString("orcid_action_id"));
        orcidAction.setActionDesc(rs.getString("action_desc"));
        orcidAction.setUpdateBy(rs.getString("update_by"));
        orcidAction.setUpdateDate(rs.getTimestamp("update_date"));
        orcidAction.setNotes(rs.getString("notes"));
        return orcidAction;
    }
}
