import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Main implements Runnable{

    BufferedReader buff;
    InputStreamReader isr;
    int selectedBank, selectedOperation;

    Main obj = null;

    public Main() {
        isr = new InputStreamReader(System.in);
        buff = new BufferedReader(isr);
    }
    public static int accountCreator(Main obj, Bank bank, Customer customer){
        System.out.println("You have to deposit at least "+bank.getMiniBalance()+" to open account with "+bank.getName()+"\nDo you want to create an account with us?Y/N");
        char ch='Y';
        try {
            ch=obj.buff.readLine().charAt(0);
        }
        catch (IOException e){
            e.fillInStackTrace();
        }
        if(ch=='N' || ch=='n')return 0;
        else{
            System.out.println("How much amount you want to deposit?\nPlease enter an amount greater than "+bank.getMiniBalance());
            float amt=0;
            try{
                amt=Integer.parseInt(obj.buff.readLine());
            }
            catch (IOException e){
                e.fillInStackTrace();
            }
            if(amt>= bank.getMiniBalance())customer.setBalance(amt);
            else {
                System.out.println("Sorry, we can't open your account with "+amt+" inr. Please try again later.");
                return 0;
            }
            return 1;
        }
    }
    public static void custOperation(Main obj, Bank bank, Customer customer){
        boolean flag = true;
        while (flag) {
            System.out.println("Select your choice\n1. Deposit\n2. Withdraw\n3. OpenFD\n4. Apply Loan\n5. Apply CC\n6. Show Balance\n7. Exit");
            try {
                obj.selectedOperation = Integer.parseInt(obj.buff.readLine());
            } catch (IOException e) {
                e.fillInStackTrace();
            }
            switch (obj.selectedOperation) {
                case 1 -> bank.depositMoney(customer);
                case 2 -> bank.withdrawMoney(customer);
                case 3 -> bank.openFD(customer);
                case 4 -> bank.applyLoan(customer);
                case 6 -> System.out.println("Currently your account balance is " + customer.getBalance() + ".");
                case 7 -> {
                    flag = false;
                    System.out.println("Hey "+customer.getCustomerName()+" thank you for banking with us. Visit again!!!");
                    System.out.println("Exiting...");
                }
                default -> System.out.println("choose correct one");
            }
        }
    }
    public static Customer searchCustInBank(Bank bank,String customerID) {
        for(Customer cust:bank.getCustList()){
            if(cust.getId().equals(customerID))return cust;
        }
        return null;
    }
    public static void main(String[] args) {

        Main obj1 = new Main();
        obj1.obj = obj1.getInstance(obj1);
        Thread t1 = new Thread(obj1);
        t1.start();
    }

    public Main getInstance(Main obj) {
        return obj;
    }

    public void run() {
        Bank bankICICI,bankHDFC,bankAXIS,bankSBI,bank;
        bankICICI= new ICICI(obj.isr, obj.buff);
        bankAXIS= new AXIS(obj.isr, obj.buff);
        bankHDFC= new HDFC(obj.isr, obj.buff);
        bankSBI= new SBI(obj.isr, obj.buff);
        ArrayList<String> arr = new ArrayList<>(Arrays.asList("ICICI", "HDFC", "SBI", "AXIS","Exit"));
        boolean flag=true;
        while(flag) {
            System.out.println("Welcome to IBS");
            System.out.println("Please select your bank");
            int i = 0;
            for (String it : arr) System.out.println(++i + ". " + it);
            try {
                obj.selectedBank = Integer.parseInt(obj.buff.readLine());
            } catch (IOException e) {
                e.fillInStackTrace();
            }
            if(obj.selectedBank==5){
                System.out.println("ICICI Customers:-");
                for(Customer cust:bankICICI.getCustList()){
                    System.out.println("-"+cust.getCustomerName());
                }
                System.out.println("HDFC Customers:-");
                for(Customer cust:bankHDFC.getCustList()){
                    System.out.println("-"+cust.getCustomerName());
                }
                System.out.println("SBI Customers:-");
                for(Customer cust:bankSBI.getCustList()){
                    System.out.println("-"+cust.getCustomerName());
                }
                System.out.println("AXIS Customers:-");
                for(Customer cust:bankAXIS.getCustList()){
                    System.out.println("-"+cust.getCustomerName());
                }
                return;
            }
            Customer customer;
            String custAadhaar="";
            System.out.println("Please enter your 12 digit aadhaar number");
            try {
                custAadhaar = obj.buff.readLine();
            } catch (IOException e) {
                e.fillInStackTrace();
            }
            switch (obj.selectedBank) {
                case 1 -> {
                    customer=searchCustInBank(bankICICI, custAadhaar+"_ICICI");
                    if(customer!=null){
                        System.out.println("Hurray! You have an account with us");
                        customer.setCustomerAadhar(custAadhaar);
                        custOperation(obj,bankICICI,customer);
                    }
                    else {
                        customer = new Customer(obj.isr, obj.buff);
                        customer.setCustomerAadhar(custAadhaar);
                        customer.setId(custAadhaar+"_ICICI");
                        if (accountCreator(obj, bankICICI, customer) == 1) {
                            bankICICI.addCustomer(customer);
                            custOperation(obj, bankICICI, customer);
                        }
                    }
                }
                case 2 -> {
                    customer=searchCustInBank(bankHDFC, custAadhaar+"_HDFC");
                    if(customer!=null){
                        System.out.println("Hurray! You have an account with us");
                        custOperation(obj,bankHDFC,customer);
                    }
                    else {
                        customer = new Customer(obj.isr, obj.buff);
                        if (accountCreator(obj, bankHDFC, customer) == 1) {
                            customer.setId(customer.getId() + "HDFC");
                            bankHDFC.addCustomer(customer);
                            custOperation(obj, bankHDFC, customer);
                        }
                    }
                }
                case 3 -> {
                    customer=searchCustInBank(bankSBI, custAadhaar+"_SBI");
                    if(customer!=null){
                        System.out.println("Hurray! You have an account with us");
                        custOperation(obj,bankSBI,customer);
                    }
                    else {
                        customer = new Customer(obj.isr, obj.buff);
                        if (accountCreator(obj, bankSBI, customer) == 1) {
                            customer.setId(customer.getId() + "SBI");
                            bankSBI.addCustomer(customer);
                            custOperation(obj, bankSBI, customer);
                        }
                    }
                }
                case 4 -> {
                    customer=searchCustInBank(bankAXIS, custAadhaar+"_AXIS");
                    if(customer!=null){
                        System.out.println("Hurray! You have an account with us");
                        custOperation(obj,bankAXIS,customer);
                    }
                    else {
                        customer = new Customer(obj.isr, obj.buff);
                        if (accountCreator(obj, bankAXIS, customer) == 1) {
                            customer.setId(customer.getId() + "AXIS");
                            bankAXIS.addCustomer(customer);
                            custOperation(obj, bankAXIS, customer);
                        }
                    }
                }
                default -> System.out.println("Choose the correct option");
            }

        }
    }

}