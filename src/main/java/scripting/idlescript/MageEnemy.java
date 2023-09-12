package scripting.idlescript;

import orsc.ORSCharacter;

public class MageEnemy extends IdleScript {
  public int start(String param[]) {
    int originalY = controller.currentY();
    int originalX = controller.currentX();
    int offset = 0;
    String[] res = param[0].split("[,]", 0);
    int enemyID = Integer.parseInt(res[0]);
    int spellId = controller.getSpellIdFromName("Fire bolt");
    try {
      offset = Integer.parseInt(res[1]);
      System.out.println(offset);
    } catch (Exception e) {
      offset = 0;
    }
    while (controller.isRunning()) {
      ORSCharacter npc = controller.getNearestNpcById(enemyID, false);
      if (npc != null) {
        controller.castSpellOnNpc(npc.serverIndex, spellId);
      }
      if (Math.abs(controller.currentX() - originalX) > offset
          || Math.abs(controller.currentY() - originalY) > offset) {
        controller.walkTo(originalX, originalY);
      }
      controller.sleep(500);
    }
    return 0;
  }
}
