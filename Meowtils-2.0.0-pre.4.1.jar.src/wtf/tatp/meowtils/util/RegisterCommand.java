/*     */ package wtf.tatp.meowtils.util;
/*     */ import net.minecraft.command.ICommand;
/*     */ import net.minecraftforge.client.ClientCommandHandler;
/*     */ import wtf.tatp.meowtils.commands.CapeFolderCommand;
/*     */ import wtf.tatp.meowtils.commands.ColorCodeCommand;
/*     */ import wtf.tatp.meowtils.commands.FakeMessageCommand;
/*     */ import wtf.tatp.meowtils.commands.RequeueCommand;
/*     */ import wtf.tatp.meowtils.commands.SendCommand;
/*     */ import wtf.tatp.meowtils.commands.config.AutoGGFirstMessageCommand;
/*     */ import wtf.tatp.meowtils.commands.config.AutoGLMessageCommand;
/*     */ import wtf.tatp.meowtils.commands.config.AutoTextMessageCommand;
/*     */ import wtf.tatp.meowtils.commands.debug.DebugMessageCommand;
/*     */ import wtf.tatp.meowtils.commands.debug.PlayerInfoCommand;
/*     */ import wtf.tatp.meowtils.commands.meowtils.MeowtilsCommand;
/*     */ import wtf.tatp.meowtils.commands.meowtils.PlayCommands;
/*     */ import wtf.tatp.meowtils.commands.playcommands.playbedwars.FourTwosBedwarsCommand;
/*     */ import wtf.tatp.meowtils.commands.playcommands.playbedwars.FoursBedwarsCommand;
/*     */ import wtf.tatp.meowtils.commands.playcommands.playbedwars.ThreesBedwarsCommand;
/*     */ import wtf.tatp.meowtils.commands.playcommands.playblitz.BlitzSolo;
/*     */ import wtf.tatp.meowtils.commands.playcommands.playblitz.BlitzTeams;
/*     */ import wtf.tatp.meowtils.commands.playcommands.playduels.BridgeDuelsFours;
/*     */ import wtf.tatp.meowtils.commands.playcommands.playduels.BridgeDuelsThrees;
/*     */ import wtf.tatp.meowtils.commands.playcommands.playduels.SkywarsDuels;
/*     */ import wtf.tatp.meowtils.commands.playcommands.playduels.UhcDuels;
/*     */ import wtf.tatp.meowtils.commands.playcommands.playskywars.DoublesNormalSkywarsCommand;
/*     */ import wtf.tatp.meowtils.commands.playcommands.playskywars.SoloNormalSkywarsCommand;
/*     */ import wtf.tatp.meowtils.commands.shortcuts.BlockRemoveCommand;
/*     */ import wtf.tatp.meowtils.commands.shortcuts.ChatCoopCommand;
/*     */ import wtf.tatp.meowtils.commands.shortcuts.ChatPartyCommand;
/*     */ import wtf.tatp.meowtils.commands.shortcuts.PartyDemoteCommand;
/*     */ import wtf.tatp.meowtils.commands.shortcuts.PartySettingsAllinviteCommand;
/*     */ import wtf.tatp.meowtils.commands.shortcuts.ReplayCommand;
/*     */ import wtf.tatp.meowtils.commands.shortcuts.SwapLobbyCommand;
/*     */ import wtf.tatp.meowtils.commands.shortcuts.TipAllCommand;
/*     */ import wtf.tatp.meowtils.commands.shortcuts.statuscommands.StatusAwayCommand;
/*     */ import wtf.tatp.meowtils.commands.shortcuts.statuscommands.StatusBusyCommand;
/*     */ import wtf.tatp.meowtils.commands.shortcuts.statuscommands.StatusOnlineCommand;
/*     */ import wtf.tatp.meowtils.commands.stats.BedwarsStatsCommand;
/*     */ 
/*     */ public class RegisterCommand {
/*     */   public static void registerCommands() {
/*  42 */     ClientCommandHandler handler = ClientCommandHandler.instance;
/*     */     
/*  44 */     handler.func_71560_a((ICommand)new MeowtilsCommand());
/*  45 */     handler.func_71560_a((ICommand)new SolosBedwarsCommand());
/*  46 */     handler.func_71560_a((ICommand)new DoublesBedwarsCommand());
/*  47 */     handler.func_71560_a((ICommand)new ThreesBedwarsCommand());
/*  48 */     handler.func_71560_a((ICommand)new FoursBedwarsCommand());
/*  49 */     handler.func_71560_a((ICommand)new FourTwosBedwarsCommand());
/*  50 */     handler.func_71560_a((ICommand)new SoloNormalSkywarsCommand());
/*  51 */     handler.func_71560_a((ICommand)new SoloInsaneSkywarsCommand());
/*  52 */     handler.func_71560_a((ICommand)new DoublesNormalSkywarsCommand());
/*  53 */     handler.func_71560_a((ICommand)new BlitzSolo());
/*  54 */     handler.func_71560_a((ICommand)new BlitzTeams());
/*  55 */     handler.func_71560_a((ICommand)new BridgeDuelsFours());
/*  56 */     handler.func_71560_a((ICommand)new BridgeDuelsOne());
/*  57 */     handler.func_71560_a((ICommand)new BridgeDuelsThrees());
/*  58 */     handler.func_71560_a((ICommand)new BridgeDuelsTwos());
/*  59 */     handler.func_71560_a((ICommand)new ClassicDuels());
/*  60 */     handler.func_71560_a((ICommand)new SkywarsDuels());
/*  61 */     handler.func_71560_a((ICommand)new SumoDuels());
/*  62 */     handler.func_71560_a((ICommand)new UhcDuels());
/*  63 */     handler.func_71560_a((ICommand)new MegaWallsFaceOff());
/*  64 */     handler.func_71560_a((ICommand)new MegaWallsStandard());
/*  65 */     handler.func_71560_a((ICommand)new TntRun());
/*  66 */     handler.func_71560_a((ICommand)new TntTag());
/*  67 */     handler.func_71560_a((ICommand)new PlayCommands());
/*  68 */     handler.func_71560_a((ICommand)new PartyTransferCommand());
/*  69 */     handler.func_71560_a((ICommand)new PartyKickOfflineCommand());
/*  70 */     handler.func_71560_a((ICommand)new PartyDisbandCommand());
/*  71 */     handler.func_71560_a((ICommand)new BedwarsStatsCommand());
/*  72 */     handler.func_71560_a((ICommand)new AutoGGFirstMessageCommand());
/*  73 */     handler.func_71560_a((ICommand)new AutoGGSecondMessageCommand());
/*  74 */     handler.func_71560_a((ICommand)new AutoGLMessageCommand());
/*  75 */     handler.func_71560_a((ICommand)new ApiKeyCommand());
/*  76 */     handler.func_71560_a((ICommand)new ColorCodeCommand());
/*  77 */     handler.func_71560_a((ICommand)new ShoutCommand());
/*  78 */     handler.func_71560_a((ICommand)new RequeueCommand());
/*  79 */     handler.func_71560_a((ICommand)new MiniNormalSkywarsCommand());
/*  80 */     handler.func_71560_a((ICommand)new BlacklistCommand());
/*  81 */     handler.func_71560_a((ICommand)new UnBlacklistCommand());
/*  82 */     handler.func_71560_a((ICommand)new SafelistCommand());
/*  83 */     handler.func_71560_a((ICommand)new UnSafelistCommand());
/*  84 */     handler.func_71560_a((ICommand)new CustomThemeCommand());
/*  85 */     handler.func_71560_a((ICommand)new SetThemeCommand());
/*  86 */     handler.func_71560_a((ICommand)new DebugMessageCommand());
/*  87 */     handler.func_71560_a((ICommand)new SetFlagMessageColorCommand());
/*  88 */     handler.func_71560_a((ICommand)new CustomFlagMessageCommand());
/*  89 */     handler.func_71560_a((ICommand)new PlanckeCommand());
/*  90 */     handler.func_71560_a((ICommand)new FakeMessageCommand());
/*  91 */     handler.func_71560_a((ICommand)new NamemcCommand());
/*  92 */     handler.func_71560_a((ICommand)new BlockAddCommand());
/*  93 */     handler.func_71560_a((ICommand)new ChatPartyCommand());
/*  94 */     handler.func_71560_a((ICommand)new ChatGuildCommand());
/*  95 */     handler.func_71560_a((ICommand)new ChatAllCommand());
/*  96 */     handler.func_71560_a((ICommand)new ChatCoopCommand());
/*  97 */     handler.func_71560_a((ICommand)new ChatOfficerCommand());
/*  98 */     handler.func_71560_a((ICommand)new PartySettingsAllinviteCommand());
/*  99 */     handler.func_71560_a((ICommand)new PartyPromoteCommand());
/* 100 */     handler.func_71560_a((ICommand)new GuildInfoCommand());
/* 101 */     handler.func_71560_a((ICommand)new GuildToggleCommand());
/* 102 */     handler.func_71560_a((ICommand)new BlitzDuels());
/* 103 */     handler.func_71560_a((ICommand)new SwapLobbyCommand());
/* 104 */     handler.func_71560_a((ICommand)new TipAllCommand());
/* 105 */     handler.func_71560_a((ICommand)new ReplayCommand());
/* 106 */     handler.func_71560_a((ICommand)new GuildOnlineCommand());
/* 107 */     handler.func_71560_a((ICommand)new BlockRemoveCommand());
/* 108 */     handler.func_71560_a((ICommand)new ToggleChatCommand());
/* 109 */     handler.func_71560_a((ICommand)new StatusOfflineCommand());
/* 110 */     handler.func_71560_a((ICommand)new StatusOnlineCommand());
/* 111 */     handler.func_71560_a((ICommand)new StatusBusyCommand());
/* 112 */     handler.func_71560_a((ICommand)new StatusAwayCommand());
/* 113 */     handler.func_71560_a((ICommand)new PartyDemoteCommand());
/* 114 */     handler.func_71560_a((ICommand)new NickReuseCommand());
/* 115 */     handler.func_71560_a((ICommand)new SendCommand());
/* 116 */     handler.func_71560_a((ICommand)new PlayerInfoCommand());
/* 117 */     handler.func_71560_a((ICommand)new AutoTextMessageCommand());
/* 118 */     handler.func_71560_a((ICommand)new CapeFolderCommand());
/* 119 */     handler.func_71560_a((ICommand)new ShortcutCommands());
/* 120 */     handler.func_71560_a((ICommand)new ResetGuiCommand());
/* 121 */     handler.func_71560_a((ICommand)new AntiCheatInfoCommand());
/*     */   }
/*     */ }


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/wtf/tatp/meowtils/util/RegisterCommand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */