package org.cmpsWeb;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.southern.cmps.service.exception.CmpsException;
import com.southern.cmps.service.impl.CmpsServiceImpl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
	
	
	ApplicationContext context = new ClassPathXmlApplicationContext("cmps-service.xml");
	CmpsServiceImpl cmpsServiceImpl = context.getBean(CmpsServiceImpl.class);

	public void getStudentDetail() throws CmpsException {
		System.out.println(cmpsServiceImpl.getStudentDetail("U01996353"));
	}
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }
}
