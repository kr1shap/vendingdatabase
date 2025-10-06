/*
Name: Krisha patel 
Date: Dec 19, 2022
ICS3U
Purpose: Create a database program for a vending machine with loyalty membership information and restocker
*/

import java.util.*;
import java.io.*;
class Main {
    public static void main(String[] args) throws IOException {
		//Variable declaration 
		String line = ""; //for the printwriter 
		String userYes_No = "", userYes_NoLoyal = ""; //yes/no input, stores yes/no input when asking user if they are a loyalty member
		String userStat, userNewOld; //stores if user is customer or restocker, new or old customer
		String userCode, userPay; //stores discount code input, user payment input 
		String userInput = ""; //stores user code num (A1, C4, etc.)
		String username = "", password = "", userDiet = ""; //for new loyalty program members 
		boolean dateCheckStock = false; //stores the return value of method checkDateStock
		boolean purchaseDone = false;//if purchase is completed or not
		boolean userLoyalty = false; //if user is a loyalty member
		boolean userPayDone = false; //if they completed payment 
		boolean cancelled = false; //if they cancelled the purchase
		boolean unique = false; //unique username or not
		boolean stockDone = false; //if restocker finishes restocking
		double cost = 0, originalCost = 0, balance = 0;  //stores the cost of the purchase, original cost in case of any cancel or for loyalty members, balance of loyalty points
		int userReplace = 0; //num of items for restocker to replace
		int userRateItem = 0, userRate = 0, userNum = 0; //Item user wants to rate, the rating they give, the number of items they want to buy
		int counter = 0; //counter used for entering discount code
		int spend = 0; //the amount of points that the user wants to spend (loyalty)
		int userYear = 0, userMonth = 0; //user year when inputting expiry date, user month for expiry date
		String [] stockNum = new String [16]; //item number 
		double [] price = new double [16]; //price of item
		String [] itemName = new String [16]; //menu items
		int [] stock = new int [16]; //stock num
		String [] expiry = new String [16]; //expiry dates 
		int [] shoppingCart = new int [16]; //to store the shopping cart 
		String [] date = new String [2]; //to store the date 
		int [] up = new int [16]; //up ratings
		int [] down = new int [16]; //down ratings 
		String [] menuDiet = new String [16]; //store the whole line of a diet 
		ArrayList<String> loyaltyProgram = new ArrayList<String>(); //loyalty program info
		String [] userInfo = new String [6]; //stores user info
		
		//Extract files into arrays 
		Scanner in = new Scanner(new File("MENU.txt")); //menu  
		//For loop to input menu
		for(int i = 0; i < 16; i++) {
			line = in.nextLine();
			stockNum[i] = (line.split("-")[0]);
			price[i] = (Double.parseDouble(line.split("-")[2]));
			itemName[i] = (line.split("-")[1]);
		} in.close(); 

		Scanner in2 = new Scanner (new File ("STOCK_DATE.txt")); //stock and stock date
		//For loop to store textfile into array
		for(int i = 0; i < 16; i++) {  
			line = in2.next();
			stock[i] = (Integer.parseInt(line.split("-")[1]));
			expiry[i] = (line.split("-")[2]);
			down[i] = (Integer.parseInt(line.split("-")[3]));
			up[i] = (Integer.parseInt(line.split("-")[4]));
		} in2.close();

		Scanner in3 = new Scanner(new File ("CURRENT_DATE.txt")); //current date 
		line = in3.next();
		date = (line.split("/"));
		in3.close();

		Scanner in4 = new Scanner(new File ("LOYALTY.txt")); //loyalty program members
		while(in4.hasNextLine()) {
			line = in4.nextLine();
			loyaltyProgram.add(line);
		}
		in4.close();

		//Ask the user if they are a user or restocker 
		Scanner input = new Scanner(System.in);
		System.out.println(".       .      ↻ Welcome to the Vendor Shop!       .       .\n1. You will be asked if you're a customer or a restocker \n2. A restocker can restock certain items \n3. A customer can buy items and even register for the loyalty program\n");
		divider("✨");
		System.out.print("Restocker or customer?: "); 
		userStat = input.next();
		//Check user input 
		while(!(userStat.equalsIgnoreCase("restocker")) && !(userStat.equalsIgnoreCase("customer"))) {
			System.out.println("You can only choose one of the two options...");
			System.out.print("Are you a restocker or customer?: "); 
			userStat = input.next();
		}
		
		//If Customer
		if(userStat.equalsIgnoreCase("customer")) {
			//Ask if they are a old or new customer 
			System.out.print("Are you an old or new customer? If you are part of the loyalty program, please type old: ");
			userNewOld = input.next();
			while(!(userNewOld.equalsIgnoreCase("old")) && !(userNewOld.equalsIgnoreCase("new"))) {
				System.out.println("You can only choose one of the two options...");
				System.out.print("Old or new?: "); 
				userNewOld = input.next();
			}
			if(userNewOld.equalsIgnoreCase("old")) {
				System.out.println("");
				//Print out the menu 
				for(int i = 0; i < 16; i++) {
					System.out.print(stockNum[i] + " " + itemName[i] + "  $" + price[i] + "  ");
					if(i %2 != 0 )
						System.out.println("\n");
				}
				//Ask for user rating 
				System.out.print("Please enter one item CODE which you have tried before!: ");
				userInput = input.next();
				int i = 0; //use variable to check through array 
				while(i <= 16) {
					//If it is not found 
					if(i == 16) {
						System.out.print("Wrong input... enter the CODE num: ");
						userInput = input.next();
						i = 0; 
					}
					else if(userInput.equalsIgnoreCase(stockNum[i])) {
						userRateItem = i; 
						i = 17; 
					}
					else 
						i++;
				}
				
				//Ratings system 
				System.out.println("\t\t\tGIVE A RATING \n \t\t\t👎   👍\n \t\t\tDOWN   UP");
				System.out.println("\t\tEnter 1 for THUMBS DOWN\n \t\tEnter 2 for THUMBS UP");
				userRate = input.nextInt();
				while(userRate < 1 || userRate > 2) {
					System.out.print("INVALID INPUT! Please enter again: ");
					userRate = input.nextInt();
				}
				if(userRate == 1) //thumbs down 
					down[userRateItem]++;
				else //thumbs up 
					up[userRateItem]++;
				
				//Check for loyalty - ask the user if they are in the loyalty program and then check if their input is true or not 
				System.out.println("THANK YOU FOR YOUR RATING!");
				divider("🥢");
				System.out.print("Are you a part of the loyalty program? (y/n): ");
				userYes_NoLoyal = input.next();
				userYes_NoLoyal = valid(userYes_NoLoyal); //call method to check y/n
				
				if(userYes_NoLoyal.equalsIgnoreCase("y")) {
					userInfo = checkPass(loyaltyProgram); //call method to check if their account is valid
					if(userInfo[0].equalsIgnoreCase("false")) 
						System.out.println("\nOops! Turns out you weren't a loyalty member at all. However, feel free to sign up at the end of your purchase.");
					else {
						userLoyalty = true; //puts boolean as true 
						System.out.println("⋆ ★⋆ ★⋆ ★⋆ ★⋆ ★⋆ ★⋆ ★⋆ ★⋆ ★⋆ ★⋆ ★⋆ ★⋆ ★⋆ ★⋆ ★⋆ ★⋆ ★⋆ ★⋆ ★⋆ \n");
						System.out.println("\t\t\t\t\t WELCOME " + userInfo[1] + "\n");
						balance = Double.parseDouble(userInfo[4]); 
						System.out.println("🔖 BALANCE: " + balance);
						System.out.println("👨‍🦲 USERNAME:" + userInfo[1]);
						System.out.println("🔑 PASSWORD: " + userInfo[2]);

						//Scanner to input every menu line in an array - only needed for loyalty program 
						Scanner in5 = new Scanner(new File ("MENU.txt"));
						for(int j = 0; j < 16; j++) {
							line = in5.nextLine();
							menuDiet[j] = line;
						} in5.close();
					}
				}
				else
					System.out.println("Hopefully you can join us next time!");
			}
			//Print out the menu by using method - if item is out of stock/expired then do not print out that certain item 
			divider("MENU");
			System.out.println("");
			printMenu(stockNum, price, itemName, expiry, date, stock, userLoyalty, userInfo[3], menuDiet); //call method to print out the menu 
			
			//While loop to check user input for entering menu items 
			while(purchaseDone == false) {
				divider("🥢");
				System.out.print("Enter the item CODE which you would like to buy: ");
				userInput = input.next();
				//For loop to check if input is valid or not
				for(int i = 0; i <= 16; i++) {
					//If input is invalid/not found in array
					if(i == 16) {
						System.out.println("Wrong input...");
						i = 20; 
					}
					//If input is found in array 
					else if(userInput.equalsIgnoreCase(stockNum[i])) {
						dateCheckStock = checkDateStock(expiry[i], date, stock[i]); //call method to 
						//If item is expired or stock has run out 
						if(dateCheckStock == true) { 
							System.out.println("INVALID CHOICE! Please enter the code num you see! ");
							i = 20; 
						}
						//If member is a loyalty member and they select an item that they cannot choose
						else if(userLoyalty == true && !menuDiet[i].contains(userInfo[3])) {
							System.out.print("Oh no! You cannot choose this item according to your dietary restrictions!");
							i = 20; 
						}
						else {
							System.out.println("RATING: 👍 " + up[i] + "\t👎 " + down[i]); 
							System.out.print("Enter the number of " + itemName[i] + " you want to buy: ");
							userNum = input.nextInt();
							//Check if the userNum is greater than stock/greater than 3/less than 0
							while(userNum > stock[i] || userNum > 3 || userNum < 0 || (shoppingCart[i] + userNum > 3)) {
								System.out.println("\nINVALID, must be under 3 items! Or you have already put 3 of this item in your cart!");
								divider("🥢");
								System.out.println("\t\t\t ↻ STOCK: " + (stock[i]-shoppingCart[i]) + "\t\t\t ↻ CART: " + shoppingCart[i]);
								System.out.print("Enter the number you want to buy:" );
								userNum = input.nextInt();
							}
							//Add num of items to cart and calculate price 
							shoppingCart[i] += userNum;
							cost = (Math.round((cost+(price[i]*userNum))*100)/100.0);
							//Ask if user is done with their purchase
							System.out.println("🍴 Shopping cart: $" + cost + "\n");
							System.out.print("Are you done with your purchase? (y/n): ");
							userYes_No = input.next();
							userYes_No = valid(userYes_No);
							if(userYes_No.equalsIgnoreCase("y"))
								purchaseDone = true; 
							
							i = 20; //to get out of the for loop
						}
					}
				}
			}

			//Ask the user for payment method 
			if(cost == 0)
				System.out.println("NO PURCHASE WAS MADE!");
			else {
				//print out the bill by using for loop
				System.out.println("\n\t\t\t\t\t\t\t BILL \t\t NUM");
				for(int i = 0; i < 16; i++) {
					if(shoppingCart[i] != 0) {
						System.out.println("\t\t\t\t" + stockNum[i] + " " + itemName[i] + " $" + price[i] + " " + shoppingCart[i]);
					}
				}
				cost = (Math.round(cost*100)/100.0);
				System.out.println("\t\t\t\t\t\tTOTAL COST: $" + cost);
				System.out.println("\t\t\t________________________________________\n");
				System.out.println("Woah! Time to generate a discount code!! 🎉 " + generateCode());
				System.out.print("Would you like to enter your discount code? (y/n): ");
				userYes_No = input.next();
				userYes_No = valid(userYes_No); //check for valid input
				//Ask to enter code if they said yes
				if(userYes_No.equalsIgnoreCase("y")) {
					System.out.print("ENTER CODE 🎟 : ");
					userCode = input.next();
					//Loop only allows user to have 6 tries (counting the previous one)
					while(counter < 5) {
						if(!(userCode.equalsIgnoreCase("SAVE10")) && !(userCode.equalsIgnoreCase("SAVE30"))) {
							System.out.print("INVALID. Re-enter please (tries left = " + (5-counter) + "): ");
							userCode = input.next();
						}
						counter++;
					}
					if(userCode.equalsIgnoreCase("SAVE10")) //10% off
						cost = cost*0.9;
					else if(userCode.equalsIgnoreCase("SAVE30")) // 30% off
						cost = cost*0.6; 
					else 
						System.out.println("You have ran out of tries to enter your code. Try again next purchase!");
					
					cost = (Math.round(cost*100)/100.0);
					System.out.println("\t\t\t\t\t\tCURRENT COST: $" + cost);
				}
				originalCost = cost; //store this in case any purchase gets cancelled and for loyalty points
				divider("✨");
 				System.out.println("\t\t\t\t↻      .    Loading    .      ↻\n\nHow would you like to pay? (Select points to use points first!) \n↻ CASH \t\t💵 \n↻ CREDIT \t💳 \n↻ POINTS \t💱 \n"); //do points later

				//While user has not paid for their purchase
				while(userPayDone == false) {
					System.out.print("Enter payment method: ");
					userPay = input.next();
					//If payment is invalid 
					while(!(userPay.equalsIgnoreCase("cash")) &&!(userPay.equalsIgnoreCase("credit")) && !(userPay.equalsIgnoreCase("points"))) {
						System.out.print("INVALID \n↻ CASH \t\t💵 \n↻ CREDIT \t💳 \n↻ POINTS \t💱"); 
						userPay = input.next();
					}
					if(userPay.equalsIgnoreCase("cash")) {
						payCash(cost, shoppingCart); //call the method for cash payment
						userPayDone = true; 
					}
					else if(userPay.equalsIgnoreCase("credit")) {
						payCredit(); //call the method for credit payment
						userPayDone = true;
					}
					else { //points - 100 points is valued at a dollar 
						//If the user is actually a loyalty member 
						if(userLoyalty == true) {
							System.out.println("🔖 BALANCE: " + balance);
							System.out.println("🔖 POINTS NEEDED FOR PURCHASE TO BE FREE: " + cost*100); 
							//Cannot purchase if balance is under a certain amount
							if(balance < 10) {
								System.out.print("Oh no! You do not have enough points!");
								//Let them pay by another method 
								System.out.print("\n↻ CASH \t\t💵 \n↻ CREDIT \t💳 ");
									userPay = input.next();
									userPay = payment(userPay); //method to check input 
									if(userPay.equalsIgnoreCase("cash")) {
										payCash(cost, shoppingCart);
										userPayDone = true; 
									}
									else {
										payCredit();
										userPayDone = true; 
									}
							}
							//If they have enough points to spend on their purchase 
							else {
								System.out.print("How many points would you like to spend? (You can only enter it once): ");
								spend = input.nextInt();
								//If input greater than balance or greater than cost 
								while(((double)spend) > balance || ((double)spend)/100 > cost) {
									System.out.println("INVALID INPUT: YOU CANNOT SPEND MORE THAN WHAT YOU HAVE! YOU ALSO CANNOT SPEND MORE POINTS THAN THE COST.");
									System.out.println("\n🔖 POINTS NEEDED FOR PURCHASE TO BE FREE: " + cost*100 + "\n🔖 CURRENT BALANCE: " + balance);
									System.out.print("How many points would you like to spend? Whole number please: "); 
									spend = input.nextInt();
								}
								cost = cost - (double)spend/100; //new cost
								cost = (Math.round(cost*100)/100.0);
								balance -= spend; //new balance
								
								//If there is still money left over
								if(cost > 0) {
									System.out.println("You still have to pay off $" + cost + "! Which method would you like to pay with."); 
									System.out.println("\n↻ CASH \t\t💵 \n↻ CREDIT \t💳 ");
									userPay = input.next();
									userPay = payment(userPay); //call method to check input
									if(userPay.equalsIgnoreCase("cash")) {
										payCash(cost, shoppingCart);
										//Check if they cancelled purchase, then add balance and stock back
										cancelled = true; 
										for(int i = 0; i < 16; i++) {
											if(shoppingCart[i] != 0) {
												cancelled = false; 
												i = 20; 
											}
										}
										if(cancelled == true) //if all items in cart is 0 then purchase has been cancelled
											balance += spend; 
										
										userPayDone = true; //get out of the loop 
									}
									else {
										payCredit();
										userPayDone = true; 
									}
								}
								else {
									System.out.println("🔖 BALANCE LEFT: " + balance);
									userPayDone = true; 
								}
							} 
						}
						//If user put points, but they are not in the program
						else
							System.out.print("You are not in the loyalty program :(. You can sign up after this purchase!");
					}
				} 

			} //if purchase was made

			//If user is a loyalty member and purchase was not cancelled, add points to balance based off original price of purchase
			if(userLoyalty == true && cancelled == false) {
				balance += Math.round(originalCost*10); //10 points per dollar cost 
				userInfo[4] = Double.toString(balance);
				loyaltyProgram.set(Integer.parseInt(userInfo[5]), userInfo[1] + "-" + userInfo[2] + "-" + userInfo[3] + "-" + userInfo[4]);
			}
			
			divider("🥢");
			message();
			
			//if user is new and not part of the program --> ask for loyalty 
			if(userNewOld.equalsIgnoreCase("new") || userYes_NoLoyal.equalsIgnoreCase("n") || userLoyalty == false) {
				System.out.println("♡ LOYALTY PROGRAM \n ⊃ get your own modified menu ✦ \n ⊃ gain points after purchases made ✦ \n ⊃ 10 points per dollar spent ✦ \n ⊃ 100 points = 1 dollar to spend ✦\n");
				System.out.print("Would you like to be a part of our loyalty program? (y/n): "); 
				userYes_No = input.next();
				userYes_No = valid(userYes_No); //check for valid input

				//If they want to be a part of the loyalty program, then ask for user information 
				if(userYes_No.equalsIgnoreCase("y")) {
					System.out.print("\n↻ Enter a USERNAME (no space): ");
					username = input.next();
					//Check if they have a unique username 
					while(unique == false) {
						for(int i = 0; i <= loyaltyProgram.size(); i++) {
							if(i == loyaltyProgram.size())
								unique = true;
							//If username is found in the database 
							else if(loyaltyProgram.get(i).contains(username) || username.length() < 4) {
								System.out.print("\n↻ THIS USERNAME IS TAKEN OR IT MUST BE OVER 3 CHARACTERS LONG! Enter a USERNAME (no space): ");
								username = input.next();
								i = 0; 
							}	
						}
					}
					System.out.print("\n↻ Enter a PASSWORD (no space): ");
					password = input.next();
					System.out.print("\nEnter a DIET (vegetarian, keto, vegan, regular): ");
					userDiet = input.next();
					//Invalid input - user enters none of the diets shown 
					while(!(userDiet.equalsIgnoreCase("vegetarian")) && !(userDiet.equalsIgnoreCase("vegan")) && !(userDiet.equalsIgnoreCase("keto")) && !(userDiet.equalsIgnoreCase("regular"))) {
						System.out.print("INVALID: Re-enter a valid diet please!: ");
						userDiet = input.next();
					}
					//Add user information into arraylist 
					loyaltyProgram.add(username + "-" + password + "-" + userDiet.toLowerCase() + "-" + 1000);
				
				}
				else
					System.out.println("Aw :( Maybe next time!");
				message();
		
			}
			//Print out loyalty program members back to textfile 
			PrintWriter p = new PrintWriter("LOYALTY.txt");
			for(int i = 0; i < loyaltyProgram.size(); i++)
				p.println(loyaltyProgram.get(i));
			p.close();
			
		} //end of customer 
			
		//If they are a restocker
		else {
			System.out.print("\nENTER THE PASSWORD FOR THE MACHINE: ");
			String vendPass = input.next();
			//If user does not enter correct password
			while(!vendPass.equalsIgnoreCase("ICS3U")) {
				System.out.print("INVALID PASSWORD!");
				divider("✨");
				System.out.print("Psssst.. it's the name of the course...\nENTER THE PASSWORD FOR THE MACHINE");
				vendPass = input.next();
			}
			System.out.println();
			System.out.println("Welcome restocker! Here are the code items below and their expiry dates 📅 \n" + "Today's date is " + (date[0] + "/" + date[1]) + "🎇\n");

			//Print out the number items and stock
			System.out.println("NUM STOCK   EXPIRY     NUM STOCK   EXPIRY");
			for(int i = 0; i < 16; i++) {
				System.out.print(stockNum[i] + "    " + stock[i] + "    " + expiry[i] + "\t\t");
				if(i%2 != 0) 
					System.out.println("\n");
			}
			//While loop to repeat stock changes 
			while(stockDone == false) {
				System.out.print("Enter the item CODE you want to restock: ");
				userInput = input.next();
				for(int i = 0; i <= 16; i++) {
					if(i == 16) { //if reached the end of the array 
						i = 20; 
						System.out.print("Wrong input...");
					}
					//Valid input 
					else if(userInput.equalsIgnoreCase(stockNum[i])) {
						dateCheckStock = checkDateStock(expiry[i], date, stock[i]); //call method to check expiry
						//If item is expired 
						if(dateCheckStock == true) {
							System.out.print("Enter the NUMBER of " + itemName[i] + " you would like to restock: ");
							userReplace = input.nextInt();
							userReplace = restocker(userReplace);
							stock[i] = userReplace; //store stock num 
							divider("✨");
							
							//User enters expiry date
							System.out.print("Enter the year of the expiry date: "); 
							userYear = input.nextInt();
							while(userYear < Integer.parseInt(date[1])) {
								System.out.print("\nINVALID year. Enter the year of the expiry date: "); 
								userYear = input.nextInt();
							}
							System.out.print("Enter the month of the expiry date: ");
							userMonth = input.nextInt();
							//If month is under 1 and more than 12 
							while(userMonth < 1 || userMonth > 12) {
								System.out.print("\nINVALID month. Enter the year of the expiry date: "); 
								userMonth = input.nextInt();
							}
							//If userYear is equal to current year, then month must be greater or equal to current month
							if(userYear == Integer.parseInt(date[1])) {
								while(userMonth < Integer.parseInt(date[0])) {
									System.out.print("\nINVALID month. Enter the year of the expiry date: "); 
									userMonth = input.nextInt();
								}	
							}	
							expiry[i] = (Integer.toString(userMonth) + "/" + Integer.toString(userYear)); //store expiry date
						}
						else
							System.out.println("Seems like you cannot restock this item!");
						divider("📦");
						//Ask user if they want to restock another item 
						System.out.print("Would you like to restock another item? (y/n): ");
						userYes_No = input.next();
						userYes_No = valid(userYes_No);
						if(userYes_No.equalsIgnoreCase("n")) 
							stockDone = true; //ends the loop
						
						i = 20; //exits the for loop to re-iterate 
					}
				} 
			}
			System.out.print("\t\t\t🎊THANK YOU FOR YOUR SERVICE.🎊");
		} //end of restocker

		//Update inventory at the end of each run of the program 
		updateInv(stockNum, expiry, down, up, shoppingCart, stock);
		
		//Counter to change the date each time
		PrintWriter p = new PrintWriter("CURRENT_DATE.txt");
		//If month is 12, change year and month 
		if(date[0].equals("12")) {
			date[0] = "1"; 
			date[1] = (Integer.toString(Integer.parseInt(date[1]) + 1));
		} 
	  	else
			date[0] = (Integer.toString(Integer.parseInt(date[0]) + 1));
		p.print(date[0]+"/"+date[1]);
		p.close(); 
		
		//left input open due to element exceptions 
	}

//METHODS
	
	/*Purpose: To check if an item is expired/out of stock or not 
	input: String expiry, String [] date, and int stock 
	output: Returns one String parameter expiredStock, which contains true or false information
	*/
	public static boolean checkDateStock(String expiry, String [] date, int stock) {
		boolean expiredStock = false; //stores true or false information 
		if(Integer.parseInt(expiry.split("/")[1]) < Integer.parseInt(date[1]) || ((Integer.parseInt(expiry.split("/")[1]) == Integer.parseInt(date[1])) && (Integer.parseInt(date[0]) > Integer.parseInt(expiry.split("/")[0]))) || stock < 1)
			expiredStock = true; 
		return expiredStock; 
	}
	
	/*Purpose: To print out the menu 
	input: String [] stockNum, double [] price, String [] itemName, String [] expiry, String [] date, int [] stock, boolean loyalty, String diet, String menuDiet
	output: N/A
	*/
	public static void printMenu(String [] stockNum, double [] price, String [] itemName, String [] expiry, String [] date, int [] stock, boolean loyalty, String diet, String [] menuDiet) {
		boolean expiredStock; //stores the return value of method checkDateStock
		for(int i = 0; i < 16; i++) {
			expiredStock = checkDateStock(expiry[i],date, stock[i]);
			//If they are a loyalty member - check two conditions. Otherwise check one condition
			if(loyalty == true) {
				if(expiredStock == false && menuDiet[i].contains(diet)) {
					System.out.print(stockNum[i] + " " + itemName[i] + " $" + price[i] + " \t\t");
				}
				else
					System.out.print("❌❌❌❌❌❌❌❌❌❌❌❌❌❌\t\t");
			}
			else {
				if(expiredStock == false)
					System.out.print(stockNum[i] + " " + itemName[i] + " $" + price[i] + " \t\t");
				else
					System.out.print("❌❌❌❌❌❌❌❌❌❌❌❌❌❌\t\t");
			}
			if(i%2 != 0) //put every two items on a line
				System.out.println("\n");
		}
	}
	/*Purpose: To check if user input for payment method (loyalty members only) is valid or not
	input: One string parameter, userPay
	output: Returns one string parameter, userPay
	*/
	public static String payment(String userPay) {
		Scanner payment = new Scanner(System.in);
		while(!(userPay.equalsIgnoreCase("cash")) && !(userPay.equalsIgnoreCase("credit"))) {
			System.out.println("How would you like to pay? \n↻ CASH \t\t💵 \n↻ CREDIT \t💳"); 
			userPay = payment.next();
		}
		return userPay; 
	}
	/*Purpose: To check if restocker restocks an item greater than 30 or less than 1 
	input: One int parameter, userInput 
	output: Returns one int parameter, userInput
	*/
	public static int restocker(int userInput) {
		Scanner restock = new Scanner(System.in);
		while(userInput > 30 || userInput < 5) {
			System.out.print("MUST BE UNDER 30 ITEMS, AND GREATER THAN 4. Input again: ");
			userInput = restock.nextInt();
		}
		return userInput; 
	}
	/* Purpose: To update the stock of items after a customer purchase
	*input: Array parameters String [] stockCode (stock code number), String [] expiry (stores expiry date), int [] down (thumbs down ratings), int [] up (thumbs up ratings), int [] cart (shopping cart) and int [] stock (stock of items)
	*output: n/a (exports back to textfile)
	*/ 
	public static void updateInv(String [] stockCode, String [] expiry, int [] down, int [] up, int [] cart, int [] stock) throws IOException {
		PrintWriter p2 = new PrintWriter("STOCK_DATE.txt");
		for(int i = 0; i < 16; i++) {
			stock[i] -= cart[i];
			p2.println(stockCode[i] + "-" + stock[i] + "-" + expiry[i] + "-" + down[i] + "-" + up[i]);
		} p2.close();
		
	}
	/*Purpose: To check for userCredit payment
	*input: N/A
	*output: N/A (message)
	*/
	public static void payCredit() {
		Scanner credit = new Scanner(System.in);
		int userPIN = 0; //stores userPin number
		System.out.print("Enter a 4-DIGIT PIN: ");
			userPIN = credit.nextInt(); 
			while(userPIN < 1000|| userPIN > 9999 || userPIN%7 != 0) {
				System.out.print("INVALID. Enter a 4-DIGIT PIN and must be divisible by 7: ");
				userPIN = credit.nextInt();
			}
			System.out.println("\t\t\t\t↻      .    Loading    .      ↻\n\t\t\t\t\t\t\tCONFIRMED");
	}
	/*Purpose: To check for userCash payment
	*input: Two parameters, double cost (final cost), int [] cart (shopping cart)
	*output: N/A
	*/
	public static void payCash(double cost, int [] cart) {
		Scanner cash = new Scanner(System.in);
		String userInput; //check for userInput to see if they want to cancel or not
		boolean payment = false; //if payment is completed 
		double money = 0; //stores the money input by the user
			while(payment == false) {
				System.out.print("Enter CASH 💵: ");
				money = cash.nextDouble();
				if(money >= cost)  {
					System.out.println("CHANGE 💸: " + (money - cost));
					payment = true; 
				} 
				else{
					System.out.print("Not enough cash, would you like to cancel?: "); 
					userInput = cash.next();
					userInput = valid(userInput); //check for valid input
					if(userInput.equalsIgnoreCase("y")) {
						for(int i = 0; i < 16; i++) {
							cart[i] = 0; 
						}
						System.out.println("\nAwh, we hope you can purchase something else next time! 💞"); 
						payment = true;

					} 
				} 
			}
	}
	
	/*Purpose: To check user input 
	*input: One string parameter, userInput
	*output: Returns one string, userInput
	*/
	public static String valid(String yes_no) {
		Scanner validity = new Scanner(System.in);
		while(!(yes_no.equalsIgnoreCase("y")) && !(yes_no.equalsIgnoreCase("n"))) {
			System.out.print("INVALID, must be y or n (yes or no): ");
			yes_no = validity.next();
		} 
		return yes_no; 
	}
	
	/*Purpose: To generate a divider
	*input: One string parameter, emoji which is an emoji or a short string
	*output: N/A (message)
	*/
	public static void divider(String emoji) {
		System.out.println("• . ° .˚ · • . ° .˚ · • . °" + emoji+ "˚ · • . ° .˚ · • . ° .˚ · •");
	}
	
	/*Purpose: To generate a random message for the user 
	*input: N/A
	*output: N/A (printed message)
	*/ 
	public static void message() {
		String [] word1 = {"Thank you", "Please accept our greatest gratitude,", "Thanks"}; //stores the first few words of random message
		String [] word2 = {" customer ", " buyer", " purchaser"}; //middle words 
		String [] word3 = {" at the vendor shop!", " at the vending machine.", ". Enjoy your day"}; //ending words
		//Generate a random number from 1-2
		String word_1 = word1[(int)(Math.random()*(2-0+1)+0)]; //store randomized word in a string
		String word_2 = word2[(int)(Math.random()*(2-0+1)+0)];
		String word_3 = word3[(int)(Math.random()*(2-0+1)+0)];
		//Print out randomized message 
		System.out.println(word_1 + word_2 + " for shopping" + word_3);
	}
	
	/*Purpose: To generate a random discount code for the user
	*input: N/A
	*output: Returns a string, discountCode which contains the discount code or noCode, saying no discount code */
	public static String generateCode() {
		int ranNum = (int)(Math.random()*(3-1+1)+1);  //generate random number
		String discountCode, noCode = "Aw, you received no code : ("; //discount code stores discount code, noCode stores nocode
		if(ranNum == 1 || ranNum == 2) {
			if(ranNum == 1)
				discountCode = "SAVE10";
			else
				discountCode = "SAVE30";
			return discountCode; 
		}
		else
			return noCode; 
		
	}

	/*Purpose: To ask the user for their loyalty information 
	*input: One arrayList, loyaltyProgram which stores all the usernames 
	*output: Returns one array, userInfo with the information of the user 
	*/
	public static String[] checkPass(ArrayList<String> loyaltyProgram) {
		Scanner loyal = new Scanner(System.in);
		String [] userInfo = new String [6]; //stores user info 
		int counter = 0; //counter for the number of tries to enter password/username
		int element = 0; //stores index number 
		String password, username; //userInput for password and username
		while(counter < 5) {
			System.out.print("Please enter your username (tries = " + (5-counter) + "): ");
			username = loyal.next();
			//For loop to find username 
			for(int i = 0; i < loyaltyProgram.size();i++) {
				if((loyaltyProgram.get(i).split("-")[0]).equals(username)) {	
					element = i; //store the element 
					i = loyaltyProgram.size();
					counter = 6; //end the loop 
				}
			}
			counter++;
		}
		if(counter == 5) 
			userInfo[0] = "false";
		//If username is correct, then check for the password
		else {
			counter = 0; 
			while(counter < 3) {
				System.out.print("Please enter your password (tries = " + (3-counter) + "): ");
				password = loyal.next();
				if((loyaltyProgram.get(element).split("-")[1]).equals(password)) {
					counter = 4; //exit the while loop if password is found 
				}
			counter++;
			}
			if(counter == 3)
				userInfo[0] = "false";
			else {
				userInfo[0] = "true";
				for(int i = 1; i < 5; i++) {
					userInfo[i] = (loyaltyProgram.get(element).split("-")[i-1]);
				}
				userInfo[5] = Integer.toString(element); //store the element so user information can be found in the textfile by using the element
			}
		}
		return userInfo;
	} 
} //end of class 