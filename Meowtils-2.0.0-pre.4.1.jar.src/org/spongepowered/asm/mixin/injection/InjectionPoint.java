/*     */ package org.spongepowered.asm.mixin.injection;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.spongepowered.asm.lib.tree.AbstractInsnNode;
/*     */ import org.spongepowered.asm.lib.tree.AnnotationNode;
/*     */ import org.spongepowered.asm.lib.tree.InsnList;
/*     */ import org.spongepowered.asm.lib.tree.MethodNode;
/*     */ import org.spongepowered.asm.mixin.MixinEnvironment;
/*     */ import org.spongepowered.asm.mixin.injection.modify.AfterStoreLocal;
/*     */ import org.spongepowered.asm.mixin.injection.modify.BeforeLoadLocal;
/*     */ import org.spongepowered.asm.mixin.injection.points.AfterInvoke;
/*     */ import org.spongepowered.asm.mixin.injection.points.BeforeConstant;
/*     */ import org.spongepowered.asm.mixin.injection.points.BeforeFieldAccess;
/*     */ import org.spongepowered.asm.mixin.injection.points.BeforeFinalReturn;
/*     */ import org.spongepowered.asm.mixin.injection.points.BeforeInvoke;
/*     */ import org.spongepowered.asm.mixin.injection.points.BeforeNew;
/*     */ import org.spongepowered.asm.mixin.injection.points.BeforeReturn;
/*     */ import org.spongepowered.asm.mixin.injection.points.BeforeStringInvoke;
/*     */ import org.spongepowered.asm.mixin.injection.points.JumpInsnPoint;
/*     */ import org.spongepowered.asm.mixin.injection.points.MethodHead;
/*     */ import org.spongepowered.asm.mixin.injection.struct.InjectionPointData;
/*     */ import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
/*     */ import org.spongepowered.asm.mixin.refmap.IMixinContext;
/*     */ import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
/*     */ import org.spongepowered.asm.util.Annotations;
/*     */ import org.spongepowered.asm.util.Bytecode;
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
/*     */ public abstract class InjectionPoint
/*     */ {
/*     */   public static final int DEFAULT_ALLOWED_SHIFT_BY = 0;
/*     */   public static final int MAX_ALLOWED_SHIFT_BY = 0;
/*     */   
/*     */   @Retention(RetentionPolicy.RUNTIME)
/*     */   @Target({ElementType.TYPE})
/*     */   public static @interface AtCode
/*     */   {
/*     */     String value();
/*     */   }
/*     */   
/*     */   public enum Selector
/*     */   {
/* 112 */     FIRST,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 117 */     LAST,
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 124 */     ONE;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 130 */     public static final Selector DEFAULT = FIRST;
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*     */     
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   enum ShiftByViolationBehaviour
/*     */   {
/* 142 */     IGNORE,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 147 */     WARN,
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 152 */     ERROR;
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
/* 171 */   private static Map<String, Class<? extends InjectionPoint>> types = new HashMap<String, Class<? extends InjectionPoint>>();
/*     */   private final String slice;
/*     */   
/*     */   static {
/* 175 */     register((Class)BeforeFieldAccess.class);
/* 176 */     register((Class)BeforeInvoke.class);
/* 177 */     register((Class)BeforeNew.class);
/* 178 */     register((Class)BeforeReturn.class);
/* 179 */     register((Class)BeforeStringInvoke.class);
/* 180 */     register((Class)JumpInsnPoint.class);
/* 181 */     register((Class)MethodHead.class);
/* 182 */     register((Class)AfterInvoke.class);
/* 183 */     register((Class)BeforeLoadLocal.class);
/* 184 */     register((Class)AfterStoreLocal.class);
/* 185 */     register((Class)BeforeFinalReturn.class);
/* 186 */     register((Class)BeforeConstant.class);
/*     */   }
/*     */ 
/*     */   
/*     */   private final Selector selector;
/*     */   private final String id;
/*     */   
/*     */   protected InjectionPoint() {
/* 194 */     this("", Selector.DEFAULT, null);
/*     */   }
/*     */   
/*     */   protected InjectionPoint(InjectionPointData data) {
/* 198 */     this(data.getSlice(), data.getSelector(), data.getId());
/*     */   }
/*     */   
/*     */   public InjectionPoint(String slice, Selector selector, String id) {
/* 202 */     this.slice = slice;
/* 203 */     this.selector = selector;
/* 204 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getSlice() {
/* 208 */     return this.slice;
/*     */   }
/*     */   
/*     */   public Selector getSelector() {
/* 212 */     return this.selector;
/*     */   }
/*     */   
/*     */   public String getId() {
/* 216 */     return this.id;
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
/*     */   public boolean checkPriority(int targetPriority, int mixinPriority) {
/* 232 */     return (targetPriority < mixinPriority);
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
/*     */   public String toString() {
/* 254 */     return String.format("@At(\"%s\")", new Object[] { getAtCode() });
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
/*     */   protected static AbstractInsnNode nextNode(InsnList insns, AbstractInsnNode insn) {
/* 266 */     int index = insns.indexOf(insn) + 1;
/* 267 */     if (index > 0 && index < insns.size()) {
/* 268 */       return insns.get(index);
/*     */     }
/* 270 */     return insn;
/*     */   }
/*     */ 
/*     */   
/*     */   static abstract class CompositeInjectionPoint
/*     */     extends InjectionPoint
/*     */   {
/*     */     protected final InjectionPoint[] components;
/*     */ 
/*     */     
/*     */     protected CompositeInjectionPoint(InjectionPoint... components) {
/* 281 */       if (components == null || components.length < 2) {
/* 282 */         throw new IllegalArgumentException("Must supply two or more component injection points for composite point!");
/*     */       }
/*     */       
/* 285 */       this.components = components;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 293 */       return "CompositeInjectionPoint(" + getClass().getSimpleName() + ")[" + Joiner.on(',').join((Object[])this.components) + "]";
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class Intersection
/*     */     extends CompositeInjectionPoint
/*     */   {
/*     */     public Intersection(InjectionPoint... points) {
/* 304 */       super(points);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean find(String desc, InsnList insns, Collection<AbstractInsnNode> nodes) {
/* 310 */       boolean found = false;
/*     */       
/* 312 */       ArrayList[] arrayOfArrayList = (ArrayList[])Array.newInstance(ArrayList.class, this.components.length);
/*     */       
/* 314 */       for (int i = 0; i < this.components.length; i++) {
/* 315 */         arrayOfArrayList[i] = new ArrayList();
/* 316 */         this.components[i].find(desc, insns, arrayOfArrayList[i]);
/*     */       } 
/*     */       
/* 319 */       ArrayList<AbstractInsnNode> alpha = arrayOfArrayList[0];
/* 320 */       for (int nodeIndex = 0; nodeIndex < alpha.size(); nodeIndex++) {
/* 321 */         AbstractInsnNode node = alpha.get(nodeIndex);
/* 322 */         boolean in = true;
/*     */         
/* 324 */         for (int b = 1; b < arrayOfArrayList.length && 
/* 325 */           arrayOfArrayList[b].contains(node); b++);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 330 */         if (in) {
/*     */ 
/*     */ 
/*     */           
/* 334 */           nodes.add(node);
/* 335 */           found = true;
/*     */         } 
/*     */       } 
/* 338 */       return found;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static final class Union
/*     */     extends CompositeInjectionPoint
/*     */   {
/*     */     public Union(InjectionPoint... points) {
/* 349 */       super(points);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean find(String desc, InsnList insns, Collection<AbstractInsnNode> nodes) {
/* 354 */       LinkedHashSet<AbstractInsnNode> allNodes = new LinkedHashSet<AbstractInsnNode>();
/*     */       
/* 356 */       for (int i = 0; i < this.components.length; i++) {
/* 357 */         this.components[i].find(desc, insns, allNodes);
/*     */       }
/*     */       
/* 360 */       nodes.addAll(allNodes);
/*     */       
/* 362 */       return (allNodes.size() > 0);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static final class Shift
/*     */     extends InjectionPoint
/*     */   {
/*     */     private final InjectionPoint input;
/*     */     
/*     */     private final int shift;
/*     */ 
/*     */     
/*     */     public Shift(InjectionPoint input, int shift) {
/* 376 */       if (input == null) {
/* 377 */         throw new IllegalArgumentException("Must supply an input injection point for SHIFT");
/*     */       }
/*     */       
/* 380 */       this.input = input;
/* 381 */       this.shift = shift;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public String toString() {
/* 389 */       return "InjectionPoint(" + getClass().getSimpleName() + ")[" + this.input + "]";
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean find(String desc, InsnList insns, Collection<AbstractInsnNode> nodes) {
/* 394 */       List<AbstractInsnNode> list = (nodes instanceof List) ? (List<AbstractInsnNode>)nodes : new ArrayList<AbstractInsnNode>(nodes);
/*     */       
/* 396 */       this.input.find(desc, insns, nodes);
/*     */       
/* 398 */       for (int i = 0; i < list.size(); i++) {
/* 399 */         list.set(i, insns.get(insns.indexOf(list.get(i)) + this.shift));
/*     */       }
/*     */       
/* 402 */       if (nodes != list) {
/* 403 */         nodes.clear();
/* 404 */         nodes.addAll(list);
/*     */       } 
/*     */       
/* 407 */       return (nodes.size() > 0);
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
/*     */   public static InjectionPoint and(InjectionPoint... operands) {
/* 419 */     return new Intersection(operands);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InjectionPoint or(InjectionPoint... operands) {
/* 430 */     return new Union(operands);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InjectionPoint after(InjectionPoint point) {
/* 441 */     return new Shift(point, 1);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static InjectionPoint before(InjectionPoint point) {
/* 452 */     return new Shift(point, -1);
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
/*     */   public static InjectionPoint shift(InjectionPoint point, int count) {
/* 464 */     return new Shift(point, count);
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
/*     */   public static List<InjectionPoint> parse(IInjectionPointContext owner, List<AnnotationNode> ats) {
/* 478 */     return parse(owner.getContext(), owner.getMethod(), owner.getAnnotation(), ats);
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
/*     */   public static List<InjectionPoint> parse(IMixinContext context, MethodNode method, AnnotationNode parent, List<AnnotationNode> ats) {
/* 494 */     ImmutableList.Builder<InjectionPoint> injectionPoints = ImmutableList.builder();
/* 495 */     for (AnnotationNode at : ats) {
/* 496 */       InjectionPoint injectionPoint = parse(context, method, parent, at);
/* 497 */       if (injectionPoint != null) {
/* 498 */         injectionPoints.add(injectionPoint);
/*     */       }
/*     */     } 
/* 501 */     return (List<InjectionPoint>)injectionPoints.build();
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
/*     */   public static InjectionPoint parse(IInjectionPointContext owner, At at) {
/* 514 */     return parse(owner.getContext(), owner.getMethod(), owner.getAnnotation(), at.value(), at.shift(), at.by(), 
/* 515 */         Arrays.asList(at.args()), at.target(), at.slice(), at.ordinal(), at.opcode(), at.id());
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
/*     */   public static InjectionPoint parse(IMixinContext context, MethodNode method, AnnotationNode parent, At at) {
/* 530 */     return parse(context, method, parent, at.value(), at.shift(), at.by(), Arrays.asList(at.args()), at.target(), at.slice(), at
/* 531 */         .ordinal(), at.opcode(), at.id());
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
/*     */   public static InjectionPoint parse(IInjectionPointContext owner, AnnotationNode node) {
/* 545 */     return parse(owner.getContext(), owner.getMethod(), owner.getAnnotation(), node);
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
/*     */   public static InjectionPoint parse(IMixinContext context, MethodNode method, AnnotationNode parent, AnnotationNode node) {
/*     */     ImmutableList immutableList;
/* 561 */     String at = (String)Annotations.getValue(node, "value");
/* 562 */     List<String> args = (List<String>)Annotations.getValue(node, "args");
/* 563 */     String target = (String)Annotations.getValue(node, "target", "");
/* 564 */     String slice = (String)Annotations.getValue(node, "slice", "");
/* 565 */     At.Shift shift = (At.Shift)Annotations.getValue(node, "shift", At.Shift.class, At.Shift.NONE);
/* 566 */     int by = ((Integer)Annotations.getValue(node, "by", Integer.valueOf(0))).intValue();
/* 567 */     int ordinal = ((Integer)Annotations.getValue(node, "ordinal", Integer.valueOf(-1))).intValue();
/* 568 */     int opcode = ((Integer)Annotations.getValue(node, "opcode", Integer.valueOf(0))).intValue();
/* 569 */     String id = (String)Annotations.getValue(node, "id");
/*     */     
/* 571 */     if (args == null) {
/* 572 */       immutableList = ImmutableList.of();
/*     */     }
/*     */     
/* 575 */     return parse(context, method, parent, at, shift, by, (List<String>)immutableList, target, slice, ordinal, opcode, id);
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
/*     */   public static InjectionPoint parse(IMixinContext context, MethodNode method, AnnotationNode parent, String at, At.Shift shift, int by, List<String> args, String target, String slice, int ordinal, int opcode, String id) {
/* 600 */     InjectionPointData data = new InjectionPointData(context, method, parent, at, args, target, slice, ordinal, opcode, id);
/* 601 */     Class<? extends InjectionPoint> ipClass = findClass(context, data);
/* 602 */     InjectionPoint point = create(context, data, ipClass);
/* 603 */     return shift(context, method, parent, point, shift, by);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Class<? extends InjectionPoint> findClass(IMixinContext context, InjectionPointData data) {
/* 608 */     String type = data.getType();
/* 609 */     Class<? extends InjectionPoint> ipClass = types.get(type);
/* 610 */     if (ipClass == null) {
/* 611 */       if (type.matches("^([A-Za-z_][A-Za-z0-9_]*\\.)+[A-Za-z_][A-Za-z0-9_]*$")) {
/*     */         try {
/* 613 */           ipClass = (Class)Class.forName(type);
/* 614 */           types.put(type, ipClass);
/* 615 */         } catch (Exception ex) {
/* 616 */           throw new InvalidInjectionException(context, data + " could not be loaded or is not a valid InjectionPoint", ex);
/*     */         } 
/*     */       } else {
/* 619 */         throw new InvalidInjectionException(context, data + " is not a valid injection point specifier");
/*     */       } 
/*     */     }
/* 622 */     return ipClass;
/*     */   }
/*     */   
/*     */   private static InjectionPoint create(IMixinContext context, InjectionPointData data, Class<? extends InjectionPoint> ipClass) {
/* 626 */     Constructor<? extends InjectionPoint> ipCtor = null;
/*     */     try {
/* 628 */       ipCtor = ipClass.getDeclaredConstructor(new Class[] { InjectionPointData.class });
/* 629 */       ipCtor.setAccessible(true);
/* 630 */     } catch (NoSuchMethodException ex) {
/* 631 */       throw new InvalidInjectionException(context, ipClass.getName() + " must contain a constructor which accepts an InjectionPointData", ex);
/*     */     } 
/*     */     
/* 634 */     InjectionPoint point = null;
/*     */     try {
/* 636 */       point = ipCtor.newInstance(new Object[] { data });
/* 637 */     } catch (Exception ex) {
/* 638 */       throw new InvalidInjectionException(context, "Error whilst instancing injection point " + ipClass.getName() + " for " + data.getAt(), ex);
/*     */     } 
/*     */     
/* 641 */     return point;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static InjectionPoint shift(IMixinContext context, MethodNode method, AnnotationNode parent, InjectionPoint point, At.Shift shift, int by) {
/* 647 */     if (point != null) {
/* 648 */       if (shift == At.Shift.BEFORE)
/* 649 */         return before(point); 
/* 650 */       if (shift == At.Shift.AFTER)
/* 651 */         return after(point); 
/* 652 */       if (shift == At.Shift.BY) {
/* 653 */         validateByValue(context, method, parent, point, by);
/* 654 */         return shift(point, by);
/*     */       } 
/*     */     } 
/*     */     
/* 658 */     return point;
/*     */   }
/*     */   
/*     */   private static void validateByValue(IMixinContext context, MethodNode method, AnnotationNode parent, InjectionPoint point, int by) {
/* 662 */     MixinEnvironment env = context.getMixin().getConfig().getEnvironment();
/* 663 */     ShiftByViolationBehaviour err = (ShiftByViolationBehaviour)env.getOption(MixinEnvironment.Option.SHIFT_BY_VIOLATION_BEHAVIOUR, ShiftByViolationBehaviour.WARN);
/* 664 */     if (err == ShiftByViolationBehaviour.IGNORE) {
/*     */       return;
/*     */     }
/*     */     
/* 668 */     int allowed = 0;
/* 669 */     if (context instanceof MixinTargetContext) {
/* 670 */       allowed = ((MixinTargetContext)context).getMaxShiftByValue();
/*     */     }
/*     */     
/* 673 */     if (by <= allowed) {
/*     */       return;
/*     */     }
/*     */     
/* 677 */     String message = String.format("@%s(%s) Shift.BY=%d on %s::%s exceeds the maximum allowed value %d.", new Object[] { Bytecode.getSimpleName(parent), point, 
/* 678 */           Integer.valueOf(by), context, method.name, Integer.valueOf(allowed) });
/*     */     
/* 680 */     if (err == ShiftByViolationBehaviour.WARN) {
/* 681 */       LogManager.getLogger("mixin").warn("{} Increase the value of maxShiftBy to suppress this warning.", new Object[] { message });
/*     */       
/*     */       return;
/*     */     } 
/* 685 */     throw new InvalidInjectionException(context, message);
/*     */   }
/*     */   
/*     */   protected String getAtCode() {
/* 689 */     AtCode code = getClass().<AtCode>getAnnotation(AtCode.class);
/* 690 */     return (code == null) ? getClass().getName() : code.value();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void register(Class<? extends InjectionPoint> type) {
/* 700 */     AtCode code = type.<AtCode>getAnnotation(AtCode.class);
/* 701 */     if (code == null) {
/* 702 */       throw new IllegalArgumentException("Injection point class " + type + " is not annotated with @AtCode");
/*     */     }
/*     */     
/* 705 */     Class<? extends InjectionPoint> existing = types.get(code.value());
/* 706 */     if (existing != null && !existing.equals(type)) {
/* 707 */       LogManager.getLogger("mixin").debug("Overriding InjectionPoint {} with {} (previously {})", new Object[] { code.value(), type.getName(), existing
/* 708 */             .getName() });
/*     */     }
/*     */     
/* 711 */     types.put(code.value(), type);
/*     */   }
/*     */   
/*     */   public abstract boolean find(String paramString, InsnList paramInsnList, Collection<AbstractInsnNode> paramCollection);
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/org/spongepowered/asm/mixin/injection/InjectionPoint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */