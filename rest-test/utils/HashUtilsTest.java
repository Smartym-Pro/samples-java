package pro.smartum.reptracker.gateway.utils;

import junit.framework.Assert;
import org.junit.Test;

import pro.smartum.reptracker.gateway.utils.HashUtils;

/**
 * User: Sergey Valuy
 
 */
public class HashUtilsTest {

    @Test
    public void testEncodeEmail() throws Exception {
        String testEmail1 = "test@email.com";
        String testEmail2 = "test@email.com";

        Assert.assertEquals(HashUtils.encodeEmail(testEmail1), HashUtils.encodeEmail(testEmail2));
    }
}
