/*      */ package org.spongepowered.asm.mixin;
/*      */ 
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Sets;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import org.apache.logging.log4j.Level;
/*      */ import org.apache.logging.log4j.LogManager;
/*      */ import org.apache.logging.log4j.Logger;
/*      */ import org.apache.logging.log4j.core.Appender;
/*      */ import org.apache.logging.log4j.core.LogEvent;
/*      */ import org.apache.logging.log4j.core.Logger;
/*      */ import org.apache.logging.log4j.core.appender.AbstractAppender;
/*      */ import org.spongepowered.asm.launch.GlobalProperties;
/*      */ import org.spongepowered.asm.mixin.extensibility.IEnvironmentTokenProvider;
/*      */ import org.spongepowered.asm.mixin.throwables.MixinException;
/*      */ import org.spongepowered.asm.mixin.transformer.MixinTransformer;
/*      */ import org.spongepowered.asm.obfuscation.RemapperChain;
/*      */ import org.spongepowered.asm.service.ILegacyClassTransformer;
/*      */ import org.spongepowered.asm.service.IMixinService;
/*      */ import org.spongepowered.asm.service.ITransformer;
/*      */ import org.spongepowered.asm.service.MixinService;
/*      */ import org.spongepowered.asm.util.ITokenProvider;
/*      */ import org.spongepowered.asm.util.JavaVersion;
/*      */ import org.spongepowered.asm.util.PrettyPrinter;
/*      */ import org.spongepowered.asm.util.perf.Profiler;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class MixinEnvironment
/*      */   implements ITokenProvider
/*      */ {
/*      */   public static final class Phase
/*      */   {
/*   75 */     static final Phase NOT_INITIALISED = new Phase(-1, "NOT_INITIALISED");
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   81 */     public static final Phase PREINIT = new Phase(0, "PREINIT");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   86 */     public static final Phase INIT = new Phase(1, "INIT");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   91 */     public static final Phase DEFAULT = new Phase(2, "DEFAULT");
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*   96 */     static final List<Phase> phases = (List<Phase>)ImmutableList.of(PREINIT, INIT, DEFAULT);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final int ordinal;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     final String name;
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private MixinEnvironment environment;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private Phase(int ordinal, String name) {
/*  118 */       this.ordinal = ordinal;
/*  119 */       this.name = name;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  124 */       return this.name;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Phase forName(String name) {
/*  135 */       for (Phase phase : phases) {
/*  136 */         if (phase.name.equals(name)) {
/*  137 */           return phase;
/*      */         }
/*      */       } 
/*  140 */       return null;
/*      */     }
/*      */     
/*      */     MixinEnvironment getEnvironment() {
/*  144 */       if (this.ordinal < 0) {
/*  145 */         throw new IllegalArgumentException("Cannot access the NOT_INITIALISED environment");
/*      */       }
/*      */       
/*  148 */       if (this.environment == null) {
/*  149 */         this.environment = new MixinEnvironment(this);
/*      */       }
/*      */       
/*  152 */       return this.environment;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum Side
/*      */   {
/*  164 */     UNKNOWN
/*      */     {
/*      */       protected boolean detect() {
/*  167 */         return false;
/*      */       }
/*      */     },
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  174 */     CLIENT
/*      */     {
/*      */       protected boolean detect() {
/*  177 */         String sideName = MixinService.getService().getSideName();
/*  178 */         return "CLIENT".equals(sideName);
/*      */       }
/*      */     },
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  185 */     SERVER
/*      */     {
/*      */       protected boolean detect() {
/*  188 */         String sideName = MixinService.getService().getSideName();
/*  189 */         return ("SERVER".equals(sideName) || "DEDICATEDSERVER".equals(sideName));
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected abstract boolean detect();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum Option
/*      */   {
/*  204 */     DEBUG_ALL("debug"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  211 */     DEBUG_EXPORT((String)DEBUG_ALL, "export"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  226 */     DEBUG_EXPORT_FILTER((String)DEBUG_EXPORT, "filter", false),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  231 */     DEBUG_EXPORT_DECOMPILE((String)DEBUG_EXPORT, Inherit.ALLOW_OVERRIDE, (Option)"decompile"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  239 */     DEBUG_EXPORT_DECOMPILE_THREADED((String)DEBUG_EXPORT_DECOMPILE, Inherit.ALLOW_OVERRIDE, (Option)"async"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  246 */     DEBUG_VERIFY((String)DEBUG_ALL, "verify"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  252 */     DEBUG_VERBOSE((String)DEBUG_ALL, "verbose"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  258 */     DEBUG_INJECTORS((String)DEBUG_ALL, "countInjections"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  263 */     DEBUG_STRICT((String)DEBUG_ALL, Inherit.INDEPENDENT, (Option)"strict"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  270 */     DEBUG_UNIQUE((String)DEBUG_STRICT, "unique"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  275 */     DEBUG_TARGETS((String)DEBUG_STRICT, "targets"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  281 */     DEBUG_PROFILER((String)DEBUG_ALL, Inherit.ALLOW_OVERRIDE, (Option)"profiler"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  287 */     DUMP_TARGET_ON_FAILURE("dumpTargetOnFailure"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  292 */     CHECK_ALL("checks"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  298 */     CHECK_IMPLEMENTS((String)CHECK_ALL, "interfaces"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  306 */     CHECK_IMPLEMENTS_STRICT((String)CHECK_IMPLEMENTS, Inherit.ALLOW_OVERRIDE, (Option)"strict"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  311 */     IGNORE_CONSTRAINTS("ignoreConstraints"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  316 */     HOT_SWAP("hotSwap"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  321 */     ENVIRONMENT((String)Inherit.ALWAYS_FALSE, "env"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  326 */     OBFUSCATION_TYPE((String)ENVIRONMENT, Inherit.ALWAYS_FALSE, (Option)"obf"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  331 */     DISABLE_REFMAP((String)ENVIRONMENT, Inherit.INDEPENDENT, (Option)"disableRefMap"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  341 */     REFMAP_REMAP((String)ENVIRONMENT, Inherit.INDEPENDENT, (Option)"remapRefMap"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  351 */     REFMAP_REMAP_RESOURCE((String)ENVIRONMENT, Inherit.INDEPENDENT, (Option)"refMapRemappingFile", (Inherit)""),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  358 */     REFMAP_REMAP_SOURCE_ENV((String)ENVIRONMENT, Inherit.INDEPENDENT, (Option)"refMapRemappingEnv", (Inherit)"searge"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  363 */     IGNORE_REQUIRED((String)ENVIRONMENT, Inherit.INDEPENDENT, (Option)"ignoreRequired"),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  368 */     DEFAULT_COMPATIBILITY_LEVEL((String)ENVIRONMENT, Inherit.INDEPENDENT, (Option)"compatLevel"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  388 */     SHIFT_BY_VIOLATION_BEHAVIOUR((String)ENVIRONMENT, Inherit.INDEPENDENT, (Option)"shiftByViolation", (Inherit)"warn"),
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  394 */     INITIALISER_INJECTION_MODE("initialiserInjectionMode", "default");
/*      */     private static final String PREFIX = "mixin";
/*      */     final Option parent;
/*      */     final Inherit inheritance;
/*      */     final String property;
/*      */     final String defaultValue;
/*      */     final boolean isFlag;
/*      */     final int depth;
/*      */     
/*      */     private enum Inherit {
/*  404 */       INHERIT,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  411 */       ALLOW_OVERRIDE,
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  417 */       INDEPENDENT,
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  422 */       ALWAYS_FALSE;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     Option(Option parent, Inherit inheritance, String property, boolean isFlag, String defaultStringValue) {
/*  503 */       this.parent = parent;
/*  504 */       this.inheritance = inheritance;
/*  505 */       this.property = ((parent != null) ? parent.property : "mixin") + "." + property;
/*  506 */       this.defaultValue = defaultStringValue;
/*  507 */       this.isFlag = isFlag;
/*  508 */       int depth = 0;
/*  509 */       for (; parent != null; depth++) {
/*  510 */         parent = parent.parent;
/*      */       }
/*  512 */       this.depth = depth;
/*      */     }
/*      */     
/*      */     Option getParent() {
/*  516 */       return this.parent;
/*      */     }
/*      */     
/*      */     String getProperty() {
/*  520 */       return this.property;
/*      */     }
/*      */ 
/*      */     
/*      */     public String toString() {
/*  525 */       return this.isFlag ? String.valueOf(getBooleanValue()) : getStringValue();
/*      */     }
/*      */     
/*      */     private boolean getLocalBooleanValue(boolean defaultValue) {
/*  529 */       return Boolean.parseBoolean(System.getProperty(this.property, Boolean.toString(defaultValue)));
/*      */     }
/*      */     
/*      */     private boolean getInheritedBooleanValue() {
/*  533 */       return (this.parent != null && this.parent.getBooleanValue());
/*      */     }
/*      */     
/*      */     final boolean getBooleanValue() {
/*  537 */       if (this.inheritance == Inherit.ALWAYS_FALSE) {
/*  538 */         return false;
/*      */       }
/*      */       
/*  541 */       boolean local = getLocalBooleanValue(false);
/*  542 */       if (this.inheritance == Inherit.INDEPENDENT) {
/*  543 */         return local;
/*      */       }
/*      */       
/*  546 */       boolean inherited = (local || getInheritedBooleanValue());
/*  547 */       return (this.inheritance == Inherit.INHERIT) ? inherited : getLocalBooleanValue(inherited);
/*      */     }
/*      */     
/*      */     final String getStringValue() {
/*  551 */       return (this.inheritance == Inherit.INDEPENDENT || this.parent == null || this.parent.getBooleanValue()) ? 
/*  552 */         System.getProperty(this.property, this.defaultValue) : this.defaultValue;
/*      */     }
/*      */ 
/*      */     
/*      */     <E extends Enum<E>> E getEnumValue(E defaultValue) {
/*  557 */       String value = System.getProperty(this.property, defaultValue.name());
/*      */       try {
/*  559 */         return Enum.valueOf((Class)defaultValue.getClass(), value.toUpperCase());
/*  560 */       } catch (IllegalArgumentException ex) {
/*  561 */         return defaultValue;
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public enum CompatibilityLevel
/*      */   {
/*  574 */     JAVA_6(6, 50, false),
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  579 */     JAVA_7(7, 51, false)
/*      */     {
/*      */       boolean isSupported()
/*      */       {
/*  583 */         return (JavaVersion.current() >= 1.7D);
/*      */       }
/*      */     },
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  591 */     JAVA_8(8, 52, true)
/*      */     {
/*      */       boolean isSupported()
/*      */       {
/*  595 */         return (JavaVersion.current() >= 1.8D);
/*      */       }
/*      */     },
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  603 */     JAVA_9(9, 53, true)
/*      */     {
/*      */       boolean isSupported()
/*      */       {
/*  607 */         return false;
/*      */       }
/*      */     };
/*      */ 
/*      */     
/*      */     private static final int CLASS_V1_9 = 53;
/*      */     
/*      */     private final int ver;
/*      */     
/*      */     private final int classVersion;
/*      */     
/*      */     private final boolean supportsMethodsInInterfaces;
/*      */     
/*      */     private CompatibilityLevel maxCompatibleLevel;
/*      */ 
/*      */     
/*      */     CompatibilityLevel(int ver, int classVersion, boolean resolveMethodsInInterfaces) {
/*  624 */       this.ver = ver;
/*  625 */       this.classVersion = classVersion;
/*  626 */       this.supportsMethodsInInterfaces = resolveMethodsInInterfaces;
/*      */     }
/*      */ 
/*      */     
/*      */     private void setMaxCompatibleLevel(CompatibilityLevel maxCompatibleLevel) {
/*  631 */       this.maxCompatibleLevel = maxCompatibleLevel;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     boolean isSupported() {
/*  639 */       return true;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int classVersion() {
/*  646 */       return this.classVersion;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean supportsMethodsInInterfaces() {
/*  654 */       return this.supportsMethodsInInterfaces;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean isAtLeast(CompatibilityLevel level) {
/*  665 */       return (level == null || this.ver >= level.ver);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean canElevateTo(CompatibilityLevel level) {
/*  675 */       if (level == null || this.maxCompatibleLevel == null) {
/*  676 */         return true;
/*      */       }
/*  678 */       return (level.ver <= this.maxCompatibleLevel.ver);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean canSupport(CompatibilityLevel level) {
/*  688 */       if (level == null) {
/*  689 */         return true;
/*      */       }
/*      */       
/*  692 */       return level.canElevateTo(this);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class TokenProviderWrapper
/*      */     implements Comparable<TokenProviderWrapper>
/*      */   {
/*  702 */     private static int nextOrder = 0;
/*      */     
/*      */     private final int priority;
/*      */     
/*      */     private final int order;
/*      */     private final IEnvironmentTokenProvider provider;
/*      */     private final MixinEnvironment environment;
/*      */     
/*      */     public TokenProviderWrapper(IEnvironmentTokenProvider provider, MixinEnvironment environment) {
/*  711 */       this.provider = provider;
/*  712 */       this.environment = environment;
/*  713 */       this.order = nextOrder++;
/*  714 */       this.priority = provider.getPriority();
/*      */     }
/*      */ 
/*      */     
/*      */     public int compareTo(TokenProviderWrapper other) {
/*  719 */       if (other == null) {
/*  720 */         return 0;
/*      */       }
/*  722 */       if (other.priority == this.priority) {
/*  723 */         return other.order - this.order;
/*      */       }
/*  725 */       return other.priority - this.priority;
/*      */     }
/*      */     
/*      */     public IEnvironmentTokenProvider getProvider() {
/*  729 */       return this.provider;
/*      */     }
/*      */     
/*      */     Integer getToken(String token) {
/*  733 */       return this.provider.getToken(token, this.environment);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static class MixinLogWatcher
/*      */   {
/*  743 */     static MixinAppender appender = new MixinAppender();
/*      */     static Logger log;
/*  745 */     static Level oldLevel = null;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static void begin() {
/*  761 */       Logger fmlLog = LogManager.getLogger("FML");
/*  762 */       if (!(fmlLog instanceof Logger)) {
/*      */         return;
/*      */       }
/*      */       
/*  766 */       log = (Logger)fmlLog;
/*  767 */       oldLevel = log.getLevel();
/*      */       
/*  769 */       appender.start();
/*  770 */       log.addAppender((Appender)appender);
/*      */       
/*  772 */       log.setLevel(Level.ALL);
/*      */     }
/*      */     
/*      */     static void end() {
/*  776 */       if (log != null)
/*      */       {
/*  778 */         log.removeAppender((Appender)appender);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     static class MixinAppender
/*      */       extends AbstractAppender
/*      */     {
/*      */       MixinAppender() {
/*  788 */         super("MixinLogWatcherAppender", null, null);
/*      */       }
/*      */ 
/*      */       
/*      */       public void append(LogEvent event) {
/*  793 */         if (event.getLevel() != Level.DEBUG || !"Validating minecraft".equals(event.getMessage().getFormattedMessage())) {
/*      */           return;
/*      */         }
/*      */ 
/*      */         
/*  798 */         MixinEnvironment.gotoPhase(MixinEnvironment.Phase.INIT);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  805 */         if (MixinEnvironment.MixinLogWatcher.log.getLevel() == Level.ALL) {
/*  806 */           MixinEnvironment.MixinLogWatcher.log.setLevel(MixinEnvironment.MixinLogWatcher.oldLevel);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  817 */   private static final Set<String> excludeTransformers = Sets.newHashSet((Object[])new String[] { "net.minecraftforge.fml.common.asm.transformers.EventSubscriptionTransformer", "cpw.mods.fml.common.asm.transformers.EventSubscriptionTransformer", "net.minecraftforge.fml.common.asm.transformers.TerminalTransformer", "cpw.mods.fml.common.asm.transformers.TerminalTransformer" });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static MixinEnvironment currentEnvironment;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  833 */   private static Phase currentPhase = Phase.NOT_INITIALISED;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  838 */   private static CompatibilityLevel compatibility = Option.DEFAULT_COMPATIBILITY_LEVEL.<CompatibilityLevel>getEnumValue(CompatibilityLevel.JAVA_6);
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static boolean showHeader = true;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  848 */   private static final Logger logger = LogManager.getLogger("mixin");
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  853 */   private static final Profiler profiler = new Profiler();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final IMixinService service;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final Phase phase;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final String configsKey;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private final boolean[] options;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  878 */   private final Set<String> tokenProviderClasses = new HashSet<String>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  883 */   private final List<TokenProviderWrapper> tokenProviders = new ArrayList<TokenProviderWrapper>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  888 */   private final Map<String, Integer> internalTokens = new HashMap<String, Integer>();
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  893 */   private final RemapperChain remappers = new RemapperChain();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private Side side;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<ILegacyClassTransformer> transformers;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  911 */   private String obfuscationContext = null;
/*      */   
/*      */   MixinEnvironment(Phase phase) {
/*  914 */     this.service = MixinService.getService();
/*  915 */     this.phase = phase;
/*  916 */     this.configsKey = "mixin.configs." + this.phase.name.toLowerCase();
/*      */ 
/*      */     
/*  919 */     Object version = getVersion();
/*  920 */     if (version == null || !"0.7.10".equals(version)) {
/*  921 */       throw new MixinException("Environment conflict, mismatched versions or you didn't call MixinBootstrap.init()");
/*      */     }
/*      */ 
/*      */     
/*  925 */     this.service.checkEnv(this);
/*      */     
/*  927 */     this.options = new boolean[(Option.values()).length];
/*  928 */     for (Option option : Option.values()) {
/*  929 */       this.options[option.ordinal()] = option.getBooleanValue();
/*      */     }
/*      */     
/*  932 */     if (showHeader) {
/*  933 */       showHeader = false;
/*  934 */       printHeader(version);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void printHeader(Object version) {
/*  939 */     String codeSource = getCodeSource();
/*  940 */     String serviceName = this.service.getName();
/*  941 */     Side side = getSide();
/*  942 */     logger.info("SpongePowered MIXIN Subsystem Version={} Source={} Service={} Env={}", new Object[] { version, codeSource, serviceName, side });
/*      */     
/*  944 */     boolean verbose = getOption(Option.DEBUG_VERBOSE);
/*  945 */     if (verbose || getOption(Option.DEBUG_EXPORT) || getOption(Option.DEBUG_PROFILER)) {
/*  946 */       PrettyPrinter printer = new PrettyPrinter(32);
/*  947 */       printer.add("SpongePowered MIXIN%s", new Object[] { verbose ? " (Verbose debugging enabled)" : "" }).centre().hr();
/*  948 */       printer.kv("Code source", codeSource);
/*  949 */       printer.kv("Internal Version", version);
/*  950 */       printer.kv("Java 8 Supported", Boolean.valueOf(CompatibilityLevel.JAVA_8.isSupported())).hr();
/*  951 */       printer.kv("Service Name", serviceName);
/*  952 */       printer.kv("Service Class", this.service.getClass().getName()).hr();
/*  953 */       for (Option option : Option.values()) {
/*  954 */         StringBuilder indent = new StringBuilder();
/*  955 */         for (int i = 0; i < option.depth; i++) {
/*  956 */           indent.append("- ");
/*      */         }
/*  958 */         printer.kv(option.property, "%s<%s>", new Object[] { indent, option });
/*      */       } 
/*  960 */       printer.hr().kv("Detected Side", side);
/*  961 */       printer.print(System.err);
/*      */     } 
/*      */   }
/*      */   
/*      */   private String getCodeSource() {
/*      */     try {
/*  967 */       return getClass().getProtectionDomain().getCodeSource().getLocation().toString();
/*  968 */     } catch (Throwable th) {
/*  969 */       return "Unknown";
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Phase getPhase() {
/*  979 */     return this.phase;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public List<String> getMixinConfigs() {
/*  990 */     List<String> mixinConfigs = (List<String>)GlobalProperties.get(this.configsKey);
/*  991 */     if (mixinConfigs == null) {
/*  992 */       mixinConfigs = new ArrayList<String>();
/*  993 */       GlobalProperties.put(this.configsKey, mixinConfigs);
/*      */     } 
/*  995 */     return mixinConfigs;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public MixinEnvironment addConfiguration(String config) {
/* 1007 */     logger.warn("MixinEnvironment::addConfiguration is deprecated and will be removed. Use Mixins::addConfiguration instead!");
/* 1008 */     Mixins.addConfiguration(config, this);
/* 1009 */     return this;
/*      */   }
/*      */   
/*      */   void registerConfig(String config) {
/* 1013 */     List<String> configs = getMixinConfigs();
/* 1014 */     if (!configs.contains(config)) {
/* 1015 */       configs.add(config);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public MixinEnvironment registerErrorHandlerClass(String handlerName) {
/* 1028 */     Mixins.registerErrorHandlerClass(handlerName);
/* 1029 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MixinEnvironment registerTokenProviderClass(String providerName) {
/* 1039 */     if (!this.tokenProviderClasses.contains(providerName)) {
/*      */       
/*      */       try {
/*      */         
/* 1043 */         Class<? extends IEnvironmentTokenProvider> providerClass = this.service.getClassProvider().findClass(providerName, true);
/* 1044 */         IEnvironmentTokenProvider provider = providerClass.newInstance();
/* 1045 */         registerTokenProvider(provider);
/* 1046 */       } catch (Throwable th) {
/* 1047 */         logger.error("Error instantiating " + providerName, th);
/*      */       } 
/*      */     }
/* 1050 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MixinEnvironment registerTokenProvider(IEnvironmentTokenProvider provider) {
/* 1060 */     if (provider != null && !this.tokenProviderClasses.contains(provider.getClass().getName())) {
/* 1061 */       String providerName = provider.getClass().getName();
/* 1062 */       TokenProviderWrapper wrapper = new TokenProviderWrapper(provider, this);
/* 1063 */       logger.info("Adding new token provider {} to {}", new Object[] { providerName, this });
/* 1064 */       this.tokenProviders.add(wrapper);
/* 1065 */       this.tokenProviderClasses.add(providerName);
/* 1066 */       Collections.sort(this.tokenProviders);
/*      */     } 
/*      */     
/* 1069 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Integer getToken(String token) {
/* 1081 */     token = token.toUpperCase();
/*      */     
/* 1083 */     for (TokenProviderWrapper provider : this.tokenProviders) {
/* 1084 */       Integer value = provider.getToken(token);
/* 1085 */       if (value != null) {
/* 1086 */         return value;
/*      */       }
/*      */     } 
/*      */     
/* 1090 */     return this.internalTokens.get(token);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public Set<String> getErrorHandlerClasses() {
/* 1101 */     return Mixins.getErrorHandlerClasses();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object getActiveTransformer() {
/* 1110 */     return GlobalProperties.get("mixin.transformer");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setActiveTransformer(ITransformer transformer) {
/* 1119 */     if (transformer != null) {
/* 1120 */       GlobalProperties.put("mixin.transformer", transformer);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public MixinEnvironment setSide(Side side) {
/* 1131 */     if (side != null && getSide() == Side.UNKNOWN && side != Side.UNKNOWN) {
/* 1132 */       this.side = side;
/*      */     }
/* 1134 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Side getSide() {
/* 1143 */     if (this.side == null) {
/* 1144 */       for (Side side : Side.values()) {
/* 1145 */         if (side.detect()) {
/* 1146 */           this.side = side;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     }
/* 1152 */     return (this.side != null) ? this.side : Side.UNKNOWN;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getVersion() {
/* 1161 */     return (String)GlobalProperties.get("mixin.initialised");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getOption(Option option) {
/* 1171 */     return this.options[option.ordinal()];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setOption(Option option, boolean value) {
/* 1181 */     this.options[option.ordinal()] = value;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getOptionValue(Option option) {
/* 1191 */     return option.getStringValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <E extends Enum<E>> E getOption(Option option, E defaultValue) {
/* 1203 */     return option.getEnumValue(defaultValue);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setObfuscationContext(String context) {
/* 1212 */     this.obfuscationContext = context;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getObfuscationContext() {
/* 1219 */     return this.obfuscationContext;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String getRefmapObfuscationContext() {
/* 1226 */     String overrideObfuscationType = Option.OBFUSCATION_TYPE.getStringValue();
/* 1227 */     if (overrideObfuscationType != null) {
/* 1228 */       return overrideObfuscationType;
/*      */     }
/* 1230 */     return this.obfuscationContext;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public RemapperChain getRemappers() {
/* 1237 */     return this.remappers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void audit() {
/* 1244 */     Object activeTransformer = getActiveTransformer();
/* 1245 */     if (activeTransformer instanceof MixinTransformer) {
/* 1246 */       MixinTransformer transformer = (MixinTransformer)activeTransformer;
/* 1247 */       transformer.audit(this);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<ILegacyClassTransformer> getTransformers() {
/* 1258 */     if (this.transformers == null) {
/* 1259 */       buildTransformerDelegationList();
/*      */     }
/*      */     
/* 1262 */     return Collections.unmodifiableList(this.transformers);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void addTransformerExclusion(String name) {
/* 1271 */     excludeTransformers.add(name);
/*      */ 
/*      */     
/* 1274 */     this.transformers = null;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void buildTransformerDelegationList() {
/* 1284 */     logger.debug("Rebuilding transformer delegation list:");
/* 1285 */     this.transformers = new ArrayList<ILegacyClassTransformer>();
/* 1286 */     for (ITransformer transformer : this.service.getTransformers()) {
/* 1287 */       if (!(transformer instanceof ILegacyClassTransformer)) {
/*      */         continue;
/*      */       }
/*      */       
/* 1291 */       ILegacyClassTransformer legacyTransformer = (ILegacyClassTransformer)transformer;
/* 1292 */       String transformerName = legacyTransformer.getName();
/* 1293 */       boolean include = true;
/* 1294 */       for (String excludeClass : excludeTransformers) {
/* 1295 */         if (transformerName.contains(excludeClass)) {
/* 1296 */           include = false;
/*      */           break;
/*      */         } 
/*      */       } 
/* 1300 */       if (include && !legacyTransformer.isDelegationExcluded()) {
/* 1301 */         logger.debug("  Adding:    {}", new Object[] { transformerName });
/* 1302 */         this.transformers.add(legacyTransformer); continue;
/*      */       } 
/* 1304 */       logger.debug("  Excluding: {}", new Object[] { transformerName });
/*      */     } 
/*      */ 
/*      */     
/* 1308 */     logger.debug("Transformer delegation list created with {} entries", new Object[] { Integer.valueOf(this.transformers.size()) });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1316 */     return String.format("%s[%s]", new Object[] { getClass().getSimpleName(), this.phase });
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Phase getCurrentPhase() {
/* 1323 */     if (currentPhase == Phase.NOT_INITIALISED) {
/* 1324 */       init(Phase.PREINIT);
/*      */     }
/*      */     
/* 1327 */     return currentPhase;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void init(Phase phase) {
/* 1336 */     if (currentPhase == Phase.NOT_INITIALISED) {
/* 1337 */       currentPhase = phase;
/* 1338 */       MixinEnvironment env = getEnvironment(phase);
/* 1339 */       getProfiler().setActive(env.getOption(Option.DEBUG_PROFILER));
/*      */       
/* 1341 */       MixinLogWatcher.begin();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MixinEnvironment getEnvironment(Phase phase) {
/* 1352 */     if (phase == null) {
/* 1353 */       return Phase.DEFAULT.getEnvironment();
/*      */     }
/* 1355 */     return phase.getEnvironment();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MixinEnvironment getDefaultEnvironment() {
/* 1364 */     return getEnvironment(Phase.DEFAULT);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static MixinEnvironment getCurrentEnvironment() {
/* 1373 */     if (currentEnvironment == null) {
/* 1374 */       currentEnvironment = getEnvironment(getCurrentPhase());
/*      */     }
/*      */     
/* 1377 */     return currentEnvironment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static CompatibilityLevel getCompatibilityLevel() {
/* 1384 */     return compatibility;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static void setCompatibilityLevel(CompatibilityLevel level) throws IllegalArgumentException {
/* 1396 */     StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
/* 1397 */     if (!"org.spongepowered.asm.mixin.transformer.MixinConfig".equals(stackTrace[2].getClassName())) {
/* 1398 */       logger.warn("MixinEnvironment::setCompatibilityLevel is deprecated and will be removed. Set level via config instead!");
/*      */     }
/*      */     
/* 1401 */     if (level != compatibility && level.isAtLeast(compatibility)) {
/* 1402 */       if (!level.isSupported()) {
/* 1403 */         throw new IllegalArgumentException("The requested compatibility level " + level + " could not be set. Level is not supported");
/*      */       }
/*      */       
/* 1406 */       compatibility = level;
/* 1407 */       logger.info("Compatibility level set to {}", new Object[] { level });
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Profiler getProfiler() {
/* 1417 */     return profiler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void gotoPhase(Phase phase) {
/* 1426 */     if (phase == null || phase.ordinal < 0) {
/* 1427 */       throw new IllegalArgumentException("Cannot go to the specified phase, phase is null or invalid");
/*      */     }
/*      */     
/* 1430 */     if (phase.ordinal > (getCurrentPhase()).ordinal) {
/* 1431 */       MixinService.getService().beginPhase();
/*      */     }
/*      */     
/* 1434 */     if (phase == Phase.DEFAULT) {
/* 1435 */       MixinLogWatcher.end();
/*      */     }
/*      */     
/* 1438 */     currentPhase = phase;
/* 1439 */     currentEnvironment = getEnvironment(getCurrentPhase());
/*      */   }
/*      */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/org/spongepowered/asm/mixin/MixinEnvironment.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */