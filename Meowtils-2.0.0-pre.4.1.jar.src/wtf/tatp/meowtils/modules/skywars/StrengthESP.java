/*     */ package wtf.tatp.meowtils.modules.skywars;
/*     */ import java.awt.Color;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraftforge.client.event.ClientChatReceivedEvent;
/*     */ import net.minecraftforge.client.event.RenderWorldLastEvent;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.ColorComponent;
/*     */ import wtf.tatp.meowtils.gui.Module;
/*     */ import wtf.tatp.meowtils.gui.values.ArrayValue;
/*     */ import wtf.tatp.meowtils.gui.values.BooleanValue;
/*     */ import wtf.tatp.meowtils.gui.values.BrightnessValue;
/*     */ import wtf.tatp.meowtils.gui.values.ColorValue;
/*     */ import wtf.tatp.meowtils.gui.values.SaturationValue;
/*     */ import wtf.tatp.meowtils.util.GamemodeUtil;
/*     */ import wtf.tatp.meowtils.util.Render;
/*     */ 
/*     */ public class StrengthESP extends Module {
/*  25 */   private static final Map<String, Long> recentKillers = new HashMap<>(); private ColorValue rgb;
/*     */   private SaturationValue saturation;
/*     */   private BrightnessValue brightness;
/*     */   private ColorComponent color;
/*     */   private BooleanValue fillBox;
/*     */   private BooleanValue fade;
/*     */   private ArrayValue mode;
/*     */   
/*     */   public StrengthESP() {
/*  34 */     super("StrengthESP", "strengthESPKey", "strengthESP", Module.Category.Skywars);
/*  35 */     tooltip("Renders a box on players who have the Strength effect.");
/*  36 */     this.color = new ColorComponent("strengthESP_red", "strengthESP_green", "strengthESP_blue");
/*  37 */     addColor(this.rgb = new ColorValue("ESP color", this.color));
/*  38 */     addSaturation(this.saturation = new SaturationValue(this.color));
/*  39 */     addBrightness(this.brightness = new BrightnessValue(this.color));
/*  40 */     addBoolean(this.fillBox = new BooleanValue("Fill entire box", "strengthESPFillBox"));
/*  41 */     addBoolean(this.fade = new BooleanValue("Fade out", "strengthESPFade"));
/*  42 */     addArray(this.mode = new ArrayValue("Mode", Arrays.asList(new String[] { "3D", "2D" }, ), "strengthESPMode"));
/*     */   }
/*     */   @SubscribeEvent
/*     */   public void onRenderWorldLast(RenderWorldLastEvent event) {
/*  46 */     if (this.mc.field_71441_e == null || this.mc.field_71439_g == null)
/*     */       return; 
/*  48 */     Entity viewEntity = this.mc.func_175606_aa();
/*  49 */     if (!(viewEntity instanceof EntityLivingBase))
/*     */       return; 
/*  51 */     EntityLivingBase camEntity = (EntityLivingBase)viewEntity;
/*  52 */     double camX = camEntity.field_70142_S + (camEntity.field_70165_t - camEntity.field_70142_S) * event.partialTicks;
/*  53 */     double camY = camEntity.field_70137_T + (camEntity.field_70163_u - camEntity.field_70137_T) * event.partialTicks;
/*  54 */     double camZ = camEntity.field_70136_U + (camEntity.field_70161_v - camEntity.field_70136_U) * event.partialTicks;
/*     */     
/*  56 */     long currentTime = System.currentTimeMillis();
/*  57 */     boolean use3D = cfg.v.strengthESPMode.equalsIgnoreCase("3D");
/*  58 */     boolean fillBox = cfg.v.strengthESPFillBox;
/*     */     
/*  60 */     for (EntityPlayer player : this.mc.field_71441_e.field_73010_i) {
/*  61 */       if (player == this.mc.field_71439_g || player.func_82150_aj())
/*     */         continue; 
/*  63 */       String name = player.func_70005_c_();
/*  64 */       Long killTime = recentKillers.get(name);
/*  65 */       if (killTime == null)
/*     */         continue; 
/*  67 */       long elapsed = currentTime - killTime.longValue();
/*  68 */       if (elapsed > 5000L)
/*     */         continue; 
/*  70 */       double x = player.field_70142_S + (player.field_70165_t - player.field_70142_S) * event.partialTicks - camX;
/*  71 */       double y = player.field_70137_T + (player.field_70163_u - player.field_70137_T) * event.partialTicks - camY;
/*  72 */       double z = player.field_70136_U + (player.field_70161_v - player.field_70136_U) * event.partialTicks - camZ;
/*     */ 
/*     */       
/*  75 */       int alpha = 150;
/*  76 */       if (cfg.v.strengthESPFade) {
/*  77 */         float fadeProgress = (float)elapsed / 5000.0F;
/*  78 */         alpha = (int)(150.0F * (1.0F - fadeProgress));
/*     */       } 
/*     */       
/*  81 */       Color color = new Color(cfg.v.strengthESP_red, cfg.v.strengthESP_green, cfg.v.strengthESP_blue, alpha);
/*     */       
/*  83 */       if (use3D) {
/*  84 */         Render.draw3DEntityBox((Entity)player, fillBox, color, true, color, 0.1D, 0.1D, 0.1D); continue;
/*     */       } 
/*  86 */       float width = 0.5F;
/*  87 */       float height = width * 2.0F;
/*  88 */       Render.draw2DEntityBox(x, y + (player.field_70131_O / 2.0F), z, width, height, color, fillBox);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @SubscribeEvent
/*     */   public void onChatReceived(ClientChatReceivedEvent event) {
/*  95 */     String message = event.message.func_150260_c();
/*     */ 
/*     */ 
/*     */     
/*  99 */     Matcher matcher = Pattern.compile("(?:was killed by|was thrown into the void by|was thrown off a cliff by|was shot by|was crushed into moon dust by|was sent the wrong way by|was blasted to the moon by|was hit by an asteroid from|got rekt by|took the L to|got dabbed on by|got bamboozled by|was bit by|got WOOF'D by|was growled off an edge by|was thrown a frisbee by|was crusaded by the knight|was jousted by|was catapulted by|was shot to the knee by|was socked by|was KO'd by|took an uppercut from|was sent into a daze by|was mushed by|was peeled by|slipped on|got banana pistol'd by|was exterminated by|was scared off an edge by|was squashed by|was tranquilized by|was glazed in BBQ sauce by|slipped in BBQ sauce off the edge spilled by|was not spicy enough for|was thrown chili powder at by|was deleted by|was ALT\\+F4'd by|was crashed by|was rm -rf by|was turned into space dust by|was sent into orbit by|was retrograded by|be sent to Davy Jones' locker by|be cannonballed to death by|be voodooed by|be shot and killed by|was given the cold shoulder by|was out of the league of|heart was broken by|was struck with Cupid's arrow by|was filled full of lead by|met their end by|lost a drinking contest with|lost the draw to|was struck down by|was turned to dust by|was turned to ash by|was melted by|was put on the naughty list by|was pushed down the chimney by|was traded in for milk and cookies by|was turned to gingerbread by|was ripped to shreds by|was charged by|was ripped and thrown by|was pounced on by|was smothered in holiday cheer by|was banished into the ether by|was pushed by|was sniped by a missile of festivity by|was wrapped up by|was tied into a bow by|tripped over a present placed by|was glued up by|was whacked with a party balloon by|was popped into the void by|was launched like a firework by|was shot with a roman candle by|was painted pretty by|was flipped off the edge by|was deviled by|was made sunny side up by|was locked outside during a snow storm by|was shoved down an icy slope by|was made into a snowman by|was hit with a snowball from|was backstabbed by|was pushed into the abyss by|was thrown into a ravine by|was brutally shot by|was trampled by|was back kicked into the void by|was headbutted off a cliff by|was impaled from a distance by|was buzzed to death by|was bzzz'd off the edge by|was stung by|was startled from a distance by|was oinked by|slipped into void for|was distracted by a piglet from|got attacked by a carrot from|was chewed up by|was squeaked off the edge by|was distracted by a rat dragging pizza from|was squeaked from a distance by|of|died in close combat to|fought to the edge with|stumbled off a ledge with help by|fell to the great marksmanship of) (\\w+)(?!'s)", 2).matcher(message);
/*     */     
/* 101 */     if (matcher.find() && GamemodeUtil.skywarsGame) {
/* 102 */       String playerName = matcher.group(1);
/* 103 */       recentKillers.put(playerName, Long.valueOf(System.currentTimeMillis()));
/*     */     } 
/*     */   }
/*     */   public static void clear() {
/* 107 */     recentKillers.clear();
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/modules/skywars/StrengthESP.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */