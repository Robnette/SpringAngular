package me.robnette.spring_angular.Util;

import me.robnette.spring_angular.exception.ForbiddenException;
import me.robnette.spring_angular.util.Util;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.fail;

@RunWith(Parameterized.class)
public class Util_PasswordTestUsingConstructor {
    private String password;

    public Util_PasswordTestUsingConstructor(String password){
        this.password = password;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] { { "Bonjour01;" }, { "Bonsoir02;" }, { "Tr@l@l@99" }, { "FinalTest0X;" } };
        return Arrays.asList(data);
    }

    @Test
    public void testMultiplyException() {
        try {
            Util.passwordCheck(password);
        }catch(ForbiddenException e){
            fail("Password not good");
        }
    }
}
