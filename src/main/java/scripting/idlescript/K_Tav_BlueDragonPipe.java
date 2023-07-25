package scripting.idlescript;

import java.awt.GridLayout;
import javax.swing.*;
import orsc.ORSCharacter;

/**
 * Wildy Fire Giant Killer - By Kaila.
 *
 * <p>
 *
 * <p>Start in Fally west with gear on, or in Dragon room!
 *
 * <p>Uses Coleslaw agility pipe shortcut.
 *
 * <p>70 Agility required, for the shortcut.
 *
 * <p>Sharks/Laws/Airs/Earths IN BANK REQUIRED.
 *
 * <p>31 Magic Required for escape tele.
 *
 * <p>Adjustable Food Withdraw amount.
 *
 * <p>@Author - Kaila
 */
public final class K_Tav_BlueDragonPipe extends K_kailaScript {
  private static int totalRdagger = 0;
  private static final int[] loot = {
    UNID_RANARR, // Grimy Ranarr Weed
    UNID_IRIT, // Grimy Irit
    UNID_AVANTOE, // Grimy Avantoe
    UNID_KWUARM, // Grimy Kwuarm
    UNID_CADA, // Grimy Cadantine
    UNID_DWARF, // Grimy Dwarf Weed
    NATURE_RUNE, // nature rune
    LAW_RUNE, // law rune
    FIRE_RUNE,
    WATER_RUNE,
    814, // D Bones
    396, // rune dagger
    154, // Addy Ore
    795, // D med
    UNCUT_SAPP, // saph
    UNCUT_EMER, // emerald
    UNCUT_RUBY, // ruby
    UNCUT_DIA, // diamond
    TOOTH_HALF, // tooth half
    LOOP_HALF, // loop half
    LEFT_HALF, // shield (left) half
    RUNE_SPEAR // rune spear
  };
  // STARTing script
  public int start(String[] parameters) {
    centerX = 361;
    centerY = 3353; // FURTHEST LOOT is 376, 3368, go 361, 3353  (15 tiles)
    centerDistance = 15;
    if (!parameters[0].equals("")) {
      try {
        foodWithdrawAmount = Integer.parseInt(parameters[0]);
        foodId = Integer.parseInt(parameters[1]);
        fightMode = Integer.parseInt(parameters[2]);
        potUp = Boolean.parseBoolean(parameters[3]);
      } catch (Exception e) {
        System.out.println("Could not parse parameters!");
        c.displayMessage("@red@Could not parse parameters!");
        c.stop();
      }
    }
    if (!guiSetup) {
      setupGUI();
      guiSetup = true;
    }
    if (scriptStarted) {
      guiSetup = false;
      scriptStarted = false;
      c.displayMessage("@red@Tavelry Blue Dragons (Pipe) - By Kaila");
      c.displayMessage("@red@Start in Fally west with gear on, or in dragon room!");
      c.displayMessage("@red@Sharks, Law, Water, Air IN BANK REQUIRED");
      c.displayMessage("@red@70 Agility required, for the shortcut!");
      if (c.isInBank()) {
        c.closeBank();
      }
      if (c.currentY() < 2800) {
        bank();
        BankToDragons();
        c.sleep(1380);
      }
      scriptStart();
    }
    return 1000; // start() must return an int value now.
  }
  // Main Script section
  private void scriptStart() {
    while (c.isRunning()) {
      boolean ate = eatFood();
      if (!ate) {
        c.setStatus("@red@We've ran out of Food! Running Away!.");
        pipeEscape();
        DragonsToBank();
        bank();
        BankToDragons();
      }
      if (potUp) {
        superAttackBoost(2, false);
        superStrengthBoost(2, false);
      }
      checkFightMode();
      lootItems(false, loot);
      if (c.getInventoryItemCount(foodId) > 0 && c.getInventoryItemCount() < 30) {
        if (!c.isInCombat()) {
          ORSCharacter npc = c.getNearestNpcById(202, false);
          if (npc != null) {
            c.setStatus("@yel@Attacking Dragons");
            c.attackNpc(npc.serverIndex);
          } else {
            c.sleep(GAME_TICK);
            lootItems(false, loot);
            if (buryBones) buryBones(false);
            if (potUp) {
              superAttackBoost(2, false);
              superStrengthBoost(2, false);
            }
            walkToCenter();
          }
        }
      }
      if (c.getInventoryItemCount() == 30) {
        dropItemToLoot(false, 1, EMPTY_VIAL);
        if (buryBones) buryBonesToLoot(false);
        eatFoodToLoot(false);
      }
      if (c.getInventoryItemCount(foodId) == 0 || timeToBank || timeToBankStay) {
        pipeEscape();
        c.setStatus("@yel@Banking..");
        timeToBank = false;
        DragonsToBank();
        bank();
        if (timeToBankStay) {
          timeToBankStay = false;
          c.displayMessage(
              "@red@Click on Start Button Again@or1@, to resume the script where it left off (preserving statistics)");
          c.setStatus("@red@Stopping Script.");
          endSession();
        }
        BankToDragons();
      }
    }
  }

  private void walkToCenter() {
    if (c.currentX() != 370 || c.currentY() != 3353) {
      c.walkTo(370, 3353);
      c.sleep(1000);
    }
  }

  private void bank() {
    c.setStatus("@yel@Banking..");
    c.openBank();
    c.sleep(1200);
    if (c.isInBank()) {
      totalBones = totalBones + c.getInventoryItemCount(814);
      totalRdagger = totalRdagger + c.getInventoryItemCount(396);
      totalGems =
          totalGems
              + c.getInventoryItemCount(160)
              + c.getInventoryItemCount(159)
              + c.getInventoryItemCount(158)
              + c.getInventoryItemCount(157);
      totalHerb =
          totalHerb
              + c.getInventoryItemCount(438)
              + c.getInventoryItemCount(439)
              + c.getInventoryItemCount(440)
              + c.getInventoryItemCount(441)
              + c.getInventoryItemCount(442)
              + c.getInventoryItemCount(443);
      totalFire = totalFire + c.getInventoryItemCount(31);
      totalLaw = totalLaw + c.getInventoryItemCount(42);
      totalNat = totalNat + c.getInventoryItemCount(40);
      totalWater = totalWater + c.getInventoryItemCount(32);
      totalAddy = totalAddy + c.getInventoryItemCount(154);
      totalLoop = totalLoop + c.getInventoryItemCount(527);
      totalTooth = totalTooth + c.getInventoryItemCount(526);
      totalLeft = totalLeft + c.getInventoryItemCount(1277);
      totalSpear = totalSpear + c.getInventoryItemCount(1092);

      for (int itemId : c.getInventoryItemIds()) {
        if (itemId != 486
            && itemId != 487
            && itemId != 488
            && itemId != 492
            && itemId != 493
            && itemId != 494) {
          c.depositItem(itemId, c.getInventoryItemCount(itemId));
        }
      }
      bankBones = c.getBankItemCount(814);
      c.sleep(1240); // Important, leave in

      if (potUp) {
        withdrawSuperAttack(1);
        withdrawSuperStrength(1);
      }
      withdrawItem(airId, 18);
      withdrawItem(lawId, 6);
      withdrawItem(waterId, 6);
      withdrawFood(foodId, foodWithdrawAmount);
      bankItemCheck(foodId, 30);
      bankItemCheck(airId, 30);
      bankItemCheck(waterId, 10); // Falador teleport
      bankItemCheck(lawId, 10);
      bankCheckAntiDragonShield();
      c.closeBank();
      c.sleep(1000);
    }
    inventoryItemCheck(airId, 18);
    inventoryItemCheck(waterId, 6);
    inventoryItemCheck(lawId, 6);
  }
  // PATHING private voids
  private void BankToDragons() {
    c.setStatus("@gre@Walking to Tav Gate..");
    c.walkTo(327, 552);
    c.walkTo(324, 549);
    c.walkTo(324, 539);
    c.walkTo(324, 530);
    c.walkTo(317, 523);
    c.walkTo(317, 516);
    c.walkTo(327, 506);
    c.walkTo(337, 496);
    c.walkTo(337, 492);
    c.walkTo(341, 488);
    tavGateEastToWest();
    c.setStatus("@gre@Walking to Tav Dungeon Ladder..");
    c.walkTo(342, 493);
    c.walkTo(350, 501);
    c.walkTo(355, 506);
    c.walkTo(360, 511);
    c.walkTo(362, 513);
    c.walkTo(367, 514);
    c.walkTo(374, 521);
    c.walkTo(376, 521);
    c.atObject(376, 520);
    c.sleep(640);
    c.walkTo(375, 3352);
    c.atObject(374, 3352);
    c.sleep(640);
    c.walkTo(372, 3352);
    c.sleep(320);
    c.setStatus("@gre@Done Walking..");
  }

  private void pipeEscape() {
    c.setStatus("We've ran out of Food! @gre@Going through Pipe.");
    c.walkTo(372, 3352);
    c.atObject(373, 3352);
    c.sleep(1000);
  }

  private void DragonsToBank() {
    teleportOutFalador();
    totalTrips = totalTrips + 1;
    c.sleep(308);
    c.walkTo(327, 552);
    c.sleep(308);
    c.setStatus("@gre@Done Walking..");
  }
  // GUI stuff below (icky)
  private void setupGUI() {
    JLabel header = new JLabel("Tavelry Blue Dragons (Pipe) - By Kaila");
    JLabel label1 = new JLabel("Start in Fally west with gear on, or in Dragon room!");
    JLabel label2 = new JLabel("Sharks, Law, Water, Air IN BANK required");
    JLabel label3 = new JLabel("70 Agility required, for the shortcut!");
    JLabel label4 = new JLabel("Chat commands can be used to direct the bot");
    JLabel label5 = new JLabel("::bank ::bankstay ::burybones");
    JLabel label6 = new JLabel("Styles ::attack :strength ::defense ::controlled");
    JCheckBox buryBonesCheckbox = new JCheckBox("Bury Dragon Bones?", false);
    JCheckBox potUpCheckbox = new JCheckBox("Use super Atk/Str Pots?", true);
    JLabel fightModeLabel = new JLabel("Fight Mode:");
    JComboBox<String> fightModeField =
        new JComboBox<>(new String[] {"Controlled", "Aggressive", "Accurate", "Defensive"});
    fightModeField.setSelectedIndex(0); // sets default to controlled
    JLabel foodLabel = new JLabel("Type of Food:");
    JComboBox<String> foodField = new JComboBox<>(foodTypes);
    foodField.setSelectedIndex(2); // sets default to sharks
    JLabel foodWithdrawAmountLabel = new JLabel("Food Withdraw amount:");
    JTextField foodWithdrawAmountField = new JTextField(String.valueOf(16));
    JButton startScriptButton = new JButton("Start");

    startScriptButton.addActionListener(
        e -> {
          if (!foodWithdrawAmountField.getText().equals("")) {
            foodWithdrawAmount = Integer.parseInt(foodWithdrawAmountField.getText());
          } else {
            foodWithdrawAmount = 22;
          }
          buryBones = buryBonesCheckbox.isSelected();
          fightMode = fightModeField.getSelectedIndex();
          foodId = foodIds[foodField.getSelectedIndex()];
          potUp = potUpCheckbox.isSelected();
          scriptFrame.setVisible(false);
          scriptFrame.dispose();
          startTime = System.currentTimeMillis();
          scriptStarted = true;
        });

    scriptFrame = new JFrame(c.getPlayerName() + " - options");

    scriptFrame.setLayout(new GridLayout(0, 1));
    scriptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    scriptFrame.add(header);
    scriptFrame.add(label1);
    scriptFrame.add(label2);
    scriptFrame.add(label3);
    scriptFrame.add(label4);
    scriptFrame.add(label5);
    scriptFrame.add(label6);
    scriptFrame.add(buryBonesCheckbox);
    scriptFrame.add(potUpCheckbox);
    scriptFrame.add(fightModeLabel);
    scriptFrame.add(fightModeField);
    scriptFrame.add(foodLabel);
    scriptFrame.add(foodField);
    scriptFrame.add(foodWithdrawAmountLabel);
    scriptFrame.add(foodWithdrawAmountField);
    scriptFrame.add(startScriptButton);
    scriptFrame.pack();
    scriptFrame.setLocationRelativeTo(null);
    scriptFrame.setVisible(true);
    scriptFrame.requestFocusInWindow();
  }

  @Override
  public void chatCommandInterrupt(String commandText) { // ::bank ::lowlevel :potup ::prayer
    if (commandText.contains("bank")) {
      c.displayMessage("@or1@Got @red@bank@or1@ command! Going to the Bank!");
      timeToBank = true;
      c.sleep(100);
    } else if (commandText.contains("bankstay")) {
      c.displayMessage("@or1@Got @red@bankstay@or1@ command! Going to the Bank and Staying!");
      timeToBankStay = true;
      c.sleep(100);
    } else if (commandText.contains("burybones")) {
      if (!buryBones) {
        c.displayMessage("@or1@Got toggle @red@bones@or1@, turning on bone bury!");
        buryBones = true;
      } else {
        c.displayMessage("@or1@Got toggle @red@buryBones@or1@, turning off bone bury!");
        buryBones = false;
      }
      c.sleep(100);
    } else if (commandText.contains("potup")) {
      if (!potUp) {
        c.displayMessage("@or1@Got toggle @red@potup@or1@, turning on regular atk/str pots!");
        potUp = true;
      } else {
        c.displayMessage("@or1@Got toggle @red@potup@or1@, turning off regular atk/str pots!");
        potUp = false;
      }
      c.sleep(100);
    } else if (commandText.contains(
        "attack")) { // field is "Controlled", "Aggressive", "Accurate", "Defensive"}
      c.displayMessage("@red@Got Combat Style Command! - Attack Xp");
      c.displayMessage("@red@Switching to \"Accurate\" combat style!");
      fightMode = 2;
      c.sleep(100);
    } else if (commandText.contains("strength")) {
      c.displayMessage("@red@Got Combat Style Command! - Strength Xp");
      c.displayMessage("@red@Switching to \"Aggressive\" combat style!");
      fightMode = 1;
      c.sleep(100);
    } else if (commandText.contains("defense")) {
      c.displayMessage("@red@Got Combat Style Command! - Defense Xp");
      c.displayMessage("@red@Switching to \"Defensive\" combat style!");
      fightMode = 3;
      c.sleep(100);
    } else if (commandText.contains("controlled")) {
      c.displayMessage("@red@Got Combat Style Command! - Controlled Xp");
      c.displayMessage("@red@Switching to \"Controlled\" combat style!");
      fightMode = 0;
      c.sleep(100);
    }
  }

  @Override
  public void paintInterrupt() {
    if (c != null) {
      String runTime = c.msToString(System.currentTimeMillis() - startTime);
      int DbonesSuccessPerHr = 0;
      int RdaggerSuccessPerHr = 0;
      int GemsSuccessPerHr = 0;
      int FireSuccessPerHr = 0;
      int LawSuccessPerHr = 0;
      int NatSuccessPerHr = 0;
      int WaterSuccessPerHr = 0;
      int AddySuccessPerHr = 0;
      int HerbSuccessPerHr = 0;
      int TripSuccessPerHr = 0;
      long currentTimeInSeconds = System.currentTimeMillis() / 1000L;
      try {
        float timeRan = currentTimeInSeconds - startTimestamp;
        float scale = (60 * 60) / timeRan;
        DbonesSuccessPerHr = (int) (totalBones * scale);
        RdaggerSuccessPerHr = (int) (totalRdagger * scale);
        GemsSuccessPerHr = (int) (totalGems * scale);
        FireSuccessPerHr = (int) (totalFire * scale);
        LawSuccessPerHr = (int) (totalLaw * scale);
        NatSuccessPerHr = (int) (totalNat * scale);
        WaterSuccessPerHr = (int) (totalWater * scale);
        AddySuccessPerHr = (int) (totalAddy * scale);
        HerbSuccessPerHr = (int) (totalHerb * scale);
        TripSuccessPerHr = (int) (totalTrips * scale);
      } catch (Exception e) {
        // divide by zero
      }
      int x = 6;
      int y = 15;
      c.drawString("@red@Tavelry Blue Dragons @gre@by Kaila", x, y - 3, 0xFFFFFF, 1);
      c.drawString("@whi@______________________", x, y, 0xFFFFFF, 1);
      c.drawString(
          "@whi@Gathered D.Bones: @gre@"
              + totalBones
              + "@yel@ (@whi@"
              + String.format("%,d", DbonesSuccessPerHr)
              + "@yel@/@whi@hr@yel@) "
              + "@whi@D.Bones in Bank: @gre@"
              + bankBones,
          x,
          y + 14,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@R. Dagger: @gre@"
              + totalRdagger
              + "@yel@ (@whi@"
              + String.format("%,d", RdaggerSuccessPerHr)
              + "@yel@/@whi@hr@yel@) "
              + "@whi@Addy Plate: @gre@"
              + totalAddy
              + "@yel@ (@whi@"
              + String.format("%,d", AddySuccessPerHr)
              + "@yel@/@whi@hr@yel@) "
              + "@whi@Addy Ore: @gre@"
              + totalAddy
              + "@yel@ (@whi@"
              + String.format("%,d", AddySuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          x,
          y + (14 * 2),
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Laws: @gre@"
              + totalLaw
              + "@yel@ (@whi@"
              + String.format("%,d", LawSuccessPerHr)
              + "@yel@/@whi@hr@yel@) "
              + "@whi@Natures: @gre@"
              + totalNat
              + "@yel@ (@whi@"
              + String.format("%,d", NatSuccessPerHr)
              + "@yel@/@whi@hr@yel@) "
              + "@whi@Waters: @gre@"
              + totalWater
              + "@yel@ (@whi@"
              + String.format("%,d", WaterSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          x,
          y + (14 * 3),
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Total Herbs: @gre@"
              + totalHerb
              + "@yel@ (@whi@"
              + String.format("%,d", HerbSuccessPerHr)
              + "@yel@/@whi@hr@yel@) "
              + "@whi@Total Gems: @gre@"
              + totalGems
              + "@yel@ (@whi@"
              + String.format("%,d", GemsSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          x,
          y + (14 * 4),
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Tooth: @gre@"
              + totalTooth
              + "@yel@ / @whi@Loop: @gre@"
              + totalLoop
              + "@yel@ / @whi@Fires: @gre@"
              + totalFire
              + "@yel@ (@whi@"
              + String.format("%,d", FireSuccessPerHr)
              + "@yel@/@whi@hr@yel@) ",
          x,
          y + (14 * 5),
          0xFFFFFF,
          1);

      c.drawString(
          "@whi@Left Half: @gre@" + totalLeft + "@yel@ / @whi@Rune Spear: @gre@" + totalSpear,
          x,
          y + (14 * 6),
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Total Trips: @gre@"
              + totalTrips
              + "@yel@ (@whi@"
              + String.format("%,d", TripSuccessPerHr)
              + "@yel@/@whi@hr@yel@) "
              + "@whi@Runtime: "
              + runTime,
          x,
          y + (14 * 7),
          0xFFFFFF,
          1);
      c.drawString("@whi@______________________", x, y + 3 + (14 * 7), 0xFFFFFF, 1);
    }
  }
}
