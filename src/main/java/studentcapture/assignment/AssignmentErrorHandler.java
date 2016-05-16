package studentcapture.assignment;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.*;
import java.util.stream.Collectors;

/**
 * Error handler for extracting exception messages from the database server.
 * Created by David Björkstrand on 5/11/16.
 */
public class AssignmentErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return (clientHttpResponse.getStatusCode() != HttpStatus.OK);
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        String body = getResponseBody(clientHttpResponse);
        String errorMessage = getErrorMessage(body);
        throw new IOException(errorMessage);
    }

    private String getResponseBody(ClientHttpResponse clientHttpResponse) throws IOException {
        InputStream inputStream = clientHttpResponse.getBody();
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream))) {
            return buffer.lines().collect(Collectors.joining("\n"));
        }
    }

    private String getErrorMessage(String body) throws IOException {
        try {
            JSONObject jsonObject = new JSONObject(body);
            String exception = jsonObject.getString("exception");
            String message = jsonObject.getString("message");

            return exception + ": " + message;
        } catch (JSONException e) {
            System.err.println(e.getMessage() + ", in class AssignmentErrorHandler");
        }

        return "";
    }
}
