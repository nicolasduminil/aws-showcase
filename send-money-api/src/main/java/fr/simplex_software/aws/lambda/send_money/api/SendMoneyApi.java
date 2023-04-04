package fr.simplex_software.aws.lambda.send_money.api;

import fr.simplex_software.aws.lambda.send_money.model.*;

public interface SendMoneyApi
{
  MoneyTransfer createMoneyTransferOrder (MoneyTransfer moneyTransfer);
  MoneyTransfers getMoneyTransferOrders();
  MoneyTransfer getMoneyTransferOrder(String reference);
  MoneyTransfer updateMoneyTransferOrder (MoneyTransfer moneyTransfer);
  void removeMoneyTransferOrder (String ref);
}
