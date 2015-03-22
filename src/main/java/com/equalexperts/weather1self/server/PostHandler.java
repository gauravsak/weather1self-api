package com.equalexperts.weather1self.server;

import com.equalexperts.weather1self.model.lib1self.Stream;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class PostHandler extends ContextHandler {

    public PostHandler() {
        setContextPath("/");
        setAllowNullPathInfo(true);
    }

    @Override
    public void doHandle(String target, Request baseRequest, HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        System.out.println("Inside POST handler");
        System.out.println("Weather Source : " + request.getHeader("x-weather-source"));
        response.setStatus(HttpStatus.OK_200);
        baseRequest.setHandled(true);

        String requestBody = getBody(request);
        System.out.println("Request body : " + requestBody);
        JSONObject streamJson = new JSONObject(requestBody);
        Stream stream = Stream.fromJSON(streamJson);

        PrintWriter out = response.getWriter();
        out.print("success : " + stream);
    }

    private static String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = request.getReader();
            char[] charBuffer = new char[128];
            int bytesRead = -1;
            while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                stringBuilder.append(charBuffer, 0, bytesRead);
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }
}