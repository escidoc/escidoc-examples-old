package org.escidoc.workingWithClientLib.ClassMapping.staging;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.escidoc.Constants;

import de.escidoc.core.client.Authentication;
import de.escidoc.core.client.StagingHandlerClient;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;
import de.escidoc.core.client.exceptions.application.security.AuthenticationException;
import de.escidoc.core.client.interfaces.StagingHandlerClientInterface;

/**
 * Example for file uploading to staging area.
 * 
 * @author JHE
 * 
 */
public class Staging {

	/**
	 * Example how to upload file to staging service.
	 * 
	 * @param args
	 *            optional path to upload file
	 */
	public static void main(String[] args) {

		// file for upload
		String filename = "templates/generic/img/escidoc-logo.jpg";

		if (args.length == 1) {
			filename = args[0];
		}

		File file = new File(filename);
		System.out.println("file: " + filename + " has size of "
				+ file.length() + " bytes");

		Authentication auth = null;

		try {
			auth = new Authentication(new URL(Constants.DEFAULT_SERVICE_URL),
					Constants.USER_NAME_DEPOSITOR,
					Constants.USER_PASSWORD_DEPOSITOR);
		} catch (AuthenticationException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		// get staging handler client
		StagingHandlerClientInterface sthc = new StagingHandlerClient(
				auth.getServiceAddress());
		try {
			sthc.setHandle(auth.getHandle());
		} catch (InternalClientException e) {
			e.printStackTrace();
		}

		URL url = null;

		try {
			// upload file and get retrieval url from staging area
			url = sthc.upload(file);
		} catch (EscidocException e) {
			e.printStackTrace();
		} catch (InternalClientException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		}

		System.out.println("file uploaded,\nURL for retrieval is:\n"
				+ url.toString());

		// use this URL as reference in Component to store binary content in
		// eSciDoc
		// Be aware that files from staging service could only be accessed
		// within a small time window and only one time.
	}
}
