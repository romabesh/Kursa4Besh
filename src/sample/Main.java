package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.classes.CPU;
import sample.classes.ClockGeneratorSystem;
import sample.classes.Resource;
import sample.classes.TaskScheduler;

import java.util.ArrayList;

public class Main extends Application {
    public static Controller GUIController;

    private static ClockGeneratorSystem systemClock;
    private static CPU cpu;
    private static TaskScheduler taskScheduler;
    private static ArrayList<Resource> resources;

    private static boolean running = false;
    private static boolean firstStart = true;
    private static boolean pause = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("КУРСОВАЯ");
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setResizable(false);

        GUIController = loader.getController();
        GUIController.initBaseTabs();
        GUIController.initControlButtons();
        GUIController.initTextFields();
        GUIController.initSliders();
      //  GUIController.initCheckBoxes();

        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        endOfWork();
    }

    public static void main(String[] args)
    {
        launch(args);
    }

    public static void setupSystem() {
        System.out.println("Starting system.");

        cpu = new CPU(4);
        taskScheduler = new TaskScheduler(cpu, Configuration.getMemoryVolume());
        systemClock = new ClockGeneratorSystem(cpu, taskScheduler);
        resources = new ArrayList<>();
        for (int i = 0; i < Configuration.getResourcesCount(); i++) {
            Resource resource = new Resource("Resource " + (i + 1));
            resources.add(resource);
            systemClock.attachSystemComponent(resource);
        }
        GUIController.initResourcesBar(resources);

        systemClock.start();
        running = true;

        System.out.println("System setup completed!");

        firstStart = false;
    }

    public static void endOfWork() {
        if(taskScheduler != null) taskScheduler.finishWork();
        if(systemClock != null) systemClock.finishWork();
        running = false;
        System.out.println("System shutdown.");
    }

    public static int getSystemTime() {
        return systemClock.getTime();
    }

    public static ClockGeneratorSystem getSystemClock() { return systemClock; }

    public static TaskScheduler getTaskScheduler() {
        return taskScheduler;
    }

    public static ArrayList<Resource> getSystemResources() {
        return resources;
    }

    public static boolean isRunning() { return running; }

    public static boolean isFirstStart() { return firstStart; }

    /*--Cross-Thread methods--*/

    public static synchronized boolean pauseActive() { return pause; }

    public static synchronized void setPause(boolean value) { pause = value; }
}
