package org.cmpsWeb;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.southern.cmps.service.exception.CmpsException;
import com.southern.cmps.service.impl.CmpsServiceImpl;

@Ignore
public class CmpsServiceTest {
	
	ApplicationContext context = new ClassPathXmlApplicationContext("cmps-service.xml");
	CmpsServiceImpl cmpsServiceImpl = context.getBean(CmpsServiceImpl.class);

	@Test
	public void getStudentDetail() throws CmpsException {
		System.out.println(cmpsServiceImpl.getStudentDetail("U01996353"));
	}

}
