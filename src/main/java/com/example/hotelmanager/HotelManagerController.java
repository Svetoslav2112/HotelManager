package com.example.hotelmanager;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

public class HotelManagerController {
    Client client;
    @FXML
    private Button btnExitChat;
    @FXML
    private Button btnSend;
    @FXML
    private Button btnStartChat;
    @FXML
    private Label txtChat;
    @FXML
    private TextArea txtChatField;
    @FXML
    private TextField txtMessageInput;
    @FXML
    private ImageView imageView;
    @FXML
    private GridPane gridCheck;
    @FXML
    private GridPane gridpane;
    @FXML
    private GridPane hotelRoomsGrid;
    @FXML
    private RadioButton img1;
    @FXML
    private DatePicker from;
    @FXML
    private DatePicker to;
    @FXML
    private Slider slider;
    @FXML
    private RadioButton img2;
    @FXML
    private RadioButton img3;
    @FXML
    private TextField tempField;
    @FXML
    private TextField condField;
    @FXML
    private TextField windField;
    @FXML
    private ImageView tempImg;
    @FXML
    private ImageView condImg;
    @FXML
    private ImageView windImg;
    private ArrayList<String> selectedCriterias = new ArrayList<>();


    public HotelManagerController() {}


   // Util functions
    private void filterRooms(){
       ArrayList<HotelRoom> rooms;
        try{
            HotelFacade stub = client.getStub();
            rooms = stub.getRooms();

            for(int i = 0; i < rooms.size(); i++) {
                boolean visible = stub.isRoomAvailable(selectedCriterias, slider.getValue(), from.getValue(), to.getValue(), rooms.get(i));
                int roomNum =  rooms.get(i).getRoomNumber();
                ToggleButton tb = (ToggleButton) hotelRoomsGrid.getChildren().get(roomNum - 1);

                if(visible){
                    tb.setVisible(true);
                    tb.setDisable(false);
                } else {
                    tb.setVisible(false);
                    tb.setDisable(true);
                }
            }
        } catch(Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
        }

   }
    private void centerImage() {
        Image img = imageView.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = imageView.getFitWidth() / img.getWidth();
            double ratioY = imageView.getFitHeight() / img.getHeight();

            double coef = Math.min(ratioX, ratioY);

            w = img.getWidth() * coef;
            h = img.getHeight() * coef;

            imageView.setX((imageView.getFitWidth() - w) / 2);
            imageView.setY((imageView.getFitHeight() - h) / 2);

        }
    }
    private void untoggleRadioExcept(String id) {
        int cnt = gridpane.getChildren().size();

        for(int i = 0; i < cnt; i++) {
            RadioButton rb = (RadioButton) gridpane.getChildren().get(i);
            if(!Objects.equals(rb.getId(), id)) {
                rb.setSelected(false);
            }
        }
    }
    private void untoggleRoomButtonsExcept(String id) {
        int cnt = hotelRoomsGrid.getChildren().size();

        for(int i = 0; i < cnt; i++) {
            ToggleButton tb = (ToggleButton) hotelRoomsGrid.getChildren().get(i);
            if(!Objects.equals(tb.getId(), id)) {
                tb.setSelected(false);
            }
        }
    }
    private String toggledRoomButtonId() {
        int cnt = hotelRoomsGrid.getChildren().size();

        for(int i = 0; i < cnt; i++) {
            ToggleButton tb = (ToggleButton) hotelRoomsGrid.getChildren().get(i);
            if(tb.isSelected()) {
                return tb.getId();
            }
        }

        return "";
    }
    private void addEvents() {
        addGridEvent();
        addImgEvent();
        addCheckEvent();
    }
    private void setIcons() {
        File tempPath = new File("images/icons/thermometer.jpg");
        File condPath = new File("images/icons/sun.jpg");
        File windPath = new File("images/icons/wind.jpg");

        tempImg.setImage(new Image(tempPath.toURI().toString()));
        condImg.setImage(new Image(condPath.toURI().toString()));
        windImg.setImage(new Image(windPath.toURI().toString()));
    }
    private void setDefaultValues() {
        slider.setValue(0);
        from.setValue(LocalDate.of(2023, 1, 1));
        to.setValue(LocalDate.of(2023, 1, 2));
    }
    private void prepareTextFields() {
        txtChatField.setEditable(false);
        txtChatField.setWrapText(true);
    }
    private void toggleImageButtons(boolean isDisabled) {
        for(int i = 0; i < gridpane.getChildren().size(); i++) {
            gridpane.getChildren().get(i).setDisable(isDisabled);
        }
    }
    void toggleChat() {
        btnStartChat.setDisable(!btnStartChat.isDisable());
        btnStartChat.setVisible(!btnStartChat.isVisible());
        btnSend.setDisable(!btnSend.isDisable());
        txtChat.setDisable(!txtChat.isDisable());
        txtChatField.setDisable(!txtChatField.isDisable());
        txtMessageInput.setDisable(!txtMessageInput.isDisable());
        btnExitChat.setDisable(!btnExitChat.isDisable());
        btnExitChat.setVisible(!btnExitChat.isVisible());
        txtMessageInput.requestFocus();
    }


    // Custom event handlers
    private void addImgEvent() {
        gridpane.getChildren().forEach(item -> {
            item.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    // Get id of pressed button
                    Control control = (Control)event.getSource();
                    String id = control.getId();
                    int intId = Integer.parseInt(id);

                    untoggleRadioExcept(id);
                    String roomId = toggledRoomButtonId();

                    try {
                        // Display image based on button's id
                        HotelFacade stub = client.getStub();
                        HotelRoom room = stub.getRoom(roomId);

                        String path = room.getImageURLs()[intId - 1];
                        File file = new File(room.getImageURLs()[intId - 1]);
                        Image image = new Image(file.toURI().toString());
                        imageView.setImage(image);
                        centerImage();
                    } catch (NotBoundException | RemoteException e) {
                        throw new RuntimeException(e);
                    }

                }
            });

        });
    }
    private void addGridEvent() {
        hotelRoomsGrid.getChildren().forEach(item -> {
            item.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Control control = (Control) event.getSource();
                    String id = control.getId();
                    ToggleButton tb = (ToggleButton) event.getSource();

                    if (!tb.isSelected()) {
                        img1.setSelected(false);
                        img2.setSelected(false);
                        img3.setSelected(false);

                        imageView.setImage(null);
                        toggleImageButtons(true);
                    } else {
                        try {
                            // Display Room
                            untoggleRoomButtonsExcept(id);
                            HotelFacade stub = client.getStub();
                            HotelRoom room = stub.getRoom(id);

                            File file = new File(room.getImageURLs()[0]);
                            Image image = new Image(file.toURI().toString());

                            imageView.setImage(image);
                            centerImage();
                            toggleImageButtons(false);

                            img2.setSelected(false);
                            img3.setSelected(false);
                            if (!img1.isSelected()) {
                                img1.fire();
                            }
                        } catch (NotBoundException | RemoteException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });

        });
    }
    private void addCheckEvent() {
        gridCheck.getChildren().forEach(item -> {
            item.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Control control = (Control)event.getSource();
                    String id = control.getId();
                    CheckBox cb = (CheckBox) event.getSource();

                    // Filter rooms based on selected criteria
                    if(cb.isSelected()) {
                        selectedCriterias.add(id);
                    }else {
                        selectedCriterias.remove(id);
                    }

                    filterRooms();

                }
            });

        });
    }


    // Initializer
    @FXML
    private void initialize() {
        client = new Client();
        addEvents();
        setIcons();
        setDefaultValues();
        prepareTextFields();
        toggleImageButtons(true);
    }


    // On select FXML event handlers
    @FXML
    void onSlide(MouseEvent event) {
        filterRooms();
    }
    @FXML
    void onExitChatButtonClicked(ActionEvent event) {
        txtChatField.clear();
        txtMessageInput.clear();
        toggleChat();
    }
    @FXML
    void onDateFromClicked(ActionEvent event) {
        if(to.getValue() != null) {
            LocalDate fromDate = (LocalDate) from.getValue();
            filterRooms();
        }
    }
    @FXML
    void onDateToClicked(ActionEvent event) {
        if(from.getValue() != null) {
            LocalDate fromDate = to.getValue();
            filterRooms();
        }
    }
    @FXML
    void onGetWeatherClicked(ActionEvent event) throws NotBoundException, IOException {
        HotelFacade stub = client.getStub();
        String response = stub.getCurrentWeatherAtHotelLocation();
        String[] forecastData = response.split(",");

        tempField.setText(forecastData[0] + " ºC");
        condField.setText(forecastData[1]);
        windField.setText(forecastData[2] + " km/h");
    }
    @FXML
    void onReserveButtonClicked(ActionEvent event) throws NotBoundException, RemoteException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        HotelRoom hotelRoom = null;

        for(int i = 0; i < hotelRoomsGrid.getChildren().size(); i++) {
            // Get the selected room
            ToggleButton tb = (ToggleButton) hotelRoomsGrid.getChildren().get(i);

            if(tb.isSelected()) {
                // Calculate the price of the stay
                int period = (int) DAYS.between(from.getValue(), to.getValue());

                HotelFacade stub = client.getStub();
                HotelRoom hr = stub.getRoom(tb.getId());

                double pricePerNight = hr.getPricePerNight();
                String price = Double.toString(pricePerNight * period);

                // Alert the user to confirm the reservation
                alert.setContentText("Would you like to reserve the room for " + price + " lv?");
                Optional<ButtonType> result = alert.showAndWait();

                // Set an occupation date
                if(result.get() == ButtonType.OK) {
                    stub.changeOccupied(tb.getId(), from.getValue(), to.getValue());

                    // Re-filter the rooms
                    filterRooms();
                    imageView.setImage(null);
                    toggleImageButtons(true);

                    for(int j = 0; j < gridpane.getChildren().size(); j++) {
                        RadioButton rb = (RadioButton) gridpane.getChildren().get(j);
                        rb.setSelected(false);
                    }
                }
            }
        }
    }
    @FXML
    void onStartChatButtonClicked(ActionEvent event) throws IOException, NotBoundException {
        toggleChat();

        // Get default help command
        HotelFacade stub = client.getStub();
        String response = stub.getHelp("");
        txtChatField.setText(response);
    }
    @FXML
    void onSendButtonClicked(ActionEvent event) throws NotBoundException, RemoteException {
        // Get help from server based on chosen command
        HotelFacade hf = client.getStub();
        String request = txtMessageInput.getText();
        txtChatField.appendText("> " + request + "\n" + "\n");
        String response = hf.getHelp(request);
        txtChatField.appendText(response);
        txtMessageInput.clear();
    }
}
