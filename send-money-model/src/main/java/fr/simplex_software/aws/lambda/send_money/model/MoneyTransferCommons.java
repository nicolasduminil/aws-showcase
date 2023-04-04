package fr.simplex_software.aws.lambda.send_money.model;

import fr.simplex_software.aws.lambda.send_money.model.*;
import jakarta.xml.bind.*;

import java.io.*;

public class MoneyTransferCommons
{
  public static Object unmarshalXmlFile (File xml)
  {
    Object moneyTransfers = null;
    try
    {
      Unmarshaller unmarshaller = JAXBContext.newInstance(MoneyTransfers.class).createUnmarshaller();
      moneyTransfers = unmarshaller.unmarshal(xml);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return moneyTransfers;
  }

  public static File marshalMoneyTransfersToXmlFile(Object moneyTransfers, File xml)
  {
    try
    {
      Marshaller marshaller = JAXBContext.newInstance(moneyTransfers.getClass()).createMarshaller();
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      marshaller.marshal(moneyTransfers, xml);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return xml;
  }
}
