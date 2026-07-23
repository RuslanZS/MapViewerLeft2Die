package mesh;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.Hashtable;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

public class ConfigAndMeshLoader {
    private final Hashtable configTable;
    private final String workingDirUri;

    /**
     * Конструктор загрузчика ресурсов. Сразу запускает парсинг файла настроек.
     * @param workingDirUri Корневой URI папки JSR-75 на флеш-карте
     */
    public ConfigAndMeshLoader(String workingDirUri) {
        this.workingDirUri = workingDirUri;
        this.configTable = new Hashtable();
        parseSettingsFile("setting.txt");
    }

    /**
     * Низкоуровневый разбор текстового файла конфигурации setting.txt
     */
    private void parseSettingsFile(String fileName) {
        FileConnection fc = null;
        InputStream is = null;
        try {
            fc = (FileConnection) Connector.open(workingDirUri + fileName, Connector.READ);
            if (!fc.exists()) {
                return;
            }
            
            is = fc.openInputStream();
            int size = (int) fc.fileSize();
            byte[] buffer = new byte[size];
            int read = 0;
            while (read < size) {
                int result = is.read(buffer, read, size - read);
                if (result == -1) break;
                read += result;
            }

            // Переводим Windows-1251 байты в Unicode через нашу системную утилиту
            String rawText = main.TextParserUtils.bytesToUnicodeString(buffer);
            String[] lines = main.TextParserUtils.splitString(rawText, '\n');

            for (int i = 0; i < lines.length; i++) {
                String line = lines[i].trim();
                if (line.length() == 0 || line.startsWith("#") || line.startsWith("//")) {
                    continue;
                }
                
                int equalsIdx = line.indexOf('=');
                if (equalsIdx > 0) {
                    String key = line.substring(0, equalsIdx).trim().toUpperCase();
                    String value = line.substring(equalsIdx + 1).trim();
                    configTable.put(key, value);
                }
            }
        } catch (Exception e) {
            main.FileLogger.log("Ошибка парсинга настроек setting.txt: " + e.toString());
        } finally {
            try { if (is != null) is.close(); if (fc != null) fc.close(); } catch (Exception e) {}
        }
    }

    public String getStringProperty(String key, String defaultValue) {
        String val = (String) configTable.get(key.toUpperCase());
        return val != null ? val : defaultValue;
    }

    public float getFloatProperty(String key, float defaultValue) {
        String val = (String) configTable.get(key.toUpperCase());
        if (val != null) {
            try { return Float.parseFloat(val); } catch (Exception e) {}
        }
        return defaultValue;
    }
    /**
     * Динамическая загрузка картинок PNG средствами рефлексии (ТЗ).
     * Если пакет tex отсутствует, перехватывает ClassNotFoundException и возвращает null.
     */
    public Object loadTextureWithFallback(String paramName) {
        String fileName = getStringProperty(paramName, "");
        if (fileName.startsWith("/")) {
            fileName = fileName.substring(1);
        }
        
        boolean isSky = paramName.toUpperCase().equals("SKY_TEXTURE");
        
        // Перехватываем отсутствие пакета tex на самом раннем этапе
        try {
            Class.forName("tex.TextureRasterizer");
        } catch (Throwable t) {
            // Если классов текстур нет, возвращаем null для Wireframe режима (ТЗ)
            return null;
        }

        // Если файл отсутствует на диске — включаем процедурный fallback генератор из tex.notex
        if (fileName.length() == 0 || !main.FileLogger.checkFileExists(fileName)) {
            main.FileLogger.log("Текстура '" + fileName + "' не найдена. Включен рефлексивный fallback.");
            try {
                Class notexClass = Class.forName("tex.notex");
                String methodName = isSky ? "createSkyFallbackTexture" : "createMainFallbackTexture";
                return notexClass.getMethod(methodName, null).invoke(null, null);
            } catch (Exception ex) {
                return null;
            }
        }

        FileConnection fc = null;
        InputStream is = null;
        try {
            fc = (FileConnection) Connector.open(workingDirUri + fileName, Connector.READ);
            is = fc.openInputStream();
            
            // Вызываем статический фабричный метод TextureRasterizer.loadFromStream() через рефлексию
            Class texClass = Class.forName("tex.TextureRasterizer");
            java.lang.reflect.Method loadMethod = texClass.getMethod("loadFromStream", new Class[]{InputStream.class});
            return loadMethod.invoke(null, new Object[]{is});
        } catch (Exception e) {
            main.FileLogger.log("Ошибка чтения файла текстуры " + fileName + ": " + e.toString());
            try {
                return Class.forName("tex.notex").getMethod(isSky ? "createSkyFallbackTexture" : "createMainFallbackTexture", null).invoke(null, null);
            } catch (Exception ex) { return null; }
        } finally {
            try { if (is != null) is.close(); if (fc != null) fc.close(); } catch (Exception e) {}
        }
    }
    /**
     * Побайтовый разбор бинарного формата 3D моделей по нативному эталону FernFlower [INDEX: 0.1.15, 0.1.16].
     */
    public MeshGeometry loadMeshWithFallback(String paramName, Object fallbackTex) {
        String fileName = getStringProperty(paramName, "");
        if (fileName.startsWith("/")) {
            fileName = fileName.substring(1);
        }
        
        boolean isSky = paramName.toUpperCase().equals("SKY_MODEL");
        float scale = getFloatProperty("SCALE", 1.0f);

        // Если файл геометрии отсутствует на диске — отдаем процедурный куб no3d
        if (fileName.length() == 0 || !main.FileLogger.checkFileExists(fileName)) {
            main.FileLogger.log("Модель '" + fileName + "' не найдена. Включен кубический fallback.");
            return isSky ? no3d.createSkyCube(fallbackTex) : no3d.createModelCube(fallbackTex);
        }

        FileConnection fc = null;
        DataInputStream dis = null;
        try {
            fc = (FileConnection) Connector.open(workingDirUri + fileName, Connector.READ);
            dis = new DataInputStream(fc.openInputStream());

            // ХАК БАЙТ-СДВИГА: untitled.3d имеет в начале лишний пустой short (00 00), отсекаем его [INDEX: 0.1.2]
            dis.mark(4);
            int checkHeader = dis.readShort();
            if (checkHeader != 0) {
                dis.reset(); // Если это skybox.3d без заголовка, возвращаем указатель в корень [INDEX: 0.1.1]
            }

            // А. Чтение количества вершин (2 байта short по FernFlower) [INDEX: 0.1.15]
            int vertexCount = dis.readShort();
            Vertex3D[] vertices = new Vertex3D[vertexCount];
            for (int i = 0; i < vertexCount; i++) {
                int x = (int) (dis.readShort() * scale);
                int y = (int) (dis.readShort() * scale);
                int z = (int) (dis.readShort() * scale);
                vertices[i] = new Vertex3D(x, y, z);
            }

            // Б. Чтение треугольников (Строго 12 байт на полигон: 3 short + 6 byte UV) [INDEX: 0.1.15]
            int triangleCount = dis.readShort();
            TexturedTriangle[] triangles = new TexturedTriangle[triangleCount];
            for (int i = 0; i < triangleCount; i++) {
                int v1Idx = dis.readShort();
                int v2Idx = dis.readShort();
                int v3Idx = dis.readShort();
                byte uv0 = dis.readByte(); byte uv1 = dis.readByte();
                byte uv2 = dis.readByte(); byte uv3 = dis.readByte();
                byte uv4 = dis.readByte(); byte uv5 = dis.readByte();
                
                // [НА ТИВНЫЙ ФЛ И П ВЕРШИН FernFlower]: Разворачиваем индексы ребер под Back-Face Culling [INDEX: 0.1.10, 0.1.15]
                triangles[i] = new TexturedTriangle(
                    vertices[v3Idx], vertices[v2Idx], vertices[v1Idx], 
                    uv4, uv5, uv2, uv3, uv0, uv1
                );
            }

            // В. Чтение четырехугольников (Строго 16 байт на полигон: 4 short + 8 byte UV) [INDEX: 0.1.15, 0.1.16]
            int quadCount = dis.readShort();
            TexturedQuad[] quads = new TexturedQuad[quadCount];
            for (int i = 0; i < quadCount; i++) {
                int v1Idx = dis.readShort();
                int v2Idx = dis.readShort();
                int v3Idx = dis.readShort();
                int v4Idx = dis.readShort();
                byte uv0 = dis.readByte(); byte uv1 = dis.readByte();
                byte uv2 = dis.readByte(); byte uv3 = dis.readByte();
                byte uv4 = dis.readByte(); byte uv5 = dis.readByte();
                byte uv6 = dis.readByte(); byte uv7 = dis.readByte();
                
                quads[i] = new TexturedQuad(
                    vertices[v4Idx], vertices[v3Idx], vertices[v2Idx], vertices[v1Idx], 
                    uv6, uv7, uv4, uv5, uv2, uv3, uv0, uv1
                );
            }

            // Объединяем полученные примитивы в кадровый массив геометрии
            int totalPolys = triangleCount + quadCount;
            BasePolygon[] polygons = new BasePolygon[totalPolys];
            if (triangleCount > 0) System.arraycopy(triangles, 0, polygons, 0, triangleCount);
            if (quadCount > 0) System.arraycopy(quads, 0, polygons, triangleCount, quadCount);

            main.FileLogger.log("Успешный импорт 3D-модели [" + fileName + "] Вершин: " + vertexCount + " Полигонов: " + totalPolys);
            return new MeshGeometry(vertices, polygons, fallbackTex);

        } catch (Exception e) {
            main.FileLogger.log("Критическая ошибка разбора формата .3d у файла " + fileName + ": " + e.toString());
            return isSky ? no3d.createSkyCube(fallbackTex) : no3d.createModelCube(fallbackTex);
        } finally {
            try { if (dis != null) dis.close(); if (fc != null) fc.close(); } catch (Exception e) {}
        }
    }
}
