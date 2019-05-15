//CHECKSTYLE:OFF
package View;

import Controller.CalendarController;
import Controller.DateUtils;
import Model.Event;
import Model.Repetition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.CheckListView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.text.DateFormatSymbols;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainViewController implements Initializable {
    public static final Logger logger = LoggerFactory.getLogger(MainViewController.class);

    private final int LENGTH_OF_WEEK = 7;
    private final int GRID_PANE_COLS = LENGTH_OF_WEEK;
    private final int GRID_PANE_ROWS = 7;
    private LocalDate currentViewedDate;
    private int firstDayIndex;

    private final DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);
    private final DateTimeFormatter yearFormatter = DateTimeFormatter.ofPattern("yyyy", Locale.ENGLISH);
    @FXML
    private TextField searchField;
    @FXML
    private Button addEventButton;
    @FXML
    private CheckListView calendarCheckListView;
    @FXML
    private ComboBox yearComboBox;
    @FXML
    private ComboBox monthComboBox;
    @FXML
    private Button mainViewQuitButton;
    @FXML
    private AnchorPane addEventForm;
    @FXML
    private TextField nameTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private CheckBox annuallyCheckBox;
    @FXML
    private CheckBox monthlyCheckBox;
    @FXML
    private CheckBox weeklyCheckBox;
    @FXML
    private CheckBox dailyCheckBox;
    @FXML
    private TextArea descriptionTextArea;
    @FXML
    private Button saveEventButton;
    @FXML
    private Button cancelButton;
    @FXML
    private GridPane monthGridPane;
    @FXML
    private Pane shadowPane;
    @FXML
    private Label errorMessages;
    @FXML
    private BorderPane eventsDetailPane;
    @FXML
    private ListView eventsList;

    private CalendarController calendarController;

    private final Background nonActiveMonthBG = new Background(
            new BackgroundFill( Color.web( "#4f4f4f" ),
                    CornerRadii.EMPTY,
                    Insets.EMPTY ) );

    private final Background activeMonthBG = new Background(
            new BackgroundFill( Color.web( "#F2F2F2" ),
                    CornerRadii.EMPTY,
                    Insets.EMPTY ) );

    @FXML
    private void addEventButtonClicked(ActionEvent event) {
        addEventForm.setVisible(true);
        shadowPane.setVisible(true);
    }

    @FXML
    private void onShadowPaneClicked(MouseEvent event) {
        this.logger.info("Shadow pane clicked, cancelling add event form.");
        resetAddEventForm();
        eventsDetailPane.setVisible(false);
    }

    @FXML
    private void onCancelButtonPressed(ActionEvent event) {
        this.logger.info("Cancel button pressed, cancelling add event form.");
        resetAddEventForm();
    }

    @FXML
    private void onDayClick(){
        //if day has no events do nothing
        //otherwise go to DAY VIEW :D :D YAAAY

    }

    @FXML
    private void onSaveClicked() {
        if (nameTextField.getText().isEmpty()) {
            errorMessages.setText("ERROR: Name is required!");
        } else if( datePicker.getValue() == null) {
            errorMessages.setText("ERROR: Date is required!");
        }
        else {
            errorMessages.setText("");
            ArrayList<Repetition> repetitions = getRepetitions();
            logger.debug("Selected repetitions: " + repetitions);
            addEvent(nameTextField.getText(), datePicker.getValue(), repetitions, descriptionTextArea.getText());
            this.resetAddEventForm();
        }
    }

    private void addEvent(String name, LocalDate date, ArrayList<Repetition> repetitions, String description) {
        logger.info("Adding event: " + name + " on date: " + date.toString());
        Event event = calendarController.createEvent(name, date, repetitions, description);
        YearMonth yearMonth = YearMonth.from(this.currentViewedDate);

        if (calendarController.isEventThisMonth(event, yearMonth)) {
            logger.info("Event occured this month, rendering it!");
            renderEvent(event);
        } else {
            logger.info("Event did not occur this month, not rendering it!");

        }

        if (calendarController.hasOccurrenceThisMonth(event, yearMonth)) {
            logger.info("Event has occurence this month, rendering occurences!");
            calendarController.getOccurrences(event, yearMonth).forEach(eventToRender -> renderEvent(eventToRender));
        }
    }

    @FXML
    private void quitApp(){
        Stage stage = (Stage) mainViewQuitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onMonthChanged(ActionEvent event) {
        this.logger.info("Month changed!");
        String monthName = monthComboBox.getValue().toString();
        try {
             Month month = Month.from(monthFormatter.parse(monthName));

            this.logger.info("Got the month: " + month);
            this.currentViewedDate = createNewDate(month);
            this.logger.info("created the new Date: " + this.currentViewedDate);
            this.initView(this.currentViewedDate);
            this.logger.info("on month changed inited succesfully");


        } catch (Exception e) {
            this.logger.error("Invalid month: " + monthComboBox.getValue() + " encountered! " + e.getMessage());
        }
    }

    @FXML
    private void onYearChanged(ActionEvent event) {
        this.logger.info("Year changed!");
        String monthName = yearComboBox.getValue().toString();
        try {
            Year year = Year.from(yearFormatter.parse(monthName));
            this.logger.info("Got the month: " + year);

            this.currentViewedDate = createNewDate(year);
            this.initView(this.currentViewedDate);

        } catch (Exception e) {
            this.logger.error("Invalid year encountered! " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logger.info("Initializing Month view.");
        this.currentViewedDate = LocalDate.now();
        this.calendarController = new CalendarController();

        initYearCombobox();
        initMonthCombobox();
        initView(currentViewedDate);
    }

    public void initEvents() {
        this.logger.debug("Initing events");
        YearMonth currentYearMonth = YearMonth.from(this.currentViewedDate);
        List<Event> eventsThisMonth = calendarController.getEventsThisMonth(currentYearMonth);

        eventsThisMonth.forEach(event -> renderEvent(event));
        this.logger.debug("Events this month " + eventsThisMonth);
    }

    private void renderEvent(Event event) {
        LocalDate date = event.getDate();
        this.logger.debug("Rendering event: " + event.getName());

        //todo if eventsToday are > 3 show "<COUNT> EVENTS"
        VBox cell = (VBox) monthGridPane.getChildren().get(firstDayIndex + date.getDayOfMonth());
        cell.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
            showDetailView();
            mouseEvent.consume();
        });
        this.logger.trace("Found event cell: " + cell);
        cell.getChildren().add(new Label("EVENT: " + event.getName()));
    }

    public void showDetailView() {
        //todo toggle detailView visibility
        shadowPane.setVisible(true);
        eventsDetailPane.setVisible(true);
    }

    private LocalDate createNewDate(Year year){
        try {
            LocalDate newDate = LocalDate.of(year.getValue(), this.currentViewedDate.getMonthValue(), 1);
            return newDate;
        } catch (Exception e) {
            this.logger.error("Invalid month somehow, returning with dayOfmonth 1: " + e.getMessage());
            return this.currentViewedDate;
        }
    }

    private LocalDate createNewDate(Month month){
        try {
            LocalDate newDate = LocalDate.of(this.currentViewedDate.getYear(), month, 1);
            return newDate;
        } catch (Exception e) {
            this.logger.error("Invalid month somehow, returning with dayOfmonth 1: " + e.getMessage());
            return this.currentViewedDate;
        }
    }

    private ArrayList<Repetition> getRepetitions() {
        ArrayList<Repetition> repetitionArray = new ArrayList<>();
        if (annuallyCheckBox.isSelected()) {
            repetitionArray.add(Repetition.ANNUALLY);
        }
        if (monthlyCheckBox.isSelected()) {
            repetitionArray.add(Repetition.MONTHLY);
        }
        if (weeklyCheckBox.isSelected()) {
            repetitionArray.add(Repetition.WEEKLY);
        }
        if(dailyCheckBox.isSelected()){
            repetitionArray.add(Repetition.DAILY);
        }

        return repetitionArray;
    }

    public void initView(LocalDate currentDate) {
        initMonthGrid();

        int daysInMonthCount = DateUtils.getLengthOfMonth(currentDate);
        //how many days in month
        LocalDate firstDayOfMonth = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), 1);
        DayOfWeek firstDayOfWeekInMonth = firstDayOfMonth.getDayOfWeek();

        logger.debug("First weekday in month is: ", firstDayOfWeekInMonth);

        LocalDate previousMonth = currentDate.minusMonths(1);
        int daysInPreviousMonthCount = DateUtils.getLengthOfMonth(previousMonth);
        fillYearCombobox(currentDate.getYear());
        //get month index
        logger.debug("Setting month in combobox: " + (currentDate.getMonthValue() - 1));

        fillMonthCombobox(currentDate.getMonthValue() - 1);
        logger.debug("Filling calendar: " + (currentDate.getMonthValue() - 1));

        fillCalendar(firstDayOfWeekInMonth, daysInMonthCount, daysInPreviousMonthCount);
        initEvents();
    }

    private void initMonthGrid() {
        monthGridPane.getChildren().forEach(child -> {
            if (child instanceof VBox) {
                ((VBox) child).getChildren().clear();
                ((VBox) child).setBackground(activeMonthBG);
            }
        });
    }

    private void initYearCombobox() {
        List<Integer> years = Stream.iterate(1990, n -> n + 1)
                .limit(100)
                .collect(Collectors.toList());

        yearComboBox.setItems(FXCollections.observableArrayList(years));
    }

    private void initMonthCombobox() {
        String[] months = new DateFormatSymbols(Locale.ENGLISH).getMonths();
        monthComboBox.setItems(FXCollections.observableArrayList(months));
    }

    private void fillYearCombobox(int year) {
        yearComboBox.setValue(year);
    }

    private void fillMonthCombobox(int monthIndex) {
        monthComboBox.setValue(new DateFormatSymbols(Locale.ENGLISH).getMonths()[monthIndex]);
    }

    private void resetAddEventForm() {
        nameTextField.clear();
        datePicker.getEditor().clear();
        annuallyCheckBox.setSelected(false);
        monthlyCheckBox.setSelected(false);
        weeklyCheckBox.setSelected(false);
        dailyCheckBox.setSelected(false);
        descriptionTextArea.setText("");
        addEventForm.setVisible(false);
        shadowPane.setVisible(false);
    }

    private void fillCalendar(DayOfWeek dayOfWeek, int daysInMonth, int previousMonthDays) {
        //Days of Week are numbered from 1, get index value of day
        int indexOfFirstCell = dayOfWeek.getValue() - 1;
        firstDayIndex = indexOfFirstCell + GRID_PANE_COLS - 1;

        //if it's not the first day, set previous days to inactive months last days
        if (indexOfFirstCell != 0) {
            fillPreviousMonth(previousMonthDays, indexOfFirstCell);
        }
        logger.info("Finished prev month filling");
        fillNextMonth(GRID_PANE_COLS + indexOfFirstCell + daysInMonth);
        logger.info("Finished next month filling");
        fillCurrentMonth(indexOfFirstCell, daysInMonth);
        logger.info("Finished current month filling");

    }

    private void fillCurrentMonth(int indexOfFirstCell, int daysInMonth) {
        int colIndex = indexOfFirstCell;
        int rowIndex = 0;

        for(int dayIndex = 1; dayIndex <= daysInMonth; dayIndex++ ) {
            if(colIndex == GRID_PANE_COLS) {
                colIndex = 0;
                rowIndex++;
            }

            //Rows start from 1 (first row of gridpane shows the weekday labels)
            fillGridCell(dayIndex, rowIndex + 1, colIndex);
            colIndex++;
        }
    }

    //todo bovit this function with the EVENTS to show on the cell
    private void fillGridCell(int dayOfMonth, int rowIndex, int colIndex) {
        //logger.debug("Filling grid cell: : [" + rowIndex + "][" + colIndex + "] with day of month: " + dayOfMonth);

        if (!(monthGridPane.getChildren().get(rowIndex * GRID_PANE_COLS + colIndex) instanceof VBox)) {
            logger.debug("Not a VBOX grid cell: : [" + rowIndex + "][" + colIndex + "] with day of month: " + dayOfMonth);
        } else {
            VBox cell = (VBox) monthGridPane.getChildren().get(rowIndex * GRID_PANE_COLS + colIndex);
            cell.getChildren().clear();
            cell.getChildren().add(new Label("" + dayOfMonth));
        }
    }

    private void fillPreviousMonth(int previousMonthDays, int firstDayOfMonthIndex) {

        for (int i = 0; i < firstDayOfMonthIndex; i++) {

            if (!(monthGridPane.getChildren().get(GRID_PANE_COLS + i) instanceof VBox)) {
                logger.info("fillPrev - cellIndex: " + (GRID_PANE_COLS + i));
                logger.info("THIS CELL IS NOT A VBOX: " + monthGridPane.getChildren().get(GRID_PANE_COLS + i));
            }

            VBox cell = (VBox) monthGridPane.getChildren().get(GRID_PANE_COLS + i);
            cell.setBackground(nonActiveMonthBG);
            cell.getChildren().clear();
            cell.getChildren().add(new Label("" + (previousMonthDays - (firstDayOfMonthIndex - (i + 1)))));
        }
    }

    private void fillNextMonth(int lastDayIndex) {
        for (int cellIndex = lastDayIndex; cellIndex < GRID_PANE_COLS * GRID_PANE_ROWS; cellIndex++) {
            if (!(monthGridPane.getChildren().get(cellIndex) instanceof VBox)) {
                logger.info("fillNEXT - cellIndex: " + cellIndex);
                logger.info("THIS CELL IS NOT A VBOX: " + monthGridPane.getChildren().get(cellIndex));
            }

            VBox cell = (VBox) monthGridPane.getChildren().get(cellIndex);
            cell.getChildren().clear();

            cell.setBackground(nonActiveMonthBG);
            cell.getChildren().add(new Label("" + (cellIndex - (lastDayIndex - 1))));
        }
    }
}
