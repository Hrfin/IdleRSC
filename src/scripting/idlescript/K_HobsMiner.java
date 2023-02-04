package scripting.idlescript;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import orsc.ORSCharacter;
import scripting.idlescript.AIOCooker.FoodObject;

/**
 * Grabs red spider eggs in edge dungeon, recommend very high stats ~90+
 * 
 * 
 * 
 * 
 * Author - Kaila
 */
public class K_HobsMiner extends IdleScript {	
	JFrame scriptFrame = null;
	boolean guiSetup = false;
	boolean scriptStarted = false;
	int coalInBank = 0;
	int mithInBank = 0;
	int addyInBank = 0;
	int totalCoal = 0;
	int totalMith = 0;
	int totalAddy = 0;
	int totalSap = 0;
	int totalEme = 0;
	int totalRub = 0;
	int totalDia = 0;
    int totalTrips = 0;

	Integer currentOre[] = {0,0};
	int addyIDs[] = {108,231,109}; //108,231,109 (addy) 106,107 (mith) 110,111 (coal)  98 empty
	int mithIDs[] = {106,107};
	int coalIDs[] = {110,111};
	int oreIDs[] = {409,154,153,155};
	int gemIDs[] = {157,158,159,160}; 
	String isMining = "none";
	
	long startTime;
	long startTimestamp = System.currentTimeMillis() / 1000L;
	
		
		public int start(String parameters[]) {
			if (!guiSetup) {
				setupGUI();
				guiSetup = true;
			}
			if (scriptStarted) {
				controller.displayMessage("@red@Hobs Miner- By Kaila");
				controller.displayMessage("@red@Start in Edge bank with Armor and pickaxe");
				controller.displayMessage("@red@Sharks/Laws/Airs/Earths IN BANK REQUIRED");
				controller.displayMessage("@red@31 Magic Required for escape tele");
				if(controller.isInBank() == true) {
					controller.closeBank();
				}
				if(controller.currentY() > 340) {
					bank();
					eat();
					bankToHobs();
					eat();
					controller.sleep(1380);
				}
				scriptStart();
			}
			return 1000; //start() must return a int value now. 
		}
		
		
		public void scriptStart() {
			while(controller.isRunning()) {
				if(controller.getInventoryItemCount(546) == 0) {
					controller.setStatus("@red@We've ran out of Food! Teleporting Away!.");
					hobsToTwenty();
					controller.sleep(100);
					controller.castSpellOnSelf(controller.getSpellIdFromName("Lumbridge Teleport"));
					controller.sleep(308);
					controller.walkTo(120,644);
					controller.atObject(119,642);
					controller.walkTo(217,447);
					controller.sleep(618);
					bank();
					bankToHobs();
				}
				if (controller.getInventoryItemCount() == 30) {
					
					goToBank();
					
				} else {	
				
				eat();
				
				while(controller.isInCombat()) {
					controller.setStatus("@red@Leaving combat..");
					controller.walkTo(controller.currentX(), controller.currentY(), 0, true);
					controller.sleep(250);
				}
					
				if (rockEmpty() || !controller.isBatching()) {
					isMining = "none";
					currentOre[0] = 0;
					currentOre[1] = 0;
				}
				if (controller.isBatching()) {
					if (isMining == "mithril") {
						if (adamantiteAvailable()) {
							mine("adamantite");
						}
					}
					if (isMining == "coal") {
						if (adamantiteAvailable()) {
							mine("adamantite");
						} else if (mithrilAvailable()) {
							mine("mithril");
						}						
					}
					controller.sleep(1280);
				}
				
				controller.setStatus("@yel@Mining..");
				
				if (!controller.isBatching() && isMining == "none" && rockEmpty()) {
					if (adamantiteAvailable()) {
						mine("adamantite");
					} else if (mithrilAvailable()) {
						mine("mithril");
					} else if (coalAvailable()) {
						mine("coal");
					}
					controller.sleep(1280);
				}
			}


		}
	}

		
		public void mine(String i) {
			if (i == "adamantite") {
				int oreCoords[] = controller.getNearestObjectByIds(addyIDs);
				if (oreCoords != null) {
					isMining = "adamantite";
					controller.atObject(oreCoords[0], oreCoords[1]);
					currentOre[0] = oreCoords[0];
					currentOre[1] = oreCoords[1];
				}
			} else if (i == "mithril") {
				int oreCoords[] = controller.getNearestObjectByIds(mithIDs);
				if (oreCoords != null) {
					isMining = "mithril";
					controller.atObject(oreCoords[0], oreCoords[1]);
					currentOre[0] = oreCoords[0];
					currentOre[1] = oreCoords[1];
				}	
			} else if (i == "coal") {
				int oreCoords[] = controller.getNearestObjectByIds(coalIDs);
				if (oreCoords != null) {
					isMining = "coal";	
					controller.atObject(oreCoords[0], oreCoords[1]);
					currentOre[0] = oreCoords[0];
					currentOre[1] = oreCoords[1];
				}
			}
			controller.sleep(1920);
		}
		public boolean adamantiteAvailable() {		
		    return controller.getNearestObjectByIds(addyIDs) != null;
		}
		public boolean mithrilAvailable() {
		    return controller.getNearestObjectByIds(mithIDs) != null;
		}
		public boolean coalAvailable() {
		    return controller.getNearestObjectByIds(coalIDs) != null;
		}
		public boolean rockEmpty() {
			if (currentOre[0] != 0) {
				return controller.getObjectAtCoord(currentOre[0], currentOre[1]) == 98;
			} else {
				return true;
			}
		}
		
	
	public void bank() {
		
		controller.setStatus("@yel@Banking..");
		controller.openBank();
		
		while(controller.isInBank()){
			
			totalCoal = totalCoal + controller.getInventoryItemCount(155);
			totalMith = totalMith + controller.getInventoryItemCount(153);
			totalAddy = totalAddy + controller.getInventoryItemCount(154);
			totalSap = totalSap + controller.getInventoryItemCount(160);
			totalEme = totalEme + controller.getInventoryItemCount(159);
			totalRub = totalRub + controller.getInventoryItemCount(158);
			totalDia = totalDia + controller.getInventoryItemCount(157);
			
			if (controller.getInventoryItemCount() > 4) {
				for (int itemId : controller.getInventoryItemIds()) {
					if (itemId != 546) {
						controller.depositItem(itemId, controller.getInventoryItemCount(itemId));
					}
				}
				controller.sleep(1280);   // increased sleep here to prevent double banking
			}
			
			coalInBank = controller.getBankItemCount(155);
			mithInBank = controller.getBankItemCount(153);
			addyInBank = controller.getBankItemCount(154);
			
			if(controller.getInventoryItemCount(33) < 3) {  //withdraw 3 air
				controller.withdrawItem(33, 3);
				controller.sleep(340);
			}
			if(controller.getInventoryItemCount(34) < 1) {  //withdraw 1 earth
				controller.withdrawItem(34, 1);
				controller.sleep(340);
			}
			if(controller.getInventoryItemCount(42) < 1) {  //withdraw 1 law
				controller.withdrawItem(42, 1);
				controller.sleep(340);
			}
			if(controller.getInventoryItemCount(546) > 1) {  //deposit extra shark
				controller.depositItem(546, controller.getInventoryItemCount(546) - 1);
				controller.sleep(340);
			}
			if(controller.getInventoryItemCount(546) < 1) {  //withdraw 1 shark
				controller.withdrawItem(546, 1);
				controller.sleep(340);
			}
			if(controller.getBankItemCount(546) == 0) {
				controller.setStatus("@red@NO Sharks/Laws/Airs/Earths in the bank, Logging Out!.");
				controller.setAutoLogin(false);
				controller.logout();
				if(!controller.isLoggedIn()) {
					controller.stop();
					return;
				}
			}
			controller.closeBank();
			controller.sleep(640);
		}
	}
	
	public void eat() {
		
		int eatLvl = controller.getBaseStat(controller.getStatId("Hits")) - 20;
		
		if(controller.getCurrentStat(controller.getStatId("Hits")) < eatLvl) {
			
			while(controller.isInCombat()) {
				controller.setStatus("@red@Leaving combat..");
				controller.walkTo(controller.currentX(), controller.currentY(), 0, true);
				controller.sleep(250);
			}
			
		controller.setStatus("@red@Eating..");
		
		boolean ate = false;
		
		for(int id : controller.getFoodIds()) {
			if(controller.getInventoryItemCount(id) > 0) {
				controller.itemCommand(id);
				controller.sleep(700);
				ate = true;
				break;
			}
		}
		if(!ate) {  //only activates if hp goes to -20 again THAT trip, will bank and get new shark usually
			controller.setStatus("@red@We've ran out of Food! Teleporting Away!.");
			hobsToTwenty();
			controller.sleep(100);
	    	while(controller.currentY() < 425) {
	    		controller.castSpellOnSelf(controller.getSpellIdFromName("Lumbridge Teleport"));
	    		controller.sleep(308);
			}
			controller.walkTo(120,644);
			controller.atObject(119,642);
			controller.walkTo(217,447);
			controller.sleep(308);
			controller.setAutoLogin(false);
			controller.logout();
			controller.sleep(1000);
		
			if(!controller.isLoggedIn()) {
				controller.stop();
				controller.logout();
				return;
			}
		}
	}
}
	
	public void goToBank() {
		isMining = "none";
		currentOre[0] = 0;
		currentOre[1] = 0;	
		controller.setStatus("@yel@Banking..");
		hobsToTwenty();
		twentyToBank();
		bank();
		bankToHobs();
		controller.sleep(618);
	}
	
	public void hobsToTwenty() {
    	controller.setStatus("@gre@Walking to 19 wildy..");
		controller.walkTo(221,262);
		controller.walkTo(221,283);
		controller.walkTo(221,301);
		controller.walkTo(221,314);
		totalTrips = totalTrips + 1;
    	controller.setStatus("@gre@Done Walking to 19..");
	}
	
    public void twentyToBank() {	
    	controller.setStatus("@gre@Walking to Bank..");
		eat();
		controller.walkTo(221,321);
		controller.walkTo(222,341);
		controller.walkTo(222,361);
		controller.walkTo(222,381);
		controller.walkTo(222,401);
		controller.walkTo(215,410);
		controller.walkTo(215,420);
		controller.walkTo(220,425);
		controller.walkTo(220,445);
		controller.walkTo(217,448);
    	
    	
    	controller.setStatus("@gre@Done Walking..");
	}
    public void bankToHobs() {	
    	controller.setStatus("@gre@Walking to Hobs Mine..");
		controller.walkTo(220,443);
		controller.walkTo(220,422);//bad pathing here, fix
		controller.walkTo(220,425);//bad pathing here, fix
		controller.walkTo(215,420);
		controller.walkTo(215,410);
		controller.walkTo(215,401);
		controller.walkTo(215,395);
		eat();
		controller.walkTo(222,388);
		controller.walkTo(222,381);
		controller.walkTo(222,361);
		controller.walkTo(222,341);
		controller.walkTo(221,321);
		controller.walkTo(221,314);
		controller.walkTo(221,301);
		controller.walkTo(221,283);
		controller.walkTo(221,262);
		
    	controller.setStatus("@gre@Done Walking..");
	}

    
    
	//GUI stuff below (icky)
    
    
	
	public static void centerWindow(Window frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}
	public void setupGUI() {
		JLabel header = new JLabel("Hobs Miner - By Kaila");
		JLabel label1 = new JLabel("Start in Edge bank with Armor and Pickaxe");
		JLabel label2 = new JLabel("Sharks/Laws/Airs/Earths IN BANK REQUIRED");
		JLabel label3 = new JLabel("31 Magic Required for Escape Tele");
		JButton startScriptButton = new JButton("Start");

		startScriptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scriptFrame.setVisible(false);
				scriptFrame.dispose();
				startTime = System.currentTimeMillis();
				scriptStarted = true;
			}
		});
		
		scriptFrame = new JFrame("Script Options");

		scriptFrame.setLayout(new GridLayout(0, 1));
		scriptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		scriptFrame.add(header);
		scriptFrame.add(label1);
		scriptFrame.add(label2);
		scriptFrame.add(label3);
		scriptFrame.add(startScriptButton);
		centerWindow(scriptFrame);
		scriptFrame.setVisible(true);
		scriptFrame.pack();
		scriptFrame.requestFocus();

	}
	public static String msToString(long milliseconds) {
		long sec = milliseconds / 1000;
		long min = sec / 60;
		long hour = min / 60;
		sec %= 60;
		min %= 60;
		DecimalFormat twoDigits = new DecimalFormat("00");

		return new String(twoDigits.format(hour) + ":" + twoDigits.format(min) + ":" + twoDigits.format(sec));
	}
	@Override
	public void paintInterrupt() {
		if (controller != null) {
			
			String runTime = msToString(System.currentTimeMillis() - startTime);
	    	int coalSuccessPerHr = 0;
	    	int mithSuccessPerHr = 0;
	    	int addySuccessPerHr = 0;
	    	int sapSuccessPerHr = 0;
	    	int emeSuccessPerHr = 0;
	    	int rubSuccessPerHr = 0;
	    	int diaSuccessPerHr = 0;
    		int TripSuccessPerHr = 0;
    		
	    	try {
	    		float timeRan = (System.currentTimeMillis() / 1000L) - startTimestamp;
	    		float scale = (60 * 60) / timeRan;
	    		coalSuccessPerHr = (int)(totalCoal * scale);
	    		mithSuccessPerHr = (int)(totalMith * scale);
	    		addySuccessPerHr = (int)(totalAddy * scale);
	    		sapSuccessPerHr = (int)(totalSap * scale);
	    		emeSuccessPerHr = (int)(totalEme * scale);
	    		rubSuccessPerHr = (int)(totalRub * scale);
	    		diaSuccessPerHr = (int)(totalDia * scale);
	    		TripSuccessPerHr = (int)(totalTrips * scale);
	    		
	    	} catch(Exception e) {
	    		//divide by zero
	    	}
			controller.drawString("@red@Hobs Miner @gre@by Kaila", 350, 48, 0xFFFFFF, 1);
			controller.drawString("@whi@Coal Mined: @gre@" + String.valueOf(this.totalCoal) + "@yel@ (@whi@" + String.format("%,d", coalSuccessPerHr) + "@yel@/@whi@hr@yel@)", 370, 62, 0xFFFFFF, 1);
			controller.drawString("@whi@Mith Mined: @gre@" + String.valueOf(this.totalMith) + "@yel@ (@whi@" + String.format("%,d", mithSuccessPerHr) + "@yel@/@whi@hr@yel@)", 370, 76, 0xFFFFFF, 1);
			controller.drawString("@whi@Addy Mined: @gre@" + String.valueOf(this.totalAddy) + "@yel@ (@whi@" + String.format("%,d", addySuccessPerHr) + "@yel@/@whi@hr@yel@)", 370, 90, 0xFFFFFF, 1);
			controller.drawString("@whi@Sapphires: @gre@" + String.valueOf(this.totalSap) + "@yel@ (@whi@" + String.format("%,d", sapSuccessPerHr) + "@yel@/@whi@hr@yel@)", 370, 104, 0xFFFFFF, 1);
			controller.drawString("@whi@Emeralds: @gre@" + String.valueOf(this.totalEme) + "@yel@ (@whi@" + String.format("%,d", emeSuccessPerHr) + "@yel@/@whi@hr@yel@)", 370, 118, 0xFFFFFF, 1);
			controller.drawString("@whi@Rubys: @gre@" + String.valueOf(this.totalRub) + "@yel@ (@whi@" + String.format("%,d", rubSuccessPerHr) + "@yel@/@whi@hr@yel@)", 370, 132, 0xFFFFFF, 1);
			controller.drawString("@whi@Diamonds: @gre@" + String.valueOf(this.totalDia) + "@yel@ (@whi@" + String.format("%,d", diaSuccessPerHr) + "@yel@/@whi@hr@yel@)", 370, 146, 0xFFFFFF, 1);
			controller.drawString("@whi@Coal in Bank: @gre@" + String.valueOf(this.coalInBank), 370, 160, 0xFFFFFF, 1);
			controller.drawString("@whi@Mith in Bank: @gre@" + String.valueOf(this.mithInBank), 370, 174, 0xFFFFFF, 1);
			controller.drawString("@whi@Addy in Bank: @gre@" + String.valueOf(this.addyInBank), 370, 188, 0xFFFFFF, 1);
			controller.drawString("@whi@Total Trips: @gre@" + String.valueOf(this.totalTrips) + "@yel@ (@whi@" + String.format("%,d", TripSuccessPerHr) + "@yel@/@whi@hr@yel@)", 370, 202, 0xFFFFFF, 1);
			controller.drawString("@whi@Runtime: " + runTime, 370, 216, 0xFFFFFF, 1);
		}
	}
}