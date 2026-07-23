package main;

import javax.microedition.lcdui.Graphics;

public class SoftwareRenderer {
    // Константные шаги оригинальной целочисленной сортировки Шелла
    private static final int[] shellGaps = new int[]{1, 4, 10, 23, 57, 145, 356, 911, 1968, 4711, 11969, 27901, 84801};
    
    // Пиксельный массив буфера кадра (Framebuffer) в формате ARGB
    public int[] framebuffer;
    public int screenWidth;
    public int screenHeight;
    
    // Фокусные параметры перспективы и координаты оптического центра экрана
    public int focusX;
    public int focusY;
    public int centerX;
    public int centerY;

    // Внутренние буферы накопления полигонов текущего кадра
    private final mesh.MeshTexturePair[] activeFrameBuffer;
    private int activePolygonCount;

    // Активные копии матриц для подсистемы окклюзии (ТЗ / FernFlower)
    private final TransformMatrix activeCameraMatrix;
    private final TransformMatrix activeProjectionMatrix;

    /**
     * Конструктор программного софтверного 3D-видеочипа
     */
    public SoftwareRenderer(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;
        this.activePolygonCount = 0;
        
        this.framebuffer = new int[width * height];
        this.activeFrameBuffer = new mesh.MeshTexturePair[700]; // Жесткий лимит кадра
        
        for (int i = 0; i < this.activeFrameBuffer.length; ++i) {
            this.activeFrameBuffer[i] = new mesh.MeshTexturePair(null, null);
        }
        
        this.centerX = width / 2;
        this.centerY = height / 2;
        
        // Магический расчет фокусного расстояния перспективы 1.5x из FernFlower
        this.focusX = this.centerX * 6144 >> 12;
        this.focusY = this.centerY * 6144 >> 12;

        this.activeCameraMatrix = new TransformMatrix();
        this.activeProjectionMatrix = new TransformMatrix();
    }

    /**
     * Быстрая очистка экрана (заливка цветом) с развёрткой цикла по 16 ячеек памяти
     */
    public final void clearFramebuffer(int clearColor) {
        int idx;
        int[] buf;
        for (idx = (buf = this.framebuffer).length - 1; idx >= 16; buf[idx--] = clearColor) {
            buf[idx--] = clearColor; buf[idx--] = clearColor; buf[idx--] = clearColor;
            buf[idx--] = clearColor; buf[idx--] = clearColor; buf[idx--] = clearColor;
            buf[idx--] = clearColor; buf[idx--] = clearColor; buf[idx--] = clearColor;
            buf[idx--] = clearColor; buf[idx--] = clearColor; buf[idx--] = clearColor;
            buf[idx--] = clearColor; buf[idx--] = clearColor; buf[idx--] = clearColor;
        }
        while (idx >= 0) {
            buf[idx--] = clearColor;
        }
        this.activePolygonCount = 0;
    }

    /**
     * Нативная подготовка кадра по FernFlower: фиксирует текущую камеру для порталов
     */
    public final void prepareFrame(TransformMatrix viewMatrix) {
        this.activeCameraMatrix.copyFrom(viewMatrix);
        this.activeProjectionMatrix.copyFrom(viewMatrix);
        this.activePolygonCount = 0;
    }
    /**
     * Фильтрация и сбор полигонов 3D-моделей в кадровый массив буфера
     */
    public final void accumulateMeshPolygons(mesh.MeshGeometry mesh, int clipL, int clipT, int clipR, int clipB) {
        if (mesh == null) return;
        
        // Ссылка на текстуру (может быть null в Wireframe режиме)
        Object texObj = mesh.getTexture();
        tex.TextureRasterizer tex = (tex.TextureRasterizer) texObj;
        
        mesh.BasePolygon[] polygons = mesh.getPolygons();
        int meshPolyCount = polygons.length;
        int maxCapacity = this.activeFrameBuffer.length;

        for (int i = 0; i < meshPolyCount; ++i) {
            if (this.activePolygonCount >= maxCapacity) {
                return;
            }
            mesh.BasePolygon poly = polygons[i];
            
            // Запуск оригинального Culling-теста полигона
            if (poly.checkVisibilityAndCulling(clipL, clipT, clipR, clipB)) {
                mesh.MeshTexturePair pair = this.activeFrameBuffer[this.activePolygonCount];
                pair.mesh = mesh;
                pair.texture = tex;
                pair.polyReference = poly; // Линкуем полигон для алгоритма художника
                ++this.activePolygonCount;
            }
        }
    }

    /**
     * Оригинальный пакетный вершинный шейдер из FernFlower с балансировкой сдвигов 12 бит
     */
    public final void transformVerticesBatch(mesh.Vertex3D[] vertices, TransformMatrix matrix) {
        int cX = this.centerX;
        int cY = this.centerY;
        int fX = this.focusX;
        int fY = this.focusY;
        
        // Нативный сдвиг матричных осей >> 2 для Fixed-Point баланса (FernFlower)
        int m00_s = matrix.m00 >> 2; int m01_s = matrix.m01 >> 2; int m02_s = matrix.m02 >> 2;
        int m03_s = matrix.m03;
        int m10_s = matrix.m10 >> 2; int m11_s = matrix.m11 >> 2; int m12_s = matrix.m12 >> 2;
        int m13_s = matrix.m13;
        int m20_s = matrix.m20 >> 2; int m21_s = matrix.m21 >> 2; int m22_s = matrix.m22 >> 2;
        int m23_s = matrix.m23;

        for (int i = vertices.length - 1; i >= 0; --i) {
            mesh.Vertex3D v = vertices[i];
            int wx = v.worldX;
            int wy = v.worldY;
            int wz = v.worldZ;

            // Формулы трансформации осей один в один по FernFlower
            int tx = (wx * m00_s >> 12) + (wy * m01_s >> 12) + (wz * m02_s >> 12) + m03_s;
            int ty = (wx * m10_s >> 12) + (wy * m11_s >> 12) + (wz * m12_s >> 12) + m13_s;
            int tz = (wx * m20_s >> 12) + (wy * m21_s >> 12) + (wz * m22_s >> 12) + m23_s;

            // Проекция на 2D экран
            if (tz <= 0) {
                tx = tx * fX / (-tz + fX) + cX;
                ty = -ty * fY / (-tz + fY) + cY;
            } else {
                tx += cX;
                ty = -ty + cY;
            }
            
            v.screenX = tx;
            v.screenY = ty;
            v.screenZ = tz;
        }
    }

    /**
     * Сортировка Шелла (Алгоритм художника) и запуск штатного текстурного рендеринга
     */
    public final void rasterizeActiveFrame() {
        mesh.MeshTexturePair[] buf = this.activeFrameBuffer;
        int count = this.activePolygonCount;
        int gapIdx;
        
        for (gapIdx = 0; shellGaps[gapIdx] < count; ++gapIdx) {}

        while (true) {
            --gapIdx;
            if (gapIdx < 0) {
                // Отрисовка от дальних полигонов к ближним
                int rIdx = this.activePolygonCount - 1;
                for (this.activePolygonCount = 0; rIdx >= 0; --rIdx) {
                    mesh.MeshTexturePair pair = buf[rIdx];
                    if (pair.polyReference != null) {
                        pair.polyReference.draw(this, (tex.TextureRasterizer)pair.texture);
                    }
                }
                return;
            }

            int gap = shellGaps[gapIdx];
            for (int i = gap; i < count; ++i) {
                int j = i;
                mesh.MeshTexturePair temp = buf[i];
                int tempDepth = temp.polyReference.averageDepth;
                
                while (j >= gap && buf[j - gap].polyReference.averageDepth < tempDepth) {
                    buf[j] = buf[j - gap];
                    j -= gap;
                }
                buf[j] = temp;
            }
        }
    }
    /**
     * Режим каркасного рендеринга Wireframe (Спецификация ТЗ).
     * Вызывается при отсутствии пакета [tex]: сортирует полигоны и чертит подсвеченные красные 
     * грани треугольников/квадов на черном фоне дисплея телефона напрямую через нативный Graphics.
     */
    public final void rasterizeActiveFrameWireframe(Graphics g) {
        mesh.MeshTexturePair[] buf = this.activeFrameBuffer;
        int count = this.activePolygonCount;
        int gapIdx;
        
        for (gapIdx = 0; shellGaps[gapIdx] < count; ++gapIdx) {}

        while (true) {
            --gapIdx;
            if (gapIdx < 0) {
                // Принудительно заливаем экран черным цветом перед рисованием линий
                g.setColor(0, 0, 0);
                g.fillRect(0, 0, screenWidth, screenHeight);
                
                // ТЗ: Отрисовка подсвеченных красных граней самих кубов или 3D карты
                g.setColor(255, 0, 0);
                
                int rIdx = this.activePolygonCount - 1;
                for (this.activePolygonCount = 0; rIdx >= 0; --rIdx) {
                    mesh.MeshTexturePair pair = buf[rIdx];
                    if (pair.polyReference != null) {
                        // Извлекаем вершины полигона в зависимости от его типа (Триангуляция)
                        if (pair.polyReference instanceof mesh.TexturedTriangle) {
                            mesh.TexturedTriangle tri = (mesh.TexturedTriangle) pair.polyReference;
                            g.drawLine(tri.v1.screenX, tri.v1.screenY, tri.v2.screenX, tri.v2.screenY);
                            g.drawLine(tri.v2.screenX, tri.v2.screenY, tri.v3.screenX, tri.v3.screenY);
                            g.drawLine(tri.v3.screenX, tri.v3.screenY, tri.v1.screenX, tri.v1.screenY);
                        } else if (pair.polyReference instanceof mesh.TexturedQuad) {
                            mesh.TexturedQuad quad = (mesh.TexturedQuad) pair.polyReference;
                            g.drawLine(quad.v1.screenX, quad.v1.screenY, quad.v2.screenX, quad.v2.screenY);
                            g.drawLine(quad.v2.screenX, quad.v2.screenY, quad.v3.screenX, quad.v3.screenY);
                            g.drawLine(quad.v3.screenX, quad.v3.screenY, quad.v4.screenX, quad.v4.screenY);
                            g.drawLine(quad.v4.screenX, quad.v4.screenY, quad.v1.screenX, quad.v1.screenY);
                        }
                    }
                }
                return;
            }

            int gap = shellGaps[gapIdx];
            for (int i = gap; i < count; ++i) {
                int j = i;
                mesh.MeshTexturePair temp = buf[i];
                int tempDepth = temp.polyReference.averageDepth;
                
                while (j >= gap && buf[j - gap].polyReference.averageDepth < tempDepth) {
                    buf[j] = buf[j - gap];
                    j -= gap;
                }
                buf[j] = temp;
            }
        }
    }

    /**
     * Вывод буфера кадра пикселей на физический дисплей
     */
    public final void drawToDisplay(Graphics g, int x, int y) {
        g.drawRGB(this.framebuffer, 0, this.screenWidth, x, y, this.screenWidth, this.screenHeight, false);
    }

    // Системные геттеры для подсистемы окклюзии OcclusionPortal
    public final TransformMatrix getActiveCameraMatrix() { return this.activeCameraMatrix; }
    public final TransformMatrix getActiveProjectionMatrix() { return this.activeProjectionMatrix; }
}
