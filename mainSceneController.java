package com.perezcalle.songlibrary;


import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.Scanner;

import com.perezcalle.songlibrary.alerts.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class MainSceneController {
    @FXML
    private ListView<Song> songList;
    @FXML
    private TextField TitleAdd;
    @FXML
    private TextField ArtistAdd;
    @FXML
    private TextField yearAdd;
    @FXML
    private TextField albumAdd;
    @FXML
    private TextField titleEdit;
    @FXML
    private TextField artistEdit;
    @FXML
    private TextField albumEdit;
    @FXML
    private TextField yearEdit;

    private ObservableList<Song> obsList;

    public void readFile() {

    }

    public void start(Stage primaryStage) {
        File data = new File("songlists.txt");
        obsList = FXCollections.observableArrayList();
        if (data.exists() && !data.isDirectory()) {
            try {
                Scanner fileIn = new Scanner(data);

                while (fileIn.hasNextLine()) {
                    String line = fileIn.nextLine();
                    String[] words = line.split(",");
                    System.out.println(Arrays.toString(words));
                    if (words[2].isBlank() && words[3].isBlank()) {
                        obsList.add(new Song(words[0], words[1]));
                    } else {
                        obsList.add(new Song(words[0], words[1], words[2], words[3]));
                    }
                }
//                FXCollections.sort(obsList);
				sortList(obsList);
                fileIn.close();
            } catch (FileNotFoundException e) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setHeaderText("File Error");
                alert.setContentText("File does not exist");
                alert.showAndWait();
                e.printStackTrace();
            }
        }
        songList.setItems(obsList);
        if (!obsList.isEmpty()) {
            songList.getSelectionModel().selectFirst();

        }

        showSongDetails();

        songList.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldValue, newValue) -> showSongDetails());

        primaryStage.setOnCloseRequest(event -> {
            PrintWriter write;
            try {
                File file = new File("songlists.txt");
                write = new PrintWriter(file);
                for (int i = 0; i < obsList.size(); i++) {
                    write.print(obsList.get(i).getTitle() + (","));
                    write.print(obsList.get(i).getArtist() + (","));
                    write.print((obsList.get(i).getalbum().isBlank() ? " " : obsList.get(i).getalbum()) + (","));
                    write.print((obsList.get(i).getYear().isBlank() ? " " : obsList.get(i).getYear()) + (","));
                    if (i != obsList.size() - 1) {
                        write.println("");
                    }
                }
                write.close();
            } catch (Exception e) {

                e.printStackTrace();
            }
        });

        TitleAdd.setText("");
        ArtistAdd.setText("");
        albumAdd.setText("");
        yearAdd.setText("");
    }


    // Event Listener on Button.onAction
    @FXML
    private void deleteSong(ActionEvent event) {
        if (obsList.isEmpty()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("WARNING");
            alert.setHeaderText("Nothing to delete");
            alert.setContentText("Song list is empty");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("WARNING");
            alert.setHeaderText("You are about to delete the selected song");
            alert.setContentText("Are you sure?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                int selectedIndex = songList.getSelectionModel().getSelectedIndex();
                obsList.remove(selectedIndex);

                if (obsList.isEmpty()) {
                    titleEdit.setText("");
                    artistEdit.setText("");
                    albumEdit.setText("");
                    yearEdit.setText("");
                } else if (selectedIndex == obsList.size() - 1) {
                    songList.getSelectionModel().select(selectedIndex--);
                } else {
                    songList.getSelectionModel().select(selectedIndex++);

                }
            } else {
                return;
            }
        }
    }

    // TODO Autogenerated
    // Event Listener on Button.onAction
    @FXML
    private void songAdd(ActionEvent event) {

        if (albumAdd.getText().compareTo("") == 0) {
            albumAdd.setText("");
        }
        if (yearAdd.getText().compareTo("") == 0) {
            yearAdd.setText("");
        }

        if (!validYear(yearAdd.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ATTENTION");
            alert.setHeaderText("Invalid Year");
            alert.setContentText("Year must be a valid number greater than zero");
            alert.showAndWait();
            return;
        }
        if (TitleAdd.getText().contains("|") || ArtistAdd.getText().contains("|") || albumAdd.getText().contains("|") || yearAdd.getText().contains("|")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ATTENTION");
            alert.setHeaderText("Invalid Character");
            alert.setContentText("'|' cannot be used.");
            alert.showAndWait();
            return;
        }

        Song tempSong = new Song(TitleAdd.getText().trim(), ArtistAdd.getText().trim(), albumAdd.getText().trim(), yearAdd.getText().trim());
        add(tempSong);


        // TODO Autogenerated
    }

    private int add(Song tempSong) {
        String artist, title;
        artist = tempSong.getArtist();
        title = tempSong.getTitle();

        if (duplicate(tempSong, obsList)) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("ATTENTION");
            alert.setHeaderText("Duplicate");
            alert.setContentText("Same title/artist combination");
            alert.showAndWait();
            return -1;
        } else if (artist.compareTo("") == 0 || title.compareTo("") == 0) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("ATTENTION");
            alert.setHeaderText("Missing input");
            alert.setContentText("Title and Artist input must not be empty");
            alert.showAndWait();
            return -1;
        } else {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("ATTENTION");
            alert.setHeaderText("Adding a new song");
            alert.setContentText("You are about to add a new song: \n" + title + " by: " + artist + "\nAre you sure you want to add?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                obsList.add(tempSong);
                songList.getSelectionModel().select(tempSong);
                sortList(obsList);
                TitleAdd.setText("");
                ArtistAdd.setText("");
                albumAdd.setText("");
                yearAdd.setText("");

                return 0;
            } else {
                showSongDetails();
            }


        }
        return 0;
    }


    // Event Listener on Button.onAction
    @FXML
    private void songEdit(ActionEvent event) {
        if (!validYear(yearEdit.getText())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ATTENTION");
            alert.setHeaderText("Invalid Year");
            alert.setContentText("Year must be a valid number greater than zero");
            alert.showAndWait();
            return;
        }
        if (titleEdit.getText().contains("|") || artistEdit.getText().contains("|") || albumEdit.getText().contains("|") || yearEdit.getText().contains("|")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ATTENTION");
            alert.setHeaderText("Invalid Character");
            alert.setContentText("'|' cannot be used.");
            alert.showAndWait();
            return;
        }

        Song selectedSong = (Song) songList.getSelectionModel().getSelectedItem();
        Song tempSong = new Song(titleEdit.getText().trim(), artistEdit.getText().trim(), albumEdit.getText().trim(), yearEdit.getText().trim());
        if (selectedSong == null) {
            return;
        }
        if (selectedSong.getTitle().compareTo(tempSong.getTitle()) == 0 && selectedSong.getArtist().compareTo(tempSong.getArtist()) == 0) {
            ((Song) songList.getSelectionModel().getSelectedItem()).setalbum(tempSong.getalbum());
            ((Song) songList.getSelectionModel().getSelectedItem()).setYear(tempSong.getYear());
        } else {
            obsList.remove(songList.getSelectionModel().getSelectedIndex());
            if (add(tempSong) == -1) {
                add(selectedSong);
            }
            // TODO Autogenerated
        }
    }

    private void showSongDetails() {
        if (songList.getSelectionModel().getSelectedIndex() < 0) {
            return;
        }

        Song song = (Song) songList.getSelectionModel().getSelectedItem();
        titleEdit.setText(song.getTitle());
        artistEdit.setText(song.getArtist());
        albumEdit.setText(song.getalbum());
        yearEdit.setText(song.getYear());
    }

    private void sortList(ObservableList<Song> obsList) {
        Comparator<Song> comparator = Comparator.comparing(Song::lowerTitle).thenComparing(Song::lowerArtist);
        obsList.sort(comparator);
    }

    private boolean duplicate(Song song, ObservableList<Song> obsList) {
        String title = song.getTitle().toLowerCase();
        String artist = song.getArtist().toLowerCase();
        for (Song compare : obsList) {
            if (title.compareTo(compare.getTitle().toLowerCase()) == 0 && artist.compareTo(compare.getArtist().toLowerCase()) == 0) {
                return true;
            }
        }
        return false;
    }

    private boolean validYear(String year) {
        if (year.compareTo("") == 0) {
            return true;
        } else if (year.compareTo("0") == 0) {
            return false;
        } else if (year.contains("-")) {
            return false;
        } else {
            for (int i = 0; i < year.length(); i++) {
                if (year.charAt(i) < '0' || year.charAt(i) > '9') {
                    return false;
                }
            }
            int intYear = Integer.parseInt(year);
            if (intYear > 0) {
                return true;
            }
        }
        return true;
    }

}
