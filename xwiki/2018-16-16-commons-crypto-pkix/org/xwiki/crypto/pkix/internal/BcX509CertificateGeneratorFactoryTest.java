package org.xwiki.crypto.pkix.internal;


import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import javax.mail.internet.InternetAddress;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.util.IPAddress;
import org.hamcrest.Matchers;
import org.hamcrest.core.AnyOf;
import org.hamcrest.core.IsEqual;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.xwiki.crypto.AbstractPKIXTest;
import org.xwiki.crypto.AsymmetricKeyFactory;
import org.xwiki.crypto.BinaryStringEncoder;
import org.xwiki.crypto.internal.DefaultSecureRandomProvider;
import org.xwiki.crypto.internal.asymmetric.keyfactory.BcDSAKeyFactory;
import org.xwiki.crypto.internal.asymmetric.keyfactory.BcRSAKeyFactory;
import org.xwiki.crypto.internal.digest.factory.BcSHA1DigestFactory;
import org.xwiki.crypto.internal.encoder.Base64BinaryStringEncoder;
import org.xwiki.crypto.params.cipher.asymmetric.PrivateKeyParameters;
import org.xwiki.crypto.params.cipher.asymmetric.PublicKeyParameters;
import org.xwiki.crypto.pkix.CertificateGenerator;
import org.xwiki.crypto.pkix.CertificateGeneratorFactory;
import org.xwiki.crypto.pkix.CertifyingSigner;
import org.xwiki.crypto.pkix.X509ExtensionBuilder;
import org.xwiki.crypto.pkix.internal.extension.DefaultX509ExtensionBuilder;
import org.xwiki.crypto.pkix.params.CertifiedKeyPair;
import org.xwiki.crypto.pkix.params.CertifiedPublicKey;
import org.xwiki.crypto.pkix.params.PrincipalIndentifier;
import org.xwiki.crypto.pkix.params.x509certificate.DistinguishedName;
import org.xwiki.crypto.pkix.params.x509certificate.X509CertificateGenerationParameters;
import org.xwiki.crypto.pkix.params.x509certificate.X509CertificateParameters;
import org.xwiki.crypto.pkix.params.x509certificate.X509CertifiedPublicKey;
import org.xwiki.crypto.pkix.params.x509certificate.extension.ExtendedKeyUsages;
import org.xwiki.crypto.pkix.params.x509certificate.extension.KeyUsage;
import org.xwiki.crypto.pkix.params.x509certificate.extension.X509DirectoryName;
import org.xwiki.crypto.pkix.params.x509certificate.extension.X509DnsName;
import org.xwiki.crypto.pkix.params.x509certificate.extension.X509Extensions;
import org.xwiki.crypto.pkix.params.x509certificate.extension.X509GeneralName;
import org.xwiki.crypto.pkix.params.x509certificate.extension.X509IpAddress;
import org.xwiki.crypto.pkix.params.x509certificate.extension.X509Rfc822Name;
import org.xwiki.crypto.pkix.params.x509certificate.extension.X509StringGeneralName;
import org.xwiki.crypto.pkix.params.x509certificate.extension.X509URI;
import org.xwiki.crypto.signer.SignerFactory;
import org.xwiki.crypto.signer.internal.factory.BcDSAwithSHA1SignerFactory;
import org.xwiki.crypto.signer.internal.factory.BcSHA1withRsaSignerFactory;
import org.xwiki.crypto.signer.internal.factory.DefaultSignerFactory;
import org.xwiki.test.annotation.ComponentList;
import org.xwiki.test.mockito.MockitoComponentMockingRule;


@ComponentList({ Base64BinaryStringEncoder.class, DefaultSecureRandomProvider.class, BcRSAKeyFactory.class, BcDSAKeyFactory.class, BcSHA1DigestFactory.class, BcSHA1withRsaSignerFactory.class, DefaultSignerFactory.class, BcX509CertificateFactory.class, BcDSAwithSHA1SignerFactory.class })
public class BcX509CertificateGeneratorFactoryTest extends AbstractPKIXTest {
    @Rule
    public final MockitoComponentMockingRule<CertificateGeneratorFactory> mocker = new MockitoComponentMockingRule<CertificateGeneratorFactory>(BcX509CertificateGeneratorFactory.class);

    @Rule
    public final MockitoComponentMockingRule<X509ExtensionBuilder> builderMocker = new MockitoComponentMockingRule<X509ExtensionBuilder>(DefaultX509ExtensionBuilder.class);

    private CertificateGeneratorFactory factory;

    private SignerFactory signerFactory;

    private static PrivateKeyParameters rsaPrivateKey;

    private static PublicKeyParameters rsaPublicKey;

    private static PrivateKeyParameters interCaDsaPrivateKey;

    private static PublicKeyParameters interCaDsaPublicKey;

    @SuppressWarnings("unused")
    private static PrivateKeyParameters dsaPrivateKey;

    private static PublicKeyParameters dsaPublicKey;

    public void setupTest(MockitoComponentMockingRule<CertificateGeneratorFactory> mocker) throws Exception {
        if ((BcX509CertificateGeneratorFactoryTest.rsaPrivateKey) == null) {
            BinaryStringEncoder base64encoder = mocker.getInstance(BinaryStringEncoder.class, "Base64");
            AsymmetricKeyFactory keyFactory = mocker.getInstance(AsymmetricKeyFactory.class, "RSA");
            BcX509CertificateGeneratorFactoryTest.rsaPrivateKey = keyFactory.fromPKCS8(base64encoder.decode(AbstractPKIXTest.RSA_PRIVATE_KEY));
            BcX509CertificateGeneratorFactoryTest.rsaPublicKey = keyFactory.fromX509(base64encoder.decode(AbstractPKIXTest.RSA_PUBLIC_KEY));
            keyFactory = mocker.getInstance(AsymmetricKeyFactory.class, "DSA");
            BcX509CertificateGeneratorFactoryTest.interCaDsaPrivateKey = keyFactory.fromPKCS8(base64encoder.decode(AbstractPKIXTest.INTERCA_DSA_PRIVATE_KEY));
            BcX509CertificateGeneratorFactoryTest.interCaDsaPublicKey = keyFactory.fromX509(base64encoder.decode(AbstractPKIXTest.INTERCA_DSA_PUBLIC_KEY));
            BcX509CertificateGeneratorFactoryTest.dsaPrivateKey = keyFactory.fromPKCS8(base64encoder.decode(AbstractPKIXTest.DSA_PRIVATE_KEY));
            BcX509CertificateGeneratorFactoryTest.dsaPublicKey = keyFactory.fromX509(base64encoder.decode(AbstractPKIXTest.DSA_PUBLIC_KEY));
        }
    }

    @Before
    public void configure() throws Exception {
        factory = mocker.getComponentUnderTest();
        signerFactory = mocker.getInstance(SignerFactory.class, "SHA1withRSAEncryption");
        setupTest(mocker);
    }

    private X509CertifiedPublicKey checkSelfSigned(CertifiedPublicKey certificate, int version) throws Exception {
        Assert.assertThat(certificate.getIssuer(), IsEqual.equalTo(((PrincipalIndentifier) (new DistinguishedName("CN=Test")))));
        Assert.assertThat(certificate.getSubject(), IsEqual.equalTo(((PrincipalIndentifier) (new DistinguishedName("CN=Test")))));
        Assert.assertThat(certificate.getIssuer(), IsEqual.equalTo(certificate.getSubject()));
        Assert.assertTrue("Signature should match used private key.", certificate.isSignedBy(BcX509CertificateGeneratorFactoryTest.rsaPublicKey));
        Assert.assertTrue("Signature should match subject public key.", certificate.isSignedBy(certificate.getPublicKeyParameters()));
        Assert.assertThat(certificate, IsInstanceOf.instanceOf(X509CertifiedPublicKey.class));
        X509CertifiedPublicKey x509cert = ((X509CertifiedPublicKey) (certificate));
        Assert.assertThat(x509cert.getVersionNumber(), IsEqual.equalTo(version));
        Date yesterday = new Date(((System.currentTimeMillis()) - 86400000));
        Date inMoreThan500Days = new Date(((System.currentTimeMillis()) + 43286400000L));
        Assert.assertTrue("Certificate should be valid today.", x509cert.isValidOn(new Date()));
        Assert.assertThat(x509cert.getNotBefore().getTime(), Matchers.greaterThan(yesterday.getTime()));
        Assert.assertThat(x509cert.getNotAfter().getTime(), Matchers.lessThan(inMoreThan500Days.getTime()));
        Assert.assertFalse("Certificate should not be valid yesterday.", x509cert.isValidOn(yesterday));
        Assert.assertFalse("Certificate should not be valid in more than 500 days.", x509cert.isValidOn(inMoreThan500Days));
        Assert.assertTrue(x509cert.isRootCA());
        return x509cert;
    }

    private X509CertifiedPublicKey checkRootSigned(CertifiedPublicKey certificate, int version) throws Exception {
        Assert.assertThat(certificate.getIssuer(), IsEqual.equalTo(((PrincipalIndentifier) (new DistinguishedName("CN=Test CA")))));
        Assert.assertThat(certificate.getSubject(), IsEqual.equalTo(((PrincipalIndentifier) (new DistinguishedName("CN=Test End Entity")))));
        Assert.assertTrue("Signature should match used private key.", certificate.isSignedBy(BcX509CertificateGeneratorFactoryTest.rsaPublicKey));
        Assert.assertThat(certificate, IsInstanceOf.instanceOf(X509CertifiedPublicKey.class));
        X509CertifiedPublicKey x509cert = ((X509CertifiedPublicKey) (certificate));
        Assert.assertThat(x509cert.getVersionNumber(), IsEqual.equalTo(version));
        Date yesterday = new Date(((System.currentTimeMillis()) - 86400000));
        Date inMoreThan500Days = new Date(((System.currentTimeMillis()) + 43286400000L));
        Assert.assertTrue("Certificate should be valid today.", x509cert.isValidOn(new Date()));
        Assert.assertThat(x509cert.getNotBefore().getTime(), Matchers.greaterThan(yesterday.getTime()));
        Assert.assertThat(x509cert.getNotAfter().getTime(), Matchers.lessThan(inMoreThan500Days.getTime()));
        Assert.assertFalse("Certificate should not be valid yesterday.", x509cert.isValidOn(yesterday));
        Assert.assertFalse("Certificate should not be valid in more than 500 days.", x509cert.isValidOn(inMoreThan500Days));
        Assert.assertFalse(x509cert.isRootCA());
        return x509cert;
    }

    @Test(timeout = 10000)
    public void testGenerateSelfSignedCertificateVersion1() throws Exception {
        CertifiedPublicKey certificate = this.factory.getInstance(this.signerFactory.getInstance(true, BcX509CertificateGeneratorFactoryTest.rsaPrivateKey), new X509CertificateGenerationParameters()).generate(new DistinguishedName("CN=Test"), BcX509CertificateGeneratorFactoryTest.rsaPublicKey, new X509CertificateParameters());
        Assert.assertTrue(((BcX509CertifiedPublicKey) (certificate)).isSelfSigned());
        Assert.assertEquals("CN=Test", ((PrincipalIndentifier) (((BcX509CertifiedPublicKey) (certificate)).getIssuer())).getName());
        byte[] array_1735271169 = new byte[]{48, 15, 49, 13, 48, 11, 6, 3, 85, 4, 3, 12, 4, 84, 101, 115, 116};
        	byte[] array_1678708783 = (byte[])((org.xwiki.crypto.pkix.params.PrincipalIndentifier)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getIssuer()).getEncoded();
        	for(int ii = 0; ii <array_1735271169.length; ii++) {
        		org.junit.Assert.assertEquals(array_1735271169[ii], array_1678708783[ii]);
        	};
        Assert.assertEquals("CN=Test", ((X500Name) (((DistinguishedName) (((BcX509CertifiedPublicKey) (certificate)).getIssuer())).getX500Name())).toString());
        Assert.assertEquals(-1505670250, ((int) (((X500Name) (((DistinguishedName) (((BcX509CertifiedPublicKey) (certificate)).getIssuer())).getX500Name())).hashCode())));
        byte[] array_1406000822 = new byte[]{48, 15, 49, 13, 48, 11, 6, 3, 85, 4, 3, 12, 4, 84, 101, 115, 116};
        	byte[] array_1988866289 = (byte[])((org.bouncycastle.asn1.x500.X500Name)((org.xwiki.crypto.pkix.params.x509certificate.DistinguishedName)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getIssuer()).getX500Name()).getEncoded();
        	for(int ii = 0; ii <array_1406000822.length; ii++) {
        		org.junit.Assert.assertEquals(array_1406000822[ii], array_1988866289[ii]);
        	};
        Assert.assertEquals(-1505670250, ((int) (((DistinguishedName) (((BcX509CertifiedPublicKey) (certificate)).getIssuer())).hashCode())));
        Assert.assertEquals("CN=Test", ((DistinguishedName) (((BcX509CertifiedPublicKey) (certificate)).getIssuer())).getName());
        byte[] array_439961467 = new byte[]{48, 15, 49, 13, 48, 11, 6, 3, 85, 4, 3, 12, 4, 84, 101, 115, 116};
        	byte[] array_1142357984 = (byte[])((org.xwiki.crypto.pkix.params.x509certificate.DistinguishedName)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getIssuer()).getEncoded();
        	for(int ii = 0; ii <array_439961467.length; ii++) {
        		org.junit.Assert.assertEquals(array_439961467[ii], array_1142357984[ii]);
        	};
        Assert.assertEquals("CN=Test", ((PrincipalIndentifier) (((BcX509CertifiedPublicKey) (certificate)).getSubject())).getName());
        byte[] array_789025789 = new byte[]{48, 15, 49, 13, 48, 11, 6, 3, 85, 4, 3, 12, 4, 84, 101, 115, 116};
        	byte[] array_248184307 = (byte[])((org.xwiki.crypto.pkix.params.PrincipalIndentifier)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getSubject()).getEncoded();
        	for(int ii = 0; ii <array_789025789.length; ii++) {
        		org.junit.Assert.assertEquals(array_789025789[ii], array_248184307[ii]);
        	};
        Assert.assertEquals("CN=Test", ((X500Name) (((DistinguishedName) (((BcX509CertifiedPublicKey) (certificate)).getSubject())).getX500Name())).toString());
        Assert.assertEquals(-1505670250, ((int) (((X500Name) (((DistinguishedName) (((BcX509CertifiedPublicKey) (certificate)).getSubject())).getX500Name())).hashCode())));
        byte[] array_610672743 = new byte[]{48, 15, 49, 13, 48, 11, 6, 3, 85, 4, 3, 12, 4, 84, 101, 115, 116};
        	byte[] array_416890303 = (byte[])((org.bouncycastle.asn1.x500.X500Name)((org.xwiki.crypto.pkix.params.x509certificate.DistinguishedName)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getSubject()).getX500Name()).getEncoded();
        	for(int ii = 0; ii <array_610672743.length; ii++) {
        		org.junit.Assert.assertEquals(array_610672743[ii], array_416890303[ii]);
        	};
        Assert.assertEquals(-1505670250, ((int) (((DistinguishedName) (((BcX509CertifiedPublicKey) (certificate)).getSubject())).hashCode())));
        Assert.assertEquals("CN=Test", ((DistinguishedName) (((BcX509CertifiedPublicKey) (certificate)).getSubject())).getName());
        byte[] array_602057079 = new byte[]{48, 15, 49, 13, 48, 11, 6, 3, 85, 4, 3, 12, 4, 84, 101, 115, 116};
        	byte[] array_477154052 = (byte[])((org.xwiki.crypto.pkix.params.x509certificate.DistinguishedName)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getSubject()).getEncoded();
        	for(int ii = 0; ii <array_602057079.length; ii++) {
        		org.junit.Assert.assertEquals(array_602057079[ii], array_477154052[ii]);
        	};
        Assert.assertFalse(((PublicKeyParameters) (((BcX509CertifiedPublicKey) (certificate)).getPublicKeyParameters())).isPrivate());
        byte[] array_1352744571 = new byte[]{48, -126, 1, 34, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 3, -126, 1, 15, 0, 48, -126, 1, 10, 2, -126, 1, 1, 0, -62, -102, 56, -90, -1, 121, 98, -112, -98, 21, 23, -98, -72, 83, 44, -96, -88, -8, -56, 95, -4, -12, -9, 64, 8, -104, -71, 116, 124, 50, -44, -112, -116, -126, 85, 52, -99, -41, -52, 9, -35, -84, 107, -42, -88, -53, -75, 40, 13, 61, 103, -83, -46, -101, -110, -60, -31, 71, -48, 12, -59, 41, 16, -119, 58, 53, 36, 65, 21, 60, -120, 9, 51, -21, -125, 11, 1, -1, 113, -81, 30, 39, 4, 64, -100, -118, -89, -127, -9, -115, 108, -45, 89, 11, -76, 111, 2, -3, 74, -126, -36, -17, 82, -64, 116, -99, -122, -43, 41, 81, 79, 21, -1, -35, 82, -120, 61, 61, -56, -46, 108, -100, -32, -30, -35, 106, -116, -34, -126, -32, -43, 6, 51, 42, 26, 26, 15, 105, -107, -93, 108, 73, 45, 15, -48, 94, -86, -10, 92, -121, 57, -117, 99, 37, -91, -6, -70, 75, -110, 77, 108, 80, -1, 35, -27, 5, -124, 21, 37, 4, -37, -125, 56, 69, 123, -74, -46, 45, -39, -31, 76, 17, 75, 31, -112, 39, -84, -76, 106, 52, 98, -124, 24, 58, -108, -53, 61, -61, -58, 69, 46, -52, -99, 38, 53, 8, -75, -122, 81, -118, -102, 49, 41, -34, -77, 73, -46, -14, 71, -74, 60, -16, -8, 67, 75, 46, 1, -100, -8, -102, -55, -31, 54, -25, -38, -98, -57, 44, -76, -49, 45, 112, -37, 108, 100, 94, -95, -113, 66, 111, 109, -102, 36, -13, -37, 51, 2, 3, 1, 0, 1};
        	byte[] array_551977022 = (byte[])((org.xwiki.crypto.params.cipher.asymmetric.PublicKeyParameters)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getPublicKeyParameters()).getEncoded();
        	for(int ii = 0; ii <array_1352744571.length; ii++) {
        		org.junit.Assert.assertEquals(array_1352744571[ii], array_551977022[ii]);
        	};
        Assert.assertEquals(1, ((int) (((BcX509CertifiedPublicKey) (certificate)).getVersionNumber())));
        Assert.assertEquals(9, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).getMonth())));
        Assert.assertEquals(12, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).getHours())));
        Assert.assertEquals(0, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).getMinutes())));
        Assert.assertEquals(0, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).getSeconds())));
        Assert.assertEquals(118, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).getYear())));
        Assert.assertEquals(2, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).getDay())));
        Assert.assertEquals(-120, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).getTimezoneOffset())));
        Assert.assertEquals(1539684000000L, ((long) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).getTime())));
        Assert.assertEquals(16, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).getDate())));
        Assert.assertEquals("Tue Oct 16 12:00:00 CEST 2018", ((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).toString());
        Assert.assertEquals(2085707878, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).hashCode())));
        Assert.assertEquals(1, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).getMonth())));
        Assert.assertEquals(12, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).getHours())));
        Assert.assertEquals(0, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).getMinutes())));
        Assert.assertEquals(0, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).getSeconds())));
        Assert.assertEquals(120, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).getYear())));
        Assert.assertEquals(5, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).getDay())));
        Assert.assertEquals(-60, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).getTimezoneOffset())));
        Assert.assertEquals(1582887600000L, ((long) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).getTime())));
        Assert.assertEquals(28, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).getDate())));
        Assert.assertEquals("Fri Feb 28 12:00:00 CET 2020", ((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).toString());
        Assert.assertEquals(-1955332368, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).hashCode())));
        Assert.assertTrue(((BcX509CertifiedPublicKey) (certificate)).isRootCA());
        Assert.assertNull(((BcX509CertifiedPublicKey) (certificate)).getAuthorityKeyIdentifier());
        Assert.assertNull(((BcX509CertifiedPublicKey) (certificate)).getSubjectKeyIdentifier());
        Assert.assertEquals(1272338794, ((int) (((AlgorithmIdentifier) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getSignatureAlgorithm())).hashCode())));
        byte[] array_541611308 = new byte[]{48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 5, 5, 0};
        	byte[] array_1075896060 = (byte[])((org.bouncycastle.asn1.x509.AlgorithmIdentifier)((org.bouncycastle.cert.X509CertificateHolder)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getX509CertificateHolder()).getSignatureAlgorithm()).getEncoded();
        	for(int ii = 0; ii <array_541611308.length; ii++) {
        		org.junit.Assert.assertEquals(array_541611308[ii], array_1075896060[ii]);
        	};
        Assert.assertTrue(((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getExtensionOIDs().isEmpty());
        Assert.assertTrue(((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNonCriticalExtensionOIDs().isEmpty());
        Assert.assertEquals(1, ((int) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getVersion())));
        Assert.assertTrue(((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getCriticalExtensionOIDs().isEmpty());
        Assert.assertEquals("CN=Test", ((X500Name) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getIssuer())).toString());
        Assert.assertEquals(-1505670250, ((int) (((X500Name) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getIssuer())).hashCode())));
        byte[] array_682353075 = new byte[]{48, 15, 49, 13, 48, 11, 6, 3, 85, 4, 3, 12, 4, 84, 101, 115, 116};
        	byte[] array_82884878 = (byte[])((org.bouncycastle.asn1.x500.X500Name)((org.bouncycastle.cert.X509CertificateHolder)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getX509CertificateHolder()).getIssuer()).getEncoded();
        	for(int ii = 0; ii <array_682353075.length; ii++) {
        		org.junit.Assert.assertEquals(array_682353075[ii], array_82884878[ii]);
        	};
        Assert.assertEquals("CN=Test", ((X500Name) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getSubject())).toString());
        Assert.assertEquals(-1505670250, ((int) (((X500Name) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getSubject())).hashCode())));
        byte[] array_88009965 = new byte[]{48, 15, 49, 13, 48, 11, 6, 3, 85, 4, 3, 12, 4, 84, 101, 115, 116};
        	byte[] array_1332478853 = (byte[])((org.bouncycastle.asn1.x500.X500Name)((org.bouncycastle.cert.X509CertificateHolder)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getX509CertificateHolder()).getSubject()).getEncoded();
        	for(int ii = 0; ii <array_88009965.length; ii++) {
        		org.junit.Assert.assertEquals(array_88009965[ii], array_1332478853[ii]);
        	};
        Assert.assertEquals(1, ((int) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getVersionNumber())));
        Assert.assertEquals(9, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).getMonth())));
        Assert.assertEquals(12, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).getHours())));
        Assert.assertEquals(0, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).getMinutes())));
        Assert.assertEquals(0, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).getSeconds())));
        Assert.assertEquals(118, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).getYear())));
        Assert.assertEquals(2, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).getDay())));
        Assert.assertEquals(-120, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).getTimezoneOffset())));
        Assert.assertEquals(1539684000000L, ((long) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).getTime())));
        Assert.assertEquals(16, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).getDate())));
        Assert.assertEquals("Tue Oct 16 12:00:00 CEST 2018", ((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).toString());
        Assert.assertEquals(2085707878, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).hashCode())));
        Assert.assertEquals(1, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).getMonth())));
        Assert.assertEquals(12, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).getHours())));
        Assert.assertEquals(0, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).getMinutes())));
        Assert.assertEquals(0, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).getSeconds())));
        Assert.assertEquals(120, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).getYear())));
        Assert.assertEquals(5, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).getDay())));
        Assert.assertEquals(-60, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).getTimezoneOffset())));
        Assert.assertEquals(1582887600000L, ((long) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).getTime())));
        Assert.assertEquals(28, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).getDate())));
        Assert.assertEquals("Fri Feb 28 12:00:00 CET 2020", ((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).toString());
        Assert.assertEquals(-1955332368, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).hashCode())));
        Assert.assertEquals(2055452705, ((int) (((SubjectPublicKeyInfo) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getSubjectPublicKeyInfo())).hashCode())));
        byte[] array_202437780 = new byte[]{48, -126, 1, 34, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 3, -126, 1, 15, 0, 48, -126, 1, 10, 2, -126, 1, 1, 0, -62, -102, 56, -90, -1, 121, 98, -112, -98, 21, 23, -98, -72, 83, 44, -96, -88, -8, -56, 95, -4, -12, -9, 64, 8, -104, -71, 116, 124, 50, -44, -112, -116, -126, 85, 52, -99, -41, -52, 9, -35, -84, 107, -42, -88, -53, -75, 40, 13, 61, 103, -83, -46, -101, -110, -60, -31, 71, -48, 12, -59, 41, 16, -119, 58, 53, 36, 65, 21, 60, -120, 9, 51, -21, -125, 11, 1, -1, 113, -81, 30, 39, 4, 64, -100, -118, -89, -127, -9, -115, 108, -45, 89, 11, -76, 111, 2, -3, 74, -126, -36, -17, 82, -64, 116, -99, -122, -43, 41, 81, 79, 21, -1, -35, 82, -120, 61, 61, -56, -46, 108, -100, -32, -30, -35, 106, -116, -34, -126, -32, -43, 6, 51, 42, 26, 26, 15, 105, -107, -93, 108, 73, 45, 15, -48, 94, -86, -10, 92, -121, 57, -117, 99, 37, -91, -6, -70, 75, -110, 77, 108, 80, -1, 35, -27, 5, -124, 21, 37, 4, -37, -125, 56, 69, 123, -74, -46, 45, -39, -31, 76, 17, 75, 31, -112, 39, -84, -76, 106, 52, 98, -124, 24, 58, -108, -53, 61, -61, -58, 69, 46, -52, -99, 38, 53, 8, -75, -122, 81, -118, -102, 49, 41, -34, -77, 73, -46, -14, 71, -74, 60, -16, -8, 67, 75, 46, 1, -100, -8, -102, -55, -31, 54, -25, -38, -98, -57, 44, -76, -49, 45, 112, -37, 108, 100, 94, -95, -113, 66, 111, 109, -102, 36, -13, -37, 51, 2, 3, 1, 0, 1};
        	byte[] array_256583365 = (byte[])((org.bouncycastle.asn1.x509.SubjectPublicKeyInfo)((org.bouncycastle.cert.X509CertificateHolder)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getX509CertificateHolder()).getSubjectPublicKeyInfo()).getEncoded();
        	for(int ii = 0; ii <array_202437780.length; ii++) {
        		org.junit.Assert.assertEquals(array_202437780[ii], array_256583365[ii]);
        	};
        Assert.assertFalse(((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).hasExtensions());
        Assert.assertNull(((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getExtensions());
        Assert.assertNull(((BcX509CertifiedPublicKey) (certificate)).getExtensions());
    }

    @Test(timeout = 10000)
    public void testGenerateSelfSignedCertificateVersion3WithoutExtension() throws Exception {
        CertifiedPublicKey certificate = this.factory.getInstance(this.signerFactory.getInstance(true, BcX509CertificateGeneratorFactoryTest.rsaPrivateKey), new X509CertificateGenerationParameters(null)).generate(new DistinguishedName("CN=Test"), BcX509CertificateGeneratorFactoryTest.rsaPublicKey, new X509CertificateParameters());
        Assert.assertTrue(((BcX509CertifiedPublicKey) (certificate)).isSelfSigned());
        Assert.assertEquals("CN=Test", ((PrincipalIndentifier) (((BcX509CertifiedPublicKey) (certificate)).getIssuer())).getName());
        byte[] array_292052944 = new byte[]{48, 15, 49, 13, 48, 11, 6, 3, 85, 4, 3, 12, 4, 84, 101, 115, 116};
        	byte[] array_974405979 = (byte[])((org.xwiki.crypto.pkix.params.PrincipalIndentifier)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getIssuer()).getEncoded();
        	for(int ii = 0; ii <array_292052944.length; ii++) {
        		org.junit.Assert.assertEquals(array_292052944[ii], array_974405979[ii]);
        	};
        Assert.assertEquals("CN=Test", ((X500Name) (((DistinguishedName) (((BcX509CertifiedPublicKey) (certificate)).getIssuer())).getX500Name())).toString());
        Assert.assertEquals(-1505670250, ((int) (((X500Name) (((DistinguishedName) (((BcX509CertifiedPublicKey) (certificate)).getIssuer())).getX500Name())).hashCode())));
        byte[] array_1618669629 = new byte[]{48, 15, 49, 13, 48, 11, 6, 3, 85, 4, 3, 12, 4, 84, 101, 115, 116};
        	byte[] array_1597744700 = (byte[])((org.bouncycastle.asn1.x500.X500Name)((org.xwiki.crypto.pkix.params.x509certificate.DistinguishedName)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getIssuer()).getX500Name()).getEncoded();
        	for(int ii = 0; ii <array_1618669629.length; ii++) {
        		org.junit.Assert.assertEquals(array_1618669629[ii], array_1597744700[ii]);
        	};
        Assert.assertEquals(-1505670250, ((int) (((DistinguishedName) (((BcX509CertifiedPublicKey) (certificate)).getIssuer())).hashCode())));
        Assert.assertEquals("CN=Test", ((DistinguishedName) (((BcX509CertifiedPublicKey) (certificate)).getIssuer())).getName());
        byte[] array_535783344 = new byte[]{48, 15, 49, 13, 48, 11, 6, 3, 85, 4, 3, 12, 4, 84, 101, 115, 116};
        	byte[] array_1384249954 = (byte[])((org.xwiki.crypto.pkix.params.x509certificate.DistinguishedName)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getIssuer()).getEncoded();
        	for(int ii = 0; ii <array_535783344.length; ii++) {
        		org.junit.Assert.assertEquals(array_535783344[ii], array_1384249954[ii]);
        	};
        Assert.assertEquals("CN=Test", ((PrincipalIndentifier) (((BcX509CertifiedPublicKey) (certificate)).getSubject())).getName());
        byte[] array_1830319410 = new byte[]{48, 15, 49, 13, 48, 11, 6, 3, 85, 4, 3, 12, 4, 84, 101, 115, 116};
        	byte[] array_1582929596 = (byte[])((org.xwiki.crypto.pkix.params.PrincipalIndentifier)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getSubject()).getEncoded();
        	for(int ii = 0; ii <array_1830319410.length; ii++) {
        		org.junit.Assert.assertEquals(array_1830319410[ii], array_1582929596[ii]);
        	};
        Assert.assertEquals("CN=Test", ((X500Name) (((DistinguishedName) (((BcX509CertifiedPublicKey) (certificate)).getSubject())).getX500Name())).toString());
        Assert.assertEquals(-1505670250, ((int) (((X500Name) (((DistinguishedName) (((BcX509CertifiedPublicKey) (certificate)).getSubject())).getX500Name())).hashCode())));
        byte[] array_1844084588 = new byte[]{48, 15, 49, 13, 48, 11, 6, 3, 85, 4, 3, 12, 4, 84, 101, 115, 116};
        	byte[] array_552152772 = (byte[])((org.bouncycastle.asn1.x500.X500Name)((org.xwiki.crypto.pkix.params.x509certificate.DistinguishedName)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getSubject()).getX500Name()).getEncoded();
        	for(int ii = 0; ii <array_1844084588.length; ii++) {
        		org.junit.Assert.assertEquals(array_1844084588[ii], array_552152772[ii]);
        	};
        Assert.assertEquals(-1505670250, ((int) (((DistinguishedName) (((BcX509CertifiedPublicKey) (certificate)).getSubject())).hashCode())));
        Assert.assertEquals("CN=Test", ((DistinguishedName) (((BcX509CertifiedPublicKey) (certificate)).getSubject())).getName());
        byte[] array_1477321960 = new byte[]{48, 15, 49, 13, 48, 11, 6, 3, 85, 4, 3, 12, 4, 84, 101, 115, 116};
        	byte[] array_668631891 = (byte[])((org.xwiki.crypto.pkix.params.x509certificate.DistinguishedName)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getSubject()).getEncoded();
        	for(int ii = 0; ii <array_1477321960.length; ii++) {
        		org.junit.Assert.assertEquals(array_1477321960[ii], array_668631891[ii]);
        	};
        Assert.assertFalse(((PublicKeyParameters) (((BcX509CertifiedPublicKey) (certificate)).getPublicKeyParameters())).isPrivate());
        byte[] array_2035544258 = new byte[]{48, -126, 1, 34, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 3, -126, 1, 15, 0, 48, -126, 1, 10, 2, -126, 1, 1, 0, -62, -102, 56, -90, -1, 121, 98, -112, -98, 21, 23, -98, -72, 83, 44, -96, -88, -8, -56, 95, -4, -12, -9, 64, 8, -104, -71, 116, 124, 50, -44, -112, -116, -126, 85, 52, -99, -41, -52, 9, -35, -84, 107, -42, -88, -53, -75, 40, 13, 61, 103, -83, -46, -101, -110, -60, -31, 71, -48, 12, -59, 41, 16, -119, 58, 53, 36, 65, 21, 60, -120, 9, 51, -21, -125, 11, 1, -1, 113, -81, 30, 39, 4, 64, -100, -118, -89, -127, -9, -115, 108, -45, 89, 11, -76, 111, 2, -3, 74, -126, -36, -17, 82, -64, 116, -99, -122, -43, 41, 81, 79, 21, -1, -35, 82, -120, 61, 61, -56, -46, 108, -100, -32, -30, -35, 106, -116, -34, -126, -32, -43, 6, 51, 42, 26, 26, 15, 105, -107, -93, 108, 73, 45, 15, -48, 94, -86, -10, 92, -121, 57, -117, 99, 37, -91, -6, -70, 75, -110, 77, 108, 80, -1, 35, -27, 5, -124, 21, 37, 4, -37, -125, 56, 69, 123, -74, -46, 45, -39, -31, 76, 17, 75, 31, -112, 39, -84, -76, 106, 52, 98, -124, 24, 58, -108, -53, 61, -61, -58, 69, 46, -52, -99, 38, 53, 8, -75, -122, 81, -118, -102, 49, 41, -34, -77, 73, -46, -14, 71, -74, 60, -16, -8, 67, 75, 46, 1, -100, -8, -102, -55, -31, 54, -25, -38, -98, -57, 44, -76, -49, 45, 112, -37, 108, 100, 94, -95, -113, 66, 111, 109, -102, 36, -13, -37, 51, 2, 3, 1, 0, 1};
        	byte[] array_1650846545 = (byte[])((org.xwiki.crypto.params.cipher.asymmetric.PublicKeyParameters)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getPublicKeyParameters()).getEncoded();
        	for(int ii = 0; ii <array_2035544258.length; ii++) {
        		org.junit.Assert.assertEquals(array_2035544258[ii], array_1650846545[ii]);
        	};
        Assert.assertEquals(3, ((int) (((BcX509CertifiedPublicKey) (certificate)).getVersionNumber())));
        Assert.assertEquals(9, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).getMonth())));
        Assert.assertEquals(12, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).getHours())));
        Assert.assertEquals(0, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).getMinutes())));
        Assert.assertEquals(0, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).getSeconds())));
        Assert.assertEquals(118, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).getYear())));
        Assert.assertEquals(2, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).getDay())));
        Assert.assertEquals(-120, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).getTimezoneOffset())));
        Assert.assertEquals(1539684000000L, ((long) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).getTime())));
        Assert.assertEquals(16, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).getDate())));
        Assert.assertEquals("Tue Oct 16 12:00:00 CEST 2018", ((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).toString());
        Assert.assertEquals(2085707878, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotBefore())).hashCode())));
        Assert.assertEquals(1, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).getMonth())));
        Assert.assertEquals(12, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).getHours())));
        Assert.assertEquals(0, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).getMinutes())));
        Assert.assertEquals(0, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).getSeconds())));
        Assert.assertEquals(120, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).getYear())));
        Assert.assertEquals(5, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).getDay())));
        Assert.assertEquals(-60, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).getTimezoneOffset())));
        Assert.assertEquals(1582887600000L, ((long) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).getTime())));
        Assert.assertEquals(28, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).getDate())));
        Assert.assertEquals("Fri Feb 28 12:00:00 CET 2020", ((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).toString());
        Assert.assertEquals(-1955332368, ((int) (((Date) (((BcX509CertifiedPublicKey) (certificate)).getNotAfter())).hashCode())));
        Assert.assertTrue(((BcX509CertifiedPublicKey) (certificate)).isRootCA());
        Assert.assertNull(((BcX509CertifiedPublicKey) (certificate)).getAuthorityKeyIdentifier());
        Assert.assertNull(((BcX509CertifiedPublicKey) (certificate)).getSubjectKeyIdentifier());
        Assert.assertEquals(1272338794, ((int) (((AlgorithmIdentifier) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getSignatureAlgorithm())).hashCode())));
        byte[] array_1294923761 = new byte[]{48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 5, 5, 0};
        	byte[] array_547186133 = (byte[])((org.bouncycastle.asn1.x509.AlgorithmIdentifier)((org.bouncycastle.cert.X509CertificateHolder)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getX509CertificateHolder()).getSignatureAlgorithm()).getEncoded();
        	for(int ii = 0; ii <array_1294923761.length; ii++) {
        		org.junit.Assert.assertEquals(array_1294923761[ii], array_547186133[ii]);
        	};
        Assert.assertTrue(((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getExtensionOIDs().isEmpty());
        Assert.assertTrue(((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNonCriticalExtensionOIDs().isEmpty());
        Assert.assertEquals(3, ((int) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getVersion())));
        Assert.assertTrue(((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getCriticalExtensionOIDs().isEmpty());
        Assert.assertEquals("CN=Test", ((X500Name) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getIssuer())).toString());
        Assert.assertEquals(-1505670250, ((int) (((X500Name) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getIssuer())).hashCode())));
        byte[] array_2096018921 = new byte[]{48, 15, 49, 13, 48, 11, 6, 3, 85, 4, 3, 12, 4, 84, 101, 115, 116};
        	byte[] array_234843415 = (byte[])((org.bouncycastle.asn1.x500.X500Name)((org.bouncycastle.cert.X509CertificateHolder)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getX509CertificateHolder()).getIssuer()).getEncoded();
        	for(int ii = 0; ii <array_2096018921.length; ii++) {
        		org.junit.Assert.assertEquals(array_2096018921[ii], array_234843415[ii]);
        	};
        Assert.assertEquals("CN=Test", ((X500Name) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getSubject())).toString());
        Assert.assertEquals(-1505670250, ((int) (((X500Name) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getSubject())).hashCode())));
        byte[] array_1093504419 = new byte[]{48, 15, 49, 13, 48, 11, 6, 3, 85, 4, 3, 12, 4, 84, 101, 115, 116};
        	byte[] array_1569961614 = (byte[])((org.bouncycastle.asn1.x500.X500Name)((org.bouncycastle.cert.X509CertificateHolder)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getX509CertificateHolder()).getSubject()).getEncoded();
        	for(int ii = 0; ii <array_1093504419.length; ii++) {
        		org.junit.Assert.assertEquals(array_1093504419[ii], array_1569961614[ii]);
        	};
        Assert.assertEquals(3, ((int) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getVersionNumber())));
        Assert.assertEquals(9, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).getMonth())));
        Assert.assertEquals(12, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).getHours())));
        Assert.assertEquals(0, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).getMinutes())));
        Assert.assertEquals(0, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).getSeconds())));
        Assert.assertEquals(118, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).getYear())));
        Assert.assertEquals(2, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).getDay())));
        Assert.assertEquals(-120, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).getTimezoneOffset())));
        Assert.assertEquals(1539684000000L, ((long) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).getTime())));
        Assert.assertEquals(16, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).getDate())));
        Assert.assertEquals("Tue Oct 16 12:00:00 CEST 2018", ((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).toString());
        Assert.assertEquals(2085707878, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotBefore())).hashCode())));
        Assert.assertEquals(1, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).getMonth())));
        Assert.assertEquals(12, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).getHours())));
        Assert.assertEquals(0, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).getMinutes())));
        Assert.assertEquals(0, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).getSeconds())));
        Assert.assertEquals(120, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).getYear())));
        Assert.assertEquals(5, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).getDay())));
        Assert.assertEquals(-60, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).getTimezoneOffset())));
        Assert.assertEquals(1582887600000L, ((long) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).getTime())));
        Assert.assertEquals(28, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).getDate())));
        Assert.assertEquals("Fri Feb 28 12:00:00 CET 2020", ((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).toString());
        Assert.assertEquals(-1955332368, ((int) (((Date) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getNotAfter())).hashCode())));
        Assert.assertEquals(2055452705, ((int) (((SubjectPublicKeyInfo) (((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getSubjectPublicKeyInfo())).hashCode())));
        byte[] array_699819759 = new byte[]{48, -126, 1, 34, 48, 13, 6, 9, 42, -122, 72, -122, -9, 13, 1, 1, 1, 5, 0, 3, -126, 1, 15, 0, 48, -126, 1, 10, 2, -126, 1, 1, 0, -62, -102, 56, -90, -1, 121, 98, -112, -98, 21, 23, -98, -72, 83, 44, -96, -88, -8, -56, 95, -4, -12, -9, 64, 8, -104, -71, 116, 124, 50, -44, -112, -116, -126, 85, 52, -99, -41, -52, 9, -35, -84, 107, -42, -88, -53, -75, 40, 13, 61, 103, -83, -46, -101, -110, -60, -31, 71, -48, 12, -59, 41, 16, -119, 58, 53, 36, 65, 21, 60, -120, 9, 51, -21, -125, 11, 1, -1, 113, -81, 30, 39, 4, 64, -100, -118, -89, -127, -9, -115, 108, -45, 89, 11, -76, 111, 2, -3, 74, -126, -36, -17, 82, -64, 116, -99, -122, -43, 41, 81, 79, 21, -1, -35, 82, -120, 61, 61, -56, -46, 108, -100, -32, -30, -35, 106, -116, -34, -126, -32, -43, 6, 51, 42, 26, 26, 15, 105, -107, -93, 108, 73, 45, 15, -48, 94, -86, -10, 92, -121, 57, -117, 99, 37, -91, -6, -70, 75, -110, 77, 108, 80, -1, 35, -27, 5, -124, 21, 37, 4, -37, -125, 56, 69, 123, -74, -46, 45, -39, -31, 76, 17, 75, 31, -112, 39, -84, -76, 106, 52, 98, -124, 24, 58, -108, -53, 61, -61, -58, 69, 46, -52, -99, 38, 53, 8, -75, -122, 81, -118, -102, 49, 41, -34, -77, 73, -46, -14, 71, -74, 60, -16, -8, 67, 75, 46, 1, -100, -8, -102, -55, -31, 54, -25, -38, -98, -57, 44, -76, -49, 45, 112, -37, 108, 100, 94, -95, -113, 66, 111, 109, -102, 36, -13, -37, 51, 2, 3, 1, 0, 1};
        	byte[] array_591333741 = (byte[])((org.bouncycastle.asn1.x509.SubjectPublicKeyInfo)((org.bouncycastle.cert.X509CertificateHolder)((org.xwiki.crypto.pkix.internal.BcX509CertifiedPublicKey)certificate).getX509CertificateHolder()).getSubjectPublicKeyInfo()).getEncoded();
        	for(int ii = 0; ii <array_699819759.length; ii++) {
        		org.junit.Assert.assertEquals(array_699819759[ii], array_591333741[ii]);
        	};
        Assert.assertFalse(((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).hasExtensions());
        Assert.assertNull(((X509CertificateHolder) (((BcX509CertifiedPublicKey) (certificate)).getX509CertificateHolder())).getExtensions());
        Assert.assertNull(((BcX509CertifiedPublicKey) (certificate)).getExtensions());
    }

    @Test
    public void testGenerateSelfSignedCertificateVersion3RootCa() throws Exception {
        X509ExtensionBuilder builder = builderMocker.getComponentUnderTest();
        CertifiedPublicKey certificate = factory.getInstance(signerFactory.getInstance(true, BcX509CertificateGeneratorFactoryTest.rsaPrivateKey), new X509CertificateGenerationParameters(builder.addBasicConstraints(true).addKeyUsage(true, EnumSet.of(KeyUsage.keyCertSign, KeyUsage.cRLSign)).build())).generate(new DistinguishedName("CN=Test"), BcX509CertificateGeneratorFactoryTest.rsaPublicKey, new X509CertificateParameters());
        X509CertifiedPublicKey cert = checkSelfSigned(certificate, 3);
        Assert.assertTrue("Basic constraints should be critical.", cert.getExtensions().isCritical(X509Extensions.BASIC_CONSTRAINTS_OID));
        Assert.assertTrue("Basic constraints should be set to CA.", cert.getExtensions().hasCertificateAuthorityBasicConstraints());
        Assert.assertTrue("KeyUsage extension should be critical.", cert.getExtensions().isCritical(KeyUsage.OID));
        Assert.assertThat(cert.getExtensions().getKeyUsage(), IsEqual.equalTo(EnumSet.of(KeyUsage.keyCertSign, KeyUsage.cRLSign)));
        Assert.assertThat(cert.getAuthorityKeyIdentifier(), Matchers.notNullValue());
        Assert.assertThat(cert.getAuthorityKeyIdentifier(), IsEqual.equalTo(cert.getSubjectKeyIdentifier()));
    }

    @Test
    public void testGenerateEndEntitySignedCertificateVersion1() throws Exception {
        CertifiedPublicKey caCertificate = factory.getInstance(signerFactory.getInstance(true, BcX509CertificateGeneratorFactoryTest.rsaPrivateKey), new X509CertificateGenerationParameters()).generate(new DistinguishedName("CN=Test CA"), BcX509CertificateGeneratorFactoryTest.rsaPublicKey, new X509CertificateParameters());
        CertifiedPublicKey certificate = factory.getInstance(CertifyingSigner.getInstance(true, new CertifiedKeyPair(BcX509CertificateGeneratorFactoryTest.rsaPrivateKey, caCertificate), signerFactory), new X509CertificateGenerationParameters()).generate(new DistinguishedName("CN=Test End Entity"), BcX509CertificateGeneratorFactoryTest.dsaPublicKey, new X509CertificateParameters());
        checkRootSigned(certificate, 1);
    }

    @Test
    public void testGenerateEndEntitySignedCertificateVersion3() throws Exception {
        X509ExtensionBuilder builder = builderMocker.getComponentUnderTest();
        CertifiedPublicKey caCertificate = factory.getInstance(signerFactory.getInstance(true, BcX509CertificateGeneratorFactoryTest.rsaPrivateKey), new X509CertificateGenerationParameters(builder.addBasicConstraints(true).addKeyUsage(true, EnumSet.of(KeyUsage.keyCertSign, KeyUsage.cRLSign)).build())).generate(new DistinguishedName("CN=Test CA"), BcX509CertificateGeneratorFactoryTest.rsaPublicKey, new X509CertificateParameters());
        builder = builderMocker.getComponentUnderTest();
        CertificateGenerator generator = factory.getInstance(CertifyingSigner.getInstance(true, new CertifiedKeyPair(BcX509CertificateGeneratorFactoryTest.rsaPrivateKey, caCertificate), signerFactory), new X509CertificateGenerationParameters(builder.addKeyUsage(EnumSet.of(KeyUsage.digitalSignature, KeyUsage.dataEncipherment)).addExtendedKeyUsage(false, new ExtendedKeyUsages(new String[]{ ExtendedKeyUsages.EMAIL_PROTECTION })).build()));
        builder = builderMocker.getComponentUnderTest();
        CertifiedPublicKey certificate = generator.generate(new DistinguishedName("CN=Test End Entity"), BcX509CertificateGeneratorFactoryTest.dsaPublicKey, new X509CertificateParameters(builder.addSubjectAltName(false, new X509GeneralName[]{ new X509Rfc822Name("test@example.com"), new X509Rfc822Name(new InternetAddress("test@test.com")), new X509DnsName("example.com"), new X509DirectoryName("CN=Test"), new X509IpAddress("192.168.1.1"), new X509IpAddress("192.168.2.0/24"), new X509IpAddress("192.168.3.0/255.255.255.0"), new X509IpAddress(InetAddress.getByName("192.168.4.1")), new X509IpAddress(InetAddress.getByName("192.168.5.0"), InetAddress.getByName("255.255.255.0")), new X509IpAddress("2001:db8:0:85a3::ac1f:8001"), new X509IpAddress("2001:db8:1f89::/48"), new X509IpAddress(InetAddress.getByName("2001:db8:0:85a3::ac1f:8001")), new X509IpAddress(InetAddress.getByName("2001:db8:1f89::"), InetAddress.getByName("ffff:ffff:ffff::")), new X509URI("http://xwiki.org"), new X509URI(new URL("http://myxwiki.org")) }).build()));
        X509CertifiedPublicKey cert = checkRootSigned(certificate, 3);
        Assert.assertThat(cert.getExtensions().getExtensionOID(), IsEqual.equalTo(new String[]{ "2.5.29.35", "2.5.29.14", "2.5.29.15", "2.5.29.37", "2.5.29.17" }));
        Assert.assertThat(cert.getExtensions().getCriticalExtensionOID(), IsEqual.equalTo(new String[]{ "2.5.29.15" }));
        Assert.assertThat(cert.getExtensions().getNonCriticalExtensionOID(), IsEqual.equalTo(new String[]{ "2.5.29.35", "2.5.29.14", "2.5.29.37", "2.5.29.17" }));
        Assert.assertTrue("KeyUsage extension should be critical.", cert.getExtensions().isCritical(KeyUsage.OID));
        Assert.assertThat(cert.getExtensions().getKeyUsage(), IsEqual.equalTo(EnumSet.of(KeyUsage.digitalSignature, KeyUsage.dataEncipherment)));
        Assert.assertFalse("ExtendedKeyUsage extension should be non critical.", cert.getExtensions().isCritical(ExtendedKeyUsages.OID));
        Assert.assertThat(cert.getExtensions().getExtendedKeyUsage().getAll().toArray(new String[0]), IsEqual.equalTo(new String[]{ ExtendedKeyUsages.EMAIL_PROTECTION }));
        Assert.assertTrue("Email data protection extended usage should be set.", cert.getExtensions().getExtendedKeyUsage().hasUsage(ExtendedKeyUsages.EMAIL_PROTECTION));
        List<X509GeneralName> names = cert.getExtensions().getSubjectAltName();
        Assert.assertThat(names.size(), IsEqual.equalTo(15));
        for (X509GeneralName name : names) {
            if (name instanceof X509Rfc822Name) {
                Assert.assertThat(((X509StringGeneralName) (name)).getName(), AnyOf.anyOf(IsEqual.equalTo("test@example.com"), IsEqual.equalTo("test@test.com")));
                Assert.assertThat(((X509Rfc822Name) (name)).getAddress(), AnyOf.anyOf(IsEqual.equalTo(new InternetAddress("test@example.com")), IsEqual.equalTo(new InternetAddress("test@test.com"))));
            }else
                if (name instanceof X509DnsName) {
                    Assert.assertThat(((X509StringGeneralName) (name)).getName(), IsEqual.equalTo("example.com"));
                    Assert.assertThat(((X509DnsName) (name)).getDomain(), IsEqual.equalTo("example.com"));
                }else
                    if (name instanceof X509DirectoryName) {
                        Assert.assertThat(((X509StringGeneralName) (name)).getName(), IsEqual.equalTo("CN=Test"));
                    }else
                        if (name instanceof X509URI) {
                            Assert.assertThat(((X509StringGeneralName) (name)).getName(), AnyOf.anyOf(IsEqual.equalTo("http://xwiki.org"), IsEqual.equalTo("http://myxwiki.org")));
                            Assert.assertThat(((X509URI) (name)).getURI(), AnyOf.anyOf(IsEqual.equalTo(new URI("http://xwiki.org")), IsEqual.equalTo(new URI("http://myxwiki.org"))));
                            Assert.assertThat(((X509URI) (name)).getURL(), AnyOf.anyOf(IsEqual.equalTo(new URL("http://xwiki.org")), IsEqual.equalTo(new URL("http://myxwiki.org"))));
                        }else
                            if (name instanceof X509IpAddress) {
                                Assert.assertTrue(("Invalid IP address: " + (((X509StringGeneralName) (name)).getName())), ((IPAddress.isValid(((X509StringGeneralName) (name)).getName())) || (IPAddress.isValidWithNetMask(((X509StringGeneralName) (name)).getName()))));
                            }else {
                                Assert.fail("Unexpected SubjectAltName type.");
                            }




        }
    }

    @Test
    public void testGenerateIntermediateCertificateVersion3() throws Exception {
        X509ExtensionBuilder builder = builderMocker.getComponentUnderTest();
        CertifiedPublicKey caCertificate = factory.getInstance(signerFactory.getInstance(true, BcX509CertificateGeneratorFactoryTest.rsaPrivateKey), new X509CertificateGenerationParameters(builder.addBasicConstraints(true).addKeyUsage(true, EnumSet.of(KeyUsage.keyCertSign, KeyUsage.cRLSign)).build())).generate(new DistinguishedName("CN=Test CA"), BcX509CertificateGeneratorFactoryTest.rsaPublicKey, new X509CertificateParameters());
        X509CertifiedPublicKey caKey = ((X509CertifiedPublicKey) (caCertificate));
        builder = builderMocker.getComponentUnderTest();
        CertificateGenerator generator = factory.getInstance(CertifyingSigner.getInstance(true, new CertifiedKeyPair(BcX509CertificateGeneratorFactoryTest.rsaPrivateKey, caCertificate), signerFactory), new X509CertificateGenerationParameters(builder.addBasicConstraints(0).addKeyUsage(EnumSet.of(KeyUsage.keyCertSign, KeyUsage.cRLSign)).build()));
        CertifiedPublicKey interCAcert = generator.generate(new DistinguishedName("CN=Test Intermediate CA"), BcX509CertificateGeneratorFactoryTest.interCaDsaPublicKey, new X509CertificateParameters());
        Assert.assertTrue("Signature should match Root CA key.", interCAcert.isSignedBy(BcX509CertificateGeneratorFactoryTest.rsaPublicKey));
        Assert.assertThat(interCAcert.getIssuer(), IsEqual.equalTo(caCertificate.getSubject()));
        Assert.assertThat(interCAcert.getSubject(), IsEqual.equalTo(((PrincipalIndentifier) (new DistinguishedName("CN=Test Intermediate CA")))));
        Assert.assertThat(interCAcert, IsInstanceOf.instanceOf(X509CertifiedPublicKey.class));
        X509CertifiedPublicKey interCaKey = ((X509CertifiedPublicKey) (interCAcert));
        Assert.assertThat(interCaKey.getVersionNumber(), IsEqual.equalTo(3));
        Assert.assertTrue("Basic constraints should be critical.", interCaKey.getExtensions().isCritical(X509Extensions.BASIC_CONSTRAINTS_OID));
        Assert.assertTrue("Basic constraints should be set to CA.", interCaKey.getExtensions().hasCertificateAuthorityBasicConstraints());
        Assert.assertThat(interCaKey.getExtensions().getBasicConstraintsPathLen(), Matchers.equalTo(0));
        Assert.assertTrue("KeyUsage extension should be critical.", interCaKey.getExtensions().isCritical(KeyUsage.OID));
        Assert.assertThat(interCaKey.getExtensions().getKeyUsage(), IsEqual.equalTo(EnumSet.of(KeyUsage.keyCertSign, KeyUsage.cRLSign)));
        Assert.assertThat(interCaKey.getAuthorityKeyIdentifier(), IsEqual.equalTo(caKey.getSubjectKeyIdentifier()));
        builder = builderMocker.getComponentUnderTest();
        generator = factory.getInstance(CertifyingSigner.getInstance(true, new CertifiedKeyPair(BcX509CertificateGeneratorFactoryTest.interCaDsaPrivateKey, interCAcert), ((SignerFactory) (mocker.getInstance(SignerFactory.class, "DSAwithSHA1")))), new X509CertificateGenerationParameters(builder.addKeyUsage(EnumSet.of(KeyUsage.digitalSignature, KeyUsage.dataEncipherment)).addExtendedKeyUsage(false, new ExtendedKeyUsages(new String[]{ ExtendedKeyUsages.EMAIL_PROTECTION })).build()));
        builder = builderMocker.getComponentUnderTest();
        CertifiedPublicKey certificate = generator.generate(new DistinguishedName("CN=Test End Entity"), BcX509CertificateGeneratorFactoryTest.dsaPublicKey, new X509CertificateParameters(builder.addSubjectAltName(false, new X509GeneralName[]{ new X509Rfc822Name("test@example.com") }).build()));
        Assert.assertTrue("Signature should match intermediate CA key.", certificate.isSignedBy(BcX509CertificateGeneratorFactoryTest.interCaDsaPublicKey));
        Assert.assertThat(certificate.getIssuer(), IsEqual.equalTo(interCAcert.getSubject()));
        Assert.assertThat(certificate.getSubject(), IsEqual.equalTo(((PrincipalIndentifier) (new DistinguishedName("CN=Test End Entity")))));
        Assert.assertThat(certificate, IsInstanceOf.instanceOf(X509CertifiedPublicKey.class));
        X509CertifiedPublicKey key = ((X509CertifiedPublicKey) (certificate));
        Assert.assertThat(key.getAuthorityKeyIdentifier(), IsEqual.equalTo(interCaKey.getSubjectKeyIdentifier()));
    }
}

