package org.escidoc.workingWithClientLib.ClassMapping.context;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.ContextHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.om.context.Context;

/**
 * Example how to open an Context by using the eSciDoc Java client library.
 * 
 * A Context could only be used if it is in status open.
 * 
 * @author SWA
 * 
 */
public class OpenContext {

	public static void main(String[] args) {

		// set objid of Context which is to open
		String objid = "escidoc:3980";
		if (args.length > 0) {
			objid = args[0];
		}

		Context context = null;
		try {
			// retrieve an already created Context
			context = retrieveContext(objid);

			// set status to open
			context.getProperties().setPublicStatus("opened");

			// open Context
			Result result = openContext(context);

			// for convenient reason: print out objid and last-modification-date
			// of opened context
			System.out.println("Context with objid='" + context.getObjid()
					+ "' at '" + result.getLastModificationDate()
					+ "' is opened now.");

		} catch (EscidocException e) {
			e.printStackTrace();
		} catch (InternalClientException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (EscidocClientException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Retrieve Context from infrastructure.
	 * 
	 * @param objid
	 *            The objid of the Context
	 * @return Context
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
	private static Context retrieveContext(final String objid)
			throws EscidocException, InternalClientException,
			TransportException {

		ContextHandlerClient client = new ContextHandlerClient();
		client.login(Util.getInfrastructureURL(), Constants.SYSTEM_ADMIN_USER,
				Constants.SYSTEM_ADMIN_PASSWORD);

		Context context = client.retrieve(objid);

		return context;
	}

	/**
	 * Update Context at infrastructure.
	 * 
	 * @param context
	 *            The to update Context
	 * @return Result
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
	private static Result openContext(final Context context)
			throws EscidocException, InternalClientException,
			TransportException {

		ContextHandlerClient client = new ContextHandlerClient();
		client.login(Util.getInfrastructureURL(), Constants.SYSTEM_ADMIN_USER,
				Constants.SYSTEM_ADMIN_PASSWORD);

		TaskParam taskParam = new TaskParam();
		taskParam.setComment("Example to open Context");
		taskParam.setLastModificationDate(context.getLastModificationDate());

		Result result = client.open(context.getObjid(), taskParam);

		return result;
	}

}
