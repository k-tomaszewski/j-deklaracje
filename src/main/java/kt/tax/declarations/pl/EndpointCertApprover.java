package kt.tax.declarations.pl;

import kt.utils.CertUtil;

import java.net.URL;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public class EndpointCertApprover implements EndpointApprover {

    @Override
    public boolean isApproved(URL endpointUrl) {
        final X509Certificate cert = CertUtil.fetchCertificate(endpointUrl);

        System.out.format("\nDO YOU ACCEPT FOLLOWING CERTIFICATE FROM [%s]?\n\n", endpointUrl);
        if (cert == null) {
            System.out.println(" No certificate.");
        } else {
            toNameValuePairs(cert).forEach(entry -> System.out.format(" %-18s %s\n", entry.getKey() + ':', entry.getValue()));
        }
        System.out.format("\nPlease enter YES only if you accept: ");
        return "YES".equalsIgnoreCase(new Scanner(System.in).nextLine());
    }

    private static Stream<Map.Entry<String, String>> toNameValuePairs(X509Certificate cert) {
        return Stream.of(
                toPair("Subject", cert.getSubjectX500Principal()),
                toPair("Subject alt names", getSubjectAltNames(cert)),
                toPair("Issuer", cert.getIssuerX500Principal()),
                toPair("Issuer alt names", getIssuerAltNames(cert)),
                toPair("Serial number", cert.getSerialNumber()),
                toPair("Version", cert.getVersion()),
                toPair("Signature alg", cert.getSigAlgName()),
                toPair("Start date", cert.getNotBefore()),
                toPair("End date", cert.getNotAfter()),
                toPair("Validity", verify(cert))
        ).filter(pair -> pair.getValue() != null);
    }

    private static String verify(X509Certificate cert) {
        try {
            cert.checkValidity();
            return "OK";
        } catch (Exception e) {
            return String.format("INVALID!!! %s", e.getMessage());
        }
    }

    private static Map.Entry<String, String> toPair(String a, Object b) {
        return new AbstractMap.SimpleImmutableEntry<>(a, (b != null) ? b.toString() : null);
    }

    private static Collection<List<?>> getSubjectAltNames(X509Certificate cert) {
        try {
            Collection<List<?>> altNames = cert.getSubjectAlternativeNames();
            return (altNames != null && altNames.isEmpty()) ? null : altNames;
        } catch (CertificateParsingException e) {
            return null;
        }
    }

    private static Collection<List<?>> getIssuerAltNames(X509Certificate cert) {
        try {
            Collection<List<?>> altNames = cert.getIssuerAlternativeNames();
            return (altNames != null && altNames.isEmpty()) ? null : altNames;
        } catch (CertificateParsingException e) {
            return null;
        }
    }
}
