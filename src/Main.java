import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        String file1 = "D:/Games/savegames/file1.dat";
        String file2 = "D:/Games/savegames/file2.dat";
        String file3 = "D:/Games/savegames/file3.dat";
        String pathToZip = "D:/Games/savegames/zip7.zip";
        String unzipToFolder = "D:/Games/savegames/";

        List<String> path = new ArrayList<>(Arrays.asList(file1, file2, file3));
        GameProgress gameProgress1 = new GameProgress(2, 4, 6, 8.5);
        GameProgress gameProgress2 = new GameProgress(7, 5, 5, 9.5);
        GameProgress gameProgress3 = new GameProgress(5, 2, 7, 1.5);

        saveGame(path.get(0), gameProgress1);
        saveGame(path.get(1), gameProgress2);
        saveGame(path.get(2), gameProgress3);
        zipFiles(pathToZip, path);
        openZip(pathToZip, unzipToFolder);
        System.out.println(openProgress(file3));
    }

    public static boolean saveGame(String path, GameProgress gameProgress) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(path))) {
            outputStream.writeObject(gameProgress);
        } catch (IOException exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean zipFiles(String path, List<String> list) {

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(path))) {
            for (String string : list) {
                File file = new File(string);
                FileInputStream fileInputStream = new FileInputStream(string);
                zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
                byte[] buffer = new byte[fileInputStream.available()];
                fileInputStream.read(buffer);
                zipOutputStream.write(buffer);
                fileInputStream.close();
                file.delete();
            }
            zipOutputStream.closeEntry();
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            return false;
        }
        return true;
    }

    public static void openZip(String pathToZip, String unzipToFolder) {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(pathToZip))) {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                FileOutputStream fileOutputStream = new FileOutputStream(unzipToFolder + zipEntry.getName());
                for (int i = zipInputStream.read(); i != -1; i = zipInputStream.read()) {
                    fileOutputStream.write(i);
                }
                zipInputStream.closeEntry();
                fileOutputStream.close();
            }
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        }
    }

    public static GameProgress openProgress(String path) {
        GameProgress gameProgress = null;
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(path))) {
            gameProgress = (GameProgress) objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return gameProgress;
    }

}







