package org.hyperpath.persistence.tests;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		suite.addTestSuite(MailsTests.class);
		suite.addTestSuite(PhonesTests.class);
		return suite;
	}

}
