package org.example.rest;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.mime.MultipartEntityBuilder;


import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.example.adapters.CustomerAdapter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



public class RestAPI {

    public RestAPI() {

    }

    private ObservableList<CustomerAdapter> listOfCustomer;
    String JSON_URL = "http://127.0.0.1:3000/v1/customer/getAll";

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public void run() {
        executorService.submit(fetchList);
        fetchList.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent t) {
                listOfCustomer = FXCollections.observableArrayList(fetchList.getValue());
                //Remove Loading Image and add GridPane
                System.out.println(listOfCustomer);
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            postRequest("http://127.0.0.1:3000/v1/customer");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
    }

    private Task<List<CustomerAdapter>> fetchList = new Task() {
        @Override
        protected List<CustomerAdapter> call() throws Exception {
            List<CustomerAdapter> list = null;
            try {
                Gson gson = new Gson();
                System.out.println(readUrl(JSON_URL));
                list = new Gson().fromJson(readUrl(JSON_URL), new TypeToken<List<CustomerAdapter>>() {
                }.getType());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }
    };

    /**
     * Task to fetch images for individual ImageViews
     * @param <V>
     */
    private class FetchImage<V> extends Task<Image> {

        private String imageUrl;

        public FetchImage(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        @Override
        protected Image call() throws Exception {
            Image image = new Image(imageUrl);
            return image;
        }

    }

    /**
     * Read the URL and return the json data
     * @param urlString
     * @return
     * @throws Exception
     */
    private static String readUrl(String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    /**
     * Send a post request to a url with parameters
     * @param urlString
     * @return none
     */
    private static String postRequest(String urlString) throws Exception {
        CustomerAdapter order = new CustomerAdapter();

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost uploadFile = new HttpPost("http://127.0.0.1:3000/v1/customer");
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.addTextBody("name", "name", ContentType.TEXT_PLAIN);

// This attaches the file to the POST:
        File f = new File("/Users/mac/IdeaProjects/mvn-test/src/main/resources/org/example/img/home.png");
        builder.addBinaryBody(
                "image_url",
                new FileInputStream(f),
                ContentType.APPLICATION_OCTET_STREAM,
                f.getName()
        );

        HttpEntity multipart = builder.build();
        uploadFile.setEntity(multipart);
        CloseableHttpResponse response = httpClient.execute(uploadFile);
        HttpEntity responseEntity = response.getEntity();
        System.out.println(responseEntity);

        return null;
    }
}
