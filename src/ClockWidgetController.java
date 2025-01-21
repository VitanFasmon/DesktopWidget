import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClockWidgetController extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    public AnchorPane root;

    @FXML
    private Label timeLabel;

    @FXML
    public void onExitButtonClick() {
        System.exit(0);
    }

    @FXML
    public AnchorPane settingsMenu;

    @FXML
    public void onSettingsButtonClick() {
        boolean isVisible = settingsMenu.isVisible();
        settingsMenu.setVisible(!isVisible);
        settingsMenu.setManaged(!isVisible);
        updateWindowSize(!isVisible);
    }

    @FXML
    public Slider textOpacitySlider;

    @FXML
    public void onTextOpacityChange() {
        timeLabel.setOpacity(textOpacitySlider.getValue() / 100);
    }

    @FXML
    public Slider bgOpacitySlider;

    @FXML
    public void onBgOpacityChange() {
        root.setStyle("-fx-background-color: rgba(0, 0, 0, " + bgOpacitySlider.getValue() / 100 + ")");
    }

    @FXML
    public Slider textSizeSlider;

    @FXML
    public void onTextSizeChange() {
        timeLabel.setStyle("-fx-font-size: " + textSizeSlider.getValue() + "px;");
        updateWindowSize(true);
        timeLabel.applyCss();
        timeLabel.layout();
    }

    public void addListeners() {

        textOpacitySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            timeLabel.setOpacity(newVal.doubleValue() / 100);
        });

        bgOpacitySlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            root.setStyle("-fx-background-color: rgba(0, 0, 0, " + newVal.doubleValue() / 100 + ")");
        });

        textSizeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            timeLabel.setStyle("-fx-font-size: " + newVal.doubleValue() + "px;");
            timeLabel.applyCss();
            timeLabel.layout();
            updateWindowSize(true);
        });
    }

    public void updateWindowSize(boolean settingsMenuIsVisible) {
        Stage stage = (Stage) root.getScene().getWindow();
        stage.sizeToScene();
        if (timeLabel.getHeight() < 30) {
            if (settingsMenuIsVisible) {
                stage.setHeight(60 + settingsMenu.getHeight());
            } else {
                stage.setHeight(60);
            }
        } else {
            if (settingsMenuIsVisible) {
                stage.setHeight(timeLabel.getHeight() + 30 + settingsMenu.getHeight());
            } else {
                stage.setHeight(timeLabel.getHeight() + 30);
            }
        }
    }

    public void updateTime() {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);
        timeLabel.setText(currentTime.format(formatter));
    }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClockWidget.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(null);
        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });
        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setTitle("ClockWidget");
        stage.show();
        Platform.runLater(() -> {
            ClockWidgetController controller = loader.getController();
            controller.updateWindowSize(true);
        });
    }

    public void startTimer() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> updateTime());
            }
        };
        Timer timer = new Timer();
        timer.schedule(task, 0, 1000);
    }

    @FXML
    public void initialize() {
        startTimer();
        addListeners();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
