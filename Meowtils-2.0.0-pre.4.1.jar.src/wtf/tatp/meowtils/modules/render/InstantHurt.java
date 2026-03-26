/*    */ package wtf.tatp.meowtils.modules.render;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraftforge.event.entity.player.AttackEntityEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.modules.advanced.AntiBot;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InstantHurt
/*    */   extends Module
/*    */ {
/*    */   public InstantHurt() {
/* 18 */     super("InstantHurt", "instantHurtKey", "instantHurt", Module.Category.Render);
/* 19 */     tooltip("Instantly render hurt animation on players clientside.");
/*    */   }
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onAttackEntity(AttackEntityEvent event) {
/* 24 */     if (!(event.target instanceof EntityPlayer))
/* 25 */       return;  EntityPlayer entity = (EntityPlayer)event.target;
/* 26 */     if (AntiBot.isBot(entity))
/* 27 */       return;  if (entity.field_70737_aN <= 0)
/* 28 */       entity.func_70057_ab(); 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/render/InstantHurt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */