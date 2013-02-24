package pk.dyplom.log;

import pk.dyplom.I18n;
import pk.dyplom.event.*;
import pk.dyplom.ui.DiagramPanel;
import pk.dyplom.ui.GUI;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DiagramLog {

    private static enum MessageType {
        INFO("info"),
        SUCCESS("info", "color: green"),
        ERROR("error", "color: red");

        private static final DateFormat DATE_FORMAT = DateFormat.getDateTimeInstance(
                DateFormat.DEFAULT, DateFormat.DEFAULT, new Locale("pl", "PL"));
        private String style;
        private String name;

        private MessageType(String name, String style) {
            this(name);
            this.style = style;
        }

        private MessageType(String name) {
            this.name = name;
        }
        
        public String text(String text) {
            return String.format("<span style='%s'>%s [%s] %s</span>",
                    style,
                    DATE_FORMAT.format(new Date()),
                    I18n.t("log." + name),
                    text);
        }
    }

    public final GUI gui;
    public final EventManager eventBus;
    private Map<DiagramPanel, String> contents = new HashMap<DiagramPanel, String>();
    
    public DiagramLog(GUI gui, EventManager eventBus) {
        this.gui = gui;
        this.eventBus = eventBus;

        registerEventHandlers();
    }

    private void registerEventHandlers() {
        eventBus.addHandler(DiagramAddedEvent.class, new EventHandler<DiagramAddedEvent>() {
            public void handle(DiagramAddedEvent event) {
                contents.put(event.diagramPanel, "");
                write(event.diagramPanel, MessageType.INFO, "log.diagramAdded");
            }
        });
        eventBus.addHandler(DiagramSavedEvent.class, new EventHandler<DiagramSavedEvent>() {
            public void handle(DiagramSavedEvent event) {
                write(MessageType.SUCCESS, "log.diagramSaved", event.name);
            }
        });
        eventBus.addHandler(DiagramRunEvent.class, new EventHandler<DiagramRunEvent>() {
            public void handle(DiagramRunEvent event) {
                write(MessageType.INFO, "log.diagramRun", event.runTarget);
            }
        });
        eventBus.addHandler(DiagramStoppedEvent.class, new EventHandler<DiagramStoppedEvent>() {
            public void handle(DiagramStoppedEvent event) {
                write(MessageType.INFO, "log.diagramStopped");
            }
        });
        eventBus.addHandler(DiagramCodeGenerated.class, new EventHandler<DiagramCodeGenerated>() {
            public void handle(DiagramCodeGenerated event) {
                write(MessageType.SUCCESS, "log.diagramCodeGenerated");
            }
        });
        eventBus.addHandler(DiagramNodeAdded.class, new EventHandler<DiagramNodeAdded>() {
            public void handle(DiagramNodeAdded event) {
                write(MessageType.INFO, "log.diagramNodeAdded",
                        I18n.t("diagram.node." + event.node.getClass().getSimpleName()));
            }
        });
        eventBus.addHandler(DiagramNodeDeleted.class, new EventHandler<DiagramNodeDeleted>() {
            public void handle(DiagramNodeDeleted event) {
                write(MessageType.INFO, "log.diagramNodeDeleted",
                        I18n.t("diagram.node." + event.node.getClass().getSimpleName()));
            }
        });
        eventBus.addHandler(DiagramRuntimeErrorEvent.class, new EventHandler<DiagramRuntimeErrorEvent>() {
            public void handle(DiagramRuntimeErrorEvent event) {
                write(MessageType.ERROR, "log.diagramRuntimeError", event.errorMessage, event.exception.getClass().getSimpleName());
            }
        });
        eventBus.addHandler(DiagramExportedEvent.class, new EventHandler<DiagramExportedEvent>() {
            public void handle(DiagramExportedEvent event) {
                write(MessageType.SUCCESS, "log.diagramExported", event.file.getPath());
            }
        });
    }

    private DiagramPanel getCurrentDiagramPanel() {
        return (DiagramPanel) gui.diagramsPanel.getSelectedComponent();
    }
    
    private void write(DiagramPanel panel, MessageType type, String msg, String... params) {
        String text = contents.get(panel);
        text += String.format(
                "<pre style='font-size: 10px'>%s</pre>",
                type.text(I18n.t(msg, params)));
        panel.diagramLog.setText(text);
        contents.put(panel, text);
    }
    
    private void write(MessageType type, String msg, String... params) {
        write(getCurrentDiagramPanel(), type, msg, params);
    }
}
