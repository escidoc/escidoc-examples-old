package org.escidoc.workingWithClientLib.RESTHandler.ou;

import java.io.IOException;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.rest.RestOrganizationalUnitHandlerClient;
import de.escidoc.core.test.client.EscidocClientTestBase;

/**
 * Example how to create an Organizational Unit by using the XML REST
 * representation and the eSciDoc Java client library.
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
public class CreateOrganizationalUnit {

    /**
     * Create Organizational Unit.
     * 
     * @param args
     *            If args[0] is given, than is it taken as filename for the
     *            resource template, otherwise default template is used.
     */
    public static void main(String[] args) {

        // select template
        String xmlFile =
            "./templates/generic/organizational-unit/"
                + "escidoc_ou_for_create.xml";
        if (args.length > 0) {
            xmlFile = args[0];
        }

        try {
            String createdResource = create(Util.getXmlFileAsString(xmlFile));

            /*
             * objid of created Context and the last modification date is
             * printed to stdout.
             */
            String[] objidLmd = Util.obtainObjidAndLmd(createdResource);
            System.out.println("Organizational Unit with objid='" + objidLmd[0]
                + "' at '" + objidLmd[1] + "' created");
        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Create Organizational Unit.
     * 
     * <p>
     * This method makes usage of a Content Relation XML (REST) template.<br/>
     * objid of created Content Relation and the last modification date is
     * finally printed to stdout.
     * </p>
     * 
     * @throws IOException
     * @throws EscidocClientException
     */
    private static String create(final String ouXml) throws IOException,
        EscidocClientException {

        // authentication (Use a user account with permission to create an
        // Organizational Unit).
        Authentication auth =
            new Authentication(EscidocClientTestBase.DEFAULT_SERVICE_URL,
                Constants.USER_NAME, Constants.USER_PASSWORD);

        // get rest handler
        RestOrganizationalUnitHandlerClient rouc =
            new RestOrganizationalUnitHandlerClient();
        // login
        rouc.setServiceAddress(auth.getServiceAddress());
        rouc.setHandle(auth.getHandle());

        // create
        String crtdOuXML = rouc.create(ouXml);

        return crtdOuXML;
    }

}
