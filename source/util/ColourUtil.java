package net.tslat.aoa3.util;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;

public final class ColourUtil {
	public static final int WHITE = 0xFFFFFF;
	public static final int BLACK = 0x000000;
	public static final int RED = 0xFF0000;
	public static final int GREEN = 0x00FF00;
	public static final int BLUE = 0x0000FF;
	public static final int YELLOW = 0xFFFF00;
	public static final int CYAN = 0x00FFFF;

	public static int RGB(int red, int green, int blue) {
		return red << 16 | green << 8 | blue;
	}

	public static int ARGB(int red, int green, int blue, int alpha) {
		return makeARGB(RGB(red, green, blue), alpha);
	}

	public static int RGB(float red, float green, float blue) {
		return (int)(red * 255f) << 16 | (int)(green * 255f) << 8 | (int)(blue * 255f);
	}

	public static int ARGB(float red, float green, float blue, float alpha) {
		return makeARGB(RGB(red, green, blue), (int)(alpha * 255f));
	}

	public static int RGB(double red, double green, double blue) {
		return (int)(red * 255f) << 16 | (int)(green * 255f) << 8 | (int)(blue * 255f);
	}

	public static int ARGB(double red, double green, double blue, double alpha) {
		return makeARGB(RGB(red, green, blue), (int)(alpha * 255f));
	}

	public static int makeARGB(int colour, int alpha) {
		return alpha << 24 | colour;
	}

	public static int getAlpha(int rgb) {
		return (rgb >> 24) & 0xFF;
	}

	public static int getRed(int rgb) {
		return (rgb >> 16) & 0xFF;
	}

	public static int getGreen(int rgb) {
		return (rgb >> 8) & 0xFF;
	}

	public static int getBlue(int rgb) {
		return rgb & 0xFF;
	}

	public static int lerpColour(int fromColour, int toColour, float delta) {
		Colour from = new Colour(fromColour);
		Colour to = new Colour(toColour);

		return ARGB(lerpColourSegment(from.red, to.red, delta), lerpColourSegment(from.green, to.green, delta), lerpColourSegment(from.blue, to.blue, delta), lerpAlpha(from.alpha, to.alpha, delta));
	}

	public static float lerpColourSegment(float from, float to, float delta) {
		return (float)Math.sqrt((1 - delta) * (from * from) + delta * to * to);
	}

	public static float lerpAlpha(float from, float to, float delta) {
		return (1 - delta) * from + delta * to;
	}

	public record Colour(float red, float green, float blue, float alpha) {
		public Colour(int red, int green, int blue) {
			this(red / 255f, green / 255f, blue / 255f, 1f);
		}

		public Colour(int rgb) {
			this(getRed(rgb) / 255f, getGreen(rgb) / 255f, getBlue(rgb) / 255f, getAlpha(rgb) / 255f);
		}

		public String rgbHex() {
			return Integer.toHexString((int)(red * 255f)) + Integer.toHexString((int)(green * 255f)) + Integer.toHexString((int)(blue * 255f));
		}

		public String argbHex() {
			return Integer.toHexString((int)(alpha * 255f)) + Integer.toHexString((int)(red * 255f)) + Integer.toHexString((int)(green * 255f)) + Integer.toHexString((int)(blue * 255f));
		}

		public int rgbInt() {
			return RGB(this.red, this.green, this.blue);
		}

		public int argbInt() {
			return ARGB(this.red, this.green, this.blue, this.alpha);
		}

		public Colour multiply(float red, float green, float blue, float alpha) {
			return new Colour(Mth.clamp(this.red * red, 0, 1), Mth.clamp(this.green * green, 0, 1), Mth.clamp(this.blue * blue, 0, 1), Mth.clamp(this.alpha * alpha, 0, 1));
		}

		public void toNetwork(FriendlyByteBuf buffer) {
			buffer.writeFloat(this.red);
			buffer.writeFloat(this.green);
			buffer.writeFloat(this.blue);
			buffer.writeFloat(this.alpha);
		}

		public static Colour fromNetwork(FriendlyByteBuf buffer) {
			return new Colour(buffer.readFloat(), buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
		}
	}
}
