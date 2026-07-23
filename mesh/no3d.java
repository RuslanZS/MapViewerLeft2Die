package mesh;

public class no3d {

    /**
     * Создает процедурный fallback-куб заданного размера для замещения отсутствующей модели.
     * Полностью заполняет массивы вершин и четырехугольников с лицевым обходом ребер.
     * @param fallbackTex Ссылка на текстуру-заглушку (TextureRasterizer)
     * @param size Отрезок полуразмера куба (например, 20 для 40x40x20 или 3 для ТЗ 3x3x3)
     * @return Экземпляр MeshGeometry, готовый к забросу в конвейер рендерера
     */
    public static MeshGeometry createCustomCube(Object fallbackTex, int size) {
        // 1. Генерируем 8 вершин куба в пространстве
        Vertex3D[] vertices = new Vertex3D[8];
        vertices[0] = new Vertex3D(-size, -size, -size);
        vertices[1] = new Vertex3D( size, -size, -size);
        vertices[2] = new Vertex3D( size,  size, -size);
        vertices[3] = new Vertex3D(-size,  size, -size);
        vertices[4] = new Vertex3D(-size, -size,  size);
        vertices[5] = new Vertex3D( size, -size,  size);
        vertices[6] = new Vertex3D( size,  size,  size);
        vertices[7] = new Vertex3D(-size,  size,  size);

        // 2. Генерируем 6 четырехугольных граней куба (Квадов)
        // ТЗ: Координаты развертки передаются в беззнаковых байтах (0..255)
        byte zero = (byte) 0;
        byte max = (byte) 255;
        
        TexturedQuad[] quads = new TexturedQuad[6];

        // Передняя грань (Инвертированный порядок по FernFlower для обхода Back-face)
        quads[0] = new TexturedQuad(vertices[3], vertices[2], vertices[1], vertices[0], zero, zero, max, zero, max, max, zero, max);
        // Задняя грань
        quads[1] = new TexturedQuad(vertices[4], vertices[5], vertices[6], vertices[7], zero, zero, max, zero, max, max, zero, max);
        // Левая грань
        quads[2] = new TexturedQuad(vertices[0], vertices[1], vertices[5], vertices[4], zero, zero, max, zero, max, max, zero, max);
        // Правая грань
        quads[3] = new TexturedQuad(vertices[2], vertices[3], vertices[7], vertices[6], zero, zero, max, zero, max, max, zero, max);
        // Верхняя грань
        quads[4] = new TexturedQuad(vertices[3], vertices[0], vertices[4], vertices[7], zero, zero, max, zero, max, max, zero, max);
        // Нижняя грань
        quads[5] = new TexturedQuad(vertices[1], vertices[2], vertices[6], vertices[5], zero, zero, max, zero, max, max, zero, max);

        BasePolygon[] polygons = new BasePolygon[6];
        System.arraycopy(quads, 0, polygons, 0, 6);

        return new MeshGeometry(vertices, polygons, fallbackTex);
    }

    /**
     * Создает штатный fallback-куб для основной модели (размер 20)
     */
    public static MeshGeometry createModelCube(Object fallbackTex) {
        return createCustomCube(fallbackTex, 20);
    }
    /**
     * Создает процедурный куб для скайбокса (Неба).
     * Отличается тем, что грани развернуты «вовнутрь» куба, так как камера находится в его центре.
     * @param fallbackTex Ссылка на текстуру неба (TextureRasterizer)
     * @return Экземпляр MeshGeometry для скайбокса
     */
    public static MeshGeometry createSkyCube(Object fallbackTex) {
        int size = 100; // Скайбокс должен быть большим, чтобы перекрывать сцену
        
        Vertex3D[] vertices = new Vertex3D[8];
        vertices[0] = new Vertex3D(-size, -size, -size);
        vertices[1] = new Vertex3D( size, -size, -size);
        vertices[2] = new Vertex3D( size,  size, -size);
        vertices[3] = new Vertex3D(-size,  size, -size);
        vertices[4] = new Vertex3D(-size, -size,  size);
        vertices[5] = new Vertex3D( size, -size,  size);
        vertices[6] = new Vertex3D( size,  size,  size);
        vertices[7] = new Vertex3D(-size,  size,  size);

        byte zero = (byte) 0;
        byte max = (byte) 255;
        
        TexturedQuad[] quads = new TexturedQuad[6];

        // Грани развернуты наоборот, чтобы камера внутри куба видела их лицевую сторону
        quads[0] = new TexturedQuad(vertices[0], vertices[1], vertices[2], vertices[3], zero, zero, max, zero, max, max, zero, max);
        quads[1] = new TexturedQuad(vertices[7], vertices[6], vertices[5], vertices[4], zero, zero, max, zero, max, max, zero, max);
        quads[2] = new TexturedQuad(vertices[4], vertices[5], vertices[1], vertices[0], zero, zero, max, zero, max, max, zero, max);
        quads[3] = new TexturedQuad(vertices[6], vertices[7], vertices[3], vertices[2], zero, zero, max, zero, max, max, zero, max);
        quads[4] = new TexturedQuad(vertices[7], vertices[4], vertices[0], vertices[3], zero, zero, max, zero, max, max, zero, max);
        quads[5] = new TexturedQuad(vertices[5], vertices[6], vertices[2], vertices[1], zero, zero, max, zero, max, max, zero, max);

        BasePolygon[] polygons = new BasePolygon[6];
        System.arraycopy(quads, 0, polygons, 0, 6);

        return new MeshGeometry(vertices, polygons, fallbackTex);
    }
}
