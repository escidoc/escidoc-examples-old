package org.escidoc.workingWithClientLib.RESTHandler.ou;

import java.io.File;
import java.io.IOException;

import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

public class Open {

    /**
     * @param args
     */
    public static void main(String[] args) {

        OrganizationalUnitEx ouEx = new OrganizationalUnitEx();

        try {
        	String objid = "escidoc:1124785";

        	// prepare and load taskParam XML
    		File templ = new File("./templates/TUE/taskParam.xml");
    		String taskParam = Util.getXmlFileAsString(templ);

            ouEx.open(objid, taskParam);
        }
        catch (EscidocException e) {
            e.printStackTrace();
        }
        catch (InternalClientException e) {
            e.printStackTrace();
        }
        catch (TransportException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

}
