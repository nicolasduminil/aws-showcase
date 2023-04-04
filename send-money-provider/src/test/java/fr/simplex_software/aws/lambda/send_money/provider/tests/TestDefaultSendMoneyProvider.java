package fr.simplex_software.aws.lambda.send_money.provider.tests;

import fr.simplex_software.aws.lambda.send_money.api.*;
import fr.simplex_software.aws.lambda.send_money.model.*;
import fr.simplex_software.aws.lambda.send_money.provider.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.math.*;
import java.util.*;

import static fr.simplex_software.aws.lambda.send_money.model.MoneyTransferCommons.*;
import static org.assertj.core.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestDefaultSendMoneyProvider
{
  private SendMoneyApi sendMoneyApi = new DefaultSendMoneyProvider();
  @Test
  @Order(10)
  public void testCreateMoneyTransferOrder()
  {
    MoneyTransfers moneyTransfers = (MoneyTransfers) unmarshalXmlFile(new File("src/test/resources/money-transfer.xml"));
    assertThat(moneyTransfers).isNotNull();
    List<MoneyTransfer> moneyTransferList = moneyTransfers.getMoneyTransfers();
    assertThat(moneyTransferList).isNotNull();
    assertThat(moneyTransferList.size()).isEqualTo(5);
    MoneyTransfer moneyTransfer = moneyTransferList.get(3);
    moneyTransfer = sendMoneyApi.createMoneyTransferOrder(moneyTransfer);
    assertThat(moneyTransfer.getReference()).isEqualTo("Meal and Entertainment");
    assertThat(moneyTransfer.getSourceAccount().getAccountID()).isEqualTo("SG01");
  }

  @Test
  @Order(20)
  public void testGetMoneyTransferOrders()
  {
    List<MoneyTransfer> moneyTransferList = sendMoneyApi.getMoneyTransferOrders().getMoneyTransfers();
    assertThat(moneyTransferList.size()).isEqualTo(1);
    MoneyTransfer moneyTransfer = moneyTransferList.get(0);
    assertThat(moneyTransfer.getReference()).isEqualTo("Meal and Entertainment");
    assertThat(moneyTransfer.getSourceAccount().getAccountID()).isEqualTo("SG01");
  }

  @Test
  @Order(30)
  public void testGetMoneyTransferOrder()
  {
    MoneyTransfer moneyTransfer = sendMoneyApi.getMoneyTransferOrder("Meal and Entertainment");
    assertThat(moneyTransfer).isNotNull();
    assertThat(moneyTransfer.getReference()).isEqualTo("Meal and Entertainment");
    assertThat(moneyTransfer.getSourceAccount().getAccountID()).isEqualTo("SG01");
  }

  @Test
  @Order(40)
  public void testUpdateMoneyTransfer()
  {
    MoneyTransfer moneyTransfer = sendMoneyApi.getMoneyTransferOrder("Meal and Entertainment");
    assertThat(moneyTransfer).isNotNull();
    moneyTransfer.setAmount(new BigDecimal(1500.00));
    moneyTransfer = sendMoneyApi.updateMoneyTransferOrder(moneyTransfer);
    assertThat(moneyTransfer.getAmount()).isEqualTo(BigDecimal.valueOf(1500));
  }

  @Test
  @Order(50)
  public void testDeleteMoneyTransfer()
  {
    sendMoneyApi.removeMoneyTransferOrder("Meal and Entertainment");
  }
}
