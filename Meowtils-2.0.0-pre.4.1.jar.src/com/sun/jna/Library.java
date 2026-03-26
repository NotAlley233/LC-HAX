/*     */ package com.sun.jna;
/*     */ 
/*     */ import com.sun.jna.internal.ReflectionUtils;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.WeakHashMap;
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
/*     */ public interface Library
/*     */ {
/*     */   public static final String OPTION_TYPE_MAPPER = "type-mapper";
/*     */   public static final String OPTION_FUNCTION_MAPPER = "function-mapper";
/*     */   public static final String OPTION_INVOCATION_MAPPER = "invocation-mapper";
/*     */   public static final String OPTION_STRUCTURE_ALIGNMENT = "structure-alignment";
/*     */   public static final String OPTION_STRING_ENCODING = "string-encoding";
/*     */   public static final String OPTION_ALLOW_OBJECTS = "allow-objects";
/*     */   public static final String OPTION_CALLING_CONVENTION = "calling-convention";
/*     */   public static final String OPTION_OPEN_FLAGS = "open-flags";
/*     */   public static final String OPTION_CLASSLOADER = "classloader";
/*     */   public static final String OPTION_SYMBOL_PROVIDER = "symbol-provider";
/*     */   
/*     */   public static class Handler
/*     */     implements InvocationHandler
/*     */   {
/*     */     static final Method OBJECT_TOSTRING;
/*     */     static final Method OBJECT_HASHCODE;
/*     */     static final Method OBJECT_EQUALS;
/*     */     private final NativeLibrary nativeLibrary;
/*     */     private final Class<?> interfaceClass;
/*     */     private final Map<String, Object> options;
/*     */     private final InvocationMapper invocationMapper;
/*     */     
/*     */     static {
/*     */       try {
/* 130 */         OBJECT_TOSTRING = Object.class.getMethod("toString", new Class[0]);
/* 131 */         OBJECT_HASHCODE = Object.class.getMethod("hashCode", new Class[0]);
/* 132 */         OBJECT_EQUALS = Object.class.getMethod("equals", new Class[] { Object.class });
/* 133 */       } catch (Exception e) {
/* 134 */         throw new Error("Error retrieving Object.toString() method");
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private static final class FunctionInfo
/*     */     {
/*     */       final InvocationHandler handler;
/*     */       
/*     */       final Function function;
/*     */       
/*     */       final boolean isVarArgs;
/*     */       
/*     */       final Object methodHandle;
/*     */       final Map<String, ?> options;
/*     */       final Class<?>[] parameterTypes;
/*     */       
/*     */       FunctionInfo(Object mh) {
/* 152 */         this.handler = null;
/* 153 */         this.function = null;
/* 154 */         this.isVarArgs = false;
/* 155 */         this.options = null;
/* 156 */         this.parameterTypes = null;
/* 157 */         this.methodHandle = mh;
/*     */       }
/*     */       
/*     */       FunctionInfo(InvocationHandler handler, Function function, Class<?>[] parameterTypes, boolean isVarArgs, Map<String, ?> options) {
/* 161 */         this.handler = handler;
/* 162 */         this.function = function;
/* 163 */         this.isVarArgs = isVarArgs;
/* 164 */         this.options = options;
/* 165 */         this.parameterTypes = parameterTypes;
/* 166 */         this.methodHandle = null;
/*     */       }
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 175 */     private final Map<Method, FunctionInfo> functions = new WeakHashMap<Method, FunctionInfo>();
/*     */     
/*     */     public Handler(String libname, Class<?> interfaceClass, Map<String, ?> options) {
/* 178 */       if (libname != null && "".equals(libname.trim())) {
/* 179 */         throw new IllegalArgumentException("Invalid library name \"" + libname + "\"");
/*     */       }
/*     */       
/* 182 */       if (!interfaceClass.isInterface()) {
/* 183 */         throw new IllegalArgumentException(libname + " does not implement an interface: " + interfaceClass.getName());
/*     */       }
/*     */       
/* 186 */       this.interfaceClass = interfaceClass;
/* 187 */       this.options = new HashMap<String, Object>(options);
/* 188 */       int callingConvention = AltCallingConvention.class.isAssignableFrom(interfaceClass) ? 63 : 0;
/*     */ 
/*     */       
/* 191 */       if (this.options.get("calling-convention") == null) {
/* 192 */         this.options.put("calling-convention", Integer.valueOf(callingConvention));
/*     */       }
/* 194 */       if (this.options.get("classloader") == null) {
/* 195 */         this.options.put("classloader", interfaceClass.getClassLoader());
/*     */       }
/* 197 */       this.nativeLibrary = NativeLibrary.getInstance(libname, this.options);
/* 198 */       this.invocationMapper = (InvocationMapper)this.options.get("invocation-mapper");
/*     */     }
/*     */     
/*     */     public NativeLibrary getNativeLibrary() {
/* 202 */       return this.nativeLibrary;
/*     */     }
/*     */     
/*     */     public String getLibraryName() {
/* 206 */       return this.nativeLibrary.getName();
/*     */     }
/*     */     
/*     */     public Class<?> getInterfaceClass() {
/* 210 */       return this.interfaceClass;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] inArgs) throws Throwable {
/* 218 */       if (OBJECT_TOSTRING.equals(method))
/* 219 */         return "Proxy interface to " + this.nativeLibrary; 
/* 220 */       if (OBJECT_HASHCODE.equals(method))
/* 221 */         return Integer.valueOf(hashCode()); 
/* 222 */       if (OBJECT_EQUALS.equals(method)) {
/* 223 */         Object o = inArgs[0];
/* 224 */         if (o != null && Proxy.isProxyClass(o.getClass())) {
/* 225 */           return Function.valueOf((Proxy.getInvocationHandler(o) == this));
/*     */         }
/* 227 */         return Boolean.FALSE;
/*     */       } 
/*     */ 
/*     */       
/* 231 */       FunctionInfo f = this.functions.get(method);
/* 232 */       if (f == null) {
/* 233 */         synchronized (this.functions) {
/* 234 */           f = this.functions.get(method);
/* 235 */           if (f == null) {
/* 236 */             boolean isDefault = ReflectionUtils.isDefault(method);
/* 237 */             if (!isDefault) {
/* 238 */               boolean isVarArgs = Function.isVarArgs(method);
/* 239 */               InvocationHandler handler = null;
/* 240 */               if (this.invocationMapper != null) {
/* 241 */                 handler = this.invocationMapper.getInvocationHandler(this.nativeLibrary, method);
/*     */               }
/* 243 */               Function function = null;
/* 244 */               Class<?>[] parameterTypes = null;
/* 245 */               Map<String, Object> options = null;
/* 246 */               if (handler == null) {
/*     */                 
/* 248 */                 function = this.nativeLibrary.getFunction(method.getName(), method);
/* 249 */                 parameterTypes = method.getParameterTypes();
/* 250 */                 options = new HashMap<String, Object>(this.options);
/* 251 */                 options.put("invoking-method", method);
/*     */               } 
/* 253 */               f = new FunctionInfo(handler, function, parameterTypes, isVarArgs, options);
/*     */             } else {
/* 255 */               f = new FunctionInfo(ReflectionUtils.getMethodHandle(method));
/*     */             } 
/* 257 */             this.functions.put(method, f);
/*     */           } 
/*     */         } 
/*     */       }
/* 261 */       if (f.methodHandle != null) {
/* 262 */         return ReflectionUtils.invokeDefaultMethod(proxy, f.methodHandle, inArgs);
/*     */       }
/* 264 */       if (f.isVarArgs) {
/* 265 */         inArgs = Function.concatenateVarArgs(inArgs);
/*     */       }
/* 267 */       if (f.handler != null) {
/* 268 */         return f.handler.invoke(proxy, method, inArgs);
/*     */       }
/* 270 */       return f.function.invoke(method, f.parameterTypes, method.getReturnType(), inArgs, f.options);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/com/sun/jna/Library.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */