package org.xwiki.context;


import org.junit.Assert;
import org.junit.Test;


public class AmplExecutionContextTest {
    @Test(timeout = 10000)
    public void illegalInheritance_failAssert0() throws Exception {
        try {
            ExecutionContext context = new ExecutionContext();
            ExecutionContext parent = new ExecutionContext();
            parent.newProperty("inherited").inherited().initial("test").makeFinal().declare();
            context.newProperty("inherited").inherited().initial("test").makeFinal().declare();
            context.inheritFrom(parent);
            org.junit.Assert.fail("illegalInheritance should have thrown IllegalStateException");
        } catch (IllegalStateException expected) {
            Assert.assertEquals("Execution context cannot be inherited because it already contains property [inherited] which must be inherited because it is an inherited final property.", expected.getMessage());
        }
    }
}

