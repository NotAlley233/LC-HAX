/*     */ package wtf.tatp.meowtils.config;
/*     */ 
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.GsonBuilder;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public class cfg {
/*  11 */   private static final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
/*     */   
/*     */   public static File configFile;
/*  14 */   public static cfg v = new cfg();
/*     */   
/*     */   public static void initFile(File file) {
/*  17 */     configFile = file;
/*     */   }
/*     */   
/*     */   public static void load() {
/*  21 */     if (configFile == null) throw new IllegalStateException("Config file not set!");
/*     */     
/*  23 */     if (configFile.exists()) {
/*  24 */       try (FileReader reader = new FileReader(configFile)) {
/*  25 */         v = (cfg)gson.fromJson(reader, cfg.class);
/*     */         
/*  27 */         if (v == null) v = new cfg();
/*     */       
/*  29 */       } catch (IOException e) {
/*  30 */         e.printStackTrace();
/*  31 */         v = new cfg();
/*     */       } 
/*     */     } else {
/*  34 */       v = new cfg();
/*  35 */       save();
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void save() {
/*  40 */     if (configFile == null)
/*  41 */       return;  try (FileWriter writer = new FileWriter(configFile)) {
/*  42 */       gson.toJson(v, writer);
/*  43 */     } catch (IOException e) {
/*  44 */       e.printStackTrace();
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
/*  62 */   public String lastPlayCommand = "";
/*     */   public boolean autoUpdate = true;
/*  64 */   public String modID = "Meowtils";
/*  65 */   public String meowtilsPrefixMode = "Default";
/*     */   
/*     */   public boolean smoothFont = true;
/*     */   
/*     */   public boolean debugMessages = false;
/*     */   
/*     */   public boolean antiCheatInfo = false;
/*     */   
/*     */   public boolean ignoreTeam = true;
/*     */   public boolean ignoreTeamSpectator = false;
/*  75 */   public int guiBind = 54;
/*  76 */   public int meowtils_red = 150;
/*  77 */   public int meowtils_green = 150;
/*  78 */   public int meowtils_blue = 255;
/*     */   public boolean guiTooltips = true;
/*     */   public boolean lowerCasePrefix = false;
/*  81 */   public String customThemeM = "WHITE";
/*  82 */   public String customThemeE = "WHITE";
/*  83 */   public String customThemeO = "WHITE";
/*  84 */   public String customThemeW = "WHITE";
/*  85 */   public String customThemeFirstBracket = "GRAY";
/*  86 */   public String customThemeSecondBracket = "GRAY";
/*  87 */   public String guiScale = "Normal";
/*  88 */   public int meowtilsCategoryX = 5;
/*  89 */   public int meowtilsCategoryY = 5;
/*  90 */   public int hypixelCategoryX = 90;
/*  91 */   public int hypixelCategoryY = 5;
/*  92 */   public int skywarsCategoryX = 175;
/*  93 */   public int skywarsCategoryY = 5;
/*  94 */   public int bedwarsCategoryX = 260;
/*  95 */   public int bedwarsCategoryY = 5;
/*  96 */   public int renderCategoryX = 345;
/*  97 */   public int renderCategoryY = 5;
/*  98 */   public int antisnipeCategoryX = 430;
/*  99 */   public int antisnipeCategoryY = 5;
/* 100 */   public int utilityCategoryX = 515;
/* 101 */   public int utilityCategoryY = 5;
/* 102 */   public int advancedCategoryX = 5;
/* 103 */   public int advancedCategoryY = 160;
/*     */   
/*     */   public boolean meowtilsCategoryExpanded = false;
/*     */   
/*     */   public boolean hypixelCategoryExpanded = false;
/*     */   
/*     */   public boolean skywarsCategoryExpanded = false;
/*     */   
/*     */   public boolean bedwarsCategoryExpanded = false;
/*     */   
/*     */   public boolean renderCategoryExpanded = false;
/*     */   
/*     */   public boolean antisnipeCategoryExpanded = false;
/*     */   public boolean utilityCategoryExpanded = false;
/*     */   public boolean advancedCategoryExpanded = false;
/*     */   public boolean warnBlacklistedPlayer = true;
/*     */   public boolean toggleNotifications = true;
/*     */   public boolean playerRemovedMessage = true;
/*     */   public boolean requeueSavedCommandMessage = true;
/*     */   public boolean failedQueueMessage = true;
/*     */   public boolean autoChannel = false;
/* 124 */   public int autoChannelKey = 0;
/*     */   
/*     */   public boolean autoGG = false;
/*     */   
/* 128 */   public int autoGGKey = 0;
/* 129 */   public String firstMessage = "gg";
/* 130 */   public String secondMessage = "Meowtils :3";
/* 131 */   public int firstMessageDelay = 0;
/* 132 */   public int secondMessageDelay = 0;
/*     */   public boolean secondMessageToggle = false;
/*     */   public boolean allowInAnyChat = false;
/*     */   public boolean autoGL = false;
/* 136 */   public String autoGLMessage = "glhf";
/* 137 */   public int autoGLDelay = 5;
/*     */   
/*     */   public boolean autoTip = false;
/*     */   
/* 141 */   public int autoTipKey = 0;
/*     */   public boolean autoTipHideMessages = true;
/* 143 */   public int autoTipDelay = 5;
/*     */   
/*     */   public boolean autoWho = false;
/*     */   
/* 147 */   public int autoWhoKey = 0;
/*     */   
/*     */   public boolean hideAutoWhoMessage = false;
/*     */   
/*     */   public boolean chatCleaner = false;
/* 152 */   public int chatCleanerKey = 0;
/*     */   
/*     */   public boolean hideBedwarsExpMessage = false;
/*     */   
/*     */   public boolean hideSendToMessage = false;
/*     */   public boolean hideWaitMessage = false;
/*     */   public boolean denicker = false;
/* 159 */   public int denickerKey = 0;
/*     */   
/*     */   public boolean rankDeobfuscator = false;
/*     */   
/*     */   public boolean nickReveal = false;
/*     */   public boolean notify = false;
/* 165 */   public int notifyKey = 0;
/*     */   
/*     */   public boolean notifyAntiCheat = true;
/*     */   
/*     */   public boolean notifyArmorAlerts = true;
/*     */   public boolean notifyBedTracker = true;
/*     */   public boolean notifyConsumeAlerts = true;
/*     */   public boolean notifyItemAlerts = true;
/*     */   public boolean notifyUpgradeAlerts = true;
/*     */   public boolean notifyDenicker = true;
/*     */   public boolean notifyPartyWarning = true;
/*     */   public boolean stats = false;
/* 177 */   public int statsKey = 0;
/*     */   
/*     */   public boolean tabStats = false;
/*     */   public boolean chatStats = true;
/* 181 */   public String starsInTab = "Prefix";
/* 182 */   public String apiKey = "";
/*     */ 
/*     */   
/*     */   public boolean cooldownHUD = false;
/*     */ 
/*     */   
/* 188 */   public int cooldownHUDKey = 0;
/* 189 */   public float cooldownHUDScale = 1.0F;
/* 190 */   public int cooldownHUDxPos = 0;
/* 191 */   public int cooldownHUDyPos = 0;
/* 192 */   public int cooldownHUDRed = 255;
/* 193 */   public int cooldownHUDGreen = 255;
/* 194 */   public int cooldownHUDBlue = 255;
/*     */   
/*     */   public boolean cooldownHUDHideWhenReady = false;
/*     */   
/*     */   public boolean equipAlerts = false;
/* 199 */   public int equipAlertsKey = 0;
/*     */   
/*     */   public boolean equipAlertsSound = false;
/*     */   
/*     */   public boolean equipAlerts_helmet = true;
/*     */   public boolean equipAlerts_chestplate = true;
/*     */   public boolean equipAlerts_leggings = true;
/*     */   public boolean equipAlerts_boots = true;
/*     */   public boolean itemHighlight = false;
/* 208 */   public int itemHighlightKey = 0;
/*     */   
/*     */   public boolean itemHighlightSkywarsOnly = true;
/*     */   
/*     */   public boolean itemHighlightBestOnly = true;
/*     */   public boolean miningAlerts = false;
/* 214 */   public int miningAlertsKey = 0;
/*     */   
/*     */   public boolean miningAlertsSound = false;
/*     */   
/*     */   public boolean noArmorDye = false;
/* 219 */   public int noArmorDyeKey = 0;
/* 220 */   public String noArmorDyeMode = "Both";
/*     */   
/*     */   public boolean skywarsAlerts = false;
/*     */   
/* 224 */   public int skywarsAlertsKey = 0;
/* 225 */   public String skywarsAlertsSoundMode = "None";
/*     */   public boolean skywarsAlertsDistance = true;
/* 227 */   public int skywarsAlertsCooldown = 10;
/*     */   
/*     */   public boolean skywarsAlerts_DiamondSword = true;
/*     */   
/*     */   public boolean skywarsAlerts_FireAspectSword = true;
/*     */   public boolean skywarsAlerts_KnockbackItems = true;
/*     */   public boolean skywarsAlerts_EnderPearls = true;
/*     */   public boolean skywarsCounter = false;
/* 235 */   public int skywarsCounterKey = 0;
/* 236 */   public float skywarsCounterScale = 1.0F;
/* 237 */   public int skywarsCounter_x = 1;
/* 238 */   public int skywarsCounter_y = 1;
/* 239 */   public int skywarsCounter_red = 255;
/* 240 */   public int skywarsCounter_green = 255;
/* 241 */   public int skywarsCounter_blue = 255;
/*     */   public boolean skywarsCounterKill = true;
/*     */   public boolean skywarsCounterExp = true;
/* 244 */   public String skywarsCounterMode = "Horizontal";
/*     */   
/*     */   public boolean skywarsCounterDynamicColor = true;
/*     */   
/*     */   public boolean skywarsCounterReset = true;
/*     */   public boolean strengthESP = false;
/* 250 */   public int strengthESPKey = 0;
/*     */   public boolean strengthESPFillBox = true;
/*     */   public boolean strengthESPFade = true;
/* 253 */   public String strengthESPMode = "3D";
/* 254 */   public int strengthESP_red = 255;
/* 255 */   public int strengthESP_green = 0;
/* 256 */   public int strengthESP_blue = 0;
/*     */   
/*     */   public boolean timeWarpDisplay = false;
/*     */   
/* 260 */   public int timeWarpDisplayKey = 0;
/* 261 */   public int timeWarpDisplay_x = 290;
/* 262 */   public int timeWarpDisplay_y = 170;
/* 263 */   public int timeWarpDisplay_red = 170;
/* 264 */   public int timeWarpDisplay_green = 0;
/* 265 */   public int timeWarpDisplay_blue = 170;
/* 266 */   public float timeWarpDisplayScale = 1.0F;
/*     */   
/*     */   public boolean timeWarpDisplayForOthers = true;
/*     */   
/*     */   public boolean timeWarpDisplayForSelf = true;
/*     */   
/*     */   public boolean armorDetector = false;
/*     */   
/* 274 */   public int armorDetectorKey = 0;
/*     */   
/*     */   public boolean armorDetectorSound = false;
/*     */   
/*     */   public boolean detectChainArmor = true;
/*     */   public boolean detectIronArmor = true;
/*     */   public boolean detectDiamondArmor = true;
/*     */   public boolean autoChest = false;
/* 282 */   public int autoChestKey = 0;
/* 283 */   public int autoChestDelay = 100;
/* 284 */   public int autoChestWaitDelay = 100;
/*     */   
/*     */   public boolean autoChestRenderClicked = true;
/*     */   
/*     */   public boolean autoChestIron = true;
/*     */   public boolean autoChestGold = true;
/*     */   public boolean autoChestDiamond = true;
/*     */   public boolean autoChestEmerald = true;
/*     */   public boolean autoClaim = false;
/* 293 */   public int autoClaimKey = 0;
/* 294 */   public int autoClaimDelay = 500;
/*     */   
/*     */   public boolean bedTracker = false;
/*     */   
/* 298 */   public int bedTrackerKey = 0;
/* 299 */   public int bedTracker_red = 255;
/* 300 */   public int bedTracker_green = 255;
/* 301 */   public int bedTracker_blue = 255;
/* 302 */   public int bedTracker_x = 1;
/* 303 */   public int bedTracker_y = 1;
/* 304 */   public float bedTrackerScale = 1.0F;
/* 305 */   public int bedTrackerAlertCooldown = 10;
/* 306 */   public int bedTrackerMaxDistance = 50;
/*     */   
/*     */   public boolean bedTrackerSound = true;
/*     */   
/*     */   public boolean bedTrackerHUD = true;
/*     */   public boolean bedwarsCounter = false;
/* 312 */   public int bedwarsCounterKey = 0;
/* 313 */   public float bedwarsCounterScale = 1.0F;
/* 314 */   public int bedwarsCounter_x = 1;
/* 315 */   public int bedwarsCounter_y = 1;
/* 316 */   public int bedwarsCounter_red = 255;
/* 317 */   public int bedwarsCounter_green = 255;
/* 318 */   public int bedwarsCounter_blue = 255;
/*     */   public boolean bedwarsCounterKill = true;
/*     */   public boolean bedwarsCounterFinal = true;
/*     */   public boolean bedwarsCounterExp = true;
/* 322 */   public String bedwarsCounterMode = "Horizontal";
/*     */   
/*     */   public boolean bedwarsCounterDynamicColor = true;
/*     */   
/*     */   public boolean bedwarsCounterReset = true;
/*     */   public boolean consumeAlerts = false;
/* 328 */   public int consumeAlertsKey = 0;
/*     */   public boolean consumeAlertsSound = false;
/*     */   public boolean consumeAlerts_goldenapple = true;
/*     */   public boolean consumeAlerts_milk = true;
/*     */   public boolean consumeAlerts_invis = true;
/*     */   public boolean consumeAlerts_speed = true;
/*     */   public boolean consumeAlerts_jump = true;
/*     */   public boolean consumeAlertsShowDistance = false;
/* 336 */   public int consumeAlerts_distance = 0;
/*     */   
/*     */   public boolean itemAlerts = false;
/*     */   
/* 340 */   public int itemAlertsKey = 0;
/* 341 */   public int itemAlertsCooldown = 10;
/* 342 */   public String itemAlertsSoundMode = "None";
/* 343 */   public String itemAlertsDistanceMode = "Important";
/*     */   
/*     */   public boolean itemAlerts_potions = true;
/*     */   
/*     */   public boolean itemAlerts_bows = true;
/*     */   public boolean itemAlerts_pickaxes = true;
/*     */   public boolean itemAlerts_mobs = true;
/*     */   public boolean itemAlerts_commonItems = true;
/*     */   public boolean itemAlerts_explosives = true;
/*     */   public boolean itemAlerts_importantItems = true;
/*     */   public boolean itemAlerts_rotationItems = true;
/*     */   public boolean resourceTracker = false;
/* 355 */   public int resourceTrackerKey = 0;
/*     */   
/*     */   public boolean resourceTrackerSound = false;
/*     */   
/*     */   public boolean trackIron = true;
/*     */   public boolean trackGold = true;
/*     */   public boolean trackDiamond = true;
/*     */   public boolean trackEmerald = true;
/*     */   public boolean resourceTrackerHideDefault = true;
/*     */   public boolean trapNotifier = false;
/* 365 */   public int trapNotifierKey = 0;
/*     */   
/*     */   public boolean trapNotifierSound = false;
/*     */   
/*     */   public boolean upgradeTracker = false;
/* 370 */   public int upgradeTrackerKey = 0;
/*     */   
/*     */   public boolean upgradeTrackerSound = false;
/*     */   
/*     */   public boolean upgradesHUD = false;
/* 375 */   public int upgradesHUDKey = 0;
/* 376 */   public int upgradesHUD_x = 1;
/* 377 */   public int upgradesHUD_y = 1;
/* 378 */   public int upgradesHUD_red = 255;
/* 379 */   public int upgradesHUD_green = 255;
/* 380 */   public int upgradesHUD_blue = 255;
/* 381 */   public float upgradesHUDScale = 1.0F;
/*     */   
/*     */   public boolean upgradesHUD_sharpness = true;
/*     */   
/*     */   public boolean upgradesHUD_protection = true;
/*     */   
/*     */   public boolean upgradesHUD_trap = true;
/*     */   
/*     */   public boolean upgradesHUD_ironforge = false;
/*     */   public boolean upgradesHUD_healpool = false;
/*     */   public boolean upgradesHUD_featherfalling = false;
/*     */   public boolean antiInvis = false;
/* 393 */   public int antiInvisKey = 0;
/* 394 */   public float antiInvisOpacity = 50.0F;
/*     */   
/*     */   public boolean blockCount = false;
/*     */   
/* 398 */   public int blockCountKey = 0;
/* 399 */   public int blockCountThreshold = 1;
/*     */   public boolean blockCountAlert = true;
/*     */   public boolean blockCountSound = true;
/* 402 */   public float blockCountScale = 1.0F;
/* 403 */   public int blockCount_red = 255;
/* 404 */   public int blockCount_green = 255;
/* 405 */   public int blockCount_blue = 255;
/* 406 */   public int blockCount_x = 310;
/* 407 */   public int blockCount_y = 220;
/*     */   
/*     */   public boolean breakProgress = false;
/*     */   
/* 411 */   public int breakProgressKey = 0;
/* 412 */   public String breakProgressMode = "Percentage";
/* 413 */   public int breakProgress_red = 255;
/* 414 */   public int breakProgress_green = 255;
/* 415 */   public int breakProgress_blue = 255;
/* 416 */   public float breakProgressScale = 1.0F;
/*     */   
/*     */   public boolean breakProgressDynamicColor = false;
/*     */   
/*     */   public boolean capeSelector = false;
/* 421 */   public int capeKey = 0;
/* 422 */   public String selectedCape = "2011";
/*     */   
/*     */   public boolean chestESP = false;
/*     */   
/* 426 */   public int chestESPKey = 0;
/* 427 */   public int chestESP_red = 255;
/* 428 */   public int chestESP_green = 255;
/* 429 */   public int chestESP_blue = 255;
/* 430 */   public int chestESPOpened_red = 255;
/* 431 */   public int chestESPOpened_green = 0;
/* 432 */   public int chestESPOpened_blue = 0;
/* 433 */   public String chestESPMode = "Normal";
/* 434 */   public String chestESPRenderMode = "Full";
/*     */   
/*     */   public boolean chestESPSkywarsOnly = false;
/*     */   
/*     */   public boolean consumeTimer = false;
/* 439 */   public int consumeTimerKey = 0;
/* 440 */   public int consumeTimerXPos = 315;
/* 441 */   public int consumeTimerYPos = 165;
/* 442 */   public String consumeTimerMode = "Ticks";
/* 443 */   public int consumeTimerRed = 255;
/* 444 */   public int consumeTimerGreen = 255;
/* 445 */   public int consumeTimerBlue = 255;
/* 446 */   public float consumeTimerScale = 1.0F;
/*     */   
/*     */   public boolean consumeTimerDynamicColor = false;
/*     */   
/*     */   public boolean fakeAutoBlock = false;
/* 451 */   public int fakeAutoBlockKey = 0;
/*     */   public boolean blockhitOnly = false;
/*     */   public boolean fakeAutoBlockKillauraOnly = false;
/*     */   public boolean fakeAutoBlockShouldBlock = false;
/* 455 */   public int fakeAutoBlockDelay = 500;
/*     */   
/*     */   public boolean healthInfo = false;
/*     */   
/* 459 */   public int healthInfoKey = 0;
/*     */   
/*     */   public boolean healthInfoDisplay = true;
/* 462 */   public int healthInfoDisplay_x = 310;
/* 463 */   public int healthInfoDisplay_y = 190;
/* 464 */   public float healthInfoDisplayScale = 1.0F;
/*     */   
/*     */   public boolean healthInfoIndicator = true;
/* 467 */   public int healthInfoIndicator_x = 310;
/* 468 */   public int healthInfoIndicator_y = 50;
/* 469 */   public float healthInfoIndicatorScale = 1.0F;
/*     */   
/*     */   public boolean healthESP = false;
/* 472 */   public String healthESPMode = "Meowtils";
/*     */   
/*     */   public boolean healthESPThroughWalls = true;
/*     */   
/*     */   public boolean instantHurt = false;
/* 477 */   public int instantHurtKey = 0;
/*     */   
/*     */   public boolean noTitles = false;
/*     */   
/* 481 */   public int noTitlesKey = 0;
/*     */   
/*     */   public boolean potionHUD = false;
/*     */   
/* 485 */   public int potionHUDKey = 0;
/* 486 */   public float potionHUDScale = 1.0F;
/* 487 */   public int potionHUDSeconds = 5;
/* 488 */   public int potionHUD_x = 1;
/* 489 */   public int potionHUD_y = 1;
/*     */   public boolean potionHUDSound = false;
/*     */   public boolean potionHUD_invis = true;
/*     */   public boolean potionHUD_jump = true;
/*     */   public boolean potionHUD_speed = true;
/*     */   public boolean potionHUD_regen = true;
/*     */   public boolean potionHUD_strength = true;
/*     */   public boolean potionHUD_fireres = true;
/*     */   public boolean potionHUD_miningfatigue = true;
/* 498 */   public String potionHUDNameMode = "Full";
/* 499 */   public String potionHUDMode = "Both";
/*     */   
/*     */   public boolean potionHUDHideInfinite = false;
/*     */   
/*     */   public boolean shinyPots = false;
/* 504 */   public int shinyPotsKey = 0;
/*     */   
/*     */   public boolean viewClip = false;
/*     */   
/* 508 */   public int viewClipKey = 0;
/*     */ 
/*     */   
/*     */   public boolean antiCheat = false;
/*     */ 
/*     */   
/* 514 */   public int antiCheatKey = 0;
/* 515 */   public int violationLevel = 0;
/*     */   public boolean flagWDRButton = false;
/*     */   public boolean flagPingSound = false;
/*     */   public boolean antiCheatAutoBlacklist = false;
/*     */   public boolean detectAutoBlock = true;
/*     */   public boolean detectLegitScaffold = true;
/*     */   public boolean detectNoSlow = true;
/*     */   public boolean detectKillaura = true;
/*     */   public boolean detectAutoClicker = true;
/* 524 */   public String flagMessageComponentColor = "RED";
/* 525 */   public String flagMessageButtonColor = "AQUA";
/* 526 */   public String flagMessageBracketColor = "GRAY";
/*     */   
/*     */   public boolean autoReport = false;
/*     */   
/* 530 */   public int autoReportKey = 0;
/* 531 */   public int autoReportClickDelay = 500;
/*     */   
/*     */   public boolean autoSafelist = false;
/*     */   
/* 535 */   public int autoSafelistKey = 0;
/*     */   
/*     */   public boolean autoSafelistMessage = true;
/*     */   
/*     */   public boolean partyWarning = false;
/* 540 */   public int partyWarningKey = 0;
/*     */   
/*     */   public boolean partyWarningSound = false;
/*     */   
/*     */   public boolean partyWarning_bedwars4 = true;
/*     */   public boolean partyWarning_bedwars3 = true;
/*     */   public boolean partyWarning_bedwars2 = true;
/*     */   public boolean sniperWarning = false;
/* 548 */   public int sniperWarningKey = 0;
/*     */   
/*     */   public boolean sniperWarningAlertParty = false;
/*     */   
/*     */   public boolean sniperWarningSound = false;
/*     */   
/*     */   public boolean actionSounds = false;
/*     */   
/* 556 */   public int actionSoundsKey = 0;
/*     */   
/*     */   public boolean actionSoundsBlock = true;
/*     */   
/*     */   public boolean actionSoundsCrit = true;
/*     */   public boolean antiObfuscate = false;
/* 562 */   public int antiObfuscateKey = 0;
/*     */   
/*     */   public boolean autoStairs = false;
/*     */   
/* 566 */   public int autoStairsKey = 0;
/*     */   
/*     */   public boolean autoSwap = false;
/*     */   
/* 570 */   public int autoSwapKey = 0;
/*     */   
/*     */   public boolean autoSwap_blocks = true;
/*     */   
/*     */   public boolean autoSwap_projectiles = true;
/*     */   public boolean autoSwap_pearls = true;
/*     */   public boolean autoSwap_swords = true;
/*     */   public boolean autoSwap_tools = true;
/*     */   public boolean autoSwap_resources = true;
/*     */   public boolean autoText;
/*     */   public int autoTextKey;
/* 581 */   public String autoText0 = "";
/* 582 */   public String autoText1 = "";
/* 583 */   public String autoText2 = "";
/* 584 */   public String autoText3 = "";
/* 585 */   public String autoText4 = "";
/* 586 */   public String autoText5 = "";
/* 587 */   public String autoText6 = "";
/* 588 */   public String autoText7 = "";
/* 589 */   public String autoText8 = "";
/* 590 */   public String autoText9 = "";
/*     */   
/*     */   public boolean hotbarLock = false;
/*     */   
/* 594 */   public int hotbarLockKey = 0;
/*     */   public boolean hotbarLock_slot1 = true;
/*     */   public boolean hotbarLock_slot2 = true;
/*     */   public boolean hotbarLock_slot3 = true;
/*     */   public boolean hotbarLock_slot4 = true;
/*     */   public boolean hotbarLock_slot5 = true;
/*     */   public boolean hotbarLock_slot6 = true;
/*     */   public boolean hotbarLock_slot7 = true;
/*     */   public boolean hotbarLock_slot8 = true;
/*     */   public boolean hotbarLock_slot9 = true;
/* 604 */   public String hotbarLockMode = "Manual";
/*     */   
/*     */   public boolean latencyAlerts = false;
/*     */   
/* 608 */   public int latencyAlertsKey = 0;
/*     */   public boolean latencyAlertsIgnoreLimbo = true;
/* 610 */   public int latencyLossThreshold = 500;
/*     */   
/*     */   public boolean nullMove = false;
/*     */   
/* 614 */   public int nullMoveKey = 0;
/*     */   
/*     */   public boolean sprint = false;
/*     */   
/* 618 */   public int sprintKey = 0;
/*     */ 
/*     */   
/*     */   public boolean antiBot = false;
/*     */ 
/*     */   
/* 624 */   public int antiBotKey = 0;
/* 625 */   public String antiBotMode = "Hypixel";
/*     */   
/*     */   public boolean delayRemover = false;
/*     */   
/* 629 */   public int delayRemoverKey = 0;
/*     */   public boolean noBreakDelay = false;
/* 631 */   public int breakDelay = 0;
/*     */   public boolean noUseDelay = true;
/* 633 */   public int noUseDelayExtraTicks = 1;
/*     */   
/*     */   public boolean noHitDelay = true;
/*     */   
/*     */   public boolean noJumpDelay = true;
/*     */   public boolean inventoryFill = false;
/* 639 */   public int inventoryFillKey = 0;
/* 640 */   public String inventoryFillClickMode = "Left";
/* 641 */   public int inventoryFillCps = 14;
/* 642 */   public int inventoryFillAutoDelay = 200;
/* 643 */   public String inventoryFillMode = "Manual";
/*     */   
/*     */   public boolean inventoryFillRenderClicked = true;
/*     */   
/*     */   public boolean inventoryMove = false;
/* 648 */   public int inventoryMoveKey = 0;
/* 649 */   public int inventoryMoveClickDelay = 1;
/*     */   
/*     */   public boolean packetDebugger = false;
/*     */   
/* 653 */   public int packetDebuggerKey = 0;
/*     */   public boolean ignorePacketSpam = true;
/* 655 */   public String packetDirection = "Both";
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/config/cfg.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */