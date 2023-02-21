package callbacks;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bot.Main;
import compatibility.sbot.Script;
import controller.Controller;
import listeners.LoginListener;
import orsc.enumerations.MessageType;
import scripting.idlescript.IdleScript;

/** 
 * Contains interrupts which are called every time the patched client receives a message. 
 * 
 * @author Dvorak
 */

public class MessageCallback {

    private static int sbotLastChatter = 0;
    private static String sbotLastChatterName = "";
    private static String sbotLastChatMessage = "";
    private static String sbotLastNPCMessage = "";
    private static String sbotLastServerMessage = "";
    
    private static final Pattern p = Pattern.compile("^(.*) (.*) level!"); //for parsing level up messages

    /**
     * The hook called my the patched client every time a message is printed on screen. 
     * @param crownEnabled
     * @param sender
     * @param message
     * @param type
     * @param crownID
     * @param formerName
     * @param colourOverride
     */
    public static void messageHook(boolean crownEnabled, String sender, String message, MessageType type, int crownID,
                                   String formerName, String colourOverride) {
    	
        if (type == MessageType.GAME) {
        	if(message.contains("You just advanced")) {
        		handleLevelUp(message);
        	} else if(message.contains("You have been standing here for")) {
        	    Controller c = Main.getController();
        	    System.out.println("got standing message!");

        	    if(c != null)
        	        c.moveCharacter();

            }
        }
        
        if (Main.isRunning() && Main.getCurrentRunningScript() != null) {
            if (Main.getCurrentRunningScript() instanceof IdleScript) {
            	if (type == MessageType.GAME) {
                    ((IdleScript) Main.getCurrentRunningScript()).serverMessageInterrupt(message);
                } else if (type == MessageType.PRIVATE_RECIEVE) {
                    ((IdleScript) Main.getCurrentRunningScript()).privateMessageReceivedInterrupt(sender, message);
                } else if (type == MessageType.CHAT) {
                    ((IdleScript) Main.getCurrentRunningScript()).chatMessageInterrupt(sender + ": " + message);
                } else if (type == MessageType.QUEST) {
                    ((IdleScript) Main.getCurrentRunningScript()).questMessageInterrupt(message);
                } else if (type == MessageType.TRADE) {
                    ((IdleScript) Main.getCurrentRunningScript()).tradeMessageInterrupt(sender);
                }
            } else if (Main.getCurrentRunningScript() instanceof compatibility.sbot.Script) {
                if (type == MessageType.GAME) {
                    ((compatibility.sbot.Script) Main.getCurrentRunningScript()).ServerMessage(message);
                    sbotLastServerMessage = message;
                } else if (type == MessageType.CHAT) {
                    ((compatibility.sbot.Script) Main.getCurrentRunningScript()).ChatMessage(sender + ": " + message);

                    sbotLastChatter = (int)(System.currentTimeMillis() / 1000L); //2038 problem, but that's what you get for using sbot scripts in 2038.
                    sbotLastChatMessage = message;
                    sbotLastChatterName = sender;
                } else if (type == MessageType.QUEST) {
                    ((compatibility.sbot.Script) Main.getCurrentRunningScript()).NPCMessage(message);
                    sbotLastNPCMessage = message;
                }
//                else if (type == MessageType.TRADE) { //UNTESTED
//                    ((compatibility.sbot.Script) Main.getCurrentRunningScript()).TradeRequest(message.split(" ")[0]); //needs to be converted to player pid
//                } 
            } else if(Main.getCurrentRunningScript() instanceof compatibility.apos.Script) {
//            	if(((compatibility.apos.Script)Main.getCurrentRunningScript()).isControllerSet()) {
	            	if (type == MessageType.GAME) {
	                    ((compatibility.apos.Script) Main.getCurrentRunningScript()).onServerMessage(message);
	                } else if (type == MessageType.CHAT) {
	                    ((compatibility.apos.Script) Main.getCurrentRunningScript()).onChatMessage(message, sender, false, false);
	                } else if (type == MessageType.QUEST) {
	                    ((compatibility.apos.Script) Main.getCurrentRunningScript()).onServerMessage(message);
	                } 
	                else if (type == MessageType.TRADE) { //UNTESTED
	                    ((compatibility.apos.Script) Main.getCurrentRunningScript()).onTradeRequest(sender);
	                } 
	                else if(type == MessageType.PRIVATE_RECIEVE) { // UNTESTED
	                	((compatibility.apos.Script) Main.getCurrentRunningScript()).onPrivateMessage(message, sender, false, false);
	                }
//            	}
            }
        }
    }
	
	private static void handleLevelUp(String message) {
		Controller c = Main.getController();
		String skillName = null;
		int skillLevel = -1;
		
		Matcher m = p.matcher(message);
		
		if(m.find()) {
			skillName = m.group(2);
			if(skillName != null && skillName.length() > 1) {
				if(skillName.toLowerCase().equals("woodcut"))
					skillName = "Woodcutting";
				
				skillName = Character.toUpperCase(skillName.charAt(0)) + skillName.substring(1);
				
				skillLevel = c.getBaseStat(c.getStatId(skillName));
				
				DrawCallback.displayAndScreenshotLevelUp(skillName, skillLevel); 
			}
		}
		
	}

	/**
	 * Provides data for the LastChatter() function in SBot. 
	 * @return int
	 */
	public static int getSbotLastChatter() {
        return sbotLastChatter;
    }

	/**
	 * Provides data for the LastChatterName() function in SBot. 
	 * @return String -- guaranteed to not be null. 
	 */
    public static String getSbotLastChatterName() {
        return sbotLastChatterName;
    }

	/**
	 * Provides data for the LastChatMessage() function in SBot. 
	 * @return String -- guaranteed to not be null. 
	 */
    public static String getSbotLastChatMessage() {
        return sbotLastChatMessage;
    }

	/**
	 * Provides data for the LastNPCMessage() function in SBot. 
	 * @return String -- guaranteed to not be null. 
	 */
    public static String getSbotLastNPCMessage() {
        return sbotLastNPCMessage;
    }

	/**
	 * Provides data for the LastServerMessage() function in SBot. 
	 * @return String -- guaranteed to not be null. 
	 */
    public static String getSbotLastServerMessage() {
        return sbotLastServerMessage;
    }
}