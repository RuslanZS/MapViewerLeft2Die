package main;

public class TextParserUtils {

    /**
     * Преобразует сырой массив байт (в однобайтовой кодировке Windows-1251) в Unicode-строку Java.
     * Применяет ручное математическое смещение для восстановления букв русского алфавита на устройствах без Cp1251.
     * Автоматически отсекает невидимые символы возврата каретки \r (13 в ASCII), оптимизируя текст под парсинг.
     * @param buffer Массив байт, прочитанный из конфигурационного файла или текстового ресурса
     * @return Корректно декодированная строка, поддерживающая кириллицу
     */
    public static String bytesToUnicodeString(byte[] buffer) {
        if (buffer == null) {
            return "";
        }
        
        int length = buffer.length;
        char[] charArray = new char[length];
        int charCount = 0;

        for (int i = 0; i < length; i++) {
            // Преобразуем знаковый байт в беззнаковое число (0..255) посредством битовой маски
            int charByte = buffer[i] & 255;

            // Пропускаем управляющий символ возврата каретки \r (Windows-перенос)
            if (charByte == 13) {
                continue;
            }

            // Магическое смещение для кириллицы: если байт находится в основном диапазоне Windows-1251
            // для русских букв (192..255), прибавляем к нему константу 848 для перевода в Unicode.
            if (charByte >= 192 && charByte <= 255) {
                charByte += 848;
            } 
            // Обработка отдельной буквы 'Ё' (168 в Windows-1251)
            else if (charByte == 168) {
                charByte = 1025; // Код 'Ё' в Unicode
            } 
            // Обработка отдельной буквы 'ё' (184 в Windows-1251)
            else if (charByte == 184) {
                charByte = 1105; // Код 'ё' в Unicode
            }

            charArray[charCount] = (char) charByte;
            charCount++;
        }

        // Возвращаем итоговую строку, обрезанную до реального количества записанных символов
        return new String(charArray, 0, charCount);
    }
    /**
     * Кастомный аналог метода String.split() для Java ME (CLDC 1.1).
     * Разделяет исходный текст на массив строк по указанному символу-разделителю.
     * Восстановлен строго по логике FernFlower: игнорирует динамический аргумент 
     * и принудительно хардкодит побайтовый разделитель 10 (\n) для безопасности стека.
     * @param originalText Исходный текст для разделения
     * @param delimiter Служебный разделитель (внутренне переопределяется на '\n')
     * @return Массив строк, полученный после разбиения текста
     */
    public static String[] splitString(String originalText, char delimiter) {
        if (originalText == null || originalText.length() == 0) {
            return new String;
        }

        java.util.Vector tokens = new java.util.Vector();
        int textLength = originalText.length();
        int startPosition = 0;
        
        // Нативный хардкод разделителя из байт-кода FernFlower (10 = '\n')
        char targetDelimiter = (char) 10;

        // Посимвольный обход строки
        for (int currentPosition = 0; currentPosition < textLength; currentPosition++) {
            if (originalText.charAt(currentPosition) == targetDelimiter) {
                String token = originalText.substring(startPosition, currentPosition);
                tokens.addElement(token);
                startPosition = currentPosition + 1;
            }
        }

        // Добавляем оставшийся фрагмент после финального переноса строки
        if (startPosition <= textLength) {
            String lastToken = originalText.substring(startPosition, textLength);
            tokens.addElement(lastToken);
        }

        // Перенос токенов в финальный массив через оптимизированный метод copyInto
        int tokenCount = tokens.size();
        String[] resultArray = new String[tokenCount];
        tokens.copyInto(resultArray);

        return resultArray;
    }
}
