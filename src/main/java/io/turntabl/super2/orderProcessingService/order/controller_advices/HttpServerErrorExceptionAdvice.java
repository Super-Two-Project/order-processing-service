package io.turntabl.super2.orderProcessingService.order.controller_advices;

import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpServerErrorException;

@ControllerAdvice
public class HttpServerErrorExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(HttpServerErrorException.InternalServerError.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    Map<String, String> exchangeErrorHandler(HttpServerErrorException.InternalServerError ex) {
        // Get the json bit of the response
        var jsonBit = ex.getMessage().split(" : ")[1];

        // Strip the starting and ending quotes
        var rawJsonString = jsonBit.substring(1, jsonBit.length() - 1);

        // Get actual response message
        var message = new Gson().fromJson(rawJsonString, JsonObject.class).get("message").getAsString();

        return Map.of("message", message);
    }
}