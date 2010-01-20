package org.escidoc.simpleConnections.item;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.escidoc.Constants;
import org.escidoc.simpleConnections.Util;

import de.escidoc.core.client.exceptions.application.security.AuthenticationException;

public class UploadBinaryContent {

    private static final String STAGING_UPLOAD_PATH = "/st/staging-file";

    /**
     * @param args
     */
    public static void main(String[] args) {

        String url =
            stageIt("templates/TUE/content/Beispiel schwäbischer Dialektdaten.xml");

        System.out.println(url);
    }

    private static String stageIt(final String path) {

        StringBuffer result = new StringBuffer();

        try {

            URL createUrl =
                new URL(Util.getInfrastructureURL() + STAGING_UPLOAD_PATH);
            HttpURLConnection uploadConnection =
                (HttpURLConnection) createUrl.openConnection();

            uploadConnection.setRequestProperty("Cookie", "escidocCookie="
                + Util.getAuthHandle(Constants.USER_NAME,
                    Constants.USER_PASSWORD));

            uploadConnection.setRequestMethod("PUT");
            uploadConnection.setDoOutput(true);

            // open POST Request
            OutputStream out = uploadConnection.getOutputStream();
            // access binary content
            InputStream in = new FileInputStream(path);
            // write template to POST Request
            byte[] bytes = new byte[4096];
            int l = in.read(bytes);
            while (l > -1) {
                out.write(bytes, 0, l);
                l = in.read(bytes);
            }
            in.close();
            out.close();
            uploadConnection.connect();

            // connect response reader
            BufferedReader createdReader = null;
            String contentEncoding = uploadConnection.getContentEncoding();
            if (contentEncoding == null) {
                contentEncoding = Util.DEFAULT_CONTENT_ENCODING;
            }
            try {
                createdReader =
                    new BufferedReader(new InputStreamReader(uploadConnection
                        .getInputStream(), contentEncoding));
            }
            catch (Exception e) {
                // write error xml to std.err
                e.printStackTrace();
                BufferedReader r =
                    new BufferedReader(new InputStreamReader(uploadConnection
                        .getErrorStream(), Util.DEFAULT_CONTENT_ENCODING));
                String line = r.readLine();
                while (line != null) {
                    System.err.println(line);
                    line = r.readLine();
                }
                throw new RuntimeException("Uploading content failed.");
            }

            // read response
            String line = createdReader.readLine();
            while (line != null) {
                result.append(line);
                result.append(Util.LINE_SEPARATOR);
                line = createdReader.readLine();
            }

            createdReader.close();

        }
        catch (AuthenticationException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return Util.obtainResourceHref(result.toString());
    }
}
