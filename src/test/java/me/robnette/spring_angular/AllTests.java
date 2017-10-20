package me.robnette.spring_angular;


import me.robnette.spring_angular.Util.Util_PasswordTest;
import me.robnette.spring_angular.Util.Util_PasswordTestUsingConstructor;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({Util_PasswordTest.class, Util_PasswordTestUsingConstructor.class})
public class AllTests {
}
