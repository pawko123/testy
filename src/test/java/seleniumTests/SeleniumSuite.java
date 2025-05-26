package seleniumTests;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages("seleniumTests")
@SuiteDisplayName("Testy Selenium")
public class SeleniumSuite {
}
