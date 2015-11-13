package com.example.rai.myapplication.backend;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.RetryOptions;
import com.google.appengine.api.taskqueue.TaskOptions;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by rai on 11/11/15.
 */
public class TaskRequestServlet extends HttpServlet{

    @Override
    public final void doGet(final HttpServletRequest req, final HttpServletResponse resp)
                throws ServletException, IOException {
            Queue queue = QueueFactory.getDefaultQueue();
            queue.add(TaskOptions.Builder.withUrl("/buildsearchindex").param("act", "refresh").
                    retryOptions(RetryOptions.Builder.withTaskRetryLimit(1)));
//        resp.getWriter().println("MaintenanceTasks completed");
//        return;
    }
}