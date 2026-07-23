package tex;

public class notex {

    private static int[] generateChessPattern(int primaryColor, int secondaryColor) {
        int size = 64; 
        int[] pixels = new int[size * size];
        
        for (int y = 0; y < size; y++) {
            int blockY = y / 8;
            for (int x = 0; x < size; x++) {
                int blockX = x / 8;
                if ((blockX + blockY) % 2 == 0) {
                    pixels[y * size + x] = primaryColor;
                } else {
                    pixels[y * size + x] = secondaryColor;
                }
            }
        }
        return pixels;
    }

    public static Object createMainFallbackTexture() {
        int size = 64;
        int[] pixels = generateChessPattern(0xFFFF00FF, 0xFF000000);
        return new TextureRasterizer(pixels, size, size, 6);
    }

    public static Object createSkyFallbackTexture() {
        int size = 64;
        int[] pixels = generateChessPattern(0xFFFF0000, 0xFF000000);
        return new TextureRasterizer(pixels, size, size, 6);
    }
}
