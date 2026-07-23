package mesh;

import javax.microedition.lcdui.Graphics;

public class OcclusionPortal {
    private MapSector destinationSector;
    private final Vertex3D[] worldVertices;
    private final Vertex3D[] screenVertices;
    private int vertexCount;
    private Vector3D normalVector;
    
    private int centerScreenX;
    private int centerScreenY;

    /**
     * Конструктор объекта портала (дверного проема).
     * @param vertices Массив вершин, формирующих рамку прохода
     */
    public OcclusionPortal(Vertex3D[] vertices) {
        this.worldVertices = vertices;
        Vertex3D v0 = this.worldVertices[0];
        Vertex3D v1 = this.worldVertices[1];
        Vertex3D v2 = this.worldVertices[2];
        
        // Расчет вектора нормали плоскости прохода через утилиту GeometryMath [INDEX: 0.1.10]
        this.normalVector = GeometryMath.calculateTriangleNormal(
            v0.worldX, v0.worldY, v0.worldZ,
            v1.worldX, v1.worldY, v1.worldZ,
            v2.worldX, v2.worldY, v2.worldZ
        );

        if (vertices == null || vertices.length != 4) {
            System.out.println("PORTAL: warning: non-standard vertex count: " + (vertices != null ? vertices.length : 0));
        }

        // Инициализируем рабочий массив экранных 2D-координат портала [INDEX: 0.1.25]
        this.screenVertices = new Vertex3D[4];
        for (int i = 0; i < this.screenVertices.length; ++i) {
            this.screenVertices[i] = new Vertex3D(0, 0, 0);
        }
    }

    public final void setDestinationSector(MapSector sector) {
        this.destinationSector = sector;
    }

    public final MapSector getDestinationSector() {
        return this.destinationSector;
    }

    public final Vertex3D[] getWorldVertices() {
        return this.worldVertices;
    }

    /**
     * Алгоритм окклюзии, проецирования и резервного реберного отсечения ближней плоскости Z.
     * Восстановлен один в один по нативным формулам байт-кода FernFlower [INDEX: 0.1.25, 0.1.26, 0.1.32].
     */
    public final boolean checkOcclusion(main.SoftwareRenderer renderer, int clipL, int clipT, int clipR, int clipB) {
        main.TransformMatrix camMatrix = renderer.getActiveCameraMatrix();
        int camZ = camMatrix.m23;
        int camY = camMatrix.m13;
        int camX = camMatrix.m03;
        
        Vertex3D v0 = this.worldVertices[0];
        int dx = camX - v0.worldX;
        int dy = camY - v0.worldY;
        int dz = camZ - v0.worldZ;

        // Грубый тест удаления портала от камеры игрока [INDEX: 0.1.25]
        if (Math.abs(dx * this.normalVector.x + dy * this.normalVector.y + dz * this.normalVector.z >> 12) < 300) {
            this.centerScreenX = clipL;
            this.centerScreenY = clipT;
            return true;
        } else {
            Vertex3D[] sVerts = this.screenVertices;
            Vertex3D[] wVerts = this.worldVertices;
            main.TransformMatrix projMatrix = renderer.getActiveProjectionMatrix();

            // Проецируем 3D точки рамки двери на экран по Fixed-Point схеме движка [INDEX: 0.1.25, 0.1.26]
            for (int i = 0; i < wVerts.length; ++i) {
                Vertex3D v = wVerts[i];
                int m00_s = projMatrix.m00 >> 2; int m01_s = projMatrix.m01 >> 2; int m02_s = projMatrix.m02 >> 2;
                int m10_s = projMatrix.m10 >> 2; int m11_s = projMatrix.m11 >> 2; int m12_s = projMatrix.m12 >> 2;
                int m20_s = projMatrix.m20 >> 2; int m21_s = projMatrix.m21 >> 2; int m22_s = projMatrix.m22 >> 2;

                sVerts[i].screenX = (v.worldX * m00_s >> 12) + (v.worldY * m01_s >> 12) + (v.worldZ * m02_s >> 12) + projMatrix.m03;
                sVerts[i].screenY = (v.worldX * m10_s >> 12) + (v.worldY * m11_s >> 12) + (v.worldZ * m12_s >> 12) + projMatrix.m13;
                sVerts[i].screenZ = (v.worldX * m20_s >> 12) + (v.worldY * m21_s >> 12) + (v.worldZ * m22_s >> 12) + projMatrix.m23;
            }
            int outCount = 0;
            // Попарно обходим ребра четырехугольника прохода и сдвигаем точки на плоскость Z [INDEX: 0.1.26]
            for (int i = 0; i < wVerts.length; ++i) {
                int next = (i + 1 > wVerts.length - 1) ? 0 : i + 1;
                Vertex3D currV = sVerts[i];
                Vertex3D nextV = sVerts[next];
                
                if (currV.screenZ <= 0 || nextV.screenZ <= 0) {
                    if (currV.screenZ < 0 && nextV.screenZ < 0) {
                        copyVertexData(sVerts[outCount], currV); ++outCount;
                    }
                    if (currV.screenZ < 0 && nextV.screenZ > 0) {
                        copyVertexData(sVerts[outCount], currV); ++outCount;
                        interpolateNearPlane(currV, nextV, sVerts[outCount]); ++outCount;
                    }
                    if (currV.screenZ > 0 && nextV.screenZ < 0) {
                        interpolateNearPlane(currV, nextV, sVerts[outCount]); ++outCount;
                    }
                }
            }

            for (int i = 0; i < outCount; ++i) {
                sVerts[i].projectToScreen(renderer);
            }

            this.vertexCount = outCount;
            return this.vertexCount >= 3;
        }
    }

    /**
     * Попиксельная интерполяция координат вершины при пересечении ближней плоскости Z [INDEX: 0.1.26]
     */
    private static final void interpolateNearPlane(Vertex3D v0, Vertex3D v1, Vertex3D outV) {
        int deltaZ = v1.screenZ - v0.screenZ;
        if (deltaZ == 0) deltaZ = 1;
        int stepX = (v1.screenX - v0.screenX << 12) / deltaZ;
        int stepY = (v1.screenY - v0.screenY << 12) / deltaZ;
        outV.screenX = v0.screenX - (stepX * v0.screenZ >> 12);
        outV.screenY = v0.screenY - (stepY * v0.screenZ >> 12);
        outV.screenZ = 1; // Безопасно фиксируем глубину Z = 1 для перспективного деления [INDEX: 0.1.26]
    }

    private static final void copyVertexData(Vertex3D dest, Vertex3D src) {
        dest.screenX = src.screenX; 
        dest.screenY = src.screenY; 
        dest.screenZ = src.screenZ;
    }

    /**
     * Метод визуальной отладки. Отрисовывает контур дверного проема ярко-красным цветом [INDEX: 0.1.34].
     */
    public final void drawDebugPortal(Graphics g, int xOffset, int yOffset) {
        g.setColor(255, 0, 0); // Ярко-красный цвет по FernFlower [INDEX: 0.1.34]
        for (int i = 0; i < this.vertexCount; ++i) {
            Vertex3D v0 = this.screenVertices[i];
            Vertex3D v1 = this.screenVertices[(i + 1) % this.vertexCount];
            g.drawLine(v0.screenX + xOffset, v0.screenY + yOffset, v1.screenX + xOffset, v1.screenY + yOffset);
        }
    }
}
