package com.hdpros.hdprosbackend.utils.http.implementaion;

import com.hdpros.hdprosbackend.utils.http.HttpService;
import kong.unirest.GetRequest;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

@Service
public class HttpImplementation implements HttpService {

    private static final Logger logger = LoggerFactory.getLogger(HttpImplementation.class);

    @Override
    public HttpResponse<JsonNode> post(Map<String, String> headerList, String jsonPayload, String url) {
        logger.info("Making POST request with header {}, jsonPayload {} and url {}", headerList, jsonPayload, url);
        Unirest.config().verifySsl(false);
        return Unirest.post(url)
                .headers(headerList)
                .body(jsonPayload)
                .asJson();
    }

    @Override
    public HttpResponse<JsonNode> get(Map<String, String> headerList, Map<String, Object> params, String url) {
        logger.info("Making GET request with header {}, params {} and url {}", headerList, params, url);
        GetRequest getRequest = Unirest.get(url).headers(headerList);
        if (Objects.isNull(params)) {
            return getRequest.asJson();
        } else {
            return getRequest.queryString(params).asJson();
        }
    }


}
