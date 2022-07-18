package dev.devious.engine.utils;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Utils {
    public static float getBarrycentric(Vector3f point1, Vector3f point2, Vector3f point3, Vector2f position) {
        float det = (point2.z - point3.z) * (point1.x - point3.x) + (point3.x - point2.x) * (point1.z - point3.z);
        float l1 = ((point2.z - point3.z) * (position.x - point3.x) + (point3.x - point2.x) * (position.y - point3.z)) / det;
        float l2 = ((point3.z - point1.z) * (position.x - point3.x) + (point1.x - point3.x) * (position.y - point3.z)) / det;
        float l3 = 1.0f - l1 - l2;
        return l1 * point1.y + l2 * point2.y + l3 * point3.y;
    }
    public static FloatBuffer storeDataInFloatBuffer(float[] data) {
        FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static IntBuffer storeDataInIntBuffer(int[] data) {
        IntBuffer buffer = MemoryUtil.memAllocInt(data.length);
        buffer.put(data).flip();
        return buffer;
    }

    public static String loadResource(String filename) throws Exception {
        String result;
        try (InputStream inputStream = Utils.class.getResourceAsStream(filename);
             Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
             result = scanner.useDelimiter("//A").next();
        }
        return result;
    }

    public static List<String> readAllLines(String filename) {
        List<String> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Class.forName(Utils.class.getName()).getResourceAsStream(filename))))) {
            String line;
            while ((line = reader.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return list;
    }
}
