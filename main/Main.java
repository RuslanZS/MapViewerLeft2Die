package main;

import javax.microedition.midlet.MIDlet;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public class Main extends MIDlet implements CommandListener {
    private Display display;
    private Command exitCommand;
    private RecordStore store;
    
    // Системные переменные для хранения состояния JSR-75
    private boolean isJsr75Supported = false;
    private String selectedPath = null;
    
    public Main() {
        display = Display.getDisplay(this);
        // Правая софт-клавиша "Выход" как в оригинальной игре
        exitCommand = new Command("Выход", Command.EXIT, 1);
    }
    
    protected void startApp() {
        if (display == null) {
            display = Display.getDisplay(this);
        }
        initAppLogic();
    }
    
    protected void pauseApp() {
        // Стандартный метод паузы MIDlet
    }
    
    protected void destroyApp(boolean unconditional) {
        closeRecordStore();
    }
    
    /**
     * Главная логика проверки RMS хранилища sysMap при старте
     */
    private void initAppLogic() {
        try {
            store = RecordStore.openRecordStore("sysMap", true);
            
            // Если записей нет — это первый запуск приложения
            if (store.getNumRecords() == 0) {
                checkAndInitFirstRun();
            } else {
                // Если записи есть — загружаем путь из RMS
                loadStoredConfig();
            }
        } catch (RecordStoreException e) {
            // Резервный запуск при повреждении RMS
            checkAndInitFirstRun();
        }
    }
    /**
     * Логика первого запуска: тест JSR-75 и вызов PathSelector
     */
    private void checkAndInitFirstRun() {
        String fileConnProp = System.getProperty("microedition.io.file.FileConnection");
        isJsr75Supported = (fileConnProp != null);
        
        if (!isJsr75Supported) {
            // Вывод оригинального предупреждения и блокировка работы
            showNoJsr75Alert();
        } else {
            // Сохраняем маркер успешной проверки JSR-75 в RMS
            saveStatusToRms("JSR75_OK");
            openPathSelector();
        }
    }
    
    /**
     * Загрузка сохранённого пути из RMS при повторных запусках
     */
    private void loadStoredConfig() {
        try {
            String status = new String(store.getRecord(1));
            isJsr75Supported = "JSR75_OK".equals(status);
            
            if (!isJsr75Supported) {
                showNoJsr75Alert();
                return;
            }
            
            if (store.getNumRecords() >= 2) {
                selectedPath = new String(store.getRecord(2));
                
                // Инициализируем логгер в выбранной папке JSR-75
                FileLogger.init(selectedPath);
                FileLogger.log("Приложение запущено. Загружен путь из RMS: " + selectedPath);
                
                // Запускаем основной холст (он сам переключится на экран загрузки)
                launchMapCanvas();
            } else {
                openPathSelector();
            }
        } catch (Exception e) {
            resetRecordStore();
            checkAndInitFirstRun();
        }
    }
    
    /**
     * Диалоговое окно отсутствия JSR-75 с правой софт-клавишей Выход
     */
    private void showNoJsr75Alert() {
        Alert alert = new Alert("Ошибка", "Ваш телефон не поддерживает jsr75", null, AlertType.ERROR);
        alert.addCommand(exitCommand);
        alert.setCommandListener(this);
        alert.setTimeout(Alert.FOREVER);
        display.setCurrent(alert);
    }
    private void openPathSelector() {
        PathSelector selector = new PathSelector(this);
        display.setCurrent(selector);
    }
    
    /**
     * Обратный вызов (Callback) из PathSelector после выбора рабочей папки
     */
    public void onPathSelected(String path) {
        this.selectedPath = path;
        savePathToRms(path);
        
        FileLogger.init(selectedPath);
        FileLogger.log("Выбран рабочий путь: " + selectedPath);
        
        launchMapCanvas();
    }
    
    /**
     * Инициализация и запуск основного 3D холста MapCanvas
     */
    private void launchMapCanvas() {
        try {
            MapCanvas mapCanvas = new MapCanvas(this, selectedPath);
            mapCanvas.addCommand(exitCommand);
            mapCanvas.setCommandListener(this);
            display.setCurrent(mapCanvas);
        } catch (Exception e) {
            // Ошибки инициализации пишем в файл через FileLogger
            FileLogger.log("Критическая ошибка запуска MapCanvas: " + e.toString());
        }
    }
    
    /**
     * Обработчик софт-клавиш для системных экранов (Alert)
     */
    public void commandAction(Command c, Displayable d) {
        if (c == exitCommand) {
            closeRecordStore();
            notifyDestroyed();
        }
    }
    
    // Вспомогательные методы сохранения данных в RecordStore
    
    private void saveStatusToRms(String status) {
        try {
            byte[] data = status.getBytes();
            if (store.getNumRecords() == 0) {
                store.addRecord(data, 0, data.length);
            } else {
                store.setRecord(1, data, 0, data.length);
            }
        } catch (Exception e) {}
    }
    
    private void savePathToRms(String path) {
        try {
            byte[] data = path.getBytes();
            if (store.getNumRecords() < 2) {
                store.addRecord(data, 0, data.length);
            } else {
                store.setRecord(2, data, 0, data.length);
            }
        } catch (Exception e) {}
    }
    
    private void closeRecordStore() {
        try {
            if (store != null) {
                store.closeRecordStore();
                store = null;
            }
        } catch (Exception e) {}
    }
    
    private void resetRecordStore() {
        closeRecordStore();
        try {
            RecordStore.deleteRecordStore("sysMap");
        } catch (Exception e) {}
    }
}
