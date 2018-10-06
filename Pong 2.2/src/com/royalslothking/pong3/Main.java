/* First (real) project without  following a tutorial! :D
* 
* KNOWN ISSUES: AudioClip will not work unless using a global system path (C:/%INSTALL_DIR%/sound.wav), sound.wav not found in runnable jar.
* Ball will sometimes bypass collision 
*/

package com.royalslothking.pong3;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

	Stage window;

	private static int windowSizeX = 600;
	private static int windowSizeY = 400;

	// Player Speeds
	private static double f = 10.0;

	private static double bfX = 2.0;
	private static double bfY = 2.0;

	private static int ballSize = 10;

	// Are buttons pressed?
	private static boolean up = false;
	private static boolean down = false;
	private static boolean up2 = false;
	private static boolean down2 = false;

	private int player1Score = 0;
	private int player2Score = 0;

	// For FPS counter
	private final long[] frameTimes = new long[100];
	private int frameTimeIndex = 0;
	private boolean arrayFilled = false;

	Rectangle player1 = makeRectangle(10, (windowSizeY / 2 - 50), 10, 100, Color.WHITE);
	Rectangle player2 = makeRectangle(windowSizeX - 20, (windowSizeY / 2 - 50), 10, 100, Color.WHITE);
	Rectangle ball = makeRectangle(windowSizeX / 2, windowSizeY / 2, ballSize, ballSize, Color.WHITE);

	// AudioClip reflectSound = new AudioClip("file:src/resources/sound.wav");

	Label score1 = new Label();
	Label score2 = new Label();
	Label fpsCounter = new Label();

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		window = primaryStage;

		StackPane root2 = new StackPane();
		Scene scene = new Scene(createContent(), windowSizeX, windowSizeY);
		Scene menu = new Scene(root2, windowSizeX, windowSizeY);

		fpsCounter.setTextFill(Color.WHITE);

		// I spent way too long getting this to work.
		Image bgImg = new Image("/resources/bg.jpg");
		ImageView bgView = new ImageView(bgImg);

		Button btn = new Button("Start");
		Button btn2 = new Button("Exit");

		Rectangle bgMenu = new Rectangle(0, 0, windowSizeX, windowSizeY);

		bgMenu.setFill(Color.BLACK);

		AnimationTimer timer = new AnimationTimer() {

			@Override
			public void handle(long now) {
				onUpdate();

				long oldFrameTime = frameTimes[frameTimeIndex];
				frameTimes[frameTimeIndex] = now;
				frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
				if (frameTimeIndex == 0) {
					arrayFilled = true;
				}

				if (arrayFilled) {
					long elapsedNanos = now - oldFrameTime;
					long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
					double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
					fpsCounter.setText(String.format("UPS: %.3f", frameRate));

				}
			};

		};

		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode key = event.getCode();

				if (key == KeyCode.W) {
					up = true;
				} else if (key == KeyCode.S) {
					down = true;
				} else if (key == KeyCode.UP) {
					up2 = true;
				} else if (key == KeyCode.DOWN) {
					down2 = true;
				} else if (key == KeyCode.ESCAPE) {
					window.setScene(menu);
					timer.stop();
					player1Score = 0;
					player2Score = 0;
				}

			}

		});

		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				KeyCode key = event.getCode();

				if (key == KeyCode.W) {
					up = false;
				} else if (key == KeyCode.S) {
					down = false;
				} else if (key == KeyCode.UP) {
					up2 = false;
				} else if (key == KeyCode.DOWN) {
					down2 = false;
				}

			}

		});

		btn.setTranslateY(-10);
		btn2.setTranslateY(25);

		root2.getChildren().addAll(bgMenu, bgView, btn, btn2);

		btn.setOnAction(e -> {
			window.setScene(scene);
			onStart();
			timer.start();
		});

		btn2.setOnAction(e -> {
			exit();
		});

		window.setOnCloseRequest(e -> exit());
		window.getIcons().add(new Image("/resources/favicon.jpg"));
		window.setTitle("Pong");
		window.setResizable(false);
		window.setScene(menu);
		window.sizeToScene();
		window.show();

	}

	public Parent createContent() {

		Pane root = new Pane();
		Rectangle bg = makeRectangle(0, 0, windowSizeX, windowSizeY, Color.BLACK);

		score1.setText("Player 1: 0");
		score1.setFont(Font.font("Arial", 20));
		score1.setTranslateX((windowSizeX / 2) - 150);
		score1.setTranslateY(10);
		score1.setTextFill(Color.WHITE);

		score2.setText("Player 2: 0");
		score2.setFont(Font.font("Arial", 20));
		score2.setTranslateX((windowSizeX / 2) + 50);
		score2.setTranslateY(10);
		score2.setTextFill(Color.WHITE);
		
		root.getChildren().addAll(bg, score1, score2, player1, player2, ball, fpsCounter);

		return root;
	}

	public void onUpdate() {

		// Player Movement
		double playerPosY = player1.getY();
		double player2PosY = player2.getY();

		if (up == true) {
			if (playerPosY > 10) {
				player1.setY(playerPosY - f);
			}
		} else if (down == true) {
			if (playerPosY + 110 < windowSizeY) {
				player1.setY(playerPosY + f);
			}
		}

		if (up2 == true) {
			if (player2PosY > 10) {
				player2.setY(player2PosY - f);
			}
		} else if (down2 == true) {
			if (player2PosY + 110 < windowSizeY) {
				player2.setY(player2PosY + f);
			}
		}

		// Ball Movement

		// For some reason using these variables broke something.
		// double ballPosX = ball.getX();
		// double ballPosY = ball.getY();

		ball.setX(ball.getX() + bfX);
		ball.setY(ball.getY() + bfY);

		// Edge collision detection.
		if (ball.getX() + ballSize >= windowSizeX) {
			resetBall();
			player1Score++;
			score1.setText("Player 1: " + player1Score);

		} else if (ball.getX() <= 0) {
			resetBall();
			player2Score++;
			score2.setText("Player 2: " + player2Score);

		} else if (ball.getY() + ballSize >= windowSizeY) {
			bfY *= -1;

		} else if (ball.getY() <= 0) {
			bfY *= -1;
		}

		// Paddle collision detection.
		// There's probably a better way of doing this.
		if (ball.getX() + ballSize == player2.getX() && ball.getY() < player2.getY() + 100
				&& !(ball.getY() + ballSize < player2.getY())) {
			bfX *= -1;
			// playSound();
		}

		if (ball.getX() == player1.getX() + ballSize && ball.getY() < player1.getY() + 100
				&& !(ball.getY() + ballSize < player1.getY())) {
			bfX *= -1;
			// playSound();
		}

	}

	// I don't know if this is useful, but I added it. Just like all these comments
	// lamoooooooooooooooooooooooooo
	private void onStart() {
		score1.setText("Player 1: " + player1Score);
		score2.setText("Player 2: " + player2Score);
		player1.setY((windowSizeY / 2 - 50));
		player2.setY((windowSizeY / 2 - 50));
		bfX = 2.0;
		bfY = 2.0;
		resetBall();
	}

	// Resets the ball's position to the center of the stage.
	private void resetBall() {

		ball.setX((windowSizeX / 2));
		ball.setY((windowSizeY / 2));

	}

	/*
	 * private void playSound(){ AudioClip reflectSound = new
	 * AudioClip("file:src/resources/sound.wav"); reflectSound.play(); }
	 */

	private Rectangle makeRectangle(int posX, int posY, int sizeX, int sizeY, Color color) {
		Rectangle rectangle = new Rectangle(posX, posY, sizeX, sizeY);
		rectangle.setFill(color);
		return rectangle;
	}

	// Exits the application, never would've guessed.
	public void exit() {
		window.close();
	}

}
