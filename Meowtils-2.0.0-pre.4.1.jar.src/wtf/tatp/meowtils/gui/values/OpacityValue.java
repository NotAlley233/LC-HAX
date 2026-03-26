/*     */ package wtf.tatp.meowtils.gui.values;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Locale;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ import wtf.tatp.meowtils.gui.ColorComponent;
/*     */ 
/*     */ 
/*     */ public class OpacityValue
/*     */ {
/*     */   private final String name;
/*     */   private final double min;
/*     */   private final double max;
/*     */   private final double increment;
/*     */   private final String valueType;
/*     */   private double value;
/*     */   private final Field configField;
/*     */   private final Object configInstance;
/*     */   private final Class<?> targetType;
/*     */   private final ColorComponent link;
/*     */   
/*     */   public OpacityValue(String name, String fieldName, ColorComponent link) {
/*  23 */     this.name = name;
/*  24 */     this.min = 0.0D;
/*  25 */     this.max = 100.0D;
/*  26 */     this.increment = 5.0D;
/*  27 */     this.valueType = "%";
/*  28 */     this.targetType = int.class;
/*  29 */     this.link = link;
/*     */     
/*     */     try {
/*  32 */       this.configInstance = cfg.v;
/*  33 */       this.configField = this.configInstance.getClass().getField(fieldName);
/*  34 */       this.configField.setAccessible(true);
/*  35 */     } catch (Exception e) {
/*  36 */       throw new RuntimeException("Failed to link config field: " + fieldName, e);
/*     */     } 
/*     */     
/*  39 */     syncFromConfig();
/*     */   }
/*     */   
/*     */   public String getName() {
/*  43 */     return this.name;
/*     */   }
/*     */   
/*     */   public double get() {
/*  47 */     return this.value;
/*     */   }
/*     */   
/*     */   public double getMin() {
/*  51 */     return this.min;
/*     */   }
/*     */   
/*     */   public double getMax() {
/*  55 */     return this.max;
/*     */   }
/*     */   
/*     */   public double getIncrement() {
/*  59 */     return this.increment;
/*     */   }
/*     */   public String getValueType() {
/*  62 */     return this.valueType;
/*     */   }
/*     */   public ColorComponent getLink() {
/*  65 */     return this.link;
/*     */   }
/*     */   
/*     */   public void set(double newValue) {
/*  69 */     this.value = snap(clamp(newValue, this.min, this.max));
/*  70 */     syncToConfig();
/*     */   }
/*     */   
/*     */   public void syncFromConfig() {
/*     */     try {
/*  75 */       Object fieldValue = this.configField.get(this.configInstance);
/*  76 */       if (fieldValue instanceof Number) {
/*  77 */         this.value = snap(((Number)fieldValue).doubleValue());
/*     */       }
/*  79 */     } catch (Exception e) {
/*  80 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void syncToConfig() {
/*     */     try {
/*  86 */       Object castedValue = castValue(this.value, this.targetType);
/*  87 */       this.configField.set(this.configInstance, castedValue);
/*  88 */       cfg.save();
/*  89 */     } catch (Exception e) {
/*  90 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private Object castValue(double value, Class<?> type) {
/*  95 */     if (type == int.class || type == Integer.class)
/*  96 */       return Integer.valueOf((int)value); 
/*  97 */     if (type == float.class || type == Float.class)
/*  98 */       return Float.valueOf((float)value); 
/*  99 */     if (type == long.class || type == Long.class)
/* 100 */       return Long.valueOf((long)value); 
/* 101 */     if (type == double.class || type == Double.class) {
/* 102 */       return Double.valueOf(value);
/*     */     }
/* 104 */     throw new IllegalArgumentException("Unsupported type: " + type);
/*     */   }
/*     */   
/*     */   private double clamp(double v, double min, double max) {
/* 108 */     return Math.max(min, Math.min(max, v));
/*     */   }
/*     */   
/*     */   private double snap(double v) {
/* 112 */     double steps = Math.round((v - this.min) / this.increment);
/* 113 */     double snapped = this.min + steps * this.increment;
/*     */ 
/*     */     
/* 116 */     int decimals = Math.max(0, getDecimalPlaces(this.increment));
/* 117 */     double scale = Math.pow(10.0D, decimals);
/* 118 */     return Math.round(snapped * scale) / scale;
/*     */   }
/*     */   private int getDecimalPlaces(double value) {
/* 121 */     String text = Double.toString(value);
/* 122 */     int index = text.indexOf('.');
/* 123 */     return (index < 0) ? 0 : (text.length() - index - 1);
/*     */   }
/*     */   public String getFormattedValue() {
/* 126 */     if (this.targetType == int.class || this.targetType == Integer.class || this.value == Math.floor(this.value)) {
/* 127 */       return String.valueOf((int)this.value);
/*     */     }
/*     */     
/* 130 */     int decimals = 0;
/* 131 */     double inc = this.increment;
/* 132 */     while (inc < 1.0D && decimals < 6) {
/* 133 */       inc *= 10.0D;
/* 134 */       decimals++;
/*     */     } 
/*     */     
/* 137 */     String raw = String.format(Locale.US, "%." + decimals + "f", new Object[] { Double.valueOf(this.value) });
/* 138 */     raw = raw.replaceAll("0*$", "").replaceAll("\\.$", "");
/*     */     
/* 140 */     return raw;
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/values/OpacityValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */