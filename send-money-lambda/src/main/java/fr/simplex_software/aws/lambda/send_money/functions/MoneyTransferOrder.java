package fr.simplex_software.aws.lambda.send_money.functions;

import com.amazonaws.services.lambda.runtime.*;
import com.amazonaws.services.lambda.runtime.events.*;
import fr.simplex_software.aws.lambda.send_money.api.*;
import fr.simplex_software.aws.lambda.send_money.model.*;
import fr.simplex_software.aws.lambda.send_money.provider.*;
import jakarta.json.bind.*;

public class MoneyTransferOrder implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent>
{
  private static SendMoneyApi sendMoneyApi = new DefaultSendMoneyProvider();
  static final Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));

  @Override
  public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent, Context context)
  {
    LambdaLogger logger = context.getLogger();
    String body = apiGatewayProxyRequestEvent.getBody();
    logger.log("### MoneyTransferOrder.handleRequest(): We got an HTTP body: " + body);
    logger.log("### MoneyTransferOrder.handleRequest(): We got a PATH: " + apiGatewayProxyRequestEvent.getPath());
    logger.log("### MoneyTransferOrder.handleRequest(): We got an HTTP method: " + apiGatewayProxyRequestEvent.getHttpMethod());
    logger.log("### MoneyTransferOrder.handleRequest(): We got a resource: " + apiGatewayProxyRequestEvent.getResource());
    APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = new APIGatewayProxyResponseEvent();
    apiGatewayProxyResponseEvent.setStatusCode(200);
    if (apiGatewayProxyRequestEvent.getResource().equals("/orders/{ref}"))
      if (apiGatewayProxyRequestEvent.getHttpMethod().equals("GET"))
        apiGatewayProxyResponseEvent.setBody(jsonb.toJson(sendMoneyApi.getMoneyTransferOrder(apiGatewayProxyRequestEvent.getPathParameters().get("ref"))));
      else if (apiGatewayProxyRequestEvent.getHttpMethod().equals("DELETE"))
        sendMoneyApi.removeMoneyTransferOrder(apiGatewayProxyRequestEvent.getPathParameters().get("ref"));
      else
        logger.log("### MoneyTransferOrder.handleRequest(): An invalid HTTP request has been received " + apiGatewayProxyRequestEvent.getHttpMethod());
    else if (apiGatewayProxyRequestEvent.getHttpMethod().equals("GET"))
      apiGatewayProxyResponseEvent.setBody(jsonb.toJson(sendMoneyApi.getMoneyTransferOrders()));
    else if (apiGatewayProxyRequestEvent.getHttpMethod().equals("POST"))
      apiGatewayProxyResponseEvent.setBody(jsonb.toJson(sendMoneyApi.createMoneyTransferOrder(jsonb.fromJson(body, MoneyTransfer.class))));
    else if (apiGatewayProxyRequestEvent.getHttpMethod().equals("PUT"))
      apiGatewayProxyResponseEvent.setBody(jsonb.toJson(sendMoneyApi.updateMoneyTransferOrder(jsonb.fromJson(body, MoneyTransfer.class))));
    else if (apiGatewayProxyRequestEvent.getHttpMethod().equals(""))
      apiGatewayProxyResponseEvent.setBody(jsonb.toJson(sendMoneyApi.updateMoneyTransferOrder(jsonb.fromJson(body, MoneyTransfer.class))));
    else
      logger.log("### MoneyTransferOrder.handleRequest(): An invalid HTTP request has been received " + apiGatewayProxyRequestEvent.getHttpMethod());
    return apiGatewayProxyResponseEvent;
  }
}
