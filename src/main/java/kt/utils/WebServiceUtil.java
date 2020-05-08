package kt.utils;

import javax.xml.ws.BindingProvider;
import java.net.MalformedURLException;
import java.net.URL;

public class WebServiceUtil {

    public static URL getEndpointAddress(Object wsClient) {
        if (wsClient instanceof BindingProvider) {
            BindingProvider bindingProvider = (BindingProvider) wsClient;
            Object endpointAddr = bindingProvider.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY);
            if (endpointAddr instanceof URL) {
                return (URL) endpointAddr;
            }
            if (endpointAddr != null) {
                try {
                    return new URL(endpointAddr.toString());
                } catch (MalformedURLException e) {
                    throw new RuntimeException("WS client object is javax.xml.ws.BindingProvider but provides invalid endpoint URL", e);
                }
            }
        }
        return null;
    }
}
