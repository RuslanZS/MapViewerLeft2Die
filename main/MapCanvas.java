package main;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import java.io.OutputStream;
import java.io.IOException;

public class MapCanvas extends Canvas implements Runnable {
    private final Main midlet;
    private final String workingDirUri;
    
    // Игровой поток
    private Thread engineThread;
    private boolean isRunning = false;
    
    // Состояния загрузки движка
    private boolean isLoading = true;
    private boolean isSettingTxtMissing = false;
    private boolean isMeshPackageLoaded = false;
    private boolean isTexPackageLoaded = false;
    
    // Ссылки на подсистемы
    private CameraController cameraController;
    private InputManager inputManager;
    private SoftwareRenderer renderer;
    private SceneGraphManager sceneManager;
    
    // Объекты геометрии и текстур (динамические типы из пакетов)
    private Object worldMesh;
    private Object skyMesh;
    private Object worldTexture;
    private Object skyTexture;
    
    // Системные счетчики
    private int currentFps = 0;
    private int currentSector = 0;

    /**
     * Конструктор основного игрового холста
     */
    public MapCanvas(Main midlet, String workingDirUri) {
        this.midlet = midlet;
        this.workingDirUri = workingDirUri;
        
        setFullScreenMode(true);
        
        // Базовые системные менеджеры создаются сразу
        this.inputManager = new InputManager(this);
        this.cameraController = new CameraController();
        this.renderer = new SoftwareRenderer(getWidth(), getHeight());
        this.sceneManager = SceneGraphManager.getInstance();
        
        // Запуск основного потока
        this.isRunning = true;
        this.engineThread = new Thread(this);
        this.engineThread.start();
    }

    /**
     * Главный цикл: сначала крутит экран загрузки, затем игровой цикл
     */
    public void run() {
        // Выполняем асинхронную рефлексивную загрузку ресурсов
        performReflectionAndLoading();
        
        long lastTime = System.currentTimeMillis();
        while (isRunning) {
            long startTime = System.currentTimeMillis();
            
            if (!isLoading) {
                // Если setting.txt отсутствует — управление намертво блокируется (ТЗ)
                if (!isSettingTxtMissing) {
                    cameraController.handleMovement(inputManager);
                }
                
                // Перерасчет FPS
                currentFps = FPSCounter.updateAndGetFps();
            }
            
            repaint();
            serviceRepaints();
            
            long elapsed = System.currentTimeMillis() - startTime;
            if (elapsed < 33) {
                try { Thread.sleep(33 - elapsed); } catch (Exception e) {}
            }
        }
    }
    /**
     * Пошаговая инициализация движка через рефлексию Class.forName()
     */
    private void performReflectionAndLoading() {
        FileLogger.log("MapCanvas: Начало динамической проверки архитектуры пакетов...");
        
        // Шаг 1: Проверяем наличие пакета mesh (ТЗ: грузится до классов текстур)
        try {
            Class.forName("mesh.MeshGeometry");
            Class.forName("mesh.ConfigAndMeshLoader");
            Class.forName("mesh.no3d");
            isMeshPackageLoaded = true;
            FileLogger.log("Рефлексия: Пакет [mesh] успешно верифицирован.");
        } catch (Throwable t) {
            isMeshPackageLoaded = false;
            FileLogger.log("КРИТИЧЕСКОЕ ПРЕДУПРЕЖДЕНИЕ: Классы пакета [mesh] отсутствуют: " + t.toString());
        }

        // Шаг 2: Проверяем наличие пакета tex
        try {
            Class.forName("tex.TextureRasterizer");
            Class.forName("tex.notex");
            isTexPackageLoaded = true;
            FileLogger.log("Рефлексия: Пакет [tex] успешно верифицирован.");
        } catch (Throwable t) {
            isTexPackageLoaded = false;
            FileLogger.log("ПРЕДУПРЕЖДЕНИЕ: Классы пакета [tex] отсутствуют (Wireframe режим): " + t.toString());
        }

        // Проверяем физическое наличие файла конфигурации
        boolean hasConfig = FileLogger.checkFileExists("setting.txt");
        if (!hasConfig) {
            isSettingTxtMissing = true;
            FileLogger.log("Логика: Файл setting.txt отсутствует. Активирован режим жесткого 3x3x3 куба.");
        }

        // Инициализируем ресурсы на основе результатов проверки пакетов
        initEngineResources();
        
        // Завершаем фазу загрузки, переключаем экран
        this.isLoading = false;
    }

    /**
     * Сборка 3D мира на основе доступных модулей
     */
    private void initEngineResources() {
        // Сценарий А: Полное отсутствие пакета mesh (Outer Fallback)
        if (!isMeshPackageLoaded) {
            FileLogger.log("Инициализация: Режим [Outer Fallback] — Наружный статичный черно-белый куб.");
            return;
        }

        // Сценарий Б: Файл настроек отсутствует (Статичный куб 3x3x3)
        if (isSettingTxtMissing) {
            try {
                // Создаем текстуру-шахматку и куб через рефлексивный вызов fallback-генераторов
                if (isTexPackageLoaded) {
                    this.worldTexture = Class.forName("tex.notex").getMethod("createMainFallbackTexture", null).invoke(null, null);
                }
                
                // no3d генерирует куб. Для 3x3x3 передаем уменьшенный размер (например, размер 3 единицы)
                // Так как в no3d методы были под жесткие размеры, мы адаптируем вызов или используем no3d.createSkyCube
                this.worldMesh = mesh.no3d.createSkyCube((tex.TextureRasterizer)this.worldTexture); 
                
                // Принудительно выставляем камеру в центр куба и блокируем координаты
                cameraController.setPosition(0, 0, 0);
            } catch (Exception ex) {
                FileLogger.log("Ошибка сборки fallback сцены 3x3x3: " + ex.toString());
            }
            return;
        }

        // Сценарий В: Штатная загрузка карты из JSR-75
        try {
            mesh.ConfigAndMeshLoader loaderInstance = new mesh.ConfigAndMeshLoader(workingDirUri);
            
            // Загружаем текстуры (если пакет tex отсутствует — возвращается null)
            if (isTexPackageLoaded) {
                this.worldTexture = loaderInstance.loadTextureWithFallback("TEXTURE");
                this.skyTexture = loaderInstance.loadTextureWithFallback("SKY_TEXTURE");
            } else {
                this.worldTexture = null;
                this.skyTexture = null;
            }

            // Загружаем 3D геометрию
            this.worldMesh = loaderInstance.loadMeshWithFallback("MODEL", (tex.TextureRasterizer)this.worldTexture);
            this.skyMesh = loaderInstance.loadMeshWithFallback("SKY_MODEL", (tex.TextureRasterizer)this.skyTexture);
            
            // Фиксируем разворот вершин для обхода Back-face culling
            mesh.MeshGeometry wM = (mesh.MeshGeometry)this.worldMesh;
            mesh.BasePolygon[] polys = wM.getPolygons();
            for (int i = 0; i < polys.length; i++) {
                if (polys[i] instanceof mesh.TexturedQuad) {
                    mesh.TexturedQuad q = (mesh.TexturedQuad)polys[i];
                    mesh.Vertex3D tmp = q.v1; q.v1 = q.v4; q.v4 = tmp;
                    tmp = q.v2; q.v2 = q.v3; q.v3 = tmp;
                }
            }

            mesh.MapSector globalSector = new mesh.MapSector(wM, 0);
            globalSector.setPortals(new mesh.OcclusionPortal);
            sceneManager.initializeGraph(new mesh.MapSector[]{globalSector});

        } catch (Exception e) {
            FileLogger.log("Ошибка стандартного парсинга ресурсов: " + e.toString());
            isSettingTxtMissing = true; // Сваливаемся в аварийный куб
        }
    }
    /**
     * Отрисовка графики холста
     */
    protected void paint(Graphics g) {
        // Сценарий 1: Экран загрузки (Черный фон, белая надпись по центру)
        if (isLoading) {
            g.setColor(0, 0, 0);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(255, 255, 255);
            g.drawString("Загрузка...", getWidth() / 2, getHeight() / 2, Graphics.HCENTER | Graphics.TOP);
            return;
        }

        // Очищаем буфер кадра рендерера
        renderer.clearFramebuffer(0x000000);

        // Сценарий 2: Тотальное отсутствие геометрии (Outer Fallback)
        if (!isMeshPackageLoaded) {
            drawOuterFallbackPattern(g);
            return;
        }

        // Сценарий 3: Штатный 3D рендер или куб 3x3x3
        TransformMatrix camMatrix = cameraController.getViewMatrix();
        TransformMatrix skyMatrix = cameraController.getSkyRotationMatrix();

        // Переводим вершины через вершинный конвейер
        if (skyMesh != null) {
            renderer.transformVerticesBatch(((mesh.MeshGeometry)skyMesh).getVertices(), skyMatrix);
        }
        if (worldMesh != null) {
            renderer.transformVerticesBatch(((mesh.MeshGeometry)worldMesh).getVertices(), camMatrix);
        }

        // Закидываем полигоны в буфер кадра
        int w = renderer.screenWidth;
        int h = renderer.screenHeight;
        
        if (skyMesh != null) renderer.accumulateMeshPolygons((mesh.MeshGeometry)skyMesh, 0, 0, w, h);
        if (worldMesh != null) renderer.accumulateMeshPolygons((mesh.MeshGeometry)worldMesh, 0, 0, w, h);

        // Проверяем режим отображения (ТЗ: если пакета tex нет — подсвеченные красные грани Wireframe)
        if (!isTexPackageLoaded) {
            // Активируем режим каркасных красных линий поверх черного кадра буфера
            renderer.rasterizeActiveFrameWireframe(g);
        } else {
            // Обычная попиксельная растеризация текстур и вывод Шелла
            renderer.rasterizeActiveFrame();
            renderer.drawToDisplay(g, 0, 0);
        }

        // Если файл настроек потерян — фиксируем надпись о блокировке камеры
        if (isSettingTxtMissing) {
            g.setColor(255, 0, 0);
            g.drawString("[ЗАБЛОКИРОВАНО]", 5, 25, Graphics.LEFT | Graphics.TOP);
        }

        drawDebugOverlay(g);
    }

    /**
     * Отрисовка наружного статичного куба с черно-белой шахматной сеткой (ТЗ)
     */
    private void drawOuterFallbackPattern(Graphics g) {
        int w = getWidth();
        int h = getHeight();
        g.setColor(0, 0, 0);
        g.fillRect(0, 0, w, h);
        
        // Процедурный рисунок шахматных плоскостей куба в 2D проекции
        int size = 80;
        int cx = w / 2;
        int cy = h / 2;
        
        // Рисуем плоскости куба с чередованием черных и белых квадратов
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                g.setColor(((i + j) % 2 == 0) ? 255 : 0, ((i + j) % 2 == 0) ? 255 : 0, ((i + j) % 2 == 0) ? 255 : 0);
                g.fillRect(cx - size/2 + (i*20), cy - size/2 + (j*20), 20, 20);
            }
        }
    }

    private void drawDebugOverlay(Graphics g) {
        g.setColor(255, 255, 255);
        g.drawString("FPS: " + currentFps, 5, 5, Graphics.LEFT | Graphics.TOP);
        
        String xCoord = "X: " + cameraController.getPosX();
        String yCoord = "Y: " + cameraController.getPosY();
        String zCoord = "Z: " + cameraController.getPosZ();
        
        int fontHeight = g.getFont().getHeight();
        int sHeight = getHeight();
        g.drawString(xCoord, 5, sHeight - (fontHeight * 3) - 5, Graphics.LEFT | Graphics.TOP);
        g.drawString(yCoord, 5, sHeight - (fontHeight * 2) - 5, Graphics.LEFT | Graphics.TOP);
        g.drawString(zCoord, 5, sHeight - fontHeight - 5, Graphics.LEFT | Graphics.TOP);
    }
    /**
     * Перехват нажатия клавиш кнопочной клавиатуры
     */
    protected void keyPressed(int keyCode) {
        if (isLoading) return;
        
        inputManager.handleKeyDown(keyCode);
        
        // Спецификация ТЗ: Сохранение map.txt переведено строго на клавишу «0» (код 48)
        if (keyCode == Canvas.KEY_NUM0 || keyCode == 48) {
            exportCurrentCoordinates();
        }
    }

    protected void keyReleased(int keyCode) {
        if (isLoading) return;
        inputManager.handleKeyUp(keyCode);
    }

    /**
     * Экспорт текущих целочисленных пространственных координат в файл map.txt
     */
    private void exportCurrentCoordinates() {
        int posX = cameraController.getPosX();
        int posY = cameraController.getPosY();
        int posZ = cameraController.getPosZ();
        
        String exportLine = "x=" + posX + " , y=" + posY + " , z=" + posZ + "\r\n";
        
        FileConnection fc = null;
        OutputStream os = null;
        try {
            fc = (FileConnection) Connector.open(workingDirUri + "map.txt", Connector.READ_WRITE);
            if (!fc.exists()) {
                fc.create();
            }
            
            long currentSize = fc.fileSize();
            os = fc.openOutputStream(currentSize);
            
            byte[] data = exportLine.getBytes("UTF-8");
            os.write(data);
            os.flush();
            
            FileLogger.log("Экспорт по клавише: Успешно записано в map.txt -> " + exportLine.trim());
        } catch (IOException e) {
            FileLogger.log("Ошибка экспорта по клавише: " + e.toString());
        } finally {
            try { if (os != null) os.close(); if (fc != null) fc.close(); } catch (Exception e) {}
        }
    }

    public void stopEngine() {
        this.isRunning = false;
        try {
            if (engineThread != null) {
                engineThread.join();
            }
        } catch (Exception e) {}
    }
}
