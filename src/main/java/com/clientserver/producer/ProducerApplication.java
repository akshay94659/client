package com.clientserver.producer;

import com.google.gson.JsonObject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) throws URISyntaxException {
        SpringApplication.run(ProducerApplication.class, args);
        Scanner sc = new Scanner(System.in);

        System.out.println("Please Enter the Value of Workload RFW ID:");
        int rfwId = sc.nextInt();

        sc.nextLine();

        System.out.print("Please Enter one of the Bench Type");
        String benchType = sc.nextLine();

        System.out.println("Please choose one of the metrics");
        String metric = sc.nextLine();

        System.out.println("Please Enter the Batch Id (from which batch you want the data to start from) in integer:");
        int batchID = sc.nextInt();

        System.out.println("Please Enter the number of samples you want in one batch in integer: ");
        int batchUnit = sc.nextInt();

        System.out.println("Please Enter the number of batches to be returned in integer: ");
        int batch_size = sc.nextInt();

        JsonObject obj=new JsonObject();
        obj.addProperty("id",rfwId);
        obj.addProperty("benchType",benchType);
        obj.addProperty("metric",metric);
        obj.addProperty("batchID",batchID);
        obj.addProperty("batchUnit",batchUnit);
        obj.addProperty("batchsize",batch_size);

        WebClient client = WebClient.create();
        MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();
        bodyValues.add("id", String.valueOf(rfwId));
        bodyValues.add("benchType",benchType);
        bodyValues.add("metric",metric);
        bodyValues.add("batchID", String.valueOf(batchID));
        bodyValues.add("batchUnit", String.valueOf(batchUnit));
        bodyValues.add("batchsize", String.valueOf(batch_size));
        ResponseEntity responseBody = client.post()
            .uri(new URI("http://localhost:8080/workload/rfw"))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromFormData(bodyValues))
            .retrieve()
            .toEntity(JsonObject.class)
            .block();
        System.out.println(responseBody);
        if (responseBody.getStatusCode().value() == HttpStatus.CREATED.value()) {
            System.out.println(responseBody.getBody());

        }
    }

}


