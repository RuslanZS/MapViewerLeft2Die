package main;

import javax.microedition.lcdui.Canvas;

public class InputManager {
    private final Canvas canvas;

    // Битовые флаги игровых действий (состояния кнопок)
    public boolean moveForward = false;
    public boolean moveBackward = false;
    public boolean turnLeft = false;
    public boolean turnRight = false;
    public boolean strafeLeft = false;
    public boolean strafeRight = false;
    public boolean lookUp = false;
    public boolean lookDown = false;

    // Системные скан-коды клавиш, определяемые динамически
    private int keyUpCode = 0;
    private int keyDownCode = 0;
    private int keyLeftCode = 0;
    private int keyRightCode = 0;
    private int keySelectCode = 0;

    /**
     * Конструктор менеджера ввода. Выполняет нативное автоопределение раскладки телефона.
     * @param canvas Ссылка на графический холст MapCanvas
     */
    public InputManager(Canvas canvas) {
        this.canvas = canvas;
        detectDeviceKeycodes();
    }

    /**
     * Алгоритм автоматического определения кодов кнопок под разные модели телефонов.
     * Восстановлен строго по логике FernFlower на основе текстовых имен клавиш.
     */
    private void detectDeviceKeycodes() {
        try {
            // Проверяем имя клавиши для стандартного кода Nokia SELECT (-26)
            String keyName26 = canvas.getKeyName(-26);
            if (keyName26 != null && keyName26.toUpperCase().indexOf("SELECT") != -1) {
                FileLogger.log("InputManager: Найдена аппаратная раскладка Nokia.");
                keyUpCode = -60;     // Стрелка Вверх
                keyDownCode = -61;   // Стрелка Вниз
                keyLeftCode = -59;   // Стрелка Влево
                keyRightCode = -62;  // Стрелка Вправо
                keySelectCode = -26; // Центральная кнопка выбора
                return;
            }

            // Проверяем имя клавиши для стандартного кода Sony Ericsson SELECT (-20)
            String keyName20 = canvas.getKeyName(-20);
            if (keyName20 != null && keyName20.toUpperCase().indexOf("SELECT") != -1) {
                FileLogger.log("InputManager: Найдена аппаратная раскладка Sony Ericsson.");
                keyUpCode = -1;      // Стрелка Вверх
                keyDownCode = -2;    // Стрелка Вниз
                keyLeftCode = -3;    // Стрелка Влево
                keyRightCode = -4;   // Стрелка Вправо
                keySelectCode = -20; // Центральная кнопка выбора
                return;
            }
        } catch (IllegalArgumentException e) {
            // Игнорируем исключения на неподдерживаемые отрицательные коды
        }

        // Дефолтная раскладка по стандарту Java ME MIDP 2.0 (Motorola, Siemens, LG, Эмуляторы)
        FileLogger.log("InputManager: Настроена стандартная раскладка MIDP 2.0 Java ME.");
        try {
            keyUpCode = canvas.getKeyCode(Canvas.UP);
            keyDownCode = canvas.getKeyCode(Canvas.DOWN);
            keyLeftCode = canvas.getKeyCode(Canvas.LEFT);
            keyRightCode = canvas.getKeyCode(Canvas.RIGHT);
            keySelectCode = canvas.getKeyCode(Canvas.FIRE);
        } catch (Exception e) {
            // Аварийные хардкод-значения на случай тотального сбоя ОС телефона
            keyUpCode = -1;
            keyDownCode = -2;
            keyLeftCode = -3;
            keyRightCode = -4;
            keySelectCode = -5;
        }
    }
    /**
     * Вызывается из MapCanvas.keyPressed(). Выставляет битовые флаги в true.
     * Реализовано по FernFlower: обзор вверх/вниз перенесен на клавиши «5» и «7».
     * @param keyCode Код нажатой клавиши
     */
    public void handleKeyDown(int keyCode) {
        // Движение Вперед: Стрелка Вверх или Кнопка '2'
        if (keyCode == keyUpCode || keyCode == Canvas.KEY_NUM2 || keyCode == 50) {
            moveForward = true;
        }
        // Движение Назад: Стрелка Вниз или Кнопка '8'
        else if (keyCode == keyDownCode || keyCode == Canvas.KEY_NUM8 || keyCode == 56) {
            moveBackward = true;
        }
        // Поворот Влево: Стрелка Влево или Кнопка '4'
        else if (keyCode == keyLeftCode || keyCode == Canvas.KEY_NUM4 || keyCode == 52) {
            turnLeft = true;
        }
        // Поворот Вправо: Стрелка Вправо или Кнопка '6'
        else if (keyCode == keyRightCode || keyCode == Canvas.KEY_NUM6 || keyCode == 54) {
            turnRight = true;
        }
        // Стрейф Влево: Кнопка '1'
        else if (keyCode == Canvas.KEY_NUM1 || keyCode == 49) {
            strafeLeft = true;
        }
        // Стрейф Вправо: Кнопка '3'
        else if (keyCode == Canvas.KEY_NUM3 || keyCode == 51) {
            strafeRight = true;
        }
        // Оригинальный обзор Вверх по FernFlower: Кнопка '5'
        else if (keyCode == Canvas.KEY_NUM5 || keyCode == 53) {
            lookUp = true;
        }
        // Оригинальный обзор Вниз по FernFlower: Кнопка '7'
        else if (keyCode == Canvas.KEY_NUM7 || keyCode == 55) {
            lookDown = true;
        }
    }

    /**
     * Вызывается из MapCanvas.keyReleased(). Сбрасывает битовые флаги в false.
     * @param keyCode Код отпущенной клавиши
     */
    public void handleKeyUp(int keyCode) {
        if (keyCode == keyUpCode || keyCode == Canvas.KEY_NUM2 || keyCode == 50) {
            moveForward = false;
        }
        else if (keyCode == keyDownCode || keyCode == Canvas.KEY_NUM8 || keyCode == 56) {
            moveBackward = false;
        }
        else if (keyCode == keyLeftCode || keyCode == Canvas.KEY_NUM4 || keyCode == 52) {
            turnLeft = false;
        }
        else if (keyCode == keyRightCode || keyCode == Canvas.KEY_NUM6 || keyCode == 54) {
            turnRight = false;
        }
        else if (keyCode == Canvas.KEY_NUM1 || keyCode == 49) {
            strafeLeft = false;
        }
        else if (keyCode == Canvas.KEY_NUM3 || keyCode == 51) {
            strafeRight = false;
        }
        else if (keyCode == Canvas.KEY_NUM5 || keyCode == 53) {
            lookUp = false;
        }
        else if (keyCode == Canvas.KEY_NUM7 || keyCode == 55) {
            lookDown = false;
        }
    }

    /**
     * Сброс всех флагов ввода при выходе или переключении экранов
     */
    public void resetAllInputs() {
        moveForward = false;
        moveBackward = false;
        turnLeft = false;
        turnRight = false;
        strafeLeft = false;
        strafeRight = false;
        lookUp = false;
        lookDown = false;
    }
}
