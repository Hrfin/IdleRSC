package scripting.idlescript;

import orsc.ORSCharacter;

/**
 * - Christmas Cracker Opener.
 *
 * <p>Opens Christmas Crackers by using them on a 2nd account. Used in conjunction with K_GiftTaker
 * Script!
 *
 * <p>Should work in any bank, Ideal bank is Draynor. requires 2 accounts. This bot is the present
 * "taker", it will bank when you have 29 items.
 *
 * <p>To setup start both accounts near each other with NO items in either inventory. start the
 * taker bot FIRST before even starting giver bot. the bots will need to be synced up similar to
 * trader bots. ideally monitor them, if something goes wrong present stuff will drop to the floor
 * and despawn!!!!! you have been warned!
 *
 * <p>WARNING: while within 1 tile of the giver, you will continue to recieve presents. WARNING:
 * regardless of how full your inventory is. items WILL drop to the floor.
 *
 * <p>Author - Kaila.
 */
public class K_CrackerGiver extends IdleScript {

  long startTimestamp = System.currentTimeMillis() / 1000L;

  public int start(String parameters[]) {
    controller.displayMessage("@red@present GIVER! Let's party like it's 2004!");
    controller.displayMessage("@red@Directions inside K_CrackerGiver.java file");
    controller.displayMessage("@red@Ideal location is Draynor Bank!");

    while (controller.isRunning()) {
      if (controller.getInventoryItemCount() > 29) {
        controller.setStatus("@gre@Banking.");
        if (!controller.isInBank()) {
          int[] bankerIds = {95, 224, 268, 540, 617, 792};
          ORSCharacter npc = controller.getNearestNpcByIds(bankerIds, false);
          if (npc != null) {
            controller.setStatus("@yel@Walking to Banker..");
            controller.displayMessage("@yel@Walking to Banker..");
            controller.walktoNPCAsync(npc.serverIndex);
            controller.sleep(200);
          } else {
            controller.log("@red@Error..");
            controller.sleep(1000);
          }
        }
        bank();
      }
      if (controller.getInventoryItemCount(575) < 2) {
        controller.setStatus("@gre@Banking.");
        if (!controller.isInBank()) {
          int[] bankerIds = {95, 224, 268, 540, 617, 792};
          ORSCharacter npc = controller.getNearestNpcByIds(bankerIds, false);
          if (npc != null) {
            controller.setStatus("@yel@Walking to Banker..");
            controller.displayMessage("@yel@Walking to Banker..");
            controller.walktoNPCAsync(npc.serverIndex);
            controller.sleep(200);
          } else {
            controller.log("@red@Error..");
            controller.sleep(1000);
          }
        }
        bank();
      }
      if (controller.getInventoryItemCount() < 30 && controller.getInventoryItemCount(575) > 1) {
        controller.setStatus("@gre@Opening.");
        controller.useItemOnPlayer(
            1, controller.getPlayerServerIndexByName("kailashu")); // replace the player name
        controller.sleep(640);
      }
    }

    return 1000; // start() must return a int value now.
  }

  public void bank() {

    controller.setStatus("@yel@Banking..");
    controller.openBank();
    controller.sleep(640);

    if (controller.isInBank()) {
      if (controller.getInventoryItemCount() > 0) {
        for (int itemId : controller.getInventoryItemIds()) {
          controller.depositItem(itemId, controller.getInventoryItemCount(itemId));
        }
        controller.sleep(1280);
      }
      if (controller.getInventoryItemCount(575) < 23) {
        controller.withdrawItem(575, 23 - controller.getInventoryItemCount(575));
        controller.sleep(1280);
        controller.closeBank();
        controller.sleep(1280);
      }
    }
  }
}
