package com.example.mod.module.modules.skywars;

import com.example.mod.event.ChatReceiveDispatcher;
import com.example.mod.event.ChatReceiveEvent;
import com.example.mod.module.BaseModule;
import com.example.mod.module.Category;
import com.example.mod.module.Tickable;
import com.example.mod.util.GamemodeUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrengthESP extends BaseModule implements Tickable {
    private static final String[] KILL_PHRASES = new String[] {
            "was killed by",
            "was thrown into the void by",
            "was thrown off a cliff by",
            "was shot by",
            "was crushed into moon dust by",
            "was sent the wrong way by",
            "was blasted to the moon by",
            "was hit by an asteroid from",
            "got rekt by",
            "took the L to",
            "got dabbed on by",
            "got bamboozled by",
            "was bit by",
            "got WOOF'D by",
            "was growled off an edge by",
            "was thrown a frisbee by",
            "was crusaded by the knight",
            "was jousted by",
            "was catapulted by",
            "was shot to the knee by",
            "was socked by",
            "was KO'd by",
            "took an uppercut from",
            "was sent into a daze by",
            "was mushed by",
            "was peeled by",
            "slipped on",
            "got banana pistol'd by",
            "was exterminated by",
            "was scared off an edge by",
            "was squashed by",
            "was tranquilized by",
            "was glazed in BBQ sauce by",
            "slipped in BBQ sauce off the edge spilled by",
            "was not spicy enough for",
            "was thrown chili powder at by",
            "was deleted by",
            "was ALT+F4'd by",
            "was crashed by",
            "was rm -rf by",
            "was turned into space dust by",
            "was sent into orbit by",
            "was retrograded by",
            "be sent to Davy Jones' locker by",
            "be cannonballed to death by",
            "be voodooed by",
            "be shot and killed by",
            "was given the cold shoulder by",
            "was out of the league of",
            "heart was broken by",
            "was struck with Cupid's arrow by",
            "was filled full of lead by",
            "met their end by",
            "lost a drinking contest with",
            "lost the draw to",
            "was struck down by",
            "was turned to dust by",
            "was turned to ash by",
            "was melted by",
            "was put on the naughty list by",
            "was pushed down the chimney by",
            "was traded in for milk and cookies by",
            "was turned to gingerbread by",
            "was ripped to shreds by",
            "was charged by",
            "was ripped and thrown by",
            "was pounced on by",
            "was smothered in holiday cheer by",
            "was banished into the ether by",
            "was pushed by",
            "was sniped by a missile of festivity by",
            "was wrapped up by",
            "was tied into a bow by",
            "tripped over a present placed by",
            "was glued up by",
            "was whacked with a party balloon by",
            "was popped into the void by",
            "was launched like a firework by",
            "was shot with a roman candle by",
            "was painted pretty by",
            "was flipped off the edge by",
            "was deviled by",
            "was made sunny side up by",
            "was locked outside during a snow storm by",
            "was shoved down an icy slope by",
            "was made into a snowman by",
            "was hit with a snowball from",
            "was backstabbed by",
            "was pushed into the abyss by",
            "was thrown into a ravine by",
            "was brutally shot by",
            "was trampled by",
            "was back kicked into the void by",
            "was headbutted off a cliff by",
            "was impaled from a distance by",
            "was buzzed to death by",
            "was bzzz'd off the edge by",
            "was stung by",
            "was startled from a distance by",
            "was oinked by",
            "slipped into void for",
            "was distracted by a piglet from",
            "got attacked by a carrot from",
            "was chewed up by",
            "was squeaked off the edge by",
            "was distracted by a rat dragging pizza from",
            "was squeaked from a distance by",
            "of",
            "died in close combat to",
            "fought to the edge with",
            "stumbled off a ledge with help by",
            "fell to the great marksmanship of"
    };
    private static final Pattern KILL_PATTERN = createKillPattern();
    private static volatile StrengthESP instance;
    private final Map<UUID, Long> highlightedPlayers = new ConcurrentHashMap<>();
    private final Consumer<ChatReceiveEvent> chatListener = this::onChatReceived;
    private int durationMs = 5000;
    private int red = 255;
    private int green = 0;
    private int blue = 0;
    private int alpha = 255;

    public StrengthESP() {
        super("strengthesp", "Highlights recent killers in SkyWars.", Category.RENDER, false);
        instance = this;
    }

    public int getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(int durationMs) {
        this.durationMs = Math.max(1000, durationMs);
    }

    public static StrengthESP getInstance() {
        return instance;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = clampColor(red);
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = clampColor(green);
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = clampColor(blue);
    }

    public int getAlpha() {
        return alpha;
    }

    public void setAlpha(int alpha) {
        this.alpha = clampColor(alpha);
    }

    @Override
    protected void onEnable() {
        ChatReceiveDispatcher.subscribe(chatListener);
    }

    @Override
    protected void onDisable() {
        ChatReceiveDispatcher.unsubscribe(chatListener);
        clear();
    }

    @Override
    public void onTick() {
        if (!GamemodeUtil.isSkywars()) {
            clear();
            return;
        }
        pruneExpired(System.currentTimeMillis());
    }

    private void onChatReceived(ChatReceiveEvent event) {
        if (!enabled() || !GamemodeUtil.isSkywars()) return;
        String message = event.getMessage();
        if (message == null || message.isEmpty()) return;
        Matcher matcher = KILL_PATTERN.matcher(message);
        if (!matcher.find()) return;
        String killerName = matcher.group(1);
        if (killerName == null || killerName.isEmpty()) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.theWorld == null || mc.thePlayer == null) return;
        EntityPlayer killer = mc.theWorld.getPlayerEntityByName(killerName);
        if (killer == null || killer == mc.thePlayer) return;
        highlightedPlayers.put(killer.getUniqueID(), System.currentTimeMillis() + durationMs);
    }

    public static boolean shouldHighlight(EntityPlayer player) {
        StrengthESP module = instance;
        if (module == null || player == null || !module.enabled() || !GamemodeUtil.isSkywars()) return false;
        Long expiresAt = module.highlightedPlayers.get(player.getUniqueID());
        if (expiresAt == null) return false;
        if (expiresAt < System.currentTimeMillis()) {
            module.highlightedPlayers.remove(player.getUniqueID());
            return false;
        }
        return true;
    }

    public void clear() {
        highlightedPlayers.clear();
    }

    private void pruneExpired(long now) {
        Iterator<Map.Entry<UUID, Long>> iterator = highlightedPlayers.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<UUID, Long> entry = iterator.next();
            if (entry.getValue() < now) {
                iterator.remove();
            }
        }
    }

    private static Pattern createKillPattern() {
        StringBuilder builder = new StringBuilder("(?i)(?:");
        for (int i = 0; i < KILL_PHRASES.length; i++) {
            if (i > 0) builder.append("|");
            builder.append(Pattern.quote(KILL_PHRASES[i]));
        }
        builder.append(")\\s+([A-Za-z0-9_]{1,16})(?!'s)");
        return Pattern.compile(builder.toString());
    }

    private static int clampColor(int value) {
        return Math.max(0, Math.min(255, value));
    }
}
