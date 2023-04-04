package fr.simplex_software.aws.lambda.send_money.provider;

import fr.simplex_software.aws.lambda.send_money.api.*;
import fr.simplex_software.aws.lambda.send_money.model.*;

import java.util.*;
import java.util.stream.*;

public class DefaultSendMoneyProvider implements SendMoneyApi
{
  private static Map<String, MoneyTransfer> moneyTransferMap = new HashMap<>();

  public DefaultSendMoneyProvider()
  {
  }

  @Override
  public MoneyTransfer createMoneyTransferOrder(MoneyTransfer moneyTransfer)
  {
    moneyTransferMap.put(moneyTransfer.getReference(), moneyTransfer);
    return moneyTransfer;
  }

  @Override
  public MoneyTransfers getMoneyTransferOrders()
  {
    MoneyTransfers moneyTransfers = new MoneyTransfers();
    moneyTransfers.getMoneyTransfers().addAll(moneyTransferMap.entrySet().stream().map(es -> es.getValue()).collect(Collectors.toList()));
    return moneyTransfers;
  }

  @Override
  public MoneyTransfer getMoneyTransferOrder(String reference)
  {
    return moneyTransferMap.get(reference);
  }

  @Override
  public MoneyTransfer updateMoneyTransferOrder(MoneyTransfer moneyTransfer)
  {
    return createMoneyTransferOrder(moneyTransfer);
  }

  @Override
  public void removeMoneyTransferOrder(String ref)
  {
    moneyTransferMap.remove(ref);
  }
}
