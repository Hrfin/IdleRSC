package scripting.idlescript;

import bot.Main;
import controller.Controller;

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

  private static final Controller c = Main.getController();

  private static long startTime = System.currentTimeMillis();
  private static long next_attempt = System.currentTimeMillis() + 5000L;
  private static long nineMinutesInMillis = 540000L;
  private static boolean makeFire = false;
  private static boolean hasAxe = false;

  private static int initialX, initialY;

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
    c.setStatus("Checking for Required Items...");

    initialX = c.currentX();
    initialY = c.currentY();
    for (int axe : ids_axe) {
      if (c.isItemInInventory(axe)) {
        hasAxe = true;
      }
    }
    if (!hasAxe) {
      c.setStatus("No Axe!");
      c.stop();
    }
    if (!c.isItemInInventory(ID_NET)) {
      c.setStatus("No Net!");
      c.stop();
    }
    while (c.isRunning()) {
      if (c.getInventoryItemCount() < 30) {
        fish();
      } else if (!onlyFishing) {
        cook();
      } else {
        fish();
        checkAutowalk();
      }
      c.sleep(1280);
    }
    return 1;
  }

  public static void fish() {
    if (!c.isBatching()) {
      c.setStatus("Fishing...");
      int[] coords = c.getNearestObjectById(ID_FISHING_SPOT);
      c.walkTo(coords[0], coords[1], 1, false);
      c.atObject(coords[0], coords[1]);
    }
  }

  public static void cook() {
    while (getRawCount() > 0 && c.isRunning()) {
      int[] fire = c.getNearestObjectById(ID_FIRE);
      // is there a fire?
      makeFire = (fire == null) ? true : false;
      if (makeFire) {
        // if there's no fire
        c.setStatus("Making Fire..");
        int[] tree = c.getNearestObjectById(ID_TREE);
        int[] log = c.getNearestItemById(ID_LOGS);
        // Logs will automatically drop on the ground since full inventory
        // If another bot hasn't lit a fire, or there's not a log on the ground
        while (log == null && fire == null && c.isRunning()) {
          // if not already cutting a tree, cut tree.
          if (!c.isBatching()) {
            tree = c.getNearestObjectById(ID_TREE);
            if (tree != null) c.walkTo(tree[0], tree[1], 1, false);
            c.atObject(tree[0], tree[1]);
          }
          c.sleep(500);
          log = c.getNearestItemById(ID_LOGS);
          fire = c.getNearestObjectById(ID_FIRE);
        }
        if (log != null) c.walkTo(log[0], log[1]);
        while (fire == null && log != null && c.isRunning()) {
          c.setStatus("Searching for Log..");
          if (!c.isBatching()) {
            c.setStatus("Trying to Light..");
            int index = c.getInventoryItemSlotIndex(ID_TINDERBOX);
            c.useItemOnGroundItem(log[0], log[1], ID_TINDERBOX, ID_LOGS);
            c.sleep(1280);
          }
          c.sleep(500);
          log = c.getNearestItemById(ID_LOGS);
          fire = c.getNearestObjectById(ID_FIRE);
        }
      } else {
        // If there's a fire already
        c.setStatus("Cooking...");
        if (fire != null) c.walkTo(fire[0], fire[1], 1, false);
        while (fire != null && getRawCount() > 0 && c.isRunning()) {
          c.setStatus("Actually Looping cook");
          for (int fish : ids_raw) {
            if (c.isItemInInventory(fish) && !c.isBatching()) {
              c.useItemIdOnObject(fire[0], fire[1], fish);
            }
          }
          c.sleep(1280);
          fire = c.getNearestObjectById(ID_FIRE);
        }
      }
    }
    c.setStatus("Dropping..");
    int index = 0;
    while (c.isItemInInventory(ID_BURNED) && c.isRunning()) {
      index = c.getInventoryItemSlotIndex(ID_BURNED);
      c.dropItem(index);
      c.sleep(640);
    }
    for (int cooked : ids_cooked) {
      while (c.isItemInInventory(cooked) && c.isRunning()) {
        index = c.getInventoryItemSlotIndex(cooked);
        c.dropItem(index);
        c.sleep(640);
      }
    }
    c.walkTo(initialX, initialY);
  }

  private static void checkAutowalk() {
    if (System.currentTimeMillis() > next_attempt) {
      c.log("@red@Walking to Avoid Logging!");
      int x = c.currentX();
      int y = c.currentY();

      if (c.isReachable(x + 1, y, false)) c.walkTo(x + 1, y, 0, false);
      else if (c.isReachable(x - 1, y, false)) c.walkTo(x - 1, y, 0, false);
      else if (c.isReachable(x, y + 1, false)) c.walkTo(x, y + 1, 0, false);
      else if (c.isReachable(x, y - 1, false)) c.walkTo(x, y - 1, 0, false);
      c.sleep(1280);
      next_attempt = System.currentTimeMillis() + nineMinutesInMillis;
      long nextAttemptInSeconds = (next_attempt - System.currentTimeMillis()) / 1000L;
      c.log("Done Walking to not Log, Next attempt in " + nextAttemptInSeconds + " seconds!");
    }
  }

  public static int getRawCount() {
    int count = 0;
    for (int id : ids_raw) {
      count += c.getInventoryItemCount(id);
    }
    return count;
  }
}
