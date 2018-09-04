package org.xwiki.component.internal;


import java.util.ArrayList;
import java.util.Collection;
import org.jmock.Mockery;
import org.junit.After;
import org.junit.Before;
import org.xwiki.component.descriptor.DefaultComponentDescriptor;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.observation.ObservationManager;


public class AmplStackingComponentEventManagerTest {
    private StackingComponentEventManager eventManager;

    private DefaultComponentDescriptor<CharSequence> descriptor1;

    private DefaultComponentDescriptor<Collection> descriptor2;

    private ObservationManager mockObservationManager;

    private ComponentManager mockComponentManager;

    private Mockery mockery;

    @Before
    public void setUp() {
        this.eventManager = new StackingComponentEventManager();
        this.descriptor1 = new DefaultComponentDescriptor<CharSequence>();
        this.descriptor1.setImplementation(String.class);
        this.descriptor1.setRoleType(CharSequence.class);
        this.descriptor1.setRoleHint("hint1");
        this.descriptor2 = new DefaultComponentDescriptor<Collection>();
        this.descriptor2.setImplementation(ArrayList.class);
        this.descriptor2.setRoleType(Collection.class);
        this.descriptor2.setRoleHint("hint2");
        this.mockery = new Mockery();
        this.mockObservationManager = this.mockery.mock(ObservationManager.class);
        this.mockComponentManager = this.mockery.mock(ComponentManager.class);
        this.eventManager.setObservationManager(this.mockObservationManager);
    }

    @After
    public void tearDown() {
        this.mockery.assertIsSatisfied();
    }
}

