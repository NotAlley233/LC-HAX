/*      */ package com.sun.jna;
/*      */ 
/*      */ import com.sun.jna.internal.Cleaner;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.Closeable;
/*      */ import java.io.File;
/*      */ import java.io.FilenameFilter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStreamReader;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
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
/*      */ public class NativeLibrary
/*      */   implements Closeable
/*      */ {
/*   89 */   private static final Logger LOG = Logger.getLogger(NativeLibrary.class.getName());
/*   90 */   private static final Level DEBUG_LOAD_LEVEL = Native.DEBUG_LOAD ? Level.INFO : Level.FINE;
/*   91 */   private static final SymbolProvider NATIVE_SYMBOL_PROVIDER = new SymbolProvider()
/*      */     {
/*      */       public long getSymbolAddress(long handle, String name, SymbolProvider parent) {
/*   94 */         return Native.findSymbol(handle, name);
/*      */       }
/*      */     };
/*      */   
/*      */   private Cleaner.Cleanable cleanable;
/*      */   private long handle;
/*      */   private final String libraryName;
/*      */   private final String libraryPath;
/*  102 */   private final Map<String, Function> functions = new HashMap<String, Function>();
/*      */   
/*      */   private final SymbolProvider symbolProvider;
/*      */   final int callFlags;
/*      */   private String encoding;
/*      */   final Map<String, ?> options;
/*  108 */   private static final Map<String, Reference<NativeLibrary>> libraries = new HashMap<String, Reference<NativeLibrary>>();
/*      */   
/*  110 */   private static final Map<String, List<String>> searchPaths = new ConcurrentHashMap<String, List<String>>();
/*  111 */   private static final LinkedHashSet<String> librarySearchPath = new LinkedHashSet<String>();
/*      */   private static final int DEFAULT_OPEN_OPTIONS = -1;
/*      */   
/*      */   static {
/*  115 */     if (Native.POINTER_SIZE == 0)
/*  116 */       throw new Error("Native library not initialized"); 
/*      */   }
/*      */   
/*      */   private static String functionKey(String name, int flags, String encoding) {
/*  120 */     return name + "|" + flags + "|" + encoding;
/*      */   }
/*      */   
/*      */   private NativeLibrary(String libraryName, String libraryPath, long handle, Map<String, ?> options) {
/*  124 */     this.libraryName = getLibraryName(libraryName);
/*  125 */     this.libraryPath = libraryPath;
/*  126 */     this.handle = handle;
/*  127 */     this.cleanable = Cleaner.getCleaner().register(this, new NativeLibraryDisposer(handle));
/*  128 */     Object option = options.get("calling-convention");
/*  129 */     int callingConvention = (option instanceof Number) ? ((Number)option).intValue() : 0;
/*  130 */     this.callFlags = callingConvention;
/*  131 */     this.options = options;
/*  132 */     this.encoding = (String)options.get("string-encoding");
/*  133 */     SymbolProvider optionSymbolProvider = (SymbolProvider)options.get("symbol-provider");
/*  134 */     if (optionSymbolProvider == null) {
/*  135 */       this.symbolProvider = NATIVE_SYMBOL_PROVIDER;
/*      */     } else {
/*  137 */       this.symbolProvider = optionSymbolProvider;
/*      */     } 
/*      */     
/*  140 */     if (this.encoding == null) {
/*  141 */       this.encoding = Native.getDefaultStringEncoding();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  146 */     if (Platform.isWindows() && "kernel32".equals(this.libraryName.toLowerCase())) {
/*  147 */       synchronized (this.functions) {
/*  148 */         Function f = new Function(this, "GetLastError", 63, this.encoding)
/*      */           {
/*      */             Object invoke(Object[] args, Class<?> returnType, boolean b, int fixedArgs) {
/*  151 */               return Integer.valueOf(Native.getLastError());
/*      */             }
/*      */ 
/*      */             
/*      */             Object invoke(Method invokingMethod, Class<?>[] paramTypes, Class<?> returnType, Object[] inArgs, Map<String, ?> options) {
/*  156 */               return Integer.valueOf(Native.getLastError());
/*      */             }
/*      */           };
/*  159 */         this.functions.put(functionKey("GetLastError", this.callFlags, this.encoding), f);
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private static int openFlags(Map<String, ?> options) {
/*  166 */     Object opt = options.get("open-flags");
/*  167 */     if (opt instanceof Number) {
/*  168 */       return ((Number)opt).intValue();
/*      */     }
/*  170 */     return -1;
/*      */   }
/*      */   
/*      */   private static NativeLibrary loadLibrary(String libraryName, Map<String, ?> options) {
/*  174 */     LOG.log(DEBUG_LOAD_LEVEL, "Looking for library '" + libraryName + "'");
/*      */     
/*  176 */     List<Throwable> exceptions = new ArrayList<Throwable>();
/*  177 */     boolean isAbsolutePath = (new File(libraryName)).isAbsolute();
/*  178 */     LinkedHashSet<String> searchPath = new LinkedHashSet<String>();
/*  179 */     int openFlags = openFlags(options);
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  184 */     List<String> customPaths = searchPaths.get(libraryName);
/*  185 */     if (customPaths != null) {
/*  186 */       synchronized (customPaths) {
/*  187 */         searchPath.addAll(customPaths);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*  193 */     String webstartPath = Native.getWebStartLibraryPath(libraryName);
/*  194 */     if (webstartPath != null) {
/*  195 */       LOG.log(DEBUG_LOAD_LEVEL, "Adding web start path " + webstartPath);
/*  196 */       searchPath.add(webstartPath);
/*      */     } 
/*      */     
/*  199 */     LOG.log(DEBUG_LOAD_LEVEL, "Adding paths from jna.library.path: " + System.getProperty("jna.library.path"));
/*      */     
/*  201 */     searchPath.addAll(initPaths("jna.library.path"));
/*  202 */     String libraryPath = findLibraryPath(libraryName, searchPath);
/*  203 */     long handle = 0L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     try {
/*  210 */       LOG.log(DEBUG_LOAD_LEVEL, "Trying " + libraryPath);
/*  211 */       handle = Native.open(libraryPath, openFlags);
/*  212 */     } catch (UnsatisfiedLinkError e) {
/*      */       
/*  214 */       LOG.log(DEBUG_LOAD_LEVEL, "Loading failed with message: " + e.getMessage());
/*  215 */       LOG.log(DEBUG_LOAD_LEVEL, "Adding system paths: " + librarySearchPath);
/*  216 */       exceptions.add(e);
/*  217 */       searchPath.addAll(librarySearchPath);
/*      */     } 
/*      */     
/*      */     try {
/*  221 */       if (handle == 0L) {
/*  222 */         libraryPath = findLibraryPath(libraryName, searchPath);
/*  223 */         LOG.log(DEBUG_LOAD_LEVEL, "Trying " + libraryPath);
/*  224 */         handle = Native.open(libraryPath, openFlags);
/*  225 */         if (handle == 0L) {
/*  226 */           throw new UnsatisfiedLinkError("Failed to load library '" + libraryName + "'");
/*      */         }
/*      */       } 
/*  229 */     } catch (UnsatisfiedLinkError ule) {
/*  230 */       LOG.log(DEBUG_LOAD_LEVEL, "Loading failed with message: " + ule.getMessage());
/*  231 */       exceptions.add(ule);
/*      */ 
/*      */ 
/*      */       
/*  235 */       if (Platform.isAndroid()) {
/*      */         try {
/*  237 */           LOG.log(DEBUG_LOAD_LEVEL, "Preload (via System.loadLibrary) " + libraryName);
/*  238 */           System.loadLibrary(libraryName);
/*  239 */           handle = Native.open(libraryPath, openFlags);
/*      */         }
/*  241 */         catch (UnsatisfiedLinkError e2) {
/*  242 */           LOG.log(DEBUG_LOAD_LEVEL, "Loading failed with message: " + e2.getMessage());
/*  243 */           exceptions.add(e2);
/*      */         }
/*      */       
/*  246 */       } else if (Platform.isLinux() || Platform.isFreeBSD()) {
/*      */ 
/*      */ 
/*      */         
/*  250 */         LOG.log(DEBUG_LOAD_LEVEL, "Looking for version variants");
/*  251 */         libraryPath = matchLibrary(libraryName, searchPath);
/*  252 */         if (libraryPath != null) {
/*  253 */           LOG.log(DEBUG_LOAD_LEVEL, "Trying " + libraryPath);
/*      */           try {
/*  255 */             handle = Native.open(libraryPath, openFlags);
/*      */           }
/*  257 */           catch (UnsatisfiedLinkError e2) {
/*  258 */             LOG.log(DEBUG_LOAD_LEVEL, "Loading failed with message: " + e2.getMessage());
/*  259 */             exceptions.add(e2);
/*      */           }
/*      */         
/*      */         }
/*      */       
/*  264 */       } else if (Platform.isMac() && !libraryName.endsWith(".dylib")) {
/*  265 */         for (String frameworkName : matchFramework(libraryName)) {
/*      */           try {
/*  267 */             LOG.log(DEBUG_LOAD_LEVEL, "Trying " + frameworkName);
/*  268 */             handle = Native.open(frameworkName, openFlags);
/*      */             
/*      */             break;
/*  271 */           } catch (UnsatisfiedLinkError e2) {
/*  272 */             LOG.log(DEBUG_LOAD_LEVEL, "Loading failed with message: " + e2.getMessage());
/*  273 */             exceptions.add(e2);
/*      */           }
/*      */         
/*      */         }
/*      */       
/*  278 */       } else if (Platform.isWindows() && !isAbsolutePath) {
/*  279 */         LOG.log(DEBUG_LOAD_LEVEL, "Looking for lib- prefix");
/*  280 */         libraryPath = findLibraryPath("lib" + libraryName, searchPath);
/*  281 */         if (libraryPath != null) {
/*  282 */           LOG.log(DEBUG_LOAD_LEVEL, "Trying " + libraryPath);
/*      */           try {
/*  284 */             handle = Native.open(libraryPath, openFlags);
/*  285 */           } catch (UnsatisfiedLinkError e2) {
/*  286 */             LOG.log(DEBUG_LOAD_LEVEL, "Loading failed with message: " + e2.getMessage());
/*  287 */             exceptions.add(e2);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */ 
/*      */       
/*  293 */       if (handle == 0L) {
/*      */         try {
/*  295 */           File embedded = Native.extractFromResourcePath(libraryName, (ClassLoader)options.get("classloader"));
/*  296 */           if (embedded != null) {
/*      */             try {
/*  298 */               handle = Native.open(embedded.getAbsolutePath(), openFlags);
/*  299 */               libraryPath = embedded.getAbsolutePath();
/*      */             } finally {
/*      */               
/*  302 */               if (Native.isUnpacked(embedded)) {
/*  303 */                 Native.deleteLibrary(embedded);
/*      */               }
/*      */             }
/*      */           
/*      */           }
/*  308 */         } catch (IOException e2) {
/*  309 */           LOG.log(DEBUG_LOAD_LEVEL, "Loading failed with message: " + e2.getMessage());
/*  310 */           exceptions.add(e2);
/*      */         } 
/*      */       }
/*      */       
/*  314 */       if (handle == 0L) {
/*  315 */         StringBuilder sb = new StringBuilder();
/*  316 */         sb.append("Unable to load library '");
/*  317 */         sb.append(libraryName);
/*  318 */         sb.append("':");
/*  319 */         for (Throwable t : exceptions) {
/*  320 */           sb.append("\n");
/*  321 */           sb.append(t.getMessage());
/*      */         } 
/*  323 */         UnsatisfiedLinkError res = new UnsatisfiedLinkError(sb.toString());
/*  324 */         for (Throwable t : exceptions) {
/*  325 */           addSuppressedReflected(res, t);
/*      */         }
/*  327 */         throw res;
/*      */       } 
/*      */     } 
/*      */     
/*  331 */     LOG.log(DEBUG_LOAD_LEVEL, "Found library '" + libraryName + "' at " + libraryPath);
/*  332 */     return new NativeLibrary(libraryName, libraryPath, handle, options);
/*      */   }
/*      */   
/*  335 */   private static Method addSuppressedMethod = null;
/*      */   
/*      */   static { try {
/*  338 */       addSuppressedMethod = Throwable.class.getMethod("addSuppressed", new Class[] { Throwable.class });
/*  339 */     } catch (NoSuchMethodException noSuchMethodException) {
/*      */     
/*  341 */     } catch (SecurityException ex) {
/*  342 */       Logger.getLogger(NativeLibrary.class.getName()).log(Level.SEVERE, "Failed to initialize 'addSuppressed' method", ex);
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
/*  908 */     String webstartPath = Native.getWebStartLibraryPath("jnidispatch");
/*  909 */     if (webstartPath != null) {
/*  910 */       librarySearchPath.add(webstartPath);
/*      */     }
/*  912 */     if (System.getProperty("jna.platform.library.path") == null && 
/*  913 */       !Platform.isWindows()) {
/*      */       
/*  915 */       String platformPath = "";
/*  916 */       String sep = "";
/*  917 */       String archPath = "";
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
/*  930 */       if (Platform.isLinux() || Platform.isSolaris() || 
/*  931 */         Platform.isFreeBSD() || Platform.iskFreeBSD())
/*      */       {
/*  933 */         archPath = (Platform.isSolaris() ? "/" : "") + (Native.POINTER_SIZE * 8);
/*      */       }
/*  935 */       String[] paths = { "/usr/lib" + archPath, "/lib" + archPath, "/usr/lib", "/lib" };
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
/*  946 */       if (Platform.isLinux() || Platform.iskFreeBSD() || Platform.isGNU()) {
/*  947 */         String multiArchPath = getMultiArchPath();
/*      */ 
/*      */         
/*  950 */         paths = new String[] { "/usr/lib/" + multiArchPath, "/lib/" + multiArchPath, "/usr/lib" + archPath, "/lib" + archPath, "/usr/lib", "/lib" };
/*      */       } 
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
/*  966 */       if (Platform.isLinux()) {
/*  967 */         ArrayList<String> ldPaths = getLinuxLdPaths();
/*      */         
/*  969 */         for (int j = paths.length - 1; 0 <= j; j--) {
/*  970 */           int found = ldPaths.indexOf(paths[j]);
/*  971 */           if (found != -1) {
/*  972 */             ldPaths.remove(found);
/*      */           }
/*  974 */           ldPaths.add(0, paths[j]);
/*      */         } 
/*  976 */         paths = ldPaths.<String>toArray(new String[0]);
/*      */       } 
/*      */       
/*  979 */       for (int i = 0; i < paths.length; i++) {
/*  980 */         File dir = new File(paths[i]);
/*  981 */         if (dir.exists() && dir.isDirectory()) {
/*  982 */           platformPath = platformPath + sep + paths[i];
/*  983 */           sep = File.pathSeparator;
/*      */         } 
/*      */       } 
/*  986 */       if (!"".equals(platformPath)) {
/*  987 */         System.setProperty("jna.platform.library.path", platformPath);
/*      */       }
/*      */     } 
/*  990 */     librarySearchPath.addAll(initPaths("jna.platform.library.path")); } private static void addSuppressedReflected(Throwable target, Throwable suppressed) { if (addSuppressedMethod == null) return;  try { addSuppressedMethod.invoke(target, new Object[] { suppressed }); } catch (IllegalAccessException ex) { throw new RuntimeException("Failed to call addSuppressedMethod", ex); } catch (IllegalArgumentException ex) { throw new RuntimeException("Failed to call addSuppressedMethod", ex); } catch (InvocationTargetException ex) { throw new RuntimeException("Failed to call addSuppressedMethod", ex); }  } static String[] matchFramework(String libraryName) { Set<String> paths = new LinkedHashSet<String>(); File framework = new File(libraryName); if (framework.isAbsolute()) { if (libraryName.contains(".framework")) { if (framework.exists()) return new String[] { framework.getAbsolutePath() };  paths.add(framework.getAbsolutePath()); } else { framework = new File(new File(framework.getParentFile(), framework.getName() + ".framework"), framework.getName()); if (framework.exists()) return new String[] { framework.getAbsolutePath() };  paths.add(framework.getAbsolutePath()); }  } else { String[] PREFIXES = { System.getProperty("user.home"), "", "/System" }; String suffix = !libraryName.contains(".framework") ? (libraryName + ".framework/" + libraryName) : libraryName; for (String prefix : PREFIXES) { framework = new File(prefix + "/Library/Frameworks/" + suffix); if (framework.exists()) return new String[] { framework.getAbsolutePath() };  paths.add(framework.getAbsolutePath()); }  }  return paths.<String>toArray(new String[0]); } private String getLibraryName(String libraryName) { String simplified = libraryName; String BASE = "---"; String template = mapSharedLibraryName("---"); int prefixEnd = template.indexOf("---"); if (prefixEnd > 0 && simplified.startsWith(template.substring(0, prefixEnd))) simplified = simplified.substring(prefixEnd);  String suffix = template.substring(prefixEnd + "---".length()); int suffixStart = simplified.indexOf(suffix); if (suffixStart != -1) simplified = simplified.substring(0, suffixStart);  return simplified; } public static final NativeLibrary getInstance(String libraryName) { return getInstance(libraryName, Collections.emptyMap()); } public static final NativeLibrary getInstance(String libraryName, ClassLoader classLoader) { return getInstance(libraryName, Collections.singletonMap("classloader", classLoader)); } public static final NativeLibrary getInstance(String libraryName, Map<String, ?> libraryOptions) { Map<String, Object> options = new HashMap<String, Object>(libraryOptions); if (options.get("calling-convention") == null) options.put("calling-convention", Integer.valueOf(0));  if ((Platform.isLinux() || Platform.isFreeBSD() || Platform.isAIX()) && Platform.C_LIBRARY_NAME.equals(libraryName)) libraryName = null;  synchronized (libraries) { Reference<NativeLibrary> ref = libraries.get(libraryName + options); NativeLibrary library = (ref != null) ? ref.get() : null; if (library == null) { if (libraryName == null) { library = new NativeLibrary("<process>", null, Native.open(null, openFlags(options)), options); } else { library = loadLibrary(libraryName, options); }  ref = new WeakReference<NativeLibrary>(library); libraries.put(library.getName() + options, ref); File file = library.getFile(); if (file != null) { libraries.put(file.getAbsolutePath() + options, ref); libraries.put(file.getName() + options, ref); }  }  return library; }  } public static final synchronized NativeLibrary getProcess() { return getInstance(null); } public static final synchronized NativeLibrary getProcess(Map<String, ?> options) { return getInstance((String)null, options); } public static final void addSearchPath(String libraryName, String path) { List<String> customPaths = searchPaths.get(libraryName); if (customPaths == null) { customPaths = Collections.synchronizedList(new ArrayList<String>()); searchPaths.put(libraryName, customPaths); }  customPaths.add(path); } public Function getFunction(String functionName) { return getFunction(functionName, this.callFlags); } Function getFunction(String name, Method method) { FunctionMapper mapper = (FunctionMapper)this.options.get("function-mapper"); if (mapper != null) name = mapper.getFunctionName(this, method);  String prefix = System.getProperty("jna.profiler.prefix", "$$YJP$$"); if (name.startsWith(prefix)) name = name.substring(prefix.length());  int flags = this.callFlags; Class<?>[] etypes = method.getExceptionTypes(); for (int i = 0; i < etypes.length; i++) { if (LastErrorException.class.isAssignableFrom(etypes[i])) flags |= 0x40;  }  return getFunction(name, flags); }
/*      */   public Function getFunction(String functionName, int callFlags) { return getFunction(functionName, callFlags, this.encoding); }
/*      */   public Function getFunction(String functionName, int callFlags, String encoding) { if (functionName == null) throw new NullPointerException("Function name may not be null");  synchronized (this.functions) { String key = functionKey(functionName, callFlags, encoding); Function function = this.functions.get(key); if (function == null) { function = new Function(this, functionName, callFlags, encoding); this.functions.put(key, function); }  return function; }  }
/*      */   public Map<String, ?> getOptions() { return this.options; }
/*  994 */   private static String getMultiArchPath() { String cpu = Platform.ARCH;
/*      */ 
/*      */     
/*  997 */     String kernel = Platform.iskFreeBSD() ? "-kfreebsd" : (Platform.isGNU() ? "" : "-linux");
/*  998 */     String libc = "-gnu";
/*      */     
/* 1000 */     if (Platform.isIntel()) {
/* 1001 */       cpu = Platform.is64Bit() ? "x86_64" : "i386";
/*      */     }
/* 1003 */     else if (Platform.isPPC()) {
/* 1004 */       cpu = Platform.is64Bit() ? "powerpc64" : "powerpc";
/*      */     }
/* 1006 */     else if (Platform.isARM()) {
/* 1007 */       cpu = "arm";
/* 1008 */       libc = "-gnueabi";
/*      */     }
/* 1010 */     else if (Platform.ARCH.equals("mips64el")) {
/* 1011 */       libc = "-gnuabi64";
/*      */     } 
/*      */     
/* 1014 */     return cpu + kernel + libc; }
/*      */   public Pointer getGlobalVariableAddress(String symbolName) { try { return new Pointer(getSymbolAddress(symbolName)); } catch (UnsatisfiedLinkError e) { throw new UnsatisfiedLinkError("Error looking up '" + symbolName + "': " + e.getMessage()); }  }
/*      */   long getSymbolAddress(String name) { if (this.handle == 0L) throw new UnsatisfiedLinkError("Library has been unloaded");  return this.symbolProvider.getSymbolAddress(this.handle, name, NATIVE_SYMBOL_PROVIDER); }
/*      */   public String toString() { return "Native Library <" + this.libraryPath + "@" + this.handle + ">"; }
/*      */   public String getName() { return this.libraryName; }
/*      */   public File getFile() { if (this.libraryPath == null) return null;  return new File(this.libraryPath); }
/*      */   static void disposeAll() { Set<Reference<NativeLibrary>> values; synchronized (libraries) { values = new LinkedHashSet<Reference<NativeLibrary>>(libraries.values()); }  for (Reference<NativeLibrary> ref : values) { NativeLibrary lib = ref.get(); if (lib != null) lib.close();  }  }
/* 1021 */   public void close() { Set<String> keys = new HashSet<String>(); synchronized (libraries) { for (Map.Entry<String, Reference<NativeLibrary>> e : libraries.entrySet()) { Reference<NativeLibrary> ref = e.getValue(); if (ref.get() == this) keys.add(e.getKey());  }  for (String k : keys) libraries.remove(k);  }  synchronized (this) { if (this.handle != 0L) { this.cleanable.clean(); this.handle = 0L; }  }  } private static ArrayList<String> getLinuxLdPaths() { ArrayList<String> ldPaths = new ArrayList<String>();
/* 1022 */     Process process = null;
/* 1023 */     BufferedReader reader = null;
/*      */     
/* 1025 */     try { process = Runtime.getRuntime().exec("/sbin/ldconfig -p");
/* 1026 */       reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
/*      */       String buffer;
/* 1028 */       while ((buffer = reader.readLine()) != null) {
/* 1029 */         int startPath = buffer.indexOf(" => ");
/* 1030 */         int endPath = buffer.lastIndexOf('/');
/* 1031 */         if (startPath != -1 && endPath != -1 && startPath < endPath) {
/* 1032 */           String path = buffer.substring(startPath + 4, endPath);
/* 1033 */           if (!ldPaths.contains(path)) {
/* 1034 */             ldPaths.add(path);
/*      */           }
/*      */         } 
/*      */       }  }
/* 1038 */     catch (Exception exception) {  }
/*      */     finally
/* 1040 */     { if (reader != null) {
/*      */         try {
/* 1042 */           reader.close();
/* 1043 */         } catch (IOException iOException) {}
/*      */       }
/*      */       
/* 1046 */       if (process != null) {
/*      */         try {
/* 1048 */           process.waitFor();
/* 1049 */         } catch (InterruptedException interruptedException) {}
/*      */       } }
/*      */ 
/*      */     
/* 1053 */     return ldPaths; }
/*      */   @Deprecated public void dispose() { close(); }
/*      */   private static List<String> initPaths(String key) { String value = System.getProperty(key, ""); if ("".equals(value)) return Collections.emptyList();  StringTokenizer st = new StringTokenizer(value, File.pathSeparator); List<String> list = new ArrayList<String>(); while (st.hasMoreTokens()) { String path = st.nextToken(); if (!"".equals(path)) list.add(path);  }  return list; }
/*      */   private static String findLibraryPath(String libName, Collection<String> searchPath) { if ((new File(libName)).isAbsolute()) return libName;  String name = mapSharedLibraryName(libName); for (String path : searchPath) { File file = new File(path, name); if (file.exists()) return file.getAbsolutePath();  if (Platform.isMac()) if (name.endsWith(".dylib")) { file = new File(path, name.substring(0, name.lastIndexOf(".dylib")) + ".jnilib"); if (file.exists()) return file.getAbsolutePath();  }   }  return name; }
/*      */   static String mapSharedLibraryName(String libName) { if (Platform.isMac()) { if (libName.startsWith("lib") && (libName.endsWith(".dylib") || libName.endsWith(".jnilib"))) return libName;  String name = System.mapLibraryName(libName); if (name.endsWith(".jnilib")) return name.substring(0, name.lastIndexOf(".jnilib")) + ".dylib";  return name; }  if (Platform.isLinux() || Platform.isFreeBSD()) { if (isVersionedName(libName) || libName.endsWith(".so")) return libName;  } else if (Platform.isAIX()) { if (isVersionedName(libName) || libName.endsWith(".so") || libName.startsWith("lib") || libName.endsWith(".a")) return libName;  } else if (Platform.isWindows() && (libName.endsWith(".drv") || libName.endsWith(".dll") || libName.endsWith(".ocx"))) { return libName; }  String mappedName = System.mapLibraryName(libName); if (Platform.isAIX() && mappedName.endsWith(".so")) return mappedName.replaceAll(".so$", ".a");  return mappedName; } private static boolean isVersionedName(String name) { if (name.startsWith("lib")) { int so = name.lastIndexOf(".so."); if (so != -1 && so + 4 < name.length()) { for (int i = so + 4; i < name.length(); i++) { char ch = name.charAt(i); if (!Character.isDigit(ch) && ch != '.') return false;  }  return true; }  }  return false; } static String matchLibrary(final String libName, Collection<String> searchPath) { File lib = new File(libName); if (lib.isAbsolute()) searchPath = Arrays.asList(new String[] { lib.getParent() });  FilenameFilter filter = new FilenameFilter() {
/*      */         public boolean accept(File dir, String filename) { return ((filename.startsWith("lib" + libName + ".so") || (filename.startsWith(libName + ".so") && libName.startsWith("lib"))) && NativeLibrary.isVersionedName(filename)); }
/*      */       }; Collection<File> matches = new LinkedList<File>(); for (String path : searchPath) { File[] files = (new File(path)).listFiles(filter); if (files != null && files.length > 0) matches.addAll(Arrays.asList(files));  }  double bestVersion = -1.0D; String bestMatch = null; for (File f : matches) { String path = f.getAbsolutePath(); String ver = path.substring(path.lastIndexOf(".so.") + 4); double version = parseVersion(ver); if (version > bestVersion) { bestVersion = version; bestMatch = path; }  }  return bestMatch; } static double parseVersion(String ver) { double v = 0.0D; double divisor = 1.0D; int dot = ver.indexOf("."); while (ver != null) { String num; if (dot != -1) { num = ver.substring(0, dot); ver = ver.substring(dot + 1); dot = ver.indexOf("."); } else { num = ver; ver = null; }  try { v += Integer.parseInt(num) / divisor; } catch (NumberFormatException e) { return 0.0D; }  divisor *= 100.0D; }  return v; } private static final class NativeLibraryDisposer implements Runnable
/*      */   {
/* 1061 */     private long handle; public NativeLibraryDisposer(long handle) { this.handle = handle; }
/*      */ 
/*      */     
/*      */     public synchronized void run() {
/* 1065 */       if (this.handle != 0L)
/*      */         try {
/* 1067 */           Native.close(this.handle);
/*      */         } finally {
/* 1069 */           this.handle = 0L;
/*      */         }  
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/com/sun/jna/NativeLibrary.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */