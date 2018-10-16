package org.xwiki.crypto.internal.symmetric.generator;


import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.xwiki.crypto.KeyGenerator;
import org.xwiki.crypto.internal.DefaultSecureRandomProvider;
import org.xwiki.crypto.params.generator.KeyGenerationParameters;
import org.xwiki.crypto.params.generator.symmetric.GenericKeyGenerationParameters;
import org.xwiki.test.annotation.ComponentList;
import org.xwiki.test.mockito.MockitoComponentMockingRule;


@ComponentList({ DefaultSecureRandomProvider.class })
public class DefaultKeyGeneratorTest {
    @Rule
    public final MockitoComponentMockingRule<KeyGenerator> mocker = new MockitoComponentMockingRule<KeyGenerator>(DefaultKeyGenerator.class);

    private KeyGenerator generator;

    @Before
    public void configure() throws Exception {
        generator = mocker.getComponentUnderTest();
    }

    @Test(expected = RuntimeException.class)
    public void testGenerateWithoutArgument() throws Exception {
        generator.generate();
    }

    @Test(timeout = 10000)
    public void testGenerateWithoutArgument_failAssert0() throws Exception {
        try {
            generator.generate();
            org.junit.Assert.fail("testGenerateWithoutArgument should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException expected) {
            Assert.assertEquals("Knowing the key strength is required to generate a key.", expected.getMessage());
        }
    }

    @Test
    public void testGenerateWithStrengthParameter() throws Exception {
        KeyGenerationParameters params = new GenericKeyGenerationParameters(128);
        byte[] key = generator.generate(params);
        Assert.assertThat(key.length, CoreMatchers.equalTo(128));
        Assert.assertThat(key, CoreMatchers.not(CoreMatchers.equalTo(generator.generate(params))));
        Assert.assertThat(generator.generate(new GenericKeyGenerationParameters(16)).length, CoreMatchers.equalTo(16));
    }
}

