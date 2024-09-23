package application;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.Comparator;

public class TheCode extends Application {

	private ObservableList<Task> tasks;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		tasks = FXCollections.observableArrayList();

		VBox root = new VBox(10);
		root.setPadding(new Insets(10));

		// Input fields
		TextField taskField = new TextField();
		taskField.setPromptText("Enter a new task");
		DatePicker datePicker = new DatePicker();
		datePicker.setPromptText("Select a due date");
		Button addButton = new Button("Add Task");

		HBox inputBox = new HBox(10, taskField, datePicker, addButton);
		inputBox.setHgrow(taskField, Priority.ALWAYS);

		// Task list
		ListView<Task> listView = new ListView<>(tasks);
		// how the cells in a ListView
		listView.setCellFactory(lv -> new TaskCell());

		// Buttons
		Button markCompletedButton = new Button("Mark Selected as Completed");
		Button deleteCompletedButton = new Button("Delete Completed Tasks");

		addButton.setOnAction(e -> {
			String taskText = taskField.getText();
			LocalDate dueDate = datePicker.getValue();
			if (!taskText.isEmpty() && dueDate != null) {
				tasks.add(new Task(taskText, dueDate));
				taskField.clear();
				datePicker.setValue(null);
				sortTasks();
			}
		});

		//The Complete Button xd
		markCompletedButton.setOnAction(e -> {
			Task selectedTask = listView.getSelectionModel().getSelectedItem();
			if (selectedTask != null) {
				selectedTask.setCompleted(true);
				sortTasks();
			}
		});

		//The Delete Button xd
		deleteCompletedButton.setOnAction(e -> {
			tasks.removeIf(Task::isCompleted);
		});

		root.getChildren().addAll(inputBox, listView, markCompletedButton, deleteCompletedButton);

		Scene scene = new Scene(root, 600, 400);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Oaky TDL");
		primaryStage.show();
	}

	//tasks is a list and comparing the getter methods from THE Data
	private void sortTasks() {
		FXCollections.sort(tasks, Comparator.comparing(Task::isCompleted).thenComparing(Task::getDueDate));
	}

	//THE Data (For building a Task object)
	public static class Task {
		private final String description;
		private final LocalDate dueDate;
		private boolean completed;

		public Task(String description, LocalDate dueDate) {
			this.description = description;
			this.dueDate = dueDate;
			this.completed = false;
		}

		public String getDescription() {
			return description;
		}

		public LocalDate getDueDate() {
			return dueDate;
		}

		public boolean isCompleted() {
			return completed;
		}

		public void setCompleted(boolean completed) {
			this.completed = completed;
		}

		@Override
		public String toString() {
			return description + " (Due: " + dueDate + ")";
		}
	}

	//THE cart TaskCell(H box 1 khu built by the object of Task)"
	public static class TaskCell extends ListCell<Task> {
		private final HBox content;
		private final Label descriptionLabel;
		private final Label dueDateLabel;

		public TaskCell() {
			descriptionLabel = new Label();
			dueDateLabel = new Label();
			dueDateLabel.setStyle("-fx-text-fill: grey;");
			content = new HBox(10, descriptionLabel, dueDateLabel);
			content.setHgrow(descriptionLabel, Priority.ALWAYS);
		}

		//what is this ?
		@Override
		public void updateItem(Task task, boolean empty) {
			super.updateItem(task, empty);
			if (task == null || empty) {
				setGraphic(null);
			} else {
				descriptionLabel.setText(task.getDescription());
				dueDateLabel.setText(task.getDueDate().toString());
				if (task.isCompleted()) {
					descriptionLabel.setStyle("-fx-strikethrough: true;");
				} else {
					descriptionLabel.setStyle("-fx-strikethrough: false;");
				}
				setGraphic(content);
			}
		}
	}
}
