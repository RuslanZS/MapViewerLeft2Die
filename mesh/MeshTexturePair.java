package mesh;

public class MeshTexturePair {
    // Открытая ссылка на объект трехмерной геометрии (сетку вершин и полигонов)
    public MeshGeometry mesh;
    
    // Открытая ссылка на связанный текстурный процессор (тип Object для рефлексии tex)
    public Object texture;
    
    // Нативная ссылка по FernFlower на конкретный полигон для Z-сортировки Шелла [INDEX: 0.1.18]
    public BasePolygon polyReference;

    /**
     * Конструктор контейнера связки меша, текстуры и ссылки на полигон кадра
     * @param mesh Ссылка на 3D-геометрию меша
     * @param texture Ссылка на текстурный процессор TextureRasterizer
     */
    public MeshTexturePair(MeshGeometry mesh, Object texture) {
        this.mesh = mesh;
        this.texture = texture;
        this.polyReference = null;
    }
}
