
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    private static final String PATH = "D:/Games/savegames/";
    private String fileName = PATH + "file.dat";
    private String pathToZip = PATH + "zip.zip";
    private List<String> path = new ArrayList<>(Arrays.asList(fileName));
    private GameProgress gameProgress = new GameProgress(2, 4, 6, 8.5);

    @BeforeAll
    @Test
    static void checkDirTest() {
        assertEquals(!new File(PATH).isDirectory(), Main.checkDir(PATH));
    }

    @BeforeEach
    void addFile() {
        if (!new File(fileName).exists()) {
            Main.saveGame(fileName, gameProgress);
        }
    }


    @Test
    @DisplayName("Сохранение и чтение сохраненного файла")
    void testSavingAndReadingSavedObject() {
        assertTrue(Main.saveGame(fileName, gameProgress));

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            GameProgress gameProgressRead = (GameProgress) objectInputStream.readObject();
            assertEquals(gameProgress, gameProgressRead);
            assertThat(gameProgress, equalTo(gameProgressRead));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Неверный путь к файлу")
    void saveGameTestFileNotFoundException() {
        assertFalse(Main.saveGame("", gameProgress));

    }

    @Test
    @DisplayName("Проверка архивирования")
    void zipFilesTest() {
        assertTrue(Main.zipFiles(pathToZip, path));
    }

    @Test
    @DisplayName("Проверка распаковки")
    void openZipTest() {
        assertTrue(Main.openZip(pathToZip, PATH));
    }

    @Test
    @DisplayName("Проверка соответствия извлеченных файлов")
    void testOpenProgress() {
        assertEquals(gameProgress, Main.openProgress(fileName));
        assertThat(gameProgress, notNullValue());
    }

    @AfterAll
    static void deleteDir() {
        File dir = new File(PATH);
        File[] listDir = dir.listFiles();
        assert listDir != null;
        for (File file : listDir) {
            file.delete();
        }
    }

}