package mesh;

public class TexturedQuad extends BasePolygon {
    // Четыре экранные и мировые вершины четырехугольника (Квада) [INDEX: 0.1.12]
    public Vertex3D v1, v2, v3, v4;

    // Восемь UV-координат текстурной развертки для четырех углов полигона [INDEX: 0.1.12].
    // Хранятся в виде знаковых byte (0..255 переводится через маску & 255)
    public byte u1, v1_coord;
    public byte u2, v2_coord;
    public byte u3, v3_coord;
    public byte u4, v4_coord;

    /**
     * Конструктор текстурированного четырехугольника.
     */
    public TexturedQuad(
        Vertex3D vertex1, Vertex3D vertex2, Vertex3D vertex3, Vertex3D vertex4,
        byte u1_val, byte v1_val,
        byte u2_val, byte v2_val,
        byte u3_val, byte v3_val,
        byte u4_val, byte v4_val
    ) {
        // Передаем первые три вершины в базовый класс BasePolygon для обсчета нормали плоскости [INDEX: 0.1.10, 0.1.12]
        super(vertex1, vertex2, vertex3);
        
        this.v1 = vertex1;
        this.v2 = vertex2;
        this.v3 = vertex3;
        this.v4 = vertex4;

        this.u1 = u1_val;
        this.v1_coord = v1_val;
        this.u2 = u2_val;
        this.v2_coord = v2_val;
        this.u3 = u3_val;
        this.v3_coord = v3_val;
        this.u4 = u4_val;
        this.v4_coord = v4_val;
    }

    /**
     * Реализация абстрактного метода Culling-а и проверки видимости полигона.
     * Восстановлена строго по нативным формулам знаков FernFlower для квадов [INDEX: 0.1.12, 0.1.13].
     */
    public boolean checkVisibilityAndCulling(int clipLeft, int clipTop, int clipRight, int clipBottom) {
        // 1. Алгоритм Back-Face Culling (Отсечение обратных граней) [INDEX: 0.1.13]
        // Проверяем знак векторного произведения для трех опорных точек четырехугольника
        if ((long)(this.v1.screenX - this.v2.screenX) * (long)(this.v2.screenY - this.v4.screenY) <= 
            (long)(this.v1.screenY - this.v2.screenY) * (long)(this.v2.screenX - this.v4.screenX)) {
            return false;
        }

        // 2. Расчет средней глубины полигона с помощью быстрого битового сдвига вправо
        this.averageDepth = (this.v1.screenZ + this.v2.screenZ + this.v3.screenZ + this.v4.screenZ) >> 2;

        // 3. Алгоритм Frustum Culling (Отсечение по границам видимости камеры) [INDEX: 0.1.10]
        if (this.v1.screenZ > 0 && this.v2.screenZ > 0 && this.v3.screenZ > 0 && this.v4.screenZ > 0) {
            return false;
        }

        // Проверяем выход четырехугольника целиком за границы рамки ножниц дисплея [INDEX: 0.1.10]
        if (this.v1.screenX <= clipLeft && this.v2.screenX <= clipLeft && this.v3.screenX <= clipLeft && this.v4.screenX <= clipLeft) return false;
        if (this.v1.screenX >= clipRight && this.v2.screenX >= clipRight && this.v3.screenX >= clipRight && this.v4.screenX >= clipRight) return false;
        if (this.v1.screenY <= clipTop && this.v2.screenY <= clipTop && this.v3.screenY <= clipTop && this.v4.screenY <= clipTop) return false;
        if (this.v1.screenY >= clipBottom && this.v2.screenY >= clipBottom && this.v3.screenY >= clipBottom && this.v4.screenY >= clipBottom) return false;

        return true;
    }
    /**
     * Реализация абстрактного метода отрисовки квада.
     * Выполняет нативную программную триангуляцию на лету по FernFlower [INDEX: 0.1.12].
     */
    public void draw(main.SoftwareRenderer renderer, Object textureInstance) {
        if (textureInstance == null) return;
        
        // Кастим переданный Object к TextureRasterizer из пакета tex
        tex.TextureRasterizer texture = (tex.TextureRasterizer) textureInstance;

        // Переводим UV координаты из знаковых byte в беззнаковые int (0..255)
        int tu1 = this.u1 & 255;
        int tv1 = this.v1_coord & 255;
        int tu2 = this.u2 & 255;
        int tv2 = this.v2_coord & 255;
        int tu3 = this.u3 & 255;
        int tv3 = this.v3_coord & 255;
        int tu4 = this.u4 & 255;
        int tv4 = this.v4_coord & 255;

        // Нативная триангуляция FernFlower: два последовательных вызова статического метода [INDEX: 0.1.12]
        // Треугольник 1: вершины (v1, v2, v4)
        tex.TextureRasterizer.renderPerspectiveTriangle(
            renderer, texture, 
            this.v1, tu1, tv1, 
            this.v2, tu2, tv2, 
            this.v4, tu4, tv4
        );

        // Треугольник 2: вершины (v2, v3, v4)
        tex.TextureRasterizer.renderPerspectiveTriangle(
            renderer, texture, 
            this.v2, tu2, tv2, 
            this.v3, tu3, tv3, 
            this.v4, tu4, tv4
        );
    }
}
