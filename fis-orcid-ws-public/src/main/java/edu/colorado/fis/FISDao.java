package edu.colorado.fis;

import oracle.jdbc.OracleTypes;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import java.util.*;

/**
 * Provides data access to the Faculty Information System database stored
 * procedures associated with the ORCID package.
 * 
 * @author Vance Howard
 * 
 */
public class FISDao {
    public static final String UPDATE_BY_WEB_SERVICE    = "FIS ORCID Web Service";
    private Log log = LogFactory.getLog(getClass());
	private DataSource dataSource;

    public FacultyMember getFacultyMember(String orcidRecordId)
            throws DataAccessFault {
        Collection<FacultyMember> result = new ArrayList<FacultyMember>();

        try {
            Map inParameters = new HashMap();
            inParameters.put(FacultyMember.ORCID_RECORD_ID, orcidRecordId);
            StoredProcedure sproc = new GetOrcidRecordStoredProcedure(
                    dataSource);
            Map results = sproc.execute(inParameters);
            result = (ArrayList<FacultyMember>) results.get("out");
        } catch (DataAccessException e) {
            this.handleDataAccessException(e);
        } catch (Exception e) {
            this.handleGenericException(e);
        }
        for (FacultyMember fm : result) {
            return fm;
        }

        return null; //Return null if no records found
    }

    public FacultyMember getFacultyMemberByOrcidId(String orcidId)
            throws DataAccessFault {
        Collection<FacultyMember> result = new ArrayList<FacultyMember>();

        try {
            Map inParameters = new HashMap();
            inParameters.put(FacultyMember.ORCID_ID, orcidId);
            StoredProcedure sproc = new GetOrcidRecordByOrcidIdStoredProcedure(
                    dataSource);
            Map results = sproc.execute(inParameters);
            result = (ArrayList<FacultyMember>) results.get("out");
        } catch (DataAccessException e) {
            this.handleDataAccessException(e);
        } catch (Exception e) {
            this.handleGenericException(e);
        }
        for (FacultyMember fm : result) {
            return fm;
        }

        return null; //Return null if no records found
    }

    public Collection<FacultyMember> getFacultyMembersPolling()
            throws DataAccessFault {
        Collection<FacultyMember> result = new ArrayList<FacultyMember>();

        try {
            StoredProcedure sproc = new GetFacultyMembersPollingStoredProcedure(
                    dataSource);
            Map results = sproc.execute(new HashMap()); // the stored procedure has no input parameters
            result = (ArrayList<FacultyMember>) results.get("out");
        } catch (DataAccessException e) {
            this.handleDataAccessException(e);
        } catch (Exception e) {
            this.handleGenericException(e);
        }
        return result;
    }

    public Collection<FacultyMember> getFacultyMembersToRegister()
            throws DataAccessFault {
        Collection<FacultyMember> result = new ArrayList<FacultyMember>();

        try {
            StoredProcedure sproc = new GetFacultyMembersToRegisterStoredProcedure(
                    dataSource);
            Map results = sproc.execute(new HashMap()); // the stored procedure has no input parameters
            result = (ArrayList<FacultyMember>) results.get("out");
        } catch (DataAccessException e) {
            this.handleDataAccessException(e);
        } catch (Exception e) {
            this.handleGenericException(e);
        }
        return result;
    }

    public FacultyMember createOrcidRecord(FacultyMember fm)
            throws DataAccessFault {

        List<FacultyMember> fms = new ArrayList<FacultyMember>();

        try {
            Map inParameters = new HashMap();
            inParameters.put(FacultyMember.FIS_ID,                   fm.getFisId());
            inParameters.put(FacultyMember.ORCID_ID,                 fm.getOrcidId());
            inParameters.put(FacultyMember.FIS_ORCID_SOURCE,         fm.getFisOrcidSource());
            inParameters.put(FacultyMember.SUBMISSION_EMAIL,         fm.getSubmissionEmail());
            inParameters.put(FacultyMember.SUBMISSION_FIRST_NAME,    fm.getSubmissionFirstName());
            inParameters.put(FacultyMember.SUBMISSION_LAST_NAME,     fm.getSubmissionLastName());
            inParameters.put(FacultyMember.SUBMISSION_NAME_TYPE,     fm.getSubmissionNameType());
            inParameters.put(FacultyMember.UPDATE_BY,                fm.getUpdateBy());
            StoredProcedure sproc = new CreateOrcidRecordStoredProcedure(
                    dataSource);
            Map results = sproc.execute(inParameters);
            fms = (ArrayList<FacultyMember>) results.get("out");

        } catch (DataAccessException e) {
            this.handleSaveException(e, fm);
        } catch (Exception e) {
            this.handleGenericException(e);
        }

        return fms.get(0);
    }

    public FacultyMember saveFacultyMember(FacultyMember fm)
        throws DataAccessFault {

        List<FacultyMember> fms = new ArrayList<FacultyMember>();

        try {
            Map inParameters = new HashMap();
            inParameters.put(FacultyMember.ORCID_RECORD_ID,          fm.getOrcidRecordId());
            inParameters.put(FacultyMember.ORCID_ID,                 fm.getOrcidId());
            inParameters.put(FacultyMember.ORCID_GIVEN_NAMES,        fm.getOrcidGivenNames());
            inParameters.put(FacultyMember.ORCID_FAMILY_NAME,        fm.getOrcidFamilyName());
            inParameters.put(FacultyMember.ORCID_EMAIL,              fm.getOrcidEmail());
            inParameters.put(FacultyMember.ORCID_CREATION_METHOD,    fm.getOrcidCreationMethod());
            inParameters.put(FacultyMember.ORCID_SUBMISSION_DATE,    fm.getOrcidSubmissionDate());
            inParameters.put(FacultyMember.ORCID_COMPLETION_DATE,    fm.getOrcidCompletionDate());
            inParameters.put(FacultyMember.ORCID_LAST_MODIFIED_DATE, fm.getOrcidLastModifiedDate());
            inParameters.put(FacultyMember.ORCID_CLAIMED,            fm.getOrcidClaimed());
            inParameters.put(FacultyMember.ORCID_DEACTIVATION_DATE,  fm.getOrcidDeactivationDate());
            StoredProcedure sproc = new SaveOrcidRecordStoredProcedure(
                    dataSource);
            Map results = sproc.execute(inParameters);
            fms = (ArrayList<FacultyMember>) results.get("out");

        } catch (DataAccessException e) {
            this.handleSaveException(e, fm);
        } catch (Exception e) {
            this.handleGenericException(e);
        }

        return fms.get(0);
    }

    public FacultyMember setOrcidRecordDeprecated(FacultyMember fm)
            throws DataAccessFault {

        List<FacultyMember> fms = new ArrayList<FacultyMember>();

        try {
            Map inParameters = new HashMap();
            inParameters.put(FacultyMember.ORCID_RECORD_ID,          fm.getOrcidRecordId());
            inParameters.put(FacultyMember.ORCID_ID,                 fm.getOrcidId());
            inParameters.put(FacultyMember.ORCID_PRIMARY_RECORD,     fm.getOrcidPrimaryRecord());
            StoredProcedure sproc = new SetOrcidRecordDeprecatedStoredProcedure(
                    dataSource);
            Map results = sproc.execute(inParameters);
            fms = (ArrayList<FacultyMember>) results.get("out");

        } catch (DataAccessException e) {
            this.handleSaveException(e, fm);
        } catch (Exception e) {
            this.handleGenericException(e);
        }

        return fms.get(0);
    }

    public OrcidAction logOrcidAction(OrcidAction orcidAction)
            throws DataAccessFault {

        List<OrcidAction> orcidActions = new ArrayList<OrcidAction>();

        try {
            Map inParameters = new HashMap();
            inParameters.put(OrcidAction.FIS_ID,                     orcidAction.getFisId());
            inParameters.put(OrcidAction.ACTION_DESC,                orcidAction.getActionDesc());
            inParameters.put(OrcidAction.UPDATE_BY,                  orcidAction.getUpdateBy());
            inParameters.put(OrcidAction.NOTES,                      orcidAction.getNotes());
            StoredProcedure sproc = new LogOrcidActionStoredProcedure(
                    dataSource);
            Map results = sproc.execute(inParameters);
            orcidActions = (ArrayList<OrcidAction>) results.get("out");

        } catch (DataAccessException e) {
            this.handleSaveException(e);
        } catch (Exception e) {
            this.handleGenericException(e);
        }

        return orcidActions.get(0);
    }

    /**** STORED PROCEDURE WRAPPERS *******/

    private class GetOrcidRecordStoredProcedure extends
            StoredProcedure {

        private static final String SPROC_NAME = "ORCID.getOrcidRecord";

        public GetOrcidRecordStoredProcedure(DataSource dataSource) {
            super(dataSource, SPROC_NAME);
            this.setFunction(true);
            declareParameter(new SqlOutParameter("out",
                    oracle.jdbc.OracleTypes.CURSOR, new FacultyMemberRowMapper()));
            declareParameter(new SqlParameter(FacultyMember.ORCID_RECORD_ID, OracleTypes.VARCHAR));
            compile();
        }
    }

    private class GetOrcidRecordByOrcidIdStoredProcedure extends
            StoredProcedure {

        private static final String SPROC_NAME = "ORCID.getOrcidRecordByOrcidId";

        public GetOrcidRecordByOrcidIdStoredProcedure(DataSource dataSource) {
            super(dataSource, SPROC_NAME);
            this.setFunction(true);
            declareParameter(new SqlOutParameter("out",
                    oracle.jdbc.OracleTypes.CURSOR, new FacultyMemberRowMapper()));
            declareParameter(new SqlParameter(FacultyMember.ORCID_ID, OracleTypes.VARCHAR));
            compile();
        }
    }

    private class GetFacultyMembersPollingStoredProcedure extends
            StoredProcedure {

        private static final String SPROC_NAME = "ORCID.getFacultyMembersPolling";

        public GetFacultyMembersPollingStoredProcedure(DataSource dataSource) {
            super(dataSource, SPROC_NAME);
            this.setFunction(true);
            declareParameter(new SqlOutParameter("out",
                    oracle.jdbc.OracleTypes.CURSOR, new FacultyMemberRowMapper()));
            compile();
        }
    }

    private class GetFacultyMembersToRegisterStoredProcedure extends
            StoredProcedure {

        private static final String SPROC_NAME = "ORCID.getFacultyMembersToRegister";

        public GetFacultyMembersToRegisterStoredProcedure(DataSource dataSource) {
            super(dataSource, SPROC_NAME);
            this.setFunction(true);
            declareParameter(new SqlOutParameter("out",
                    oracle.jdbc.OracleTypes.CURSOR, new FacultyMemberUnregisteredRowMapper()));
            compile();
        }
    }

    private class CreateOrcidRecordStoredProcedure extends
            StoredProcedure {

        private static final String SPROC_NAME = "ORCID.createOrcidRecord";

        public CreateOrcidRecordStoredProcedure(DataSource dataSource) {
            super(dataSource, SPROC_NAME);
            this.setFunction(true);
            declareParameter(new SqlOutParameter("out",
                    oracle.jdbc.OracleTypes.CURSOR, new FacultyMemberRowMapper()));
            declareParameter(new SqlParameter(FacultyMember.FIS_ID,                   OracleTypes.NUMBER));
            declareParameter(new SqlParameter(FacultyMember.ORCID_ID,                 OracleTypes.VARCHAR));
            declareParameter(new SqlParameter(FacultyMember.FIS_ORCID_SOURCE,         OracleTypes.VARCHAR));
            declareParameter(new SqlParameter(FacultyMember.SUBMISSION_EMAIL,         OracleTypes.VARCHAR));
            declareParameter(new SqlParameter(FacultyMember.SUBMISSION_FIRST_NAME,    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter(FacultyMember.SUBMISSION_LAST_NAME, OracleTypes.VARCHAR));
            declareParameter(new SqlParameter(FacultyMember.SUBMISSION_NAME_TYPE, OracleTypes.VARCHAR));
            declareParameter(new SqlParameter(FacultyMember.UPDATE_BY,                OracleTypes.VARCHAR));
            compile();
        }
    }

    private class SaveOrcidRecordStoredProcedure extends
            StoredProcedure {

        private static final String SPROC_NAME = "ORCID.saveOrcidRecord";

        public SaveOrcidRecordStoredProcedure(DataSource dataSource) {
            super(dataSource, SPROC_NAME);
            this.setFunction(true);
            declareParameter(new SqlOutParameter("out",
                    oracle.jdbc.OracleTypes.CURSOR, new FacultyMemberRowMapper()));
            declareParameter(new SqlParameter(FacultyMember.ORCID_RECORD_ID, OracleTypes.NUMBER));
            declareParameter(new SqlParameter(FacultyMember.ORCID_ID, OracleTypes.VARCHAR));
            declareParameter(new SqlParameter(FacultyMember.ORCID_GIVEN_NAMES,        OracleTypes.VARCHAR));
            declareParameter(new SqlParameter(FacultyMember.ORCID_FAMILY_NAME,        OracleTypes.VARCHAR));
            declareParameter(new SqlParameter(FacultyMember.ORCID_EMAIL,              OracleTypes.VARCHAR));
            declareParameter(new SqlParameter(FacultyMember.ORCID_CREATION_METHOD,    OracleTypes.VARCHAR));
            declareParameter(new SqlParameter(FacultyMember.ORCID_SUBMISSION_DATE,    OracleTypes.TIMESTAMP));
            declareParameter(new SqlParameter(FacultyMember.ORCID_COMPLETION_DATE,    OracleTypes.TIMESTAMP));
            declareParameter(new SqlParameter(FacultyMember.ORCID_LAST_MODIFIED_DATE, OracleTypes.TIMESTAMP));
            declareParameter(new SqlParameter(FacultyMember.ORCID_CLAIMED,            OracleTypes.VARCHAR));
            declareParameter(new SqlParameter(FacultyMember.ORCID_DEACTIVATION_DATE,  OracleTypes.TIMESTAMP));
            compile();
        }
    }

    private class SetOrcidRecordDeprecatedStoredProcedure extends
            StoredProcedure {

        private static final String SPROC_NAME = "ORCID.setOrcidRecordDeprecated";

        public SetOrcidRecordDeprecatedStoredProcedure(DataSource dataSource) {
            super(dataSource, SPROC_NAME);
            this.setFunction(true);
            declareParameter(new SqlOutParameter("out",
                    oracle.jdbc.OracleTypes.CURSOR, new FacultyMemberRowMapper()));
            declareParameter(new SqlParameter(FacultyMember.ORCID_RECORD_ID,          OracleTypes.NUMBER));
            declareParameter(new SqlParameter(FacultyMember.ORCID_ID,                 OracleTypes.VARCHAR));
            declareParameter(new SqlParameter(FacultyMember.ORCID_PRIMARY_RECORD,     OracleTypes.VARCHAR));
            compile();
        }
    }

    private class LogOrcidActionStoredProcedure extends
            StoredProcedure {

        private static final String SPROC_NAME = "ORCID.logOrcidAction";

        public LogOrcidActionStoredProcedure(DataSource dataSource) {
            super(dataSource, SPROC_NAME);
            this.setFunction(true);
            declareParameter(new SqlOutParameter("out",
                    oracle.jdbc.OracleTypes.CURSOR, new OrcidActionRowMapper()));
            declareParameter(new SqlParameter(OrcidAction.FIS_ID,                     OracleTypes.NUMBER));
            declareParameter(new SqlParameter(OrcidAction.ACTION_DESC,                OracleTypes.VARCHAR));
            declareParameter(new SqlParameter(OrcidAction.UPDATE_BY,                  OracleTypes.VARCHAR));
            declareParameter(new SqlParameter(OrcidAction.NOTES,                      OracleTypes.VARCHAR));
            compile();
        }
    }

    /**** GETTERS/SETTERS *******/

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

    /**** UTILITY METHODS *******/

	private void handleInvalidParameterException() throws DataAccessFault {
		DataAccessFault daf = new DataAccessFault();
		daf.setCode(DataAccessFault.INVALID_PARAMETER_CODE);
		daf.setMessage(DataAccessFault.INVALID_PARAMETER_MSG);
		daf.setDetail(DataAccessFault.INVALID_PARAMETER_DETAIL);
        log.error(daf);
        daf.printStackTrace();
        throw daf;
	}

	private void handleDataAccessException(Exception e) throws DataAccessFault {
		log.error(e);
		DataAccessFault daf = new DataAccessFault(e);
		daf.setCode(DataAccessFault.SERVICE_FAILURE_CODE);
		daf.setMessage(DataAccessFault.SERVICE_FAILURE_MSG);
		daf.setDetail(DataAccessFault.SERVICE_FAILURE_DETAIL_UNABLE_TO_CONNECT);
        log.error(daf);
        daf.printStackTrace();
        throw daf;
	}

	private void handleGenericException(Exception e) throws DataAccessFault {
		DataAccessFault daf = new DataAccessFault(e);
		daf.setCode(DataAccessFault.SERVICE_FAILURE_CODE);
		daf.setMessage(DataAccessFault.SERVICE_FAILURE_MSG);
		daf.setDetail(DataAccessFault.SERVICE_FAILURE_DETAIL_DEFAULT);
        log.error(daf);
        daf.printStackTrace();
        throw daf;
	}

    private void handleSaveException(Exception e, FacultyMember fm) throws DataAccessFault {
        DataAccessFault daf = new DataAccessFault(e);
        daf.setCode(DataAccessFault.SAVE_FAILURE_CODE);
        String msg = DataAccessFault.SAVE_FAILURE_MSG + ": " + fm;
        daf.setMessage(msg);
        daf.setDetail(msg);
        log.error(daf);
        daf.printStackTrace();
        throw daf;
    }

    private void handleSaveException(Exception e) throws DataAccessFault {
        DataAccessFault daf = new DataAccessFault(e);
        daf.setCode(DataAccessFault.SAVE_FAILURE_CODE);
        String msg = DataAccessFault.SAVE_FAILURE_MSG;
        daf.setMessage(msg);
        daf.setDetail(msg);
        log.error(daf);
        daf.printStackTrace();
        throw daf;
    }
}
