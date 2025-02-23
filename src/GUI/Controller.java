package GUI;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.image.BufferedImage;
import javafx.stage.FileChooser;
import solver.Colors;
import solver.Puzzle;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;

public class Controller {

    @FXML
    private Button FileButton;

    @FXML
    private Label fileLabel;

    @FXML
    private Button solveButton;

    @FXML
    private ImageView resultImage;

    @FXML
    private Label attempts;

    @FXML
    private Label time;

    @FXML
    private Label LabelHasil;

    @FXML
    private Label LabelDownload;

    @FXML
    private Hyperlink linkDownload;

    private File inputFile;

    @FXML
    public void initialize() {
        if (FileButton != null) {
            FileButton.setOnAction(event -> handleUploadButton());
        }
    }

    @FXML
    private void handleUploadButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih File");

        // Filter file agar hanya menampilkan TXT
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Buka dialog file chooser
        File selectedFile = fileChooser.showOpenDialog(FileButton.getScene().getWindow());

        if (selectedFile != null) {
            inputFile = selectedFile;
            if (fileLabel != null) {
                fileLabel.setText("File selected: " + selectedFile.getName());
            }
            System.out.println("File yang dipilih: " + selectedFile.getAbsolutePath());
        } else {
            if (fileLabel != null) {
                fileLabel.setText("No file selected");
            }
            System.out.println("Tidak ada file yang dipilih.");
        }
    }

    @FXML
    private void handleDownloadLink(File outputFile) {
        if (outputFile == null) {
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save File");

        // Filter file agar hanya menampilkan TXT
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Buka dialog file chooser
        File selectedFile = fileChooser.showSaveDialog(FileButton.getScene().getWindow());

        if (selectedFile != null) {
            try {
                File destination = new File(selectedFile.getAbsolutePath());
                Files.copy(outputFile.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleSolveButton() {
        try {
            if (inputFile == null) {
                LabelHasil.setText("Tidak ada file input. Silahkan pilih file terlebih dahulu.");
                return;
            }

            if (Puzzle.returnThrowMessage(inputFile.getAbsolutePath()) != null) {
                LabelHasil.setText(Puzzle.returnThrowMessage(inputFile.getAbsolutePath()));
                Platform.runLater(() -> resultImage.setImage(null));
                return;
            }
            Map<String, Object> result = Puzzle.MainGUI(inputFile.getAbsolutePath());

            if (result != null) {
                BufferedImage bufferedImage = (BufferedImage) result.get("image");
                if (bufferedImage != null) {

                    Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
                    Platform.runLater(() -> resultImage.setImage(fxImage));
                    Platform.runLater(() -> linkDownload.setText((String) "Output.txt"));
                    attempts.setText(" : " + result.get("attempts"));
                    time.setText(" : " + result.get("duration") + " ms");

                    LabelHasil.setText("Hasil Solusi : ");
                    Platform.runLater(() -> LabelDownload.setText("File Hasil : "));
                    linkDownload.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2) { // double-click
                            handleDownloadLink((File) result.get("file"));
                        }
                    });

                } else {
                    Platform.runLater(() -> LabelDownload.setText(null));
                    Platform.runLater(() -> linkDownload.setText(null));
                    LabelHasil.setText("Tidak Ada Solusi Ditemukan");
                    attempts.setText(" : " + result.get("attempts"));
                    time.setText(" : " + result.get("duration") + " ms");
                    Platform.runLater(() -> resultImage.setImage(null));
                }
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
