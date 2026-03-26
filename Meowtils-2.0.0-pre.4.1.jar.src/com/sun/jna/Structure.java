/*      */ package com.sun.jna;
/*      */ 
/*      */ import java.lang.annotation.Documented;
/*      */ import java.lang.annotation.ElementType;
/*      */ import java.lang.annotation.Retention;
/*      */ import java.lang.annotation.RetentionPolicy;
/*      */ import java.lang.annotation.Target;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.nio.Buffer;
/*      */ import java.util.AbstractCollection;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class Structure
/*      */ {
/*  114 */   private static final Logger LOG = Logger.getLogger(Structure.class.getName());
/*      */ 
/*      */   
/*      */   public static final int ALIGN_DEFAULT = 0;
/*      */ 
/*      */   
/*      */   public static final int ALIGN_NONE = 1;
/*      */   
/*      */   public static final int ALIGN_GNUC = 2;
/*      */   
/*      */   public static final int ALIGN_MSVC = 3;
/*      */   
/*      */   protected static final int CALCULATE_SIZE = -1;
/*      */ 
/*      */   
/*      */   private static class NativeStringTracking
/*      */   {
/*      */     private final Object value;
/*      */     
/*      */     private NativeString peer;
/*      */ 
/*      */     
/*      */     NativeStringTracking(Object lastValue) {
/*  137 */       this.value = lastValue;
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  158 */   static final Map<Class<?>, LayoutInfo> layoutInfo = new WeakHashMap<Class<?>, LayoutInfo>();
/*  159 */   static final Map<Class<?>, List<String>> fieldOrder = new WeakHashMap<Class<?>, List<String>>();
/*      */   
/*      */   private Pointer memory;
/*      */   
/*  163 */   private int size = -1;
/*      */   
/*      */   private int alignType;
/*      */   
/*      */   private String encoding;
/*      */   private int actualAlignType;
/*      */   private int structAlignment;
/*      */   private Map<String, StructField> structFields;
/*  171 */   private final Map<String, NativeStringTracking> nativeStrings = new HashMap<String, NativeStringTracking>(8);
/*      */   
/*      */   private TypeMapper typeMapper;
/*      */   
/*      */   private long typeInfo;
/*      */   
/*      */   private boolean autoRead = true;
/*      */   private boolean autoWrite = true;
/*      */   private Structure[] array;
/*      */   private boolean readCalled;
/*      */   
/*      */   protected Structure() {
/*  183 */     this(0);
/*      */   }
/*      */   
/*      */   protected Structure(TypeMapper mapper) {
/*  187 */     this(null, 0, mapper);
/*      */   }
/*      */   
/*      */   protected Structure(int alignType) {
/*  191 */     this((Pointer)null, alignType);
/*      */   }
/*      */   
/*      */   protected Structure(int alignType, TypeMapper mapper) {
/*  195 */     this(null, alignType, mapper);
/*      */   }
/*      */ 
/*      */   
/*      */   protected Structure(Pointer p) {
/*  200 */     this(p, 0);
/*      */   }
/*      */   
/*      */   protected Structure(Pointer p, int alignType) {
/*  204 */     this(p, alignType, null);
/*      */   }
/*      */   
/*      */   protected Structure(Pointer p, int alignType, TypeMapper mapper) {
/*  208 */     setAlignType(alignType);
/*  209 */     setStringEncoding(Native.getStringEncoding(getClass()));
/*  210 */     initializeTypeMapper(mapper);
/*  211 */     validateFields();
/*  212 */     if (p != null) {
/*  213 */       useMemory(p, 0, true);
/*      */     } else {
/*      */       
/*  216 */       allocateMemory(-1);
/*      */     } 
/*  218 */     initializeFields();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Map<String, StructField> fields() {
/*  229 */     return this.structFields;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   TypeMapper getTypeMapper() {
/*  236 */     return this.typeMapper;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void initializeTypeMapper(TypeMapper mapper) {
/*  246 */     if (mapper == null) {
/*  247 */       mapper = Native.getTypeMapper(getClass());
/*      */     }
/*  249 */     this.typeMapper = mapper;
/*  250 */     layoutChanged();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void layoutChanged() {
/*  257 */     if (this.size != -1) {
/*  258 */       this.size = -1;
/*  259 */       if (this.memory instanceof AutoAllocated) {
/*  260 */         this.memory = null;
/*      */       }
/*      */       
/*  263 */       ensureAllocated();
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setStringEncoding(String encoding) {
/*  272 */     this.encoding = encoding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected String getStringEncoding() {
/*  280 */     return this.encoding;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void setAlignType(int alignType) {
/*  289 */     this.alignType = alignType;
/*  290 */     if (alignType == 0) {
/*  291 */       alignType = Native.getStructureAlignment(getClass());
/*  292 */       if (alignType == 0)
/*  293 */         if (Platform.isWindows()) {
/*  294 */           alignType = 3;
/*      */         } else {
/*  296 */           alignType = 2;
/*      */         }  
/*      */     } 
/*  299 */     this.actualAlignType = alignType;
/*  300 */     layoutChanged();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Memory autoAllocate(int size) {
/*  309 */     return new AutoAllocated(size);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void useMemory(Pointer m) {
/*  319 */     useMemory(m, 0);
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
/*      */   protected void useMemory(Pointer m, int offset) {
/*  331 */     useMemory(m, offset, false);
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
/*      */   void useMemory(Pointer m, int offset, boolean force) {
/*      */     try {
/*  347 */       this.nativeStrings.clear();
/*      */       
/*  349 */       if (this instanceof ByValue && !force) {
/*      */ 
/*      */         
/*  352 */         byte[] buf = new byte[size()];
/*  353 */         m.read(0L, buf, 0, buf.length);
/*  354 */         this.memory.write(0L, buf, 0, buf.length);
/*      */       } else {
/*      */         
/*  357 */         if (this.size == -1) {
/*  358 */           this.size = calculateSize(false);
/*      */         }
/*  360 */         if (this.size != -1) {
/*  361 */           this.memory = m.share(offset, this.size);
/*      */         }
/*      */         else {
/*      */           
/*  365 */           this.memory = m.share(offset);
/*      */         } 
/*      */       } 
/*  368 */       this.array = null;
/*  369 */       this.readCalled = false;
/*      */     }
/*  371 */     catch (IndexOutOfBoundsException e) {
/*  372 */       throw new IllegalArgumentException("Structure exceeds provided memory bounds", e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void ensureAllocated() {
/*  379 */     ensureAllocated(false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void ensureAllocated(boolean avoidFFIType) {
/*  388 */     if (this.memory == null) {
/*  389 */       allocateMemory(avoidFFIType);
/*      */     }
/*  391 */     else if (this.size == -1) {
/*  392 */       this.size = calculateSize(true, avoidFFIType);
/*  393 */       if (!(this.memory instanceof AutoAllocated)) {
/*      */         
/*      */         try {
/*  396 */           this.memory = this.memory.share(0L, this.size);
/*      */         }
/*  398 */         catch (IndexOutOfBoundsException e) {
/*  399 */           throw new IllegalArgumentException("Structure exceeds provided memory bounds", e);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void allocateMemory() {
/*  409 */     allocateMemory(false);
/*      */   }
/*      */   
/*      */   private void allocateMemory(boolean avoidFFIType) {
/*  413 */     allocateMemory(calculateSize(true, avoidFFIType));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void allocateMemory(int size) {
/*  424 */     if (size == -1) {
/*      */       
/*  426 */       size = calculateSize(false);
/*      */     }
/*  428 */     else if (size <= 0) {
/*  429 */       throw new IllegalArgumentException("Structure size must be greater than zero: " + size);
/*      */     } 
/*      */ 
/*      */     
/*  433 */     if (size != -1) {
/*  434 */       if (this.memory == null || this.memory instanceof AutoAllocated)
/*      */       {
/*  436 */         this.memory = autoAllocate(size);
/*      */       }
/*  438 */       this.size = size;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int size() {
/*  446 */     ensureAllocated();
/*  447 */     return this.size;
/*      */   }
/*      */ 
/*      */   
/*      */   public void clear() {
/*  452 */     ensureAllocated();
/*      */     
/*  454 */     this.nativeStrings.clear();
/*  455 */     this.memory.clear(size());
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
/*      */   public Pointer getPointer() {
/*  469 */     ensureAllocated();
/*  470 */     return this.memory;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  479 */   private static final ThreadLocal<Map<Pointer, Structure>> reads = new ThreadLocal<Map<Pointer, Structure>>()
/*      */     {
/*      */       protected synchronized Map<Pointer, Structure> initialValue() {
/*  482 */         return new HashMap<Pointer, Structure>();
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */   
/*  488 */   private static final ThreadLocal<Set<Structure>> busy = new ThreadLocal<Set<Structure>>()
/*      */     {
/*      */       protected synchronized Set<Structure> initialValue() {
/*  491 */         return new Structure.StructureSet();
/*      */       }
/*      */     };
/*      */   
/*      */   static class StructureSet
/*      */     extends AbstractCollection<Structure>
/*      */     implements Set<Structure> {
/*      */     Structure[] elements;
/*      */     private int count;
/*      */     
/*      */     private void ensureCapacity(int size) {
/*  502 */       if (this.elements == null) {
/*  503 */         this.elements = new Structure[size * 3 / 2];
/*      */       }
/*  505 */       else if (this.elements.length < size) {
/*  506 */         Structure[] e = new Structure[size * 3 / 2];
/*  507 */         System.arraycopy(this.elements, 0, e, 0, this.elements.length);
/*  508 */         this.elements = e;
/*      */       } 
/*      */     }
/*      */     public Structure[] getElements() {
/*  512 */       return this.elements;
/*      */     }
/*      */     public int size() {
/*  515 */       return this.count;
/*      */     }
/*      */     public boolean contains(Object o) {
/*  518 */       return (indexOf((Structure)o) != -1);
/*      */     }
/*      */     
/*      */     public boolean add(Structure o) {
/*  522 */       if (!contains(o)) {
/*  523 */         ensureCapacity(this.count + 1);
/*  524 */         this.elements[this.count++] = o;
/*  525 */         return true;
/*      */       } 
/*  527 */       return false;
/*      */     }
/*      */     private int indexOf(Structure s1) {
/*  530 */       for (int i = 0; i < this.count; i++) {
/*  531 */         Structure s2 = this.elements[i];
/*  532 */         if (s1 == s2 || (s1
/*  533 */           .getClass() == s2.getClass() && s1
/*  534 */           .size() == s2.size() && s1
/*  535 */           .getPointer().equals(s2.getPointer()))) {
/*  536 */           return i;
/*      */         }
/*      */       } 
/*  539 */       return -1;
/*      */     }
/*      */     
/*      */     public boolean remove(Object o) {
/*  543 */       int idx = indexOf((Structure)o);
/*  544 */       if (idx != -1) {
/*  545 */         if (--this.count >= 0) {
/*  546 */           this.elements[idx] = this.elements[this.count];
/*  547 */           this.elements[this.count] = null;
/*      */         } 
/*  549 */         return true;
/*      */       } 
/*  551 */       return false;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Iterator<Structure> iterator() {
/*  558 */       Structure[] e = new Structure[this.count];
/*  559 */       if (this.count > 0) {
/*  560 */         System.arraycopy(this.elements, 0, e, 0, this.count);
/*      */       }
/*  562 */       return Arrays.<Structure>asList(e).iterator();
/*      */     }
/*      */   }
/*      */   
/*      */   static Set<Structure> busy() {
/*  567 */     return busy.get();
/*      */   }
/*      */   static Map<Pointer, Structure> reading() {
/*  570 */     return reads.get();
/*      */   }
/*      */ 
/*      */   
/*      */   void conditionalAutoRead() {
/*  575 */     if (!this.readCalled) {
/*  576 */       autoRead();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void read() {
/*  585 */     if (this.memory == PLACEHOLDER_MEMORY) {
/*      */       return;
/*      */     }
/*  588 */     this.readCalled = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  594 */     ensureAllocated();
/*      */ 
/*      */     
/*  597 */     if (!busy().add(this)) {
/*      */       return;
/*      */     }
/*  600 */     if (this instanceof ByReference) {
/*  601 */       reading().put(getPointer(), this);
/*      */     }
/*      */     try {
/*  604 */       for (StructField structField : fields().values()) {
/*  605 */         readField(structField);
/*      */       }
/*      */     } finally {
/*      */       
/*  609 */       busy().remove(this);
/*  610 */       if (this instanceof ByReference && reading().get(getPointer()) == this) {
/*  611 */         reading().remove(getPointer());
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int fieldOffset(String name) {
/*  621 */     ensureAllocated();
/*  622 */     StructField f = fields().get(name);
/*  623 */     if (f == null) {
/*  624 */       throw new IllegalArgumentException("No such field: " + name);
/*      */     }
/*  626 */     return f.offset;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Object readField(String name) {
/*  636 */     ensureAllocated();
/*  637 */     StructField f = fields().get(name);
/*  638 */     if (f == null)
/*  639 */       throw new IllegalArgumentException("No such field: " + name); 
/*  640 */     return readField(f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Object getFieldValue(Field field) {
/*      */     try {
/*  650 */       return field.get(this);
/*      */     }
/*  652 */     catch (Exception e) {
/*  653 */       throw new Error("Exception reading field '" + field.getName() + "' in " + getClass(), e);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   void setFieldValue(Field field, Object value) {
/*  662 */     setFieldValue(field, value, false);
/*      */   }
/*      */ 
/*      */   
/*      */   private void setFieldValue(Field field, Object value, boolean overrideFinal) {
/*      */     try {
/*  668 */       field.set(this, value);
/*      */     }
/*  670 */     catch (IllegalAccessException e) {
/*  671 */       int modifiers = field.getModifiers();
/*  672 */       if (Modifier.isFinal(modifiers)) {
/*  673 */         if (overrideFinal)
/*      */         {
/*      */           
/*  676 */           throw new UnsupportedOperationException("This VM does not support Structures with final fields (field '" + field.getName() + "' within " + getClass() + ")", e);
/*      */         }
/*  678 */         throw new UnsupportedOperationException("Attempt to write to read-only field '" + field.getName() + "' within " + getClass(), e);
/*      */       } 
/*  680 */       throw new Error("Unexpectedly unable to write to field '" + field.getName() + "' within " + getClass(), e);
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
/*      */   static <T extends Structure> T updateStructureByReference(Class<T> type, T s, Pointer address) {
/*  692 */     if (address == null) {
/*  693 */       s = null;
/*      */     
/*      */     }
/*  696 */     else if (s == null || !address.equals(s.getPointer())) {
/*  697 */       Structure s1 = reading().get(address);
/*  698 */       if (s1 != null && type.equals(s1.getClass())) {
/*  699 */         Structure structure = s1;
/*  700 */         structure.autoRead();
/*      */       } else {
/*      */         
/*  703 */         s = newInstance(type, address);
/*  704 */         s.conditionalAutoRead();
/*      */       } 
/*      */     } else {
/*      */       
/*  708 */       s.autoRead();
/*      */     } 
/*      */     
/*  711 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected Object readField(StructField structField) {
/*      */     Object result;
/*  723 */     int offset = structField.offset;
/*      */ 
/*      */     
/*  726 */     Class<?> fieldType = structField.type;
/*  727 */     FromNativeConverter readConverter = structField.readConverter;
/*  728 */     if (readConverter != null) {
/*  729 */       fieldType = readConverter.nativeType();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  738 */     Object currentValue = (Structure.class.isAssignableFrom(fieldType) || Callback.class.isAssignableFrom(fieldType) || (Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(fieldType)) || Pointer.class.isAssignableFrom(fieldType) || NativeMapped.class.isAssignableFrom(fieldType) || fieldType.isArray()) ? getFieldValue(structField.field) : null;
/*      */ 
/*      */     
/*  741 */     if (fieldType == String.class) {
/*  742 */       Pointer p = this.memory.getPointer(offset);
/*  743 */       result = (p == null) ? null : p.getString(0L, this.encoding);
/*      */     } else {
/*      */       
/*  746 */       result = this.memory.getValue(offset, fieldType, currentValue);
/*      */     } 
/*  748 */     if (readConverter != null) {
/*  749 */       result = readConverter.fromNative(result, structField.context);
/*  750 */       if (currentValue != null && currentValue.equals(result)) {
/*  751 */         result = currentValue;
/*      */       }
/*      */     } 
/*      */     
/*  755 */     if (fieldType.equals(String.class) || fieldType
/*  756 */       .equals(WString.class)) {
/*  757 */       if (result != null) {
/*  758 */         NativeStringTracking current = new NativeStringTracking(result);
/*  759 */         NativeStringTracking previous = this.nativeStrings.put(structField.name, current);
/*      */         
/*  761 */         if (previous != null)
/*      */         {
/*  763 */           current.peer = previous.peer;
/*      */         }
/*      */       } else {
/*      */         
/*  767 */         this.nativeStrings.remove(structField.name);
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*  772 */     setFieldValue(structField.field, result, true);
/*  773 */     return result;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void write() {
/*  781 */     if (this.memory == PLACEHOLDER_MEMORY) {
/*      */       return;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  788 */     ensureAllocated();
/*      */ 
/*      */     
/*  791 */     if (this instanceof ByValue) {
/*  792 */       getTypeInfo();
/*      */     }
/*      */ 
/*      */     
/*  796 */     if (!busy().add(this)) {
/*      */       return;
/*      */     }
/*      */     
/*      */     try {
/*  801 */       for (StructField sf : fields().values()) {
/*  802 */         if (!sf.isVolatile) {
/*  803 */           writeField(sf);
/*      */         }
/*      */       } 
/*      */     } finally {
/*      */       
/*  808 */       busy().remove(this);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeField(String name) {
/*  818 */     ensureAllocated();
/*  819 */     StructField f = fields().get(name);
/*  820 */     if (f == null)
/*  821 */       throw new IllegalArgumentException("No such field: " + name); 
/*  822 */     writeField(f);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeField(String name, Object value) {
/*  833 */     ensureAllocated();
/*  834 */     StructField structField = fields().get(name);
/*  835 */     if (structField == null)
/*  836 */       throw new IllegalArgumentException("No such field: " + name); 
/*  837 */     setFieldValue(structField.field, value);
/*  838 */     writeField(structField, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void writeField(StructField structField) {
/*  846 */     if (structField.isReadOnly) {
/*      */       return;
/*      */     }
/*      */     
/*  850 */     Object value = getFieldValue(structField.field);
/*      */     
/*  852 */     writeField(structField, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void writeField(StructField structField, Object value) {
/*  862 */     int offset = structField.offset;
/*      */ 
/*      */     
/*  865 */     Class<?> fieldType = structField.type;
/*  866 */     ToNativeConverter converter = structField.writeConverter;
/*  867 */     if (converter != null) {
/*  868 */       value = converter.toNative(value, new StructureWriteContext(this, structField.field));
/*  869 */       fieldType = converter.nativeType();
/*      */     } 
/*      */ 
/*      */     
/*  873 */     if (String.class == fieldType || WString.class == fieldType)
/*      */     {
/*  875 */       if (value != null) {
/*  876 */         NativeStringTracking current = new NativeStringTracking(value);
/*  877 */         NativeStringTracking previous = this.nativeStrings.put(structField.name, current);
/*      */ 
/*      */ 
/*      */         
/*  881 */         if (previous != null && value.equals(previous.value)) {
/*      */           
/*  883 */           current.peer = previous.peer;
/*      */           
/*      */           return;
/*      */         } 
/*  887 */         boolean wide = (fieldType == WString.class);
/*      */ 
/*      */         
/*  890 */         NativeString nativeString = wide ? new NativeString(value.toString(), true) : new NativeString(value.toString(), this.encoding);
/*      */         
/*  892 */         current.peer = nativeString;
/*  893 */         value = nativeString.getPointer();
/*      */       } else {
/*      */         
/*  896 */         this.nativeStrings.remove(structField.name);
/*      */       } 
/*      */     }
/*      */     
/*      */     try {
/*  901 */       this.memory.setValue(offset, value, fieldType);
/*      */     }
/*  903 */     catch (IllegalArgumentException e) {
/*  904 */       String msg = "Structure field \"" + structField.name + "\" was declared as " + structField.type + ((structField.type == fieldType) ? "" : (" (native type " + fieldType + ")")) + ", which is not supported within a Structure";
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  909 */       throw new IllegalArgumentException(msg, e);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected List<String> getFieldOrder() {
/*  984 */     List<String> fields = new LinkedList<String>();
/*  985 */     for (Class<?> clazz = getClass(); clazz != Structure.class; clazz = clazz.getSuperclass()) {
/*  986 */       FieldOrder order = clazz.<FieldOrder>getAnnotation(FieldOrder.class);
/*  987 */       if (order != null) {
/*  988 */         fields.addAll(0, Arrays.asList(order.value()));
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  993 */     return Collections.unmodifiableList(fields);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void sortFields(List<Field> fields, List<String> names) {
/* 1001 */     for (int i = 0; i < names.size(); i++) {
/* 1002 */       String name = names.get(i);
/* 1003 */       for (int f = 0; f < fields.size(); f++) {
/* 1004 */         Field field = fields.get(f);
/* 1005 */         if (name.equals(field.getName())) {
/* 1006 */           Collections.swap(fields, i, f);
/*      */           break;
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected List<Field> getFieldList() {
/* 1018 */     List<Field> flist = new ArrayList<Field>();
/* 1019 */     Class<?> cls = getClass();
/* 1020 */     for (; !cls.equals(Structure.class); 
/* 1021 */       cls = cls.getSuperclass()) {
/* 1022 */       List<Field> classFields = new ArrayList<Field>();
/* 1023 */       Field[] fields = cls.getDeclaredFields();
/* 1024 */       for (int i = 0; i < fields.length; i++) {
/* 1025 */         int modifiers = fields[i].getModifiers();
/* 1026 */         if (!Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers))
/*      */         {
/*      */           
/* 1029 */           classFields.add(fields[i]); } 
/*      */       } 
/* 1031 */       flist.addAll(0, classFields);
/*      */     } 
/* 1033 */     return flist;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private List<String> fieldOrder() {
/* 1040 */     Class<?> clazz = getClass();
/* 1041 */     synchronized (fieldOrder) {
/* 1042 */       List<String> list = fieldOrder.get(clazz);
/* 1043 */       if (list == null) {
/* 1044 */         list = getFieldOrder();
/* 1045 */         fieldOrder.put(clazz, list);
/*      */       } 
/* 1047 */       return list;
/*      */     } 
/*      */   }
/*      */   
/*      */   public static List<String> createFieldsOrder(List<String> baseFields, String... extraFields) {
/* 1052 */     return createFieldsOrder(baseFields, Arrays.asList(extraFields));
/*      */   }
/*      */   
/*      */   public static List<String> createFieldsOrder(List<String> baseFields, List<String> extraFields) {
/* 1056 */     List<String> fields = new ArrayList<String>(baseFields.size() + extraFields.size());
/* 1057 */     fields.addAll(baseFields);
/* 1058 */     fields.addAll(extraFields);
/* 1059 */     return Collections.unmodifiableList(fields);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> createFieldsOrder(String field) {
/* 1067 */     return Collections.unmodifiableList(Collections.singletonList(field));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static List<String> createFieldsOrder(String... fields) {
/* 1075 */     return Collections.unmodifiableList(Arrays.asList(fields));
/*      */   }
/*      */   
/*      */   private static <T extends Comparable<T>> List<T> sort(Collection<? extends T> c) {
/* 1079 */     List<T> list = new ArrayList<T>(c);
/* 1080 */     Collections.sort(list);
/* 1081 */     return list;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected List<Field> getFields(boolean force) {
/* 1092 */     List<Field> flist = getFieldList();
/* 1093 */     Set<String> names = new HashSet<String>();
/* 1094 */     for (Field f : flist) {
/* 1095 */       names.add(f.getName());
/*      */     }
/*      */     
/* 1098 */     List<String> fieldOrder = fieldOrder();
/* 1099 */     if (fieldOrder.size() != flist.size() && flist.size() > 1) {
/* 1100 */       if (force) {
/* 1101 */         throw new Error("Structure.getFieldOrder() on " + getClass() + (
/* 1102 */             (fieldOrder.size() < flist.size()) ? " does not provide enough" : " provides too many") + " names [" + fieldOrder
/*      */ 
/*      */             
/* 1105 */             .size() + "] (" + 
/*      */             
/* 1107 */             sort(fieldOrder) + ") to match declared fields [" + flist
/* 1108 */             .size() + "] (" + 
/*      */             
/* 1110 */             sort(names) + ")");
/*      */       }
/*      */       
/* 1113 */       return null;
/*      */     } 
/*      */     
/* 1116 */     Set<String> orderedNames = new HashSet<String>(fieldOrder);
/* 1117 */     if (!orderedNames.equals(names)) {
/* 1118 */       throw new Error("Structure.getFieldOrder() on " + getClass() + " returns names (" + 
/*      */           
/* 1120 */           sort(fieldOrder) + ") which do not match declared field names (" + 
/*      */           
/* 1122 */           sort(names) + ")");
/*      */     }
/*      */     
/* 1125 */     sortFields(flist, fieldOrder);
/* 1126 */     return flist;
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
/*      */   protected int calculateSize(boolean force) {
/* 1144 */     return calculateSize(force, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static int size(Class<? extends Structure> type) {
/* 1152 */     return size(type, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static <T extends Structure> int size(Class<T> type, T value) {
/*      */     LayoutInfo info;
/* 1162 */     synchronized (layoutInfo) {
/* 1163 */       info = layoutInfo.get(type);
/*      */     } 
/* 1165 */     int sz = (info != null && !info.variable) ? info.size : -1;
/* 1166 */     if (sz == -1) {
/* 1167 */       if (value == null) {
/* 1168 */         value = newInstance(type, PLACEHOLDER_MEMORY);
/*      */       }
/* 1170 */       sz = value.size();
/*      */     } 
/* 1172 */     return sz;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   int calculateSize(boolean force, boolean avoidFFIType) {
/*      */     LayoutInfo info;
/* 1183 */     int size = -1;
/* 1184 */     Class<?> clazz = getClass();
/*      */     
/* 1186 */     synchronized (layoutInfo) {
/* 1187 */       info = layoutInfo.get(clazz);
/*      */     } 
/* 1189 */     if (info == null || this.alignType != info
/* 1190 */       .alignType || this.typeMapper != info
/* 1191 */       .typeMapper) {
/* 1192 */       info = deriveLayout(force, avoidFFIType);
/*      */     }
/* 1194 */     if (info != null) {
/* 1195 */       this.structAlignment = info.alignment;
/* 1196 */       this.structFields = info.fields;
/*      */       
/* 1198 */       if (!info.variable) {
/* 1199 */         synchronized (layoutInfo) {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1205 */           if (!layoutInfo.containsKey(clazz) || this.alignType != 0 || this.typeMapper != null)
/*      */           {
/*      */             
/* 1208 */             layoutInfo.put(clazz, info);
/*      */           }
/*      */         } 
/*      */       }
/* 1212 */       size = info.size;
/*      */     } 
/* 1214 */     return size;
/*      */   }
/*      */   
/*      */   private static class LayoutInfo
/*      */   {
/*      */     private LayoutInfo() {}
/*      */     
/* 1221 */     private int size = -1;
/* 1222 */     private int alignment = 1;
/* 1223 */     private final Map<String, Structure.StructField> fields = Collections.synchronizedMap(new LinkedHashMap<String, Structure.StructField>());
/* 1224 */     private int alignType = 0;
/*      */     private TypeMapper typeMapper;
/*      */     private boolean variable;
/*      */   }
/*      */   
/*      */   private void validateField(String name, Class<?> type) {
/* 1230 */     if (this.typeMapper != null) {
/* 1231 */       ToNativeConverter toNative = this.typeMapper.getToNativeConverter(type);
/* 1232 */       if (toNative != null) {
/* 1233 */         validateField(name, toNative.nativeType());
/*      */         return;
/*      */       } 
/*      */     } 
/* 1237 */     if (type.isArray()) {
/* 1238 */       validateField(name, type.getComponentType());
/*      */     } else {
/*      */       
/*      */       try {
/* 1242 */         getNativeSize(type);
/*      */       }
/* 1244 */       catch (IllegalArgumentException e) {
/* 1245 */         String msg = "Invalid Structure field in " + getClass() + ", field name '" + name + "' (" + type + "): " + e.getMessage();
/* 1246 */         throw new IllegalArgumentException(msg, e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void validateFields() {
/* 1253 */     List<Field> fields = getFieldList();
/* 1254 */     for (Field f : fields) {
/* 1255 */       validateField(f.getName(), f.getType());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private LayoutInfo deriveLayout(boolean force, boolean avoidFFIType) {
/* 1264 */     int calculatedSize = 0;
/* 1265 */     List<Field> fields = getFields(force);
/* 1266 */     if (fields == null) {
/* 1267 */       return null;
/*      */     }
/*      */     
/* 1270 */     LayoutInfo info = new LayoutInfo();
/* 1271 */     info.alignType = this.alignType;
/* 1272 */     info.typeMapper = this.typeMapper;
/*      */     
/* 1274 */     boolean firstField = true;
/* 1275 */     for (Iterator<Field> i = fields.iterator(); i.hasNext(); firstField = false) {
/* 1276 */       Field field = i.next();
/* 1277 */       int modifiers = field.getModifiers();
/*      */       
/* 1279 */       Class<?> type = field.getType();
/* 1280 */       if (type.isArray()) {
/* 1281 */         info.variable = true;
/*      */       }
/* 1283 */       StructField structField = new StructField();
/* 1284 */       structField.isVolatile = Modifier.isVolatile(modifiers);
/* 1285 */       structField.isReadOnly = Modifier.isFinal(modifiers);
/* 1286 */       if (structField.isReadOnly) {
/* 1287 */         if (!Platform.RO_FIELDS) {
/* 1288 */           throw new IllegalArgumentException("This VM does not support read-only fields (field '" + field
/* 1289 */               .getName() + "' within " + getClass() + ")");
/*      */         }
/*      */ 
/*      */         
/* 1293 */         field.setAccessible(true);
/*      */       } 
/* 1295 */       structField.field = field;
/* 1296 */       structField.name = field.getName();
/* 1297 */       structField.type = type;
/*      */ 
/*      */       
/* 1300 */       if (Callback.class.isAssignableFrom(type) && !type.isInterface()) {
/* 1301 */         throw new IllegalArgumentException("Structure Callback field '" + field
/* 1302 */             .getName() + "' must be an interface");
/*      */       }
/*      */       
/* 1305 */       if (type.isArray() && Structure.class
/* 1306 */         .equals(type.getComponentType())) {
/* 1307 */         String msg = "Nested Structure arrays must use a derived Structure type so that the size of the elements can be determined";
/*      */ 
/*      */         
/* 1310 */         throw new IllegalArgumentException(msg);
/*      */       } 
/*      */       
/* 1313 */       int fieldAlignment = 1;
/* 1314 */       if (Modifier.isPublic(field.getModifiers())) {
/*      */ 
/*      */ 
/*      */         
/* 1318 */         Object value = getFieldValue(structField.field);
/* 1319 */         if (value == null && type.isArray()) {
/* 1320 */           if (force) {
/* 1321 */             throw new IllegalStateException("Array fields must be initialized");
/*      */           }
/*      */           
/* 1324 */           return null;
/*      */         } 
/* 1326 */         Class<?> nativeType = type;
/* 1327 */         if (NativeMapped.class.isAssignableFrom(type)) {
/* 1328 */           NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
/* 1329 */           nativeType = tc.nativeType();
/* 1330 */           structField.writeConverter = tc;
/* 1331 */           structField.readConverter = tc;
/* 1332 */           structField.context = new StructureReadContext(this, field);
/*      */         }
/* 1334 */         else if (this.typeMapper != null) {
/* 1335 */           ToNativeConverter writeConverter = this.typeMapper.getToNativeConverter(type);
/* 1336 */           FromNativeConverter readConverter = this.typeMapper.getFromNativeConverter(type);
/* 1337 */           if (writeConverter != null && readConverter != null) {
/* 1338 */             value = writeConverter.toNative(value, new StructureWriteContext(this, structField.field));
/*      */             
/* 1340 */             nativeType = (value != null) ? value.getClass() : Pointer.class;
/* 1341 */             structField.writeConverter = writeConverter;
/* 1342 */             structField.readConverter = readConverter;
/* 1343 */             structField.context = new StructureReadContext(this, field);
/*      */           }
/* 1345 */           else if (writeConverter != null || readConverter != null) {
/* 1346 */             String msg = "Structures require bidirectional type conversion for " + type;
/* 1347 */             throw new IllegalArgumentException(msg);
/*      */           } 
/*      */         } 
/*      */         
/* 1351 */         if (value == null) {
/* 1352 */           value = initializeField(structField.field, type);
/*      */         }
/*      */         
/*      */         try {
/* 1356 */           structField.size = getNativeSize(nativeType, value);
/* 1357 */           fieldAlignment = getNativeAlignment(nativeType, value, firstField);
/*      */         }
/* 1359 */         catch (IllegalArgumentException e) {
/*      */           
/* 1361 */           if (!force && this.typeMapper == null) {
/* 1362 */             return null;
/*      */           }
/* 1364 */           String msg = "Invalid Structure field in " + getClass() + ", field name '" + structField.name + "' (" + structField.type + "): " + e.getMessage();
/* 1365 */           throw new IllegalArgumentException(msg, e);
/*      */         } 
/*      */ 
/*      */         
/* 1369 */         if (fieldAlignment == 0) {
/* 1370 */           throw new Error("Field alignment is zero for field '" + structField.name + "' within " + getClass());
/*      */         }
/* 1372 */         info.alignment = Math.max(info.alignment, fieldAlignment);
/* 1373 */         if (calculatedSize % fieldAlignment != 0) {
/* 1374 */           calculatedSize += fieldAlignment - calculatedSize % fieldAlignment;
/*      */         }
/* 1376 */         if (this instanceof Union) {
/* 1377 */           structField.offset = 0;
/* 1378 */           calculatedSize = Math.max(calculatedSize, structField.size);
/*      */         } else {
/*      */           
/* 1381 */           structField.offset = calculatedSize;
/* 1382 */           calculatedSize += structField.size;
/*      */         } 
/*      */ 
/*      */         
/* 1386 */         info.fields.put(structField.name, structField);
/*      */       } 
/*      */     } 
/* 1389 */     if (calculatedSize > 0) {
/* 1390 */       int size = addPadding(calculatedSize, info.alignment);
/*      */       
/* 1392 */       if (this instanceof ByValue && !avoidFFIType) {
/* 1393 */         getTypeInfo();
/*      */       }
/* 1395 */       info.size = size;
/* 1396 */       return info;
/*      */     } 
/*      */     
/* 1399 */     throw new IllegalArgumentException("Structure " + getClass() + " has unknown or zero size (ensure all fields are public)");
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
/*      */   private void initializeFields() {
/* 1411 */     List<Field> flist = getFieldList();
/* 1412 */     for (Field f : flist) {
/*      */       try {
/* 1414 */         Object o = f.get(this);
/* 1415 */         if (o == null) {
/* 1416 */           initializeField(f, f.getType());
/*      */         }
/*      */       }
/* 1419 */       catch (Exception e) {
/* 1420 */         throw new Error("Exception reading field '" + f.getName() + "' in " + getClass(), e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private Object initializeField(Field field, Class<?> type) {
/* 1426 */     Object value = null;
/* 1427 */     if (Structure.class.isAssignableFrom(type) && 
/* 1428 */       !ByReference.class.isAssignableFrom(type)) {
/*      */       try {
/* 1430 */         value = newInstance(type, PLACEHOLDER_MEMORY);
/* 1431 */         setFieldValue(field, value);
/*      */       }
/* 1433 */       catch (IllegalArgumentException e) {
/* 1434 */         String msg = "Can't determine size of nested structure";
/* 1435 */         throw new IllegalArgumentException(msg, e);
/*      */       }
/*      */     
/* 1438 */     } else if (NativeMapped.class.isAssignableFrom(type)) {
/* 1439 */       NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
/* 1440 */       value = tc.defaultValue();
/* 1441 */       setFieldValue(field, value);
/*      */     } 
/* 1443 */     return value;
/*      */   }
/*      */   
/*      */   private int addPadding(int calculatedSize) {
/* 1447 */     return addPadding(calculatedSize, this.structAlignment);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private int addPadding(int calculatedSize, int alignment) {
/* 1453 */     if (this.actualAlignType != 1 && 
/* 1454 */       calculatedSize % alignment != 0) {
/* 1455 */       calculatedSize += alignment - calculatedSize % alignment;
/*      */     }
/*      */     
/* 1458 */     return calculatedSize;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getStructAlignment() {
/* 1465 */     if (this.size == -1)
/*      */     {
/* 1467 */       calculateSize(true);
/*      */     }
/* 1469 */     return this.structAlignment;
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
/*      */   protected int getNativeAlignment(Class<?> type, Object value, boolean isFirstElement) {
/* 1483 */     int alignment = 1;
/* 1484 */     if (NativeMapped.class.isAssignableFrom(type)) {
/* 1485 */       NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
/* 1486 */       type = tc.nativeType();
/* 1487 */       value = tc.toNative(value, new ToNativeContext());
/*      */     } 
/* 1489 */     int size = Native.getNativeSize(type, value);
/* 1490 */     if (type.isPrimitive() || Long.class == type || Integer.class == type || Short.class == type || Character.class == type || Byte.class == type || Boolean.class == type || Float.class == type || Double.class == type) {
/*      */ 
/*      */ 
/*      */       
/* 1494 */       alignment = size;
/*      */     }
/* 1496 */     else if ((Pointer.class.isAssignableFrom(type) && !Function.class.isAssignableFrom(type)) || (Platform.HAS_BUFFERS && Buffer.class
/* 1497 */       .isAssignableFrom(type)) || Callback.class
/* 1498 */       .isAssignableFrom(type) || WString.class == type || String.class == type) {
/*      */ 
/*      */       
/* 1501 */       alignment = Native.POINTER_SIZE;
/*      */     }
/* 1503 */     else if (Structure.class.isAssignableFrom(type)) {
/* 1504 */       if (ByReference.class.isAssignableFrom(type)) {
/* 1505 */         alignment = Native.POINTER_SIZE;
/*      */       } else {
/*      */         
/* 1508 */         if (value == null)
/* 1509 */           value = newInstance(type, PLACEHOLDER_MEMORY); 
/* 1510 */         alignment = ((Structure)value).getStructAlignment();
/*      */       }
/*      */     
/* 1513 */     } else if (type.isArray()) {
/* 1514 */       alignment = getNativeAlignment(type.getComponentType(), null, isFirstElement);
/*      */     } else {
/*      */       
/* 1517 */       throw new IllegalArgumentException("Type " + type + " has unknown native alignment");
/*      */     } 
/*      */     
/* 1520 */     if (this.actualAlignType == 1) {
/* 1521 */       alignment = 1;
/*      */     }
/* 1523 */     else if (this.actualAlignType == 3) {
/* 1524 */       alignment = Math.min(8, alignment);
/*      */     }
/* 1526 */     else if (this.actualAlignType == 2) {
/*      */ 
/*      */       
/* 1529 */       if (!isFirstElement || !Platform.isMac() || !Platform.isPPC()) {
/* 1530 */         alignment = Math.min(Native.MAX_ALIGNMENT, alignment);
/*      */       }
/* 1532 */       if (!isFirstElement && Platform.isAIX() && (type == double.class || type == Double.class)) {
/* 1533 */         alignment = 4;
/*      */       }
/*      */     } 
/* 1536 */     return alignment;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1546 */     return toString(Boolean.getBoolean("jna.dump_memory"));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public String toString(boolean debug) {
/* 1555 */     return toString(0, true, debug);
/*      */   }
/*      */   
/*      */   private String format(Class<?> type) {
/* 1559 */     String s = type.getName();
/* 1560 */     int dot = s.lastIndexOf(".");
/* 1561 */     return s.substring(dot + 1);
/*      */   }
/*      */   
/*      */   private String toString(int indent, boolean showContents, boolean dumpMemory) {
/* 1565 */     ensureAllocated();
/* 1566 */     String LS = System.getProperty("line.separator");
/* 1567 */     String name = format(getClass()) + "(" + getPointer() + ")";
/* 1568 */     if (!(getPointer() instanceof Memory)) {
/* 1569 */       name = name + " (" + size() + " bytes)";
/*      */     }
/* 1571 */     String prefix = "";
/* 1572 */     for (int idx = 0; idx < indent; idx++) {
/* 1573 */       prefix = prefix + "  ";
/*      */     }
/* 1575 */     String contents = LS;
/* 1576 */     if (!showContents) {
/* 1577 */       contents = "...}";
/*      */     } else {
/* 1579 */       for (Iterator<StructField> i = fields().values().iterator(); i.hasNext(); ) {
/* 1580 */         StructField sf = i.next();
/* 1581 */         Object value = getFieldValue(sf.field);
/* 1582 */         String type = format(sf.type);
/* 1583 */         String index = "";
/* 1584 */         contents = contents + prefix;
/* 1585 */         if (sf.type.isArray() && value != null) {
/* 1586 */           type = format(sf.type.getComponentType());
/* 1587 */           index = "[" + Array.getLength(value) + "]";
/*      */         } 
/* 1589 */         contents = contents + String.format("  %s %s%s@0x%X", new Object[] { type, sf.name, index, Integer.valueOf(sf.offset) });
/* 1590 */         if (value instanceof Structure) {
/* 1591 */           value = ((Structure)value).toString(indent + 1, !(value instanceof ByReference), dumpMemory);
/*      */         }
/* 1593 */         contents = contents + "=";
/* 1594 */         if (value instanceof Long) {
/* 1595 */           contents = contents + String.format("0x%08X", new Object[] { value });
/*      */         }
/* 1597 */         else if (value instanceof Integer) {
/* 1598 */           contents = contents + String.format("0x%04X", new Object[] { value });
/*      */         }
/* 1600 */         else if (value instanceof Short) {
/* 1601 */           contents = contents + String.format("0x%02X", new Object[] { value });
/*      */         }
/* 1603 */         else if (value instanceof Byte) {
/* 1604 */           contents = contents + String.format("0x%01X", new Object[] { value });
/*      */         } else {
/*      */           
/* 1607 */           contents = contents + String.valueOf(value).trim();
/*      */         } 
/* 1609 */         contents = contents + LS;
/* 1610 */         if (!i.hasNext())
/* 1611 */           contents = contents + prefix + "}"; 
/*      */       } 
/*      */     } 
/* 1614 */     if (indent == 0 && dumpMemory) {
/* 1615 */       int BYTES_PER_ROW = 4;
/* 1616 */       contents = contents + LS + "memory dump" + LS;
/* 1617 */       byte[] buf = getPointer().getByteArray(0L, size());
/* 1618 */       for (int i = 0; i < buf.length; i++) {
/* 1619 */         if (i % 4 == 0) contents = contents + "["; 
/* 1620 */         if (buf[i] >= 0 && buf[i] < 16)
/* 1621 */           contents = contents + "0"; 
/* 1622 */         contents = contents + Integer.toHexString(buf[i] & 0xFF);
/* 1623 */         if (i % 4 == 3 && i < buf.length - 1)
/* 1624 */           contents = contents + "]" + LS; 
/*      */       } 
/* 1626 */       contents = contents + "]";
/*      */     } 
/* 1628 */     return name + " {" + contents;
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
/*      */   public Structure[] toArray(Structure[] array) {
/* 1640 */     ensureAllocated();
/* 1641 */     if (this.memory instanceof AutoAllocated) {
/*      */       
/* 1643 */       Memory m = (Memory)this.memory;
/* 1644 */       int requiredSize = array.length * size();
/* 1645 */       if (m.size() < requiredSize) {
/* 1646 */         useMemory(autoAllocate(requiredSize));
/*      */       }
/*      */     } 
/*      */     
/* 1650 */     array[0] = this;
/* 1651 */     int size = size();
/* 1652 */     for (int i = 1; i < array.length; i++) {
/* 1653 */       array[i] = newInstance(getClass(), this.memory.share((i * size), size));
/* 1654 */       array[i].conditionalAutoRead();
/*      */     } 
/*      */     
/* 1657 */     if (!(this instanceof ByValue))
/*      */     {
/* 1659 */       this.array = array;
/*      */     }
/*      */     
/* 1662 */     return array;
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
/*      */   public Structure[] toArray(int size) {
/* 1675 */     return toArray((Structure[])Array.newInstance(getClass(), size));
/*      */   }
/*      */   
/*      */   private Class<?> baseClass() {
/* 1679 */     if ((this instanceof ByReference || this instanceof ByValue) && Structure.class
/*      */       
/* 1681 */       .isAssignableFrom(getClass().getSuperclass())) {
/* 1682 */       return getClass().getSuperclass();
/*      */     }
/* 1684 */     return getClass();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean dataEquals(Structure s) {
/* 1693 */     return dataEquals(s, false);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean dataEquals(Structure s, boolean clear) {
/* 1703 */     if (clear) {
/* 1704 */       s.getPointer().clear(s.size());
/* 1705 */       s.write();
/* 1706 */       getPointer().clear(size());
/* 1707 */       write();
/*      */     } 
/* 1709 */     byte[] data = s.getPointer().getByteArray(0L, s.size());
/* 1710 */     byte[] ref = getPointer().getByteArray(0L, size());
/* 1711 */     if (data.length == ref.length) {
/* 1712 */       for (int i = 0; i < data.length; i++) {
/* 1713 */         if (data[i] != ref[i]) {
/* 1714 */           return false;
/*      */         }
/*      */       } 
/* 1717 */       return true;
/*      */     } 
/* 1719 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean equals(Object o) {
/* 1727 */     return (o instanceof Structure && o
/* 1728 */       .getClass() == getClass() && ((Structure)o)
/* 1729 */       .getPointer().equals(getPointer()));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1737 */     Pointer p = getPointer();
/* 1738 */     if (p != null) {
/* 1739 */       return getPointer().hashCode();
/*      */     }
/* 1741 */     return getClass().hashCode();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void cacheTypeInfo(Pointer p) {
/* 1748 */     this.typeInfo = p.peer;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   FFIType getFieldTypeInfo(StructField f) {
/* 1756 */     Class<?> type = f.type;
/* 1757 */     Object value = getFieldValue(f.field);
/* 1758 */     if (this.typeMapper != null) {
/* 1759 */       ToNativeConverter nc = this.typeMapper.getToNativeConverter(type);
/* 1760 */       if (nc != null) {
/* 1761 */         type = nc.nativeType();
/* 1762 */         value = nc.toNative(value, new ToNativeContext());
/*      */       } 
/*      */     } 
/* 1765 */     return FFIType.get(value, type);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   Pointer getTypeInfo() {
/* 1772 */     Pointer p = getTypeInfo(this).getPointer();
/* 1773 */     cacheTypeInfo(p);
/* 1774 */     return p;
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
/*      */   public void setAutoSynch(boolean auto) {
/* 1798 */     setAutoRead(auto);
/* 1799 */     setAutoWrite(auto);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoRead(boolean auto) {
/* 1807 */     this.autoRead = auto;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAutoRead() {
/* 1815 */     return this.autoRead;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void setAutoWrite(boolean auto) {
/* 1823 */     this.autoWrite = auto;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean getAutoWrite() {
/* 1831 */     return this.autoWrite;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static FFIType getTypeInfo(Object obj) {
/* 1839 */     return FFIType.get(obj);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T extends Structure> T newInstance(Class<T> type, long init) {
/*      */     try {
/* 1848 */       T s = newInstance(type, (init == 0L) ? PLACEHOLDER_MEMORY : new Pointer(init));
/* 1849 */       if (init != 0L) {
/* 1850 */         s.conditionalAutoRead();
/*      */       }
/* 1852 */       return s;
/*      */     }
/* 1854 */     catch (Throwable e) {
/* 1855 */       LOG.log(Level.WARNING, "JNA: Error creating structure", e);
/* 1856 */       return null;
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
/*      */   public static <T extends Structure> T newInstance(Class<T> type, Pointer init) throws IllegalArgumentException {
/*      */     try {
/* 1869 */       Constructor<T> ctor = getPointerConstructor(type);
/* 1870 */       if (ctor != null) {
/* 1871 */         return ctor.newInstance(new Object[] { init });
/*      */       
/*      */       }
/*      */     }
/* 1875 */     catch (SecurityException securityException) {
/*      */ 
/*      */     
/* 1878 */     } catch (InstantiationException e) {
/* 1879 */       String msg = "Can't instantiate " + type;
/* 1880 */       throw new IllegalArgumentException(msg, e);
/*      */     }
/* 1882 */     catch (IllegalAccessException e) {
/* 1883 */       String msg = "Instantiation of " + type + " (Pointer) not allowed, is it public?";
/* 1884 */       throw new IllegalArgumentException(msg, e);
/*      */     }
/* 1886 */     catch (InvocationTargetException e) {
/* 1887 */       String msg = "Exception thrown while instantiating an instance of " + type;
/* 1888 */       throw new IllegalArgumentException(msg, e);
/*      */     } 
/* 1890 */     T s = newInstance(type);
/* 1891 */     if (init != PLACEHOLDER_MEMORY) {
/* 1892 */       s.useMemory(init);
/*      */     }
/* 1894 */     return s;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T extends Structure> T newInstance(Class<T> type) throws IllegalArgumentException {
/* 1904 */     Structure structure = Klass.<Structure>newInstance(type);
/* 1905 */     if (structure instanceof ByValue) {
/* 1906 */       structure.allocateMemory();
/*      */     }
/* 1908 */     return (T)structure;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <T> Constructor<T> getPointerConstructor(Class<T> type) {
/* 1918 */     for (Constructor<T> constructor : type.getConstructors()) {
/* 1919 */       Class[] parameterTypes = constructor.getParameterTypes();
/* 1920 */       if (parameterTypes.length == 1 && parameterTypes[0].equals(Pointer.class)) {
/* 1921 */         return constructor;
/*      */       }
/*      */     } 
/*      */     
/* 1925 */     return null;
/*      */   }
/*      */   
/*      */   protected static class StructField {
/*      */     public String name;
/*      */     public Class<?> type;
/*      */     public Field field;
/* 1932 */     public int size = -1;
/* 1933 */     public int offset = -1;
/*      */     public boolean isVolatile;
/*      */     public boolean isReadOnly;
/*      */     public FromNativeConverter readConverter;
/*      */     public ToNativeConverter writeConverter;
/*      */     public FromNativeContext context;
/*      */     
/*      */     public String toString() {
/* 1941 */       return this.name + "@" + this.offset + "[" + this.size + "] (" + this.type + ")";
/*      */     }
/*      */   }
/*      */   
/*      */   @FieldOrder({"size", "alignment", "type", "elements"})
/*      */   static class FFIType
/*      */     extends Structure
/*      */   {
/*      */     public static class size_t
/*      */       extends IntegerType {
/*      */       private static final long serialVersionUID = 1L;
/*      */       
/*      */       public size_t() {
/* 1954 */         this(0L); } public size_t(long value) {
/* 1955 */         super(Native.SIZE_T_SIZE, value);
/*      */       }
/*      */     }
/* 1958 */     private static final Map<Class, Map<Integer, FFIType>> typeInfoMap = (Map)new WeakHashMap<Class<?>, Map<Integer, FFIType>>();
/* 1959 */     private static final Map<Class, FFIType> unionHelper = (Map)new WeakHashMap<Class<?>, FFIType>();
/* 1960 */     private static final Map<Pointer, FFIType> ffiTypeInfo = new HashMap<Pointer, FFIType>(); private static final int FFI_TYPE_STRUCT = 13;
/*      */     public size_t size;
/*      */     public short alignment;
/*      */     public short type;
/*      */     public Pointer elements;
/*      */     
/*      */     private static class FFITypes {
/*      */       private static Pointer ffi_type_void;
/*      */       private static Pointer ffi_type_float;
/*      */       private static Pointer ffi_type_double;
/*      */       private static Pointer ffi_type_longdouble;
/*      */       private static Pointer ffi_type_uint8;
/*      */       private static Pointer ffi_type_sint8;
/*      */       private static Pointer ffi_type_uint16;
/*      */       private static Pointer ffi_type_sint16;
/*      */       private static Pointer ffi_type_uint32;
/*      */       private static Pointer ffi_type_sint32;
/*      */       private static Pointer ffi_type_uint64;
/*      */       private static Pointer ffi_type_sint64;
/*      */       private static Pointer ffi_type_pointer; }
/*      */     
/*      */     private static boolean isIntegerType(FFIType type) {
/* 1982 */       Pointer typePointer = type.getPointer();
/* 1983 */       return (typePointer.equals(FFITypes.ffi_type_uint8) || typePointer
/* 1984 */         .equals(FFITypes.ffi_type_sint8) || typePointer
/* 1985 */         .equals(FFITypes.ffi_type_uint16) || typePointer
/* 1986 */         .equals(FFITypes.ffi_type_sint16) || typePointer
/* 1987 */         .equals(FFITypes.ffi_type_uint32) || typePointer
/* 1988 */         .equals(FFITypes.ffi_type_sint32) || typePointer
/* 1989 */         .equals(FFITypes.ffi_type_uint64) || typePointer
/* 1990 */         .equals(FFITypes.ffi_type_sint64) || typePointer
/* 1991 */         .equals(FFITypes.ffi_type_pointer));
/*      */     }
/*      */     
/*      */     private static boolean isFloatType(FFIType type) {
/* 1995 */       Pointer typePointer = type.getPointer();
/* 1996 */       return (typePointer.equals(FFITypes.ffi_type_float) || typePointer
/* 1997 */         .equals(FFITypes.ffi_type_double));
/*      */     }
/*      */     
/*      */     static {
/* 2001 */       if (Native.POINTER_SIZE == 0)
/* 2002 */         throw new Error("Native library not initialized"); 
/* 2003 */       if (FFITypes.ffi_type_void == null)
/* 2004 */         throw new Error("FFI types not initialized"); 
/* 2005 */       ffiTypeInfo.put(FFITypes.ffi_type_void, Structure.newInstance(FFIType.class, FFITypes.ffi_type_void));
/* 2006 */       ffiTypeInfo.put(FFITypes.ffi_type_float, Structure.newInstance(FFIType.class, FFITypes.ffi_type_float));
/* 2007 */       ffiTypeInfo.put(FFITypes.ffi_type_double, Structure.newInstance(FFIType.class, FFITypes.ffi_type_double));
/* 2008 */       ffiTypeInfo.put(FFITypes.ffi_type_longdouble, Structure.newInstance(FFIType.class, FFITypes.ffi_type_longdouble));
/* 2009 */       ffiTypeInfo.put(FFITypes.ffi_type_uint8, Structure.newInstance(FFIType.class, FFITypes.ffi_type_uint8));
/* 2010 */       ffiTypeInfo.put(FFITypes.ffi_type_sint8, Structure.newInstance(FFIType.class, FFITypes.ffi_type_sint8));
/* 2011 */       ffiTypeInfo.put(FFITypes.ffi_type_uint16, Structure.newInstance(FFIType.class, FFITypes.ffi_type_uint16));
/* 2012 */       ffiTypeInfo.put(FFITypes.ffi_type_sint16, Structure.newInstance(FFIType.class, FFITypes.ffi_type_sint16));
/* 2013 */       ffiTypeInfo.put(FFITypes.ffi_type_uint32, Structure.newInstance(FFIType.class, FFITypes.ffi_type_uint32));
/* 2014 */       ffiTypeInfo.put(FFITypes.ffi_type_sint32, Structure.newInstance(FFIType.class, FFITypes.ffi_type_sint32));
/* 2015 */       ffiTypeInfo.put(FFITypes.ffi_type_uint64, Structure.newInstance(FFIType.class, FFITypes.ffi_type_uint64));
/* 2016 */       ffiTypeInfo.put(FFITypes.ffi_type_sint64, Structure.newInstance(FFIType.class, FFITypes.ffi_type_sint64));
/* 2017 */       ffiTypeInfo.put(FFITypes.ffi_type_pointer, Structure.newInstance(FFIType.class, FFITypes.ffi_type_pointer));
/* 2018 */       for (FFIType f : ffiTypeInfo.values()) {
/* 2019 */         f.read();
/*      */       }
/* 2021 */       storeTypeInfo(void.class, ffiTypeInfo.get(FFITypes.ffi_type_void));
/* 2022 */       storeTypeInfo(Void.class, ffiTypeInfo.get(FFITypes.ffi_type_void));
/* 2023 */       storeTypeInfo(float.class, ffiTypeInfo.get(FFITypes.ffi_type_float));
/* 2024 */       storeTypeInfo(Float.class, ffiTypeInfo.get(FFITypes.ffi_type_float));
/* 2025 */       storeTypeInfo(double.class, ffiTypeInfo.get(FFITypes.ffi_type_double));
/* 2026 */       storeTypeInfo(Double.class, ffiTypeInfo.get(FFITypes.ffi_type_double));
/* 2027 */       storeTypeInfo(long.class, ffiTypeInfo.get(FFITypes.ffi_type_sint64));
/* 2028 */       storeTypeInfo(Long.class, ffiTypeInfo.get(FFITypes.ffi_type_sint64));
/* 2029 */       storeTypeInfo(int.class, ffiTypeInfo.get(FFITypes.ffi_type_sint32));
/* 2030 */       storeTypeInfo(Integer.class, ffiTypeInfo.get(FFITypes.ffi_type_sint32));
/* 2031 */       storeTypeInfo(short.class, ffiTypeInfo.get(FFITypes.ffi_type_sint16));
/* 2032 */       storeTypeInfo(Short.class, ffiTypeInfo.get(FFITypes.ffi_type_sint16));
/*      */       
/* 2034 */       FFIType ctype = (Native.WCHAR_SIZE == 2) ? ffiTypeInfo.get(FFITypes.ffi_type_uint16) : ffiTypeInfo.get(FFITypes.ffi_type_uint32);
/* 2035 */       storeTypeInfo(char.class, ctype);
/* 2036 */       storeTypeInfo(Character.class, ctype);
/* 2037 */       storeTypeInfo(byte.class, ffiTypeInfo.get(FFITypes.ffi_type_sint8));
/* 2038 */       storeTypeInfo(Byte.class, ffiTypeInfo.get(FFITypes.ffi_type_sint8));
/* 2039 */       storeTypeInfo(Pointer.class, ffiTypeInfo.get(FFITypes.ffi_type_pointer));
/* 2040 */       storeTypeInfo(String.class, ffiTypeInfo.get(FFITypes.ffi_type_pointer));
/* 2041 */       storeTypeInfo(WString.class, ffiTypeInfo.get(FFITypes.ffi_type_pointer));
/* 2042 */       storeTypeInfo(boolean.class, ffiTypeInfo.get(FFITypes.ffi_type_uint32));
/* 2043 */       storeTypeInfo(Boolean.class, ffiTypeInfo.get(FFITypes.ffi_type_uint32));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public FFIType(FFIType reference) {
/* 2050 */       this.type = 13;
/*      */ 
/*      */ 
/*      */       
/* 2054 */       this.size = reference.size;
/* 2055 */       this.alignment = reference.alignment;
/* 2056 */       this.type = reference.type;
/* 2057 */       this.elements = reference.elements;
/*      */     } public FFIType() {
/*      */       this.type = 13;
/*      */     }
/*      */     public FFIType(Structure ref) {
/*      */       Pointer[] els;
/*      */       this.type = 13;
/* 2064 */       ref.ensureAllocated(true);
/*      */       
/* 2066 */       if (ref instanceof Union) {
/* 2067 */         FFIType unionType = null;
/* 2068 */         int size = 0;
/* 2069 */         boolean hasInteger = false;
/* 2070 */         for (Structure.StructField sf : ref.fields().values()) {
/* 2071 */           FFIType type = ref.getFieldTypeInfo(sf);
/* 2072 */           if (isIntegerType(type)) {
/* 2073 */             hasInteger = true;
/*      */           }
/* 2075 */           if (unionType == null || size < sf.size || (size == sf.size && Structure.class
/*      */ 
/*      */             
/* 2078 */             .isAssignableFrom(sf.type))) {
/* 2079 */             unionType = type;
/* 2080 */             size = sf.size;
/*      */           } 
/*      */         } 
/* 2083 */         if ((Platform.isIntel() && Platform.is64Bit() && !Platform.isWindows()) || 
/* 2084 */           Platform.isARM() || Platform.isLoongArch())
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 2095 */           if (hasInteger && isFloatType(unionType)) {
/* 2096 */             unionType = new FFIType(unionType);
/* 2097 */             if (unionType.size.intValue() == 4) {
/* 2098 */               unionType.type = ((FFIType)ffiTypeInfo.get(FFITypes.ffi_type_uint32)).type;
/* 2099 */             } else if (unionType.size.intValue() == 8) {
/* 2100 */               unionType.type = ((FFIType)ffiTypeInfo.get(FFITypes.ffi_type_uint64)).type;
/*      */             } 
/* 2102 */             unionType.write();
/*      */           } 
/*      */         }
/*      */         
/* 2106 */         els = new Pointer[] { unionType.getPointer(), null };
/*      */ 
/*      */         
/* 2109 */         unionHelper.put(ref.getClass(), unionType);
/*      */       } else {
/*      */         
/* 2112 */         els = new Pointer[ref.fields().size() + 1];
/* 2113 */         int idx = 0;
/* 2114 */         for (Structure.StructField sf : ref.fields().values()) {
/* 2115 */           els[idx++] = ref.getFieldTypeInfo(sf).getPointer();
/*      */         }
/*      */       } 
/* 2118 */       init(els);
/* 2119 */       write();
/*      */     }
/*      */     public FFIType(Object array, Class<?> type) {
/*      */       this.type = 13;
/* 2123 */       int length = Array.getLength(array);
/* 2124 */       Pointer[] els = new Pointer[length + 1];
/* 2125 */       Pointer p = get((Object)null, type.getComponentType()).getPointer();
/* 2126 */       for (int i = 0; i < length; i++) {
/* 2127 */         els[i] = p;
/*      */       }
/* 2129 */       init(els);
/* 2130 */       write();
/*      */     }
/*      */     
/*      */     private void init(Pointer[] els) {
/* 2134 */       this.elements = new Memory((Native.POINTER_SIZE * els.length));
/* 2135 */       this.elements.write(0L, els, 0, els.length);
/* 2136 */       write();
/*      */     }
/*      */ 
/*      */     
/*      */     static FFIType get(Object obj) {
/* 2141 */       if (obj == null)
/* 2142 */         synchronized (typeInfoMap) {
/* 2143 */           return getTypeInfo(Pointer.class, 0);
/*      */         }  
/* 2145 */       if (obj instanceof Class)
/* 2146 */         return get((Object)null, (Class)obj); 
/* 2147 */       return get(obj, obj.getClass());
/*      */     }
/*      */     
/*      */     private static FFIType get(Object obj, Class<?> cls) {
/* 2151 */       TypeMapper mapper = Native.getTypeMapper(cls);
/* 2152 */       if (mapper != null) {
/* 2153 */         ToNativeConverter nc = mapper.getToNativeConverter(cls);
/* 2154 */         if (nc != null) {
/* 2155 */           cls = nc.nativeType();
/*      */         }
/*      */       } 
/* 2158 */       synchronized (typeInfoMap) {
/* 2159 */         FFIType o = getTypeInfo(cls, cls.isArray() ? Array.getLength(obj) : 0);
/* 2160 */         if (o != null) {
/* 2161 */           return o;
/*      */         }
/* 2163 */         if ((Platform.HAS_BUFFERS && Buffer.class.isAssignableFrom(cls)) || Callback.class
/* 2164 */           .isAssignableFrom(cls)) {
/* 2165 */           typeInfoMap.put(cls, typeInfoMap.get(Pointer.class));
/* 2166 */           return (FFIType)((Map)typeInfoMap.get(Pointer.class)).get(Integer.valueOf(0));
/*      */         } 
/* 2168 */         if (Structure.class.isAssignableFrom(cls)) {
/* 2169 */           if (obj == null) obj = newInstance(cls, Structure.PLACEHOLDER_MEMORY); 
/* 2170 */           if (Structure.ByReference.class.isAssignableFrom(cls)) {
/* 2171 */             typeInfoMap.put(cls, typeInfoMap.get(Pointer.class));
/* 2172 */             return (FFIType)((Map)typeInfoMap.get(Pointer.class)).get(Integer.valueOf(0));
/*      */           } 
/* 2174 */           FFIType type = new FFIType((Structure)obj);
/* 2175 */           storeTypeInfo(cls, type);
/* 2176 */           return type;
/*      */         } 
/* 2178 */         if (NativeMapped.class.isAssignableFrom(cls)) {
/* 2179 */           NativeMappedConverter c = NativeMappedConverter.getInstance(cls);
/* 2180 */           return get(c.toNative(obj, new ToNativeContext()), c.nativeType());
/*      */         } 
/* 2182 */         if (cls.isArray()) {
/* 2183 */           FFIType type = new FFIType(obj, cls);
/*      */           
/* 2185 */           storeTypeInfo(cls, Array.getLength(obj), type);
/* 2186 */           return type;
/*      */         } 
/* 2188 */         throw new IllegalArgumentException("Unsupported type " + cls);
/*      */       } 
/*      */     }
/*      */     
/*      */     private static FFIType getTypeInfo(Class clazz, int elementCount) {
/* 2193 */       Map<Integer, FFIType> typeMap = typeInfoMap.get(clazz);
/* 2194 */       if (typeMap != null) {
/* 2195 */         return typeMap.get(Integer.valueOf(elementCount));
/*      */       }
/* 2197 */       return null;
/*      */     }
/*      */ 
/*      */     
/*      */     private static void storeTypeInfo(Class clazz, FFIType type) {
/* 2202 */       storeTypeInfo(clazz, 0, type);
/*      */     }
/*      */     
/*      */     private static void storeTypeInfo(Class clazz, int elementCount, FFIType type) {
/* 2206 */       synchronized (typeInfoMap) {
/* 2207 */         Map<Integer, FFIType> typeMap = typeInfoMap.get(clazz);
/* 2208 */         if (typeMap == null) {
/* 2209 */           typeMap = new HashMap<Integer, FFIType>();
/* 2210 */           typeInfoMap.put(clazz, typeMap);
/*      */         } 
/* 2212 */         typeMap.put(Integer.valueOf(elementCount), type);
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   private static class AutoAllocated extends Memory {
/*      */     public AutoAllocated(int size) {
/* 2219 */       super(size);
/*      */       
/* 2221 */       clear();
/*      */     }
/*      */     
/*      */     public String toString() {
/* 2225 */       return "auto-" + super.toString();
/*      */     }
/*      */   }
/*      */   
/*      */   private static void structureArrayCheck(Structure[] ss) {
/* 2230 */     if (ByReference[].class.isAssignableFrom(ss.getClass())) {
/*      */       return;
/*      */     }
/* 2233 */     Pointer base = ss[0].getPointer();
/* 2234 */     int size = ss[0].size();
/* 2235 */     for (int si = 1; si < ss.length; si++) {
/* 2236 */       if ((ss[si].getPointer()).peer != base.peer + (size * si)) {
/* 2237 */         String msg = "Structure array elements must use contiguous memory (bad backing address at Structure array index " + si + ")";
/*      */         
/* 2239 */         throw new IllegalArgumentException(msg);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void autoRead(Structure[] ss) {
/* 2245 */     structureArrayCheck(ss);
/* 2246 */     if ((ss[0]).array == ss) {
/* 2247 */       ss[0].autoRead();
/*      */     } else {
/*      */       
/* 2250 */       for (int si = 0; si < ss.length; si++) {
/* 2251 */         if (ss[si] != null) {
/* 2252 */           ss[si].autoRead();
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void autoRead() {
/* 2259 */     if (getAutoRead()) {
/* 2260 */       read();
/* 2261 */       if (this.array != null) {
/* 2262 */         for (int i = 1; i < this.array.length; i++) {
/* 2263 */           this.array[i].autoRead();
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public static void autoWrite(Structure[] ss) {
/* 2270 */     structureArrayCheck(ss);
/* 2271 */     if ((ss[0]).array == ss) {
/* 2272 */       ss[0].autoWrite();
/*      */     } else {
/*      */       
/* 2275 */       for (int si = 0; si < ss.length; si++) {
/* 2276 */         if (ss[si] != null) {
/* 2277 */           ss[si].autoWrite();
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public void autoWrite() {
/* 2284 */     if (getAutoWrite()) {
/* 2285 */       write();
/* 2286 */       if (this.array != null) {
/* 2287 */         for (int i = 1; i < this.array.length; i++) {
/* 2288 */           this.array[i].autoWrite();
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
/*      */   protected int getNativeSize(Class<?> nativeType) {
/* 2300 */     return getNativeSize(nativeType, null);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getNativeSize(Class<?> nativeType, Object value) {
/* 2310 */     return Native.getNativeSize(nativeType, value);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/* 2316 */   private static final Pointer PLACEHOLDER_MEMORY = new Pointer(0L) {
/*      */       public Pointer share(long offset, long sz) {
/* 2318 */         return this;
/*      */       }
/*      */     };
/*      */ 
/*      */ 
/*      */   
/*      */   static void validate(Class<? extends Structure> cls) {
/*      */     try {
/* 2326 */       cls.getConstructor(new Class[0]);
/*      */       return;
/* 2328 */     } catch (NoSuchMethodException noSuchMethodException) {
/*      */     
/* 2330 */     } catch (SecurityException securityException) {}
/*      */     
/* 2332 */     throw new IllegalArgumentException("No suitable constructor found for class: " + cls.getName());
/*      */   }
/*      */   
/*      */   @Documented
/*      */   @Retention(RetentionPolicy.RUNTIME)
/*      */   @Target({ElementType.TYPE})
/*      */   public static @interface FieldOrder {
/*      */     String[] value();
/*      */   }
/*      */   
/*      */   public static interface ByReference {}
/*      */   
/*      */   public static interface ByValue {}
/*      */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/com/sun/jna/Structure.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */