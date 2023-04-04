package fr.simplex_software.aws.lambda.send_money.functions.tests;

import fr.simplex_software.aws.lambda.send_money.model.*;
import jakarta.ws.rs.client.*;
import jakarta.ws.rs.core.*;
import org.apache.http.*;
import org.junit.jupiter.api.*;

import java.math.*;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FunctionsIT
{
  private Client client;
  private WebTarget webTarget;
  private static final String API_GATEWAY_URL = "https://7v4qaqdy34.execute-api.eu-west-3.amazonaws.com/dev";
  private static MoneyTransfer moneyTransfer = new MoneyTransfer("reference",
    new BankAccount(new Bank(List.of(new BankAddress("streetName", "10",
      "cityName", "poBox", "zipCode", "countryName")), "bankName"), "accountId", BankAccountType.CHECKING, "sortCode", "accountNumber", "transCode"),
    new BankAccount(new Bank(List.of(new BankAddress("streetName", "10",
      "cityName", "poBox", "zipCode", "countryName")), "bankName"), "accountId", BankAccountType.CHECKING, "sortCode", "accountNumber", "transCode"),
    new BigDecimal("100.00"));

  @BeforeEach
  public void beforeEach()
  {
    client = ClientBuilder.newClient();
    webTarget = client.target(API_GATEWAY_URL).path("/orders");
  }

  @AfterEach
  public void afterEach()
  {
    if (client != null)
    {
      client.close();
      client = null;
    }
    webTarget = null;
  }
  @Test
  @Order(10)
  public void testCreateMoneyTransferOrder()
  {
    Response response = webTarget.request().accept(MediaType.APPLICATION_JSON)
      .post(Entity.entity(moneyTransfer, MediaType.APPLICATION_JSON));
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
    MoneyTransfer moneyTransfer1 = response.readEntity(MoneyTransfer.class);
    assertThat(moneyTransfer1).isNotNull();
    assertThat(moneyTransfer1.getReference()).isEqualTo("reference");
  }

  @Test
  @Order(20)
  public void testGetMoneyTransferOrder()
  {
    Response response = webTarget.path("{ref}").resolveTemplate("ref", "reference").request().accept(MediaType.APPLICATION_JSON).get();
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
    MoneyTransfer moneyTransfer1 = response.readEntity(MoneyTransfer.class);
    assertThat(moneyTransfer1).isNotNull();
    assertThat(moneyTransfer1.getReference()).isEqualTo("reference");
  }

  @Test
  @Order(30)
  public void testUpdateMoneyTransferOrder()
  {
    moneyTransfer.setAmount(new BigDecimal(500));
    Response response = webTarget.request().accept(MediaType.APPLICATION_JSON)
      .put(Entity.entity(moneyTransfer, MediaType.APPLICATION_JSON));
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
    MoneyTransfer moneyTransfer1 = response.readEntity(MoneyTransfer.class);
    assertThat(moneyTransfer1).isNotNull();
    assertThat(moneyTransfer1.getReference()).isEqualTo("reference");
  }

  @Test
  @Order(40)
  public void testDeleteMoneyTransferOrder()
  {
    Response response = webTarget.path("{ref}").resolveTemplate("ref", "reference").request().accept(MediaType.APPLICATION_JSON).get();
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
    response = webTarget.request().accept(MediaType.APPLICATION_JSON).get();
    assertThat(response).isNotNull();
    assertThat(response.getStatus()).isEqualTo(HttpStatus.SC_OK);
    MoneyTransfers moneyTransfers = response.readEntity(MoneyTransfers.class);
    assertThat(moneyTransfers).isNotNull();
    List<MoneyTransfer> moneyTransferList = moneyTransfers.getMoneyTransfers();
    assertThat(moneyTransferList).isNotNull();
    assertThat(moneyTransferList).hasSize(0);
  }
}
