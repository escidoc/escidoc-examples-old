package org.escidoc.workingWithClientLib.ClassMapping.context;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.ContextHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.om.context.Context;
import de.escidoc.core.resources.om.context.Properties;

/**
 * Example how to create an Context by using the eSciDoc Java client library.
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
		} catch (EscidocException e) {
			e.printStackTrace();
		} catch (InternalClientException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		}

		// for convenient reason: print out objid and last-modification-date of
		// created context
		System.out.println("Context with objid='" + context.getObjid() + "' at '"
				+ context.getLastModificationDate() + "' created.");
		
	}

	/**
	 * The value object Context is to create and to fill with (at least
	 * required) parameter.
	 * 
	 * @return Context (which is not created within the infrastructure).
	 */
	private static Context prepareContext() {

		Context context = new Context();

		Properties properties = new Properties();

		// Context requires a name
		properties.setName("Example_Package_Context");

		// description is nice
		properties.setDescription("Example package Context.");

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
	 */
	private static Context createContext(final Context context)
			throws EscidocException, InternalClientException,
			TransportException {

		ContextHandlerClient client = new ContextHandlerClient();
		client.login(Util.getInfrastructureURL(), Constants.SYSTEM_ADMIN_USER,
				Constants.SYSTEM_ADMIN_PASSWORD);

		Context createdContext = client.create(context);

		return createdContext;
	}
}
