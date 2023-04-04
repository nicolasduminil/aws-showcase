package fr.simplex_software.aws.lambda.send_money.functions.tests;

import com.amazonaws.services.lambda.runtime.*;
import com.amazonaws.services.lambda.runtime.events.*;
import fr.simplex_software.aws.lambda.send_money.functions.*;
import fr.simplex_software.aws.lambda.send_money.model.*;
import jakarta.json.bind.*;
import org.junit.jupiter.api.*;
import org.slf4j.*;

import java.math.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestFunctions
{
  private static final Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withFormatting(true));
  private static final MoneyTransfer moneyTransfer = new MoneyTransfer("reference",
    new BankAccount(new Bank(List.of(new BankAddress("streetName", "10",
      "cityName", "poBox", "zipCode", "countryName")), "bankName"), "accountId", BankAccountType.CHECKING, "sortCode", "accountNumber", "transCode"),
    new BankAccount(new Bank(List.of(new BankAddress("streetName", "10",
      "cityName", "poBox", "zipCode", "countryName")), "bankName"), "accountId", BankAccountType.CHECKING, "sortCode", "accountNumber", "transCode"),
    new BigDecimal("100.00"));

  @Test
  @Order(10)
  public void testCreateMoneyTransferOrder()
  {
    MoneyTransferOrder createMoneyTransferOrder = new MoneyTransferOrder();
    APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent = new APIGatewayProxyRequestEvent();
    apiGatewayProxyRequestEvent.setBody(jsonb.toJson(moneyTransfer));
    apiGatewayProxyRequestEvent.setPath("/orders");
    apiGatewayProxyRequestEvent.setHttpMethod("POST");
    apiGatewayProxyRequestEvent.setResource("/orders");
    APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = createMoneyTransferOrder.handleRequest(apiGatewayProxyRequestEvent, new TestContext());
    assertThat(apiGatewayProxyResponseEvent).isNotNull();
    assertThat(jsonb.fromJson(apiGatewayProxyRequestEvent.getBody(), MoneyTransfer.class).getReference()).isEqualTo("reference");
  }

  @Test
  @Order(20)
  public void testGetMoneyTransferOrder()
  {
    MoneyTransferOrder getMoneyTransferOrder = new MoneyTransferOrder();
    APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent = new APIGatewayProxyRequestEvent();
    apiGatewayProxyRequestEvent.setPathParameters(Map.of("ref", "reference"));
    apiGatewayProxyRequestEvent.setHttpMethod("GET");
    apiGatewayProxyRequestEvent.setPath("/orders");
    apiGatewayProxyRequestEvent.setResource("/orders/{ref}");
    APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = getMoneyTransferOrder.handleRequest(apiGatewayProxyRequestEvent, new TestContext());
    assertThat(apiGatewayProxyResponseEvent).isNotNull();
    assertThat(apiGatewayProxyResponseEvent.getBody()).isNotNull();
    assertThat(jsonb.fromJson(apiGatewayProxyResponseEvent.getBody(), MoneyTransfer.class).getReference()).isEqualTo("reference");
  }

  @Test
  @Order(30)
  public void testGetMoneyTransferOrders()
  {
    MoneyTransferOrder getMoneyTransferOrders = new MoneyTransferOrder();
    APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent = new APIGatewayProxyRequestEvent();
    apiGatewayProxyRequestEvent.setPath("/orders");
    apiGatewayProxyRequestEvent.setHttpMethod("GET");
    apiGatewayProxyRequestEvent.setResource("/orders");
    APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = getMoneyTransferOrders.handleRequest(apiGatewayProxyRequestEvent, new TestContext());
    assertThat(apiGatewayProxyResponseEvent).isNotNull();
    //assertThat(jsonb.fromJson(apiGatewayProxyResponseEvent.getBody(), MoneyTransfers.class).getMoneyTransfers().size()).isEqualTo(1);
  }

  @Test
  @Order(40)
  public void testUpdateMoneyTransferOrder()
  {
    MoneyTransferOrder updateMoneyTransferOrder = new MoneyTransferOrder();
    APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent = new APIGatewayProxyRequestEvent();
    moneyTransfer.setAmount(new BigDecimal(200.00));
    apiGatewayProxyRequestEvent.setBody(jsonb.toJson(moneyTransfer));
    apiGatewayProxyRequestEvent.setHttpMethod("PUT");
    apiGatewayProxyRequestEvent.setPath("/orders");
    apiGatewayProxyRequestEvent.setResource("/orders");
    APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = updateMoneyTransferOrder.handleRequest(apiGatewayProxyRequestEvent, new TestContext());
    assertThat(apiGatewayProxyResponseEvent).isNotNull();
    assertThat(jsonb.fromJson(apiGatewayProxyResponseEvent.getBody(), MoneyTransfer.class).getAmount()).isEqualTo(BigDecimal.valueOf(200));
  }

  @Test
  @Order(50)
  public void testRemoveMoneyTransferOrder()
  {
    MoneyTransferOrder removeMoneyTransferOrder = new MoneyTransferOrder();
    APIGatewayProxyRequestEvent apiGatewayProxyRequestEvent = new APIGatewayProxyRequestEvent();
    apiGatewayProxyRequestEvent.setPathParameters(Map.of("ref", "reference"));
    apiGatewayProxyRequestEvent.setResource("/orders/{ref}");
    apiGatewayProxyRequestEvent.setHttpMethod("DELETE");
    apiGatewayProxyRequestEvent.setPath("/orders");
    APIGatewayProxyResponseEvent apiGatewayProxyResponseEvent = removeMoneyTransferOrder.handleRequest(apiGatewayProxyRequestEvent, new TestContext());
    assertThat(apiGatewayProxyResponseEvent).isNotNull();
  }

  private static class TestLogger implements LambdaLogger
  {
    private static final Logger logger = LoggerFactory.getLogger(TestLogger.class);

    @Override
    public void log(String message)
    {
      logger.info(message);
    }

    @Override
    public void log(byte[] message)
    {
      logger.info(new String(message));
    }
  }

  private static class TestContext implements Context
  {
    public TestContext()
    {
    }

    @Override
    public String getAwsRequestId()
    {
      return "495b12a8-xmpl-4eca-8168-160484189f99";
    }

    @Override
    public String getLogGroupName()
    {
      return "/aws/lambda/my-function";
    }

    @Override
    public String getLogStreamName()
    {
      return "2020/02/26/[$LATEST]704f8dxmpla04097b9134246b8438f1a";
    }

    @Override
    public String getFunctionName()
    {
      return "my-function";
    }

    @Override
    public String getFunctionVersion()
    {
      return "$LATEST";
    }

    @Override
    public String getInvokedFunctionArn()
    {
      return "arn:aws:lambda:us-east-2:123456789012:function:my-function";
    }

    @Override
    public CognitoIdentity getIdentity()
    {
      return null;
    }

    @Override
    public ClientContext getClientContext()
    {
      return null;
    }

    @Override
    public int getRemainingTimeInMillis()
    {
      return 300000;
    }

    @Override
    public int getMemoryLimitInMB()
    {
      return 512;
    }

    @Override
    public LambdaLogger getLogger()
    {
      return new TestLogger();
    }
  }
}
