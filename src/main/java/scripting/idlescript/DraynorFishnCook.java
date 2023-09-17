package scripting.idlescript;

public class DraynorFishnCook extends IdleScript {

  private static final int ID_FISHING_SPOT = 193;
  private static final int ID_TINDERBOX = 166;
  private static final int ID_FIRE = 97;
  private static final int ID_LOGS = 14;
  private static final int ID_TREE = 1;
  private static final int ID_BURNED = 353;
  private static final int ID_NET = 376;
  private static final int[] ids_raw = {351, 349};
  private static final int[] ids_cooked = {352, 350};
  private static final int[] ids_axe = {12, 87, 88, 203, 204, 405};

  Boolean makeFire = false;
  Boolean hasAxe = false;

  int initialX, initialY;

  public int start(String parameters[]) {
    // If 1 is entered as a parameter then it'll only fish, no cooking.
    boolean onlyFishing = false;
    if (parameters[0] != null) {
      String[] res = parameters[0].split("[,]", 0);
      try {
        if (Integer.parseInt(res[0]) == 1) {
          onlyFishing = true;
          System.out.println("Only Fishing.");
        }
      } catch (NumberFormatException e) {
        System.out.println("put 1 in the parameters for fishing only mode");
      }
    }
    controller.setStatus("Checking for Required Items...");
    initialX = controller.currentX();
    initialY = controller.currentY();
    for (int axe : ids_axe) {
      if (controller.isItemInInventory(axe)) {
        hasAxe = true;
      }
    }
    if (!hasAxe) {
      controller.setStatus("No Axe!");
      controller.stop();
    }
    if (!controller.isItemInInventory(ID_NET)) {
      controller.setStatus("No Net!");
      controller.stop();
    }
    while (controller.isRunning()) {
      if (controller.getInventoryItemCount() < 30) {
        fish();
      } else if (!onlyFishing) {
        cook();
      } else {
        fish();
      }
      controller.sleep(1280);
    }
    return 1;
  }

  public void fish() {
    if (!controller.isBatching()) {
      controller.setStatus("Fishing...");
      int[] coords = controller.getNearestObjectById(ID_FISHING_SPOT);
      controller.walkTo(coords[0], coords[1], 1, false);
      controller.atObject(coords[0], coords[1]);
    }
  }

  public void cook() {
    while (getRawCount() > 0 && controller.isRunning()) {
      int[] fire = controller.getNearestObjectById(ID_FIRE);
      // is there a fire?
      makeFire = (fire == null) ? true : false;
      if (makeFire) {
        // if there's no fire
        controller.setStatus("Making Fire..");
        int[] tree = controller.getNearestObjectById(ID_TREE);
        int[] log = controller.getNearestItemById(ID_LOGS);
        // Logs will automatically drop on the ground since full inventory
        // If another bot hasn't lit a fire, or there's not a log on the ground
        while (log == null && fire == null && controller.isRunning()) {
          // if not already cutting a tree, cut tree.
          if (!controller.isBatching()) {
            tree = controller.getNearestObjectById(ID_TREE);
            if (tree != null) controller.walkTo(tree[0], tree[1], 1, false);
            controller.atObject(tree[0], tree[1]);
          }
          controller.sleep(500);
          log = controller.getNearestItemById(ID_LOGS);
          fire = controller.getNearestObjectById(ID_FIRE);
        }
        if (log != null) controller.walkTo(log[0], log[1]);
        while (fire == null && log != null && controller.isRunning()) {
          controller.setStatus("Searching for Log..");
          if (!controller.isBatching()) {
            controller.setStatus("Trying to Light..");
            int index = controller.getInventoryItemSlotIndex(ID_TINDERBOX);
            controller.useItemOnGroundItem(log[0], log[1], ID_TINDERBOX, ID_LOGS);
            controller.sleep(1280);
          }
          controller.sleep(500);
          log = controller.getNearestItemById(ID_LOGS);
          fire = controller.getNearestObjectById(ID_FIRE);
        }
      } else {
        // If there's a fire already
        controller.setStatus("Cooking...");
        if (fire != null) controller.walkTo(fire[0], fire[1], 1, false);
        while (fire != null && getRawCount() > 0 && controller.isRunning()) {
          controller.setStatus("Actually Looping cook");
          for (int fish : ids_raw) {
            if (controller.isItemInInventory(fish) && !controller.isBatching()) {
              controller.useItemIdOnObject(fire[0], fire[1], fish);
            }
          }
          controller.sleep(1280);
          fire = controller.getNearestObjectById(ID_FIRE);
        }
      }
    }
    controller.setStatus("Dropping..");
    int index = 0;
    while (controller.isItemInInventory(ID_BURNED) && controller.isRunning()) {
      index = controller.getInventoryItemSlotIndex(ID_BURNED);
      controller.dropItem(index);
      controller.sleep(640);
    }
    for (int cooked : ids_cooked) {
      while (controller.isItemInInventory(cooked) && controller.isRunning()) {
        index = controller.getInventoryItemSlotIndex(cooked);
        controller.dropItem(index);
        controller.sleep(640);
      }
    }
    controller.walkTo(initialX, initialY);
  }

  public int getRawCount() {
    int count = 0;
    for (int id : ids_raw) {
      count += controller.getInventoryItemCount(id);
    }
    return count;
  }
}
