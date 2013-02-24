package pk.dyplom;

import pk.dyplom.codegen.CodeGeneration;
import pk.dyplom.editor.DiagramEditor;
import pk.dyplom.event.EventManager;
import pk.dyplom.log.DiagramLog;
import pk.dyplom.runtime.DiagramRunner;
import pk.dyplom.ui.GUI;
import pk.dyplom.ui.MainFrame;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

/**
 * Główna klasa reprezentująca instancję aplikacji.
 */
public class Application {

    /** Log wypisywany na std. wyjście */
    public final Logger logger = Logger.getLogger("App");

    /** Interfejs użytkownika */
    public GUI gui;
    /** Manager zdarzeń */
    public EventManager eventBus;
    /** Komponent odpowiedzialny za uruchamianie diagramów */
    public DiagramRunner runner;
    /** Edytor diagramów */
    public DiagramEditor editor;
    /** Komponent odpowiedzialny za generację kodu */
    public CodeGeneration codeGeneration;
    /** Panel logujący zdarzenia aplikacji */
    public DiagramLog diagramLog;

    /**
     * Stwórz instancję aplikacji.
     * @param args Argumenty linii poleceń
     */
    public Application(String... args) {
        logger.info("Start");

        addShutdownHook();
        initComponents();
    }

    /**
     * Dodaj handler poprawnie zamykający aplikację w przypadku przerwania za pomocą {@code Ctrl+C}.
     * @see Application#close
     */
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                close();
            }
        });
    }

    /**
     * Zamknij aplikację.
     */
    public void close() {
        logger.info("Close");
    }

    /**
     * Zainicjuj główne okno aplikacji, interfejs użytkownika i poszczególne komponenty.
     */
    private void initComponents() {
        logger.info("Create GUI");

        gui = new GUI();
        eventBus = new EventManager();
        editor = new DiagramEditor(gui, eventBus);
        runner = new DiagramRunner(gui, eventBus);
        codeGeneration = new CodeGeneration(gui, eventBus);
        diagramLog = new DiagramLog(gui, eventBus);

        new MainFrame(I18n.t("app.title"), gui, new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                close();
            }
        });

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            public void uncaughtException(Thread t, Throwable error) {
                logger.warning(String.format("[ERROR] %s", error.toString()));
                String msg = String.format("%s\n\n%s", I18n.t("error.message"), error.getMessage());
                JOptionPane.showMessageDialog(gui, msg, I18n.t("error.title"), JOptionPane.ERROR_MESSAGE);
            }
        });
    }


    /**
     * Uruchom aplikację.
     *
     * @param args Argumenty linii poleceń.
     */
    public static void main(String[] args) {
        new Application(args);
    }
}
