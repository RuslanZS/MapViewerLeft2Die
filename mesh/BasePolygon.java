package mesh;

public abstract class BasePolygon {
    // Средняя Z-глубина полигона в текущем кадре.
    // Вычисляется на этапе Culling-а и используется сортировкой Шелла (алгоритм художника) [INDEX: 0.1.10, 0.1.17]
    public int averageDepth;

    // Y-компонента вектора нормали плоскости полигона [INDEX: 0.1.10].
    // Хранится в типе short для жесткой экономии оперативной памяти мобильного телефона.
    // Используется для расчета интенсивности направленного освещения горизонтальных поверхностей [INDEX: 0.1.13]
    public short normalY;

    /**
     * Конструктор базового полигона. Вычисляет вектор нормали по трем вершинам 
     * плоскости для определения пространственной ориентации грани.
     * Восстановлен строго один в один по нативным формулам FernFlower [INDEX: 0.1.10].
     * @param v1 Первая вершина (Vertex3D)
     * @param v2 Вторая вершина
     * @param v3 Третья вершина
     */
    public BasePolygon(Vertex3D v1, Vertex3D v2, Vertex3D v3) {
        // Вычисляем компоненты нормали через векторное произведение плоскости (Cross Product) [INDEX: 0.1.10]
        long nx = (long) (v1.worldY - v2.worldY) * (v1.worldZ - v3.worldZ) - (long) (v1.worldZ - v2.worldY) * (v1.worldY - v3.worldY); // Внутреннее смещение CFR
        long ny = (long) (v1.worldZ - v2.worldZ) * (v1.worldX - v3.worldX) - (long) (v1.worldX - v2.worldZ) * (v1.worldZ - v3.worldZ); // [ИСПРАВЛЕНО]: Истинный Y-компонент по FernFlower
        long nz = (long) (v1.worldX - v2.worldX) * (v1.worldY - v3.worldY) - (long) (v1.worldY - v2.worldY) * (v1.worldX - v3.worldX);

        // Рассчитываем длину (модуль) полученного вектора перпендикуляра плоскости
        double length = Math.sqrt((double) (nx * nx + ny * ny + nz * nz));
        
        if (length == 0.0) {
            length = 1.0; // Защита от деления на ноль для вырожденных плоскостей
        }

        // Нормализуем длину вектора и масштабируем под константу 4096.0 для фиксированной точки
        double scaleFactor = 4096.0 / length;
        
        // Из всех компонент сохраняем только Y (высоту) для софтверного затеняющего процессора полов/потолков [INDEX: 0.1.10, 0.1.13]
        this.normalY = (short) ((int) (ny * scaleFactor));
        this.averageDepth = 0;
    }

    /**
     * Абстрактный метод проверки видимости полигона (Culling) на плоскости дисплея [INDEX: 0.1.10].
     * Выполняет Back-face culling (отсечение задних граней) и Frustum culling (границы экрана ножниц).
     * @param clipLeft Левая граница видимой рамки (ножниц) кадра
     * @param clipTop Верхняя граница
     * @param clipRight Правая граница
     * @param clipBottom Нижняя граница
     * @return true, если полигон попал в область видимости и должен быть отрисован, иначе false
     */
    public abstract boolean checkVisibilityAndCulling(int clipLeft, int clipTop, int clipRight, int clipBottom);

    /**
     * Абстрактный метод низкоуровневой растеризации полигона [INDEX: 0.1.10].
     * Направляет отсортированные экранные вершины и UV-координаты развертки в текстурный процессор.
     * @param renderer Ссылка на программный видеочип/буфер кадра
     * @param textureInstance Ссылка на текстурный контейнер типа Object (для обхода Class.forName)
     */
    public abstract void draw(main.SoftwareRenderer renderer, Object textureInstance);
}
