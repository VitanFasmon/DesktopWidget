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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ClockWidgetController extends Application {

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private Label timeLabel;

    @FXML
    public void onExitButtonClick() {
        System.exit(0);
    }

    public void updateTime() {
        LocalTime currentTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM);
        timeLabel.setText(currentTime.format(formatter));
    }

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("ClockWidgetController.start");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("ClockWidget.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);
        stage.setScene(scene);
        scene.setFill(null); // Transparent background

        // Set up dragging
        root.setOnMousePressed((MouseEvent event) -> {
            xOffset = stage.getX() - event.getScreenX();
            yOffset = stage.getY() - event.getScreenY();
        });

        root.setOnMouseDragged((MouseEvent event) -> {
            stage.setX(event.getScreenX() + xOffset);
            stage.setY(event.getScreenY() + yOffset);
        });

        stage.initStyle(StageStyle.TRANSPARENT);
        // stage.setAlwaysOnTop(true);
        stage.setTitle("ClockWidget");
        stage.show();

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
    }

    public static void main(String[] args) {
        launch(args);
    }
}
