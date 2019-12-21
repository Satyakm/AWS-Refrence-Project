package com.serverlesseducation.handler;

import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverlesseducation.ApiGatewayResponse;
import com.serverlesseducation.Response;
import com.serverlesseducation.dao.User;

public class UpdateUserHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(UpdateUserHandler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

	    try {
	    	LOG.info(input);
	        // get the 'pathParameters' from input
	        @SuppressWarnings("unchecked")
			Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
	        String userID = pathParameters.get("id");
	        // get the 'body' from input
	        JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
	        LOG.info("log body:"+body.asText());
	          // create the User object for post
	        // update the User by id
	        User user = new User();
	        user.setName(body.get("name").asText());
	        user.setProcessStatus(body.get("processStatus").asText());
	        user.update(userID,user);
	        Boolean success = new User().update(userID,user);

	        // send the response back
	        if (success) {
	          return ApiGatewayResponse.builder()
	      				.setStatusCode(204)
	      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
	      				.build();
	        } else {
	          return ApiGatewayResponse.builder()
	      				.setStatusCode(404)
	      				.setObjectBody("User with id: '" + userID + "' not found.")
	      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
	      				.build();
	        }
	    } catch (Exception ex) {
	        LOG.error("Error in deleting user: " + ex);

	        // send the error response back
	  			Response responseBody = new Response("Error in updating user: ", input);
	  			return ApiGatewayResponse.builder()
	  					.setStatusCode(500)
	  					.setObjectBody(responseBody)
	  					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
	  					.build();
	    }
		}
}
