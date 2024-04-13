package com.southern.cmps.dao.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.southern.cmps.dao.CmpsDao;

public class CmpsDaoTest {
	ApplicationContext context;
	CmpsDao cmpsDao = context.getBean(CmpsDao.class);
	
//	@Ignore
//	@Test
	public void getconcentrations() {
		System.out.println(cmpsDao.getConcentrations());
	}

}
