package org.escidoc.workingWithClientLib.RESTHandler.container;

import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestContainerHandlerClient;

/**
 * Example how to handle a Container by using the XML REST representation and
 * the eSciDoc Java client library.
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
public class CreateContainer {
    /**
     * Create Container.
     * 
     * @param args
     *            If args[0] is given, than is it taken as filename for the
     *            resource template, otherwise default template is used
     */
    public static void main(String[] args) {

        String xmlFile =
            "templates/generic/container/Container_create_minimal.xml";
        if (args.length > 0) {
            xmlFile = args[0];
        }

        try {

            String createdResource =
                createContainer(Util.getXmlFileAsString(xmlFile));

            String[] objidLmd = Util.obtainObjidAndLmd(createdResource);
            System.out.println("Container with objid='" + objidLmd[0]
                + "' at '" + objidLmd[1] + "' created.");

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
     * Create Container.
     * 
     * @param containerXml
     *            Container XML
     * @return XML representation of the created Container
     * @throws EscidocClientException
     */
    private static String createContainer(final String containerXml)
        throws EscidocClientException {

        // get REST handler
        RestContainerHandlerClient rchc = new RestContainerHandlerClient();

        // login
        rchc.login(Util.getInfrastructureURL(), Constants.USER_NAME,
            Constants.USER_PASSWORD);

        // create the Container
        String createdContainer = rchc.create(containerXml);

        return createdContainer;
    }

}
