package scripting.idlescript;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import orsc.ORSCharacter;

public class MageEnemy extends IdleScript {

  public final JFrame scriptFrame = new JFrame("Script Options");

  public JTextField enemyIdField = new JTextField("528");
  public JTextField spellNameField = new JTextField("Fire bolt");
  public JTextField offsetField = new JTextField("0");

  public boolean guiSetup = false;
  public boolean scriptStarted = false;
  public int enemyId;
  public int spellId;
  public int offset;

  public int start(String param[]) {
    int originalY = controller.currentY();
    int originalX = controller.currentX();
    if (!guiSetup) {
      setupGUI();
      guiSetup = true;
    }
    if (!scriptStarted) {
      guiSetup = false;
      scriptStarted = false;
      while (controller.isRunning()) {
        ORSCharacter npc = controller.getNearestNpcById(enemyId, false);
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
    return 0;
    //	    String[] res = param[0].split("[,]", 0);
    //	    int enemyID = Integer.parseInt(res[0]);
    //	    int spellId = controller.getSpellIdFromName("Fire bolt");
    //	    try {
    //	      offset = Integer.parseInt(res[1]);
    //	      System.out.println(offset);
    //	    } catch (Exception e) {
    //	      offset = 0;
    //	    }

  }

  public void setupGUI() {
    JLabel headerLabel = new JLabel("Mage Enemy");
    JButton startScriptButton = new JButton("Start");
    JLabel enemyIdLabel = new JLabel("Single enemy Id:");
    JLabel spellNameLabel = new JLabel("Exact spell name:");
    JLabel offsetLabel = new JLabel("Tiles to move(Leave blank to stay still):");

    startScriptButton.addActionListener(
        e -> {
          scriptFrame.setVisible(false);
          scriptFrame.dispose();
          controller.displayMessage("@blu@- Hire");
          scriptStarted = true;
          completeSetup();
        });

    scriptFrame.setLayout(new GridLayout(0, 1));
    scriptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    scriptFrame.add(headerLabel);
    scriptFrame.add(enemyIdLabel);
    scriptFrame.add(enemyIdField);
    scriptFrame.add(spellNameLabel);
    scriptFrame.add(spellNameField);
    scriptFrame.add(offsetLabel);
    scriptFrame.add(offsetField);
    scriptFrame.add(startScriptButton);
    scriptFrame.pack();
    scriptFrame.setLocationRelativeTo(null);
    scriptFrame.setVisible(true);
    scriptFrame.requestFocusInWindow();
  }

  public void completeSetup() {
    try {
      enemyId = Integer.parseInt(enemyIdField.getText());
      spellId = controller.getSpellIdFromName(spellNameField.getText());
      offset = Integer.parseInt(offsetField.getText());
      scriptStarted = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
