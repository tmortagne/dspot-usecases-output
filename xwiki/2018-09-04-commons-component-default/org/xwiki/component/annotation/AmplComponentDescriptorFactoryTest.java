package org.xwiki.component.annotation;


import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import org.junit.Assert;
import org.xwiki.component.descriptor.ComponentDependency;
import org.xwiki.component.descriptor.ComponentDescriptor;
import org.xwiki.component.descriptor.ComponentInstantiationStrategy;
import org.xwiki.component.util.DefaultParameterizedType;


public class AmplComponentDescriptorFactoryTest {
    @ComponentRole
    public interface NonGenericFieldRole<T> {}

    @Role
    public interface GenericFieldRole<T> {}

    @Component(staticRegistration = false)
    @Singleton
    public class FieldroleImpl implements AmplComponentDescriptorFactoryTest.NonGenericFieldRole<String> {}

    @Component(value = "special", staticRegistration = false)
    @Singleton
    public class SpecialFieldRoleImpl implements AmplComponentDescriptorFactoryTest.NonGenericFieldRole<String> {}

    @ComponentRole
    public interface NonGenericRole {}

    @ComponentRole
    public interface ExtendedRole extends AmplComponentDescriptorFactoryTest.NonGenericRole {}

    @Component(staticRegistration = false)
    @Singleton
    public class RoleImpl implements AmplComponentDescriptorFactoryTest.ExtendedRole {
        @Inject
        private AmplComponentDescriptorFactoryTest.NonGenericFieldRole<String> fieldRole;

        @Inject
        @Named("special")
        private AmplComponentDescriptorFactoryTest.NonGenericFieldRole<String> specialFieldRole;

        @Inject
        private AmplComponentDescriptorFactoryTest.GenericFieldRole<String> genericFieldRole;

        @Inject
        private AmplComponentDescriptorFactoryTest.GenericFieldRole nonGenericFieldRole;

        @Inject
        private List<AmplComponentDescriptorFactoryTest.NonGenericFieldRole<String>> roles;

        @Inject
        private Map<String, AmplComponentDescriptorFactoryTest.NonGenericFieldRole<String>> mapRoles;
    }

    @Component(staticRegistration = false)
    @Singleton
    public class SuperRoleImpl extends AmplComponentDescriptorFactoryTest.RoleImpl {
        @Inject
        @Named("other")
        private AmplComponentDescriptorFactoryTest.NonGenericFieldRole<String> fieldRole;
    }

    @Component(hints = { "hint1", "hint2" }, staticRegistration = false)
    @Singleton
    public class MultipleRolesImpl implements AmplComponentDescriptorFactoryTest.NonGenericRole {}

    @Component(staticRegistration = false)
    @Singleton
    public class SingletonImpl implements AmplComponentDescriptorFactoryTest.NonGenericRole {}

    @Component(staticRegistration = false)
    @Named("special")
    @Singleton
    public class SpecialImpl implements AmplComponentDescriptorFactoryTest.NonGenericRole {}

    private void assertComponentDescriptor(Class<?> componentClass, String fieldRoleName) {
        ComponentDescriptorFactory factory = new ComponentDescriptorFactory();
        List<ComponentDescriptor> descriptors = factory.createComponentDescriptors(componentClass, AmplComponentDescriptorFactoryTest.ExtendedRole.class);
        Assert.assertEquals(1, descriptors.size());
        ComponentDescriptor descriptor = descriptors.get(0);
        Assert.assertSame(componentClass, descriptor.getImplementation());
        Assert.assertSame(AmplComponentDescriptorFactoryTest.ExtendedRole.class, descriptor.getRole());
        Assert.assertSame(AmplComponentDescriptorFactoryTest.ExtendedRole.class, descriptor.getRoleType());
        Assert.assertEquals("default", descriptor.getRoleHint());
        Assert.assertEquals(ComponentInstantiationStrategy.SINGLETON, descriptor.getInstantiationStrategy());
        Collection<ComponentDependency<?>> deps = descriptor.getComponentDependencies();
        Assert.assertEquals(6, deps.size());
        Iterator<ComponentDependency<?>> it = deps.iterator();
        ComponentDependency dep = it.next();
        Assert.assertSame(AmplComponentDescriptorFactoryTest.NonGenericFieldRole.class, dep.getRole());
        Assert.assertSame(AmplComponentDescriptorFactoryTest.NonGenericFieldRole.class, dep.getRoleType());
        Assert.assertEquals(fieldRoleName, dep.getRoleHint());
        Assert.assertSame(AmplComponentDescriptorFactoryTest.NonGenericFieldRole.class, dep.getMappingType());
        Assert.assertEquals("fieldRole", dep.getName());
        dep = it.next();
        Assert.assertSame(AmplComponentDescriptorFactoryTest.NonGenericFieldRole.class, dep.getRole());
        Assert.assertSame(AmplComponentDescriptorFactoryTest.NonGenericFieldRole.class, dep.getRoleType());
        Assert.assertEquals("special", dep.getRoleHint());
        Assert.assertSame(AmplComponentDescriptorFactoryTest.NonGenericFieldRole.class, dep.getMappingType());
        Assert.assertEquals("specialFieldRole", dep.getName());
        dep = it.next();
        Assert.assertSame(AmplComponentDescriptorFactoryTest.GenericFieldRole.class, dep.getRole());
        Assert.assertEquals(new DefaultParameterizedType(AmplComponentDescriptorFactoryTest.class, AmplComponentDescriptorFactoryTest.GenericFieldRole.class, String.class), dep.getRoleType());
        Assert.assertEquals("default", dep.getRoleHint());
        Assert.assertSame(AmplComponentDescriptorFactoryTest.GenericFieldRole.class, dep.getMappingType());
        Assert.assertEquals("genericFieldRole", dep.getName());
        dep = it.next();
        Assert.assertSame(AmplComponentDescriptorFactoryTest.GenericFieldRole.class, dep.getRole());
        Assert.assertEquals(AmplComponentDescriptorFactoryTest.GenericFieldRole.class, dep.getRoleType());
        Assert.assertEquals("default", dep.getRoleHint());
        Assert.assertSame(AmplComponentDescriptorFactoryTest.GenericFieldRole.class, dep.getMappingType());
        Assert.assertEquals("nonGenericFieldRole", dep.getName());
        dep = it.next();
        Assert.assertSame(AmplComponentDescriptorFactoryTest.NonGenericFieldRole.class, dep.getRole());
        Assert.assertEquals("default", dep.getRoleHint());
        Assert.assertSame(List.class, dep.getMappingType());
        Assert.assertEquals("roles", dep.getName());
        dep = it.next();
        Assert.assertSame(AmplComponentDescriptorFactoryTest.NonGenericFieldRole.class, dep.getRole());
        Assert.assertEquals("default", dep.getRoleHint());
        Assert.assertSame(Map.class, dep.getMappingType());
        Assert.assertEquals("mapRoles", dep.getName());
    }
}

