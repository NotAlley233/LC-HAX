/*    */ package wtf.tatp.meowtils.modules.advanced;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ 
/*    */ public class ViewPackets extends Module {
/*    */   private BooleanValue showAll;
/*    */   private ArrayValue direction;
/*    */   
/*    */   public ViewPackets() {
/* 13 */     super("ViewPackets", "packetDebuggerKey", "packetDebugger", Module.Category.Advanced);
/* 14 */     tooltip("View all outgoing/incoming packets in chat.");
/* 15 */     addBoolean(this.showAll = new BooleanValue("Ignore spam", "ignorePacketSpam"));
/* 16 */     addArray(this.direction = new ArrayValue("Direction", Arrays.asList(new String[] { "Both", "Outgoing", "Incoming" }, ), "packetDirection"));
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/advanced/ViewPackets.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */