package varvelworld.base64;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Created by luzhonghao on 2015/12/28.
 */
@RunWith(JUnit4.class)
public class Base64Test {

    @Test
    public void testEncrypt() {
        Assert.assertEquals(new String(java.util.Base64.getEncoder().encode("hello world".getBytes()))
                , Base64.encrypt("hello world".getBytes()));

        Assert.assertEquals(new String(java.util.Base64.getEncoder()
                .encode("Enables scanning for Servlet components".getBytes()))
                , Base64.encrypt("Enables scanning for Servlet components".getBytes()));
    }

    @Test
    public void testDecrypt() {
        Assert.assertArrayEquals("hello world".getBytes()
                , Base64.decrypt(new String(java.util.Base64.getEncoder().encode("hello world".getBytes()))));

        Assert.assertArrayEquals("Enables scanning for Servlet components".getBytes()
                , Base64.decrypt(new String(java.util.Base64.getEncoder()
                        .encode("Enables scanning for Servlet components".getBytes()))));
    }
}
