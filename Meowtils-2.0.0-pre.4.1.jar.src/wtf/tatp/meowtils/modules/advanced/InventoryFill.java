/*     */ package wtf.tatp.meowtils.modules.advanced;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.inventory.GuiChest;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.client.resources.I18n;
/*     */ import net.minecraft.inventory.ContainerChest;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import org.lwjgl.input.Keyboard;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*     */ import wtf.tatp.meowtils.mixins.GuiContainerAccessor;
/*     */ import wtf.tatp.meowtils.modules.skywars.ItemHighlight;
/*     */ 
/*     */ public class InventoryFill
/*     */   extends Module {
/*  30 */   public static final Set<Integer> clickedSlots = new HashSet<>();
/*  31 */   private long nextClickTime = 0L;
/*  32 */   private long nextAutoClickTime = 0L;
/*     */   
/*     */   private ArrayValue clickMode;
/*     */   
/*     */   private BooleanValue renderClicked;
/*     */   
/*     */   public InventoryFill() {
/*  39 */     super("InventoryFill", "inventoryFillKey", "inventoryFill", Module.Category.Advanced);
/*  40 */     tooltip("Automatically clicks items in inventories for you.\n-Auto/Manual mode, grab items from chests automatically or manually\n§cWARNING: §cThis §cmight §cflag §con §clow §cdelay.");
/*  41 */     addValue(this.cps = new NumberValue("CPS", 1.0D, 20.0D, 1.0D, null, "inventoryFillCps", int.class));
/*  42 */     addValue(this.autoDelay = new NumberValue("Auto delay", 50.0D, 1000.0D, 50.0D, "ms", "inventoryFillAutoDelay", int.class));
/*  43 */     addBoolean(this.renderClicked = new BooleanValue("Render clicked", "inventoryFillRenderClicked"));
/*  44 */     addArray(this.clickMode = new ArrayValue("Click Mode", Arrays.asList(new String[] { "Left", "Drop", "Both" }, ), "inventoryFillClickMode"));
/*  45 */     addArray(this.mode = new ArrayValue("Mode", Arrays.asList(new String[] { "Manual", "Auto" }, ), "inventoryFillMode"));
/*     */   }
/*     */   private NumberValue cps; private NumberValue autoDelay; private ArrayValue mode;
/*     */   @SubscribeEvent
/*     */   public void onTick(TickEvent.ClientTickEvent event) {
/*  50 */     Minecraft mc = Minecraft.func_71410_x();
/*  51 */     if (event.phase != TickEvent.Phase.START)
/*  52 */       return;  if (mc.field_71439_g == null || mc.field_71462_r == null || mc.field_71442_b == null)
/*  53 */       return;  if (!(mc.field_71462_r instanceof GuiContainer))
/*  54 */       return;  if (!mc.field_71442_b.func_178889_l().func_77144_e())
/*  55 */       return;  GuiContainer gui = (GuiContainer)mc.field_71462_r;
/*  56 */     Slot hovered = gui.getSlotUnderMouse();
/*     */     
/*  58 */     boolean shiftDown = Keyboard.isKeyDown(42);
/*  59 */     boolean leftClickDown = Mouse.isButtonDown(0);
/*  60 */     boolean dropKeyDown = Keyboard.isKeyDown(mc.field_71474_y.field_74316_C.func_151463_i());
/*  61 */     boolean ctrlKeyDown = Keyboard.isKeyDown(29);
/*  62 */     long now = System.currentTimeMillis();
/*     */     
/*  64 */     String clickMode = cfg.v.inventoryFillClickMode;
/*     */     
/*  66 */     boolean allowLeft = (clickMode.equals("Left") || clickMode.equals("Both"));
/*  67 */     boolean allowDrop = (clickMode.equals("Drop") || clickMode.equals("Both"));
/*     */     
/*  69 */     if (cfg.v.inventoryFillRenderClicked) {
/*  70 */       clickedSlots.clear();
/*     */     }
/*     */     
/*  73 */     if (cfg.v.inventoryFillMode.equals("Auto") && allowLeft && leftClickDown && shiftDown && 
/*  74 */       now >= this.nextAutoClickTime) {
/*  75 */       this.nextAutoClickTime = now + cfg.v.inventoryFillAutoDelay;
/*  76 */       autoHandler(gui);
/*     */     } 
/*     */ 
/*     */     
/*  80 */     if (allowLeft && leftClickDown && shiftDown && 
/*  81 */       now >= this.nextClickTime) {
/*  82 */       this.nextClickTime = now + 1000L / cfg.v.inventoryFillCps;
/*  83 */       leftClickHandler(gui, hovered);
/*     */     } 
/*     */ 
/*     */     
/*  87 */     if (allowDrop && dropKeyDown && hovered != null && hovered.func_75216_d()) {
/*  88 */       dropHandler(gui, hovered, ctrlKeyDown);
/*     */     }
/*     */   }
/*     */   
/*     */   private void autoHandler(GuiContainer gui) {
/*  93 */     if (!(gui instanceof GuiChest))
/*  94 */       return;  GuiChest guiChest = (GuiChest)(Minecraft.func_71410_x()).field_71462_r;
/*  95 */     IInventory lowerInventory = ((ContainerChest)guiChest.field_147002_h).func_85151_d();
/*  96 */     String inventoryName = lowerInventory.func_145748_c_().func_150260_c();
/*  97 */     String localizedChest = I18n.func_135052_a("container.chest", new Object[0]);
/*  98 */     String localizedLargeChest = I18n.func_135052_a("container.chestDouble", new Object[0]);
/*  99 */     if (!inventoryName.equals(localizedChest) && !inventoryName.equals(localizedLargeChest))
/*     */       return; 
/* 101 */     for (Slot chestSlot : gui.field_147002_h.field_75151_b) {
/* 102 */       if (chestSlot == null || !chestSlot.func_75216_d() || 
/* 103 */         chestSlot.field_75224_c == (Minecraft.func_71410_x()).field_71439_g.field_71071_by)
/*     */         continue; 
/* 105 */       ItemStack stack = chestSlot.func_75211_c();
/* 106 */       if (stack == null)
/*     */         continue; 
/* 108 */       String id = ((ResourceLocation)Item.field_150901_e.func_177774_c(stack.func_77973_b())).toString();
/* 109 */       if (ItemHighlight.getItemBlacklist().contains(id))
/*     */         continue; 
/* 111 */       click(gui, chestSlot, 0, 1);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void leftClickHandler(GuiContainer gui, Slot hovered) {
/* 117 */     if (hovered == null || !hovered.func_75216_d())
/* 118 */       return;  click(gui, hovered, 0, 1);
/*     */   }
/*     */   
/*     */   private void dropHandler(GuiContainer gui, Slot hovered, boolean ctrlDown) {
/* 122 */     int type = 4;
/* 123 */     int button = ctrlDown ? 1 : 0;
/* 124 */     click(gui, hovered, button, type);
/*     */   }
/*     */   
/*     */   private void click(GuiContainer gui, Slot slot, int mouseButton, int type) {
/* 128 */     ((GuiContainerAccessor)gui).clickSlot(slot, slot.field_75222_d, mouseButton, type);
/* 129 */     if (cfg.v.inventoryFillRenderClicked)
/* 130 */       clickedSlots.add(Integer.valueOf(slot.field_75222_d)); 
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/advanced/InventoryFill.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */