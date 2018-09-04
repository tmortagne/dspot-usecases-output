package org.xwiki.context;


import java.lang.reflect.Field;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import org.xwiki.context.internal.ExecutionContextProperty;


public class AmplExecutionContextPropertyTest {
    @SuppressWarnings("unchecked")
    private ExecutionContextProperty fetch(ExecutionContext context, String key) throws Exception {
        Field propertiesField = ExecutionContext.class.getDeclaredField("properties");
        propertiesField.setAccessible(true);
        Map<String, ExecutionContextProperty> properties = ((Map<String, ExecutionContextProperty>) (propertiesField.get(context)));
        return properties.get(key);
    }

    @Test(timeout = 10000)
    public void cloningNonPublicCloneMethod_failAssert0() throws Exception {
        try {
            ExecutionContext context = new ExecutionContext();
            final String key = "test";
            AmplExecutionContextPropertyTest.TestNonpublicClone value = new AmplExecutionContextPropertyTest.TestNonpublicClone();
            context.newProperty(key).cloneValue().initial(new AmplExecutionContextPropertyTest.TestNonpublicClone()).declare();
            fetch(context, key).clone();
            org.junit.Assert.fail("cloningNonPublicCloneMethod should have thrown IllegalStateException");
        } catch (IllegalStateException expected) {
            Assert.assertEquals("cloneValue attribute was set on property [test], but the value had class [org.xwiki.context.ExecutionContextPropertyTest$TestNonpublicClone] which has no public clone method", expected.getMessage());
        }
    }

    @Test(timeout = 10000)
    public void nonNullCheck_failAssert1() throws Exception {
        try {
            ExecutionContext context = new ExecutionContext();
            final String key = "test";
            new ExecutionContext().newProperty("test").nonNull().initial(null).declare();
            org.junit.Assert.fail("nonNullCheck should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            Assert.assertEquals("The property [test] may not be null!", expected.getMessage());
        }
    }

    @Test(timeout = 10000)
    public void typeCheckingMismatch_failAssert2() throws Exception {
        try {
            ExecutionContext context = new ExecutionContext();
            final String key = "test";
            context.newProperty(key).type(AmplExecutionContextPropertyTest.SomeSubClass.class).declare();
            context.setProperty(key, new AmplExecutionContextPropertyTest.SomeClass());
            org.junit.Assert.fail("typeCheckingMismatch should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            Assert.assertEquals("The value of property [test] must be of type [class org.xwiki.context.ExecutionContextPropertyTest$SomeSubClass], but was [class org.xwiki.context.ExecutionContextPropertyTest$SomeClass]", expected.getMessage());
        }
    }

    public static class TestCloneable implements Cloneable {
        public String value = "original";

        @Override
        public Object clone() throws CloneNotSupportedException {
            AmplExecutionContextPropertyTest.TestCloneable clone = ((AmplExecutionContextPropertyTest.TestCloneable) (super.clone()));
            clone.value = "clone";
            return clone;
        }
    }

    public static class TestNonpublicClone implements Cloneable {
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    public static class SomeClass {}

    public static class SomeSubClass extends AmplExecutionContextPropertyTest.SomeClass {}
}

