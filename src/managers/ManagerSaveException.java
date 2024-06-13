package managers;

public class ManagerSaveException extends RuntimeException {
    ManagerSaveException() {
        super("Ошибка работы с файлами");
    }
}
