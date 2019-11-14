package TicTacToe;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TicTacToe extends Application {
	
	private boolean playable = true;
	private boolean turnX = true;
	private Tile[][] board = new Tile[3][3];
	private List<Combo> combos = new ArrayList<>();
	
	private int playCount = 1;
	
	public boolean winner = false;
	private BorderPane bp = new BorderPane();
	private Pane root = new Pane();
	private Text playerTurn = new Text("Player1's turn");
	private HBox hbox = new HBox();
	
	private Parent createContent() {
		bp.getChildren().addAll(root, hbox);
		hbox.getChildren().add(playerTurn);
		playerTurn.setFont(Font.font("Arial", 15));
		bp.setPrefSize(500, 500);
		root.setPrefSize(500, 500);
		
		
		for(int i = 0; i < 3; i++) {
			for(int ii = 0; ii < 3; ii++) {
				Tile tile = new Tile();
				tile.setTranslateX(ii * 166);
				tile.setTranslateY(i * 166);
				
				root.getChildren().add(tile);
				
				board[ii][i] = tile;
			}
		}
		
		for(int y = 0; y < 3; y++) {
			combos.add(new Combo(board[0][y], board[1][y], board[2][y]));
		}
		
		for(int x = 0; x < 3; x++) {
			combos.add(new Combo(board[x][0], board[x][1], board[x][2]));
		}
		
		combos.add(new Combo(board[0][0], board[1][1], board[2][2]));
		combos.add(new Combo(board[2][0], board[1][1], board[0][2]));
		
		return bp;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setScene(new Scene(createContent()));
		primaryStage.setTitle("ACP Project 4 - Richard Sheeder");
		primaryStage.show();
	}
	
	private void checkState() {
		for (Combo combo : combos) {
			if(combo.isComplete()) {
				playable = false;
				playWinAnimation(combo);
				break;
			}
		}
	}
	
	private void playWinAnimation(Combo combo) {
		Line line = new Line();
		line.setStrokeWidth(10);
		line.setStroke(Color.LIMEGREEN);
		line.setStartX(combo.tiles[0].getCenterX());
		line.setStartY(combo.tiles[0].getCenterY());
		line.setEndX(combo.tiles[0].getCenterX());
		line.setEndY(combo.tiles[0].getCenterX());
		root.getChildren().add(line);
		Timeline timeline = new Timeline();
		timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.0001), new KeyValue(line.endXProperty(), combo.tiles[2].getCenterX()), new KeyValue(line.endYProperty(), combo.tiles[2].getCenterY())));
		timeline.play();
		playerTurn.setText(null);
		if(turnX == false) {
			JOptionPane message = new JOptionPane("Player1 has won the game");
			JDialog dialog = message.createDialog("Winner!");
			dialog.setModal(false);
			dialog.setVisible(true);
			winner = true;
			return;
		}
		
		if(turnX == true) {
			JOptionPane message = new JOptionPane("Player2 has won the game");
			JDialog dialog = message.createDialog("Winner!");
			dialog.setModal(false);
			dialog.setVisible(true);
			winner = true;
			return;
		}
		
		
	}
	
	private void drawGame() {
		JOptionPane message = new JOptionPane("This game was a draw");
		JDialog dialog = message.createDialog("Draw Game");
		dialog.setModal(false);
		dialog.setVisible(true);
		playerTurn.setText(null);
		return;
	}
	
	private class Combo {
		private Tile[] tiles;
		public Combo(Tile ...tiles ) {
			this.tiles = tiles;
		}
		
		public boolean isComplete() {
			if (tiles[0].getValue().isEmpty()) {
				return false;
			}
			return tiles[0].getValue().equals(tiles[1].getValue()) && tiles[0].getValue().equals(tiles[2].getValue());
		}
	}
	
	private class Tile extends StackPane {
		private Text text = new Text();
		
		public Tile() {
			Rectangle border  = new Rectangle(166, 166);
			border.setFill(null);
			border.setStroke(Color.BLACK);
			
			text.setFont(Font.font(120));
			
			setAlignment(Pos.CENTER);
			getChildren().addAll(border, text);
			
			setOnMouseClicked(event -> {
				if(!playable) {
					return;
				}
				
				if (event.getButton() == MouseButton.PRIMARY) {
					if(turnX) {
						if(text.getText() == "O" || text.getText() == "X") {
							JOptionPane message = new JOptionPane("Cannot place X here");
							JDialog dialog = message.createDialog("Illegal move");
							dialog.setModal(false);
							dialog.setVisible(true);
						} else {
						drawX();
						turnX = false;
						playCount++;
						checkState();
						if(playCount == 10 && winner == false) {
							drawGame();
						}
						
						return;
						}
						
					}
					
					if(!turnX) {
						if(text.getText() == "O" || text.getText() == "X") {
							JOptionPane message = new JOptionPane("Cannot place O here");
							JDialog dialog = message.createDialog("Illegal move");
							dialog.setModal(false);
							dialog.setVisible(true);
						} else {
							drawO();
						turnX = true;
						playCount++;
						checkState();
						if(playCount == 10 && winner == false) {
							drawGame();
						}
						
						return;
						}
					}	
				}
			});
		}
		
		public double getCenterX() {
			return getTranslateX() + 85;
		}
		
		public double getCenterY() {
			return getTranslateY() + 85;
		}
		
		public String getValue() {
			return text.getText();
		}
		
		private void drawX() {
			if(text.getText() == "O" || text.getText() == "X") {
				System.out.println("Illegal Move");
				drawX();
				return;
			} else {
			text.setText("X");
			playerTurn.setText("Player2's turn");
			text.setFill(Color.BLUE);
			return;
			}
		}
		
		private void drawO() {
			if(text.getText() == "O" || text.getText() == "X") {
				System.out.println("Illegal Move");
				drawO();
				return;
			} else {
			text.setText("O");
			playerTurn.setText("Player1's turn");
			text.setFill(Color.RED);
			return;
			}
		}
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}
}
