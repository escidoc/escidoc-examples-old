package org.escidoc.workingWithClientLib.ClassMapping.container;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.ContainerHandlerClient;
import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.resources.common.Filter;
import de.escidoc.core.resources.common.Result;
import de.escidoc.core.resources.common.TaskParam;
import de.escidoc.core.resources.om.container.Container;

/**
 * Example how to release an Container.
 * 
 * @author SWA
 * 
 */
public class SubmitContainer {

	/**
	 * 
	 * @param args
	 *            The objid of the to release Container.
	 */
	public static void main(String[] args) {

		try {

			/*
			 * Either set the objid of the Container by parameter or set the objid in
			 * the following id variable.
			 */
			String id = "escidoc:7005";
			if (args.length > 0) {
				id = args[0];
			}

			submitContainer(id);

		} catch (EscidocClientException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Submit a Container.
	 * 
	 * <p>
	 * Submit an Container is a task oriented method and needs and additional
	 * parameter instead of the Container representation.
	 * </p>
	 * <p>
	 * The taskParam has to contain the last-modification-date of the Container
	 * (optimistic locking) and the release comment.
	 * </p>
	 * 
	 * @param objid
	 *            The objid of the Container.
	 * @throws EscidocClientException
	 * @throws MalformedURLException 
	 */
	public static void submitContainer(final String objid)
			throws EscidocClientException, MalformedURLException {

		// prepare client object
		Authentication auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL), Constants.USER_NAME, Constants.USER_PASSWORD);
		ContainerHandlerClient chc = new ContainerHandlerClient(auth.getServiceAddress());
		chc.setHandle(auth.getHandle());

		// retrieve the Container to detect last modification date
		Container container = chc.retrieve(objid);

		// submit container
		TaskParam taskParam = new TaskParam();
		taskParam.setLastModificationDate(container.getLastModificationDate());
		taskParam.setComment("Comment for release");
		taskParam.setFilters(new Vector<Filter>());
		Result submitResult = chc.submit(container, taskParam);
		
		System.out.println("Container with objid='" + objid + "' at '"
				+ submitResult.getLastModificationDate()
				+ "' submitted.");
	}
}
