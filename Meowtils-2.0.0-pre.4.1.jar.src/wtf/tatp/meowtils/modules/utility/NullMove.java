/*    */ package wtf.tatp.meowtils.modules.utility;
/*    */ 
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ 
/*    */ public class NullMove
/*    */   extends Module {
/*  7 */   public static long LEFT_STRAFE_LAST_PRESS_TIME = 0L;
/*  8 */   public static long RIGHT_STRAFE_LAST_PRESS_TIME = 0L;
/*  9 */   public static long FORWARD_STRAFE_LAST_PRESS_TIME = 0L;
/* 10 */   public static long BACKWARD_STRAFE_LAST_PRESS_TIME = 0L;
/*    */   
/*    */   public NullMove() {
/* 13 */     super("NullMove", "nullMoveKey", "nullMove", Module.Category.Utility);
/* 14 */     tooltip("Does not prevent movement if several movement keys are held at once.");
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/utility/NullMove.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */