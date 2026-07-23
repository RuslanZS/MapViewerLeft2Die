package mesh;

public class MeshGeometry {
    // Базовые массивы трехмерной сетки (Mesh)
    private final Vertex3D[] vertices;
    private final BasePolygon[] polygons;
    
    // Связанный текстурный процессор (может быть null в Wireframe режиме)
    private final Object texture;

    // Геометрические габариты Bounding Box всей 3D-модели
    private int minX, maxX;
    private int minZ, maxZ;

    /**
     * Конструктор геометрического 3D-меша
     * @param vertices Массив пространственных вершин Vertex3D
     * @param polygons Массив полигонов (треугольники или четырехугольники)
     * @param texture Ссылка на связанный класс TextureRasterizer
     */
    public MeshGeometry(Vertex3D[] vertices, BasePolygon[] polygons, Object texture) {
        this.vertices = vertices;
        this.polygons = polygons;
        this.texture = texture;
        
        // Автоматически вычисляем габаритную коробку при создании объекта
        calculateBoundingBox();
    }

    /**
     * Автоматический расчет прямоугольных границ всей модели в пространстве (AABB)
     */
    private void calculateBoundingBox() {
        if (vertices == null || vertices.length == 0) {
            return;
        }

        int tempMinX = Integer.MAX_VALUE;
        int tempMaxX = Integer.MIN_VALUE;
        int tempMinZ = Integer.MAX_VALUE;
        int tempMaxZ = Integer.MIN_VALUE;

        // Поочередно обходим все вершины и фиксируем крайние точки
        for (int i = 0; i < vertices.length; i++) {
            int x = vertices[i].worldX;
            int z = vertices[i].worldZ;

            if (x < tempMinX) tempMinX = x;
            if (x > tempMaxX) tempMaxX = x;
            if (z < tempMinZ) tempMinZ = z;
            if (z > tempMaxZ) tempMaxZ = z;
        }

        this.minX = tempMinX;
        this.maxX = tempMaxX;
        this.minZ = tempMinZ;
        this.maxZ = tempMaxZ;
    }
    // Геттеры для доступа из программного конвейера рендеринга и менеджера сцены
    
    public Vertex3D[] getVertices() { 
        return this.vertices; 
    }
    
    public BasePolygon[] getPolygons() { 
        return this.polygons; 
    }
    
    public Object getTexture() { 
        return this.texture; 
    }
    
    public int getMinX() { 
        return this.minX; 
    }
    
    public int getMaxX() { 
        return this.maxX; 
    }
    
    public int getMinZ() { 
        return this.minZ; 
    }
    
    public int getMaxZ() { 
        return this.maxZ; 
    }
}
