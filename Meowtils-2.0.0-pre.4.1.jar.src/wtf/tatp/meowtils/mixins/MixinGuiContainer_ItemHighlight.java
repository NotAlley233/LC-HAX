/*     */ package wtf.tatp.meowtils.mixins;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import org.spongepowered.asm.mixin.Mixin;
/*     */ import org.spongepowered.asm.mixin.Shadow;
/*     */ import org.spongepowered.asm.mixin.injection.At;
/*     */ import org.spongepowered.asm.mixin.injection.Inject;
/*     */ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.modules.skywars.ItemHighlight;
/*     */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*     */ import wtf.tatp.meowtils.util.Render;
/*     */ 
/*     */ @SideOnly(Side.CLIENT)
/*     */ @Mixin({GuiContainer.class})
/*     */ public abstract class MixinGuiContainer_ItemHighlight
/*     */ {
/*     */   @Inject(method = {"drawSlot"}, at = {@At("HEAD")})
/*     */   private void onDrawSlot(Slot slot, CallbackInfo ci) {
/*  28 */     if (slot != null && slot.func_75216_d()) {
/*  29 */       ItemStack stack = slot.func_75211_c();
/*  30 */       String id = ((ResourceLocation)Item.field_150901_e.func_177774_c(stack.func_77973_b())).toString();
/*  31 */       if (cfg.v.itemHighlightSkywarsOnly && !GamemodeUtil.skywarsGame && !GamemodeUtil.skywarsMiniGame)
/*     */         return; 
/*  33 */       if (cfg.v.itemHighlight) {
/*  34 */         if (ItemHighlight.getItemBlacklist().contains(id)) {
/*  35 */           Render.drawSlotBackground(slot.field_75223_e, slot.field_75221_f, -2130771968);
/*  36 */         } else if (shouldHighlight(stack)) {
/*  37 */           Render.drawSlotBackground(slot.field_75223_e, slot.field_75221_f, -2147418368);
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Shadow
/*     */   protected abstract void func_146977_a(Slot paramSlot);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean shouldHighlight(ItemStack stack) {
/*  74 */     if (!ItemHighlight.shouldHighlight(stack)) return false;
/*     */     
/*  76 */     Minecraft mc = Minecraft.func_71410_x();
/*  77 */     ItemStack cursor = mc.field_71439_g.field_71071_by.func_70445_o();
/*     */     
/*  79 */     if (cfg.v.itemHighlightBestOnly && cursor != null && ItemHighlight.isBestItem(cursor)) {
/*  80 */       if (stack == cursor) return true;
/*     */       
/*  82 */       if ((stack.func_77973_b() instanceof net.minecraft.item.ItemSword && cursor.func_77973_b() instanceof net.minecraft.item.ItemSword) || (stack.func_77973_b() instanceof net.minecraft.item.ItemAxe && cursor.func_77973_b() instanceof net.minecraft.item.ItemAxe) || (stack.func_77973_b() instanceof net.minecraft.item.ItemBow && cursor.func_77973_b() instanceof net.minecraft.item.ItemBow)) {
/*  83 */         return false;
/*     */       }
/*     */       
/*  86 */       if (stack.func_77973_b() instanceof ItemArmor && cursor.func_77973_b() instanceof ItemArmor) {
/*  87 */         ItemArmor armorStack = (ItemArmor)stack.func_77973_b();
/*  88 */         ItemArmor armorCursor = (ItemArmor)cursor.func_77973_b();
/*  89 */         if (armorStack.field_77881_a == armorCursor.field_77881_a) {
/*  90 */           return false;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  95 */     if (!cfg.v.itemHighlightBestOnly) {
/*  96 */       return true;
/*     */     }
/*     */     
/*  99 */     if (stack.func_77973_b() instanceof net.minecraft.item.ItemSword || stack.func_77973_b() instanceof net.minecraft.item.ItemAxe || stack
/* 100 */       .func_77973_b() instanceof net.minecraft.item.ItemBow || stack.func_77973_b() instanceof ItemArmor) {
/* 101 */       return ItemHighlight.isBestItem(stack);
/*     */     }
/*     */     
/* 104 */     return true;
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/mixins/MixinGuiContainer_ItemHighlight.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */