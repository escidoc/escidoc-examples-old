package org.escidoc.workingWithClientLib.RESTHandler.ou;

import java.net.MalformedURLException;
import java.net.URL;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.rest.RestOrganizationalUnitHandlerClient;

/**
 * Example how to set status of a Organizational Unit to to 'open' by using the
 * XML REST representation and the eSciDoc Java client library.
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
public class OpenOU {

    /**
     * @param args
     *            If args[0] is given, it is used as objid for Organizational
     *            Unit. Otherwise is escidoc:1 used.
     */
    public static void main(String[] args) {

        /*
         * Either set the objid of the OU by parameter or set the objid in the
         * following id variable.
         */
        String id = "escidoc:organizational-unit";
        if (args.length > 0) {
            id = args[0];
        }

        try {
            String responseXml = openOU(id);

            // write out objid and last modification date
            String[] objidLmd = Util.obtainObjidAndLmd(responseXml);
            System.out.println("Organizational Unit with objid='" + id
                + "' at '" + objidLmd[1] + "' set to open");
        }
        catch (EscidocClientException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
			e.printStackTrace();
		}
    }

    /**
     * Set Organizational Unit to open.
     * 
     * @param objd
     *            The objid of the Organizational Unit
     * @param lmd
     *            last modification date of Organizational Unit
     * @throws InternalClientException
     * @throws EscidocException
     * @throws TransportException
     * @throws MalformedURLException 
     */
    private static String openOU(final String objid)
        throws InternalClientException, EscidocException, TransportException, MalformedURLException {

    	// prepare client object
        Authentication auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME, Constants.USER_PASSWORD);
        RestOrganizationalUnitHandlerClient rouc = new RestOrganizationalUnitHandlerClient(auth.getServiceAddress());
        rouc.setHandle(auth.getHandle());

        /*
         * Prepare and load taskParam XML.
         * 
         * You have to add the last-modification-date of the Organizational Unit
         * which is to set in status open into the taskParam XML template.
         */
        // retrieving the OU
        String ouXml = rouc.retrieve(objid);

        // we need last-modification-date for open
        String[] objidLmd = Util.obtainObjidAndLmd(ouXml);
        String taskParam =
            "<param last-modification-date=\"" + objidLmd[1] + "\">\n"
                + "<comment>Open OU example</comment>\n" + "</param>";

        // open OU
        String responseXml = rouc.open(objid, taskParam);

        return responseXml;
    }

}
