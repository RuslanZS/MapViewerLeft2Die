package mesh;

import java.util.Vector;

public class MapSector {
    // Геометрический 3D-меш (сетка полигонов) этого сектора
    private final MeshGeometry sectorMesh;

    // Прямоугольные границы сектора на плоскости карты (Bounding Box)
    private final int minX, maxX;
    private final int minZ, maxZ;

    // Массив порталов (проходов), ведущих из этой комнаты в соседние
    private OcclusionPortal[] portals;

    // Список динамических объектов, находящихся внутри данного сектора
    private final Vector dynamicObjects;

    // Флаг проходимости сектора (true — по сектору можно перемещаться)
    private boolean isWalkable;

    /**
     * Конструктор игрового сектора (комнаты)
     * @param mesh Ссылка на 3D-геометрию сектора
     * @param sectorIndex Системный идентификатор комнаты
     */
    public MapSector(MeshGeometry mesh, int sectorIndex) {
        this.sectorMesh = mesh;
        this.dynamicObjects = new Vector();
        this.isWalkable = true;

        // 1. Автоматически рассчитываем физические габариты Bounding Box сектора
        int tempMinX = Integer.MAX_VALUE;
        int tempMaxX = Integer.MIN_VALUE;
        int tempMinZ = Integer.MAX_VALUE;
        int tempMaxZ = Integer.MIN_VALUE;

        Vertex3D[] vertices = mesh.getVertices();
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

        // 2. Алгоритм анализа наклона пола по FernFlower [INDEX: 0.1.13]
        // Обходим полигоны сектора. Если полигон имеет крутой угол наклона нормали 
        // плоскости по оси Y (normalY <= 2048), комната помечается как непроходимая
        BasePolygon[] polygons = mesh.getPolygons();
        for (int i = 0; i < polygons.length; i++) {
            if (polygons[i].normalY <= 2048) {
                this.isWalkable = false;
                break;
            }
        }
    }

    /**
     * Инициализация и привязка массива порталов к текущему сектору
     */
    public void setPortals(OcclusionPortal[] sectorPortals) {
        this.portals = sectorPortals;
    }
    /**
     * Проверяет, входят ли переданные координаты внутрь границ данного сектора.
     * Реализует нативный хак разворота обхода вершин по знаку нормали floorY [INDEX: 0.1.14].
     * @return true, если точка физически находится внутри сектора, иначе false
     */
    public boolean containsPoint(int px, int py, int pz) {
        // 1. Грубая проверка по прямоугольной коробке габаритов (AABB Test)
        if (px < minX || px > maxX || pz < minZ || pz > maxZ) {
            return false;
        }

        // 2. Точная пополигональная проверка попадания точки
        BasePolygon[] polygons = sectorMesh.getPolygons();
        for (int i = 0; i < polygons.length; i++) {
            BasePolygon poly = polygons[i];
            short nY = poly.normalY;
            
            // Нас интересуют горизонтальные полигоны пола/потолков
            if (nY != 0) {
                if (poly instanceof TexturedTriangle) {
                    TexturedTriangle tri = (TexturedTriangle) poly;
                    // Нативный разворот вершин по FernFlower для барицентрического теста [INDEX: 0.1.14]
                    if (nY > 0) {
                        if (GeometryMath.isPointInTriangle(px, pz, tri.v1.worldX, tri.v1.worldZ, tri.v2.worldX, tri.v2.worldZ, tri.v3.worldX, tri.v3.worldZ)) {
                            return this.isWalkable;
                        }
                    } else if (nY < 0) {
                        if (GeometryMath.isPointInTriangle(px, pz, tri.v3.worldX, tri.v3.worldZ, tri.v2.worldX, tri.v2.worldZ, tri.v1.worldX, tri.v1.worldZ)) {
                            return this.isWalkable;
                        }
                    }
                } else if (poly instanceof TexturedQuad) {
                    TexturedQuad quad = (TexturedQuad) poly;
                    if (nY > 0) {
                        if (GeometryMath.isPointInQuad(px, pz, quad.v1.worldX, quad.v1.worldZ, quad.v2.worldX, quad.v2.worldZ, quad.v3.worldX, quad.v3.worldZ, quad.v4.worldX, quad.v4.worldZ)) {
                            return this.isWalkable;
                        }
                    } else if (nY < 0) {
                        if (GeometryMath.isPointInQuad(px, pz, quad.v4.worldX, quad.v4.worldZ, quad.v3.worldX, quad.v3.worldZ, quad.v2.worldX, quad.v2.worldZ, quad.v1.worldX, quad.v1.worldZ)) {
                            return this.isWalkable;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Генерирует массив смежных секторов на основе привязанных дверей-порталов [INDEX: 0.1.15].
     * @return Массив соседних секторов, куда можно заглянуть из этой комнаты
     */
    public MapSector[] getConnectedSectors() {
        if (portals == null || portals.length == 0) {
            return new MapSector[0];
        }

        MapSector[] connected = new MapSector[portals.length];
        for (int i = 0; i < portals.length; i++) {
            connected[i] = portals[i].getDestinationSector();
        }
        return connected;
    }

    // Геттеры для доступа из модулей рендеринга и графа сцены
    public MeshGeometry getGeometryMesh() { return this.sectorMesh; }
    public OcclusionPortal[] getPortals() { return this.portals; }
    public Vector getDynamicObjects() { return this.dynamicObjects; }
    public boolean isWalkable() { return this.isWalkable; }
}
