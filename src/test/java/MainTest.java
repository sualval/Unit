import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    static String PATH = "D:/Games/savegames/";

    String file1 = PATH + "file1.dat";
    String pathToZip = PATH + "zip.zip";
    List<String> path = new ArrayList<>(Arrays.asList(file1));
    GameProgress gameProgress1 = new GameProgress(2, 4, 6, 8.5);

    @BeforeAll
    @Test
    static void checkDirTest() {
        assertEquals(!new File(PATH).isDirectory(), Main.checkDir(PATH));
    }

    @BeforeEach
    void addFile() {
        if (!new File(file1).isFile()) {
            Main.saveGame(file1, gameProgress1);
        }
    }


    @Test
    @DisplayName("Сохранение и чтение сохраненного файла")
    void testSavingAndReadingSavedObject() throws IOException, ClassNotFoundException {
        assertTrue(Main.saveGame(file1, gameProgress1));

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(file1))) {
            assertEquals(objectInputStream.readObject(), gameProgress1);
            assertThat(objectInputStream.readObject(), equalTo(gameProgress1));
            assertEquals(objectInputStream.readObject(), gameProgress1);
            assertThat(objectInputStream.readObject(), equalTo(gameProgress1));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("Неверный путь к файлу")
    void saveGameTestFileNotFoundException() {
        assertFalse(Main.saveGame("", gameProgress1));

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

        assertInstanceOf(GameProgress.class, Main.openProgress(file1));
        assertEquals(Main.openProgress(file1), gameProgress1);
        assertDoesNotThrow(() -> Main.openProgress(file1));
        assertThat(Main.openProgress(file1), samePropertyValuesAs(gameProgress1));
        assertThat(Main.openProgress(file1), instanceOf(GameProgress.class));
        assertThat(Main.openProgress(file1), Matchers.notNullValue());
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