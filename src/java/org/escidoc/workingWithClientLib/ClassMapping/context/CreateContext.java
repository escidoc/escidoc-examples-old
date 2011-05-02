package org.escidoc.workingWithClientLib.ClassMapping.context;

import java.net.MalformedURLException;
import java.net.URL;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ContextHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.common.reference.OrganizationalUnitRef;
import de.escidoc.core.resources.om.context.Context;
import de.escidoc.core.resources.om.context.ContextProperties;
import de.escidoc.core.resources.om.context.OrganizationalUnitRefs;

/**
 * Example how to create an Context by using the eSciDoc Java client library.
 * 
 * At least one Organizational Unit (OU) has to exists. The referenze of the OU
 * is to define as reference (see prepareContext method).
 * 
 * @author SWA
 * 
 */
public class CreateContext {

    public static void main(String[] args) {

        // Prepare a value object with new values of Context.
        Context context = prepareContext();

        try {
            // call create with VO on eSciDoc
            context = createContext(context);

            System.out.println("Context with objid='" + context.getObjid()
                + "' at '" + context.getLastModificationDate()
                + "' created.");
        }
        catch (EscidocException e) {
            e.printStackTrace();
        }
        catch (InternalClientException e) {
            e.printStackTrace();
        }
        catch (TransportException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
			e.printStackTrace();
		}
    }

    /**
     * The value object Context is to create and to fill with (at least
     * required) parameter.
     * 
     * @return Context (which is not created within the infrastructure).
     */
    private static Context prepareContext() {

        Context context = new Context();

        ContextProperties properties = new ContextProperties();

        // Context requires a name
        properties.setName("Example_Package_Context");

        // description is nice
        properties.setDescription("Example package Context.");

        // define the type
        properties.setType("ExampleType");

        /*
         * Organizational Unit(s) is/are required
         */
        OrganizationalUnitRefs ous = new OrganizationalUnitRefs();

        // add the Organizational Unit with objid escidoc:ex3 (the ou of the
        // example eSciDoc representation package) to the list of
        // organizational Units
        ous.add(new OrganizationalUnitRef("escidoc:ex3"));
        properties.setOrganizationalUnitRefs(ous);

        context.setProperties(properties);

        return context;
    }

    /**
     * Creating the Context within the eSciDoc infrastructure. The value object
     * Context is send to the create method of the infrastructure. The
     * infrastructure delivers the the created Context as response. The created
     * Context is enriched with values from the infrastructure.
     * 
     * @param context
     *            The value object of a Context.
     * @return Value Object of created Context (enriched with values by
     *         infrastructure)
     * 
     * @throws EscidocException
     *             Thrown if eSciDoc infrastructure throws an exception. This
     *             happens mostly if data structure is incomplete for the
     *             required operation, method is not allowed in object status or
     *             permissions are restricted.
     * @throws InternalClientException
     *             These are thrown if client library internal failure occur.
     * @throws TransportException
     *             Is thrown if transport between client library and framework
     *             is malfunctioned.
     * @throws MalformedURLException 
     */
    private static Context createContext(final Context context)
        throws EscidocException, InternalClientException, TransportException, MalformedURLException {
    	
    	// prepare client object
    	Authentication auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME, Constants.USER_PASSWORD);
    	ContextHandlerClient chc = new ContextHandlerClient(auth.getServiceAddress());
    	chc.setHandle(auth.getHandle());

        Context createdContext = chc.create(context);

        return createdContext;
    }
}
