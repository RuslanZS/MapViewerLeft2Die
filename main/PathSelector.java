package main;

import javax.microedition.lcdui.List;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.io.file.FileSystemRegistry;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.Connector;
import java.util.Enumeration;
import java.io.IOException;

public class PathSelector extends List implements CommandListener {
    private final Main midlet;
    
    // Команды управления навигацией
    private final Command selectCommand;
    private final Command openCommand;
    private final Command backCommand;
    
    // Текущий абсолютный URI файловой системы
    private String currentUri;
    private boolean isAtRoot;

    /**
     * Конструктор экрана проводника для выбора рабочей папки
     * @param midlet Ссылка на главный MIDlet для возврата выбранного URI
     */
    public PathSelector(Main midlet) {
        super("Выберите папку карт", List.IMPLICIT);
        this.midlet = midlet;
        
        // Инициализация софт-клавиш
        selectCommand = new Command("Выбрать", Command.OK, 1);
        openCommand = new Command("Открыть", Command.ITEM, 2);
        backCommand = new Command("Назад", Command.BACK, 3);
        
        addCommand(selectCommand);
        addCommand(openCommand);
        addCommand(backCommand);
        
        setCommandListener(this);
        
        // Сканируем корень устройства при старте
        showRoots();
    }

    /**
     * Сканирование и вывод всех доступных накопителей устройства (c:/, e:/, MMC)
     */
    private void showRoots() {
        deleteAll();
        currentUri = "file:///";
        isAtRoot = true;
        
        try {
            Enumeration roots = FileSystemRegistry.listRoots();
            while (roots.hasMoreElements()) {
                String root = (String) roots.nextElement();
                append(root, null);
            }
        } catch (Exception e) {
            append("Ошибка чтения накопителей", null);
        }
        
        // В самом корне устройств кнопка "Назад" не нужна
        removeCommand(backCommand);
    }
    /**
     * Сканирование и вывод папок внутри выбранной директории
     * @param uri Полный путь к папке (URI)
     */
    private void showDirectory(String uri) {
        FileConnection fc = null;
        try {
            fc = (FileConnection) Connector.open(uri, Connector.READ);
            if (!fc.exists() || !fc.isDirectory()) {
                return;
            }
            
            deleteAll();
            currentUri = uri;
            isAtRoot = false;
            
            // Если ушли из корня — возвращаем софт-кнопку "Назад"
            addCommand(backCommand);
            
            Enumeration fileList = fc.list("*", false);
            while (fileList.hasMoreElements()) {
                String name = (String) fileList.nextElement();
                // Фильтруем контент: выводим только папки (в JSR-75 их имена оканчиваются на '/')
                if (name.endsWith("/")) {
                    append(name, null);
                }
            }
            
            if (size() == 0) {
                append("[Папка пуста]", null);
            }
            
        } catch (IOException e) {
            deleteAll();
            append("Доступ запрещен или ошибка", null);
        } finally {
            closeFileConnection(fc);
        }
    }

    /**
     * Метод возврата на один уровень вверх по дереву папок
     */
    private void goBack() {
        if (isAtRoot) {
            return;
        }
        
        // Отсекаем последний слэш для поиска родительского каталога
        String cleanUri = currentUri.substring(0, currentUri.length() - 1);
        int lastSlashIdx = cleanUri.lastIndexOf('/');
        
        // Если слэш относится к протоколу "file:///", возвращаемся к корню накопителей
        if (lastSlashIdx < 7) { 
            showRoots();
        } else {
            String parentUri = currentUri.substring(0, lastSlashIdx + 1);
            showDirectory(parentUri);
        }
    }

    /**
     * Обработка кликов по элементам списка и софт-клавишам
     */
    public void commandAction(Command c, Displayable d) {
        int selectedIdx = getSelectedIndex();
        
        if (c == openCommand || c == List.SELECT_COMMAND) {
            if (selectedIdx < 0) return;
            
            String selectedItem = getString(selectedIdx);
            if (selectedItem.equals("[Папка пуста]") || 
                selectedItem.equals("Ошибка чтения накопителей") || 
                selectedItem.equals("Доступ запрещен или ошибка")) {
                return;
            }
            
            if (isAtRoot) {
                showDirectory("file:///" + selectedItem);
            } else {
                showDirectory(currentUri + selectedItem);
            }
        } 
        else if (c == backCommand) {
            goBack();
        } 
        else if (c == selectCommand) {
            if (isAtRoot) {
                return; // Нельзя выбрать корень устройств в качестве рабочей директории
            }
            midlet.onPathSelected(currentUri);
        }
    }

    private void closeFileConnection(FileConnection fc) {
        if (fc != null) {
            try {
                fc.close();
            } catch (IOException e) {}
        }
    }
}
