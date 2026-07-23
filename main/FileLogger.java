package main;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import java.io.OutputStream;
import java.io.IOException;

public class FileLogger {
    private static String baseFolderUri = null;
    private static final String LOG_FILE_NAME = "map_log.txt";

    /**
     * Инициализация логгера URI выбранной рабочей папки
     * @param path Полный URI папки (например, "file:///e:/other/3dMap/")
     */
    public static void init(String path) {
        baseFolderUri = path;
        
        // При каждой новой инициализации стираем старый лог и создаем новый файл
        FileConnection fc = null;
        try {
            fc = (FileConnection) Connector.open(baseFolderUri + LOG_FILE_NAME, Connector.READ_WRITE);
            if (fc.exists()) {
                fc.delete();
            }
            fc.create();
        } catch (Exception e) {
            System.out.println("FileLogger init critical error: " + e.toString());
        } finally {
            closeFileConnection(fc);
        }
    }

    /**
     * Запись строкового сообщения в лог-файл map_log.txt в кодировке UTF-8 (Режим Append)
     * @param message Текст для записи
     */
    public static void log(String message) {
        if (baseFolderUri == null) {
            System.out.println("[Console Only]: " + message);
            return;
        }

        // Дублируем вывод в консоль AIDE/эмулятора для удобства отладки
        System.out.println("[MapLog]: " + message);

        FileConnection fc = null;
        OutputStream os = null;
        try {
            fc = (FileConnection) Connector.open(baseFolderUri + LOG_FILE_NAME, Connector.READ_WRITE);
            if (!fc.exists()) {
                fc.create();
            }
            
            // Встаем в конец файла для дозаписи данных
            long fileSize = fc.fileSize();
            os = fc.openOutputStream(fileSize);
            
            String logLine = "[" + System.currentTimeMillis() + "] " + message + "\r\n";
            
            // Спецификация ТЗ: принудительная кодировка UTF-8 для логов
            byte[] data = null;
            try {
                data = logLine.getBytes("UTF-8");
            } catch (java.io.UnsupportedEncodingException ex) {
                data = logLine.getBytes(); // Запасной фоллбэк
            }
            
            os.write(data);
            os.flush();
            
        } catch (Exception e) {
            System.out.println("Failed to write to map_log.txt: " + e.toString());
        } finaly {
            closeOutputStream(os);
            closeFileConnection(fc);
        }
    }
    /**
     * Быстрая проверка существования файла в выбранной рабочей директории
     * @param fileName Имя проверяемого файла (например, "setting.txt")
     * @return true, если файл существует, иначе false
     */
    public static boolean checkFileExists(String fileName) {
        if (baseFolderUri == null) {
            return false;
        }
        
        // Корректируем начальные слэши из старых конфигурационных файлов
        if (fileName.startsWith("/")) {
            fileName = fileName.substring(1);
        }

        FileConnection fc = null;
        boolean exists = false;
        try {
            fc = (FileConnection) Connector.open(baseFolderUri + fileName, Connector.READ);
            exists = fc.exists();
        } catch (Exception e) {
            System.out.println("Error check file " + fileName + ": " + e.toString());
            exists = false;
        } finally {
            closeFileConnection(fc);
        }
        return exists;
    }

    private static void closeOutputStream(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {}
        }
    }

    private static void closeFileConnection(FileConnection fc) {
        if (fc != null) {
            try {
                fc.close();
            } catch (IOException e) {}
        }
    }
}
