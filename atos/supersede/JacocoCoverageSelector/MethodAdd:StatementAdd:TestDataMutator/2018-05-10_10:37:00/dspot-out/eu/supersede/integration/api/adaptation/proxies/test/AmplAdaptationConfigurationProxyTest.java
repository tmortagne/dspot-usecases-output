package eu.supersede.integration.api.adaptation.proxies.test;


import eu.supersede.integration.api.adaptation.proxies.AdaptationConfigurationProxy;
import eu.supersede.integration.api.adaptation.types.AdaptationMode;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.springframework.util.Assert.notNull;


public class AmplAdaptationConfigurationProxyTest {
    private static AdaptationConfigurationProxy<?, ?> proxy;

    @BeforeClass
    public static void setup() throws Exception {
        AmplAdaptationConfigurationProxyTest.proxy = new AdaptationConfigurationProxy<Object, Object>();
    }

    @Test(timeout = 10000)
    public void testAdaptationMode_add1() throws Exception {
        AdaptationMode o_testAdaptationMode_add1__1 = AdaptationMode.valueOf("SUPERVISED");
        Assert.assertEquals("SUPERVISED", ((eu.supersede.integration.api.adaptation.types.AdaptationMode)o_testAdaptationMode_add1__1).name());
        Assert.assertEquals("supervised", ((eu.supersede.integration.api.adaptation.types.AdaptationMode)o_testAdaptationMode_add1__1).getMode());
        Assert.assertEquals(0, ((int) (((eu.supersede.integration.api.adaptation.types.AdaptationMode)o_testAdaptationMode_add1__1).ordinal())));
        AdaptationMode mode = AdaptationMode.valueOf("SUPERVISED");
    }

    @Test(timeout = 10000)
    public void testAdaptationMode_literalMutationString3_failAssert0() throws Exception {
        try {
            AdaptationMode mode = AdaptationMode.valueOf("");
            org.junit.Assert.fail("testAdaptationMode_literalMutationString3 should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException eee) {
        }
    }

    @Test(timeout = 10000)
    public void testAdaptationMode_literalMutationString4_failAssert1() throws Exception {
        try {
            AdaptationMode mode = AdaptationMode.valueOf("googleplay_api_googleplay_tool");
            org.junit.Assert.fail("testAdaptationMode_literalMutationString4 should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException eee) {
        }
    }

    @Test(timeout = 10000)
    public void testAdaptationMode_add1_add30() throws Exception {
        AdaptationMode o_testAdaptationMode_add1_add30__1 = AdaptationMode.valueOf("SUPERVISED");
        Assert.assertEquals("supervised", ((eu.supersede.integration.api.adaptation.types.AdaptationMode)o_testAdaptationMode_add1_add30__1).getMode());
        Assert.assertEquals(0, ((int) (((eu.supersede.integration.api.adaptation.types.AdaptationMode)o_testAdaptationMode_add1_add30__1).ordinal())));
        Assert.assertEquals("SUPERVISED", ((eu.supersede.integration.api.adaptation.types.AdaptationMode)o_testAdaptationMode_add1_add30__1).name());
        AdaptationMode o_testAdaptationMode_add1__1 = AdaptationMode.valueOf("SUPERVISED");
        AdaptationMode mode = AdaptationMode.valueOf("SUPERVISED");
    }

    @Test(timeout = 10000)
    public void testAdaptationMode_add1_literalMutationString44_failAssert0() throws Exception {
        try {
            AdaptationMode o_testAdaptationMode_add1__1 = AdaptationMode.valueOf("");
            AdaptationMode mode = AdaptationMode.valueOf("SUPERVISED");
            org.junit.Assert.fail("testAdaptationMode_add1_literalMutationString44 should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException eee) {
        }
    }

    @Test(timeout = 10000)
    public void testAdaptationMode_add1_literalMutationString54_failAssert10() throws Exception {
        try {
            AdaptationMode o_testAdaptationMode_add1__1 = AdaptationMode.valueOf("SUPERVISED");
            AdaptationMode mode = AdaptationMode.valueOf("SUPRVISED");
            org.junit.Assert.fail("testAdaptationMode_add1_literalMutationString54 should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException eee) {
        }
    }

    @Test(timeout = 10000)
    public void testAdaptationMode_add1_add30_add133() throws Exception {
        AdaptationMode o_testAdaptationMode_add1_add30_add133__1 = AdaptationMode.valueOf("SUPERVISED");
        Assert.assertEquals(0, ((int) (((eu.supersede.integration.api.adaptation.types.AdaptationMode)o_testAdaptationMode_add1_add30_add133__1).ordinal())));
        Assert.assertEquals("supervised", ((eu.supersede.integration.api.adaptation.types.AdaptationMode)o_testAdaptationMode_add1_add30_add133__1).getMode());
        Assert.assertEquals("SUPERVISED", ((eu.supersede.integration.api.adaptation.types.AdaptationMode)o_testAdaptationMode_add1_add30_add133__1).name());
        AdaptationMode o_testAdaptationMode_add1_add30__1 = AdaptationMode.valueOf("SUPERVISED");
        AdaptationMode o_testAdaptationMode_add1__1 = AdaptationMode.valueOf("SUPERVISED");
        AdaptationMode mode = AdaptationMode.valueOf("SUPERVISED");
    }

    @Test(timeout = 10000)
    public void testAdaptationMode_add1_add30_literalMutationString157_failAssert0() throws Exception {
        try {
            AdaptationMode o_testAdaptationMode_add1_add30__1 = AdaptationMode.valueOf("");
            AdaptationMode o_testAdaptationMode_add1__1 = AdaptationMode.valueOf("SUPERVISED");
            AdaptationMode mode = AdaptationMode.valueOf("SUPERVISED");
            org.junit.Assert.fail("testAdaptationMode_add1_add30_literalMutationString157 should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException eee) {
        }
    }

    @Test(timeout = 10000)
    public void testAdaptationMode_add1_add30_literalMutationString168_failAssert3() throws Exception {
        try {
            AdaptationMode o_testAdaptationMode_add1_add30__1 = AdaptationMode.valueOf("0/|]6^FT)-");
            AdaptationMode o_testAdaptationMode_add1__1 = AdaptationMode.valueOf("SUPERVISED");
            AdaptationMode mode = AdaptationMode.valueOf("SUPERVISED");
            org.junit.Assert.fail("testAdaptationMode_add1_add30_literalMutationString168 should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException eee) {
        }
    }

    @Test(timeout = 10000)
    public void testGetAdaptationConfigurationMode_add325() throws Exception {
        AdaptationMode o_testGetAdaptationConfigurationMode_add325__1 = AmplAdaptationConfigurationProxyTest.proxy.getAdaptationConfigurationMode();
        Assert.assertEquals("SUPERVISED", ((eu.supersede.integration.api.adaptation.types.AdaptationMode)o_testGetAdaptationConfigurationMode_add325__1).name());
        Assert.assertEquals("supervised", ((eu.supersede.integration.api.adaptation.types.AdaptationMode)o_testGetAdaptationConfigurationMode_add325__1).getMode());
        Assert.assertEquals(0, ((int) (((eu.supersede.integration.api.adaptation.types.AdaptationMode)o_testGetAdaptationConfigurationMode_add325__1).ordinal())));
        notNull(AmplAdaptationConfigurationProxyTest.proxy.getAdaptationConfigurationMode());
    }

    @Test(timeout = 10000)
    public void testGetAdaptationConfigurationMode_sd327() throws Exception {
        AdaptationMode mode = AmplAdaptationConfigurationProxyTest.proxy.getAdaptationConfigurationMode();
        notNull(mode);
        Assert.assertEquals("supervised", mode.getMode());
    }

    @Test(timeout = 10000)
    public void testGetAdaptationConfigurationMode_add325_add338() throws Exception {
        AdaptationMode o_testGetAdaptationConfigurationMode_add325_add338__1 = AmplAdaptationConfigurationProxyTest.proxy.getAdaptationConfigurationMode();
        Assert.assertEquals("supervised", ((eu.supersede.integration.api.adaptation.types.AdaptationMode)o_testGetAdaptationConfigurationMode_add325_add338__1).getMode());
        Assert.assertEquals(0, ((int) (((eu.supersede.integration.api.adaptation.types.AdaptationMode)o_testGetAdaptationConfigurationMode_add325_add338__1).ordinal())));
        Assert.assertEquals("SUPERVISED", ((eu.supersede.integration.api.adaptation.types.AdaptationMode)o_testGetAdaptationConfigurationMode_add325_add338__1).name());
        AdaptationMode o_testGetAdaptationConfigurationMode_add325__1 = AmplAdaptationConfigurationProxyTest.proxy.getAdaptationConfigurationMode();
        notNull(AmplAdaptationConfigurationProxyTest.proxy.getAdaptationConfigurationMode());
    }

    @Test(timeout = 10000)
    public void testGetAdaptationConfigurationMode_add325_add341() throws Exception {
        AdaptationMode o_testGetAdaptationConfigurationMode_add325__1 = AmplAdaptationConfigurationProxyTest.proxy.getAdaptationConfigurationMode();
        AdaptationMode o_testGetAdaptationConfigurationMode_add325_add341__4 = AmplAdaptationConfigurationProxyTest.proxy.getAdaptationConfigurationMode();
        Assert.assertEquals("supervised", ((eu.supersede.integration.api.adaptation.types.AdaptationMode)o_testGetAdaptationConfigurationMode_add325_add341__4).getMode());
        Assert.assertEquals(0, ((int) (((eu.supersede.integration.api.adaptation.types.AdaptationMode)o_testGetAdaptationConfigurationMode_add325_add341__4).ordinal())));
        Assert.assertEquals("SUPERVISED", ((eu.supersede.integration.api.adaptation.types.AdaptationMode)o_testGetAdaptationConfigurationMode_add325_add341__4).name());
        notNull(AmplAdaptationConfigurationProxyTest.proxy.getAdaptationConfigurationMode());
    }
}

