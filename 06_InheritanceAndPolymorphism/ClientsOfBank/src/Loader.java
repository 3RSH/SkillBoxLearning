import Clients.Company;
import Clients.Person;
import Clients.SoleProprietor;
import java.math.BigDecimal;

public class Loader {

  public static void main(String[] args) {

    //Проверка работы ФИЗ.ЛИЦА

    Person personAcc = new Person(BigDecimal.valueOf(500));

    personAcc.printInfo();

    personAcc.deposit(BigDecimal.valueOf(1500.152));
    personAcc.printInfo();

    personAcc.withdraw(BigDecimal.valueOf(1000.622));
    personAcc.printInfo();

    personAcc.withdraw(BigDecimal.valueOf(10000));
    personAcc.printInfo();

    System.out.println("=================================");
    //Проверка работы ЮР.ЛИЦА

    Company companyAcc = new Company(BigDecimal.valueOf(500));

    companyAcc.printInfo();

    companyAcc.deposit(BigDecimal.valueOf(1500.152));
    companyAcc.printInfo();

    companyAcc.withdraw(BigDecimal.valueOf(1000.622));
    companyAcc.printInfo();

    companyAcc.withdraw(BigDecimal.valueOf(10000));
    companyAcc.printInfo();

    System.out.println("=================================");
    //Проверка работы ИП

    SoleProprietor soleProprietor = new SoleProprietor(BigDecimal.valueOf(500));

    soleProprietor.printInfo();

    soleProprietor.deposit(BigDecimal.valueOf(1000));
    soleProprietor.printInfo();

    soleProprietor.deposit(BigDecimal.valueOf(1500.152));
    soleProprietor.printInfo();

    soleProprietor.withdraw(BigDecimal.valueOf(1000.622));
    soleProprietor.printInfo();

    soleProprietor.withdraw(BigDecimal.valueOf(10000));
    soleProprietor.printInfo();

    System.out.println("=================================");
  }
}
