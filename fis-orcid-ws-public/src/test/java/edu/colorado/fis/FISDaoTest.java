package edu.colorado.fis;

import edu.colorado.BaseTest;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * Tests data access to the FIS test database, checking for exception conditions. Note
 * that this class uses direct access to the FIS test database instance. This test
 * exercises the FIS Data Access Object for the FIS ORCID Web Service data access.
 */
public class FISDaoTest extends BaseTest {

    protected final Log log = LogFactory.getLog(getClass());

    /**** CONNECTION TESTS *******/

	public void testNullDataSource() {
		dao.setDataSource(null);
		try {
			dao.getFacultyMembersPolling();
			fail("Null datasource should throw an exception");
		}
		catch (DataAccessFault expected) {
			assertEquals(DataAccessFault.SERVICE_FAILURE_CODE, expected
					.getCode());
			assertEquals(DataAccessFault.SERVICE_FAILURE_MSG, expected
					.getMessage());
			assertEquals(
					DataAccessFault.SERVICE_FAILURE_DETAIL_UNABLE_TO_CONNECT,
					expected.getDetail());
		}
	}

	public void testGetFacultyMembersIncorrectPassword() {
		ds.setPassword("incorrectpassword");
		try {
			dao.setDataSource(ds);
			dao.getFacultyMembersPolling();
			fail("Invalid password should throw an exception");
		} catch (DataAccessFault expected) {
			assertEquals(DataAccessFault.SERVICE_FAILURE_CODE, expected
					.getCode());
			assertEquals(DataAccessFault.SERVICE_FAILURE_MSG, expected
					.getMessage());
			assertEquals(
					DataAccessFault.SERVICE_FAILURE_DETAIL_UNABLE_TO_CONNECT,
					expected.getDetail());
		}
	}

    /**** POLLING TESTS *******/

    public void testGetFacultyMember() {
        try {
            this.resetBaselineFacultyMember();
        } catch (DataAccessFault daf) {
            fail("Error resetting baseline Faculty Member");
            daf.printStackTrace();
        }
        FacultyMember expected = makeBaselineFacultyMember();
        FacultyMember actual = new FacultyMember();
        try {
            actual = dao.getFacultyMember(expected.getOrcidRecordId());
        } catch (DataAccessFault daf) {
            fail("Error resetting baseline Faculty Member");
            daf.printStackTrace();
        }
        assertEquals(expected.getFisId(), actual.getFisId());
        assertEquals(expected.getOrcidId(), actual.getOrcidId());
        assertEquals(expected.getOrcidPrimaryRecord(), actual.getOrcidPrimaryRecord());
    }

    public void testGetFacultyMemberByOrcidId() {
        try {
            this.resetBaselineFacultyMember();
        } catch (DataAccessFault daf) {
            fail("Error resetting baseline Faculty Member");
            daf.printStackTrace();
        }
        FacultyMember expected = makeBaselineFacultyMember();
        FacultyMember actual = new FacultyMember();
        try {
            actual = dao.getFacultyMemberByOrcidId(expected.getOrcidId());
        } catch (DataAccessFault daf) {
            fail("Error resetting baseline Faculty Member");
            daf.printStackTrace();
        }
        assertEquals(expected.getFisId(), actual.getFisId());
        assertEquals(expected.getOrcidId(), actual.getOrcidId());
        assertEquals(expected.getOrcidPrimaryRecord(), actual.getOrcidPrimaryRecord());
    }

    public void testGetFacultyMembersPolling() {
        FacultyMember expected = new FacultyMember();
        expected.setFisId("112946"); //Liz Tomich
        expected.setOrcidId("0000-0002-0493-5108");
        log.debug("Expected Faculty Member -> " + expected);
        boolean found = false;
        Collection<FacultyMember> fms= new ArrayList<FacultyMember>();
        try {
            fms = dao.getFacultyMembersPolling();
        } catch (DataAccessFault e) {
            fail(e.getMessage());
        }
        for (FacultyMember actual : fms) {
            log.debug("Actual Faculty Member -> " + actual.toString());
            if (expected.getFisId().equalsIgnoreCase(actual.getFisId())) {
                assertEquals(expected.getFisId(), actual.getFisId());
                found = true;
            }
        }
        Assert.assertTrue("All Expected Fields Faculty Member not found", found);
    }

    public void testSaveOrcidRecord() {
        FacultyMember expected = this.makeBaselineFacultyMember();
        FacultyMember actual = null;
        try {
            resetBaselineFacultyMember();
            log.debug("Expected Faculty Member -> " + expected);
            actual = dao.saveFacultyMember(expected);
            log.debug("Actual Faculty Member -> " + actual);
        } catch (DataAccessFault e) {
            fail(e.getMessage());
        }
        assertEquals(expected, actual);
    }

    public void testSaveOrcidRecordMissingOrcidId() {
        FacultyMember expected = makeBaselineFacultyMember();
        expected.setOrcidId("");
        FacultyMember actual = null;
        try {
            resetBaselineFacultyMember();
            log.debug("Expected Faculty Member -> " + expected);
            actual = dao.saveFacultyMember(expected);
            log.debug("Actual Faculty Member -> " + actual);
            fail("Missing ORCID iD should throw data access fault");
        } catch (DataAccessFault e) {
            assertEquals(e.getCode(), DataAccessFault.SAVE_FAILURE_CODE);
        }
    }

    public void testSaveOrcidRecordUpdateClaimedStatus() {
        FacultyMember expected = makeBaselineFacultyMember();
        expected.setOrcidClaimed(FacultyMember.ORCID_CLAIMED_FALSE);
        FacultyMember actual = null;
        try {
            resetBaselineFacultyMember();
            actual = dao.saveFacultyMember(expected);
            assertEquals(FacultyMember.ORCID_CLAIMED_FALSE, actual.getOrcidClaimed());
        } catch (DataAccessFault e) {
            fail(e.getMessage());
        }
    }

    public void testSaveOrcidRecordUpdateDeactivationDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd:HH:mm:ss"); //date matches to second
        FacultyMember expected = makeBaselineFacultyMember();
        Date deactivationDate = Calendar.getInstance().getTime();
        expected.setOrcidDeactivationDate(deactivationDate);
        assertEquals(expected.getOrcidDeactivationDate(), deactivationDate);
        FacultyMember actual = null;
        try {
            resetBaselineFacultyMember();
            actual = dao.saveFacultyMember(expected);
            assertEquals(formatter.format(deactivationDate), formatter.format(actual.getOrcidDeactivationDate()));
        } catch (DataAccessFault e) {
            fail(e.getMessage());
        }
    }

    public void testSetOrcidRecordDeprecated() {
        FacultyMember expected = this.makeDeprecatedFacultyMember();
        assertEquals(expected.getOrcidDeprecated(), FacultyMember.ORCID_DEPRECATED_TRUE);
        assertFalse(expected.getOrcidPrimaryRecord().isEmpty());
        FacultyMember actual = null;
        try {
            actual = dao.setOrcidRecordDeprecated(expected);
        } catch (DataAccessFault e) {
            fail(e.getMessage());
        }
        assertNull(actual.getOrcidDeprecated());
        //Deprecated primary record value should now be ORCID iD of new record
        assertEquals(expected.getOrcidPrimaryRecord(), actual.getOrcidId());
        //Both should have same FIS Id
        assertEquals(expected.getFisId(), actual.getFisId());
        //New record should not have values in submission fields
        assertNull(actual.getSubmissionEmail());
        assertNull(actual.getSubmissionFirstName());
        assertNull(actual.getSubmissionLastName());
        assertNull(actual.getSubmissionNameType());
        assertNull(actual.getFisOrcidSource());
        //Check that notes has a value
        assertFalse(actual.getNotes().isEmpty());
        //ORCID primary record field should be empty on new record
        assertNull(actual.getOrcidPrimaryRecord());
    }

    /**** REGISTRATION TESTS *******/

    public void testGetFacultyMembersRegister() {
        FacultyMember expected = this.makeUnregisteredFacultyMember();
        log.debug("Expected Faculty Member -> " + expected);
        boolean found = false;
        Collection<FacultyMember> fms= new ArrayList<FacultyMember> ();
        try {
            fms = dao.getFacultyMembersToRegister();
        } catch (DataAccessFault e) {
            fail(e.getMessage());
        }
        for (FacultyMember actual : fms) {
            log.debug("Actual Faculty Member -> " + actual.toString());
            if (expected.getFisId().equalsIgnoreCase(actual.getFisId())) {
                assertEquals(expected.getFisId(), actual.getFisId());
                assertEquals(expected.getSubmissionFirstName(), actual.getSubmissionFirstName());
                assertEquals(expected.getSubmissionLastName(), actual.getSubmissionLastName());
                assertEquals(expected.getSubmissionNameType(), actual.getSubmissionNameType());
                found = true;
            }
        }
        Assert.assertTrue("All Expected Fields Faculty Member not found", found);
    }

    public void testCreateOrcidRecord() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); //date matches to day
        Date today = Calendar.getInstance().getTime();
        FacultyMember expected = this.makeUnregisteredFacultyMember();
        expected.setOrcidId("0000-1111-2222-3333");//ORCID is bogus
        expected.setFisOrcidSource(FacultyMember.FIS_ORCID_SOURCE_ORCID);
        expected.setUpdateBy(FISDao.UPDATE_BY_WEB_SERVICE);
        FacultyMember actual = null;
        try {
            actual = dao.createOrcidRecord(expected);
        } catch (DataAccessFault e) {
            fail(e.getMessage());
        }
        assertNotNull(actual.getOrcidRecordId());
        assertNotNull(actual.getFisId());
        assertNotNull(actual.getOrcidId());
        assertEquals(expected.getFisOrcidSource(), actual.getFisOrcidSource());
        assertEquals(expected.getSubmissionEmail(), actual.getSubmissionEmail());
        assertEquals(expected.getSubmissionFirstName(), actual.getSubmissionFirstName());
        assertEquals(expected.getSubmissionLastName(), actual.getSubmissionLastName());
        assertEquals(expected.getSubmissionNameType(), actual.getSubmissionNameType());
        assertNull(actual.getOrcidGivenNames());
        assertNull(actual.getOrcidFamilyName());
        assertNull(actual.getOrcidEmail());
        assertNull(actual.getOrcidCreationMethod());
        assertNull(actual.getOrcidSubmissionDate());
        assertNull(actual.getOrcidCompletionDate());
        assertNull(actual.getOrcidDeprecated());
        assertNull(actual.getOrcidDeactivationDate());
        assertNull(actual.getOrcidPrimaryRecord());
        assertEquals(FISDao.UPDATE_BY_WEB_SERVICE, actual.getUpdateBy());
        assertEquals(formatter.format(today), formatter.format(actual.getUpdateDate()));
        assertNull(actual.getNotes());
    }

    public void testLogOrcidAction() {
        FacultyMember fm = this.makeBaselineFacultyMember();
        OrcidAction expected = new OrcidAction();
        expected.setFisId(fm.getFisId());
        expected.setActionDesc(OrcidAction.ACTION_DESC_EMAIL_EXISTS);
        expected.setUpdateBy(FISDao.UPDATE_BY_WEB_SERVICE);
        expected.setNotes("Test notes");
        OrcidAction actual = new OrcidAction();
        try {
            actual = dao.logOrcidAction(expected);
        } catch (DataAccessFault dataAccessFault) {
            dataAccessFault.printStackTrace();
            fail();
        }
        assertNotNull(actual.getOrcidActionId());
        assertEquals(expected.getFisId(), actual.getFisId());
        assertEquals(expected.getActionDesc(), actual.getActionDesc());
        assertEquals(expected.getUpdateBy(), actual.getUpdateBy());
        assertNotNull(actual.getUpdateDate());
        assertEquals(expected.getNotes(), actual.getNotes());
    }

}
