package main;

public class CameraController {
    // Текущие пространственные координаты камеры (в обычном целочисленном масштабе мира)
    private int posX = 0;
    private int posY = 0;
    private int posZ = -40; // Стартовое смещение, чтобы объект был прямо перед глазами

    // Углы поворота камеры (в градусах от 0 до 359)
    private int angleHeading = 0; // Поворот влево/вправо (вокруг оси Y)
    private int anglePitch = 0;   // Наклон вверх/вниз (вокруг оси X)

    // Предыдущие безопасные координаты для отката столкновений
    private int prevX = 0;
    private int prevY = 0;
    private int prevZ = 0;

    // Матрицы трансформации для рендерера
    private final TransformMatrix viewMatrix;
    private final TransformMatrix skyMatrix;

    /**
     * Конструктор контроллера камеры
     */
    public CameraController() {
        this.viewMatrix = new TransformMatrix();
        this.skyMatrix = new TransformMatrix();
        
        this.posX = 0;
        this.posY = 0; 
        this.posZ = -40;
    }

    /**
     * Обработка перемещения на основе флагов из InputManager.
     * Реализует тригонометрический шаг с нормализацией фиксированной точки (>> 14).
     * @param input Ссылка на менеджер ввода с флагами зажатых кнопок
     */
    public void handleMovement(InputManager input) {
        // Сохраняем текущие координаты на случай отката при столкновении
        this.prevX = this.posX;
        this.prevY = this.posY;
        this.prevZ = this.posZ;

        int moveSpeed = 4; 
        int turnSpeed = 3;

        // 1. Повороты направления взгляда (Влево / Вправо)
        if (input.turnLeft) {
            this.angleHeading = (this.angleHeading - turnSpeed + 360) % 360;
        }
        if (input.turnRight) {
            this.angleHeading = (this.angleHeading + turnSpeed) % 360;
        }

        // 2. Наклоны головы (Вверх / Вниз)
        if (input.lookUp) {
            this.anglePitch = (this.anglePitch - turnSpeed + 360) % 360;
            // Ограничение взгляда (Look clamping), предотвращающее Gimbal Lock
            if (this.anglePitch > 80 && this.anglePitch < 180) this.anglePitch = 80;
        }
        if (input.lookDown) {
            this.anglePitch = (this.anglePitch + turnSpeed) % 360;
            if (this.anglePitch < 280 && this.anglePitch > 180) this.anglePitch = 280;
        }

        // Извлекаем значения синусов и косинусов из таблицы LUT матрицы
        int sinH = TransformMatrix.getSineTableValue(this.angleHeading);
        int cosH = TransformMatrix.getCosineTableValue(this.angleHeading);

        // 3. Движение Вперед / Назад (Применяем >> 14 для нормализации масштаба мира)
        if (input.moveForward) {
            this.posX += (sinH * moveSpeed) >> 14;
            this.posZ += (cosH * moveSpeed) >> 14;
        }
        if (input.moveBackward) {
            this.posX -= (sinH * moveSpeed) >> 14;
            this.posZ -= (cosH * moveSpeed) >> 14;
        }

        // 4. Движение Боком (Стрейф Влево / Вправо)
        if (input.strafeLeft) {
            int sinStrafe = TransformMatrix.getSineTableValue((this.angleHeading - 90 + 360) % 360);
            int cosStrafe = TransformMatrix.getCosineTableValue((this.angleHeading - 90 + 360) % 360);
            this.posX += (sinStrafe * moveSpeed) >> 14;
            this.posZ += (cosStrafe * moveSpeed) >> 14;
        }
        if (input.strafeRight) {
            int sinStrafe = TransformMatrix.getSineTableValue((this.angleHeading + 90) % 360);
            int cosStrafe = TransformMatrix.getCosineTableValue((this.angleHeading + 90) % 360);
            this.posX += (sinStrafe * moveSpeed) >> 14;
            this.posZ += (cosStrafe * moveSpeed) >> 14;
        }
    }
    /**
     * Обработка столкновений с границами секторов.
     * Если игрок вышел за стену, откатываем координаты.
     * @param currentSector Индекс текущей комнаты
     */
    public void updateSectorCollision(int currentSector) {
        if (currentSector == -1) {
            this.posX = this.prevX;
            this.posY = this.prevY;
            this.posZ = this.prevZ;
        }
    }

    /**
     * Генерация финальной матрицы вида (View Matrix) по нативной схеме FernFlower.
     * Сбрасывает вращение и записывает чистые координаты напрямую в поля смещения.
     */
    public TransformMatrix getViewMatrix() {
        viewMatrix.m00 = 16384; viewMatrix.m01 = 0;     viewMatrix.m02 = 0;     viewMatrix.m03 = 0;
        viewMatrix.m10 = 0;     viewMatrix.m11 = 16384; viewMatrix.m12 = 0;     viewMatrix.m13 = 0;
        viewMatrix.m20 = 0;     viewMatrix.m21 = 0;     viewMatrix.m22 = 16384; viewMatrix.m23 = 0;
        
        viewMatrix.rotateY((360 - this.angleHeading) % 360);
        viewMatrix.rotateX((360 - this.anglePitch) % 360);
        
        // Передаем чистую целочисленную позицию напрямую в поля смещения матрицы (ТЗ / FernFlower)
        viewMatrix.setTranslationOnly(-this.posX, -this.posY, -this.posZ);
        return viewMatrix;
    }

    /**
     * Генерация матрицы вращения неба (Skybox Matrix).
     * Вращается вместе с головой игрока, но аппаратно фиксирует трансляцию в центре (0,0,0).
     */
    public TransformMatrix getSkyRotationMatrix() {
        skyMatrix.m00 = 16384; skyMatrix.m01 = 0;     skyMatrix.m02 = 0;     skyMatrix.m03 = 0;
        skyMatrix.m10 = 0;     skyMatrix.m11 = 16384; skyMatrix.m12 = 0;     skyMatrix.m13 = 0;
        skyMatrix.m20 = 0;     skyMatrix.m21 = 0;     skyMatrix.m22 = 16384; skyMatrix.m23 = 0;
        
        skyMatrix.rotateY((360 - this.angleHeading) % 360);
        skyMatrix.rotateX((360 - this.anglePitch) % 360);
        
        skyMatrix.setTranslationOnly(0, 0, 0);
        return skyMatrix;
    }

    // Геттеры и сеттеры пространственных целочисленных координат
    
    public int getPosX() { return this.posX; }
    public int getPosY() { return this.posY; }
    public int getPosZ() { return this.posZ; }

    public void setPosition(int x, int y, int z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }
}
