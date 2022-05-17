package nl.rug.ai.oop.crazyeights.model;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.HashMap;
import java.util.Map;

public class GameView extends JFrame{

	public static final Map<String, BufferedImage> cardImages = new HashMap(5);
	public GameView() throws IOException {

		if (cardImages.size() < 1) {
			initializeGame();
		}
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int selected = getSelectedCard(e.getX());
				if (selected >= 0) {
					System.out.println("The user selected card "+selected+".");
					revalidate();
					repaint();
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				repaint();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				selectedCard = getSelectedCard(e.getX());
				repaint();
			}
		});
	}
	public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame();
		GameView gameView = new GameView();
		GameModel gameModel = new GameModel();

		gameView.setHand(new String[]{"C1", "S5", "C11", "D2", "H3"});
		frame.add(gameView, BorderLayout.SOUTH);
		frame.setSize(900,500);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameView.setup(gameModel);
	}

	public void setup(GameModel gameModel) {
		Scanner myObj = new Scanner(System.in);
		System.out.println("Enter player amount between 2 and 7");
		int playerAmount = myObj.nextInt();

		gameModel.runGame(playerAmount, gameModel);
	}

	public static void initializeGame() throws IOException {
		JFrame gameFrame = new JFrame("Crazy Eights");
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setSize(800, 600);
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setVisible(true);

		char suit = 'C';
		for(int i = 0; i < 4; i++){
			if(i == 0){
				suit = 'C';
			}
			if(i == 1){
				suit = 'D';
			}
			if(i == 2){
				suit = 'H';
			}
			if(i == 3){
				suit = 'S';
			}
			for(int cardRank = 1; cardRank < 14; cardRank++){
				//File file = new File("src/main/resources/" + suit + cardRank + ".png");
				cardImages.put(suit + "" + i, ImageIO.read(GameView.class.getResource("/" + suit + i + ".png")));
				//BufferedImage bufferedImage = ImageIO.read(file);
				//cardImages.add(bufferedImage);
			}
		}
		return;
	}


	public static int CARDS_PER_SUIT = 13;
	public static int DEFAULT_SELECTION_DELTA = -40;
	private String[] hand = {};
	private int selectedCard = -1;
	private Dimension cardSize = new Dimension(120, 150);
	private int[] xCardPosition = {};
	private int selectionDelta = DEFAULT_SELECTION_DELTA;

	/**
	 * Preloads images
	 *
	 private static void loadImages() {
	 try {
	 for (String suit : new String[]{"C", "S", "D", "H"}) {
	 for (int i = 1; i <= CARDS_PER_SUIT; i++) {
	 cardImages.put(suit + "" + i,
	 ImageIO.read(GamePane.class.getResource("/" + suit + i + ".png")));
	 }
	 }
	 cardImages.put("00", ImageIO.read(GamePane.class.getResource("/00.png")));
	 } catch (IOException e) {
	 e.printStackTrace();
	 System.exit(-1);
	 }
	 }
	 */

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(1000, (int)cardSize.getHeight()+Math.abs(selectionDelta));
	}

	/**
	 * Sets up a new panel and mouse interactions.
	 */

	private int getSelectedCard(int x) {
		for (int i = hand.length - 1; i >= 0; i--) {
			if (x > xCardPosition[i] && x < xCardPosition[i] + cardSize.getWidth()) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void invalidate() {
		super.invalidate();
		selectedCard = -1;
		xCardPosition = new int[hand.length];
		int xDelta = (int)Math.floor(Math.min(cardSize.getWidth() * 1.1, (getWidth() - cardSize.getWidth())/(hand.length - 1)));
		int xPos = (int) ((getWidth() - cardSize.getWidth() - (hand.length - 1)*xDelta) / 2);
		for (int i = 0; i < hand.length; i++) {
			xCardPosition[i] = xPos;
			xPos += xDelta;
		}
	}

	//@Override
	public void paint(Graphics g) {
//        super.paintComponent(g);
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g.create();
		for (int i = 0; i < hand.length; i++) {
			g2d.drawImage(cardImages.get(hand[i]),
					xCardPosition[i], selectedCard == i ? Math.max(selectionDelta, 0) : Math.max(-selectionDelta, 0),
					(int)cardSize.getWidth(), (int)cardSize.getHeight(), null);
		}
		g2d.dispose();
	}

	/**
	 * Sets the hand of cards and resets the view to match.
	 * @param hand hand of cards, where every card is a character "C", "S", "D", or "H"
	 *             followed by a number between 1 and 13, or 00 for a face-down card
	 */
	public void setHand(String[] hand) {
		this.hand = hand;
		revalidate();
	}

	public int getSelectionDelta() {
		return selectionDelta;
	}

	public void setSelectionDelta(int selectionDelta) {
		this.selectionDelta = selectionDelta;
	}
}
