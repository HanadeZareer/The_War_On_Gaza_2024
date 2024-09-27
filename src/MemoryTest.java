import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class MemoryTest extends Application {
	private static ArrayList<Martyr> nameList = new ArrayList<>();
	private static Label[] nameLabels = new Label[100];
	private TextField first = new TextField();
	private TextField second = new TextField();
	private Button submit = new Button("Submit");
	private Button clear = new Button("Clear");
	private Label response = new Label();
	private static File file = new File("MartyrsList.dat");

	public static void main(String[] args) {
		// read martyr from the file to upload it on the nameList
		try (DataInputStream input = new DataInputStream(new FileInputStream(file))) {
			while (input.available() != -1) {
				String[] data = (input.readUTF().split(" "));
				nameList.add(new Martyr(data[0].trim(), data[1]));
			}
		} catch (EOFException ex) {
		} catch (IOException ex) {
		}
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// create the main scene
		Button createListBT = new Button("Create Martyr List Window");
		Button memoTestBT = new Button("Memory Test Window");
		VBox vBox1 = new VBox(10, createListBT, memoTestBT);
		vBox1.setAlignment(Pos.CENTER);

		// show the main scene
		Scene mainScene = new Scene(vBox1, 400, 400);
		primaryStage.setScene(mainScene);
		primaryStage.setTitle("The War on Gaza");
		primaryStage.show();

		// create martyr list window
		createListBT.setOnAction(e -> {
			Label messageLabel = new Label("");
			messageLabel.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.REGULAR, 20));
			messageLabel.setTextFill(Color.RED);

			Label addMartyrL = new Label("Add Martyr: (Name date of martyrdom)");
			TextField nameDateTxtF = new TextField("");
			Button addBT = new Button("Add to File");

			HBox hBox1 = new HBox(10, addMartyrL, nameDateTxtF, addBT);
			hBox1.setAlignment(Pos.CENTER);

			VBox vBox2 = new VBox(20, hBox1, messageLabel, memoTestBT);
			vBox2.setAlignment(Pos.CENTER);

			// add data to the file
			addBT.setOnAction(a -> {
				if (!(nameDateTxtF.getText().equals(" "))) {
					try (DataOutputStream output = new DataOutputStream(new FileOutputStream(file, true))) {
						String[] data = (nameDateTxtF.getText().trim()).split(" ");
						if (data.length == 2) {
							if (isValidDate(data[1]).equalsIgnoreCase("Added successfully!")) {
								String s = nameDateTxtF.getText().trim();
								output.writeUTF(s + "\n");
								// add martyr to the nameList
								nameList.add(new Martyr(data[0].trim(), data[1].trim()));
								messageLabel.setText("Added successfully!");
							} else
								messageLabel.setText(isValidDate(data[1]));
						} else
							messageLabel.setText("Error entry!");
					} catch (IOException ex) {
						System.out.println(ex.toString());
					}
				} else
					messageLabel.setText("You forgot to enter a martyr!");
			});

			Scene addMartyrScene = new Scene(vBox2, 550, 150);
			primaryStage.setScene(addMartyrScene);
			primaryStage.setTitle("Add a Martyr to the File");
		});

		// create memory test window
		memoTestBT.setOnAction(e -> {
			BorderPane borderPane = new BorderPane();
			response.setText(" ");
			response.setTextFill(Color.RED);
			response.setFont(Font.font("", FontWeight.BOLD, 20));

			// add the header scene
			Text headerText = new Text("Test your memory");
			headerText.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
			headerText.setTextAlignment(TextAlignment.CENTER);
			VBox vBox2 = new VBox(10, headerText,
					new Text("Hey, my friend! Test your memory to see if you remember who was martyred before."),
					new Text("Pick two Martyr names from the following list, "
							+ "enter them in the boxes in the correct order (dete of martyred), "
							+ "and then press the Submit button. "));

			vBox2.setAlignment(Pos.CENTER);
			borderPane.setAlignment(vBox2, Pos.CENTER);
			borderPane.setTop(vBox2);

			GridPane gridNames = new GridPane(30, 10);
			gridNames.setAlignment(Pos.CENTER);

			// add the the martyr's names
			int n = 0;
			for (int i = 0; i < 10; i++) {
				for (int j = 0; j < 8; j++) {
					if (n == nameList.size()) {
						break;
					}
					nameLabels[n] = new Label(nameList.get(n).getMartyrName());
					nameLabels[n].setFont(Font.font(15));
					gridNames.add(nameLabels[n], j, i);
					n++;
				}
			}

			borderPane.setCenter(gridNames);

			HBox hBox1 = new HBox(30, first, new Label("martyred before:"), second);
			first.setPrefColumnCount(10);
			second.setPrefColumnCount(10);
			hBox1.setAlignment(Pos.CENTER);

			ComboBox<String> comboBox = new ComboBox<>();
			comboBox.getItems().addAll("White", "Red", "Green", "Blue", "Yellow");
			comboBox.setValue("White");
			HBox hBox2 = new HBox(10, submit, clear, comboBox);

			// clear the text of the TextFeilds
			clear.setOnAction(c -> {
				first.setText(" ");
				second.setText(" ");
				response.setText(" "); // clear the text of the response label
			});

			// check if the name is exist in the list
			submit.setOnAction(s -> {
				response.setText(" ");
				// // check if the first and second name isn't exist
				if (!inList(first.getText().trim()) && !inList(second.getText().trim())) {
					response.setText("First and Second entry are not in name list – check spelling.");
				}
				// check if the first name isn't exist
				else if (!inList(first.getText().trim())) {
					response.setText("First entry not in name list – check spelling.");
				}
				// check if the second name isn't exist
				else if (!inList(second.getText().trim())) {
					response.setText("Second entry not in name list – check spelling.");
				}
				// check if the first and second name not null
				else if (first.getText().trim().equals("") || second.getText().trim().equals("")) {
					response.setText("Enter names in both boxes. Then press Submit.");
				}
				// check if first and second name not the same
				else if (inList(first.getText().trim()) && inList(second.getText().trim())) {
					if (first.getText().trim().equalsIgnoreCase(second.getText().trim())) {
						response.setText("You entered the same names. Try again.");
					} else {
						// the first and second name now can compare
						compareTo(first.getText().trim(), second.getText().trim());
					}
				}
			});

			// change background's color
			comboBox.setOnAction(a -> {
				if (comboBox.getValue().equals("White"))
					borderPane.setStyle("-fx-background-color: White");
				else if (comboBox.getValue().equals("Red"))
					borderPane.setStyle("-fx-background-color: red");
				else if (comboBox.getValue().equals("Green"))
					borderPane.setStyle("-fx-background-color: lightgreen");
				else if (comboBox.getValue().equals("Blue"))
					borderPane.setStyle("-fx-background-color: lightblue");
				else if (comboBox.getValue().equals("Yellow"))
					borderPane.setStyle("-fx-background-color: lightyellow");
				else
					borderPane.setStyle("-fx-background-color: lightyellow");
			});
			hBox2.setAlignment(Pos.CENTER);

			VBox vBox3 = new VBox(15, hBox1, hBox2, response, createListBT);
			vBox3.setAlignment(Pos.CENTER);
			borderPane.setBottom(vBox3);

			Scene testScene = new Scene(borderPane, 800, 400);
			primaryStage.setScene(testScene);
			primaryStage.setTitle("Memory Test");
		});

	}

	/**
	 * to check if the name in the nameList
	 * 
	 * @param word
	 * @return boolean
	 */
	private boolean inList(String word) {
		boolean found = false;
		for (int i = 0; i < nameList.size(); i++) {
			if (word.equalsIgnoreCase((nameList.get(i).getMartyrName()).trim())) {
				found = true;
			}
		}
		return found;
	}

	/**
	 * to check if the date of martyrdom is valid or not
	 * 
	 * @param s
	 * @return text
	 */
	private String isValidDate(String s) {
		String text = "";
		int day = 0, month = 0, year = 0;
		// split dateOfMartyrdom into day , month , year
		String[] date = s.split("/");
		if (date.length == 3) {
			try {
				day = Integer.valueOf(date[0]);
				month = Integer.parseInt(date[1]);
				year = Integer.parseInt(date[2]);
				// check if the date is in correct range
				if (month > 0 && month <= 12 && year >= 1948 && day > 0 && day <= 31) {
					// check if the day is in the rang of month's days
					Calendar calendar = new GregorianCalendar(year, month - 1, day);
					calendar.set(Calendar.DAY_OF_MONTH, 1);
					// get the maximum number of days in the month
					int maxDayInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
					if (day <= maxDayInMonth) {
						text = "Added successfully!";

					} else
						text = "out of range day in month " + month;
				} else
					text = "Out of range date!";
			} catch (NumberFormatException ex) { // if the date isn't integer
				text = ex.toString();
			}
		} else
			text = "wrong date format! : day/month/year";
		return text;
	}

	/**
	 * to compare the date of martyrdom
	 * 
	 * @param firstMartyrName
	 * @param secondMartyrName
	 */
	public void compareTo(String firstMartyrName, String secondMartyrName) {
		String[] firstDate = new String[3];
		String[] secondDate = new String[3];
		// Neither entry is in the name list.
		for (int i = 0; i < nameList.size(); i++) {
			if (firstMartyrName.equalsIgnoreCase(nameList.get(i).getMartyrName())) {
				firstDate = nameList.get(i).getDateOfMartyrdom().split("/");
			}
		}
		for (int j = 0; j < nameList.size(); j++) {
			if (secondMartyrName.equalsIgnoreCase(nameList.get(j).getMartyrName())) {
				secondDate = nameList.get(j).getDateOfMartyrdom().split("/");
			}
		}

		int day1 = Integer.parseInt(firstDate[0].trim()); // day of martyrdom for the first martyr
		int day2 = Integer.parseInt(secondDate[0].trim()); // day of martyrdom for the second martyr
		int month1 = Integer.parseInt(firstDate[1].trim()); // month of martyrdom for the first martyr
		int month2 = Integer.parseInt(secondDate[1].trim()); // month of martyrdom for the second martyr
		int year1 = Integer.parseInt(firstDate[2].trim()); // year of martyrdom for the first martyr
		int year2 = Integer.parseInt(secondDate[2].trim()); // year of martyrdom for the second martyr

		// compare the date of martyrdom between the two martyr
		if (year1 < year2 || (year1 == year2 && month1 < month2) || (year1 == year2 && month1 == month2 && day1 < day2))
			response.setText("You are correct!");
		else
			response.setText("Wrong. Try again..");
	}

}
