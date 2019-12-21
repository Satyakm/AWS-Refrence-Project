package com.serverlesseducation.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Collections;
import java.util.Map;

import com.serverlesseducation.ApiGatewayResponse;
import com.serverlesseducation.Response;
import com.serverlesseducation.dao.User;


public class CreateUserHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private final Logger LOG =  LogManager.getLogger(CreateUserHandler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

      try {
          // get the 'body' from input
          JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
          LOG.info("log body:"+body.asText());
          // create the User object for post
          User user = new User();
          user.setId(body.get("id").asText());
          user.setName(body.get("name").asText());
          user.setProcessStatus(body.get("processStatus").asText());
          user.save(user);

          // send the response back
      		return ApiGatewayResponse.builder()
      				.setStatusCode(200)
      				.setObjectBody(user)
      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
      				.build();

      } catch (Exception ex) {
          LOG.error("Error in saving user: " + ex);

          // send the error response back
    			Response responseBody = new Response("Error in saving user: ", input);
    			return ApiGatewayResponse.builder()
    					.setStatusCode(500)
    					.setObjectBody(responseBody)
    					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
    					.build();
      }
	}
}
