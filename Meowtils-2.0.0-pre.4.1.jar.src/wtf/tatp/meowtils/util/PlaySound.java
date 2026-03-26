/*    */ package wtf.tatp.meowtils.util;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ 
/*    */ public class PlaySound {
/*  6 */   private static final PlaySound INSTANCE = new PlaySound();
/*    */ 
/*    */ 
/*    */   
/*    */   public static PlaySound getInstance() {
/* 11 */     return INSTANCE;
/*    */   }
/* 13 */   Minecraft mc = Minecraft.func_71410_x();
/*    */   
/*    */   public void playPingSound() {
/* 16 */     if (this.mc.field_71439_g != null)
/* 17 */       this.mc.field_71439_g.func_85030_a("random.orb", 1.0F, 1.0F); 
/*    */   }
/*    */   
/*    */   public void playPingSoundDeep() {
/* 21 */     if (this.mc.field_71439_g != null)
/* 22 */       this.mc.field_71439_g.func_85030_a("random.orb", 1.0F, 0.2F); 
/*    */   }
/*    */   
/*    */   public void playPingSoundMedium() {
/* 26 */     if (this.mc.field_71439_g != null)
/* 27 */       this.mc.field_71439_g.func_85030_a("random.orb", 1.0F, 0.5F); 
/*    */   }
/*    */   
/*    */   public void playPingSoundLevel() {
/* 31 */     if (this.mc.field_71439_g != null)
/* 32 */       this.mc.field_71439_g.func_85030_a("random.levelup", 1.0F, 2.0F); 
/*    */   }
/*    */   
/*    */   public void playAnvilSound() {
/* 36 */     if (this.mc.field_71439_g != null)
/* 37 */       this.mc.field_71439_g.func_85030_a("random.anvil_land", 0.7F, 1.8F); 
/*    */   }
/*    */   
/*    */   public void playCritSound() {
/* 41 */     if (this.mc.field_71439_g != null)
/* 42 */       this.mc.field_71439_g.func_85030_a("meowtils:crit", 1.0F, 1.0F); 
/*    */   }
/*    */   
/*    */   public void playMeowSound() {
/* 46 */     if (this.mc.field_71439_g != null)
/* 47 */       this.mc.field_71439_g.func_85030_a("mob.cat.meow", 1.0F, 1.0F); 
/*    */   }
/*    */   
/*    */   public void playAnvilBreakSound() {
/* 51 */     if (this.mc.field_71439_g != null)
/* 52 */       this.mc.field_71439_g.func_85030_a("random.anvil_use", 1.0F, 1.0F); 
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/PlaySound.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */