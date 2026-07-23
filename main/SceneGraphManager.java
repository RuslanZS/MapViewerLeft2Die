package main;

import java.util.Vector;

public class SceneGraphManager {
    // Единственный глобальный экземпляр класса (Singleton) [INDEX: 0.1.15]
    private static SceneGraphManager instance;

    // Массив всех секторов (комнат) локации [INDEX: 0.1.15]
    private mesh.MapSector[] allSectors;
    
    // Двухмерная матрица смежности секторов (описывает связи комнат) [INDEX: 0.1.15]
    private mesh.MapSector[][] adjacencyMatrix;

    // Динамические списки для накопления видимой геометрии текущего кадра [INDEX: 0.1.15]
    private final Vector visibleSectors;
    private final Vector visibleObjects;

    /**
     * Закрытый конструктор для реализации паттерна Singleton
     */
    private SceneGraphManager() {
        this.visibleSectors = new Vector();
        this.visibleObjects = new Vector();
    }

    /**
     * Возвращает глобальный экземпляр менеджера графа сцены [INDEX: 0.1.15]
     * @return Текущий экземпляр SceneGraphManager
     */
    public static SceneGraphManager getInstance() {
        if (instance == null) {
            instance = new SceneGraphManager();
        }
        return instance;
    }

    /**
     * Инициализация графа сцены массивом секторов карты [INDEX: 0.1.15]
     * @param sectors Массив секторов, полученный из загрузчика
     */
    public void initializeGraph(mesh.MapSector[] sectors) {
        this.allSectors = sectors;
        
        // Строим базовую матрицу смежности на основе порталов секторов [INDEX: 0.1.15]
        if (sectors != null) {
            this.adjacencyMatrix = new mesh.MapSector[sectors.length][];
            for (int i = 0; i < sectors.length; i++) {
                // Извлекаем из сектора список связанных с ним через двери комнат [INDEX: 0.1.15]
                this.adjacencyMatrix[i] = sectors[i].getConnectedSectors();
            }
            FileLogger.log("SceneGraphManager: Инициализирован граф карты. Секторов: " + sectors.length);
        }
    }
    /**
     * Упрощенный пакетный метод сбора геометрии для утилиты просмотра одиночных карт.
     * Полностью обходит портальные отсечки и рендерит доступные сектора напрямую [INDEX: 0.1.15, 0.1.17].
     * @param renderer Ссылка на программный видеочип
     * @param viewMatrix Инвертированная матрица камеры
     * @param activeSectorIndex Индекс текущего сектора (игнорируется в режиме одиночной карты)
     */
    public void accumulateVisibleGeometry(SoftwareRenderer renderer, TransformMatrix viewMatrix, int activeSectorIndex) {
        // Очищаем списки видимости предыдущего кадра [INDEX: 0.1.15]
        this.visibleSectors.removeAllElements();
        this.visibleObjects.removeAllElements();

        // [НА ТИВНЫЙ ХАК FernFlower]: Передаем команду подготовки матриц кадра в рендерер [INDEX: 0.1.14]
        renderer.prepareFrame(viewMatrix);

        // Перебираем все доступные сектора (в нашем случае он 1) и закидываем их в софтверный рендерер [INDEX: 0.1.15]
        if (allSectors != null) {
            int w = renderer.screenWidth;
            int h = renderer.screenHeight;
            for (int i = 0; i < allSectors.length; i++) {
                this.visibleSectors.addElement(allSectors[i]);
                renderer.accumulateMeshPolygons(allSectors[i].getGeometryMesh(), 0, 0, w, h);
            }
        }
    }

    /**
     * Поиск индекса сектора по текущим пространственным координатам камеры [INDEX: 0.1.15]
     * @return Индекс сектора в массиве allSectors, либо -1, если точка вне карты
     */
    public int findSectorByCoords(int px, int py, int pz) {
        if (allSectors == null) {
            return -1;
        }

        // Обходим все сектора и проверяем, входит ли точка в их полигоны пола [INDEX: 0.1.12, 0.1.15]
        for (int i = 0; i < allSectors.length; i++) {
            if (allSectors[i].containsPoint(px, py, pz)) {
                return i;
            }
        }
        
        return -1;
    }

    /**
     * Сброс менеджера сцены при выгрузке карты [INDEX: 0.1.15]
     */
    public void clear() {
        this.visibleSectors.removeAllElements();
        this.visibleObjects.removeAllElements();
        this.allSectors = null;
        this.adjacencyMatrix = null;
    }
}
