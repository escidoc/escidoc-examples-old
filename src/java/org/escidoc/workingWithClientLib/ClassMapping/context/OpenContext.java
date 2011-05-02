package org.escidoc.workingWithClientLib.ClassMapping.context;

import java.net.MalformedURLException;
import java.net.URL;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ContextHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.common.properties.PublicStatus;
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
		String objid = "escidoc:1";
		if (args.length > 0) {
			objid = args[0];
		}

		Context context = null;
		try {
			// retrieve an already created Context
			context = retrieveContext(objid);

			// set status to open
			context.getProperties().setPublicStatus(PublicStatus.OPENED);

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
		} catch (MalformedURLException e) {
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
	 * @throws MalformedURLException 
	 */
	private static Context retrieveContext(final String objid)
			throws EscidocException, InternalClientException,
			TransportException, MalformedURLException {
		
		// prepare client object
    	Authentication auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME, Constants.USER_PASSWORD);
    	ContextHandlerClient chc = new ContextHandlerClient(auth.getServiceAddress());
    	chc.setHandle(auth.getHandle());

		Context context = chc.retrieve(objid);

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
	 * @throws MalformedURLException 
	 */
	private static Result openContext(final Context context)
			throws EscidocException, InternalClientException,
			TransportException, MalformedURLException {

		// prepare client object
		Authentication auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME, Constants.USER_PASSWORD);
    	ContextHandlerClient chc = new ContextHandlerClient(auth.getServiceAddress());
    	chc.setHandle(auth.getHandle());
		
		TaskParam taskParam = new TaskParam();
		taskParam.setComment("Example to open Context");
		taskParam.setLastModificationDate(context.getLastModificationDate());

		Result result = chc.open(context.getObjid(), taskParam);

		return result;
	}
}
