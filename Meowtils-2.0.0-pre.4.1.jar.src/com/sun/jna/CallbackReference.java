/*     */ package com.sun.jna;
/*     */ 
/*     */ import com.sun.jna.internal.Cleaner;
/*     */ import java.io.Closeable;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public class CallbackReference
/*     */   extends WeakReference<Callback>
/*     */   implements Closeable
/*     */ {
/*  56 */   static final Map<Callback, CallbackReference> callbackMap = new WeakHashMap<Callback, CallbackReference>();
/*  57 */   static final Map<Callback, CallbackReference> directCallbackMap = new WeakHashMap<Callback, CallbackReference>();
/*     */   
/*  59 */   static final Map<Pointer, Reference<Callback>[]> pointerCallbackMap = (Map)new WeakHashMap<Pointer, Reference<Callback>>();
/*     */ 
/*     */   
/*  62 */   static final Map<Object, Object> allocations = Collections.synchronizedMap(new WeakHashMap<Object, Object>());
/*     */   
/*  64 */   private static final Map<Long, Reference<CallbackReference>> allocatedMemory = new ConcurrentHashMap<Long, Reference<CallbackReference>>();
/*     */   private static final Method PROXY_CALLBACK_METHOD;
/*     */   private static final Class<?> DLL_CALLBACK_CLASS;
/*     */   
/*     */   static {
/*     */     try {
/*  70 */       PROXY_CALLBACK_METHOD = CallbackProxy.class.getMethod("callback", new Class[] { Object[].class });
/*  71 */     } catch (Exception e) {
/*  72 */       throw new Error("Error looking up CallbackProxy.callback() method");
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  79 */     if (Platform.isWindows()) {
/*     */       try {
/*  81 */         DLL_CALLBACK_CLASS = Class.forName("com.sun.jna.win32.DLLCallback");
/*  82 */       } catch (ClassNotFoundException e) {
/*  83 */         throw new Error("Error loading DLLCallback class", e);
/*     */       } 
/*     */     } else {
/*  86 */       DLL_CALLBACK_CLASS = null;
/*     */     } 
/*     */   }
/*     */   
/*  90 */   private static final Map<Callback, CallbackThreadInitializer> initializers = new WeakHashMap<Callback, CallbackThreadInitializer>(); Cleaner.Cleanable cleanable;
/*     */   Pointer cbstruct;
/*     */   Pointer trampoline;
/*     */   CallbackProxy proxy;
/*     */   Method method;
/*     */   int callingConvention;
/*     */   
/*     */   static CallbackThreadInitializer setCallbackThreadInitializer(Callback cb, CallbackThreadInitializer initializer) {
/*  98 */     synchronized (initializers) {
/*  99 */       if (initializer != null) {
/* 100 */         return initializers.put(cb, initializer);
/*     */       }
/* 102 */       return initializers.remove(cb);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static class AttachOptions
/*     */     extends Structure
/*     */   {
/*     */     AttachOptions() {
/* 114 */       setStringEncoding("utf8");
/*     */     }
/*     */     public static final List<String> FIELDS = createFieldsOrder(new String[] { "daemon", "detach", "name" }); public boolean daemon;
/*     */     
/*     */     protected List<String> getFieldOrder() {
/* 119 */       return FIELDS;
/*     */     }
/*     */     public boolean detach;
/*     */     public String name; }
/*     */   
/*     */   private static ThreadGroup initializeThread(Callback cb, AttachOptions args) {
/* 125 */     CallbackThreadInitializer init = null;
/* 126 */     if (cb instanceof DefaultCallbackProxy) {
/* 127 */       cb = ((DefaultCallbackProxy)cb).getCallback();
/*     */     }
/* 129 */     synchronized (initializers) {
/* 130 */       init = initializers.get(cb);
/*     */     } 
/* 132 */     ThreadGroup group = null;
/* 133 */     if (init != null) {
/* 134 */       group = init.getThreadGroup(cb);
/* 135 */       args.name = init.getName(cb);
/* 136 */       args.daemon = init.isDaemon(cb);
/* 137 */       args.detach = init.detach(cb);
/* 138 */       args.write();
/*     */     } 
/* 140 */     return group;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Callback getCallback(Class<?> type, Pointer p) {
/* 151 */     return getCallback(type, p, false);
/*     */   }
/*     */   
/*     */   private static Callback getCallback(Class<?> type, Pointer p, boolean direct) {
/* 155 */     if (p == null) {
/* 156 */       return null;
/*     */     }
/*     */     
/* 159 */     if (!type.isInterface())
/* 160 */       throw new IllegalArgumentException("Callback type must be an interface"); 
/* 161 */     Map<Callback, CallbackReference> map = direct ? directCallbackMap : callbackMap;
/* 162 */     synchronized (pointerCallbackMap) {
/* 163 */       Reference[] arrayOfReference = (Reference[])pointerCallbackMap.get(p);
/* 164 */       Callback cb = getTypeAssignableCallback(type, (Reference<Callback>[])arrayOfReference);
/* 165 */       if (cb != null) {
/* 166 */         return cb;
/*     */       }
/* 168 */       cb = createCallback(type, p);
/* 169 */       pointerCallbackMap.put(p, addCallbackToArray(cb, (Reference<Callback>[])arrayOfReference));
/*     */ 
/*     */       
/* 172 */       map.remove(cb);
/* 173 */       return cb;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static Callback getTypeAssignableCallback(Class<?> type, Reference<Callback>[] array) {
/* 178 */     if (array != null) {
/* 179 */       for (int i = 0; i < array.length; i++) {
/* 180 */         Callback cb = array[i].get();
/* 181 */         if (cb != null && type.isAssignableFrom(cb.getClass())) {
/* 182 */           return cb;
/*     */         }
/*     */       } 
/*     */     }
/* 186 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private static Reference<Callback>[] addCallbackToArray(Callback cb, Reference<Callback>[] array) {
/* 191 */     int reqArraySize = 1;
/* 192 */     if (array != null)
/*     */     {
/* 194 */       for (int i = 0; i < array.length; i++) {
/* 195 */         if (array[i].get() == null) {
/* 196 */           array[i] = null;
/*     */         } else {
/*     */           
/* 199 */           reqArraySize++;
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 204 */     Reference[] arrayOfReference = new Reference[reqArraySize];
/* 205 */     int nidx = 0;
/* 206 */     if (array != null)
/*     */     {
/* 208 */       for (int i = 0; i < array.length; i++) {
/* 209 */         if (array[i] != null) {
/* 210 */           arrayOfReference[nidx++] = array[i];
/*     */         }
/*     */       } 
/*     */     }
/* 214 */     arrayOfReference[nidx] = new WeakReference<Callback>(cb);
/* 215 */     return (Reference<Callback>[])arrayOfReference;
/*     */   }
/*     */   
/*     */   private static Callback createCallback(Class<?> type, Pointer p) {
/* 219 */     int ctype = AltCallingConvention.class.isAssignableFrom(type) ? 63 : 0;
/*     */     
/* 221 */     Map<String, Object> foptions = new HashMap<String, Object>(Native.getLibraryOptions(type));
/* 222 */     foptions.put("invoking-method", getCallbackMethod(type));
/* 223 */     NativeFunctionHandler h = new NativeFunctionHandler(p, ctype, foptions);
/* 224 */     return (Callback)Proxy.newProxyInstance(type.getClassLoader(), new Class[] { type }, h);
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
/*     */   private CallbackReference(Callback callback, int callingConvention, boolean direct) {
/* 236 */     super(callback);
/* 237 */     TypeMapper mapper = Native.getTypeMapper(callback.getClass());
/* 238 */     this.callingConvention = callingConvention;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 244 */     boolean ppc = Platform.isPPC();
/* 245 */     if (direct) {
/* 246 */       Method m = getCallbackMethod(callback);
/* 247 */       Class<?>[] ptypes = m.getParameterTypes();
/* 248 */       for (int i = 0; i < ptypes.length; i++) {
/*     */         
/* 250 */         if (ppc && (ptypes[i] == float.class || ptypes[i] == double.class)) {
/*     */           
/* 252 */           direct = false;
/*     */           
/*     */           break;
/*     */         } 
/* 256 */         if (mapper != null && mapper
/* 257 */           .getFromNativeConverter(ptypes[i]) != null) {
/* 258 */           direct = false;
/*     */           
/*     */           break;
/*     */         } 
/*     */       } 
/* 263 */       if (mapper != null && mapper
/* 264 */         .getToNativeConverter(m.getReturnType()) != null) {
/* 265 */         direct = false;
/*     */       }
/*     */     } 
/*     */     
/* 269 */     String encoding = Native.getStringEncoding(callback.getClass());
/* 270 */     long peer = 0L;
/* 271 */     if (direct) {
/* 272 */       this.method = getCallbackMethod(callback);
/* 273 */       Class<?>[] nativeParamTypes = this.method.getParameterTypes();
/* 274 */       Class<?> returnType = this.method.getReturnType();
/* 275 */       int flags = 1;
/* 276 */       if (DLL_CALLBACK_CLASS != null && DLL_CALLBACK_CLASS
/* 277 */         .isInstance(callback)) {
/* 278 */         flags |= 0x2;
/*     */       }
/* 280 */       peer = Native.createNativeCallback(callback, this.method, nativeParamTypes, returnType, callingConvention, flags, encoding);
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 285 */       if (callback instanceof CallbackProxy) {
/* 286 */         this.proxy = (CallbackProxy)callback;
/*     */       } else {
/*     */         
/* 289 */         this.proxy = new DefaultCallbackProxy(getCallbackMethod(callback), mapper, encoding);
/*     */       } 
/* 291 */       Class<?>[] nativeParamTypes = this.proxy.getParameterTypes();
/* 292 */       Class<?> returnType = this.proxy.getReturnType();
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 297 */       if (mapper != null) {
/* 298 */         for (int j = 0; j < nativeParamTypes.length; j++) {
/* 299 */           FromNativeConverter rc = mapper.getFromNativeConverter(nativeParamTypes[j]);
/* 300 */           if (rc != null) {
/* 301 */             nativeParamTypes[j] = rc.nativeType();
/*     */           }
/*     */         } 
/* 304 */         ToNativeConverter tn = mapper.getToNativeConverter(returnType);
/* 305 */         if (tn != null) {
/* 306 */           returnType = tn.nativeType();
/*     */         }
/*     */       } 
/* 309 */       for (int i = 0; i < nativeParamTypes.length; i++) {
/* 310 */         nativeParamTypes[i] = getNativeType(nativeParamTypes[i]);
/* 311 */         if (!isAllowableNativeType(nativeParamTypes[i])) {
/* 312 */           String msg = "Callback argument " + nativeParamTypes[i] + " requires custom type conversion";
/*     */           
/* 314 */           throw new IllegalArgumentException(msg);
/*     */         } 
/*     */       } 
/* 317 */       returnType = getNativeType(returnType);
/* 318 */       if (!isAllowableNativeType(returnType)) {
/* 319 */         String msg = "Callback return type " + returnType + " requires custom type conversion";
/*     */         
/* 321 */         throw new IllegalArgumentException(msg);
/*     */       } 
/*     */       
/* 324 */       int flags = (DLL_CALLBACK_CLASS != null && DLL_CALLBACK_CLASS.isInstance(callback)) ? 2 : 0;
/*     */       
/* 326 */       peer = Native.createNativeCallback(this.proxy, PROXY_CALLBACK_METHOD, nativeParamTypes, returnType, callingConvention, flags, encoding);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 331 */     this.cbstruct = (peer != 0L) ? new Pointer(peer) : null;
/* 332 */     if (peer != 0L) {
/* 333 */       allocatedMemory.put(Long.valueOf(peer), new WeakReference<CallbackReference>(this));
/* 334 */       this.cleanable = Cleaner.getCleaner().register(this, new CallbackReferenceDisposer(this.cbstruct));
/*     */     } 
/*     */   }
/*     */   
/*     */   private Class<?> getNativeType(Class<?> cls) {
/* 339 */     if (Structure.class.isAssignableFrom(cls))
/*     */     
/* 341 */     { Structure.validate((Class)cls);
/* 342 */       if (!Structure.ByValue.class.isAssignableFrom(cls))
/* 343 */         return Pointer.class;  }
/* 344 */     else { if (NativeMapped.class.isAssignableFrom(cls))
/* 345 */         return NativeMappedConverter.getInstance(cls).nativeType(); 
/* 346 */       if (cls == String.class || cls == WString.class || cls == String[].class || cls == WString[].class || Callback.class
/*     */ 
/*     */ 
/*     */         
/* 350 */         .isAssignableFrom(cls))
/* 351 */         return Pointer.class;  }
/*     */     
/* 353 */     return cls;
/*     */   }
/*     */   
/*     */   private static Method checkMethod(Method m) {
/* 357 */     if ((m.getParameterTypes()).length > 256) {
/* 358 */       String msg = "Method signature exceeds the maximum parameter count: " + m;
/*     */       
/* 360 */       throw new UnsupportedOperationException(msg);
/*     */     } 
/* 362 */     return m;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static Class<?> findCallbackClass(Class<?> type) {
/* 371 */     if (!Callback.class.isAssignableFrom(type)) {
/* 372 */       throw new IllegalArgumentException(type.getName() + " is not derived from com.sun.jna.Callback");
/*     */     }
/* 374 */     if (type.isInterface()) {
/* 375 */       return type;
/*     */     }
/* 377 */     Class<?>[] ifaces = type.getInterfaces();
/* 378 */     for (int i = 0; i < ifaces.length; i++) {
/* 379 */       if (Callback.class.isAssignableFrom(ifaces[i])) {
/*     */         
/*     */         try {
/* 382 */           getCallbackMethod(ifaces[i]);
/* 383 */           return ifaces[i];
/*     */         }
/* 385 */         catch (IllegalArgumentException e) {
/*     */           break;
/*     */         } 
/*     */       }
/*     */     } 
/* 390 */     if (Callback.class.isAssignableFrom(type.getSuperclass())) {
/* 391 */       return findCallbackClass(type.getSuperclass());
/*     */     }
/* 393 */     return type;
/*     */   }
/*     */   
/*     */   private static Method getCallbackMethod(Callback callback) {
/* 397 */     return getCallbackMethod(findCallbackClass(callback.getClass()));
/*     */   }
/*     */ 
/*     */   
/*     */   private static Method getCallbackMethod(Class<?> cls) {
/* 402 */     Method[] pubMethods = cls.getDeclaredMethods();
/* 403 */     Method[] classMethods = cls.getMethods();
/* 404 */     Set<Method> pmethods = new HashSet<Method>(Arrays.asList(pubMethods));
/* 405 */     pmethods.retainAll(Arrays.asList((Object[])classMethods));
/*     */ 
/*     */     
/* 408 */     for (Iterator<Method> i = pmethods.iterator(); i.hasNext(); ) {
/* 409 */       Method m = i.next();
/* 410 */       if (Callback.FORBIDDEN_NAMES.contains(m.getName())) {
/* 411 */         i.remove();
/*     */       }
/*     */     } 
/*     */     
/* 415 */     Method[] methods = pmethods.<Method>toArray(new Method[0]);
/* 416 */     if (methods.length == 1) {
/* 417 */       return checkMethod(methods[0]);
/*     */     }
/* 419 */     for (int j = 0; j < methods.length; j++) {
/* 420 */       Method m = methods[j];
/* 421 */       if ("callback".equals(m.getName())) {
/* 422 */         return checkMethod(m);
/*     */       }
/*     */     } 
/* 425 */     String msg = "Callback must implement a single public method, or one public method named 'callback'";
/*     */     
/* 427 */     throw new IllegalArgumentException(msg);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setCallbackOptions(int options) {
/* 432 */     this.cbstruct.setInt(Native.POINTER_SIZE, options);
/*     */   }
/*     */ 
/*     */   
/*     */   public Pointer getTrampoline() {
/* 437 */     if (this.trampoline == null) {
/* 438 */       this.trampoline = this.cbstruct.getPointer(0L);
/*     */     }
/* 440 */     return this.trampoline;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 445 */     if (this.cleanable != null) {
/* 446 */       this.cleanable.clean();
/*     */     }
/* 448 */     this.cbstruct = null;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected void dispose() {
/* 453 */     close();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   static void disposeAll() {
/* 459 */     Collection<Reference<CallbackReference>> refs = new LinkedList<Reference<CallbackReference>>(allocatedMemory.values());
/* 460 */     for (Reference<CallbackReference> r : refs) {
/* 461 */       CallbackReference ref = r.get();
/* 462 */       if (ref != null) {
/* 463 */         ref.close();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private Callback getCallback() {
/* 469 */     return get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Pointer getNativeFunctionPointer(Callback cb) {
/* 476 */     if (Proxy.isProxyClass(cb.getClass())) {
/* 477 */       Object handler = Proxy.getInvocationHandler(cb);
/* 478 */       if (handler instanceof NativeFunctionHandler) {
/* 479 */         return ((NativeFunctionHandler)handler).getPointer();
/*     */       }
/*     */     } 
/* 482 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Pointer getFunctionPointer(Callback cb) {
/* 489 */     return getFunctionPointer(cb, false);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Pointer getFunctionPointer(Callback cb, boolean direct) {
/* 494 */     Pointer fp = null;
/* 495 */     if (cb == null) {
/* 496 */       return null;
/*     */     }
/* 498 */     if ((fp = getNativeFunctionPointer(cb)) != null) {
/* 499 */       return fp;
/*     */     }
/* 501 */     Map<String, ?> options = Native.getLibraryOptions(cb.getClass());
/*     */ 
/*     */ 
/*     */     
/* 505 */     int callingConvention = (cb instanceof AltCallingConvention) ? 63 : ((options != null && options.containsKey("calling-convention")) ? ((Integer)options.get("calling-convention")).intValue() : 0);
/*     */ 
/*     */     
/* 508 */     Map<Callback, CallbackReference> map = direct ? directCallbackMap : callbackMap;
/* 509 */     synchronized (pointerCallbackMap) {
/* 510 */       CallbackReference cbref = map.get(cb);
/* 511 */       if (cbref == null) {
/* 512 */         cbref = new CallbackReference(cb, callingConvention, direct);
/* 513 */         map.put(cb, cbref);
/* 514 */         pointerCallbackMap.put(cbref.getTrampoline(), 
/* 515 */             addCallbackToArray(cb, null));
/*     */         
/* 517 */         if (initializers.containsKey(cb)) {
/* 518 */           cbref.setCallbackOptions(1);
/*     */         }
/*     */       } 
/* 521 */       return cbref.getTrampoline();
/*     */     } 
/*     */   }
/*     */   
/*     */   private class DefaultCallbackProxy implements CallbackProxy { private final Method callbackMethod;
/*     */     private ToNativeConverter toNative;
/*     */     private final FromNativeConverter[] fromNative;
/*     */     private final String encoding;
/*     */     
/*     */     public DefaultCallbackProxy(Method callbackMethod, TypeMapper mapper, String encoding) {
/* 531 */       this.callbackMethod = callbackMethod;
/* 532 */       this.encoding = encoding;
/* 533 */       Class<?>[] argTypes = callbackMethod.getParameterTypes();
/* 534 */       Class<?> returnType = callbackMethod.getReturnType();
/* 535 */       this.fromNative = new FromNativeConverter[argTypes.length];
/* 536 */       if (NativeMapped.class.isAssignableFrom(returnType)) {
/* 537 */         this.toNative = NativeMappedConverter.getInstance(returnType);
/*     */       }
/* 539 */       else if (mapper != null) {
/* 540 */         this.toNative = mapper.getToNativeConverter(returnType);
/*     */       } 
/* 542 */       for (int i = 0; i < this.fromNative.length; i++) {
/* 543 */         if (NativeMapped.class.isAssignableFrom(argTypes[i])) {
/* 544 */           this.fromNative[i] = new NativeMappedConverter(argTypes[i]);
/*     */         }
/* 546 */         else if (mapper != null) {
/* 547 */           this.fromNative[i] = mapper.getFromNativeConverter(argTypes[i]);
/*     */         } 
/*     */       } 
/* 550 */       if (!callbackMethod.isAccessible()) {
/*     */         try {
/* 552 */           callbackMethod.setAccessible(true);
/*     */         }
/* 554 */         catch (SecurityException e) {
/* 555 */           throw new IllegalArgumentException("Callback method is inaccessible, make sure the interface is public: " + callbackMethod);
/*     */         } 
/*     */       }
/*     */     }
/*     */     
/*     */     public Callback getCallback() {
/* 561 */       return CallbackReference.this.getCallback();
/*     */     }
/*     */     
/*     */     private Object invokeCallback(Object[] args) {
/* 565 */       Class<?>[] paramTypes = this.callbackMethod.getParameterTypes();
/* 566 */       Object[] callbackArgs = new Object[args.length];
/*     */ 
/*     */       
/* 569 */       for (int i = 0; i < args.length; i++) {
/* 570 */         Class<?> type = paramTypes[i];
/* 571 */         Object arg = args[i];
/* 572 */         if (this.fromNative[i] != null) {
/* 573 */           FromNativeContext context = new CallbackParameterContext(type, this.callbackMethod, args, i);
/*     */           
/* 575 */           callbackArgs[i] = this.fromNative[i].fromNative(arg, context);
/*     */         } else {
/* 577 */           callbackArgs[i] = convertArgument(arg, type);
/*     */         } 
/*     */       } 
/*     */       
/* 581 */       Object result = null;
/* 582 */       Callback cb = getCallback();
/* 583 */       if (cb != null) {
/*     */         try {
/* 585 */           result = convertResult(this.callbackMethod.invoke(cb, callbackArgs));
/*     */         }
/* 587 */         catch (IllegalArgumentException e) {
/* 588 */           Native.getCallbackExceptionHandler().uncaughtException(cb, e);
/*     */         }
/* 590 */         catch (IllegalAccessException e) {
/* 591 */           Native.getCallbackExceptionHandler().uncaughtException(cb, e);
/*     */         }
/* 593 */         catch (InvocationTargetException e) {
/* 594 */           Native.getCallbackExceptionHandler().uncaughtException(cb, e.getTargetException());
/*     */         } 
/*     */       }
/*     */       
/* 598 */       for (int j = 0; j < callbackArgs.length; j++) {
/* 599 */         if (callbackArgs[j] instanceof Structure && !(callbackArgs[j] instanceof Structure.ByValue))
/*     */         {
/* 601 */           ((Structure)callbackArgs[j]).autoWrite();
/*     */         }
/*     */       } 
/*     */       
/* 605 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public Object callback(Object[] args) {
/*     */       try {
/* 616 */         return invokeCallback(args);
/*     */       }
/* 618 */       catch (Throwable t) {
/* 619 */         Native.getCallbackExceptionHandler().uncaughtException(getCallback(), t);
/* 620 */         return null;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private Object convertArgument(Object value, Class<?> dstType) {
/* 628 */       if (value instanceof Pointer) {
/* 629 */         if (dstType == String.class) {
/* 630 */           value = ((Pointer)value).getString(0L, this.encoding);
/*     */         }
/* 632 */         else if (dstType == WString.class) {
/* 633 */           value = new WString(((Pointer)value).getWideString(0L));
/*     */         }
/* 635 */         else if (dstType == String[].class) {
/* 636 */           value = ((Pointer)value).getStringArray(0L, this.encoding);
/*     */         }
/* 638 */         else if (dstType == WString[].class) {
/* 639 */           value = ((Pointer)value).getWideStringArray(0L);
/*     */         }
/* 641 */         else if (Callback.class.isAssignableFrom(dstType)) {
/* 642 */           value = CallbackReference.getCallback(dstType, (Pointer)value);
/*     */         }
/* 644 */         else if (Structure.class.isAssignableFrom(dstType)) {
/*     */ 
/*     */           
/* 647 */           if (Structure.ByValue.class.isAssignableFrom(dstType)) {
/* 648 */             Structure s = (Structure)Structure.newInstance(dstType);
/* 649 */             byte[] buf = new byte[s.size()];
/* 650 */             ((Pointer)value).read(0L, buf, 0, buf.length);
/* 651 */             s.getPointer().write(0L, buf, 0, buf.length);
/* 652 */             s.read();
/* 653 */             value = s;
/*     */           } else {
/* 655 */             Structure s = (Structure)Structure.newInstance(dstType, (Pointer)value);
/* 656 */             s.conditionalAutoRead();
/* 657 */             value = s;
/*     */           }
/*     */         
/*     */         } 
/* 661 */       } else if ((boolean.class == dstType || Boolean.class == dstType) && value instanceof Number) {
/*     */         
/* 663 */         value = Function.valueOf((((Number)value).intValue() != 0));
/*     */       } 
/* 665 */       return value;
/*     */     }
/*     */     
/*     */     private Object convertResult(Object value) {
/* 669 */       if (this.toNative != null) {
/* 670 */         value = this.toNative.toNative(value, new CallbackResultContext(this.callbackMethod));
/*     */       }
/* 672 */       if (value == null) {
/* 673 */         return null;
/*     */       }
/*     */       
/* 676 */       Class<?> cls = value.getClass();
/* 677 */       if (Structure.class.isAssignableFrom(cls)) {
/* 678 */         if (Structure.ByValue.class.isAssignableFrom(cls)) {
/* 679 */           return value;
/*     */         }
/* 681 */         return ((Structure)value).getPointer();
/* 682 */       }  if (cls == boolean.class || cls == Boolean.class) {
/* 683 */         return Boolean.TRUE.equals(value) ? Function.INTEGER_TRUE : Function.INTEGER_FALSE;
/*     */       }
/* 685 */       if (cls == String.class || cls == WString.class)
/* 686 */         return CallbackReference.getNativeString(value, (cls == WString.class)); 
/* 687 */       if (cls == String[].class || cls == WString[].class) {
/* 688 */         StringArray sa = (cls == String[].class) ? new StringArray((String[])value, this.encoding) : new StringArray((WString[])value);
/*     */ 
/*     */ 
/*     */         
/* 692 */         CallbackReference.allocations.put(value, sa);
/* 693 */         return sa;
/* 694 */       }  if (Callback.class.isAssignableFrom(cls)) {
/* 695 */         return CallbackReference.getFunctionPointer((Callback)value);
/*     */       }
/* 697 */       return value;
/*     */     }
/*     */     
/*     */     public Class<?>[] getParameterTypes() {
/* 701 */       return this.callbackMethod.getParameterTypes();
/*     */     }
/*     */     
/*     */     public Class<?> getReturnType() {
/* 705 */       return this.callbackMethod.getReturnType();
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class NativeFunctionHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     private final Function function;
/*     */     
/*     */     private final Map<String, ?> options;
/*     */     
/*     */     public NativeFunctionHandler(Pointer address, int callingConvention, Map<String, ?> options) {
/* 718 */       this.options = options;
/* 719 */       this.function = new Function(address, callingConvention, (String)options.get("string-encoding"));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 725 */       if (Library.Handler.OBJECT_TOSTRING.equals(method)) {
/* 726 */         String str = "Proxy interface to " + this.function;
/* 727 */         Method m = (Method)this.options.get("invoking-method");
/* 728 */         Class<?> cls = CallbackReference.findCallbackClass(m.getDeclaringClass());
/* 729 */         str = str + " (" + cls.getName() + ")";
/*     */         
/* 731 */         return str;
/* 732 */       }  if (Library.Handler.OBJECT_HASHCODE.equals(method))
/* 733 */         return Integer.valueOf(hashCode()); 
/* 734 */       if (Library.Handler.OBJECT_EQUALS.equals(method)) {
/* 735 */         Object o = args[0];
/* 736 */         if (o != null && Proxy.isProxyClass(o.getClass())) {
/* 737 */           return Function.valueOf((Proxy.getInvocationHandler(o) == this));
/*     */         }
/* 739 */         return Boolean.FALSE;
/*     */       } 
/* 741 */       if (Function.isVarArgs(method)) {
/* 742 */         args = Function.concatenateVarArgs(args);
/*     */       }
/* 744 */       return this.function.invoke(method.getReturnType(), args, this.options);
/*     */     }
/*     */     
/*     */     public Pointer getPointer() {
/* 748 */       return this.function;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static boolean isAllowableNativeType(Class<?> cls) {
/* 756 */     return (cls == void.class || cls == Void.class || cls == boolean.class || cls == Boolean.class || cls == byte.class || cls == Byte.class || cls == short.class || cls == Short.class || cls == char.class || cls == Character.class || cls == int.class || cls == Integer.class || cls == long.class || cls == Long.class || cls == float.class || cls == Float.class || cls == double.class || cls == Double.class || (Structure.ByValue.class
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 765 */       .isAssignableFrom(cls) && Structure.class
/* 766 */       .isAssignableFrom(cls)) || Pointer.class
/* 767 */       .isAssignableFrom(cls));
/*     */   }
/*     */   
/*     */   private static Pointer getNativeString(Object value, boolean wide) {
/* 771 */     if (value != null) {
/* 772 */       NativeString ns = new NativeString(value.toString(), wide);
/*     */       
/* 774 */       allocations.put(value, ns);
/* 775 */       return ns.getPointer();
/*     */     } 
/* 777 */     return null;
/*     */   }
/*     */   
/*     */   private static final class CallbackReferenceDisposer
/*     */     implements Runnable {
/*     */     private Pointer cbstruct;
/*     */     
/*     */     public CallbackReferenceDisposer(Pointer cbstruct) {
/* 785 */       this.cbstruct = cbstruct;
/*     */     }
/*     */     
/*     */     public synchronized void run() {
/* 789 */       if (this.cbstruct != null)
/*     */         try {
/* 791 */           Native.freeNativeCallback(this.cbstruct.peer);
/*     */         } finally {
/* 793 */           CallbackReference.allocatedMemory.remove(Long.valueOf(this.cbstruct.peer));
/* 794 */           this.cbstruct.peer = 0L;
/* 795 */           this.cbstruct = null;
/*     */         }  
/*     */     }
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/com/sun/jna/CallbackReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */