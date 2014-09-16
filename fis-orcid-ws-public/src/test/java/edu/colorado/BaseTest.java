package edu.colorado;

import edu.colorado.fis.DataAccessFault;
import edu.colorado.fis.FISDao;
import edu.colorado.fis.FacultyMember;
import edu.colorado.orcid.mvc.OrcidController;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BaseTest extends TestCase {
    protected OrcidController controller;
    protected FISDao dao = new FISDao();
    protected DriverManagerDataSource ds;
    private Log log = LogFactory.getLog(getClass());

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        GenericXmlApplicationContext ctx =
                new GenericXmlApplicationContext(new FileSystemResource("src/main/webapp/WEB-INF/spring-servlet.xml"));
        controller = (OrcidController) ctx.getBean("orcidController");
        ds = (DriverManagerDataSource) ctx.getBean("dataSource");
        dao.setDataSource(ds);

        log.info("Connected as -> " + ds.getUsername());
    }

    @Test
    public void testIgnore() {
        assertTrue(true);  //used to get IntelliJ to ignore no tests in class warning
    }

    /**** UTILITY METHODS *******/

    protected FacultyMember makeBaselineFacultyMember(){
        SimpleDateFormat formatter = new SimpleDateFormat(FacultyMember.DATE_FORMAT);
        FacultyMember fm = new FacultyMember();
        try {
            fm.setOrcidRecordId("10001");
            fm.setOrcidId("0000-0002-0493-5108");
            fm.setFisId("112946"); //Liz Tomich
            fm.setFisOrcidSource(FacultyMember.FIS_ORCID_SOURCE_FACULTY_MEMBER);
            fm.setSubmissionEmail("LIZ.TOMICH@MAILINATOR.COM");
            fm.setSubmissionFirstName("LIZ");
            fm.setSubmissionLastName("TOMICH");
            fm.setSubmissionNameType(FacultyMember.SUBMISSION_NAME_TYPE_HR);
            fm.setOrcidGivenNames("TOMICH_GIVEN");
            fm.setOrcidFamilyName("TOMICH_FAMILY");
            fm.setOrcidEmail("TOMICH_EMAIL@MAILINATOR.COM");
            fm.setOrcidCreationMethod(FacultyMember.ORCID_CREATION_METHOD_API);
            fm.setOrcidSubmissionDate(formatter.parse("2014-03-20T15:48:42.998Z"));
            fm.setOrcidCompletionDate(formatter.parse("2014-03-20T15:50:17.876Z"));
            fm.setOrcidLastModifiedDate(formatter.parse("2014-03-20T16:06:57.850Z"));
            fm.setOrcidClaimed(null);
            fm.setOrcidDeprecated(null);
            fm.setOrcidDeactivationDate(null);
            fm.setOrcidPrimaryRecord(null);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
        return fm;
    }

    protected FacultyMember makeDeprecatedFacultyMember(){
        FacultyMember fm = new FacultyMember();
        fm.setFisId("105590"); //Craig Jones
        fm.setOrcidId("0000-0002-2157-3882"); //Test account deprecated - ORCID iD for Paula Jones originally
        fm.setOrcidRecordId("10005");
        fm.setOrcidPrimaryRecord("0000-0002-5157-4747");
        fm.setOrcidDeprecated(FacultyMember.ORCID_DEPRECATED_TRUE);
        return fm;
    }

    protected FacultyMember makeNotFoundFacultyMember(){
        FacultyMember fm = new FacultyMember();
        fm.setOrcidRecordId("10000"); //bogus
        fm.setOrcidId("9999-8888-7777-6666");//bogus ORCID not found
        return fm;
    }

    protected FacultyMember makeDeactivatedFacultyMember(){
        FacultyMember fm = new FacultyMember();
        fm.setFisId("122697"); //Ryan Gill
        fm.setOrcidRecordId("10004");
        fm.setOrcidId("0000-0002-1227-2324");//ORCID is closed
        return fm;
    }

    protected FacultyMember makeUnregisteredFacultyMember(){
        String emailSuffix = getUniquePrefix();
        FacultyMember fm = new FacultyMember();
        fm.setFisId("112101"); //Mary Nelson
        fm.setSubmissionEmail("Mary.Nelson" + emailSuffix + "@Mailinator.COM");
        fm.setSubmissionFirstName("MARY");
        fm.setSubmissionLastName("NELSON");
        fm.setSubmissionNameType("H");
        return fm;
    }

    protected FacultyMember makeFacultyMemberAlreadyExists(){
        FacultyMember fm = new FacultyMember();
        fm.setFisId("101193"); //Tom Robbins
        fm.setSubmissionEmail("TOM.ROBBINS@MAILINATOR.COM");
        fm.setSubmissionFirstName("TOM");
        fm.setSubmissionLastName("ROBBINS");
        fm.setSubmissionNameType("H");
        return fm;
    }

    protected FacultyMember makeFacultyMemberOrcidOKUnableToStore(){
        String emailSuffix = getUniquePrefix();
        FacultyMember fm = new FacultyMember();
        fm.setFisId(""); //Missing required FIS Id
        fm.setSubmissionEmail("TOM.HOBBINS" + emailSuffix + "@MAILINATOR.COM");
        fm.setSubmissionFirstName("TOM");
        fm.setSubmissionLastName("HOBBINS");
        fm.setSubmissionNameType("H");
        return fm;
    }

    protected void resetBaselineFacultyMember() throws DataAccessFault {
        FacultyMember fm = makeBaselineFacultyMember();
        dao.saveFacultyMember(fm);
    }

    private String getUniquePrefix(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HHmmssSSS");
        return formatter.format(Calendar.getInstance().getTime());
    }

}
