/**
 * 
 */
package org.escidoc.workingWithClientLib.crud;

import javax.xml.parsers.ParserConfigurationException;

import de.escidoc.core.client.exceptions.EscidocException;
import de.escidoc.core.client.exceptions.InternalClientException;
import de.escidoc.core.client.exceptions.TransportException;

/**
 * @author SWA
 * 
 */
public class Crud {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Create c = new Create();

		try {
			c.createItem();

		} catch (EscidocException e) {
			e.printStackTrace();
		} catch (InternalClientException e) {
			e.printStackTrace();
		} catch (TransportException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

	}

}
