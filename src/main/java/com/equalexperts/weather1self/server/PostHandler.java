package com.equalexperts.weather1self.server;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.ContextHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PostHandler extends ContextHandler {

    public PostHandler() {
        setContextPath("/");
        setAllowNullPathInfo(true);
    }

    @Override
    public void doHandle(String target, Request baseRequest, HttpServletRequest request,
                         HttpServletResponse response) throws IOException, ServletException {
        System.out.println(request.getMethod());
        response.setStatus(HttpStatus.OK_200);
        baseRequest.setHandled(true);
    }
}