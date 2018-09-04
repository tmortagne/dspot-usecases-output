package org.xwiki.component.guice;


import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.annotation.Role;


public class AmplGuiceCompatibilityTest {
    @Role
    public interface FieldRole {}

    @Component(staticRegistration = false)
    @Named("name")
    @Singleton
    public static class FieldRoleImpl1 implements AmplGuiceCompatibilityTest.FieldRole {}

    @Component(staticRegistration = false)
    @Singleton
    public static class FieldRoleImpl2 implements AmplGuiceCompatibilityTest.FieldRole {}

    @Role
    public interface RoleClass {}

    @Component(staticRegistration = false)
    @Singleton
    public static class ProviderImpl implements Provider<AmplGuiceCompatibilityTest.FieldRole> {
        @Override
        public AmplGuiceCompatibilityTest.FieldRole get() {
            return new AmplGuiceCompatibilityTest.FieldRoleImpl2();
        }
    }

    @Role
    public interface GenericFieldRole<T> {}

    @Component(staticRegistration = false)
    @Singleton
    public static class GenericFieldRoleImpl<String> implements AmplGuiceCompatibilityTest.GenericFieldRole<String> {}

    @Component(staticRegistration = false)
    @Named("whatever")
    @Singleton
    public static class RoleImpl implements AmplGuiceCompatibilityTest.RoleClass {
        @Inject
        @Named("name")
        private AmplGuiceCompatibilityTest.FieldRole fieldRole1;

        @Inject
        private Provider<AmplGuiceCompatibilityTest.FieldRole> fieldRoleProvider;

        @Inject
        private AmplGuiceCompatibilityTest.GenericFieldRole<String> genericFieldRole;

        @Inject
        private Logger logger;

        public AmplGuiceCompatibilityTest.FieldRole getFieldRole1() {
            return this.fieldRole1;
        }

        public AmplGuiceCompatibilityTest.FieldRole getFieldRole2() {
            return this.fieldRoleProvider.get();
        }

        public AmplGuiceCompatibilityTest.GenericFieldRole<String> getGenericFieldRole() {
            return this.genericFieldRole;
        }

        public Logger getLogger() {
            return this.logger;
        }
    }

    public class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            bindListener(Matchers.any(), new Slf4jInjectionTypeListener());
            bind(Logger.class).toInstance(LoggerFactory.getLogger("Not used!"));
            bind(AmplGuiceCompatibilityTest.FieldRole.class).annotatedWith(Names.named("name")).to(AmplGuiceCompatibilityTest.FieldRoleImpl1.class);
            bind(AmplGuiceCompatibilityTest.FieldRole.class).toProvider(AmplGuiceCompatibilityTest.ProviderImpl.class);
            bind(new TypeLiteral<AmplGuiceCompatibilityTest.GenericFieldRole<String>>() {}).to(new TypeLiteral<AmplGuiceCompatibilityTest.GenericFieldRoleImpl<String>>() {});
        }
    }
}

