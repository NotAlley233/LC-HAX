/*    */ package wtf.tatp.meowtils.util.anticheat.checks;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.UUID;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class LegitScaffoldCheck {
/* 11 */   private static final Map<UUID, Long> lastCrouchStart = new HashMap<>();
/* 12 */   private static final Map<UUID, Long> lastCrouchEnd = new HashMap<>();
/* 13 */   private static final Map<UUID, Boolean> wasSneaking = new HashMap<>();
/* 14 */   private static final Map<UUID, Long> lastSwingTick = new HashMap<>();
/* 15 */   private static final Map<UUID, List<Integer>> crouchDurations = new HashMap<>();
/* 16 */   private static final Map<UUID, Long> lastFlagTick = new HashMap<>();
/* 17 */   private final long cooldownTicks = 60L;
/*    */   
/*    */   private boolean flagged = false;
/*    */   
/*    */   public void anticheatCheck(EntityPlayer player) {
/* 22 */     if (!cfg.v.detectLegitScaffold || player == null || player == (Minecraft.func_71410_x()).field_71439_g)
/*    */       return; 
/* 24 */     UUID uuid = player.func_110124_au();
/* 25 */     long tick = player.field_70173_aa;
/* 26 */     boolean currSneak = player.func_70093_af();
/* 27 */     boolean prevSneak = ((Boolean)wasSneaking.getOrDefault(uuid, Boolean.valueOf(false))).booleanValue();
/*    */     
/* 29 */     if (currSneak && !prevSneak) {
/* 30 */       lastCrouchStart.put(uuid, Long.valueOf(tick));
/* 31 */     } else if (!currSneak && prevSneak) {
/* 32 */       lastCrouchEnd.put(uuid, Long.valueOf(tick));
/* 33 */       long start = ((Long)lastCrouchStart.getOrDefault(uuid, Long.valueOf(tick - 1L))).longValue();
/* 34 */       int duration = (int)(tick - start);
/* 35 */       ((List<Integer>)crouchDurations.computeIfAbsent(uuid, k -> new ArrayList())).add(0, Integer.valueOf(duration));
/* 36 */       if (((List)crouchDurations.get(uuid)).size() > 5)
/* 37 */         ((List)crouchDurations.get(uuid)).remove(5); 
/*    */     } 
/* 39 */     wasSneaking.put(uuid, Boolean.valueOf(currSneak));
/*    */     
/* 41 */     if (player.field_82175_bq && player.field_70732_aI != player.field_70733_aJ) {
/* 42 */       lastSwingTick.put(uuid, Long.valueOf(tick));
/*    */     }
/*    */ 
/*    */     
/* 46 */     if (player.field_70125_A >= 60.0F && player
/* 47 */       .func_70694_bm() != null && player
/* 48 */       .func_70694_bm().func_77973_b() instanceof net.minecraft.item.ItemBlock && player.field_70122_E) {
/*    */ 
/*    */       
/* 51 */       long end = ((Long)lastCrouchEnd.getOrDefault(uuid, Long.valueOf(0L))).longValue();
/* 52 */       long swing = ((Long)lastSwingTick.getOrDefault(uuid, Long.valueOf(Long.MIN_VALUE))).longValue();
/* 53 */       int crouchDuration = (int)(end - ((Long)lastCrouchStart.getOrDefault(uuid, Long.valueOf(end - 1L))).longValue());
/* 54 */       boolean quickCrouch = (crouchDuration >= 1 && crouchDuration <= 2);
/* 55 */       boolean swingTiming = (swing >= end && swing <= end + 1L);
/*    */       
/* 57 */       List<Integer> durations = crouchDurations.getOrDefault(uuid, Collections.emptyList());
/* 58 */       boolean consistent = (durations.size() >= 3 && ((Integer)durations.get(0)).intValue() <= 2 && ((Integer)durations.get(1)).intValue() <= 2 && ((Integer)durations.get(2)).intValue() <= 2);
/*    */       
/* 60 */       if (quickCrouch && swingTiming && consistent) {
/* 61 */         long lastFlag = ((Long)lastFlagTick.getOrDefault(uuid, Long.valueOf(0L))).longValue();
/* 62 */         if (tick - lastFlag >= 60L) {
/* 63 */           this.flagged = true;
/* 64 */           lastFlagTick.put(uuid, Long.valueOf(tick));
/*    */         } else {
/* 66 */           this.flagged = false;
/*    */         } 
/*    */       } else {
/* 69 */         this.flagged = false;
/*    */       } 
/*    */     } else {
/* 72 */       this.flagged = false;
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean failedLegitScaffold() {
/* 77 */     return this.flagged;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 81 */     this.flagged = false;
/*    */   }
/*    */   
/*    */   public static void clear() {
/* 85 */     lastCrouchStart.clear();
/* 86 */     lastCrouchEnd.clear();
/* 87 */     wasSneaking.clear();
/* 88 */     lastSwingTick.clear();
/* 89 */     crouchDurations.clear();
/* 90 */     lastFlagTick.clear();
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/anticheat/checks/LegitScaffoldCheck.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */