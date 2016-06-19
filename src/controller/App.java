package controller;


import com.itextpdf.text.pdf.qrcode.Mode;
import model.MicrosoftTranslator;
import model.Model;
import model.Settings;
import view.View;


/**
 * Created by pierre on 19/04/16.
 */
public class App {
    static View view;
    static Model model;
    static Controller controller;
    static public Settings settings = new Settings();
    public static void main(String args[]) {
        System.out.println("Initializing application");
//        view = new View();
        model = new Model();
        controller = new Controller(model);
        controller.setTranslator(new MicrosoftTranslator());

        new View().launchView(args);
        settings.save();
        if(controller.newFile!=null)
            controller.newFile.export();
        System.out.println("Exiting application");
    }

    public static Controller getController() {
        return controller;
    }
    public static Model getModel() {
        return model;
    }

    public static void setView(View view) {
        App.view = view;
        controller.setView(view);
    }
}

//public class App extends Application {
//    private LanguageClass path=new LanguageClass();
//    public static void main(LanguageClass[] args) {
//        launch(args);
//    }
//
//    @Override
//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Hello World!");
//        Button btn = new Button();
//        btn.setText("Choose *.pdf file");
//        GridPane grid=new GridPane();
//        grid.setPadding(new Insets(25, 25, 25, 25));
//        FileChooser fileChooser = new FileChooser();
//
//        FlowPane flow = new FlowPane();
//
//        for(int i=0; i<40; i++) {
//            flow.getChildren().addAll(new WordButton("DUPA"), new WordButton("HAHH"), new WordButton("A"));
//        }
//
//        grid.getChildren().add(flow);
//
//        btn.setOnAction(new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
////                System.out.println("A");
//                File file = fileChooser.showOpenDialog(primaryStage);
//                if (file != null) {
//                    new TextContainer(file);
//                }
//            }
//        });
//        grid.add(btn, 0, 0);
//
//
//
//        path = new File(".").getAbsolutePath();
//        LanguageClass textFile=prepareFile(1);
//        Text text=new Text();
//        text.setFont(new Font(12));
//        grid.add(text, 1, 1, 10, 10);
//        if(!textFile.equals("blad")){
//            text.setText(textFile);
//            text.autosize();
//        }
//        Scene scene = new Scene(grid, 300, 250);
//        primaryStage.setScene(scene);
//        scene.getStylesheets().add(App.class.getResource("view/ViewStylesheet.css").toExternalForm());
//        primaryStage.show();
//        System.out.println("DONE INIT");
//    }
//
//    private LanguageClass readFile(LanguageClass pathname) throws IOException {
//
//        File file = new File(pathname);
//        StringBuilder fileContents = new StringBuilder((int) file.length());
//        Scanner scanner = new Scanner(file);
//        LanguageClass lineSeparator = System.getProperty("line.separator");
//
//        try {
//            while (scanner.hasNextLine()) {
//                fileContents.append(scanner.nextLine() + lineSeparator);
//            }
//            return fileContents.toString();
//        } finally {
//            scanner.close();
//        }
//    }
//    private LanguageClass prepareFile(int pageNumber){
//        LanguageClass A, B, C=new LanguageClass();
//        B=path.substring(0, path.length()-1);
//        B=B+Integer.toString(pageNumber)+".txt";
//        try{
//            C=readFile(B);
//        }
//        catch(IOException e){
//            C="blad";
//        }
//        return C;
//    }
//}