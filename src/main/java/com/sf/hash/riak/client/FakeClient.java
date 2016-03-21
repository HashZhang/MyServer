package com.sf.hash.riak.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Created by 862911 on 2016/3/17.
 */
public class FakeClient {
    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            String url = "http://10.202.7.25:10018/riak/user/"+i;
            Client client = ClientBuilder.newClient();
            WebTarget target = client.target(url);
//            Response response = target.request().post(Entity.entity(""+i, MediaType.APPLICATION_JSON_TYPE));
            Response response = target.request().get();
            try {
                System.out.println("Successfully got result: " + response.getStatus() +response.readEntity(String.class));
            } finally {
                response.close();
                client.close();
            }
        }

    }
}
