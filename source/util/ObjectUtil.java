package net.tslat.aoa3.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ObjectUtil {
	public static String bufferedReaderToString(BufferedReader reader) {
		final StringBuilder content = new StringBuilder();

		reader.lines().forEach(line -> {
			content.append(line);
			content.append("\n");
		});

		return content.toString();
	}

	@Nullable
	public static <T> T getFromCollection(Collection<T> collection, Predicate<T> predicate) {
		for (T t : collection) {
			if (predicate.test(t))
				return t;
		}

		return null;
	}

	public static <T> JsonObject codecToJson(Codec<T> codec, T object, Function<String, String> errMsg) {
		return codecToJson(codec, object, JsonOps.INSTANCE, errMsg);
	}

	public static <T> JsonObject codecToJson(Codec<T> codec, T object, DynamicOps<JsonElement> ops, Function<String, String> errMsg) {
		return codec.encodeStart(ops, object).getOrThrow(error -> new IllegalArgumentException(errMsg.apply(error))).getAsJsonObject();
	}

	public static <T> T[] shuffleArray(T[] array, RandomSource random) {
		int length = array.length;

		for (int i = length; i > 1; i--) {
			int swapTarget = random.nextInt(i);
			T temp = array[swapTarget];

			array[swapTarget] = array[i - 1];
			array[i - 1] = temp;
		}

		return array;
	}

	public static <T> boolean allEquivalent(BiPredicate<@Nullable T, @Nullable T> predicate, T... elements) {
		for (int i = 0; i < elements.length - 1; i++) {
			if (!predicate.test(elements[i], elements[i + 1]))
				return false;
		}

		return true;
	}

	public static Path getOrCreateDirectory(Path parentPath, String directory) throws IOException {
		Path resolvedPath = parentPath.resolve(directory);

		if (!Files.isDirectory(resolvedPath))
			Files.createDirectories(resolvedPath);

		return resolvedPath;
	}
}
