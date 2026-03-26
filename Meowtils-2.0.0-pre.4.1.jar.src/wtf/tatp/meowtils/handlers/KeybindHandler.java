/*    */ package wtf.tatp.meowtils.handlers;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraft.client.settings.KeyBinding;
/*    */ import net.minecraftforge.fml.client.registry.ClientRegistry;
/*    */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*    */ import net.minecraftforge.fml.common.gameevent.InputEvent;
/*    */ import org.lwjgl.input.Keyboard;
/*    */ import wtf.tatp.meowtils.Meowtils;
/*    */ import wtf.tatp.meowtils.config.cfg;
/*    */ 
/*    */ public class KeybindHandler
/*    */ {
/*    */   public static final int NUM_KEYS = 10;
/* 16 */   public static final KeyBinding[] autotextKeybinds = new KeyBinding[10];
/*    */   public static KeyBinding fakeAutoBlockKeybind;
/*    */   
/*    */   @SubscribeEvent
/*    */   public void onKeyInput(InputEvent.KeyInputEvent event) {
/* 21 */     Minecraft mc = Minecraft.func_71410_x();
/* 22 */     if (Keyboard.isKeyDown(cfg.v.guiBind) && 
/* 23 */       mc.field_71462_r == null && mc.field_71439_g != null && mc.field_71441_e != null) {
/* 24 */       mc.func_147108_a((GuiScreen)Meowtils.getClickGUI());
/*    */     }
/*    */   }
/*    */   
/*    */   public static void init() {
/* 29 */     for (int i = 0; i < 10; i++) {
/* 30 */       autotextKeybinds[i] = new KeyBinding(String.format("AutoText message %02d", new Object[] { Integer.valueOf(i + 1) }), 0, "Meowtils");
/* 31 */       ClientRegistry.registerKeyBinding(autotextKeybinds[i]);
/*    */     } 
/* 33 */     fakeAutoBlockKeybind = new KeyBinding("FakeAutoBlock extra toggle", 0, "Meowtils");
/* 34 */     ClientRegistry.registerKeyBinding(fakeAutoBlockKeybind);
/*    */   }
/*    */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/handlers/KeybindHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */