package org.xwiki.component.annotation;


import java.lang.reflect.Type;
import java.util.Set;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.slf4j.Logger;
import org.xwiki.test.jmock.JMockRule;


public class AmplComponentAnnotationLoaderTest {
    @Rule
    public final JMockRule mockery = new JMockRule();

    @SuppressWarnings("deprecation")
    @ComponentRole
    public interface NotGenericRole<T> {}

    @SuppressWarnings("deprecation")
    @ComponentRole
    public interface ExtendedRole extends AmplComponentAnnotationLoaderTest.NotGenericRole<String> {}

    @Component(staticRegistration = false)
    @Singleton
    public class RoleImpl implements AmplComponentAnnotationLoaderTest.ExtendedRole {}

    @Component(staticRegistration = false)
    @Singleton
    public class SuperRoleImpl extends AmplComponentAnnotationLoaderTest.RoleImpl implements AmplComponentAnnotationLoaderTest.NotGenericRole<String> {}

    @Component("test")
    @Singleton
    public class SimpleRole implements AmplComponentAnnotationLoaderTest.NotGenericRole<String> {}

    @Component("test")
    @Singleton
    public class OverrideRole implements AmplComponentAnnotationLoaderTest.NotGenericRole<String> {}

    @Component("deprecated")
    @Singleton
    public class DeprecatedSimpleRole implements AmplComponentAnnotationLoaderTest.NotGenericRole<String> {}

    @Component("deprecated")
    @Singleton
    public class DeprecatedOverrideRole implements AmplComponentAnnotationLoaderTest.NotGenericRole<String> {}

    @Component(staticRegistration = false)
    @Named("customprovider")
    @Singleton
    public class ProviderImpl implements Provider<AmplComponentAnnotationLoaderTest.NotGenericRole<String>> {
        @Override
        public AmplComponentAnnotationLoaderTest.NotGenericRole<String> get() {
            return new AmplComponentAnnotationLoaderTest.RoleImpl();
        }
    }

    @Role
    public interface GenericRole<T> {}

    @Component(staticRegistration = false)
    @Singleton
    public class GenericComponent implements AmplComponentAnnotationLoaderTest.GenericRole<String> {}

    @Component(staticRegistration = false)
    @Singleton
    @SuppressWarnings("rawtypes")
    public class NonGenericComponent implements AmplComponentAnnotationLoaderTest.GenericRole {}

    public abstract class AbstractGenericComponent<V> implements AmplComponentAnnotationLoaderTest.GenericRole<V> {}

    @Component(staticRegistration = false)
    @Singleton
    public class ExtendingGenericComponent extends AmplComponentAnnotationLoaderTest.AbstractGenericComponent<String> {}

    @Component(staticRegistration = false)
    @Singleton
    @SuppressWarnings("rawtypes")
    public class ExtendingNonGenericComponent extends AmplComponentAnnotationLoaderTest.AbstractGenericComponent {}

    private ComponentAnnotationLoader loader;

    private class TestableComponentAnnotationLoader extends ComponentAnnotationLoader {
        private Logger logger;

        TestableComponentAnnotationLoader(Logger logger) {
            this.logger = logger;
        }

        @Override
        protected Logger getLogger() {
            return this.logger;
        }
    }

    @Before
    public void setupLogger() throws Exception {
        this.loader = new AmplComponentAnnotationLoaderTest.TestableComponentAnnotationLoader(this.mockery.mock(Logger.class));
    }

    @After
    public void tearDown() throws Exception {
        this.mockery.assertIsSatisfied();
    }

    private void assertComponentRoleTypes(Class<?> componentClass) {
        Set<Type> type = this.loader.findComponentRoleTypes(componentClass);
        Assert.assertEquals(2, type.size());
        Assert.assertTrue(type.contains(AmplComponentAnnotationLoaderTest.NotGenericRole.class));
        Assert.assertTrue(type.contains(AmplComponentAnnotationLoaderTest.ExtendedRole.class));
    }
}

