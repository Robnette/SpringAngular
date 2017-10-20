package me.robnette.spring_angular.Util;

import me.robnette.spring_angular.exception.ForbiddenException;
import me.robnette.spring_angular.util.Util;
import org.junit.Test;

import static org.junit.Assert.fail;

public class Util_PasswordTest {
    @Test (expected = ForbiddenException.class)
    public void passwordShort(){
        Util.passwordCheck("dd");
//        assertFalse("Password to short", Util.passwordCheck("dd"));
    }

    @Test (expected = ForbiddenException.class)
    public void passwordUpperCase(){
        Util.passwordCheck("dddddddd");
//        assertFalse("An upper case letter must occur at least once", userService.passwordCheck("dddddddd"));
    }

    @Test (expected = ForbiddenException.class)
    public void passwordLowerCase(){
        Util.passwordCheck("DDDDDDDD");
//        assertFalse("A lower case letter must occur at least once", userService.passwordCheck("DDDDDDDD"));
    }

    @Test (expected = ForbiddenException.class)
    public void passwordDigit(){
        Util.passwordCheck("Dddddddd");
//        assertFalse("A digit must occur at least once", userService.passwordCheck("Dddddddd"));
    }

    @Test (expected = ForbiddenException.class)
    public void passwordSpecialCharacter(){
        Util.passwordCheck("Ddddddd0");
//        assertFalse("A special character must occur at least once", userService.passwordCheck("Ddddddd0"));
    }

    @Test (expected = ForbiddenException.class)
    public void passwordWhitespace(){
        Util.passwordCheck("Ddddd 0;");
//        assertFalse("No whitespace allowed in the entire string", userService.passwordCheck("Ddddd 0;"));
    }

    @Test (expected = ForbiddenException.class)
    public void passwordLong(){
        Util.passwordCheck("BonjourL@Terre01234;a");
//        assertFalse("Password to long", userService.passwordCheck("BonjourL@Terre01234;a"));
    }

    @Test
    public void passwordGood(){
        try {
            Util.passwordCheck("BonjourL@Terre01234;");
        }catch(ForbiddenException e){
            fail("Password not good");
        }
//        assertTrue("Good password", userService.passwordCheck("BonjourL@Terre01234;"));
    }
}
