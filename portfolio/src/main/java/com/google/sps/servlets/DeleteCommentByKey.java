package com.google.sps.servlets;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.KeyFactory;
import java.io.BufferedReader;
import java.io.IOException;

@WebServlet("/delete-comment")
public class DeleteCommentByKey extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        String key =  null;
        try {
            BufferedReader reader = request.getReader();
            key = reader.readLine();
            datastore.delete(KeyFactory.stringToKey(key));
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }
    }

}