package kt.tax.declarations.pl;

import kt.utils.WebServiceUtil;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.gov.mf.edeklaracje.GateService;
import pl.gov.mf.edeklaracje.GateServicePortType;

import java.io.Closeable;
import java.io.IOException;
import java.net.URL;

public class Submitter {
    private static final Logger LOG = LoggerFactory.getLogger(Submitter.class);

    public static void main(String[] args) {
        LOG.info("Submitter started.");

        URL wsdlUrl = Validate.notNull(Submitter.class.getResource("/e-deklaracje/dokumenty.wsdl"));
        GateServicePortType gateServicePort = (new GateService(wsdlUrl)).getGateServiceSOAP12Port();
        try {
            LOG.info("GateServicePortType: {}", ClassUtils.getAllInterfaces(gateServicePort.getClass()));

            LOG.info("Endpoint: {}", WebServiceUtil.getEndpointAddress(gateServicePort));

            new EndpointCertApprover().isApproved(WebServiceUtil.getEndpointAddress(gateServicePort));

            //LOG.info("Certificate: {}", CertUtil.fetchCertificate(WebServiceUtil.getEndpointAddress(gateServicePort)));

        } finally {
            if (gateServicePort instanceof Closeable) {
                try {
                    ((Closeable)gateServicePort).close();
                } catch (IOException e) {
                    LOG.warn("Error when closing WS client", e);
                }
            }
        }
    }
}
