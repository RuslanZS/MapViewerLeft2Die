package main;

public class FPSCounter {
    // Временная метка предыдущего кадра в миллисекундах
    private static long lastFrameTime = 0L;
    
    // Накопленное время внутри текущей секунды
    private static long accumulatedTime = 0L;
    
    // Внутренний счетчик отрисованных кадров
    private static int frameCount = 0;
    
    // Итоговое значение кадров в секунду для вывода на экран
    private static int currentFps = 0;

    static {
        // Первичная инициализация временных счетчиков при старте приложения
        lastFrameTime = System.currentTimeMillis();
        accumulatedTime = 0L;
        frameCount = 0;
        currentFps = 0;
    }

    /**
     * Высокоточный расчет кадров в секунду.
     * Метод должен вызываться один раз за итерацию в главном цикле потока (MapCanvas.java).
     * @return Текущее актуальное значение FPS для вывода в HUD-интерфейс
     */
    public static int updateAndGetFps() {
        // Получаем системное время в текущий момент
        long currentTime = System.currentTimeMillis();
        
        // Вычисляем, сколько миллисекунд прошло с момента отрисовки предыдущего кадра
        long elapsedTime = currentTime - lastFrameTime;
        
        // Запоминаем текущее время как базовую точку для следующего кадра
        lastFrameTime = currentTime;
        
        // Увеличиваем внутренний счетчик кадров на единицу
        frameCount++;
        
        // Добавляем прошедшее время в общую копилку текущей секунды
        accumulatedTime += elapsedTime;

        // Если суммарно накопилась 1 секунда (1000 миллисекунд) или больше
        if (accumulatedTime >= 1000L) {
            // Фиксируем накопленные кадры как итоговый показатель производительности
            currentFps = frameCount;
            
            // Сбрасываем внутренние счетчики для начала расчета новой секунды
            frameCount = 0;
            accumulatedTime = 0L;
        }
        
        // Возвращаем последнее стабильное значение FPS
        return currentFps;
    }

    /**
     * Принудительный сброс счетчика (например, при загрузке новой карты, 
     * чтобы долгое чтение файлов не искажало первый замер производительности).
     */
    public static void reset() {
        lastFrameTime = System.currentTimeMillis();
        accumulatedTime = 0L;
        frameCount = 0;
    }
}
