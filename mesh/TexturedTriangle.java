package mesh;

public class TexturedTriangle extends BasePolygon {
    // Три экранные и мировые вершины треугольника [INDEX: 0.1.10]
    public Vertex3D v1, v2, v3;

    // UV-координаты текстурной развертки для каждой из трех вершин [INDEX: 0.1.10].
    // Хранятся в виде знаковых byte (0..255 переводится через маску & 255)
    public byte u1, v1_coord;
    public byte u2, v2_coord;
    public byte u3, v3_coord;

    /**
     * Конструктор текстурированного треугольника.
     */
    public TexturedTriangle(
        Vertex3D vertex1, Vertex3D vertex2, Vertex3D vertex3,
        byte u1_val, byte v1_val,
        byte u2_val, byte v2_val,
        byte u3_val, byte v3_val
    ) {
        // Передаем вершины в базовый класс BasePolygon для обсчета вектора нормали плоскости [INDEX: 0.1.10]
        super(vertex1, vertex2, vertex3);
        
        this.v1 = vertex1;
        this.v2 = vertex2;
        this.v3 = vertex3;

        this.u1 = u1_val;
        this.v1_coord = v1_val;
        this.u2 = u2_val;
        this.v2_coord = v2_val;
        this.u3 = u3_val;
        this.v3_coord = v3_val;
    }

    /**
     * Реализация абстрактного метода Culling-а и проверки видимости полигона.
     * Восстановлена строго один в один по нативным формулам знаков FernFlower [INDEX: 0.1.10].
     */
    public boolean checkVisibilityAndCulling(int clipLeft, int clipTop, int clipRight, int clipBottom) {
        // 1. Алгоритм Back-Face Culling (Отсечение обратных граней) [INDEX: 0.1.10]
        // Проверяем знак векторного произведения ребер на плоскости экрана (2D Cross Product)
        if ((long)(this.v1.screenX - this.v2.screenX) * (long)(this.v2.screenY - this.v3.screenY) <= 
            (long)(this.v1.screenY - this.v2.screenY) * (long)(this.v2.screenX - this.v3.screenX)) {
            return false;
        }

        // 2. Расчет средней глубины полигона для алгоритма художника (Z-сортировка Шелла) [INDEX: 0.1.10]
        this.averageDepth = (this.v1.screenZ + this.v2.screenZ + this.v3.screenZ) / 3;

        // 3. Алгоритм Frustum Culling (Отсечение по границам видимости камеры) [INDEX: 0.1.10]
        // Если все вершины находятся за плоскостью экрана (Z > 0), полигон невидим
        if (this.v1.screenZ > 0 && this.v2.screenZ > 0 && this.v3.screenZ > 0) {
            return false;
        }

        // Проверяем выход треугольника целиком за левую, правую, верхнюю или нижнюю рамку ножниц дисплея [INDEX: 0.1.10, 0.1.11]
        if (this.v1.screenX <= clipLeft && this.v2.screenX <= clipLeft && this.v3.screenX <= clipLeft) return false;
        if (this.v1.screenX >= clipRight && this.v2.screenX >= clipRight && this.v3.screenX >= clipRight) return false;
        if (this.v1.screenY <= clipTop && this.v2.screenY <= clipTop && this.v3.screenY <= clipTop) return false;
        if (this.v1.screenY >= clipBottom && this.v2.screenY >= clipBottom && this.v3.screenY >= clipBottom) return false;

        return true;
    }
    /**
     * Направление вершин и беззнаковых UV-масок в статический текстурный процессор [INDEX: 0.1.10].
     */
    public void draw(main.SoftwareRenderer renderer, Object textureInstance) {
        if (textureInstance == null) return;
        
        // Кастим переданный Object к TextureRasterizer из пакета tex
        tex.TextureRasterizer texture = (tex.TextureRasterizer) textureInstance;

        // Переводим UV координаты из знаковых byte в беззнаковые int (0..255) посредством битовой маски & 255 [INDEX: 0.1.10]
        int tu1 = this.u1 & 255;
        int tv1 = this.v1_coord & 255;
        int tu2 = this.u2 & 255;
        int tv2 = this.v2_coord & 255;
        int tu3 = this.u3 & 255;
        int tv3 = this.v3_coord & 255;

        // Вызываем оригинальный метод растеризации треугольника из пакета tex [INDEX: 0.1.10]
        tex.TextureRasterizer.renderPerspectiveTriangle(
            renderer, texture, 
            this.v1, tu1, tv1, 
            this.v2, tu2, tv2, 
            this.v3, tu3, tv3
        );
    }
}
