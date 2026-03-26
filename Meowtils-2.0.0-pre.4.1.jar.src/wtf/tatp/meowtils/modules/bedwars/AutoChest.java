/*     */ package wtf.tatp.meowtils.modules.bedwars;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.inventory.GuiChest;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.inventory.ContainerChest;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ import net.minecraftforge.client.event.GuiOpenEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import wtf.tatp.meowtils.DelayedTask;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*     */ import wtf.tatp.meowtils.mixins.GuiContainerAccessor;
/*     */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*     */ 
/*     */ public class AutoChest
/*     */   extends Module
/*     */ {
/*     */   private NumberValue waitDelay;
/*     */   private NumberValue delay;
/*     */   private BooleanValue renderClicked;
/*     */   private BooleanValue depositIron;
/*     */   private BooleanValue depositGold;
/*     */   private BooleanValue depositDiamond;
/*     */   private BooleanValue depositEmerald;
/*  37 */   public static final Set<Integer> clickedSlots = new HashSet<>();
/*  38 */   private long nextClickTime = 0L;
/*     */   private boolean shouldDeposit = false;
/*  40 */   private int lastCheckedSlot = 0;
/*     */   
/*     */   public AutoChest() {
/*  43 */     super("AutoChest", "autoChestKey", "autoChest", Module.Category.Bedwars);
/*  44 */     tooltip("Automatically deposit resources into a chest.\n" + EnumChatFormatting.RED + "WARNING: " + EnumChatFormatting.RED + "Low " + EnumChatFormatting.RED + "delays " + EnumChatFormatting.RED + "are " + EnumChatFormatting.RED + "detectable.");
/*  45 */     addValue(this.delay = new NumberValue("Delay", 0.0D, 500.0D, 50.0D, "ms", "autoChestDelay", int.class));
/*  46 */     addValue(this.waitDelay = new NumberValue("Wait delay", 0.0D, 500.0D, 50.0D, "ms", "autoChestWaitDelay", int.class));
/*  47 */     addBoolean(this.renderClicked = new BooleanValue("Render clicked", "autoChestRenderClicked"));
/*  48 */     addBoolean(this.depositIron = new BooleanValue("§7Iron Ingots", "autoChestIron"));
/*  49 */     addBoolean(this.depositGold = new BooleanValue("§6Gold Ingots", "autoChestGold"));
/*  50 */     addBoolean(this.depositDiamond = new BooleanValue("§bDiamonds", "autoChestDiamond"));
/*  51 */     addBoolean(this.depositEmerald = new BooleanValue("§2Emeralds", "autoChestEmerald"));
/*     */   }
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onGuiOpen(GuiOpenEvent event) {
/*  56 */     if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby)
/*  57 */       return;  if (!(event.gui instanceof GuiChest)) {
/*  58 */       this.shouldDeposit = false;
/*     */       return;
/*     */     } 
/*  61 */     String localizedChest = I18n.func_135052_a("container.chest", new Object[0]);
/*  62 */     String localizedEnderChest = I18n.func_135052_a("container.enderchest", new Object[0]);
/*  63 */     int waitDelay = cfg.v.autoChestWaitDelay / 50;
/*     */     
/*  65 */     if (event.gui instanceof GuiChest) {
/*  66 */       GuiChest chest = (GuiChest)event.gui;
/*  67 */       if (chest.field_147002_h instanceof ContainerChest) {
/*  68 */         ContainerChest container = (ContainerChest)chest.field_147002_h;
/*  69 */         IInventory containerName = container.func_85151_d();
/*  70 */         if (localizedChest.equals(containerName.func_145748_c_().func_150260_c()) || localizedEnderChest.equals(containerName.func_145748_c_().func_150260_c())) {
/*  71 */           new DelayedTask(() -> { this.shouldDeposit = true; this.lastCheckedSlot = 0; }waitDelay);
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onTick(TickEvent.ClientTickEvent event) {
/*  81 */     Minecraft mc = Minecraft.func_71410_x();
/*  82 */     if (event.phase != TickEvent.Phase.START)
/*  83 */       return;  if (!(mc.field_71462_r instanceof GuiChest))
/*  84 */       return;  if (!GamemodeUtil.bedwarsGame || GamemodeUtil.bedwarsLobby)
/*  85 */       return;  if (!this.shouldDeposit)
/*     */       return; 
/*  87 */     if (cfg.v.autoChestRenderClicked) {
/*  88 */       clickedSlots.clear();
/*     */     }
/*     */     
/*  91 */     long now = System.currentTimeMillis();
/*  92 */     if (now >= this.nextClickTime) {
/*  93 */       this.nextClickTime = now + cfg.v.autoChestDelay;
/*  94 */       boolean clicked = deposit((GuiContainer)mc.field_71462_r);
/*     */       
/*  96 */       if (!clicked) {
/*  97 */         this.shouldDeposit = false;
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean deposit(GuiContainer gui) {
/* 103 */     Minecraft mc = Minecraft.func_71410_x();
/*     */     
/* 105 */     for (int i = this.lastCheckedSlot; i < gui.field_147002_h.field_75151_b.size(); i++) {
/* 106 */       Slot slot = gui.field_147002_h.func_75139_a(i);
/*     */       
/* 108 */       if (slot != null && slot.func_75216_d() && 
/* 109 */         slot.field_75224_c == mc.field_71439_g.field_71071_by) {
/*     */         
/* 111 */         ItemStack stack = slot.func_75211_c();
/* 112 */         if (stack != null)
/*     */         {
/* 114 */           if (allowedItem(stack.func_77973_b())) {
/* 115 */             click(gui, slot, 0, 1);
/* 116 */             this.lastCheckedSlot = i + 1;
/* 117 */             return true;
/*     */           }  } 
/*     */       } 
/* 120 */     }  return false;
/*     */   }
/*     */   private void click(GuiContainer gui, Slot slot, int mouseButton, int type) {
/* 123 */     ((GuiContainerAccessor)gui).clickSlot(slot, slot.field_75222_d, mouseButton, type);
/* 124 */     if (cfg.v.autoChestRenderClicked)
/* 125 */       clickedSlots.add(Integer.valueOf(slot.field_75222_d)); 
/*     */   }
/*     */   
/*     */   private boolean allowedItem(Item item) {
/* 129 */     return ((cfg.v.autoChestIron && item == Items.field_151042_j) || (cfg.v.autoChestGold && item == Items.field_151043_k) || (cfg.v.autoChestDiamond && item == Items.field_151045_i) || (cfg.v.autoChestEmerald && item == Items.field_151166_bC));
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/bedwars/AutoChest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */