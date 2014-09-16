package edu.colorado.orcid.mvc;

import edu.colorado.BaseTest;
import edu.colorado.fis.FacultyMember;
import edu.colorado.fis.OrcidAction;
import edu.colorado.orcid.OrcidException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.ui.ExtendedModelMap;

public class OrcidControllerTest extends BaseTest {
    protected final Log log = LogFactory.getLog(getClass());

    @Test
    public void testOrcidPollHappyPath() throws Exception {
        assertEquals("result", controller.orcidPoll(new ExtendedModelMap()));
    }

    @Test
    public void testPollFacultyMember(){
        FacultyMember expected = makeBaselineFacultyMember();
        try {
            controller.pollFacultyMember(expected);
        } catch (OrcidException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testPollFacultyMemberDeprecated(){
        FacultyMember expected = makeDeprecatedFacultyMember();
        FacultyMember oldRecord = new FacultyMember();
        FacultyMember newRecord = new FacultyMember();

        try {
            controller.pollFacultyMember(expected);
            newRecord = dao.getFacultyMemberByOrcidId(expected.getOrcidPrimaryRecord());
            oldRecord = dao.getFacultyMember(expected.getOrcidRecordId());
        } catch (Exception e) {
            fail(e.getMessage());
        }
        log.debug("newRecord: " + newRecord);
        log.debug("oldRecord: " + oldRecord);        //Record is deprecated, so we expect to see that old record flagged as such
        assertEquals(FacultyMember.ORCID_DEPRECATED_TRUE, oldRecord.getOrcidDeprecated());
        assertEquals(expected.getOrcidPrimaryRecord(), oldRecord.getOrcidPrimaryRecord());

        //A new record should have been created with the correct ORCID iD
        assertEquals(expected.getOrcidPrimaryRecord(), newRecord.getOrcidId());

        //Finally, we'd expect the ORCID fields to be filled in, since the
        //new record should have been polled after it was created
        assertNotNull(newRecord.getOrcidGivenNames());
        assertNotNull(newRecord.getOrcidFamilyName());
        assertNotNull(newRecord.getOrcidCreationMethod());
        assertNotNull(newRecord.getOrcidSubmissionDate());
        assertNotNull(newRecord.getOrcidLastModifiedDate());
        assertNull(newRecord.getOrcidDeprecated());
        assertNull(newRecord.getOrcidPrimaryRecord());
        assertNotNull(newRecord.getOrcidClaimed());
    }

    @Test
    public void testPollFacultyMemberNotFound(){
        FacultyMember fm = makeNotFoundFacultyMember();
        try {
            controller.pollFacultyMember(fm);
        } catch (OrcidException e) {
            String userMsg = "Unable to find ORCID " + fm.getOrcidId() + " in ORCID repository";
            assertEquals(userMsg, e.getMessage());
        }
    }

    @Test
    public void testPollFacultyMemberDeactivated() {
        FacultyMember fm = this.makeDeactivatedFacultyMember();
        try {
            controller.pollFacultyMember(fm);
            fm = dao.getFacultyMember(fm.getOrcidRecordId());
            assertNotNull(fm.getOrcidDeactivationDate());
            assertEquals(fm.getOrcidDeactivationDate().getClass(), java.sql.Timestamp.class);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testRegisterFacultyMember() {
        FacultyMember fm = this.makeUnregisteredFacultyMember();
        try {
            controller.registerFacultyMember(fm);
        } catch (OrcidException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testRegisterFacultyMemberEmailAlreadyExists() {
        FacultyMember fm = this.makeFacultyMemberAlreadyExists();
        try {
            controller.registerFacultyMember(fm);
        } catch (OrcidException e) {
            String userMsg = "Unable to create new ORCID for " +
                    fm.getSubmissionFirstName() + " " +
                    fm.getSubmissionLastName() +
                    " email: " + fm.getSubmissionEmail() +
                    " FIS Id: " + fm.getFisId();
            assertEquals(userMsg, e.getMessage());
        }
    }

    @Test
    public void testRegisterFacultyMemberOrcidOKUnableToStore() {
        FacultyMember fm = this.makeFacultyMemberOrcidOKUnableToStore();
        try {
            controller.registerFacultyMember(fm);
        } catch (OrcidException e) {
            String userMsg = "Unable to store new ORCID " +
                    fm.getOrcidId() +
                    " for " +
                    fm.getSubmissionFirstName() + " " +
                    fm.getSubmissionLastName() +
                    " email: " + fm.getSubmissionEmail() +
                    " FIS Id: " + fm.getFisId();
            assertEquals(userMsg, e.getMessage());
        }
    }
}
