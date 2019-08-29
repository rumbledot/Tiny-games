package application;

import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;


public class Main extends Application {

	private ArrayList<Circular> cirRow = new ArrayList<>();
	private ArrayList<Circular> cirCol = new ArrayList<>();
	private Curve[][] curves;
	private Circular[][] circulars;

	private Timeline timeline;

	private Scene s;
	private Pane canvas;

	private int width, height;
	private int cRow, cCol;
	private double d, r;

	final public static double PI = 3.1415;

	public int w() {
		return this.width;
	}

	public int h() {
		return this.height;
	}

	@Override
	public void start(Stage primaryStage) {

		width = 600;
		height = 600;
		cRow = 6; 
		cCol = 6;
		d = width / cCol;
		r = d / 2;

		curves = new Curve[cRow][cCol];
		circulars = new Circular[cRow][cCol];

		initialiseUI(primaryStage);

		for (int row = 1; row < cRow; row++) {
			Circular c = new Circular(row * d + r, r, r - 5, row, 0);

			cirRow.add(c);
			canvas.getChildren().add(c);
			canvas.getChildren().add(c.p());
			canvas.getChildren().add(c.l());
		}
		
		System.out.println();

		for (int col = 1; col < cCol; col++) {
			Circular c1 = new Circular(r, col * d + r, r - 5, col, 1);

			cirCol.add(c1);
			canvas.getChildren().add(c1);
			canvas.getChildren().add(c1.p());
			canvas.getChildren().add(c1.l());
		}

		Circular cR; Circular cC;
		for (int col = 0; col < cCol - 1; col++) {
			cC = cirCol.get(col);
			for (int row = 0; row < cRow - 1; row++) {
				cR = cirRow.get(row);
				Curve curve = new Curve(cR.pX(), cC.pY());
				curves[row][col] = curve;
				canvas.getChildren().addAll(curve.c());
			}
		}
		
		KeyFrame frame = new KeyFrame(Duration.millis(16), new EventHandler<ActionEvent>() {

			@Override
			
			public void handle(ActionEvent e) {
				
				for (Circular cR : cirRow) {
					cR.move();
				}
				for (Circular cC : cirCol) {
					cC.move();
				}
				
				Circular cR; Circular cC;
				for (int col = 0; col < cCol - 1; col++) {
					cC = cirCol.get(col);
					for (int row = 0; row < cRow - 1; row++) {
						cR = cirRow.get(row);
						Curve c = curves[row][col];
						c.move(cR.pX(), cC.pY());
					}
				}
			}

		});

		timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setAutoReverse(true);
		timeline.getKeyFrames().add(frame);
		timeline.play();

	}

	private void initialiseUI(Stage primaryStage) {
		HBox menu = new HBox();
		menu.setPrefSize(600, 25);
		menu.setPadding(new Insets(5, 5, 5, 5));
		menu.setSpacing(5);
		menu.setAlignment(Pos.CENTER);

		VBox borderL = new VBox();
		borderL.setPrefSize(25, 600);

		VBox borderR = new VBox();
		borderR.setPrefSize(25, 600);

		HBox borderB = new HBox();
		borderB.setPrefSize(600, 25);

		canvas = new Pane();
		canvas.setStyle("-fx-background-color : #C0B07E; height:600; width:600");

		BorderPane g = new BorderPane();
		g.setTop(menu);
		g.setLeft(borderL);
		g.setRight(borderR);
		g.setBottom(borderB);
		g.setCenter(canvas);

		s = new Scene(g, 650, 650);

		primaryStage.setTitle("Game of Life");
		primaryStage.setResizable(false);
		primaryStage.setScene(s);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
