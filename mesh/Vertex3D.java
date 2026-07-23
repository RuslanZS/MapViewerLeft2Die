package mesh;

public class Vertex3D {
    // 1. Координаты вершины в трехмерном мировом пространстве [INDEX: 0.1.9]
    // Хранятся в формате фиксированной точки (перемножаются на SCALE при импорте)
    public int worldX;
    public int worldY;
    public int worldZ;

    // 2. Полученные после трансформации экранные координаты камеры и глубина Z [INDEX: 0.1.9]
    public int screenX;
    public int screenY;
    public int screenZ; // Хранит глубину относительно камеры для Z-сортировки кадра

    /**
     * Конструктор: создает трехмерную вершину с заданными мировыми координатами [INDEX: 0.1.9]
     * @param x Координата ширины мира
     * @param y Координата высоты мира
     * @param z Координата глубины мира
     */
    public Vertex3D(int x, int y, int z) {
        this.worldX = x;
        this.worldY = y;
        this.worldZ = z;
        
        // Сброс экранных регистров кадра по умолчанию
        this.screenX = 0;
        this.screenY = 0;
        this.screenZ = 0;
    }

    /**
     * Алгоритм перспективной проекции трехмерной точки на плоскость дисплея.
     * Восстановлен строго один в один по нативным формулам FernFlower [INDEX: 0.1.9].
     * @param renderer Ссылка на программный видеочип, хранящий фокусные параметры экрана
     */
    public void projectToScreen(main.SoftwareRenderer renderer) {
        // Проверка ближней плоскости отсечения (Z-Clipping / Perspective protection)
        // По нативной логике FernFlower: если вершина перед камерой (глубина screenZ <= 0) [INDEX: 0.1.9]
        if (this.screenZ <= 0) {
            // Классическая фокусная проекция: Screen = (Coord * Focus) / (-Z + Focus) + Center [INDEX: 0.1.9]
            // Никаких искусственных битовых сдвигов << 7 от CFR здесь нет! [INDEX: 0.1.9]
            int divX = -this.screenZ + renderer.focusX;
            int divY = -this.screenZ + renderer.focusY;
            
            if (divX == 0) divX = 1; // Защита от ArithmeticException деления на ноль
            if (divY == 0) divY = 1;

            this.screenX = (this.screenX * renderer.focusX) / divX + renderer.centerX;
            this.screenY = (-this.screenY * renderer.focusY) / divY + renderer.centerY;
        } else {
            // Ортогональный сдвиг или заглушка для вершин, ушедших за плоскость видимости (за спину) [INDEX: 0.1.9]
            this.screenX += renderer.centerX;
            this.screenY = -this.screenY + renderer.centerY;
        }
    }

    /**
     * Копирует мировые координаты из другой вершины [INDEX: 0.1.9]
     * @param other Ссылка на исходную вершину Vertex3D
     */
    public void copyWorldCoordsFrom(Vertex3D other) {
        if (other != null) {
            this.worldX = other.worldX;
            this.worldY = other.worldY;
            this.worldZ = other.worldZ;
        }
    }
}
