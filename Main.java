/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package br.ufrgs.rmpestano.rsa;

import java.math.BigInteger;
import java.util.List;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;  


/**
 *
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BigInteger p;
        BigInteger q;
        BigInteger e;
        final String message;
        boolean isFile = false;
		
		//variables
		InetAddress ip;
		String macAddress = "";
		BigInteger prime1, prime2;
		String str = "";
		
		//Get MAC
		try{
			
			System.out.println("************************************************"); 
			System.out.println("RSA Encryption and Decryption using MAC Address"); 
			System.out.println("************************************************");
			
			//get input string
			//Scanner sc= new Scanner(System.in); //System.in is a standard input stream  
			//System.out.println("Enter a string: ");  
			//str= sc.nextLine();              //reads string   
			
			//Get MAC Address
			ip = InetAddress.getLocalHost();
			NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            
			byte[] mac = network.getHardwareAddress();
				
			System.out.print("Current MAC address : ");
				
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < mac.length; i++) {
				sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
			}
			System.out.println(sb.toString());
			macAddress = sb.toString();
			   } catch (UnknownHostException ex) {
			
			ex.printStackTrace();
        
    } catch (SocketException ex){
            
        ex.printStackTrace();
            
    }
	
	String[] macAddressParts = macAddress.split("-");

	// convert hex string to byte values
	Byte[] macAddressBytes = new Byte[6];
	for(int i=0; i<6; i++){
		Integer hex = Integer.parseInt(macAddressParts[i], 16);
		macAddressBytes[i] = hex.byteValue();
	}
	
	String input =  (args.length == 0) ? "false" : args[0];
	
	System.out.println(input);
	
	Boolean isMAC = Boolean.valueOf(input);
	
	
	if(isMAC)
	{
		System.out.println("true");
		//convert mac to bigint
		String newAddress = macAddress.replace("-", "");
		
		BigInteger bigInt = new BigInteger(newAddress, 16);
		
		//Check prime or not. If not get next prime
		
		int prime = isPrime(bigInt.intValue());

		if (prime != 1) {
				int nextPrimeNumber = nextPrime(bigInt.intValue());
				prime1 = BigInteger.valueOf(nextPrimeNumber);//set P1
		} else {
			prime1 = bigInt;//set P1
		}
	} else {
		System.out.println("false");
		prime1 = BigInteger.valueOf(getRandomPrimeNumber());//set P1
	}
	
	prime2 = BigInteger.valueOf(getRandomPrimeNumber());

	 String mod = "B633443643654656565390F7412F2554387597814A25BC11BFFD95DB2D1456F1B66CDF52BCC1D20C7FF24F3CCE7B2D66E143213F64247454782A377C79C74477A 89 89 67 76 54 35 25 46 67 78  68 BC 6E 8F F0 01 D3 75 F9 36 3B 5A 71 61 C2 DF BC 2E D0 85 06 97 A5 44 21 55 2C 62 88 99 6A C6 1A F5 A9 F7 DE 21 8A BB C7 5A 14 5F 89 12 66 61 5E B8 1D 11 A2 2B 72 60 F7 60 80 83 B3 73 BA 4B C0 75 6B".replace(" ", "");
		
        if (args.length != 4) {//at leat four parametter should be given
            p = prime1;
            q = prime2;
            e = new BigInteger(mod, 32);//new BigInteger("65537");//can be set dynamic 
			
			 isFile = true;
                message = "E:\\Edel\\rsa\\sample.txt";
				
            //message = "The header lines were kept separate because they looked like mail headers and I have mailmode on.  The same thing applies to Bozo's quoted text.  Mailmode doesn't screw things up very often, but since most people are usually converting non-mail, it's off by default.";
			//message = str;//input string
            
        } else {
            p = new BigInteger(args[0]);
            q = new BigInteger(args[1]);
            e = new BigInteger(args[2]);
			 isFile = true;
                message = "E:\\Edel\\rsa\\sample.txt";
            /*if (args[3].contains("-f")) {
                isFile = true;
                message = args[3].substring(2);
            }
            else{
                message = args[3];
             }*/
        }
            
		
		/*RSA Algorithm*/
		
        RSA RSA = new RSAImpl(p, q, e);
        System.out.println(RSA);

        List<BigInteger> encryption;
        List<BigInteger> signed;
        List<BigInteger> decimalMessage;
        if(isFile){
			System.out.println(message);
			//System.out.println("test");
			long startTime = System.nanoTime();
            encryption = RSA.encryptFile(message);
			long stopTime = System.nanoTime();
			System.out.println(stopTime - startTime);
            signed = RSA.signFile(message);
            decimalMessage = RSA.fileToDecimal(message);
        } else {
            encryption = RSA.encryptMessage(message);
            signed = RSA.signMessage(message);
            decimalMessage = RSA.messageToDecimal(message);
        }

        List<BigInteger> decrypt = RSA.decrypt(encryption);
        List<BigInteger> verify = RSA.verify(signed);
		System.out.println("******************** Output *****************");
		//Output display
        System.out.println();
        System.out.println("message(plain text)   = " + Utils.bigIntegerToString(decimalMessage));
        //System.out.println("message(decimal)      = " + Utils.bigIntegerSum(decimalMessage));
        //System.out.println("encrypted(decimal)    = " + Utils.bigIntegerSum(encryption));
		System.out.println("encrypted(text)    = " 	  + Utils.bigIntegerToString(encryption));
        //System.out.println("decrypted(plain text) = " + Utils.bigIntegerToString(decrypt));
        //System.out.println("decrypted(decimal)    = " + Utils.bigIntegerSum(decrypt));
        //System.out.println("signed(decimal)       = " + Utils.bigIntegerSum(signed));
        //System.out.println("verified(plain text)  = " + Utils.bigIntegerToString(verify));
        //System.out.println("verified(decimal)     = " + Utils.bigIntegerSum(verify));
		System.out.println("************************************************");
    }
	
	 public static int getRandomPrimeNumber() {
        int num = 0;
        Random rand = new Random(); // generate a random number
        num = rand.nextInt(10000) + 1;

        while (!isPrimeCheck(num)) {          
            num = rand.nextInt(10000) + 1;
        }
        //System.out.println(num);  // print the number
		
		return num;
    }

    /**
     * Checks to see if the requested value is prime.
     */
    private static boolean isPrimeCheck(int inputNum){
        if (inputNum <= 3 || inputNum % 2 == 0) 
            return inputNum == 2 || inputNum == 3; //this returns false if number is <=1 & true if number = 2 or 3
        int divisor = 3;
        while ((divisor <= Math.sqrt(inputNum)) && (inputNum % divisor != 0)) 
            divisor += 2; //iterates through all possible divisors
        return inputNum % divisor != 0; //returns true/false
    }
	
	public static int isPrime(int num){
      int prime = 1;
      for(int i = 2; i < num; i++) {
         if((num % i) == 0) {
            prime = 0;
         }
      }
      return num;
   }
   public static int nextPrime(int num) {
      num++;
      for (int i = 2; i < num; i++) {
         if(num%i == 0) {
            num++;
            i=2;
         } else {
            continue;
         }
      }
      return num;
   }
}