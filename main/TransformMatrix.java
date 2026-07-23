package main;

public class TransformMatrix {
    // Ячейки матрицы преобразования 4x3 (3 строки, 4 столбца)
    // Строка 1: m00, m01, m02, m03 (смещение X)
    public int m00 = 16384, m01 = 0,     m02 = 0,     m03 = 0;
    // Строка 2: m10, m11, m12, m13 (смещение Y)
    public int m10 = 0,     m11 = 16384, m12 = 0,     m13 = 0;
    // Строка 3: m20, m21, m22, m23 (смещение Z)
    public int m20 = 0,     m21 = 0,     m22 = 16384, m23 = 0;

    // Статические кэш-таблицы синусов и косинусов для всех 360 градусов
    private static final short[] sineTable = new short[360];
    private static final short[] cosineTable = new short[360];

    static {
        // Предварительный расчет тригонометрии при старте под CLDC 1.1 (Масштаб 2^14 = 16384)
        double scale = 16384.0;
        double radConversion = Math.PI / 180.0;
        for (int i = 0; i < 360; i++) {
            double angleRad = i * radConversion;
            
            // Ручное округление без Math.round() для совместимости с CLDC 1.1
            double sVal = Math.sin(angleRad) * scale;
            sineTable[i] = (short) (sVal >= 0.0 ? (int)(sVal + 0.5) : (int)(sVal - 0.5));
            
            double cVal = Math.cos(angleRad) * scale;
            cosineTable[i] = (short) (cVal >= 0.0 ? (int)(cVal + 0.5) : (int)(cVal - 0.5));
        }
    }

    /**
     * Конструктор: инициализирует единичную матрицу
     */
    public TransformMatrix() {
        setIdentity();
    }

    /**
     * Сбрасывает матрицу к дефолтному единичному состоянию (Identity Matrix)
     */
    public void setIdentity() {
        m00 = 16384; m01 = 0;     m02 = 0;     m03 = 0;
        m10 = 0;     m11 = 16384; m12 = 0;     m13 = 0;
        m20 = 0;     m21 = 0;     m22 = 16384; m23 = 0;
    }

    /**
     * Копирование ячеек из другой матрицы преобразования
     */
    public final void copyFrom(TransformMatrix other) {
        this.m00 = other.m00; this.m01 = other.m01; this.m02 = other.m02; this.m03 = other.m03;
        this.m10 = other.m10; this.m11 = other.m11; this.m12 = other.m12; this.m13 = other.m13;
        this.m20 = other.m20; this.m21 = other.m21; this.m22 = other.m22; this.m23 = other.m23;
    }

    /**
     * Прямой перенос (смещение) полей матрицы без масштабирования
     */
    public final void setTranslationOnly(int tx, int ty, int tz) {
        this.m03 = tx;
        this.m13 = ty;
        this.m23 = tz;
    }

    /**
     * Нормализация входящих углов для безопасной выборки из массивов LUT (0..359)
     */
    private static int normalizeAngle(int angle) {
        while (angle < 0) { angle += 360; }
        while (angle >= 360) { angle -= 360; }
        return angle;
    }
    /**
     * Вращение вокруг оси X (Pitch / Наклон вверх-вниз) по нативной схеме FernFlower
     */
    public final void rotateX(int angle) {
        angle = normalizeAngle(angle);
        this.m00 = 16384; this.m01 = 0; this.m02 = 0; this.m03 = 0;
        this.m10 = 0;
        this.m11 = cosineTable[angle];
        this.m12 = -sineTable[angle];
        this.m13 = 0;
        this.m20 = 0;
        this.m21 = sineTable[angle];
        this.m22 = cosineTable[angle];
        this.m23 = 0;
    }

    /**
     * Вращение вокруг оси Y (Heading / Поворот влево-вправо) по нативной схеме FernFlower [INDEX: 0.1.3]
     */
    public final void rotateY(int angle) {
        angle = normalizeAngle(angle);
        short cos = cosineTable[angle];
        short sin = sineTable[angle];
        int var3 = this.m00 * cos + this.m20 * sin >> 14;
        int var4 = this.m01 * cos + this.m21 * sin >> 14;
        int var5 = this.m02 * cos + this.m22 * sin >> 14;
        int var6 = this.m20 * cos - this.m00 * sin >> 14;
        int var7 = this.m21 * cos - this.m01 * sin >> 14;
        int var8 = this.m22 * cos - this.m02 * sin >> 14;
        
        this.m00 = var3; this.m01 = var4; this.m02 = var5;
        this.m20 = var6; this.m21 = var7; this.m22 = var8;
    }

    /**
     * Перемножение текущей матрицы на матрицу 'other' с нативным 64-битным FIXED-POINT сдвигом смещений [INDEX: 0.1.3, 0.1.4]
     */
    public final void multiply(TransformMatrix other) {
        long var5 = (long)(this.m00 * other.m00 + this.m01 * other.m10 + this.m02 * other.m20) >> 14;
        long var7 = (long)(this.m00 * other.m01 + this.m01 * other.m11 + this.m02 * other.m21) >> 14;
        long var9 = (long)(this.m00 * other.m02 + this.m01 * other.m12 + this.m02 * other.m22) >> 14;
        long var11 = (long)this.m00 * (long)other.m03 + (long)this.m01 * (long)other.m13 + (long)this.m02 * (long)other.m23 + ((long)this.m03 << 14) >> 14;
        
        long var13 = (long)(this.m10 * other.m00 + this.m11 * other.m10 + this.m12 * other.m20) >> 14;
        long var15 = (long)(this.m10 * other.m01 + this.m11 * other.m11 + this.m12 * other.m21) >> 14;
        long var17 = (long)(this.m10 * other.m02 + this.m11 * other.m12 + this.m12 * other.m22) >> 14;
        long var19 = (long)this.m10 * (long)other.m03 + (long)this.m11 * (long)other.m13 + (long)this.m12 * (long)other.m23 + ((long)this.m13 << 14) >> 14;
        
        long var21 = (long)(this.m20 * other.m00 + this.m21 * other.m10 + this.m22 * other.m20) >> 14;
        long var23 = (long)(this.m20 * other.m01 + this.m21 * other.m11 + this.m22 * other.m21) >> 14;
        long var25 = (long)(this.m20 * other.m02 + this.m21 * other.m12 + this.m22 * other.m22) >> 14;
        long var27 = (long)this.m20 * (long)other.m03 + (long)this.m21 * (long)other.m13 + (long)this.m22 * (long)other.m23 + ((long)this.m23 << 14) >> 14;
        
        this.m00 = (int)var5;  this.m01 = (int)var7;  this.m02 = (int)var9;  this.m03 = (int)var11;
        this.m10 = (int)var13; this.m11 = (int)var15; this.m12 = (int)var17; this.m13 = (int)var19;
        this.m20 = (int)var21; this.m21 = (int)var23; this.m22 = (int)var25; this.m23 = (int)var27;
    }

    /**
     * Высокоточная инверсия матрицы вида (View Matrix) по нативному байт-коду FernFlower [INDEX: 0.1.4]
     */
    public final void invert() {
        long var1 = ((long)this.m00 * (long)this.m03 >> 14) + ((long)this.m10 * (long)this.m13 >> 14) + ((long)this.m20 * (long)this.m23 >> 14);
        long var3 = ((long)this.m01 * (long)this.m03 >> 14) + ((long)this.m11 * (long)this.m13 >> 14) + ((long)this.m21 * (long)this.m23 >> 14);
        long var5 = ((long)this.m02 * (long)this.m03 >> 14) + ((long)this.m12 * (long)this.m13 >> 14) + ((long)this.m22 * (long)this.m23 >> 14);
        
        this.m03 = (int)(-var1);
        this.m13 = (int)(-var3);
        this.m23 = (int)(-var5);
        
        // Транспонирование осей вращения матрицы
        int temp = this.m01; this.m01 = this.m10; this.m10 = temp;
        temp = this.m02; this.m02 = this.m20; this.m20 = temp;
        temp = this.m12; this.m12 = this.m21; this.m21 = temp;
    }

    // Быстрый константный доступ к тригонометрическим Look-Up таблицам
    public static int getSineTableValue(int angle) { return sineTable[angle]; }
    public static int getCosineTableValue(int angle) { return cosineTable[angle]; }
}
