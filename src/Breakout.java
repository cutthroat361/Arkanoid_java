
/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 *
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 800;
	public static final int APPLICATION_HEIGHT = 600;

	/** Border of game board */
	private static final int BORDER = 20;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = (int) (APPLICATION_WIDTH * 0.6);
	private static final int HEIGHT = APPLICATION_HEIGHT - (BORDER * 2);

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 75;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP - 5) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 14;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static int NTURNS = 3;

	/**
	 * We need to add speed to our ball when it bounces from walls or paddle or
	 * bricks. We decided to pick 3 not to make it very fast or slow
	 */
	private static double SPEED_X = 3;
	private static double SPEED_Y = -3;

	/**
	 * DELAY- our default delay on some "levels" of our game it helps us to avoid
	 * little bugs quant- is our score from beginning that is 0 then u start the
	 * game
	 */
	private int DELAY = 10;
	private int quant = 0;

	/**
	 * We needed some "perfect" color for our space theme so we had some struggle
	 * with picking it, hope you love it and it gives you atmosphere
	 */
	Color MOON = new Color(245, 243, 206);

	/** Adding our "products" to game(components) */
	GRect paddle, brick, heart1, heart2, heart3;
	GLabel score, looser, winner;
	GOval ball;
	GImage gameboard;
	Frame frame;
	boolean playing = false;

	/** We needed a random generator to make game fun a bit */
	RandomGenerator random = new RandomGenerator();

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
		setup();
		while (NTURNS != 0) {
			ballMove();
			checkForCollisions();
			pause(DELAY);
		}
		cheakForWin();
	}

	/**
	 * Setting size of our world and adding hearts(we can not make it a loop because
	 * we need somehow to remove it so decided to make it separated).
	 * gameboard-background of our atmosphere game radar-component to make it look
	 * like a spaceship Frame-helps us to make all background "moves" with theme
	 * brick- a simple brick in a loop to make it easy to build all of them paddle-a
	 * square from which ball is bouncing up score- an easy construction of counting
	 * score for player addMouseListeners- adds "a way" to control game
	 */
	private void setup() {

		this.setSize(APPLICATION_WIDTH, APPLICATION_HEIGHT + 55);
		this.setBackground(Color.DARK_GRAY);

		heart1 = new GRect(30, 30);
		heart1.setFilled(true);
		heart1.setFillColor(Color.RED);
		add(heart1, 510 + 60, 500);

		heart2 = new GRect(30, 30);
		heart2.setFilled(true);
		heart2.setFillColor(Color.RED);
		add(heart2, 510 + 60 * 2, 500);

		heart3 = new GRect(30, 30);
		heart3.setFilled(true);
		heart3.setFillColor(Color.RED);
		add(heart3, 510 + 60 * 3, 500);

		gameboard = new GImage("gameboard.jpg");
		gameboard.scale(WIDTH / gameboard.getWidth(), HEIGHT / gameboard.getHeight());
		add(gameboard, BORDER, BORDER);

		GImage radar = new GImage("radar.gif");
		radar.scale(180 / radar.getWidth(), 180 / radar.getHeight());
		add(radar, WIDTH + (APPLICATION_WIDTH - WIDTH - radar.getWidth()) / 2, 20);

		frame = new Frame(BORDER - 2, BORDER, WIDTH, HEIGHT);
		add(frame);

		for (int n = 1; n <= NBRICK_ROWS; n++) {

			for (int i = 1; i <= NBRICKS_PER_ROW; i++) {
				brick = new GRect(BORDER + BRICK_SEP * i + BRICK_WIDTH * (i - 1) + 3,
						BORDER + BRICK_Y_OFFSET + n * (BRICK_HEIGHT + BRICK_SEP), BRICK_WIDTH, BRICK_HEIGHT);
				brick.setFilled(true);
				brick.setFillColor(MOON);
				add(brick);
			}
		}

		paddle = new GRect(BORDER + (WIDTH - PADDLE_WIDTH) / 2, BORDER + HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT,
				PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFillColor(MOON);
		paddle.setFilled(true);
		add(paddle);

		ball = new GOval(paddle.getX() + (PADDLE_WIDTH - BALL_RADIUS) / 2, paddle.getY() - BALL_RADIUS - 1, BALL_RADIUS,
				BALL_RADIUS);
		ball.setFillColor(Color.WHITE);
		ball.setFilled(true);
		add(ball);

		score = new GLabel("SCORE: " + quant);
		score.setFont("Times new roman-24");
		score.setColor(MOON);
		add(score, APPLICATION_WIDTH * 0.7, APPLICATION_HEIGHT / 2);

		addMouseListeners();
	}

	/**
	 * Default mouse listener that helps paddle to follow mouse and adds ball to
	 * game
	 */
	public void mouseMoved(MouseEvent e) {

		double x = e.getX() - paddle.getWidth() / 2;
		if (x <= BORDER + 2)
			x = BORDER + 2;
		else if (x + PADDLE_WIDTH - 2 >= WIDTH + BORDER)
			x = WIDTH + BORDER - PADDLE_WIDTH - 2;
		paddle.setLocation(x, paddle.getY());

		if (!playing) {
			ball.setLocation(paddle.getX() + (PADDLE_WIDTH - BALL_RADIUS) / 2, paddle.getY() - BALL_RADIUS - 1);
		}

	}

	/** Switcher for playing game */
	public void mouseClicked(MouseEvent k) {

		if (!playing) {
			playing = true;
		}
	}

	/** Ball moves while game is going on with speed SPEED_X and SPEED_Y */
	private void ballMove() {

		if (playing)
			ball.move(SPEED_X, SPEED_Y);

	}

	/** checking for collisions */
	private void checkForCollisions() {
		outOfGameBoard();
		contactWithPaddle();
		collideWithBrick();
		downboard();

	}

	/**
	 * Removes hearts then you lost ball and counts how many turns left also adds
	 * ball in center then new live given
	 */
	private void downboard() {
		if (ball.getY() >= APPLICATION_HEIGHT) {
			if (NTURNS == 2) {
				remove(heart2);
			}
			NTURNS--;
			remove(ball);
			remove(heart1);
			if (NTURNS > 0) {
				add(ball, BORDER + WIDTH / 2, BORDER + HEIGHT / 2);
			} else {
				remove(heart3);
			}
			pause(1000);
		}
	}

	/**
	 * if ball hit a wall it changes X or Y depends on how ball "lands" on wall
	 */
	private void outOfGameBoard() {
		if (ball.getX() <= BORDER + 2 || ball.getX() + BALL_RADIUS >= BORDER + WIDTH - 2)
			SPEED_X = -SPEED_X;
		else if (ball.getY() <= BORDER)
			SPEED_Y = -SPEED_Y;
	}

	/**
	 * changing speed when ball makes contact with paddle using math.abs to make it
	 * simple
	 */
	private void contactWithPaddle() {

		if (paddle.contains(ball.getX() + BALL_RADIUS / 2, ball.getY() + BALL_RADIUS)
				|| paddle.contains(ball.getX() + BALL_RADIUS / 4, ball.getY() + BALL_RADIUS * 0.75)
				|| paddle.contains(ball.getX() + BALL_RADIUS * 0.75, ball.getY() + BALL_RADIUS * 0.75))
			SPEED_Y = -Math.abs(SPEED_Y);

		else if (paddle.contains(ball.getX(), ball.getY() + BALL_RADIUS / 2)
				|| paddle.contains(ball.getX() + BALL_RADIUS, ball.getY() + BALL_RADIUS / 2))
			SPEED_X = -SPEED_X;
	}

	/**
	 * Removes brick that was hit by ball counting score for player 1 brick= 1 point
	 */
	private void collideWithBrick() {

		if (getElementAt(ball.getLocation()) != null && getElementAt(ball.getLocation()) != paddle
				&& getElementAt(ball.getLocation()) != frame && getElementAt(ball.getLocation()) != gameboard) {
			SPEED_Y = -SPEED_Y;
			SPEED_X = random.nextDouble(0, 5);
			remove(getElementAt(ball.getLocation()));
			quant += 1;
			remove(score);
			score = new GLabel("SCORE: " + quant);
			score.setFont("Times new roman-24");
			score.setColor(MOON);
			add(score, APPLICATION_WIDTH * 0.7, APPLICATION_HEIGHT / 2);
		}
	}

	/**
	 * checking for winner or looser gives you nice label if you win and if you
	 * loose gives you a bad sign
	 */
	private void cheakForWin() {
		if (quant < 100 || NTURNS == 0) {
			looser = new GLabel("MY BOI U LOST");
			looser.setFont("Times new roman-60");
			looser.setColor(Color.RED);
			add(looser, 50, 300);
		}

		if (quant == 100) {
			winner = new GLabel("WINNER WINNER!");
			winner.setFont("Times new roman-55");
			winner.setColor(MOON);
			add(winner, 50, 300);
		}

	}
}