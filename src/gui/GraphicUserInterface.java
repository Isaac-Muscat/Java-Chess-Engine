package gui;


import javax.imageio.ImageIO;
import javax.swing.*;

import boardRepresentation.Board;
import boardRepresentation.Moves.CastleKingside;
import boardRepresentation.Moves.CastleQueenside;
import boardRepresentation.Moves.Move;
import boardRepresentation.Pieces.*;
import utilities.BitboardUtils;
import main.Main;

import java.awt.*;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class GraphicUserInterface extends JPanel implements MouseListener, MouseMotionListener {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 820;
	public static final int SCALE = 100;
	private BufferedImage img;
	private int mouseX, mouseY;
	private Piece selectedPiece;
	private long selectedPieceBitboard;
	public long[] pieces = new long[12];
	private Board board;
	
	public GraphicUserInterface(Board board) {
		try {
			img = ImageIO.read(this.getClass().getResource("images/Chess_Pieces.png"));
		} catch(IOException e) {System.out.println("Bad path for sprite");}
		addMouseListener(this);
		addMouseMotionListener(this);
		JFrame frame = new JFrame("Chess");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setSize(WIDTH, HEIGHT);
		frame.setVisible(true);
		startGame(board);
	}
	
	private void startGame(Board board) {
		this.board = board;
		for(Piece piece: board.getPieces()) {
			if(Main.playerColor==boardRepresentation.Color.BLACK) {
				pieces[piece.getPieceEnum().getIndex()] = Long.reverse(piece.getBitboard());
				continue;
			}
			pieces[piece.getPieceEnum().getIndex()] = piece.getBitboard();
		}
		Main.RUN=true;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		this.setBackground(new Color(20, 100, 200));
		g.setColor(Color.WHITE);
		for(int i = 0; i<64; i++) {
			if((i/8 + i%8)%2==0) {
				g.fillRect((i/8)*SCALE,(i%8)*SCALE, SCALE, SCALE);	
			}
		}
		for(int i = 0;i<12;i++) {
			if(selectedPiece!=null&&i==selectedPiece.getPieceEnum().getIndex()) {
			} else {
				drawPieces(g, pieces[i], getSprite(i));
			}
		}
		if(selectedPiece!=null) {
			int i = selectedPiece.getPieceEnum().getIndex();
			g.drawImage(getSprite(selectedPiece.getPieceEnum().getIndex()), this.mouseX-SCALE/2, this.mouseY-SCALE/2, SCALE, SCALE, null);
			drawPieces(g, pieces[i]&~Long.reverse(selectedPieceBitboard), getSprite(i));
		}
	}
	
	private void drawPieces(Graphics g, long bitboard, BufferedImage sprite) {
		while(bitboard!=0) {
			int square = Long.numberOfLeadingZeros(bitboard);
			int x = (square%8)*SCALE;
			int y = (square/8)*SCALE;
			g.drawImage(sprite, x, y, SCALE, SCALE, null);
			
			bitboard^=BitboardUtils.SQUARE[square];
		}
	}
	
	private BufferedImage getSprite(int i) {
		switch(i) {
		case 0:
			return img.getSubimage(1000, 0, 200, 200);
		case 1:
			return img.getSubimage(400, 0, 200, 200);
		case 2:
			return img.getSubimage(600, 0, 200, 200);
		case 3:
			return img.getSubimage(800, 0, 200, 200);
		case 4:
			return img.getSubimage(200, 0, 200, 200);
		case 5:
			return img.getSubimage(0, 0, 200, 200);
		case 6:
			return img.getSubimage(1000, 200, 200, 200);
		case 7:
			return img.getSubimage(400, 200, 200, 200);
		case 8:
			return img.getSubimage(600, 200, 200, 200);
		case 9:
			return img.getSubimage(800, 200, 200, 200);
		case 10:
			return img.getSubimage(200, 200, 200, 200);
		default:
			return img.getSubimage(0, 200, 200, 200);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(Main.RUN&&Main.sideToMove==Main.playerColor) {
			this.mouseX = e.getX();
			this.mouseY = e.getY();
			int target = mouseX/SCALE + (mouseY/SCALE)*8;
			long from = BitboardUtils.SQUARE[target];
			if(Main.playerColor == boardRepresentation.Color.BLACK) {
				from = Long.reverse(from);
			}
			for(Piece piece: board.getPieces()) {
				if(piece.getColor()==Main.playerColor&&(from&piece.getBitboard())!=0) {
					selectedPieceBitboard = piece.getBitboard()&from;
					selectedPiece = piece;
				}
			}
		}
		
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		if(Main.RUN) {
			this.mouseX = e.getX();
			this.mouseY = e.getY();
			repaint();
		}
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(Main.RUN) {
			if(selectedPiece!=null&&Main.sideToMove==Main.playerColor) {
				int to = mouseX/SCALE + (mouseY/SCALE)*8;
				if(Main.playerColor == boardRepresentation.Color.BLACK) {
					to = 63-to;
				}
				int from = Long.numberOfLeadingZeros(selectedPieceBitboard);
				for(Move move: board.generateLegalMoves()) {
					if(move.getFrom()==from&&move.getTo()==to||((to-from)==2&&move instanceof CastleKingside)
							||((to-from)==-2&&move instanceof CastleQueenside)) {
						move.makeMove(board);
						for(Piece piece: board.getPieces()) {
							if(Main.playerColor==boardRepresentation.Color.BLACK) {
								pieces[piece.getPieceEnum().getIndex()] = Long.reverse(piece.getBitboard());
								continue;
							}
							pieces[piece.getPieceEnum().getIndex()] = piece.getBitboard();
						}
						board.updateSideToMove();
						Main.sideToMove = Main.playerColor.getOther();
						selectedPiece = null;
						break;
					}
				}
				selectedPiece = null;
			}
			repaint();
		}
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
}
