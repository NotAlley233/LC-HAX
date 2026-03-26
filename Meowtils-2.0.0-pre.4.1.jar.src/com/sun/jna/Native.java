/*      */ package com.sun.jna;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.GraphicsEnvironment;
/*      */ import java.awt.HeadlessException;
/*      */ import java.awt.Window;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FilenameFilter;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.UnsupportedEncodingException;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationHandler;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.net.URL;
/*      */ import java.net.URLClassLoader;
/*      */ import java.nio.Buffer;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.IllegalCharsetNameException;
/*      */ import java.nio.charset.UnsupportedCharsetException;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.WeakHashMap;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class Native
/*      */   implements Version
/*      */ {
/*  114 */   private static final Logger LOG = Logger.getLogger(Native.class.getName());
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static final Charset DEFAULT_CHARSET;
/*      */ 
/*      */ 
/*      */   
/*      */   public static final String DEFAULT_ENCODING;
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/*  129 */     String nativeEncoding = System.getProperty("native.encoding");
/*  130 */     Charset nativeCharset = null;
/*  131 */     if (nativeEncoding != null) {
/*      */       try {
/*  133 */         nativeCharset = Charset.forName(nativeEncoding);
/*  134 */       } catch (Exception ex) {
/*  135 */         LOG.log(Level.WARNING, "Failed to get charset for native.encoding value : '" + nativeEncoding + "'", ex);
/*      */       } 
/*      */     }
/*  138 */     if (nativeCharset == null) {
/*  139 */       nativeCharset = Charset.defaultCharset();
/*      */     }
/*  141 */     DEFAULT_CHARSET = nativeCharset;
/*  142 */     DEFAULT_ENCODING = nativeCharset.name();
/*      */   }
/*  144 */   public static final boolean DEBUG_LOAD = Boolean.getBoolean("jna.debug_load");
/*  145 */   public static final boolean DEBUG_JNA_LOAD = Boolean.getBoolean("jna.debug_load.jna");
/*  146 */   private static final Level DEBUG_JNA_LOAD_LEVEL = DEBUG_JNA_LOAD ? Level.INFO : Level.FINE;
/*      */ 
/*      */   
/*  149 */   static String jnidispatchPath = null;
/*  150 */   private static final Map<Class<?>, Map<String, Object>> typeOptions = Collections.synchronizedMap(new WeakHashMap<Class<?>, Map<String, Object>>());
/*  151 */   private static final Map<Class<?>, Reference<?>> libraries = Collections.synchronizedMap(new WeakHashMap<Class<?>, Reference<?>>()); private static final String _OPTION_ENCLOSING_LIBRARY = "enclosing-library";
/*      */   
/*  153 */   private static final Callback.UncaughtExceptionHandler DEFAULT_HANDLER = new Callback.UncaughtExceptionHandler()
/*      */     {
/*      */       public void uncaughtException(Callback c, Throwable e)
/*      */       {
/*  157 */         Native.LOG.log(Level.WARNING, "JNA: Callback " + c + " threw the following exception", e);
/*      */       }
/*      */     };
/*  160 */   private static Callback.UncaughtExceptionHandler callbackExceptionHandler = DEFAULT_HANDLER;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean isCompatibleVersion(String expectedVersion, String nativeVersion) {
/*  198 */     String[] expectedVersionParts = expectedVersion.split("\\.");
/*  199 */     String[] nativeVersionParts = nativeVersion.split("\\.");
/*  200 */     if (expectedVersionParts.length < 3 || nativeVersionParts.length < 3) {
/*  201 */       return false;
/*      */     }
/*      */     
/*  204 */     int expectedMajor = Integer.parseInt(expectedVersionParts[0]);
/*  205 */     int nativeMajor = Integer.parseInt(nativeVersionParts[0]);
/*  206 */     int expectedMinor = Integer.parseInt(expectedVersionParts[1]);
/*  207 */     int nativeMinor = Integer.parseInt(nativeVersionParts[1]);
/*      */     
/*  209 */     if (expectedMajor != nativeMajor) {
/*  210 */       return false;
/*      */     }
/*      */     
/*  213 */     if (expectedMinor > nativeMinor) {
/*  214 */       return false;
/*      */     }
/*      */     
/*  217 */     return true;
/*      */   }
/*      */   
/*      */   static {
/*  221 */     loadNativeDispatchLibrary();
/*      */     
/*  223 */     if (!isCompatibleVersion("6.1.6", getNativeVersion())) {
/*  224 */       String LS = System.getProperty("line.separator");
/*  225 */       throw new Error(LS + LS + "There is an incompatible JNA native library installed on this system" + LS + "Expected: " + "6.1.6" + LS + "Found:    " + 
/*      */ 
/*      */           
/*  228 */           getNativeVersion() + LS + ((jnidispatchPath != null) ? ("(at " + jnidispatchPath + ")") : 
/*      */           
/*  230 */           System.getProperty("java.library.path")) + "." + LS + "To resolve this issue you may do one of the following:" + LS + " - remove or uninstall the offending library" + LS + " - set the system property jna.nosys=true" + LS + " - set jna.boot.library.path to include the path to the version of the " + LS + "   jnidispatch library included with the JNA jar file you are using" + LS);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  239 */   public static final int POINTER_SIZE = sizeof(0);
/*  240 */   public static final int LONG_SIZE = sizeof(1);
/*  241 */   public static final int WCHAR_SIZE = sizeof(2);
/*  242 */   public static final int SIZE_T_SIZE = sizeof(3);
/*  243 */   public static final int BOOL_SIZE = sizeof(4);
/*  244 */   public static final int LONG_DOUBLE_SIZE = sizeof(5); private static final int TYPE_VOIDP = 0; private static final int TYPE_LONG = 1; private static final int TYPE_WCHAR_T = 2; private static final int TYPE_SIZE_T = 3; private static final int TYPE_BOOL = 4;
/*      */   private static final int TYPE_LONG_DOUBLE = 5;
/*      */   
/*      */   static {
/*  248 */     initIDs();
/*  249 */     if (Boolean.getBoolean("jna.protected"))
/*  250 */       setProtected(true); 
/*      */   }
/*  252 */   static final int MAX_ALIGNMENT = (Platform.isSPARC() || Platform.isWindows() || (
/*  253 */     Platform.isLinux() && (Platform.isARM() || Platform.isPPC() || Platform.isMIPS() || Platform.isLoongArch())) || 
/*  254 */     Platform.isAIX() || (
/*  255 */     Platform.isAndroid() && !Platform.isIntel())) ? 8 : LONG_SIZE;
/*      */   
/*  257 */   static final int MAX_PADDING = (Platform.isMac() && Platform.isPPC()) ? 8 : MAX_ALIGNMENT; static {
/*  258 */     System.setProperty("jna.loaded", "true");
/*      */   }
/*      */ 
/*      */   
/*  262 */   private static final Object finalizer = new Object()
/*      */     {
/*      */       protected void finalize() throws Throwable {
/*  265 */         Native.dispose();
/*  266 */         super.finalize();
/*      */       }
/*      */     };
/*      */ 
/*      */   
/*      */   static final String JNA_TMPLIB_PREFIX = "jna";
/*      */ 
/*      */   
/*      */   private static void dispose() {
/*  275 */     CallbackReference.disposeAll();
/*  276 */     Memory.disposeAll();
/*  277 */     NativeLibrary.disposeAll();
/*  278 */     unregisterAll();
/*  279 */     jnidispatchPath = null;
/*  280 */     System.setProperty("jna.loaded", "false");
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
/*      */ 
/*      */ 
/*      */   
/*      */   static boolean deleteLibrary(File lib) {
/*  295 */     if (lib.delete()) {
/*  296 */       return true;
/*      */     }
/*      */ 
/*      */     
/*  300 */     markTemporaryFile(lib);
/*      */     
/*  302 */     return false;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getWindowID(Window w) throws HeadlessException {
/*  342 */     return AWT.getWindowID(w);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static long getComponentID(Component c) throws HeadlessException {
/*  352 */     return AWT.getComponentID(c);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Pointer getWindowPointer(Window w) throws HeadlessException {
/*  362 */     return new Pointer(AWT.getWindowID(w));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Pointer getComponentPointer(Component c) throws HeadlessException {
/*  372 */     return new Pointer(AWT.getComponentID(c));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Pointer getDirectBufferPointer(Buffer b) {
/*  381 */     long peer = _getDirectBufferPointer(b);
/*  382 */     return (peer == 0L) ? null : new Pointer(peer);
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
/*      */   
/*      */   private static Charset getCharset(String encoding) {
/*  395 */     Charset charset = null;
/*  396 */     if (encoding != null) {
/*      */       try {
/*  398 */         charset = Charset.forName(encoding);
/*      */       }
/*  400 */       catch (IllegalCharsetNameException e) {
/*  401 */         LOG.log(Level.WARNING, "JNA Warning: Encoding ''{0}'' is unsupported ({1})", new Object[] { encoding, e
/*  402 */               .getMessage() });
/*      */       }
/*  404 */       catch (UnsupportedCharsetException e) {
/*  405 */         LOG.log(Level.WARNING, "JNA Warning: Encoding ''{0}'' is unsupported ({1})", new Object[] { encoding, e
/*  406 */               .getMessage() });
/*      */       } 
/*      */     }
/*  409 */     if (charset == null) {
/*  410 */       LOG.log(Level.WARNING, "JNA Warning: Using fallback encoding {0}", DEFAULT_CHARSET);
/*  411 */       charset = DEFAULT_CHARSET;
/*      */     } 
/*  413 */     return charset;
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
/*      */   public static String toString(byte[] buf) {
/*  425 */     return toString(buf, getDefaultStringEncoding());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(byte[] buf, String encoding) {
/*  442 */     return toString(buf, getCharset(encoding));
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
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(byte[] buf, Charset charset) {
/*  458 */     int len = buf.length;
/*      */     
/*  460 */     for (int index = 0; index < len; index++) {
/*  461 */       if (buf[index] == 0) {
/*  462 */         len = index;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  467 */     if (len == 0) {
/*  468 */       return "";
/*      */     }
/*      */     
/*  471 */     return new String(buf, 0, len, charset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String toString(char[] buf) {
/*  481 */     int len = buf.length;
/*  482 */     for (int index = 0; index < len; index++) {
/*  483 */       if (buf[index] == '\000') {
/*  484 */         len = index;
/*      */         
/*      */         break;
/*      */       } 
/*      */     } 
/*  489 */     if (len == 0) {
/*  490 */       return "";
/*      */     }
/*  492 */     return new String(buf, 0, len);
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
/*      */ 
/*      */   
/*      */   public static List<String> toStringList(char[] buf) {
/*  506 */     return toStringList(buf, 0, buf.length);
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
/*      */ 
/*      */   
/*      */   public static List<String> toStringList(char[] buf, int offset, int len) {
/*  520 */     List<String> list = new ArrayList<String>();
/*  521 */     int lastPos = offset;
/*  522 */     int maxPos = offset + len;
/*  523 */     for (int curPos = offset; curPos < maxPos; curPos++) {
/*  524 */       if (buf[curPos] == '\000') {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  529 */         if (lastPos == curPos) {
/*  530 */           return list;
/*      */         }
/*      */         
/*  533 */         String value = new String(buf, lastPos, curPos - lastPos);
/*  534 */         list.add(value);
/*  535 */         lastPos = curPos + 1;
/*      */       } 
/*      */     } 
/*      */     
/*  539 */     if (lastPos < maxPos) {
/*  540 */       String value = new String(buf, lastPos, maxPos - lastPos);
/*  541 */       list.add(value);
/*      */     } 
/*      */     
/*  544 */     return list;
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends Library> T load(Class<T> interfaceClass) {
/*  559 */     return load((String)null, interfaceClass);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends Library> T load(Class<T> interfaceClass, Map<String, ?> options) {
/*  578 */     return load(null, interfaceClass, options);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends Library> T load(String name, Class<T> interfaceClass) {
/*  596 */     return load(name, interfaceClass, Collections.emptyMap());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends Library> T load(String name, Class<T> interfaceClass, Map<String, ?> options) {
/*  616 */     if (!Library.class.isAssignableFrom(interfaceClass))
/*      */     {
/*  618 */       throw new IllegalArgumentException("Interface (" + interfaceClass.getSimpleName() + ") of library=" + name + " does not extend " + Library.class
/*  619 */           .getSimpleName());
/*      */     }
/*      */     
/*  622 */     Library.Handler handler = new Library.Handler(name, interfaceClass, options);
/*  623 */     ClassLoader loader = interfaceClass.getClassLoader();
/*  624 */     Object proxy = Proxy.newProxyInstance(loader, new Class[] { interfaceClass }, handler);
/*  625 */     cacheOptions(interfaceClass, options, proxy);
/*  626 */     return interfaceClass.cast(proxy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <T> T loadLibrary(Class<T> interfaceClass) {
/*  636 */     return loadLibrary((String)null, interfaceClass);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <T> T loadLibrary(Class<T> interfaceClass, Map<String, ?> options) {
/*  646 */     return loadLibrary(null, interfaceClass, options);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <T> T loadLibrary(String name, Class<T> interfaceClass) {
/*  656 */     return loadLibrary(name, interfaceClass, Collections.emptyMap());
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public static <T> T loadLibrary(String name, Class<T> interfaceClass, Map<String, ?> options) {
/*  666 */     if (!Library.class.isAssignableFrom(interfaceClass))
/*      */     {
/*  668 */       throw new IllegalArgumentException("Interface (" + interfaceClass.getSimpleName() + ") of library=" + name + " does not extend " + Library.class
/*  669 */           .getSimpleName());
/*      */     }
/*      */     
/*  672 */     Library.Handler handler = new Library.Handler(name, interfaceClass, options);
/*  673 */     ClassLoader loader = interfaceClass.getClassLoader();
/*  674 */     Object proxy = Proxy.newProxyInstance(loader, new Class[] { interfaceClass }, handler);
/*  675 */     cacheOptions(interfaceClass, options, proxy);
/*  676 */     return interfaceClass.cast(proxy);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void loadLibraryInstance(Class<?> cls) {
/*  685 */     if (cls != null && !libraries.containsKey(cls)) {
/*      */       try {
/*  687 */         Field[] fields = cls.getFields();
/*  688 */         for (int i = 0; i < fields.length; i++) {
/*  689 */           Field field = fields[i];
/*  690 */           if (field.getType() == cls && 
/*  691 */             Modifier.isStatic(field.getModifiers())) {
/*      */             
/*  693 */             field.setAccessible(true);
/*  694 */             libraries.put(cls, new WeakReference(field.get(null)));
/*      */             
/*      */             break;
/*      */           } 
/*      */         } 
/*  699 */       } catch (Exception e) {
/*  700 */         throw new IllegalArgumentException("Could not access instance of " + cls + " (" + e + ")");
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
/*      */ 
/*      */   
/*      */   static Class<?> findEnclosingLibraryClass(Class<?> cls) {
/*  714 */     if (cls == null) {
/*  715 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  719 */     Map<String, ?> libOptions = typeOptions.get(cls);
/*  720 */     if (libOptions != null) {
/*  721 */       Class<?> enclosingClass = (Class)libOptions.get("enclosing-library");
/*  722 */       if (enclosingClass != null) {
/*  723 */         return enclosingClass;
/*      */       }
/*  725 */       return cls;
/*      */     } 
/*  727 */     if (Library.class.isAssignableFrom(cls)) {
/*  728 */       return cls;
/*      */     }
/*  730 */     if (Callback.class.isAssignableFrom(cls)) {
/*  731 */       cls = CallbackReference.findCallbackClass(cls);
/*      */     }
/*  733 */     Class<?> declaring = cls.getDeclaringClass();
/*  734 */     Class<?> fromDeclaring = findEnclosingLibraryClass(declaring);
/*  735 */     if (fromDeclaring != null) {
/*  736 */       return fromDeclaring;
/*      */     }
/*  738 */     return findEnclosingLibraryClass(cls.getSuperclass());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<String, Object> getLibraryOptions(Class<?> type) {
/*  757 */     Map<String, Object> libraryOptions = typeOptions.get(type);
/*  758 */     if (libraryOptions != null) {
/*  759 */       return libraryOptions;
/*      */     }
/*      */     
/*  762 */     Class<?> mappingClass = findEnclosingLibraryClass(type);
/*  763 */     if (mappingClass != null) {
/*  764 */       loadLibraryInstance(mappingClass);
/*      */     } else {
/*  766 */       mappingClass = type;
/*      */     } 
/*      */     
/*  769 */     libraryOptions = typeOptions.get(mappingClass);
/*  770 */     if (libraryOptions != null) {
/*  771 */       typeOptions.put(type, libraryOptions);
/*  772 */       return libraryOptions;
/*      */     } 
/*      */     
/*      */     try {
/*  776 */       Field field = mappingClass.getField("OPTIONS");
/*  777 */       field.setAccessible(true);
/*  778 */       libraryOptions = (Map<String, Object>)field.get(null);
/*  779 */       if (libraryOptions == null) {
/*  780 */         throw new IllegalStateException("Null options field");
/*      */       }
/*  782 */     } catch (NoSuchFieldException e) {
/*  783 */       libraryOptions = Collections.emptyMap();
/*  784 */     } catch (Exception e) {
/*  785 */       throw new IllegalArgumentException("OPTIONS must be a public field of type java.util.Map (" + e + "): " + mappingClass);
/*      */     } 
/*      */     
/*  788 */     libraryOptions = new HashMap<String, Object>(libraryOptions);
/*  789 */     if (!libraryOptions.containsKey("type-mapper")) {
/*  790 */       libraryOptions.put("type-mapper", lookupField(mappingClass, "TYPE_MAPPER", TypeMapper.class));
/*      */     }
/*  792 */     if (!libraryOptions.containsKey("structure-alignment")) {
/*  793 */       libraryOptions.put("structure-alignment", lookupField(mappingClass, "STRUCTURE_ALIGNMENT", Integer.class));
/*      */     }
/*  795 */     if (!libraryOptions.containsKey("string-encoding")) {
/*  796 */       libraryOptions.put("string-encoding", lookupField(mappingClass, "STRING_ENCODING", String.class));
/*      */     }
/*  798 */     libraryOptions = cacheOptions(mappingClass, libraryOptions, null);
/*      */     
/*  800 */     if (type != mappingClass) {
/*  801 */       typeOptions.put(type, libraryOptions);
/*      */     }
/*  803 */     return libraryOptions;
/*      */   }
/*      */   
/*      */   private static Object lookupField(Class<?> mappingClass, String fieldName, Class<?> resultClass) {
/*      */     try {
/*  808 */       Field field = mappingClass.getField(fieldName);
/*  809 */       field.setAccessible(true);
/*  810 */       return field.get(null);
/*      */     }
/*  812 */     catch (NoSuchFieldException e) {
/*  813 */       return null;
/*      */     }
/*  815 */     catch (Exception e) {
/*  816 */       throw new IllegalArgumentException(fieldName + " must be a public field of type " + resultClass
/*  817 */           .getName() + " (" + e + "): " + mappingClass);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static TypeMapper getTypeMapper(Class<?> cls) {
/*  826 */     Map<String, ?> options = getLibraryOptions(cls);
/*  827 */     return (TypeMapper)options.get("type-mapper");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getStringEncoding(Class<?> cls) {
/*  837 */     Map<String, ?> options = getLibraryOptions(cls);
/*  838 */     String encoding = (String)options.get("string-encoding");
/*  839 */     return (encoding != null) ? encoding : getDefaultStringEncoding();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getDefaultStringEncoding() {
/*  847 */     return System.getProperty("jna.encoding", DEFAULT_ENCODING);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static int getStructureAlignment(Class<?> cls) {
/*  856 */     Integer alignment = (Integer)getLibraryOptions(cls).get("structure-alignment");
/*  857 */     return (alignment == null) ? 0 : alignment.intValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] getBytes(String s) {
/*  866 */     return getBytes(s, getDefaultStringEncoding());
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
/*      */   static byte[] getBytes(String s, String encoding) {
/*  878 */     return getBytes(s, getCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static byte[] getBytes(String s, Charset charset) {
/*  888 */     return s.getBytes(charset);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(String s) {
/*  898 */     return toByteArray(s, getDefaultStringEncoding());
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
/*      */   public static byte[] toByteArray(String s, String encoding) {
/*  910 */     return toByteArray(s, getCharset(encoding));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static byte[] toByteArray(String s, Charset charset) {
/*  921 */     byte[] bytes = getBytes(s, charset);
/*  922 */     byte[] buf = new byte[bytes.length + 1];
/*  923 */     System.arraycopy(bytes, 0, buf, 0, bytes.length);
/*  924 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static char[] toCharArray(String s) {
/*  932 */     char[] chars = s.toCharArray();
/*  933 */     char[] buf = new char[chars.length + 1];
/*  934 */     System.arraycopy(chars, 0, buf, 0, chars.length);
/*  935 */     return buf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void loadNativeDispatchLibrary() {
/*  944 */     if (!Boolean.getBoolean("jna.nounpack")) {
/*      */       try {
/*  946 */         removeTemporaryFiles();
/*      */       }
/*  948 */       catch (IOException e) {
/*  949 */         LOG.log(Level.WARNING, "JNA Warning: IOException removing temporary files", e);
/*      */       } 
/*      */     }
/*      */     
/*  953 */     String libName = System.getProperty("jna.boot.library.name", "jnidispatch");
/*  954 */     String bootPath = System.getProperty("jna.boot.library.path");
/*  955 */     if (bootPath != null) {
/*      */       
/*  957 */       StringTokenizer dirs = new StringTokenizer(bootPath, File.pathSeparator);
/*  958 */       while (dirs.hasMoreTokens()) {
/*  959 */         String dir = dirs.nextToken();
/*  960 */         File file = new File(new File(dir), System.mapLibraryName(libName).replace(".dylib", ".jnilib"));
/*  961 */         String path = file.getAbsolutePath();
/*  962 */         LOG.log(DEBUG_JNA_LOAD_LEVEL, "Looking in {0}", path);
/*  963 */         if (file.exists()) {
/*      */           try {
/*  965 */             LOG.log(DEBUG_JNA_LOAD_LEVEL, "Trying {0}", path);
/*  966 */             System.setProperty("jnidispatch.path", path);
/*  967 */             System.load(path);
/*  968 */             jnidispatchPath = path;
/*  969 */             LOG.log(DEBUG_JNA_LOAD_LEVEL, "Found jnidispatch at {0}", path);
/*      */             return;
/*  971 */           } catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  977 */         if (Platform.isMac()) {
/*      */           String orig, ext;
/*  979 */           if (path.endsWith("dylib")) {
/*  980 */             orig = "dylib";
/*  981 */             ext = "jnilib";
/*      */           } else {
/*  983 */             orig = "jnilib";
/*  984 */             ext = "dylib";
/*      */           } 
/*  986 */           path = path.substring(0, path.lastIndexOf(orig)) + ext;
/*  987 */           LOG.log(DEBUG_JNA_LOAD_LEVEL, "Looking in {0}", path);
/*  988 */           if ((new File(path)).exists()) {
/*      */             try {
/*  990 */               LOG.log(DEBUG_JNA_LOAD_LEVEL, "Trying {0}", path);
/*  991 */               System.setProperty("jnidispatch.path", path);
/*  992 */               System.load(path);
/*  993 */               jnidispatchPath = path;
/*  994 */               LOG.log(DEBUG_JNA_LOAD_LEVEL, "Found jnidispatch at {0}", path);
/*      */               return;
/*  996 */             } catch (UnsatisfiedLinkError ex) {
/*  997 */               LOG.log(Level.WARNING, "File found at " + path + " but not loadable: " + ex.getMessage(), ex);
/*      */             } 
/*      */           }
/*      */         } 
/*      */       } 
/*      */     } 
/* 1003 */     String jnaNosys = System.getProperty("jna.nosys", "true");
/* 1004 */     if (!Boolean.parseBoolean(jnaNosys) || Platform.isAndroid()) {
/*      */       try {
/* 1006 */         LOG.log(DEBUG_JNA_LOAD_LEVEL, "Trying (via loadLibrary) {0}", libName);
/* 1007 */         System.loadLibrary(libName);
/* 1008 */         LOG.log(DEBUG_JNA_LOAD_LEVEL, "Found jnidispatch on system path");
/*      */         
/*      */         return;
/* 1011 */       } catch (UnsatisfiedLinkError unsatisfiedLinkError) {}
/*      */     }
/*      */     
/* 1014 */     if (!Boolean.getBoolean("jna.noclasspath")) {
/* 1015 */       loadNativeDispatchLibraryFromClasspath();
/*      */     } else {
/*      */       
/* 1018 */       throw new UnsatisfiedLinkError("Unable to locate JNA native support library");
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static void loadNativeDispatchLibraryFromClasspath() {
/*      */     try {
/* 1029 */       String mappedName = System.mapLibraryName("jnidispatch").replace(".dylib", ".jnilib");
/* 1030 */       if (Platform.isAIX())
/*      */       {
/*      */         
/* 1033 */         mappedName = "libjnidispatch.a";
/*      */       }
/* 1035 */       String libName = "/com/sun/jna/" + Platform.RESOURCE_PREFIX + "/" + mappedName;
/* 1036 */       File lib = extractFromResourcePath(libName, Native.class.getClassLoader());
/* 1037 */       if (lib == null && 
/* 1038 */         lib == null) {
/* 1039 */         throw new UnsatisfiedLinkError("Could not find JNA native support");
/*      */       }
/*      */ 
/*      */       
/* 1043 */       LOG.log(DEBUG_JNA_LOAD_LEVEL, "Trying {0}", lib.getAbsolutePath());
/* 1044 */       System.setProperty("jnidispatch.path", lib.getAbsolutePath());
/* 1045 */       System.load(lib.getAbsolutePath());
/* 1046 */       jnidispatchPath = lib.getAbsolutePath();
/* 1047 */       LOG.log(DEBUG_JNA_LOAD_LEVEL, "Found jnidispatch at {0}", jnidispatchPath);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1053 */       if (isUnpacked(lib) && 
/* 1054 */         !Boolean.getBoolean("jnidispatch.preserve")) {
/* 1055 */         deleteLibrary(lib);
/*      */       }
/*      */     }
/* 1058 */     catch (IOException e) {
/* 1059 */       throw new UnsatisfiedLinkError(e.getMessage());
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   static boolean isUnpacked(File file) {
/* 1065 */     return file.getName().startsWith("jna");
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static File extractFromResourcePath(String name) throws IOException {
/* 1080 */     return extractFromResourcePath(name, null);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static File extractFromResourcePath(String name, ClassLoader loader) throws IOException {
/* 1098 */     Level DEBUG = (DEBUG_LOAD || (DEBUG_JNA_LOAD && name.contains("jnidispatch"))) ? Level.INFO : Level.FINE;
/* 1099 */     if (loader == null) {
/* 1100 */       loader = Thread.currentThread().getContextClassLoader();
/*      */       
/* 1102 */       if (loader == null) {
/* 1103 */         loader = Native.class.getClassLoader();
/*      */       }
/*      */     } 
/* 1106 */     LOG.log(DEBUG, "Looking in classpath from {0} for {1}", new Object[] { loader, name });
/* 1107 */     String libname = name.startsWith("/") ? name : NativeLibrary.mapSharedLibraryName(name);
/* 1108 */     String resourcePath = name.startsWith("/") ? name : (Platform.RESOURCE_PREFIX + "/" + libname);
/* 1109 */     if (resourcePath.startsWith("/")) {
/* 1110 */       resourcePath = resourcePath.substring(1);
/*      */     }
/* 1112 */     URL url = loader.getResource(resourcePath);
/* 1113 */     if (url == null) {
/* 1114 */       if (resourcePath.startsWith(Platform.RESOURCE_PREFIX)) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1119 */         if (Platform.RESOURCE_PREFIX.startsWith("darwin")) {
/* 1120 */           url = loader.getResource("darwin/" + resourcePath.substring(Platform.RESOURCE_PREFIX.length() + 1));
/*      */         }
/* 1122 */         if (url == null)
/*      */         {
/* 1124 */           url = loader.getResource(libname);
/*      */         }
/* 1126 */       } else if (resourcePath.startsWith("com/sun/jna/" + Platform.RESOURCE_PREFIX + "/")) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1131 */         if (Platform.RESOURCE_PREFIX.startsWith("com/sun/jna/darwin")) {
/* 1132 */           url = loader.getResource("com/sun/jna/darwin" + resourcePath.substring(("com/sun/jna/" + Platform.RESOURCE_PREFIX).length() + 1));
/*      */         }
/* 1134 */         if (url == null)
/*      */         {
/* 1136 */           url = loader.getResource(libname);
/*      */         }
/*      */       } 
/*      */     }
/* 1140 */     if (url == null) {
/* 1141 */       String path = System.getProperty("java.class.path");
/* 1142 */       if (loader instanceof URLClassLoader) {
/* 1143 */         path = Arrays.<URL>asList(((URLClassLoader)loader).getURLs()).toString();
/*      */       }
/* 1145 */       throw new IOException("Native library (" + resourcePath + ") not found in resource path (" + path + ")");
/*      */     } 
/* 1147 */     LOG.log(DEBUG, "Found library resource at {0}", url);
/*      */     
/* 1149 */     File lib = null;
/* 1150 */     if (url.getProtocol().toLowerCase().equals("file")) {
/*      */       try {
/* 1152 */         lib = new File(new URI(url.toString()));
/*      */       }
/* 1154 */       catch (URISyntaxException e) {
/* 1155 */         lib = new File(url.getPath());
/*      */       } 
/* 1157 */       LOG.log(DEBUG, "Looking in {0}", lib.getAbsolutePath());
/* 1158 */       if (!lib.exists()) {
/* 1159 */         throw new IOException("File URL " + url + " could not be properly decoded");
/*      */       }
/*      */     }
/* 1162 */     else if (!Boolean.getBoolean("jna.nounpack")) {
/* 1163 */       InputStream is = url.openStream();
/* 1164 */       if (is == null) {
/* 1165 */         throw new IOException("Can't obtain InputStream for " + resourcePath);
/*      */       }
/*      */       
/* 1168 */       FileOutputStream fos = null;
/*      */ 
/*      */ 
/*      */       
/*      */       try {
/* 1173 */         File dir = getTempDir();
/* 1174 */         lib = File.createTempFile("jna", Platform.isWindows() ? ".dll" : null, dir);
/* 1175 */         if (!Boolean.getBoolean("jnidispatch.preserve")) {
/* 1176 */           lib.deleteOnExit();
/*      */         }
/* 1178 */         LOG.log(DEBUG, "Extracting library to {0}", lib.getAbsolutePath());
/* 1179 */         fos = new FileOutputStream(lib);
/*      */         
/* 1181 */         byte[] buf = new byte[1024]; int count;
/* 1182 */         while ((count = is.read(buf, 0, buf.length)) > 0) {
/* 1183 */           fos.write(buf, 0, count);
/*      */         }
/*      */       }
/* 1186 */       catch (IOException e) {
/* 1187 */         throw new IOException("Failed to create temporary file for " + name + " library: " + e.getMessage());
/*      */       } finally {
/*      */         
/* 1190 */         try { is.close(); } catch (IOException iOException) {}
/* 1191 */         if (fos != null) {
/* 1192 */           try { fos.close(); } catch (IOException iOException) {}
/*      */         }
/*      */       } 
/*      */     } 
/* 1196 */     return lib;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Library synchronizedLibrary(final Library library) {
/* 1239 */     Class<?> cls = library.getClass();
/* 1240 */     if (!Proxy.isProxyClass(cls)) {
/* 1241 */       throw new IllegalArgumentException("Library must be a proxy class");
/*      */     }
/* 1243 */     InvocationHandler ih = Proxy.getInvocationHandler(library);
/* 1244 */     if (!(ih instanceof Library.Handler)) {
/* 1245 */       throw new IllegalArgumentException("Unrecognized proxy handler: " + ih);
/*      */     }
/* 1247 */     final Library.Handler handler = (Library.Handler)ih;
/* 1248 */     InvocationHandler newHandler = new InvocationHandler()
/*      */       {
/*      */         public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 1251 */           synchronized (handler.getNativeLibrary()) {
/* 1252 */             return handler.invoke(library, method, args);
/*      */           } 
/*      */         }
/*      */       };
/* 1256 */     return (Library)Proxy.newProxyInstance(cls.getClassLoader(), cls
/* 1257 */         .getInterfaces(), newHandler);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static String getWebStartLibraryPath(String libName) {
/* 1277 */     if (System.getProperty("javawebstart.version") == null) {
/* 1278 */       return null;
/*      */     }
/*      */     try {
/* 1281 */       ClassLoader cl = Native.class.getClassLoader();
/* 1282 */       Method m = AccessController.<Method>doPrivileged(new PrivilegedAction<Method>()
/*      */           {
/*      */             public Method run() {
/*      */               try {
/* 1286 */                 Method m = ClassLoader.class.getDeclaredMethod("findLibrary", new Class[] { String.class });
/* 1287 */                 m.setAccessible(true);
/* 1288 */                 return m;
/*      */               }
/* 1290 */               catch (Exception e) {
/* 1291 */                 return null;
/*      */               } 
/*      */             }
/*      */           });
/* 1295 */       String libpath = (String)m.invoke(cl, new Object[] { libName });
/* 1296 */       if (libpath != null) {
/* 1297 */         return (new File(libpath)).getParent();
/*      */       }
/* 1299 */       return null;
/*      */     }
/* 1301 */     catch (Exception e) {
/* 1302 */       return null;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void markTemporaryFile(File file) {
/*      */     try {
/* 1312 */       File marker = new File(file.getParentFile(), file.getName() + ".x");
/* 1313 */       marker.createNewFile();
/*      */     } catch (IOException e) {
/* 1315 */       e.printStackTrace();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   static File getTempDir() throws IOException {
/*      */     File jnatmp;
/* 1323 */     String prop = System.getProperty("jna.tmpdir");
/* 1324 */     if (prop != null) {
/* 1325 */       jnatmp = new File(prop);
/* 1326 */       jnatmp.mkdirs();
/*      */     } else {
/*      */       
/* 1329 */       File tmp = new File(System.getProperty("java.io.tmpdir"));
/* 1330 */       if (Platform.isMac()) {
/*      */         
/* 1332 */         jnatmp = new File(System.getProperty("user.home"), "Library/Caches/JNA/temp");
/* 1333 */       } else if (Platform.isLinux() || Platform.isSolaris() || Platform.isAIX() || Platform.isFreeBSD() || Platform.isNetBSD() || Platform.isOpenBSD() || Platform.iskFreeBSD()) {
/*      */         File xdgCacheFile;
/*      */         
/* 1336 */         String xdgCacheEnvironment = System.getenv("XDG_CACHE_HOME");
/*      */         
/* 1338 */         if (xdgCacheEnvironment == null || xdgCacheEnvironment.trim().isEmpty()) {
/* 1339 */           xdgCacheFile = new File(System.getProperty("user.home"), ".cache");
/*      */         } else {
/* 1341 */           xdgCacheFile = new File(xdgCacheEnvironment);
/*      */         } 
/* 1343 */         jnatmp = new File(xdgCacheFile, "JNA/temp");
/*      */       
/*      */       }
/*      */       else {
/*      */         
/* 1348 */         jnatmp = new File(tmp, "jna-" + System.getProperty("user.name").hashCode());
/*      */       } 
/*      */       
/* 1351 */       jnatmp.mkdirs();
/* 1352 */       if (!jnatmp.exists() || !jnatmp.canWrite()) {
/* 1353 */         jnatmp = tmp;
/*      */       }
/*      */     } 
/* 1356 */     if (!jnatmp.exists()) {
/* 1357 */       throw new IOException("JNA temporary directory '" + jnatmp + "' does not exist");
/*      */     }
/* 1359 */     if (!jnatmp.canWrite()) {
/* 1360 */       throw new IOException("JNA temporary directory '" + jnatmp + "' is not writable");
/*      */     }
/* 1362 */     return jnatmp;
/*      */   }
/*      */ 
/*      */   
/*      */   static void removeTemporaryFiles() throws IOException {
/* 1367 */     File dir = getTempDir();
/* 1368 */     FilenameFilter filter = new FilenameFilter()
/*      */       {
/*      */         public boolean accept(File dir, String name) {
/* 1371 */           return (name.endsWith(".x") && name.startsWith("jna"));
/*      */         }
/*      */       };
/* 1374 */     File[] files = dir.listFiles(filter);
/* 1375 */     for (int i = 0; files != null && i < files.length; i++) {
/* 1376 */       File marker = files[i];
/* 1377 */       String name = marker.getName();
/* 1378 */       name = name.substring(0, name.length() - 2);
/* 1379 */       File target = new File(marker.getParentFile(), name);
/* 1380 */       if (!target.exists() || target.delete()) {
/* 1381 */         marker.delete();
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
/*      */   public static int getNativeSize(Class<?> type, Object value) {
/* 1393 */     if (type.isArray()) {
/* 1394 */       int len = Array.getLength(value);
/* 1395 */       if (len > 0) {
/* 1396 */         Object o = Array.get(value, 0);
/* 1397 */         return len * getNativeSize(type.getComponentType(), o);
/*      */       } 
/*      */       
/* 1400 */       throw new IllegalArgumentException("Arrays of length zero not allowed: " + type);
/*      */     } 
/* 1402 */     if (Structure.class.isAssignableFrom(type) && 
/* 1403 */       !Structure.ByReference.class.isAssignableFrom(type)) {
/* 1404 */       return Structure.size(type, value);
/*      */     }
/*      */     try {
/* 1407 */       return getNativeSize(type);
/*      */     }
/* 1409 */     catch (IllegalArgumentException e) {
/* 1410 */       throw new IllegalArgumentException("The type \"" + type.getName() + "\" is not supported: " + e
/*      */           
/* 1412 */           .getMessage());
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
/*      */   
/*      */   public static int getNativeSize(Class<?> cls) {
/* 1425 */     if (NativeMapped.class.isAssignableFrom(cls)) {
/* 1426 */       cls = NativeMappedConverter.getInstance(cls).nativeType();
/*      */     }
/*      */     
/* 1429 */     if (cls == boolean.class || cls == Boolean.class) return 4; 
/* 1430 */     if (cls == byte.class || cls == Byte.class) return 1; 
/* 1431 */     if (cls == short.class || cls == Short.class) return 2; 
/* 1432 */     if (cls == char.class || cls == Character.class) return WCHAR_SIZE; 
/* 1433 */     if (cls == int.class || cls == Integer.class) return 4; 
/* 1434 */     if (cls == long.class || cls == Long.class) return 8; 
/* 1435 */     if (cls == float.class || cls == Float.class) return 4; 
/* 1436 */     if (cls == double.class || cls == Double.class) return 8; 
/* 1437 */     if (Structure.class.isAssignableFrom(cls)) {
/* 1438 */       if (Structure.ByValue.class.isAssignableFrom(cls)) {
/* 1439 */         return Structure.size((Class)cls);
/*      */       }
/* 1441 */       return POINTER_SIZE;
/*      */     } 
/* 1443 */     if (Pointer.class.isAssignableFrom(cls) || (Platform.HAS_BUFFERS && 
/* 1444 */       Buffers.isBuffer(cls)) || Callback.class
/* 1445 */       .isAssignableFrom(cls) || String.class == cls || WString.class == cls)
/*      */     {
/*      */       
/* 1448 */       return POINTER_SIZE;
/*      */     }
/* 1450 */     throw new IllegalArgumentException("Native size for type \"" + cls.getName() + "\" is unknown");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isSupportedNativeType(Class<?> cls) {
/* 1459 */     if (Structure.class.isAssignableFrom(cls)) {
/* 1460 */       return true;
/*      */     }
/*      */     try {
/* 1463 */       return (getNativeSize(cls) != 0);
/*      */     }
/* 1465 */     catch (IllegalArgumentException e) {
/* 1466 */       return false;
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
/*      */   public static void setCallbackExceptionHandler(Callback.UncaughtExceptionHandler eh) {
/* 1478 */     callbackExceptionHandler = (eh == null) ? DEFAULT_HANDLER : eh;
/*      */   }
/*      */ 
/*      */   
/*      */   public static Callback.UncaughtExceptionHandler getCallbackExceptionHandler() {
/* 1483 */     return callbackExceptionHandler;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void register(String libName) {
/* 1493 */     register(findDirectMappedClass(getCallingClass()), libName);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void register(NativeLibrary lib) {
/* 1503 */     register(findDirectMappedClass(getCallingClass()), lib);
/*      */   }
/*      */ 
/*      */   
/*      */   static Class<?> findDirectMappedClass(Class<?> cls) {
/* 1508 */     Method[] methods = cls.getDeclaredMethods();
/* 1509 */     for (Method m : methods) {
/* 1510 */       if ((m.getModifiers() & 0x100) != 0) {
/* 1511 */         return cls;
/*      */       }
/*      */     } 
/* 1514 */     int idx = cls.getName().lastIndexOf("$");
/* 1515 */     if (idx != -1) {
/* 1516 */       String name = cls.getName().substring(0, idx);
/*      */       try {
/* 1518 */         return findDirectMappedClass(Class.forName(name, true, cls.getClassLoader()));
/* 1519 */       } catch (ClassNotFoundException classNotFoundException) {}
/*      */     } 
/*      */ 
/*      */     
/* 1523 */     throw new IllegalArgumentException("Can't determine class with native methods from the current context (" + cls + ")");
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
/*      */   static Class<?> getCallingClass() {
/* 1535 */     Class<?>[] context = (new SecurityManager() { public Class<?>[] getClassContext() { return super.getClassContext(); } }).getClassContext();
/* 1536 */     if (context == null) {
/* 1537 */       throw new IllegalStateException("The SecurityManager implementation on this platform is broken; you must explicitly provide the class to register");
/*      */     }
/* 1539 */     if (context.length < 4) {
/* 1540 */       throw new IllegalStateException("This method must be called from the static initializer of a class");
/*      */     }
/* 1542 */     return context[3];
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void setCallbackThreadInitializer(Callback cb, CallbackThreadInitializer initializer) {
/* 1552 */     CallbackReference.setCallbackThreadInitializer(cb, initializer);
/*      */   }
/*      */   
/* 1555 */   private static final Map<Class<?>, long[]> registeredClasses = (Map)new WeakHashMap<Class<?>, long>();
/* 1556 */   private static final Map<Class<?>, NativeLibrary> registeredLibraries = new WeakHashMap<Class<?>, NativeLibrary>(); static final int CB_HAS_INITIALIZER = 1; private static final int CVT_UNSUPPORTED = -1; private static final int CVT_DEFAULT = 0; private static final int CVT_POINTER = 1; private static final int CVT_STRING = 2; private static final int CVT_STRUCTURE = 3; private static final int CVT_STRUCTURE_BYVAL = 4; private static final int CVT_BUFFER = 5; private static final int CVT_ARRAY_BYTE = 6; private static final int CVT_ARRAY_SHORT = 7; private static final int CVT_ARRAY_CHAR = 8; private static final int CVT_ARRAY_INT = 9; private static final int CVT_ARRAY_LONG = 10; private static final int CVT_ARRAY_FLOAT = 11; private static final int CVT_ARRAY_DOUBLE = 12; private static final int CVT_ARRAY_BOOLEAN = 13; private static final int CVT_BOOLEAN = 14; private static final int CVT_CALLBACK = 15; private static final int CVT_FLOAT = 16; private static final int CVT_NATIVE_MAPPED = 17; private static final int CVT_NATIVE_MAPPED_STRING = 18; private static final int CVT_NATIVE_MAPPED_WSTRING = 19; private static final int CVT_WSTRING = 20; private static final int CVT_INTEGER_TYPE = 21; private static final int CVT_POINTER_TYPE = 22; private static final int CVT_TYPE_MAPPER = 23; private static final int CVT_TYPE_MAPPER_STRING = 24; private static final int CVT_TYPE_MAPPER_WSTRING = 25; private static final int CVT_OBJECT = 26; private static final int CVT_JNIENV = 27; private static final int CVT_SHORT = 28; private static final int CVT_BYTE = 29; static final int CB_OPTION_DIRECT = 1; static final int CB_OPTION_IN_DLL = 2;
/*      */   
/*      */   private static void unregisterAll() {
/* 1559 */     synchronized (registeredClasses) {
/* 1560 */       for (Map.Entry<Class<?>, long[]> e : registeredClasses.entrySet()) {
/* 1561 */         unregister(e.getKey(), e.getValue());
/*      */       }
/*      */       
/* 1564 */       registeredClasses.clear();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unregister() {
/* 1573 */     unregister(findDirectMappedClass(getCallingClass()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void unregister(Class<?> cls) {
/* 1581 */     synchronized (registeredClasses) {
/* 1582 */       long[] handles = registeredClasses.get(cls);
/* 1583 */       if (handles != null) {
/* 1584 */         unregister(cls, handles);
/* 1585 */         registeredClasses.remove(cls);
/* 1586 */         registeredLibraries.remove(cls);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean registered(Class<?> cls) {
/* 1596 */     synchronized (registeredClasses) {
/* 1597 */       return registeredClasses.containsKey(cls);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String getSignature(Class<?> cls) {
/* 1605 */     if (cls.isArray()) {
/* 1606 */       return "[" + getSignature(cls.getComponentType());
/*      */     }
/* 1608 */     if (cls.isPrimitive()) {
/* 1609 */       if (cls == void.class) return "V"; 
/* 1610 */       if (cls == boolean.class) return "Z"; 
/* 1611 */       if (cls == byte.class) return "B"; 
/* 1612 */       if (cls == short.class) return "S"; 
/* 1613 */       if (cls == char.class) return "C"; 
/* 1614 */       if (cls == int.class) return "I"; 
/* 1615 */       if (cls == long.class) return "J"; 
/* 1616 */       if (cls == float.class) return "F"; 
/* 1617 */       if (cls == double.class) return "D"; 
/*      */     } 
/* 1619 */     return "L" + replace(".", "/", cls.getName()) + ";";
/*      */   }
/*      */ 
/*      */   
/*      */   static String replace(String s1, String s2, String str) {
/* 1624 */     StringBuilder buf = new StringBuilder();
/*      */     while (true) {
/* 1626 */       int idx = str.indexOf(s1);
/* 1627 */       if (idx == -1) {
/* 1628 */         buf.append(str);
/*      */         
/*      */         break;
/*      */       } 
/* 1632 */       buf.append(str.substring(0, idx));
/* 1633 */       buf.append(s2);
/* 1634 */       str = str.substring(idx + s1.length());
/*      */     } 
/*      */     
/* 1637 */     return buf.toString();
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static int getConversion(Class<?> type, TypeMapper mapper, boolean allowObjects) {
/* 1676 */     if (type == Void.class) type = void.class;
/*      */     
/* 1678 */     if (mapper != null) {
/* 1679 */       FromNativeConverter fromNative = mapper.getFromNativeConverter(type);
/* 1680 */       ToNativeConverter toNative = mapper.getToNativeConverter(type);
/* 1681 */       if (fromNative != null) {
/* 1682 */         Class<?> nativeType = fromNative.nativeType();
/* 1683 */         if (nativeType == String.class) {
/* 1684 */           return 24;
/*      */         }
/* 1686 */         if (nativeType == WString.class) {
/* 1687 */           return 25;
/*      */         }
/* 1689 */         return 23;
/*      */       } 
/* 1691 */       if (toNative != null) {
/* 1692 */         Class<?> nativeType = toNative.nativeType();
/* 1693 */         if (nativeType == String.class) {
/* 1694 */           return 24;
/*      */         }
/* 1696 */         if (nativeType == WString.class) {
/* 1697 */           return 25;
/*      */         }
/* 1699 */         return 23;
/*      */       } 
/*      */     } 
/*      */     
/* 1703 */     if (Pointer.class.isAssignableFrom(type)) {
/* 1704 */       return 1;
/*      */     }
/* 1706 */     if (String.class == type) {
/* 1707 */       return 2;
/*      */     }
/* 1709 */     if (WString.class.isAssignableFrom(type)) {
/* 1710 */       return 20;
/*      */     }
/* 1712 */     if (Platform.HAS_BUFFERS && Buffers.isBuffer(type)) {
/* 1713 */       return 5;
/*      */     }
/* 1715 */     if (Structure.class.isAssignableFrom(type)) {
/* 1716 */       if (Structure.ByValue.class.isAssignableFrom(type)) {
/* 1717 */         return 4;
/*      */       }
/* 1719 */       return 3;
/*      */     } 
/* 1721 */     if (type.isArray()) {
/* 1722 */       switch (type.getName().charAt(1)) { case 'Z':
/* 1723 */           return 13;
/* 1724 */         case 'B': return 6;
/* 1725 */         case 'S': return 7;
/* 1726 */         case 'C': return 8;
/* 1727 */         case 'I': return 9;
/* 1728 */         case 'J': return 10;
/* 1729 */         case 'F': return 11;
/* 1730 */         case 'D': return 12; }
/*      */ 
/*      */     
/*      */     }
/* 1734 */     if (type.isPrimitive()) {
/* 1735 */       return (type == boolean.class) ? 14 : 0;
/*      */     }
/* 1737 */     if (Callback.class.isAssignableFrom(type)) {
/* 1738 */       return 15;
/*      */     }
/* 1740 */     if (IntegerType.class.isAssignableFrom(type)) {
/* 1741 */       return 21;
/*      */     }
/* 1743 */     if (PointerType.class.isAssignableFrom(type)) {
/* 1744 */       return 22;
/*      */     }
/* 1746 */     if (NativeMapped.class.isAssignableFrom(type)) {
/* 1747 */       Class<?> nativeType = NativeMappedConverter.getInstance(type).nativeType();
/* 1748 */       if (nativeType == String.class) {
/* 1749 */         return 18;
/*      */       }
/* 1751 */       if (nativeType == WString.class) {
/* 1752 */         return 19;
/*      */       }
/* 1754 */       return 17;
/*      */     } 
/* 1756 */     if (JNIEnv.class == type) {
/* 1757 */       return 27;
/*      */     }
/* 1759 */     return allowObjects ? 26 : -1;
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
/*      */ 
/*      */ 
/*      */   
/*      */   public static void register(Class<?> cls, String libName) {
/* 1774 */     NativeLibrary library = NativeLibrary.getInstance(libName, Collections.singletonMap("classloader", cls.getClassLoader()));
/* 1775 */     register(cls, library);
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
/*      */   
/*      */   public static void register(Class<?> cls, NativeLibrary lib) {
/* 1788 */     Method[] methods = cls.getDeclaredMethods();
/* 1789 */     List<Method> mlist = new ArrayList<Method>();
/* 1790 */     Map<String, ?> options = lib.getOptions();
/* 1791 */     TypeMapper mapper = (TypeMapper)options.get("type-mapper");
/* 1792 */     boolean allowObjects = Boolean.TRUE.equals(options.get("allow-objects"));
/* 1793 */     options = cacheOptions(cls, options, null);
/*      */     
/* 1795 */     for (Method m : methods) {
/* 1796 */       if ((m.getModifiers() & 0x100) != 0) {
/* 1797 */         mlist.add(m);
/*      */       }
/*      */     } 
/*      */     
/* 1801 */     long[] handles = new long[mlist.size()];
/* 1802 */     for (int i = 0; i < handles.length; i++) {
/* 1803 */       long rtype, closure_rtype; Method method = mlist.get(i);
/* 1804 */       String sig = "(";
/* 1805 */       Class<?> rclass = method.getReturnType();
/*      */       
/* 1807 */       Class<?>[] ptypes = method.getParameterTypes();
/* 1808 */       long[] atypes = new long[ptypes.length];
/* 1809 */       long[] closure_atypes = new long[ptypes.length];
/* 1810 */       int[] cvt = new int[ptypes.length];
/* 1811 */       ToNativeConverter[] toNative = new ToNativeConverter[ptypes.length];
/* 1812 */       FromNativeConverter fromNative = null;
/* 1813 */       int rcvt = getConversion(rclass, mapper, allowObjects);
/* 1814 */       boolean throwLastError = false;
/* 1815 */       switch (rcvt) {
/*      */         case -1:
/* 1817 */           throw new IllegalArgumentException(rclass + " is not a supported return type (in method " + method.getName() + " in " + cls + ")");
/*      */         case 23:
/*      */         case 24:
/*      */         case 25:
/* 1821 */           fromNative = mapper.getFromNativeConverter(rclass);
/*      */ 
/*      */ 
/*      */           
/* 1825 */           closure_rtype = (Structure.FFIType.get(rclass.isPrimitive() ? rclass : Pointer.class).getPointer()).peer;
/* 1826 */           rtype = (Structure.FFIType.get(fromNative.nativeType()).getPointer()).peer;
/*      */           break;
/*      */         case 17:
/*      */         case 18:
/*      */         case 19:
/*      */         case 21:
/*      */         case 22:
/* 1833 */           closure_rtype = (Structure.FFIType.get(Pointer.class).getPointer()).peer;
/* 1834 */           rtype = (Structure.FFIType.get(NativeMappedConverter.getInstance(rclass).nativeType()).getPointer()).peer;
/*      */           break;
/*      */         case 3:
/*      */         case 26:
/* 1838 */           closure_rtype = rtype = (Structure.FFIType.get(Pointer.class).getPointer()).peer;
/*      */         
/*      */         case 4:
/* 1841 */           closure_rtype = (Structure.FFIType.get(Pointer.class).getPointer()).peer;
/* 1842 */           rtype = (Structure.FFIType.get(rclass).getPointer()).peer;
/*      */           break;
/*      */         default:
/* 1845 */           closure_rtype = rtype = (Structure.FFIType.get(rclass).getPointer()).peer;
/*      */           break;
/*      */       } 
/* 1848 */       for (int t = 0; t < ptypes.length; t++) {
/* 1849 */         Class<?> type = ptypes[t];
/* 1850 */         sig = sig + getSignature(type);
/* 1851 */         int conversionType = getConversion(type, mapper, allowObjects);
/* 1852 */         cvt[t] = conversionType;
/* 1853 */         if (conversionType == -1) {
/* 1854 */           throw new IllegalArgumentException(type + " is not a supported argument type (in method " + method.getName() + " in " + cls + ")");
/*      */         }
/* 1856 */         if (conversionType == 17 || conversionType == 18 || conversionType == 19 || conversionType == 21) {
/*      */ 
/*      */ 
/*      */           
/* 1860 */           type = NativeMappedConverter.getInstance(type).nativeType();
/* 1861 */         } else if (conversionType == 23 || conversionType == 24 || conversionType == 25) {
/*      */ 
/*      */           
/* 1864 */           toNative[t] = mapper.getToNativeConverter(type);
/*      */         } 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1870 */         switch (conversionType) {
/*      */           case 4:
/*      */           case 17:
/*      */           case 18:
/*      */           case 19:
/*      */           case 21:
/*      */           case 22:
/* 1877 */             atypes[t] = (Structure.FFIType.get(type).getPointer()).peer;
/* 1878 */             closure_atypes[t] = (Structure.FFIType.get(Pointer.class).getPointer()).peer;
/*      */             break;
/*      */           case 23:
/*      */           case 24:
/*      */           case 25:
/* 1883 */             closure_atypes[t] = (Structure.FFIType.get(type.isPrimitive() ? type : Pointer.class).getPointer()).peer;
/* 1884 */             atypes[t] = (Structure.FFIType.get(toNative[t].nativeType()).getPointer()).peer;
/*      */             break;
/*      */           case 0:
/* 1887 */             atypes[t] = (Structure.FFIType.get(type).getPointer()).peer; closure_atypes[t] = (Structure.FFIType.get(type).getPointer()).peer;
/*      */           
/*      */           default:
/* 1890 */             atypes[t] = (Structure.FFIType.get(Pointer.class).getPointer()).peer; closure_atypes[t] = (Structure.FFIType.get(Pointer.class).getPointer()).peer; break;
/*      */         } 
/*      */       } 
/* 1893 */       sig = sig + ")";
/* 1894 */       sig = sig + getSignature(rclass);
/*      */       
/* 1896 */       Class<?>[] etypes = method.getExceptionTypes();
/* 1897 */       for (int e = 0; e < etypes.length; e++) {
/* 1898 */         if (LastErrorException.class.isAssignableFrom(etypes[e])) {
/* 1899 */           throwLastError = true;
/*      */           
/*      */           break;
/*      */         } 
/*      */       } 
/* 1904 */       Function f = lib.getFunction(method.getName(), method);
/*      */       try {
/* 1906 */         handles[i] = registerMethod(cls, method.getName(), sig, cvt, closure_atypes, atypes, rcvt, closure_rtype, rtype, method, f.peer, f
/*      */ 
/*      */ 
/*      */ 
/*      */             
/* 1911 */             .getCallingConvention(), throwLastError, toNative, fromNative, f.encoding);
/*      */ 
/*      */       
/*      */       }
/* 1915 */       catch (NoSuchMethodError noSuchMethodError) {
/* 1916 */         throw new UnsatisfiedLinkError("No method " + method.getName() + " with signature " + sig + " in " + cls);
/*      */       } 
/*      */     } 
/* 1919 */     synchronized (registeredClasses) {
/* 1920 */       registeredClasses.put(cls, handles);
/* 1921 */       registeredLibraries.put(cls, lib);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Map<String, Object> cacheOptions(Class<?> cls, Map<String, ?> options, Object proxy) {
/* 1929 */     Map<String, Object> libOptions = new HashMap<String, Object>(options);
/* 1930 */     libOptions.put("enclosing-library", cls);
/* 1931 */     typeOptions.put(cls, libOptions);
/* 1932 */     if (proxy != null) {
/* 1933 */       libraries.put(cls, new WeakReference(proxy));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1939 */     if (!cls.isInterface() && Library.class
/* 1940 */       .isAssignableFrom(cls)) {
/* 1941 */       Class<?>[] ifaces = cls.getInterfaces();
/* 1942 */       for (Class<?> ifc : ifaces) {
/* 1943 */         if (Library.class.isAssignableFrom(ifc)) {
/* 1944 */           cacheOptions(ifc, libOptions, proxy);
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/* 1949 */     return libOptions;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static NativeMapped fromNative(Class<?> cls, Object value) {
/* 1973 */     return (NativeMapped)NativeMappedConverter.getInstance(cls).fromNative(value, new FromNativeContext(cls));
/*      */   }
/*      */   
/*      */   private static NativeMapped fromNative(Method m, Object value) {
/* 1977 */     Class<?> cls = m.getReturnType();
/* 1978 */     return (NativeMapped)NativeMappedConverter.getInstance(cls).fromNative(value, new MethodResultContext(cls, null, null, m));
/*      */   }
/*      */   
/*      */   private static Class<?> nativeType(Class<?> cls) {
/* 1982 */     return NativeMappedConverter.getInstance(cls).nativeType();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private static Object toNative(ToNativeConverter cvt, Object o) {
/* 1988 */     return cvt.toNative(o, new ToNativeContext());
/*      */   }
/*      */   
/*      */   private static Object fromNative(FromNativeConverter cvt, Object o, Method m) {
/* 1992 */     return cvt.fromNative(o, new MethodResultContext(m.getReturnType(), null, null, m));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void main(String[] args) {
/* 2011 */     String DEFAULT_TITLE = "Java Native Access (JNA)";
/* 2012 */     String DEFAULT_VERSION = "5.13.0";
/* 2013 */     String DEFAULT_BUILD = "5.13.0 (package information missing)";
/* 2014 */     Package pkg = Native.class.getPackage();
/*      */     
/* 2016 */     String title = (pkg != null) ? pkg.getSpecificationTitle() : "Java Native Access (JNA)";
/* 2017 */     if (title == null) title = "Java Native Access (JNA)";
/*      */     
/* 2019 */     String version = (pkg != null) ? pkg.getSpecificationVersion() : "5.13.0";
/* 2020 */     if (version == null) version = "5.13.0"; 
/* 2021 */     title = title + " API Version " + version;
/* 2022 */     System.out.println(title);
/*      */     
/* 2024 */     version = (pkg != null) ? pkg.getImplementationVersion() : "5.13.0 (package information missing)";
/* 2025 */     if (version == null) version = "5.13.0 (package information missing)"; 
/* 2026 */     System.out.println("Version: " + version);
/* 2027 */     System.out.println(" Native: " + getNativeVersion() + " (" + 
/* 2028 */         getAPIChecksum() + ")");
/* 2029 */     System.out.println(" Prefix: " + Platform.RESOURCE_PREFIX);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Structure invokeStructure(Function function, long fp, int callFlags, Object[] args, Structure s) {
/* 2154 */     invokeStructure(function, fp, callFlags, args, (s.getPointer()).peer, 
/* 2155 */         (s.getTypeInfo()).peer);
/* 2156 */     return s;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static long open(String name) {
/* 2174 */     return open(name, -1);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static Pointer getPointer(long addr) {
/* 2263 */     long peer = _getPointer(addr);
/* 2264 */     return (peer == 0L) ? null : new Pointer(peer);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static String getString(Pointer pointer, long offset) {
/* 2272 */     return getString(pointer, offset, getDefaultStringEncoding());
/*      */   }
/*      */   
/*      */   static String getString(Pointer pointer, long offset, String encoding) {
/* 2276 */     byte[] data = getStringBytes(pointer, pointer.peer, offset);
/* 2277 */     if (encoding != null) {
/*      */       try {
/* 2279 */         return new String(data, encoding);
/*      */       }
/* 2281 */       catch (UnsupportedEncodingException unsupportedEncodingException) {}
/*      */     }
/*      */     
/* 2284 */     return new String(data);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2326 */   private static final ThreadLocal<Memory> nativeThreadTerminationFlag = new ThreadLocal<Memory>()
/*      */     {
/*      */       protected Memory initialValue()
/*      */       {
/* 2330 */         Memory m = new Memory(4L);
/* 2331 */         m.clear();
/* 2332 */         return m;
/*      */       }
/*      */     };
/* 2335 */   private static final Map<Thread, Pointer> nativeThreads = Collections.synchronizedMap(new WeakHashMap<Thread, Pointer>()); private static native void initIDs(); public static synchronized native void setProtected(boolean paramBoolean); public static synchronized native boolean isProtected(); static native long getWindowHandle0(Component paramComponent); private static native long _getDirectBufferPointer(Buffer paramBuffer); private static native int sizeof(int paramInt);
/*      */   private static native String getNativeVersion();
/*      */   private static native String getAPIChecksum();
/*      */   public static native int getLastError();
/*      */   public static native void setLastError(int paramInt);
/*      */   private static native void unregister(Class<?> paramClass, long[] paramArrayOflong);
/*      */   private static native long registerMethod(Class<?> paramClass, String paramString1, String paramString2, int[] paramArrayOfint, long[] paramArrayOflong1, long[] paramArrayOflong2, int paramInt1, long paramLong1, long paramLong2, Method paramMethod, long paramLong3, int paramInt2, boolean paramBoolean, ToNativeConverter[] paramArrayOfToNativeConverter, FromNativeConverter paramFromNativeConverter, String paramString3);
/*      */   public static native long ffi_prep_cif(int paramInt1, int paramInt2, long paramLong1, long paramLong2);
/*      */   public static native void ffi_call(long paramLong1, long paramLong2, long paramLong3, long paramLong4);
/*      */   public static native long ffi_prep_closure(long paramLong, ffi_callback paramffi_callback);
/*      */   public static native void ffi_free_closure(long paramLong);
/*      */   static native int initialize_ffi_type(long paramLong);
/*      */   static synchronized native void freeNativeCallback(long paramLong);
/*      */   public static void detach(boolean detach) {
/* 2349 */     Thread thread = Thread.currentThread();
/* 2350 */     if (detach) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2357 */       nativeThreads.remove(thread);
/* 2358 */       Pointer p = nativeThreadTerminationFlag.get();
/* 2359 */       setDetachState(true, 0L);
/*      */     
/*      */     }
/* 2362 */     else if (!nativeThreads.containsKey(thread)) {
/* 2363 */       Pointer p = nativeThreadTerminationFlag.get();
/* 2364 */       nativeThreads.put(thread, p);
/* 2365 */       setDetachState(false, p.peer);
/*      */     } 
/*      */   } static synchronized native long createNativeCallback(Callback paramCallback, Method paramMethod, Class<?>[] paramArrayOfClass, Class<?> paramClass, int paramInt1, int paramInt2, String paramString); static native int invokeInt(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject); static native long invokeLong(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject); static native void invokeVoid(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject); static native float invokeFloat(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject); static native double invokeDouble(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject); static native long invokePointer(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject); private static native void invokeStructure(Function paramFunction, long paramLong1, int paramInt, Object[] paramArrayOfObject, long paramLong2, long paramLong3); static native Object invokeObject(Function paramFunction, long paramLong, int paramInt, Object[] paramArrayOfObject); static native long open(String paramString, int paramInt); static native void close(long paramLong); static native long findSymbol(long paramLong, String paramString); static native long indexOf(Pointer paramPointer, long paramLong1, long paramLong2, byte paramByte); static native void read(Pointer paramPointer, long paramLong1, long paramLong2, byte[] paramArrayOfbyte, int paramInt1, int paramInt2); static native void read(Pointer paramPointer, long paramLong1, long paramLong2, short[] paramArrayOfshort, int paramInt1, int paramInt2); static native void read(Pointer paramPointer, long paramLong1, long paramLong2, char[] paramArrayOfchar, int paramInt1, int paramInt2); static native void read(Pointer paramPointer, long paramLong1, long paramLong2, int[] paramArrayOfint, int paramInt1, int paramInt2); static native void read(Pointer paramPointer, long paramLong1, long paramLong2, long[] paramArrayOflong, int paramInt1, int paramInt2); static native void read(Pointer paramPointer, long paramLong1, long paramLong2, float[] paramArrayOffloat, int paramInt1, int paramInt2); static native void read(Pointer paramPointer, long paramLong1, long paramLong2, double[] paramArrayOfdouble, int paramInt1, int paramInt2); static native void write(Pointer paramPointer, long paramLong1, long paramLong2, byte[] paramArrayOfbyte, int paramInt1, int paramInt2); static native void write(Pointer paramPointer, long paramLong1, long paramLong2, short[] paramArrayOfshort, int paramInt1, int paramInt2); static native void write(Pointer paramPointer, long paramLong1, long paramLong2, char[] paramArrayOfchar, int paramInt1, int paramInt2); static native void write(Pointer paramPointer, long paramLong1, long paramLong2, int[] paramArrayOfint, int paramInt1, int paramInt2); static native void write(Pointer paramPointer, long paramLong1, long paramLong2, long[] paramArrayOflong, int paramInt1, int paramInt2); static native void write(Pointer paramPointer, long paramLong1, long paramLong2, float[] paramArrayOffloat, int paramInt1, int paramInt2); static native void write(Pointer paramPointer, long paramLong1, long paramLong2, double[] paramArrayOfdouble, int paramInt1, int paramInt2); static native byte getByte(Pointer paramPointer, long paramLong1, long paramLong2); static native char getChar(Pointer paramPointer, long paramLong1, long paramLong2); static native short getShort(Pointer paramPointer, long paramLong1, long paramLong2); static native int getInt(Pointer paramPointer, long paramLong1, long paramLong2); static native long getLong(Pointer paramPointer, long paramLong1, long paramLong2); static native float getFloat(Pointer paramPointer, long paramLong1, long paramLong2);
/*      */   static native double getDouble(Pointer paramPointer, long paramLong1, long paramLong2);
/*      */   private static native long _getPointer(long paramLong);
/*      */   static Pointer getTerminationFlag(Thread t) {
/* 2371 */     return nativeThreads.get(t);
/*      */   } static native String getWideString(Pointer paramPointer, long paramLong1, long paramLong2); static native byte[] getStringBytes(Pointer paramPointer, long paramLong1, long paramLong2); static native void setMemory(Pointer paramPointer, long paramLong1, long paramLong2, long paramLong3, byte paramByte); static native void setByte(Pointer paramPointer, long paramLong1, long paramLong2, byte paramByte); static native void setShort(Pointer paramPointer, long paramLong1, long paramLong2, short paramShort); static native void setChar(Pointer paramPointer, long paramLong1, long paramLong2, char paramChar); static native void setInt(Pointer paramPointer, long paramLong1, long paramLong2, int paramInt); static native void setLong(Pointer paramPointer, long paramLong1, long paramLong2, long paramLong3); static native void setFloat(Pointer paramPointer, long paramLong1, long paramLong2, float paramFloat); static native void setDouble(Pointer paramPointer, long paramLong1, long paramLong2, double paramDouble); static native void setPointer(Pointer paramPointer, long paramLong1, long paramLong2, long paramLong3); static native void setWideString(Pointer paramPointer, long paramLong1, long paramLong2, String paramString); static native ByteBuffer getDirectByteBuffer(Pointer paramPointer, long paramLong1, long paramLong2, long paramLong3); public static native long malloc(long paramLong);
/*      */   public static native void free(long paramLong);
/*      */   private static native void setDetachState(boolean paramBoolean, long paramLong);
/*      */   public static interface ffi_callback {
/*      */     void invoke(long param1Long1, long param1Long2, long param1Long3); }
/*      */   private static class Buffers { static boolean isBuffer(Class<?> cls) {
/* 2378 */       return Buffer.class.isAssignableFrom(cls);
/*      */     } }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static class AWT
/*      */   {
/*      */     static long getWindowID(Window w) throws HeadlessException {
/* 2387 */       return getComponentID(w);
/*      */     }
/*      */ 
/*      */     
/*      */     static long getComponentID(Object o) throws HeadlessException {
/* 2392 */       if (GraphicsEnvironment.isHeadless()) {
/* 2393 */         throw new HeadlessException("No native windows when headless");
/*      */       }
/* 2395 */       Component c = (Component)o;
/* 2396 */       if (c.isLightweight()) {
/* 2397 */         throw new IllegalArgumentException("Component must be heavyweight");
/*      */       }
/* 2399 */       if (!c.isDisplayable()) {
/* 2400 */         throw new IllegalStateException("Component must be displayable");
/*      */       }
/* 2402 */       if (Platform.isX11() && 
/* 2403 */         System.getProperty("java.version").startsWith("1.4") && 
/* 2404 */         !c.isVisible()) {
/* 2405 */         throw new IllegalStateException("Component must be visible");
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2411 */       return Native.getWindowHandle0(c);
/*      */     }
/*      */   }
/*      */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/com/sun/jna/Native.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */