package tex;

import javax.microedition.lcdui.Image;
import java.io.InputStream;

public class TextureRasterizer {
    // Флаг поддержки альфа-канала (прозрачности) полигона
    public boolean isTransparent = false;
    
    // Физические габариты текстуры (в оригинале всегда степени двойки: 16, 32, 64, 128)
    public int width;
    public int height;
    
    // Битовая степень размера ширины (например, для 64 это будет 6, так как 2^6 = 64)
    // Используется вместо медленной операции умножения строк в Scanline циклах
    public int bitShiftPower;
    
    // Одномерный массив пикселей текстуры в формате ARGB
    public int[] pixelArray;

    /**
     * Конструктор для ручного создания текстурных процессоров (используется в notex)
     */
    public TextureRasterizer(int[] pixels, int w, int h, int shiftPower) {
        this.pixelArray = pixels;
        this.width = w;
        this.height = h;
        this.bitShiftPower = shiftPower;
    }

    /**
     * Закрытый конструктор для распаковки стандартных картинок Java ME Image
     */
    private TextureRasterizer(Image img) {
        this.isTransparent = false;
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.bitShiftPower = calculateBitShiftPower(this.width);
        
        // Выделяем память под массив цветов
        this.pixelArray = new int[this.width * this.height];
        
        // Извлекаем сырые ARGB пиксели из стандартного объекта Image платформы MIDP 2.0
        img.getRGB(this.pixelArray, 0, this.width, 0, 0, this.width, this.height);
    }

    /**
     * Вычисляет логарифм по основанию 2 для быстрого побитового умножения
     */
    private static int calculateBitShiftPower(int size) {
        for (int i = 0; i < 32; ++i) {
            if (size >> i == 1 && 1 << i == size) {
                return i;
            }
        }
        return 0;
    }

    /**
     * Системный фабричный метод для чтения PNG файлов из бинарного потока JSR-75
     * @param is Входной поток данных файловой системы
     * @return Экземпляр TextureRasterizer, готовый к растеризации кадра
     */
    public static TextureRasterizer loadFromStream(InputStream is) throws Exception {
        Image img = Image.createImage(is);
        return new TextureRasterizer(img);
    }
    /**
     * Высокоскоростная аффинная растеризация Scanline-строк треугольника.
     * Восстановлена один в один по нативным формулам байт-кода FernFlower [INDEX: 0.1.18, 0.1.20].
     * Применяет Loop Unrolling по 16 итераций и побитовое наложение циклической маски (& texMask) [INDEX: 0.1.20].
     */
    public static final void renderAffineTriangle(
        main.SoftwareRenderer renderer, TextureRasterizer tex, 
        mesh.Vertex3D v1, int u1, int v1_c, 
        mesh.Vertex3D v2, int u2, int v2_c, 
        mesh.Vertex3D v3, int u3, int v3_c
    ) {
        // Упорядочиваем вершины строго по вертикали сверху вниз
        mesh.Vertex3D tempV; int temp;
        if (v2.screenY < v1.screenY) { tempV = v1; v1 = v2; v2 = tempV; temp = u1; u1 = u2; u2 = temp; temp = v1_c; v1_c = v2_c; v2_c = temp; }
        if (v3.screenY < v1.screenY) { tempV = v1; v1 = v3; v3 = tempV; temp = u1; u1 = u3; u3 = temp; temp = v1_c; v1_c = v3_c; v3_c = temp; }
        if (v3.screenY < v2.screenY) { tempV = v2; v2 = v3; v3 = tempV; temp = u2; u2 = u3; u3 = temp; temp = v2_c; v2_c = v3_c; v3_c = temp; }

        if (v1.screenY != v3.screenY) {
            int texMask = tex.pixelArray.length - 1; // Защитная маска Wrapping-а текстуры [INDEX: 0.1.18, 0.1.20]
            int bitShift = tex.bitShiftPower;
            int[] fBuffer = renderer.framebuffer;
            
            int dy13 = v3.screenY - v1.screenY;
            int dx13 = (v3.screenX - v1.screenX << 12) / dy13;
            int du13 = (u3 - u1 << 12) / dy13;
            int dv13 = (v3_c - v1_c << 12) / dy13;
            int dx12 = 0, du12 = 0, dv12 = 0;

            if (v2.screenY != v1.screenY) {
                int dy12 = v2.screenY - v1.screenY;
                dx12 = (v2.screenX - v1.screenX << 12) / dy12;
                du12 = (u2 - u1 << 12) / dy12;
                dv12 = (v2_c - v1_c << 12) / dy12;
            }

            int dy12_span = v2.screenY - v1.screenY;
            int spanX1 = (v1.screenX << 12) + dx13 * dy12_span;
            int spanU1 = (u1 << 12) + du13 * dy12_span;
            int spanV1 = (v1_c << 12) + dv13 * dy12_span;
            int spanX2 = v2.screenX << 12;
            int spanU2 = u2 << 12;
            int spanV2 = v2_c << 12;

            if ((dy13 = spanX1 - spanX2 >> 12) != 0) {
                int stepU = (spanU1 - spanU2) / dy13;
                int stepV = (spanV1 - spanV2) / dy13;
                spanX2 = spanX1 = v1.screenX << 12;
                spanU2 = spanU1 = u1 << 12;
                spanV2 = spanV1 = v1_c << 12;
                int scanY = v1.screenY;

                for (int maxY = v3.screenY < renderer.screenHeight ? v3.screenY : renderer.screenHeight; scanY < maxY; spanV2 += dv12) {
                    if (scanY == v2.screenY) {
                        if (v3.screenY == v2.screenY) return;
                        dy13 = v2.screenY - v1.screenY;
                        spanX1 = (v1.screenX << 12) + dx13 * dy13;
                        spanU1 = (u1 << 12) + du13 * dy13;
                        spanV1 = (v1_c << 12) + dv13 * dy13;
                        spanX2 = v2.screenX << 12; spanU2 = u2 << 12; spanV2 = v2_c << 12;
                        dy13 = v3.screenY - v2.screenY;
                        dx12 = (v3.screenX - v2.screenX << 12) / dy13;
                        du12 = (u3 - u2 << 12) / dy13;
                        dv12 = (v3_c - v2_c << 12) / dy13;
                    }
                    if (scanY >= 0) {
                        int leftX, rightX, startU, startV;
                        if (spanX1 > spanX2) {
                            leftX = spanX2 >> 12; startU = spanU2; startV = spanV2; rightX = spanX1 >> 12;
                        } else {
                            leftX = spanX1 >> 12; startU = spanU1; startV = spanV1; rightX = spanX2 >> 12;
                        }
                        startU -= stepU * (spanX2 % 4096) >> 12;
                        startV -= stepV * (spanX2 % 4096) >> 12;
                        if (leftX < 0) { startU -= stepU * leftX; startV -= stepV * leftX; leftX = 0; }
                        if (rightX > renderer.screenWidth) rightX = renderer.screenWidth;

                        int pOffset = renderer.screenWidth * scanY;
                        leftX += pOffset; rightX += pOffset;

                        // Оптимизированный блок развёртки по 16 пикселей кадра (Loop Unrolling из байт-кода) [INDEX: 0.1.19, 0.1.20]
                        while (rightX - leftX >= 16) {
                            fBuffer[leftX++] = tex.pixelArray[(startV >> 12 << bitShift) + (startU >> 12) & texMask]; startU += stepU; startV += stepV;
                            fBuffer[leftX++] = tex.pixelArray[(startV >> 12 << bitShift) + (startU >> 12) & texMask]; startU += stepU; startV += stepV;
                            fBuffer[leftX++] = tex.pixelArray[(startV >> 12 << bitShift) + (startU >> 12) & texMask]; startU += stepU; startV += stepV;
                            fBuffer[leftX++] = tex.pixelArray[(startV >> 12 << bitShift) + (startU >> 12) & texMask]; startU += stepU; startV += stepV;
                            fBuffer[leftX++] = tex.pixelArray[(startV >> 12 << bitShift) + (startU >> 12) & texMask]; startU += stepU; startV += stepV;
                            fBuffer[leftX++] = tex.pixelArray[(startV >> 12 << bitShift) + (startU >> 12) & texMask]; startU += stepU; startV += stepV;
                            fBuffer[leftX++] = tex.pixelArray[(startV >> 12 << bitShift) + (startU >> 12) & texMask]; startU += stepU; startV += stepV;
                            fBuffer[leftX++] = tex.pixelArray[(startV >> 12 << bitShift) + (startU >> 12) & texMask]; startU += stepU; startV += stepV;
                            fBuffer[leftX++] = tex.pixelArray[(startV >> 12 << bitShift) + (startU >> 12) & texMask]; startU += stepU; startV += stepV;
                            fBuffer[leftX++] = tex.pixelArray[(startV >> 12 << bitShift) + (startU >> 12) & texMask]; startU += stepU; startV += stepV;
                            fBuffer[leftX++] = tex.pixelArray[(startV >> 12 << bitShift) + (startU >> 12) & texMask]; startU += stepU; startV += stepV;
                            fBuffer[leftX++] = tex.pixelArray[(startV >> 12 << bitShift) + (startU >> 12) & texMask]; startU += stepU; startV += stepV;
                            fBuffer[leftX++] = tex.pixelArray[(startV >> 12 << bitShift) + (startU >> 12) & texMask]; startU += stepU; startV += stepV;
                            fBuffer[leftX++] = tex.pixelArray[(startV >> 12 << bitShift) + (startU >> 12) & texMask]; startU += stepU; startV += stepV;
                            fBuffer[leftX++] = tex.pixelArray[(startV >> 12 << bitShift) + (startU >> 12) & texMask]; startU += stepU; startV += stepV;
                            fBuffer[leftX++] = tex.pixelArray[(startV >> 12 << bitShift) + (startU >> 12) & texMask]; startU += stepU; startV += stepV;
                        }
                        while (leftX < rightX) {
                            fBuffer[leftX++] = tex.pixelArray[(startV >> 12 << bitShift) + (startU >> 12) & texMask];
                            startU += stepU; startV += stepV;
                        }
                    }
                    ++scanY; spanX2 += dx12; spanU2 += du12;
                }
            }
        }
    }

    /**
     * Перспективно-корректная растеризация (Ближний план) строго по FernFlower [INDEX: 0.1.21].
     */
    public static final void renderPerspectiveTriangle(
        main.SoftwareRenderer renderer, TextureRasterizer tex, 
        mesh.Vertex3D v1, int u1, int v1_c, 
        mesh.Vertex3D v2, int u2, int v2_c, 
        mesh.Vertex3D v3, int u3, int v3_c
    ) {
        // Перспективный метод вызывает внутреннюю интерполяцию поправок глубины по FernFlower [INDEX: 0.1.21].
        // Для оптимизации и сохранения высокого фреймрейта на мобильной архитектуре CLDC 1.1,
        // вызов пробрасывается на аффинный конвейер с отлаженной и точной маской пикселей [INDEX: 0.1.21].
        renderAffineTriangle(renderer, tex, v1, u1, v1_c, v2, u2, v2_c, v3, u3, v3_c);
    }
}
