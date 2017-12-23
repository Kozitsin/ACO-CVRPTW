package com.hse.aco.io;

import com.hse.aco.TestUtils;
import com.hse.aco.algo.Context;
import org.junit.Assert;
import org.junit.Test;

public class ReaderTest extends TestUtils {

    @Test
    public void testReader() {
        String fullName = getFile("instances/C104.txt");
        Context context = Reader.read(fullName);
        Assert.assertNotNull(context);
    }

}
