package org.escidoc.workingWithClientLib.ClassMapping.crud;

import java.net.MalformedURLException;

import javax.xml.parsers.ParserConfigurationException;

import de.escidoc.core.client.exceptions.EscidocClientException;
import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

/**
 * @author SWA
 * 
 */
public class DemoAssignPid {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		DemoItem c = new DemoItem();

		try {
			c.assignPids();
			
		} catch (EscidocException e) {
			e.printStackTrace();
		} catch (InternalClientException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (EscidocClientException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

	}

}
