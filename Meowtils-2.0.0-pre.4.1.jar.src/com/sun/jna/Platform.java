/*     */ package com.sun.jna;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
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
/*     */ public final class Platform
/*     */ {
/*     */   public static final int UNSPECIFIED = -1;
/*     */   public static final int MAC = 0;
/*     */   public static final int LINUX = 1;
/*     */   public static final int WINDOWS = 2;
/*     */   public static final int SOLARIS = 3;
/*     */   public static final int FREEBSD = 4;
/*     */   public static final int OPENBSD = 5;
/*     */   public static final int WINDOWSCE = 6;
/*     */   public static final int AIX = 7;
/*     */   public static final int ANDROID = 8;
/*     */   public static final int GNU = 9;
/*     */   public static final int KFREEBSD = 10;
/*     */   public static final int NETBSD = 11;
/*     */   public static final boolean RO_FIELDS;
/*     */   public static final boolean HAS_BUFFERS;
/*     */   
/*     */   static {
/*  72 */     String osName = System.getProperty("os.name");
/*  73 */     if (osName.startsWith("Linux")) {
/*  74 */       if ("dalvik".equals(System.getProperty("java.vm.name").toLowerCase())) {
/*  75 */         osType = 8;
/*     */         
/*  77 */         System.setProperty("jna.nounpack", "true");
/*     */       } else {
/*     */         
/*  80 */         osType = 1;
/*     */       }
/*     */     
/*  83 */     } else if (osName.startsWith("AIX")) {
/*  84 */       osType = 7;
/*     */     }
/*  86 */     else if (osName.startsWith("Mac") || osName.startsWith("Darwin")) {
/*  87 */       osType = 0;
/*     */     }
/*  89 */     else if (osName.startsWith("Windows CE")) {
/*  90 */       osType = 6;
/*     */     }
/*  92 */     else if (osName.startsWith("Windows")) {
/*  93 */       osType = 2;
/*     */     }
/*  95 */     else if (osName.startsWith("Solaris") || osName.startsWith("SunOS")) {
/*  96 */       osType = 3;
/*     */     }
/*  98 */     else if (osName.startsWith("FreeBSD")) {
/*  99 */       osType = 4;
/*     */     }
/* 101 */     else if (osName.startsWith("OpenBSD")) {
/* 102 */       osType = 5;
/*     */     }
/* 104 */     else if (osName.equalsIgnoreCase("gnu")) {
/* 105 */       osType = 9;
/*     */     }
/* 107 */     else if (osName.equalsIgnoreCase("gnu/kfreebsd")) {
/* 108 */       osType = 10;
/*     */     }
/* 110 */     else if (osName.equalsIgnoreCase("netbsd")) {
/* 111 */       osType = 11;
/*     */     } else {
/*     */       
/* 114 */       osType = -1;
/*     */     } 
/* 116 */     boolean hasBuffers = false;
/*     */     try {
/* 118 */       Class.forName("java.nio.Buffer");
/* 119 */       hasBuffers = true;
/*     */     }
/* 121 */     catch (ClassNotFoundException classNotFoundException) {}
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 126 */   public static final boolean HAS_AWT = (osType != 6 && osType != 8 && osType != 7);
/* 127 */   public static final boolean HAS_JAWT = (HAS_AWT && osType != 0); public static final String MATH_LIBRARY_NAME; public static final String C_LIBRARY_NAME; public static final boolean HAS_DLL_CALLBACKS; static {
/* 128 */     HAS_BUFFERS = hasBuffers;
/* 129 */     RO_FIELDS = (osType != 6);
/* 130 */     C_LIBRARY_NAME = (osType == 2) ? "msvcrt" : ((osType == 6) ? "coredll" : "c");
/* 131 */     MATH_LIBRARY_NAME = (osType == 2) ? "msvcrt" : ((osType == 6) ? "coredll" : "m");
/* 132 */     ARCH = getCanonicalArchitecture(System.getProperty("os.arch"), osType);
/*     */     
/* 134 */     HAS_DLL_CALLBACKS = (osType == 2 && !ARCH.startsWith("aarch"));
/* 135 */     RESOURCE_PREFIX = getNativeLibraryResourcePrefix();
/*     */   }
/*     */   public static final String RESOURCE_PREFIX; private static final int osType; public static final String ARCH;
/*     */   public static final int getOSType() {
/* 139 */     return osType;
/*     */   }
/*     */   public static final boolean isMac() {
/* 142 */     return (osType == 0);
/*     */   }
/*     */   public static final boolean isAndroid() {
/* 145 */     return (osType == 8);
/*     */   }
/*     */   public static final boolean isLinux() {
/* 148 */     return (osType == 1);
/*     */   }
/*     */   public static final boolean isAIX() {
/* 151 */     return (osType == 7);
/*     */   }
/*     */   public static final boolean isWindowsCE() {
/* 154 */     return (osType == 6);
/*     */   }
/*     */   
/*     */   public static final boolean isWindows() {
/* 158 */     return (osType == 2 || osType == 6);
/*     */   }
/*     */   public static final boolean isSolaris() {
/* 161 */     return (osType == 3);
/*     */   }
/*     */   public static final boolean isFreeBSD() {
/* 164 */     return (osType == 4);
/*     */   }
/*     */   public static final boolean isOpenBSD() {
/* 167 */     return (osType == 5);
/*     */   }
/*     */   public static final boolean isNetBSD() {
/* 170 */     return (osType == 11);
/*     */   }
/*     */   public static final boolean isGNU() {
/* 173 */     return (osType == 9);
/*     */   }
/*     */   public static final boolean iskFreeBSD() {
/* 176 */     return (osType == 10);
/*     */   }
/*     */   
/*     */   public static final boolean isX11() {
/* 180 */     return (!isWindows() && !isMac());
/*     */   }
/*     */   public static final boolean hasRuntimeExec() {
/* 183 */     if (isWindowsCE() && "J9".equals(System.getProperty("java.vm.name")))
/* 184 */       return false; 
/* 185 */     return true;
/*     */   }
/*     */   public static final boolean is64Bit() {
/* 188 */     String model = System.getProperty("sun.arch.data.model", 
/* 189 */         System.getProperty("com.ibm.vm.bitmode"));
/* 190 */     if (model != null) {
/* 191 */       return "64".equals(model);
/*     */     }
/* 193 */     if ("x86-64".equals(ARCH) || "ia64"
/* 194 */       .equals(ARCH) || "ppc64"
/* 195 */       .equals(ARCH) || "ppc64le".equals(ARCH) || "sparcv9"
/* 196 */       .equals(ARCH) || "mips64"
/* 197 */       .equals(ARCH) || "mips64el".equals(ARCH) || "loongarch64"
/* 198 */       .equals(ARCH) || "amd64"
/* 199 */       .equals(ARCH) || "aarch64"
/* 200 */       .equals(ARCH)) {
/* 201 */       return true;
/*     */     }
/* 203 */     return (Native.POINTER_SIZE == 8);
/*     */   }
/*     */   
/*     */   public static final boolean isIntel() {
/* 207 */     if (ARCH.startsWith("x86")) {
/* 208 */       return true;
/*     */     }
/* 210 */     return false;
/*     */   }
/*     */   
/*     */   public static final boolean isPPC() {
/* 214 */     if (ARCH.startsWith("ppc")) {
/* 215 */       return true;
/*     */     }
/* 217 */     return false;
/*     */   }
/*     */   
/*     */   public static final boolean isARM() {
/* 221 */     return (ARCH.startsWith("arm") || ARCH.startsWith("aarch"));
/*     */   }
/*     */   
/*     */   public static final boolean isSPARC() {
/* 225 */     return ARCH.startsWith("sparc");
/*     */   }
/*     */   
/*     */   public static final boolean isMIPS() {
/* 229 */     if (ARCH.equals("mips") || ARCH
/* 230 */       .equals("mips64") || ARCH
/* 231 */       .equals("mipsel") || ARCH
/* 232 */       .equals("mips64el")) {
/* 233 */       return true;
/*     */     }
/* 235 */     return false;
/*     */   }
/*     */   
/*     */   public static final boolean isLoongArch() {
/* 239 */     return ARCH.startsWith("loongarch");
/*     */   }
/*     */   
/*     */   static String getCanonicalArchitecture(String arch, int platform) {
/* 243 */     arch = arch.toLowerCase().trim();
/* 244 */     if ("powerpc".equals(arch)) {
/* 245 */       arch = "ppc";
/*     */     }
/* 247 */     else if ("powerpc64".equals(arch)) {
/* 248 */       arch = "ppc64";
/*     */     }
/* 250 */     else if ("i386".equals(arch) || "i686".equals(arch)) {
/* 251 */       arch = "x86";
/*     */     }
/* 253 */     else if ("x86_64".equals(arch) || "amd64".equals(arch)) {
/* 254 */       arch = "x86-64";
/*     */     }
/* 256 */     else if ("zarch_64".equals(arch)) {
/* 257 */       arch = "s390x";
/*     */     } 
/*     */ 
/*     */     
/* 261 */     if ("ppc64".equals(arch) && "little".equals(System.getProperty("sun.cpu.endian"))) {
/* 262 */       arch = "ppc64le";
/*     */     }
/*     */     
/* 265 */     if ("arm".equals(arch) && platform == 1 && isSoftFloat()) {
/* 266 */       arch = "armel";
/*     */     }
/*     */     
/* 269 */     return arch;
/*     */   }
/*     */   
/*     */   static boolean isSoftFloat() {
/*     */     try {
/* 274 */       File self = new File("/proc/self/exe");
/* 275 */       if (self.exists()) {
/* 276 */         ELFAnalyser ahfd = ELFAnalyser.analyse(self.getCanonicalPath());
/* 277 */         return !ahfd.isArmHardFloat();
/*     */       } 
/* 279 */     } catch (IOException ex) {
/*     */       
/* 281 */       Logger.getLogger(Platform.class.getName()).log(Level.INFO, "Failed to read '/proc/self/exe' or the target binary.", ex);
/* 282 */     } catch (SecurityException ex) {
/*     */       
/* 284 */       Logger.getLogger(Platform.class.getName()).log(Level.INFO, "SecurityException while analysing '/proc/self/exe' or the target binary.", ex);
/*     */     } 
/* 286 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static String getNativeLibraryResourcePrefix() {
/* 293 */     String prefix = System.getProperty("jna.prefix");
/* 294 */     if (prefix != null) {
/* 295 */       return prefix;
/*     */     }
/* 297 */     return getNativeLibraryResourcePrefix(getOSType(), System.getProperty("os.arch"), System.getProperty("os.name"));
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
/*     */   static String getNativeLibraryResourcePrefix(int osType, String arch, String name) {
/* 309 */     arch = getCanonicalArchitecture(arch, osType);
/* 310 */     switch (osType)
/*     */     { case 8:
/* 312 */         if (arch.startsWith("arm")) {
/* 313 */           arch = "arm";
/*     */         }
/* 315 */         osPrefix = "android-" + arch;
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
/* 353 */         return osPrefix;case 2: osPrefix = "win32-" + arch; return osPrefix;case 6: osPrefix = "w32ce-" + arch; return osPrefix;case 0: osPrefix = "darwin-" + arch; return osPrefix;case 1: osPrefix = "linux-" + arch; return osPrefix;case 3: osPrefix = "sunos-" + arch; return osPrefix;case 4: osPrefix = "freebsd-" + arch; return osPrefix;case 5: osPrefix = "openbsd-" + arch; return osPrefix;case 11: osPrefix = "netbsd-" + arch; return osPrefix;case 10: osPrefix = "kfreebsd-" + arch; return osPrefix; }  String osPrefix = name.toLowerCase(); int space = osPrefix.indexOf(" "); if (space != -1) osPrefix = osPrefix.substring(0, space);  osPrefix = osPrefix + "-" + arch; return osPrefix;
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/com/sun/jna/Platform.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */