/*    */ package wtf.tatp.meowtils.modules.utility;
/*    */ 
/*    */ import net.minecraft.network.play.server.S19PacketEntityStatus;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraftforge.event.entity.player.AttackEntityEvent;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ import wtf.tatp.meowtils.events.ReceivePacketEvent;
/*    */ import wtf.tatp.meowtils.gui.Module;
/*    */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*    */ import wtf.tatp.meowtils.util.PlaySound;
/*    */ 
/*    */ public class ActionSounds
/*    */   extends Module
/*    */ {
/*    */   private BooleanValue block;
/*    */   private BooleanValue crit;
/* 18 */   private long hurtTime = 0L;
/* 19 */   private long lastCritTime = 0L;
/*    */   
/*    */   public ActionSounds() {
/* 22 */     super("ActionSounds", "actionSoundsKey", "actionSounds", Module.Category.Utility);
/* 23 */     tooltip("Plays a sound when performing certain actions.");
/* 24 */     addBoolean(this.block = new BooleanValue("Blocked damage", "actionSoundsBlock"));
/* 25 */     addBoolean(this.crit = new BooleanValue("Critical hit", "actionSoundsCrit"));
/*    */   }
/*    */ 
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onPacketReceived(ReceivePacketEvent event) {
/* 31 */     if (this.mc.field_71441_e == null || this.mc.field_71439_g == null)
/* 32 */       return;  if (!cfg.v.actionSoundsBlock)
/* 33 */       return;  if (event.getPacket() instanceof S19PacketEntityStatus) {
/* 34 */       if (((S19PacketEntityStatus)event.getPacket()).func_149161_a((World)this.mc.field_71441_e) != this.mc.field_71439_g || ((S19PacketEntityStatus)event.getPacket()).func_149160_c() != 2)
/* 35 */         return;  if (!this.mc.field_71439_g.func_70632_aY()) {
/*    */         return;
/*    */       }
/* 38 */       long now = System.currentTimeMillis();
/*    */       
/* 40 */       if (now - this.hurtTime >= 250L) {
/* 41 */         PlaySound.getInstance().playAnvilSound();
/* 42 */         this.hurtTime = now;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onEntityInteract(AttackEntityEvent event) {
/* 50 */     if (!(event.target instanceof net.minecraft.entity.player.EntityPlayer))
/* 51 */       return;  if (!cfg.v.actionSoundsCrit)
/* 52 */       return;  if (this.mc.field_71439_g.field_70122_E || this.mc.field_71439_g.field_70143_R <= 0.0F || this.mc.field_71439_g.func_70090_H() || this.mc.field_71439_g.func_180799_ab() || this.mc.field_71439_g.func_70115_ae())
/* 53 */       return;  long now = System.currentTimeMillis();
/*    */     
/* 55 */     if (now - this.lastCritTime > 250L) {
/* 56 */       PlaySound.getInstance().playCritSound();
/* 57 */       this.lastCritTime = now;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/utility/ActionSounds.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */