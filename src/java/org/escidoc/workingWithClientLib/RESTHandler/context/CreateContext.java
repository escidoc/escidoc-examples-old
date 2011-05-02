package org.escidoc.workingWithClientLib.RESTHandler.context;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.rest.RestContextHandlerClient;

/**
 * Example how to handle a Context by using the XML REST representation and the
 * eSciDoc Java client library.
 * 
 * The eSciDoc Java client library is used for communication with framework.
 * Unused is mapping between Java classes and XML representations to explain the
 * XML data structure.
 * 
 * eSciDoc XML REST representation is used. Please keep that in mind, if you
 * adapt these examples for SOAP.
 * 
 * @author SWA
 * 
 */
public class CreateContext {

    /**
     * Create Context.
     * 
     * @param args
     *            If args[0] is given, than is it taken as filename for the
     *            resource template, otherwise default template is used
     */
    public static void main(String[] args) {

        // select template
        String xmlFile = "templates/generic/context/Context_create.xml";
        if (args.length > 0) {
            xmlFile = args[0];
        }

        try {
            String createdResource =
                createContext(Util.getXmlFileAsString(xmlFile));

            /*
             * objid of created Context and the last modification date is
             * printed to stdout.
             */
            String[] objidLmd = Util.obtainObjidAndLmd(createdResource);
            System.out.println("Context with objid='" + objidLmd[0] + "' at '"
                + objidLmd[1] + "' created.");

        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            System.out
                .println("First parameter must be an eSciDoc Item XML File.");
        }
    }

    /**
     * Create Context.
     * 
     * <p>
     * This method makes usage of a Context XML (REST) template.<br/>
     * </p>
     * @throws MalformedURLException 
     * 
     * @throws InternalClientException
     * @throws EscidocException
     * @throws TransportException
     * @throws IOException
     */
    public static String createContext(final String contextXml)
        throws EscidocClientException, MalformedURLException {

//        // get the REST handler
        Authentication auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME, Constants.USER_PASSWORD);
        RestContextHandlerClient client = new RestContextHandlerClient(auth.getServiceAddress());
        client.setHandle(auth.getHandle());

        // create
        String createdContext = client.create(contextXml);

        return createdContext;
    }

}
