import javax.microedition.lcdui.*;
import javax.microedition.midlet.MIDlet;

public class MonTP extends MIDlet implements CommandListener, ItemStateListener {

    Ticker newsTicker = new Ticker("PROJET DE JAVA EMBARQUE");
    private Gauge mGauge;
    private StringItem mStringItem;

    private Command backCommand = new Command("Revenir", Command.BACK, 1);
    private Command exitCommand = new Command("Quitter", Command.EXIT, 1);
    private Command execCommand = new Command("Lancer", Command.OK, 1);
    private Command initCommand = new Command("Initialiser", Command.OK, 1);
    private Command saveCommand = new Command("Enregistrer", Command.SCREEN, 2);

    private Command menuCommand = new Command("Accueil", Command.SCREEN, 1);
    private Command txtBoxCommand = new Command("TextBoxes", Command.SCREEN, 1);
    private Command formCommand = new Command("Formulaires", Command.SCREEN, 1);
    private Command alarmCommand = new Command("Alertes", Command.SCREEN, 2);

    String[] menuItem = { "TextBox", "Formulaires", "Alertes" };
    String[] txtBoxItems = { "Email", "Mot de passe", "Telephone", "lien URL", "Autres" };
    String[] formItem = { "Exemple 1", "Exemple 2", "Exemple 3", "Exemple 4" };
    String[] alarmItem = { "Confirmation", "Erreur", "Information", "Alarme", "Alerte Temporisee (5 sec)" };

    private List menuList = new List("Selectionner un composant :", List.IMPLICIT, menuItem, null);
    private List formList;
    private List txtBoxList;
    private List alarmList;

    private TextBox currentTxtBox = null;

    private String currentScreen = "";
    private Display display = Display.getDisplay(this);

    public void startApp() {
        menuList.setTicker(newsTicker);
        menuList.addCommand(exitCommand);
        menuList.setCommandListener(this);
        afficherMainScreen();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public void commandAction(Command c, Displayable s) {
        if (c == exitCommand) {
            destroyApp(false);
            notifyDestroyed();
        }

        // list select
        if (c == List.SELECT_COMMAND) {
            if (currentScreen == "main_screen") {
                int itemIndex = menuList.getSelectedIndex();
                switch (itemIndex) {
                    case 0: {
                        afficherTextBoxType();
                        break;
                    }
                    case 1:
                        afficherForm();
                        break;
                    case 2:
                        afficherAlarm();
                        break;
                }
            } else if (currentScreen == "textbox_screen") {
                int itemIndex = txtBoxList.getSelectedIndex();
                switch (itemIndex) {
                    case 0: {
                        currentTxtBox = new TextBox("Email :", "", 25, TextField.EMAILADDR);
                        break;
                    }
                    case 1: {
                        currentTxtBox = new TextBox("Mot de passe :", "", 25, TextField.PASSWORD);
                        break;
                    }
                    case 2: {
                        currentTxtBox = new TextBox("Telephone :", "", 25, TextField.PHONENUMBER);
                        break;
                    }
                    case 3: {
                        currentTxtBox = new TextBox("Lien URL :", "", 25, TextField.URL);
                        break;
                    }
                    case 4: {
                        currentTxtBox = new TextBox("Text general :", "", 100, TextField.ANY);
                        break;
                    }
                }
                currentTxtBox.addCommand(exitCommand);
                currentTxtBox.addCommand(backCommand);
                currentTxtBox.addCommand(execCommand);
                currentTxtBox.addCommand(initCommand);
                currentTxtBox.setCommandListener(this);
                display.setCurrent(currentTxtBox);
            } else if (currentScreen == "alarm_screen") {
                int itemIndex = alarmList.getSelectedIndex();
                Alert alert = null;
                switch (itemIndex) {
                    case 0: {
                        alert = new Alert("Confirmation :", "C'est une alerte de type \"AlertType.CONFIRMATION\".",
                                null, AlertType.CONFIRMATION);
                        break;
                    }
                    case 1: {
                        alert = new Alert("Erreur :", "C'est une alerte de type \"AlertType.ERROR\".", null,
                                AlertType.ERROR);
                        break;
                    }
                    case 2: {
                        alert = new Alert("Information :", "C'est une alerte de type \"AlertType.INFO\".", null,
                                AlertType.INFO);
                        break;
                    }
                    case 3: {
                        alert = new Alert("Alerte :", "C'est une alerte de type \"AlertType.WARNING\".", null,
                                AlertType.WARNING);
                        break;
                    }
                    case 4: {
                        alert = new Alert("Alarme temporisee :",
                                "C'est une alarme temporisee (10 sec) de type \"AlertType.ALARM\".", null,
                                AlertType.WARNING);
                        alert.setTimeout(10000);
                        break;
                    }
                }
                alert.addCommand(alarmCommand);
                alert.setCommandListener(this);
                display.setCurrent(alert);
            }
        } else {
            // menu cmds
            if (c == menuCommand) {
                afficherMainScreen();
            } else if (c == txtBoxCommand) {
                afficherTextBoxType();
            } else if (c == formCommand) {
                afficherForm();
            } else if (c == alarmCommand) {
                afficherAlarm();
            } else if (c == backCommand) {
                if (currentScreen == "textbox_screen")
                    display.setCurrent(txtBoxList);
                else if (currentScreen == "form_screen")
                    display.setCurrent(formList);
            } else if (c == execCommand) {
                if (currentScreen == "textbox_screen")
                    if (currentTxtBox.size() > 0)
                        System.out.println("Vous avez ecrit :" + currentTxtBox.getString());
                    else
                        System.out.println("le texte est vide ...");
                else if (currentScreen == "form_screen") {
                    switch (formList.getSelectedIndex()) {
                        case 0:
                            showForm1();
                            break;
                        case 1:
                            showForm2();
                            break;
                        case 2:
                            showForm3();
                            break;
                        case 3:
                            showForm4();
                            break;
                        default:
                            break;
                    }
                }
            } else if (c == initCommand) {
                if (currentScreen == "textbox_screen")
                    currentTxtBox.delete(0, currentTxtBox.size());
            }
        }
    }

    private void afficherMainScreen() {
        currentScreen = "main_screen";
        display.setCurrent(menuList);
    }

    private void afficherTextBoxType() {
        txtBoxList = new List("TextBox :", List.IMPLICIT, txtBoxItems, null);
        txtBoxList.addCommand(menuCommand);
        txtBoxList.addCommand(formCommand);
        txtBoxList.addCommand(alarmCommand);
        txtBoxList.addCommand(exitCommand);
        txtBoxList.setCommandListener(this);
        currentScreen = "textbox_screen";
        display.setCurrent(txtBoxList);
    }

    private void afficherForm() {
        formList = new List("Formulaires :", List.EXCLUSIVE, formItem, null);
        formList.addCommand(menuCommand);
        formList.addCommand(txtBoxCommand);
        formList.addCommand(alarmCommand);
        formList.addCommand(exitCommand);
        formList.addCommand(execCommand);
        formList.setCommandListener(this);
        currentScreen = "form_screen";
        display.setCurrent(formList);
    }

    private void afficherAlarm() {
        alarmList = new List("Alarmes :", List.IMPLICIT, alarmItem, null);
        alarmList.addCommand(menuCommand);
        alarmList.addCommand(txtBoxCommand);
        alarmList.addCommand(formCommand);
        alarmList.addCommand(exitCommand);
        alarmList.setCommandListener(this);
        currentScreen = "alarm_screen";
        display.setCurrent(alarmList);
    }

    private void showForm1() {
        Form form = new Form("Formulaires : premier exemple");
        form.append("Ceci est un text sur une seule ligne. ");
        form.append("Celui-la est un autre texte.");
        form.append("\nCe text contient\ndes sauts\nde ligne\n");
        form.append("TextField.ANY =>");
        form.append(new TextField("NOM :", "", 25, TextField.ANY));
        form.append("TextField.PASSWORD =>");
        form.append(new TextField("MOT DE PASSE :", "", 15, TextField.PASSWORD));
        form.append("TextField.EMAILADDR =>");
        form.append(new TextField("EMAIL :", "", 25, TextField.EMAILADDR));
        form.append("TextField.NUMERIC =>");
        form.append(new TextField("AGE (ans) :", "", 2, TextField.NUMERIC));
        form.append("TextField.PHONENUMBER =>");
        form.append(new TextField("TELEPHONE :", "", 16, TextField.PHONENUMBER));
        form.append("TextField.URL =>");
        form.append(new TextField("SITE WEB :", "", 50, TextField.URL));
        form.addCommand(backCommand);
        form.addCommand(exitCommand);
        form.addCommand(txtBoxCommand);
        form.addCommand(alarmCommand);
        form.addCommand(menuCommand);
        form.setCommandListener(this);
        display.setCurrent(form);
    }

    public void itemStateChanged(Item item) {
        if (item == mGauge)
            mStringItem.setText("Valeur courante = " + mGauge.getValue());
    }

    private void showForm2() {
        mGauge = new Gauge("Jauge :", true, 20, 10);
        mStringItem = new StringItem(null, "[value]");
        itemStateChanged(mGauge);
        Form form = new Form("Formulaires : deuxieme exemple");
        form.append(mGauge);
        form.append(mStringItem);
        form.setItemStateListener(this);
        form.addCommand(backCommand);
        form.addCommand(exitCommand);
        form.addCommand(txtBoxCommand);
        form.addCommand(alarmCommand);
        form.addCommand(menuCommand);
        form.setCommandListener(this);
        display.setCurrent(form);
    }

    private void showForm3() {
        Form form = new Form("Formulaires : troisieme exemple"); // avec leurs paramètres
        String choix[] = { "Choix 1", "Choix 2", "Choix 3" };
        StringItem stringItem = new StringItem(null, "Ceci est un StringItem");
        ChoiceGroup chExcl = new ChoiceGroup("Selectionner (choice.EXCLUSIVE)", Choice.EXCLUSIVE, choix, null);
        ChoiceGroup chMult = new ChoiceGroup("Selectionner (choice.MULTIPLE)", Choice.MULTIPLE, choix, null);
        DateField dateField = new DateField("Date", DateField.DATE);
        DateField timeField = new DateField("Heure", DateField.TIME);
        form.append(stringItem);
        form.append(chExcl);
        form.append(chMult);
        form.append(timeField);
        form.append(dateField);
        form.addCommand(backCommand);
        form.addCommand(exitCommand);
        form.addCommand(txtBoxCommand);
        form.addCommand(alarmCommand);
        form.addCommand(menuCommand);
        form.setCommandListener(this);
        display.setCurrent(form);
    }

    public void showForm4() {
        Form form = new Form("Formulaires : quatrieme exemple");
        Ticker t = new Ticker("Be Carful, A Tigeriscomming;)");
        StringItem tiger = new StringItem("", " -A Tiger-");
        try {
            Image img = Image.createImage("/images/tiger.jpg");
            form.append(new ImageItem(null, img, ImageItem.LAYOUT_CENTER, null));

        } catch (java.io.IOException e) {
            StringItem str = new StringItem("Exception IO :", e.getMessage());
            form.append(str);
        }
        form.addCommand(backCommand);
        form.addCommand(exitCommand);
        form.addCommand(txtBoxCommand);
        form.addCommand(alarmCommand);
        form.addCommand(menuCommand);
        form.setCommandListener(this);
        display.setCurrent(form);
    }
    // alert = new Alert("Info", information, null, AlertType.INFO);
    // alert.setTimeout(Alert.FOREVER);
    // display.setCurrent(alert);
    // }

    // private void doAppend() {
    // Form appendForm = new Form("Add");
    // saveCommand = new Command("Save", Command.SCREEN, 1);
    // TextField nameField = new TextField("Name", null, 10, TextField.ANY);
    // TextField EMailField = new TextField("E Mail", null, 10,
    // TextField.EMAILADDR);
    // TextField ageField = new TextField("Age", null, 10, TextField.NUMERIC);
    // appendForm.append(nameField);
    // appendForm.append(EMailField);
    // appendForm.append(ageField);
    // appendForm.addCommand(saveCommand);
    // appendForm.addCommand(commCommand);
    // appendForm.addCommand(luckyCommand);
    // appendForm.setCommandListener(this);
    // currentScreen = "ContactAdd";
    // display.setCurrent(appendForm);
    // }

    // private void doModification() {
    // Form modificationForm = new Form("Edit");
    // TextField nameField = new TextField("Name", null, 10, TextField.ANY);
    // modificationForm.append(nameField);
    // modificationForm.addCommand(execCommand);
    // modificationForm.addCommand(commCommand);
    // modificationForm.addCommand(luckyCommand);
    // modificationForm.setCommandListener(this);
    // currentScreen = "ContactEdit";
    // display.setCurrent(modificationForm);
    // }

    // private void doQuery() {
    // Form queryForm = new Form("Search");
    // TextField nameField = new TextField("Name", null, 10, TextField.ANY);
    // queryForm.append(nameField);
    // queryForm.addCommand(execCommand);
    // queryForm.addCommand(commCommand);
    // queryForm.addCommand(luckyCommand);
    // queryForm.setCommandListener(this);
    // currentScreen = "ContactSearch";
    // display.setCurrent(queryForm);
    // }
}