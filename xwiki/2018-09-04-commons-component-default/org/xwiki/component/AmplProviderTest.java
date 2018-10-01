package org.xwiki.component;


import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import org.junit.Assert;
import org.junit.Test;
import org.xwiki.component.annotation.Component;
import org.xwiki.component.annotation.ComponentRole;
import org.xwiki.component.embed.EmbeddableComponentManager;
import org.xwiki.component.embed.EmbeddableComponentManagerTest;
import org.xwiki.component.manager.ComponentLookupException;
import org.xwiki.component.phase.Initializable;
import org.xwiki.component.phase.InitializationException;


public class AmplProviderTest {
    @ComponentRole
    public static interface TestComponentRole {}

    @Component
    @Singleton
    public static class TestComponentWithProviders implements AmplProviderTest.TestComponentRole {
        @Inject
        public Provider<String> provider1;

        @Inject
        @Named("another")
        public Provider<String> provider12;

        @Inject
        public Provider<Integer> provider2;

        @Inject
        public Provider<List<EmbeddableComponentManagerTest.Role>> providerList;

        @Inject
        public Provider<Map<String, EmbeddableComponentManagerTest.Role>> providerMap;
    }

    public static class TestProvider1 implements Provider<String> {
        @Override
        public String get() {
            return "value";
        }
    }

    @Named("another")
    public static class TestProvider12 implements Provider<String> {
        @Override
        public String get() {
            return "another value";
        }
    }

    public static class TestProvider2 implements Provider<Integer> {
        @Override
        public Integer get() {
            return 1;
        }
    }

    @Component
    @Named("exception")
    @Singleton
    public static class TestComponentWithProviderInException implements AmplProviderTest.TestComponentRole {
        @Inject
        @Named("exception")
        public Provider<String> providerWithExceptionInInitialize;
    }

    @Named("exception")
    public static class TestProviderWithExceptionInInitialize implements Provider<String> , Initializable {
        @Override
        public void initialize() throws InitializationException {
            throw new InitializationException("Some error in init");
        }

        @Override
        public String get() {
            throw new RuntimeException("should not be called!");
        }
    }

    @Test(timeout = 10000)
    public void loadAndInjectProviderWhenExceptionInInitialize() throws Exception {
        EmbeddableComponentManager cm = new EmbeddableComponentManager();
        Assert.assertNull(((EmbeddableComponentManager) (cm)).getParent());
        Assert.assertNull(((EmbeddableComponentManager) (cm)).getNamespace());
        Assert.assertNull(((EmbeddableComponentManager) (cm)).getComponentEventManager());
        cm.initialize(getClass().getClassLoader());
        try {
            cm.getInstance(AmplProviderTest.TestComponentRole.class, "exception");
        } catch (ComponentLookupException expected) {
            String String_0 = "Failed to lookup component " + ("[org.xwiki.component.ProviderTest$TestComponentWithProviderInException] identified by " + "type [interface org.xwiki.component.ProviderTest$TestComponentRole] and hint [exception]");
            Assert.assertEquals("Failed to lookup component [org.xwiki.component.ProviderTest$TestComponentWithProviderInException] identified by type [interface org.xwiki.component.ProviderTest$TestComponentRole] and hint [exception]", String_0);
            expected.getMessage();
            String String_1 = "Failed to lookup component " + ("[org.xwiki.component.ProviderTest$TestProviderWithExceptionInInitialize] identified by " + "type [javax.inject.Provider<java.lang.String>] and hint [exception]");
            Assert.assertEquals("Failed to lookup component [org.xwiki.component.ProviderTest$TestProviderWithExceptionInInitialize] identified by type [javax.inject.Provider<java.lang.String>] and hint [exception]", String_1);
            expected.getCause().getMessage();
            expected.getCause().getCause().getMessage();
        }
    }
}

