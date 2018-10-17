package org.xwiki.crypto.signer.internal.cms;


import java.util.Arrays;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.xwiki.crypto.AbstractPKIXTest;
import org.xwiki.crypto.AsymmetricKeyFactory;
import org.xwiki.crypto.BinaryStringEncoder;
import org.xwiki.crypto.internal.asymmetric.keyfactory.BcDSAKeyFactory;
import org.xwiki.crypto.internal.asymmetric.keyfactory.BcRSAKeyFactory;
import org.xwiki.crypto.internal.digest.factory.BcSHA1DigestFactory;
import org.xwiki.crypto.internal.digest.factory.DefaultDigestFactory;
import org.xwiki.crypto.internal.encoder.Base64BinaryStringEncoder;
import org.xwiki.crypto.params.cipher.asymmetric.PrivateKeyParameters;
import org.xwiki.crypto.pkix.CertificateFactory;
import org.xwiki.crypto.pkix.CertifyingSigner;
import org.xwiki.crypto.pkix.internal.BcStoreX509CertificateProvider;
import org.xwiki.crypto.pkix.internal.BcX509CertificateChainBuilder;
import org.xwiki.crypto.pkix.internal.BcX509CertificateFactory;
import org.xwiki.crypto.pkix.params.CertifiedKeyPair;
import org.xwiki.crypto.pkix.params.CertifiedPublicKey;
import org.xwiki.crypto.signer.CMSSignedDataGenerator;
import org.xwiki.crypto.signer.CMSSignedDataVerifier;
import org.xwiki.crypto.signer.SignerFactory;
import org.xwiki.crypto.signer.internal.DefaultBcContentVerifierProviderBuilder;
import org.xwiki.crypto.signer.internal.factory.BcDSAwithSHA1SignerFactory;
import org.xwiki.crypto.signer.internal.factory.BcSHA1withRsaSignerFactory;
import org.xwiki.crypto.signer.internal.factory.DefaultSignerFactory;
import org.xwiki.crypto.signer.param.CMSSignedDataGeneratorParameters;
import org.xwiki.crypto.signer.param.CMSSignedDataVerified;
import org.xwiki.crypto.signer.param.CMSSignerVerifiedInformation;
import org.xwiki.test.annotation.ComponentList;
import org.xwiki.test.mockito.MockitoComponentMockingRule;


@ComponentList({ Base64BinaryStringEncoder.class, BcRSAKeyFactory.class, BcDSAKeyFactory.class, DefaultDigestFactory.class, BcSHA1DigestFactory.class, BcSHA1withRsaSignerFactory.class, BcDSAwithSHA1SignerFactory.class, DefaultSignerFactory.class, BcX509CertificateFactory.class, DefaultBcContentVerifierProviderBuilder.class, BcStoreX509CertificateProvider.class, BcX509CertificateChainBuilder.class })
public class DefaultCMSSignedDataTest extends AbstractPKIXTest {
    @Rule
    public final MockitoComponentMockingRule<CMSSignedDataGenerator> generatorMocker = new MockitoComponentMockingRule<CMSSignedDataGenerator>(DefaultCMSSignedDataGenerator.class);

    @Rule
    public final MockitoComponentMockingRule<CMSSignedDataVerifier> verifierMocker = new MockitoComponentMockingRule<CMSSignedDataVerifier>(DefaultCMSSignedDataVerifier.class);

    private CMSSignedDataGenerator generator;

    private CMSSignedDataVerifier verifier;

    @SuppressWarnings("unused")
    private static SignerFactory rsaSignerFactory;

    private static SignerFactory dsaSignerFactory;

    private static PrivateKeyParameters rsaPrivateKey;

    private static PrivateKeyParameters dsaPrivateKey;

    private static CertifiedPublicKey v3CaCert;

    private static CertifiedPublicKey v3InterCaCert;

    private static CertifiedPublicKey v3Cert;

    protected static byte[] text;

    public void setupTest(MockitoComponentMockingRule<CMSSignedDataGenerator> mocker) throws Exception {
        if ((DefaultCMSSignedDataTest.rsaPrivateKey) == null) {
            BinaryStringEncoder base64encoder = mocker.getInstance(BinaryStringEncoder.class, "Base64");
            AsymmetricKeyFactory rsaKeyFactory = mocker.getInstance(AsymmetricKeyFactory.class, "RSA");
            AsymmetricKeyFactory dsaKeyFactory = mocker.getInstance(AsymmetricKeyFactory.class, "DSA");
            CertificateFactory certFactory = mocker.getInstance(CertificateFactory.class, "X509");
            DefaultCMSSignedDataTest.rsaPrivateKey = rsaKeyFactory.fromPKCS8(base64encoder.decode(AbstractPKIXTest.RSA_PRIVATE_KEY));
            DefaultCMSSignedDataTest.dsaPrivateKey = dsaKeyFactory.fromPKCS8(base64encoder.decode(AbstractPKIXTest.DSA_PRIVATE_KEY));
            DefaultCMSSignedDataTest.v3CaCert = certFactory.decode(base64encoder.decode(AbstractPKIXTest.V3_CA_CERT));
            DefaultCMSSignedDataTest.v3InterCaCert = certFactory.decode(base64encoder.decode(AbstractPKIXTest.V3_ITERCA_CERT));
            DefaultCMSSignedDataTest.v3Cert = certFactory.decode(base64encoder.decode(AbstractPKIXTest.V3_CERT));
            DefaultCMSSignedDataTest.text = AbstractPKIXTest.TEXT.getBytes("UTF-8");
            DefaultCMSSignedDataTest.rsaSignerFactory = mocker.getInstance(SignerFactory.class, "SHA1withRSAEncryption");
            DefaultCMSSignedDataTest.dsaSignerFactory = mocker.getInstance(SignerFactory.class, "DSAwithSHA1");
        }
    }

    @Before
    public void configure() throws Exception {
        generator = generatorMocker.getComponentUnderTest();
        verifier = verifierMocker.getComponentUnderTest();
        setupTest(generatorMocker);
    }

    @Test
    public void testDSASignatureAllEmbedded() throws Exception {
        byte[] signature = generator.generate(DefaultCMSSignedDataTest.text, new CMSSignedDataGeneratorParameters().addSigner(CertifyingSigner.getInstance(true, new CertifiedKeyPair(DefaultCMSSignedDataTest.dsaPrivateKey, DefaultCMSSignedDataTest.v3Cert), DefaultCMSSignedDataTest.dsaSignerFactory)).addCertificate(DefaultCMSSignedDataTest.v3Cert).addCertificate(DefaultCMSSignedDataTest.v3InterCaCert).addCertificate(DefaultCMSSignedDataTest.v3CaCert), true);
        CMSSignedDataVerified result = verifier.verify(signature);
        Assert.assertThat(result.isVerified(), Matchers.equalTo(true));
        Assert.assertThat(result.getCertificates(), Matchers.containsInAnyOrder(DefaultCMSSignedDataTest.v3CaCert, DefaultCMSSignedDataTest.v3InterCaCert, DefaultCMSSignedDataTest.v3Cert));
        Assert.assertThat(result.getContent(), Matchers.equalTo(DefaultCMSSignedDataTest.text));
        Assert.assertThat(result.getContentType(), Matchers.equalTo("1.2.840.113549.1.7.1"));
        Assert.assertThat(result.getSignatures().size(), Matchers.equalTo(1));
        CMSSignerVerifiedInformation signerInfo = result.getSignatures().iterator().next();
        Assert.assertThat(signerInfo.isVerified(), Matchers.equalTo(true));
        Assert.assertThat(signerInfo.getCertificateChain(), Matchers.contains(DefaultCMSSignedDataTest.v3CaCert, DefaultCMSSignedDataTest.v3InterCaCert, DefaultCMSSignedDataTest.v3Cert));
    }

    @Test
    public void testDSASignatureWithExternalCerts() throws Exception {
        byte[] signature = generator.generate(DefaultCMSSignedDataTest.text, new CMSSignedDataGeneratorParameters().addSigner(CertifyingSigner.getInstance(true, new CertifiedKeyPair(DefaultCMSSignedDataTest.dsaPrivateKey, DefaultCMSSignedDataTest.v3Cert), DefaultCMSSignedDataTest.dsaSignerFactory)), true);
        CMSSignedDataVerified result = verifier.verify(signature, Arrays.asList(DefaultCMSSignedDataTest.v3Cert, DefaultCMSSignedDataTest.v3InterCaCert, DefaultCMSSignedDataTest.v3CaCert));
        Assert.assertThat(result.isVerified(), Matchers.equalTo(true));
        Assert.assertThat(result.getCertificates().isEmpty(), Matchers.equalTo(true));
        Assert.assertThat(result.getContent(), Matchers.equalTo(DefaultCMSSignedDataTest.text));
        Assert.assertThat(result.getContentType(), Matchers.equalTo("1.2.840.113549.1.7.1"));
        Assert.assertThat(result.getSignatures().size(), Matchers.equalTo(1));
        CMSSignerVerifiedInformation signerInfo = result.getSignatures().iterator().next();
        Assert.assertThat(signerInfo.isVerified(), Matchers.equalTo(true));
        Assert.assertThat(signerInfo.getCertificateChain(), Matchers.contains(DefaultCMSSignedDataTest.v3CaCert, DefaultCMSSignedDataTest.v3InterCaCert, DefaultCMSSignedDataTest.v3Cert));
    }

    @Test(timeout = 10000)
    public void testDSADetachedSignatureWithEmbeddedCerts() throws Exception {
        CMSSignedDataVerified result = verifier.verify(generator.generate(DefaultCMSSignedDataTest.text, new CMSSignedDataGeneratorParameters().addSigner(CertifyingSigner.getInstance(true, new CertifiedKeyPair(DefaultCMSSignedDataTest.dsaPrivateKey, DefaultCMSSignedDataTest.v3Cert), DefaultCMSSignedDataTest.dsaSignerFactory)).addCertificate(DefaultCMSSignedDataTest.v3Cert).addCertificate(DefaultCMSSignedDataTest.v3InterCaCert).addCertificate(DefaultCMSSignedDataTest.v3CaCert)), DefaultCMSSignedDataTest.text);
        result.isVerified();
        Matcher<Boolean> o_testDSADetachedSignatureWithEmbeddedCerts__13 = Matchers.equalTo(true);
        Assert.assertEquals("<true>", ((IsEqual) (o_testDSADetachedSignatureWithEmbeddedCerts__13)).toString());
        result.getCertificates();
        Matchers.containsInAnyOrder(DefaultCMSSignedDataTest.v3CaCert, DefaultCMSSignedDataTest.v3InterCaCert, DefaultCMSSignedDataTest.v3Cert);
        result.getContent();
        Matcher<byte[]> o_testDSADetachedSignatureWithEmbeddedCerts__17 = Matchers.equalTo(DefaultCMSSignedDataTest.text);
        Assert.assertEquals("[<67>, <111>, <110>, <103>, <114>, <101>, <115>, <115>, <32>, <115>, <104>, <97>, <108>, <108>, <32>, <109>, <97>, <107>, <101>, <32>, <110>, <111>, <32>, <108>, <97>, <119>, <32>, <114>, <101>, <115>, <112>, <101>, <99>, <116>, <105>, <110>, <103>, <32>, <97>, <110>, <32>, <101>, <115>, <116>, <97>, <98>, <108>, <105>, <115>, <104>, <109>, <101>, <110>, <116>, <32>, <111>, <102>, <32>, <114>, <101>, <108>, <105>, <103>, <105>, <111>, <110>, <44>, <32>, <111>, <114>, <32>, <112>, <114>, <111>, <104>, <105>, <98>, <105>, <116>, <105>, <110>, <103>, <32>, <116>, <104>, <101>, <32>, <102>, <114>, <101>, <101>, <32>, <101>, <120>, <101>, <114>, <99>, <105>, <115>, <101>, <32>, <116>, <104>, <101>, <114>, <101>, <111>, <102>, <59>, <32>, <111>, <114>, <32>, <97>, <98>, <114>, <105>, <100>, <103>, <105>, <110>, <103>, <32>, <116>, <104>, <101>, <32>, <102>, <114>, <101>, <101>, <100>, <111>, <109>, <32>, <111>, <102>, <32>, <115>, <112>, <101>, <101>, <99>, <104>, <44>, <32>, <111>, <114>, <32>, <111>, <102>, <32>, <116>, <104>, <101>, <32>, <112>, <114>, <101>, <115>, <115>, <59>, <32>, <111>, <114>, <32>, <116>, <104>, <101>, <32>, <114>, <105>, <103>, <104>, <116>, <32>, <111>, <102>, <32>, <116>, <104>, <101>, <32>, <112>, <101>, <111>, <112>, <108>, <101>, <32>, <112>, <101>, <97>, <99>, <101>, <97>, <98>, <108>, <121>, <32>, <116>, <111>, <32>, <97>, <115>, <115>, <101>, <109>, <98>, <108>, <101>, <44>, <32>, <97>, <110>, <100>, <32>, <116>, <111>, <32>, <112>, <101>, <116>, <105>, <116>, <105>, <111>, <110>, <32>, <116>, <104>, <101>, <32>, <71>, <111>, <118>, <101>, <114>, <110>, <109>, <101>, <110>, <116>, <32>, <102>, <111>, <114>, <32>, <97>, <32>, <114>, <101>, <100>, <114>, <101>, <115>, <115>, <32>, <111>, <102>, <32>, <103>, <114>, <105>, <101>, <118>, <97>, <110>, <99>, <101>, <115>, <46>]", ((IsEqual) (o_testDSADetachedSignatureWithEmbeddedCerts__17)).toString());
        result.getContentType();
        Matcher<String> o_testDSADetachedSignatureWithEmbeddedCerts__19 = Matchers.equalTo("1.2.840.113549.1.7.1");
        Assert.assertEquals("\"1.2.840.113549.1.7.1\"", ((IsEqual) (o_testDSADetachedSignatureWithEmbeddedCerts__19)).toString());
        int o_testDSADetachedSignatureWithEmbeddedCerts__20 = result.getSignatures().size();
        Assert.assertEquals(1, ((int) (o_testDSADetachedSignatureWithEmbeddedCerts__20)));
        Matcher<Integer> o_testDSADetachedSignatureWithEmbeddedCerts__22 = Matchers.equalTo(1);
        Assert.assertEquals("<1>", ((IsEqual) (o_testDSADetachedSignatureWithEmbeddedCerts__22)).toString());
        CMSSignerVerifiedInformation signerInfo = result.getSignatures().iterator().next();
        signerInfo.isVerified();
        Matcher<Boolean> o_testDSADetachedSignatureWithEmbeddedCerts__28 = Matchers.equalTo(true);
        Assert.assertEquals("<true>", ((IsEqual) (o_testDSADetachedSignatureWithEmbeddedCerts__28)).toString());
        signerInfo.getCertificateChain();
        Matchers.contains(DefaultCMSSignedDataTest.v3CaCert, DefaultCMSSignedDataTest.v3InterCaCert, DefaultCMSSignedDataTest.v3Cert);
    }

    @Test
    public void testDSADetachedSignatureWithExternalCerts() throws Exception {
        byte[] signature = generator.generate(DefaultCMSSignedDataTest.text, new CMSSignedDataGeneratorParameters().addSigner(CertifyingSigner.getInstance(true, new CertifiedKeyPair(DefaultCMSSignedDataTest.dsaPrivateKey, DefaultCMSSignedDataTest.v3Cert), DefaultCMSSignedDataTest.dsaSignerFactory)));
        CMSSignedDataVerified result = verifier.verify(signature, DefaultCMSSignedDataTest.text, Arrays.asList(DefaultCMSSignedDataTest.v3Cert, DefaultCMSSignedDataTest.v3InterCaCert, DefaultCMSSignedDataTest.v3CaCert));
        Assert.assertThat(result.isVerified(), Matchers.equalTo(true));
        Assert.assertThat(result.getCertificates().isEmpty(), Matchers.equalTo(true));
        Assert.assertThat(result.getContent(), Matchers.equalTo(DefaultCMSSignedDataTest.text));
        Assert.assertThat(result.getContentType(), Matchers.equalTo("1.2.840.113549.1.7.1"));
        Assert.assertThat(result.getSignatures().size(), Matchers.equalTo(1));
        CMSSignerVerifiedInformation signerInfo = result.getSignatures().iterator().next();
        Assert.assertThat(signerInfo.isVerified(), Matchers.equalTo(true));
        Assert.assertThat(signerInfo.getCertificateChain(), Matchers.contains(DefaultCMSSignedDataTest.v3CaCert, DefaultCMSSignedDataTest.v3InterCaCert, DefaultCMSSignedDataTest.v3Cert));
    }

    @Test
    public void testDSADetachedSignatureWitMixedCerts() throws Exception {
        byte[] signature = generator.generate(DefaultCMSSignedDataTest.text, new CMSSignedDataGeneratorParameters().addSigner(CertifyingSigner.getInstance(true, new CertifiedKeyPair(DefaultCMSSignedDataTest.dsaPrivateKey, DefaultCMSSignedDataTest.v3Cert), DefaultCMSSignedDataTest.dsaSignerFactory)).addCertificate(DefaultCMSSignedDataTest.v3Cert));
        CMSSignedDataVerified result = verifier.verify(signature, DefaultCMSSignedDataTest.text, Arrays.asList(DefaultCMSSignedDataTest.v3InterCaCert, DefaultCMSSignedDataTest.v3CaCert));
        Assert.assertThat(result.isVerified(), Matchers.equalTo(true));
        Assert.assertThat(result.getCertificates(), Matchers.containsInAnyOrder(DefaultCMSSignedDataTest.v3Cert));
        Assert.assertThat(result.getContent(), Matchers.equalTo(DefaultCMSSignedDataTest.text));
        Assert.assertThat(result.getContentType(), Matchers.equalTo("1.2.840.113549.1.7.1"));
        Assert.assertThat(result.getSignatures().size(), Matchers.equalTo(1));
        CMSSignerVerifiedInformation signerInfo = result.getSignatures().iterator().next();
        Assert.assertThat(signerInfo.isVerified(), Matchers.equalTo(true));
        Assert.assertThat(signerInfo.getCertificateChain(), Matchers.contains(DefaultCMSSignedDataTest.v3CaCert, DefaultCMSSignedDataTest.v3InterCaCert, DefaultCMSSignedDataTest.v3Cert));
    }

    @Test
    public void testPreCalculatedSignature() throws Exception {
        byte[] signature = generator.generate(DefaultCMSSignedDataTest.text, new CMSSignedDataGeneratorParameters().addSigner(CertifyingSigner.getInstance(true, new CertifiedKeyPair(DefaultCMSSignedDataTest.dsaPrivateKey, DefaultCMSSignedDataTest.v3Cert), DefaultCMSSignedDataTest.dsaSignerFactory)));
        CMSSignedDataVerified result = verifier.verify(signature, DefaultCMSSignedDataTest.text, Arrays.asList(DefaultCMSSignedDataTest.v3Cert, DefaultCMSSignedDataTest.v3InterCaCert, DefaultCMSSignedDataTest.v3CaCert));
        byte[] signature2 = generator.generate(DefaultCMSSignedDataTest.text, new CMSSignedDataGeneratorParameters().addSignature(result.getSignatures().iterator().next()));
        result = verifier.verify(signature2, DefaultCMSSignedDataTest.text, Arrays.asList(DefaultCMSSignedDataTest.v3Cert, DefaultCMSSignedDataTest.v3InterCaCert, DefaultCMSSignedDataTest.v3CaCert));
        Assert.assertThat(signature2, Matchers.equalTo(signature));
    }

    @Test
    public void testAddingCertificatesToSignature() throws Exception {
        byte[] signature = generator.generate(DefaultCMSSignedDataTest.text, new CMSSignedDataGeneratorParameters().addSigner(CertifyingSigner.getInstance(true, new CertifiedKeyPair(DefaultCMSSignedDataTest.dsaPrivateKey, DefaultCMSSignedDataTest.v3Cert), DefaultCMSSignedDataTest.dsaSignerFactory)));
        CMSSignedDataVerified result = verifier.verify(signature, DefaultCMSSignedDataTest.text, Arrays.asList(DefaultCMSSignedDataTest.v3Cert, DefaultCMSSignedDataTest.v3InterCaCert, DefaultCMSSignedDataTest.v3CaCert));
        byte[] signature2 = generator.generate(DefaultCMSSignedDataTest.text, new CMSSignedDataGeneratorParameters().addSignature(result.getSignatures().iterator().next()).addCertificates(result.getSignatures().iterator().next().getCertificateChain()));
        result = verifier.verify(signature2, DefaultCMSSignedDataTest.text);
        Assert.assertThat(result.isVerified(), Matchers.equalTo(true));
        Assert.assertThat(result.getCertificates(), Matchers.containsInAnyOrder(DefaultCMSSignedDataTest.v3CaCert, DefaultCMSSignedDataTest.v3InterCaCert, DefaultCMSSignedDataTest.v3Cert));
        Assert.assertThat(result.getSignatures().size(), Matchers.equalTo(1));
        CMSSignerVerifiedInformation signerInfo = result.getSignatures().iterator().next();
        Assert.assertThat(signerInfo.isVerified(), Matchers.equalTo(true));
        Assert.assertThat(signerInfo.getCertificateChain(), Matchers.contains(DefaultCMSSignedDataTest.v3CaCert, DefaultCMSSignedDataTest.v3InterCaCert, DefaultCMSSignedDataTest.v3Cert));
    }
}

