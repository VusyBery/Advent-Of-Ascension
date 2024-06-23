package net.tslat.aoa3.library.object;

import com.google.common.collect.Lists;
import com.mojang.brigadier.Message;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.tslat.aoa3.util.LocaleUtil;
import org.jetbrains.annotations.ApiStatus;

import java.util.UUID;
import java.util.function.UnaryOperator;

public final class Text extends MutableComponent {
    private static final ChatFormatting[] NO_FORMAT = new ChatFormatting[0];

    private Text(ComponentContents contents) {
        super(contents, Lists.newArrayList(), Style.EMPTY);
    }

    public static Text of(String langKey) {
        return of(langKey, NO_FORMAT);
    }

    public static Text of(String langKey, ChatFormatting... styles) {
        return of(langKey, formatting(styles), TranslatableContents.NO_ARGS);
    }

    public static Text of(String langKey, int colour, ChatFormatting... styles) {
        return of(langKey, colourAndFormatting(colour, styles), TranslatableContents.NO_ARGS);
    }

    public static Text of(String langKey, UnaryOperator<Style> style, Object... args) {
        return new Text(new TranslatableContents(langKey, null, transformArgs(args))).withStyle(style);
    }

    public static Text ofLiteral(String text) {
        return ofLiteral(text, NO_FORMAT);
    }

    public static Text ofLiteral(String text, ChatFormatting... styles) {
        return ofLiteral(text, formatting(styles));
    }

    public static Text ofLiteral(String text, int colour, ChatFormatting... styles) {
        return ofLiteral(text, colourAndFormatting(colour, styles));
    }

    public static Text ofLiteral(String text, UnaryOperator<Style> style) {
        return new Text(new PlainTextContents.LiteralContents(text)).withStyle(style);
    }

    public static Text formatAsBeneficial(String langKey, Object... args) {
        return ofFixedFormat(langKey, LocaleUtil.ItemDescriptionType.BENEFICIAL, args);
    }

    public static Text formatAsNegative(String langKey, Object... args) {
        return ofFixedFormat(langKey, LocaleUtil.ItemDescriptionType.HARMFUL, args);
    }

    public static Text formatAsNeutral(String langKey, Object... args) {
        return ofFixedFormat(langKey, LocaleUtil.ItemDescriptionType.NEUTRAL, args);
    }

    public static Text formatAsUnique(String langKey, Object... args) {
        return ofFixedFormat(langKey, LocaleUtil.ItemDescriptionType.UNIQUE, args);
    }

    public static Text formatAsSpecial(String langKey, Object... args) {
        return ofFixedFormat(langKey, LocaleUtil.ItemDescriptionType.SPECIAL, args);
    }

    public static Text formatAsItemTypeInfo(String langKey, Object... args) {
        return ofFixedFormat(langKey, LocaleUtil.ItemDescriptionType.ITEM_TYPE_INFO, args);
    }

    public static Text formatAsDamageValue(String langKey, Object... args) {
        return ofFixedFormat(langKey, LocaleUtil.ItemDescriptionType.ITEM_DAMAGE, args);
    }

    public static Text formatAsAmmoCost(String langKey, Object... args) {
        return ofFixedFormat(langKey, LocaleUtil.ItemDescriptionType.ITEM_AMMO_COST, args);
    }

    public static Text ofFixedFormat(String langKey, LocaleUtil.ItemDescriptionType type, Object... args) {
        return of(langKey, formatting(type.format), args);
    }

    public Text then(String langKey) {
        return then(langKey, NO_FORMAT);
    }

    public Text then(String langKey, ChatFormatting... styles) {
        return then(langKey, formatting(styles), TranslatableContents.NO_ARGS);
    }

    public Text then(String langKey, int colour, ChatFormatting... styles) {
        return then(langKey, colourAndFormatting(colour, styles), TranslatableContents.NO_ARGS);
    }

    public Text then(String langKey, UnaryOperator<Style> style, Object... args) {
        append(Component.translatable(langKey, transformArgs(args)).withStyle(style));

        return this;
    }

    public Text thenLiteral(String text) {
        return thenLiteral(text, NO_FORMAT);
    }

    public Text thenLiteral(String text, ChatFormatting... styles) {
        return thenLiteral(text, formatting(styles));
    }

    public Text thenLiteral(String text, int colour, ChatFormatting... styles) {
        return thenLiteral(text, colourAndFormatting(colour, styles));
    }

    public Text thenLiteral(String text, UnaryOperator<Style> style) {
        append(Component.literal(text).withStyle(style));

        return this;
    }

    private static UnaryOperator<Style> formatting(ChatFormatting... styles) {
        if (styles.length == 0)
            return UnaryOperator.identity();

        return style -> style.applyFormats(styles);
    }

    private static UnaryOperator<Style> colourInt(int colour) {
        return style -> style.withColor(colour);
    }

    private static UnaryOperator<Style> colourAndFormatting(int colour, ChatFormatting... styles) {
        if (styles.length == 0)
            return colourInt(colour);

        return style -> style.withColor(colour).applyFormats(styles);
    }

    private static Object[] transformArgs(Object... args) {
        for (int i = 0; i < args.length; i++) {
            args[i] = switch (args[i]) {
                case Component component -> component;
                case String text -> ofLiteral(text);
                case Number number -> ofLiteral(number.toString());
                case Boolean bool -> ofLiteral(bool.toString());
                case ResourceLocation path -> ofLiteral(path.toString());
                case BlockPos pos -> ofLiteral(pos.toString());
                case ChunkPos pos -> ofLiteral(pos.toString());
                case UUID uuid -> ofLiteral(uuid.toString());
                case Message message when message instanceof Component component -> component;
                case Message message -> ofLiteral(message.toString());
                case Item item -> item.getDefaultInstance().getDisplayName();
                case ItemStack stack -> stack.getDisplayName();
                case null, default -> TranslatableContents.TEXT_NULL;
            };
        }

        return args;
    }

    @ApiStatus.Internal
    @Override
    public Text setStyle(Style style) {
        return (Text)super.setStyle(style);
    }

    @ApiStatus.Internal
    @Override
    public Text append(String text) {
        return append(Component.literal(text));
    }

    @ApiStatus.Internal
    @Override
    public Text append(Component sibling) {
        return (Text)super.append(sibling);
    }

    @ApiStatus.Internal
    @Override
    public Text withStyle(UnaryOperator<Style> style) {
        return (Text)super.withStyle(style);
    }

    @ApiStatus.Internal
    @Override
    public Text withStyle(Style style) {
        return (Text)super.withStyle(style);
    }

    @ApiStatus.Internal
    @Override
    public Text withStyle(ChatFormatting... formatting) {
        return (Text)super.withStyle(formatting);
    }

    @ApiStatus.Internal
    @Override
    public Text withStyle(ChatFormatting format) {
        return (Text)super.withStyle(format);
    }

    @ApiStatus.Internal
    @Override
    public Text withColor(int colour) {
        return (Text)super.withColor(colour);
    }
}
