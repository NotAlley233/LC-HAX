/*     */ package wtf.tatp.meowtils.gui.values;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Locale;
/*     */ import wtf.tatp.meowtils.config.cfg;
/*     */ 
/*     */ 
/*     */ public class NumberValue
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
/*     */   
/*     */   public NumberValue(String name, double min, double max, double increment, String valueType, String fieldName, Class<?> targetType) {
/*  21 */     this.name = name;
/*  22 */     this.min = min;
/*  23 */     this.max = max;
/*  24 */     this.increment = increment;
/*  25 */     this.valueType = valueType;
/*  26 */     this.targetType = targetType;
/*     */     
/*     */     try {
/*  29 */       this.configInstance = cfg.v;
/*  30 */       this.configField = this.configInstance.getClass().getField(fieldName);
/*  31 */       this.configField.setAccessible(true);
/*  32 */     } catch (Exception e) {
/*  33 */       throw new RuntimeException("Failed to link config field: " + fieldName, e);
/*     */     } 
/*     */     
/*  36 */     syncFromConfig();
/*     */   }
/*     */   
/*     */   public String getName() {
/*  40 */     return this.name;
/*     */   }
/*     */   
/*     */   public double get() {
/*  44 */     return this.value;
/*     */   }
/*     */   
/*     */   public double getMin() {
/*  48 */     return this.min;
/*     */   }
/*     */   
/*     */   public double getMax() {
/*  52 */     return this.max;
/*     */   }
/*     */   
/*     */   public double getIncrement() {
/*  56 */     return this.increment;
/*     */   }
/*     */   public String getValueType() {
/*  59 */     return this.valueType;
/*     */   }
/*     */   public void set(double newValue) {
/*  62 */     this.value = snap(clamp(newValue, this.min, this.max));
/*  63 */     syncToConfig();
/*     */   }
/*     */   
/*     */   public void syncFromConfig() {
/*     */     try {
/*  68 */       Object fieldValue = this.configField.get(this.configInstance);
/*  69 */       if (fieldValue instanceof Number) {
/*  70 */         this.value = snap(((Number)fieldValue).doubleValue());
/*     */       }
/*  72 */     } catch (Exception e) {
/*  73 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void syncToConfig() {
/*     */     try {
/*  79 */       Object castedValue = castValue(this.value, this.targetType);
/*  80 */       this.configField.set(this.configInstance, castedValue);
/*  81 */       cfg.save();
/*  82 */     } catch (Exception e) {
/*  83 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */   
/*     */   private Object castValue(double value, Class<?> type) {
/*  88 */     if (type == int.class || type == Integer.class)
/*  89 */       return Integer.valueOf((int)value); 
/*  90 */     if (type == float.class || type == Float.class)
/*  91 */       return Float.valueOf((float)value); 
/*  92 */     if (type == long.class || type == Long.class)
/*  93 */       return Long.valueOf((long)value); 
/*  94 */     if (type == double.class || type == Double.class) {
/*  95 */       return Double.valueOf(value);
/*     */     }
/*  97 */     throw new IllegalArgumentException("Unsupported type: " + type);
/*     */   }
/*     */   
/*     */   private double clamp(double v, double min, double max) {
/* 101 */     return Math.max(min, Math.min(max, v));
/*     */   }
/*     */   
/*     */   private double snap(double v) {
/* 105 */     double steps = Math.round((v - this.min) / this.increment);
/* 106 */     double snapped = this.min + steps * this.increment;
/*     */ 
/*     */     
/* 109 */     int decimals = Math.max(0, getDecimalPlaces(this.increment));
/* 110 */     double scale = Math.pow(10.0D, decimals);
/* 111 */     return Math.round(snapped * scale) / scale;
/*     */   }
/*     */   private int getDecimalPlaces(double value) {
/* 114 */     String text = Double.toString(value);
/* 115 */     int index = text.indexOf('.');
/* 116 */     return (index < 0) ? 0 : (text.length() - index - 1);
/*     */   }
/*     */   public String getFormattedValue() {
/* 119 */     if (this.targetType == int.class || this.targetType == Integer.class || this.value == Math.floor(this.value)) {
/* 120 */       return String.valueOf((int)this.value);
/*     */     }
/*     */     
/* 123 */     int decimals = 0;
/* 124 */     double inc = this.increment;
/* 125 */     while (inc < 1.0D && decimals < 6) {
/* 126 */       inc *= 10.0D;
/* 127 */       decimals++;
/*     */     } 
/*     */     
/* 130 */     String raw = String.format(Locale.US, "%." + decimals + "f", new Object[] { Double.valueOf(this.value) });
/* 131 */     raw = raw.replaceAll("0*$", "").replaceAll("\\.$", "");
/*     */     
/* 133 */     return raw;
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/gui/values/NumberValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */