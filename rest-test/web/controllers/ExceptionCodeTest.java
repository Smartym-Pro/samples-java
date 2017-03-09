package pro.smartum.reptracker.gateway.web.controllers;

import org.junit.Assert;
import org.junit.Test;

import pro.smartum.reptracker.gateway.web.controllers.ExceptionCode;

/**
 * User: Sergey Valuy
 
 */
public class ExceptionCodeTest {

    @Test
    public void testMatches() throws Exception {
        IllegalArgumentException e = new IllegalArgumentException();

        Assert.assertTrue(ExceptionCode.INVALID_ARGUMENTS_ERROR.matches(e));
        Assert.assertFalse(ExceptionCode.LIMIT_EXCEEDED_ERROR.matches(e));
        Assert.assertTrue(ExceptionCode.UNEXPECTED_ERROR.matches(e));
    }
}
