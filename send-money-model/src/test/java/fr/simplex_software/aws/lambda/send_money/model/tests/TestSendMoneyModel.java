package fr.simplex_software.aws.lambda.send_money.model.tests;

import fr.simplex_software.aws.lambda.send_money.model.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.*;

import static fr.simplex_software.aws.lambda.send_money.model.MoneyTransferCommons.*;
import static org.assertj.core.api.Assertions.*;

public class TestSendMoneyModel
{
  @Test
  public void testUnmarshalMoneyTransferFile()
  {
    MoneyTransfers moneyTransfers = (MoneyTransfers) unmarshalXmlFile(new File("src/test/resources/money-transfer.xml"));
    assertThat(moneyTransfers).isNotNull();
    List<MoneyTransfer> moneyTransferList = moneyTransfers.getMoneyTransfers();
    assertThat(moneyTransferList).isNotNull();
    assertThat(moneyTransferList.size()).isEqualTo(5);
    MoneyTransfer moneyTransfer = moneyTransferList.get(3);
    assertThat(moneyTransfer.getReference()).isEqualTo("Meal and Entertainment");
    assertThat(moneyTransfer.getSourceAccount().getAccountID()).isEqualTo("SG01");
  }

  @Test
  public void testMarshalMoneyTransfers()
  {
    MoneyTransfers moneyTransfers = (MoneyTransfers) unmarshalXmlFile(new File("src/test/resources/money-transfer.xml"));
    assertThat(moneyTransfers).isNotNull();
    List<MoneyTransfer> moneyTransferList = moneyTransfers.getMoneyTransfers();
    assertThat(marshalMoneyTransfersToXmlFile(moneyTransfers, new File ("src/test/resources/xfer.xml"))).exists();
  }
}
