/*     */ package wtf.tatp.meowtils.util.stats;
/*     */ 
/*     */ import net.minecraft.util.EnumChatFormatting;
/*     */ 
/*     */ public class BedwarsStarColor
/*     */ {
/*     */   static class LevelFormat {
/*     */     public final boolean boldStar;
/*     */     EnumChatFormatting leftBracketColor;
/*     */     EnumChatFormatting[] digitColors;
/*     */     EnumChatFormatting starColor;
/*     */     String starIcon;
/*     */     EnumChatFormatting rightBracketColor;
/*     */     
/*     */     public LevelFormat(EnumChatFormatting leftBracketColor, EnumChatFormatting[] digitColors, EnumChatFormatting starColor, String starIcon, EnumChatFormatting rightBracketColor, boolean boldStar) {
/*  16 */       this.leftBracketColor = leftBracketColor;
/*  17 */       this.digitColors = digitColors;
/*  18 */       this.starColor = starColor;
/*  19 */       this.starIcon = starIcon;
/*  20 */       this.rightBracketColor = rightBracketColor;
/*  21 */       this.boldStar = boldStar;
/*     */     }
/*     */   }
/*     */   
/*     */   private static LevelFormat getFormatForLevel(int level) {
/*  26 */     if (level < 100) {
/*  27 */       return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[] { EnumChatFormatting.GRAY }, EnumChatFormatting.GRAY, "✫", EnumChatFormatting.GRAY, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  35 */     if (level < 200) {
/*  36 */       return new LevelFormat(EnumChatFormatting.WHITE, new EnumChatFormatting[] { EnumChatFormatting.WHITE }, EnumChatFormatting.WHITE, "✫", EnumChatFormatting.WHITE, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  44 */     if (level < 300) {
/*  45 */       return new LevelFormat(EnumChatFormatting.GOLD, new EnumChatFormatting[] { EnumChatFormatting.GOLD }, EnumChatFormatting.GOLD, "✫", EnumChatFormatting.GOLD, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  53 */     if (level < 400) {
/*  54 */       return new LevelFormat(EnumChatFormatting.AQUA, new EnumChatFormatting[] { EnumChatFormatting.AQUA }, EnumChatFormatting.AQUA, "✫", EnumChatFormatting.AQUA, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  62 */     if (level < 500) {
/*  63 */       return new LevelFormat(EnumChatFormatting.DARK_GREEN, new EnumChatFormatting[] { EnumChatFormatting.DARK_GREEN }, EnumChatFormatting.DARK_GREEN, "✫", EnumChatFormatting.DARK_GREEN, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  71 */     if (level < 600) {
/*  72 */       return new LevelFormat(EnumChatFormatting.DARK_AQUA, new EnumChatFormatting[] { EnumChatFormatting.DARK_AQUA }, EnumChatFormatting.DARK_AQUA, "✫", EnumChatFormatting.DARK_AQUA, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  81 */     if (level < 700) {
/*  82 */       return new LevelFormat(EnumChatFormatting.DARK_RED, new EnumChatFormatting[] { EnumChatFormatting.DARK_RED }, EnumChatFormatting.DARK_RED, "✫", EnumChatFormatting.DARK_RED, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  91 */     if (level < 800) {
/*  92 */       return new LevelFormat(EnumChatFormatting.LIGHT_PURPLE, new EnumChatFormatting[] { EnumChatFormatting.LIGHT_PURPLE }, EnumChatFormatting.LIGHT_PURPLE, "✫", EnumChatFormatting.LIGHT_PURPLE, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 101 */     if (level < 900) {
/* 102 */       return new LevelFormat(EnumChatFormatting.BLUE, new EnumChatFormatting[] { EnumChatFormatting.BLUE }, EnumChatFormatting.BLUE, "✫", EnumChatFormatting.BLUE, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     if (level < 1000) {
/* 112 */       return new LevelFormat(EnumChatFormatting.DARK_PURPLE, new EnumChatFormatting[] { EnumChatFormatting.DARK_PURPLE }, EnumChatFormatting.DARK_PURPLE, "✫", EnumChatFormatting.DARK_PURPLE, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     if (level < 1100) {
/* 122 */       return new LevelFormat(EnumChatFormatting.RED, new EnumChatFormatting[] { EnumChatFormatting.GOLD, EnumChatFormatting.YELLOW, EnumChatFormatting.GREEN, EnumChatFormatting.AQUA }, EnumChatFormatting.LIGHT_PURPLE, "✫", EnumChatFormatting.DARK_PURPLE, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     if (level < 1200) {
/* 132 */       return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[] { EnumChatFormatting.WHITE }, EnumChatFormatting.GRAY, "✪", EnumChatFormatting.GRAY, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     if (level < 1300) {
/* 142 */       return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[] { EnumChatFormatting.YELLOW }, EnumChatFormatting.GOLD, "✪", EnumChatFormatting.GRAY, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 151 */     if (level < 1400) {
/* 152 */       return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[] { EnumChatFormatting.AQUA }, EnumChatFormatting.DARK_AQUA, "✪", EnumChatFormatting.GRAY, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 161 */     if (level < 1500) {
/* 162 */       return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[] { EnumChatFormatting.GREEN }, EnumChatFormatting.DARK_GREEN, "✪", EnumChatFormatting.GRAY, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     if (level < 1600) {
/* 172 */       return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[] { EnumChatFormatting.DARK_AQUA }, EnumChatFormatting.BLUE, "✪", EnumChatFormatting.GRAY, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 181 */     if (level < 1700) {
/* 182 */       return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[] { EnumChatFormatting.RED }, EnumChatFormatting.DARK_RED, "✪", EnumChatFormatting.GRAY, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 191 */     if (level < 1800) {
/* 192 */       return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[] { EnumChatFormatting.LIGHT_PURPLE }, EnumChatFormatting.DARK_PURPLE, "✪", EnumChatFormatting.GRAY, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 201 */     if (level < 1900) {
/* 202 */       return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[] { EnumChatFormatting.BLUE }, EnumChatFormatting.DARK_BLUE, "✪", EnumChatFormatting.GRAY, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 211 */     if (level < 2000) {
/* 212 */       return new LevelFormat(EnumChatFormatting.GRAY, new EnumChatFormatting[] { EnumChatFormatting.DARK_PURPLE }, EnumChatFormatting.DARK_GRAY, "✪", EnumChatFormatting.GRAY, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 221 */     if (level < 2100) {
/* 222 */       return new LevelFormat(EnumChatFormatting.DARK_GRAY, new EnumChatFormatting[] { EnumChatFormatting.GRAY, EnumChatFormatting.WHITE, EnumChatFormatting.WHITE, EnumChatFormatting.GRAY }, EnumChatFormatting.GRAY, "✪", EnumChatFormatting.DARK_GRAY, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 231 */     if (level < 2200) {
/* 232 */       return new LevelFormat(EnumChatFormatting.WHITE, new EnumChatFormatting[] { EnumChatFormatting.WHITE, EnumChatFormatting.YELLOW, EnumChatFormatting.YELLOW, EnumChatFormatting.GOLD }, EnumChatFormatting.GOLD, "⚝", EnumChatFormatting.GOLD, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 241 */     if (level < 2300) {
/* 242 */       return new LevelFormat(EnumChatFormatting.GOLD, new EnumChatFormatting[] { EnumChatFormatting.GOLD, EnumChatFormatting.WHITE, EnumChatFormatting.WHITE, EnumChatFormatting.AQUA }, EnumChatFormatting.DARK_AQUA, "⚝", EnumChatFormatting.DARK_AQUA, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 251 */     if (level < 2400) {
/* 252 */       return new LevelFormat(EnumChatFormatting.DARK_PURPLE, new EnumChatFormatting[] { EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.LIGHT_PURPLE, EnumChatFormatting.LIGHT_PURPLE, EnumChatFormatting.GOLD }, EnumChatFormatting.YELLOW, "⚝", EnumChatFormatting.YELLOW, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 261 */     if (level < 2500) {
/* 262 */       return new LevelFormat(EnumChatFormatting.AQUA, new EnumChatFormatting[] { EnumChatFormatting.AQUA, EnumChatFormatting.WHITE, EnumChatFormatting.WHITE, EnumChatFormatting.GRAY }, EnumChatFormatting.GRAY, "⚝", EnumChatFormatting.DARK_GRAY, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 271 */     if (level < 2600) {
/* 272 */       return new LevelFormat(EnumChatFormatting.WHITE, new EnumChatFormatting[] { EnumChatFormatting.WHITE, EnumChatFormatting.GREEN, EnumChatFormatting.GREEN, EnumChatFormatting.DARK_GREEN }, EnumChatFormatting.DARK_GREEN, "⚝", EnumChatFormatting.DARK_GREEN, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 281 */     if (level < 2700) {
/* 282 */       return new LevelFormat(EnumChatFormatting.DARK_RED, new EnumChatFormatting[] { EnumChatFormatting.DARK_RED, EnumChatFormatting.RED, EnumChatFormatting.RED, EnumChatFormatting.LIGHT_PURPLE }, EnumChatFormatting.LIGHT_PURPLE, "⚝", EnumChatFormatting.DARK_PURPLE, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 291 */     if (level < 2800) {
/* 292 */       return new LevelFormat(EnumChatFormatting.YELLOW, new EnumChatFormatting[] { EnumChatFormatting.YELLOW, EnumChatFormatting.WHITE, EnumChatFormatting.WHITE, EnumChatFormatting.DARK_GRAY }, EnumChatFormatting.DARK_GRAY, "⚝", EnumChatFormatting.DARK_GRAY, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 301 */     if (level < 2900) {
/* 302 */       return new LevelFormat(EnumChatFormatting.GREEN, new EnumChatFormatting[] { EnumChatFormatting.GREEN, EnumChatFormatting.DARK_GREEN, EnumChatFormatting.DARK_GREEN, EnumChatFormatting.GOLD }, EnumChatFormatting.GOLD, "⚝", EnumChatFormatting.YELLOW, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 311 */     if (level < 3000) {
/* 312 */       return new LevelFormat(EnumChatFormatting.AQUA, new EnumChatFormatting[] { EnumChatFormatting.AQUA, EnumChatFormatting.DARK_AQUA, EnumChatFormatting.DARK_AQUA, EnumChatFormatting.BLUE }, EnumChatFormatting.BLUE, "⚝", EnumChatFormatting.DARK_BLUE, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 321 */     if (level < 3100) {
/* 322 */       return new LevelFormat(EnumChatFormatting.YELLOW, new EnumChatFormatting[] { EnumChatFormatting.YELLOW, EnumChatFormatting.GOLD, EnumChatFormatting.GOLD, EnumChatFormatting.RED }, EnumChatFormatting.RED, "⚝", EnumChatFormatting.DARK_RED, true);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 331 */     if (level < 3200) {
/* 332 */       return new LevelFormat(EnumChatFormatting.BLUE, new EnumChatFormatting[] { EnumChatFormatting.BLUE, EnumChatFormatting.DARK_AQUA, EnumChatFormatting.DARK_AQUA, EnumChatFormatting.GOLD }, EnumChatFormatting.GOLD, "✥", EnumChatFormatting.YELLOW, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 341 */     if (level < 3300) {
/* 342 */       return new LevelFormat(EnumChatFormatting.RED, new EnumChatFormatting[] { EnumChatFormatting.DARK_RED, EnumChatFormatting.GRAY, EnumChatFormatting.GRAY, EnumChatFormatting.DARK_RED }, EnumChatFormatting.RED, "✥", EnumChatFormatting.RED, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 351 */     if (level < 3400) {
/* 352 */       return new LevelFormat(EnumChatFormatting.BLUE, new EnumChatFormatting[] { EnumChatFormatting.BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.LIGHT_PURPLE, EnumChatFormatting.RED }, EnumChatFormatting.RED, "✥", EnumChatFormatting.DARK_RED, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 361 */     if (level < 3500) {
/* 362 */       return new LevelFormat(EnumChatFormatting.DARK_GREEN, new EnumChatFormatting[] { EnumChatFormatting.GREEN, EnumChatFormatting.LIGHT_PURPLE, EnumChatFormatting.LIGHT_PURPLE, EnumChatFormatting.DARK_PURPLE }, EnumChatFormatting.DARK_PURPLE, "✥", EnumChatFormatting.DARK_GREEN, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 371 */     if (level < 3600) {
/* 372 */       return new LevelFormat(EnumChatFormatting.RED, new EnumChatFormatting[] { EnumChatFormatting.RED, EnumChatFormatting.DARK_RED, EnumChatFormatting.DARK_RED, EnumChatFormatting.DARK_GREEN }, EnumChatFormatting.GREEN, "✥", EnumChatFormatting.GREEN, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 381 */     if (level < 3700) {
/* 382 */       return new LevelFormat(EnumChatFormatting.GREEN, new EnumChatFormatting[] { EnumChatFormatting.GREEN, EnumChatFormatting.GREEN, EnumChatFormatting.AQUA, EnumChatFormatting.BLUE }, EnumChatFormatting.BLUE, "✥", EnumChatFormatting.DARK_BLUE, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 391 */     if (level < 3800) {
/* 392 */       return new LevelFormat(EnumChatFormatting.DARK_RED, new EnumChatFormatting[] { EnumChatFormatting.DARK_RED, EnumChatFormatting.RED, EnumChatFormatting.RED, EnumChatFormatting.AQUA }, EnumChatFormatting.DARK_AQUA, "✥", EnumChatFormatting.DARK_AQUA, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 401 */     if (level < 3900) {
/* 402 */       return new LevelFormat(EnumChatFormatting.DARK_BLUE, new EnumChatFormatting[] { EnumChatFormatting.DARK_BLUE, EnumChatFormatting.BLUE, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.DARK_PURPLE }, EnumChatFormatting.LIGHT_PURPLE, "✥", EnumChatFormatting.DARK_BLUE, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 411 */     if (level < 4000) {
/* 412 */       return new LevelFormat(EnumChatFormatting.RED, new EnumChatFormatting[] { EnumChatFormatting.RED, EnumChatFormatting.GREEN, EnumChatFormatting.GREEN, EnumChatFormatting.DARK_AQUA }, EnumChatFormatting.BLUE, "✥", EnumChatFormatting.BLUE, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 421 */     if (level < 4100) {
/* 422 */       return new LevelFormat(EnumChatFormatting.DARK_PURPLE, new EnumChatFormatting[] { EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.RED, EnumChatFormatting.RED, EnumChatFormatting.GOLD }, EnumChatFormatting.GOLD, "✥", EnumChatFormatting.YELLOW, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 431 */     if (level < 4200) {
/* 432 */       return new LevelFormat(EnumChatFormatting.YELLOW, new EnumChatFormatting[] { EnumChatFormatting.YELLOW, EnumChatFormatting.GOLD, EnumChatFormatting.RED, EnumChatFormatting.LIGHT_PURPLE }, EnumChatFormatting.LIGHT_PURPLE, "✥", EnumChatFormatting.DARK_PURPLE, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 441 */     if (level < 4300) {
/* 442 */       return new LevelFormat(EnumChatFormatting.DARK_BLUE, new EnumChatFormatting[] { EnumChatFormatting.BLUE, EnumChatFormatting.DARK_AQUA, EnumChatFormatting.AQUA, EnumChatFormatting.WHITE }, EnumChatFormatting.GRAY, "✥", EnumChatFormatting.GRAY, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 451 */     if (level < 4400) {
/* 452 */       return new LevelFormat(EnumChatFormatting.BLACK, new EnumChatFormatting[] { EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.DARK_GRAY, EnumChatFormatting.DARK_GRAY, EnumChatFormatting.DARK_PURPLE }, EnumChatFormatting.DARK_PURPLE, "✥", EnumChatFormatting.BLACK, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 461 */     if (level < 4500) {
/* 462 */       return new LevelFormat(EnumChatFormatting.DARK_GREEN, new EnumChatFormatting[] { EnumChatFormatting.DARK_GREEN, EnumChatFormatting.GREEN, EnumChatFormatting.YELLOW, EnumChatFormatting.GOLD }, EnumChatFormatting.DARK_PURPLE, "✥", EnumChatFormatting.LIGHT_PURPLE, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 471 */     if (level < 4600) {
/* 472 */       return new LevelFormat(EnumChatFormatting.WHITE, new EnumChatFormatting[] { EnumChatFormatting.WHITE, EnumChatFormatting.AQUA, EnumChatFormatting.AQUA, EnumChatFormatting.DARK_AQUA }, EnumChatFormatting.DARK_AQUA, "✥", EnumChatFormatting.DARK_AQUA, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 481 */     if (level < 4700) {
/* 482 */       return new LevelFormat(EnumChatFormatting.DARK_AQUA, new EnumChatFormatting[] { EnumChatFormatting.AQUA, EnumChatFormatting.YELLOW, EnumChatFormatting.YELLOW, EnumChatFormatting.GOLD }, EnumChatFormatting.LIGHT_PURPLE, "✥", EnumChatFormatting.DARK_PURPLE, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 491 */     if (level < 4800) {
/* 492 */       return new LevelFormat(EnumChatFormatting.WHITE, new EnumChatFormatting[] { EnumChatFormatting.DARK_RED, EnumChatFormatting.RED, EnumChatFormatting.RED, EnumChatFormatting.BLUE }, EnumChatFormatting.DARK_BLUE, "✥", EnumChatFormatting.BLUE, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 501 */     if (level < 4900) {
/* 502 */       return new LevelFormat(EnumChatFormatting.DARK_PURPLE, new EnumChatFormatting[] { EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.RED, EnumChatFormatting.GOLD, EnumChatFormatting.YELLOW }, EnumChatFormatting.AQUA, "✥", EnumChatFormatting.DARK_AQUA, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 511 */     if (level < 5000) {
/* 512 */       return new LevelFormat(EnumChatFormatting.DARK_GREEN, new EnumChatFormatting[] { EnumChatFormatting.GREEN, EnumChatFormatting.WHITE, EnumChatFormatting.WHITE, EnumChatFormatting.GREEN }, EnumChatFormatting.GREEN, "✥", EnumChatFormatting.DARK_GREEN, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 521 */     if (level < 5100) {
/* 522 */       return new LevelFormat(EnumChatFormatting.DARK_RED, new EnumChatFormatting[] { EnumChatFormatting.DARK_RED, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.BLUE, EnumChatFormatting.BLUE }, EnumChatFormatting.DARK_BLUE, "✥", EnumChatFormatting.BLACK, false);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 533 */     return new LevelFormat(EnumChatFormatting.DARK_RED, new EnumChatFormatting[] { EnumChatFormatting.DARK_RED, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.BLUE, EnumChatFormatting.BLUE }, EnumChatFormatting.DARK_BLUE, "✥", EnumChatFormatting.BLACK, false);
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
/*     */   public static String getFormattedBedwarsLevel(int level) {
/* 545 */     LevelFormat format = getFormatForLevel(level);
/* 546 */     String levelStr = String.valueOf(level);
/*     */     
/* 548 */     StringBuilder sb = new StringBuilder();
/* 549 */     sb.append(format.leftBracketColor).append("[");
/*     */     
/* 551 */     for (int i = 0; i < levelStr.length(); i++) {
/* 552 */       EnumChatFormatting color = (i < format.digitColors.length) ? format.digitColors[i] : format.digitColors[format.digitColors.length - 1]; sb.append(color).append(levelStr.charAt(i));
/*     */     } 
/*     */     
/* 555 */     if (format.starColor != null) {
/* 556 */       sb.append(format.starColor);
/*     */     }
/* 558 */     if (format.boldStar) {
/* 559 */       sb.append(EnumChatFormatting.BOLD);
/*     */     }
/* 561 */     sb.append(format.starIcon);
/* 562 */     sb.append(EnumChatFormatting.RESET);
/* 563 */     sb.append(format.rightBracketColor).append("]");
/*     */     
/* 565 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/stats/BedwarsStarColor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */