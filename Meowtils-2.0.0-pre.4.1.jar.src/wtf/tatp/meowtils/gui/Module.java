/*     */ package wtf.tatp.meowtils.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.ArrayList;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.network.play.server.S18PacketEntityTeleport;
/*     */ import net.minecraftforge.client.event.RenderGameOverlayEvent;
/*     */ import net.minecraftforge.client.event.RenderWorldLastEvent;
/*     */ import net.minecraftforge.common.MinecraftForge;
/*     */ import net.minecraftforge.fml.common.gameevent.TickEvent;
/*     */ import wtf.tatp.meowtils.Meowtils;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.gui.values.BrightnessValue;
/*     */ import wtf.tatp.meowtils.gui.values.ColorValue;
/*     */ import wtf.tatp.meowtils.gui.values.NumberValue;
/*     */ import wtf.tatp.meowtils.gui.values.OpacityValue;
/*     */ import wtf.tatp.meowtils.gui.values.SaturationValue;
/*     */ import wtf.tatp.meowtils.util.Wrapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class Module
/*     */ {
/*     */   protected Minecraft mc;
/*     */   private String name;
/*     */   private int key;
/*     */   private boolean state;
/*     */   private Category category;
/*     */   private ArrayList<ColorValue> rgb;
/*     */   private ArrayList<SaturationValue> saturation;
/*     */   private ArrayList<BrightnessValue> brightness;
/*  38 */   private final Object configInstance = cfg.v; private ArrayList<BooleanValue> booleans; private ArrayList<ArrayValue> arrays; private ArrayList<NumberValue> values; private ArrayList<OpacityValue> opacity; public String tooltip; protected String moduleName; private boolean alwaysEnabled = false; private final Field keyField; private final Field stateField;
/*     */   
/*     */   public Module(String name, String keyFieldName, String stateFieldName, Category category) {
/*  41 */     this.mc = Wrapper.getMinecraft();
/*  42 */     this.booleans = new ArrayList<>();
/*  43 */     this.values = new ArrayList<>();
/*  44 */     this.arrays = new ArrayList<>();
/*  45 */     this.rgb = new ArrayList<>();
/*  46 */     this.saturation = new ArrayList<>();
/*  47 */     this.brightness = new ArrayList<>();
/*  48 */     this.opacity = new ArrayList<>();
/*  49 */     this.name = name;
/*  50 */     this.state = false;
/*  51 */     this.category = category;
/*  52 */     this.tooltip = null;
/*  53 */     this.moduleName = name;
/*     */     
/*  55 */     int tempKey = 0;
/*  56 */     boolean tempState = false;
/*  57 */     Field tmpKeyField = null;
/*  58 */     Field tmpStateField = null;
/*     */     
/*     */     try {
/*  61 */       if (keyFieldName != null && !keyFieldName.isEmpty()) {
/*  62 */         tmpKeyField = this.configInstance.getClass().getField(keyFieldName);
/*  63 */         tmpKeyField.setAccessible(true);
/*  64 */         Object value = tmpKeyField.get(this.configInstance);
/*  65 */         if (value instanceof Integer) tempKey = ((Integer)value).intValue();
/*     */       
/*     */       } 
/*  68 */       if (stateFieldName != null && !stateFieldName.isEmpty()) {
/*  69 */         tmpStateField = this.configInstance.getClass().getField(stateFieldName);
/*  70 */         tmpStateField.setAccessible(true);
/*  71 */         Object value = tmpStateField.get(this.configInstance);
/*  72 */         if (value instanceof Boolean) tempState = ((Boolean)value).booleanValue(); 
/*     */       } 
/*  74 */     } catch (Exception e) {
/*  75 */       throw new RuntimeException("Failed to link config fields: " + keyFieldName + ", " + stateFieldName, e);
/*     */     } 
/*     */     
/*  78 */     this.key = tempKey;
/*  79 */     this.keyField = tmpKeyField;
/*  80 */     this.stateField = tmpStateField;
/*     */     
/*  82 */     if (tempState) {
/*  83 */       setState(true);
/*     */     }
/*     */   }
/*     */   
/*     */   public Module(String name, String keyFieldName, String stateFieldName, Category category, boolean alwaysEnabled) {
/*  88 */     this(name, keyFieldName, stateFieldName, category);
/*  89 */     this.alwaysEnabled = alwaysEnabled;
/*     */     
/*  91 */     if (alwaysEnabled) {
/*  92 */       this.state = true;
/*  93 */       MinecraftForge.EVENT_BUS.register(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setState(boolean enabled) {
/*  98 */     if (this.alwaysEnabled && !enabled)
/*     */       return; 
/* 100 */     if (this.state == enabled)
/* 101 */       return;  this.state = enabled;
/*     */     
/* 103 */     if (enabled) {
/* 104 */       MinecraftForge.EVENT_BUS.register(this);
/* 105 */       onEnable();
/*     */     } else {
/* 107 */       MinecraftForge.EVENT_BUS.unregister(this);
/* 108 */       onDisable();
/*     */     } 
/*     */     
/* 111 */     if (!this.alwaysEnabled && this.stateField != null) {
/*     */       try {
/* 113 */         this.stateField.set(this.configInstance, Boolean.valueOf(enabled));
/* 114 */         cfg.save();
/* 115 */       } catch (Exception e) {
/* 116 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */     
/* 120 */     if (!this.alwaysEnabled && (Minecraft.func_71410_x()).field_71439_g != null && cfg.v.toggleNotifications) {
/* 121 */       String message = this.state ? (this.moduleName + Meowtils.onMessage) : (this.moduleName + Meowtils.offMessage);
/* 122 */       Meowtils.addMessage(message);
/*     */     } 
/*     */   }
/*     */   public String getName() {
/* 126 */     return this.name;
/*     */   } public void setName(String name) {
/* 128 */     this.name = name;
/*     */   }
/*     */   public void setKey(int key) {
/* 131 */     this.key = key;
/*     */     
/* 133 */     if (this.keyField != null) {
/*     */       try {
/* 135 */         this.keyField.set(this.configInstance, Integer.valueOf(key));
/* 136 */         cfg.save();
/* 137 */       } catch (Exception e) {
/* 138 */         e.printStackTrace();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setToggled(boolean toggled) {
/* 145 */     return this.state = toggled;
/*     */   }
/*     */   
/*     */   public boolean getState() {
/* 149 */     return this.state;
/*     */   }
/*     */   
/*     */   public int getKey() {
/* 153 */     return this.key;
/*     */   }
/*     */   
/*     */   public Category getCategory() {
/* 157 */     return this.category;
/*     */   }
/*     */   
/*     */   public ArrayList<BooleanValue> getBooleans() {
/* 161 */     return this.booleans;
/*     */   }
/*     */   public ArrayList<ArrayValue> getArrays() {
/* 164 */     return this.arrays;
/*     */   }
/*     */   public ArrayList<NumberValue> getValues() {
/* 167 */     return this.values;
/*     */   }
/*     */   public ArrayList<ColorValue> getRgb() {
/* 170 */     return this.rgb;
/*     */   } public ArrayList<SaturationValue> getSaturation() {
/* 172 */     return this.saturation;
/*     */   } public ArrayList<BrightnessValue> getBrightness() {
/* 174 */     return this.brightness;
/*     */   } public ArrayList<OpacityValue> getOpacity() {
/* 176 */     return this.opacity;
/*     */   }
/*     */   public void toggle() {
/* 179 */     setState(!this.state);
/*     */   }
/*     */   
/*     */   public void addBoolean(BooleanValue booleans) {
/* 183 */     this.booleans.add(booleans);
/*     */   }
/*     */   
/*     */   public void addArray(ArrayValue array) {
/* 187 */     this.arrays.add(array);
/*     */   }
/*     */   
/*     */   public void addValue(NumberValue values) {
/* 191 */     this.values.add(values);
/*     */   }
/*     */   public void addColor(ColorValue colorValue) {
/* 194 */     this.rgb.add(colorValue);
/*     */   } public void addSaturation(SaturationValue saturationValue) {
/* 196 */     this.saturation.add(saturationValue);
/*     */   } public void addBrightness(BrightnessValue brightnessValue) {
/* 198 */     this.brightness.add(brightnessValue);
/*     */   } public void addOpacity(OpacityValue opacityValue) {
/* 200 */     this.opacity.add(opacityValue);
/*     */   }
/*     */   public static ArrayList<Module> getCategoryModules(Category cat) {
/* 203 */     ArrayList<Module> modsInCategory = new ArrayList<>();
/* 204 */     for (Module mod : ModuleManager.getModules()) {
/* 205 */       if (mod.getCategory() == cat) {
/* 206 */         modsInCategory.add(mod);
/*     */       }
/*     */     } 
/* 209 */     return modsInCategory;
/*     */   }
/*     */   
/*     */   public static Module getModule(Class<? extends Module> clazz) {
/* 213 */     for (Module mod : ModuleManager.getModules()) {
/* 214 */       if (mod.getClass() == clazz) {
/* 215 */         return mod;
/*     */       }
/*     */     } 
/* 218 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onEnable() {}
/*     */ 
/*     */   
/*     */   public void onDisable() {}
/*     */ 
/*     */   
/*     */   public void onClientTick(TickEvent.ClientTickEvent e) {}
/*     */ 
/*     */   
/*     */   public void onRenderTick(TickEvent.RenderTickEvent e) {}
/*     */ 
/*     */   
/*     */   public void onRenderText(RenderGameOverlayEvent.Post e) throws IOException {}
/*     */ 
/*     */   
/*     */   public S18PacketEntityTeleport onEntityTeleport(S18PacketEntityTeleport packet) {
/* 238 */     return packet;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onRenderEvent(RenderWorldLastEvent e) {}
/*     */   
/*     */   public Module tooltip(String tooltip) {
/* 245 */     this.tooltip = tooltip;
/* 246 */     return this;
/*     */   }
/*     */   
/*     */   public String getTooltip() {
/* 250 */     return this.tooltip;
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
/*     */   public enum Category
/*     */   {
/* 324 */     Meowtils,
/* 325 */     Hypixel,
/* 326 */     Skywars,
/* 327 */     Bedwars,
/* 328 */     Render,
/* 329 */     Antisnipe,
/* 330 */     Utility,
/* 331 */     Advanced;
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/Module.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */