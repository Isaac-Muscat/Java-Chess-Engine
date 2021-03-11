package gui;


import javax.imageio.ImageIO;
import javax.swing.*;

import boardRepresentation.Board;
import boardRepresentation.Moves.CastleKingside;
import boardRepresentation.Moves.CastleQueenside;
import boardRepresentation.Moves.Move;
import boardRepresentation.*;
import boardRepresentation.Pieces.*;
import search.Search;
import utilities.BitboardUtils;

import java.awt.*;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.awt.event.MouseEvent;

@SuppressWarnings("serial")
public class GraphicUserInterface extends JPanel implements MouseListener, MouseMotionListener {
	public static final int WIDTH = 800;
	public static final int HEIGHT = 820;
	public static final int SCALE = 100;
	private boardRepresentation.Color sideToMove;
	private BufferedImage img;
	private int mouseX, mouseY;
	private Piece selectedPiece;
	private long selectedPieceBitboard;
	private Board board;
	private boardRepresentation.Color playerColor;
	
	public GraphicUserInterface(Board board, boardRepresentation.Color playerColor) {
		this.board = board;
		sideToMove = board.getSideToMove();
		this.playerColor = playerColor;
		try {
			img = ImageIO.read(new File("src/gui/Chess_Pieces.png"));
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
		for(Piece piece: board.getPieces()) {
			if(piece==selectedPiece&&selectedPiece!=null) {
				g.drawImage(getSprite(selectedPiece), this.mouseX-SCALE/2, this.mouseY-SCALE/2, SCALE, SCALE, null);
				drawPieces(g, piece.getBitboard()&~selectedPieceBitboard, getSprite(piece));
			} else {
				drawPieces(g, piece.getBitboard(), getSprite(piece));
			}
			
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
	
	private BufferedImage getSprite(Piece piece) {
		switch(piece.getPieceEnum()) {
		case WP:
			return img.getSubimage(1000, 0, 200, 200);
		case WB:
			return img.getSubimage(400, 0, 200, 200);
		case WN:
			return img.getSubimage(600, 0, 200, 200);
		case WR:
			return img.getSubimage(800, 0, 200, 200);
		case WQ:
			return img.getSubimage(200, 0, 200, 200);
		case WK:
			return img.getSubimage(0, 0, 200, 200);
		case BP:
			return img.getSubimage(1000, 200, 200, 200);
		case BB:
			return img.getSubimage(400, 200, 200, 200);
		case BN:
			return img.getSubimage(600, 200, 200, 200);
		case BR:
			return img.getSubimage(800, 200, 200, 200);
		case BQ:
			return img.getSubimage(200, 200, 200, 200);
		default:
			return img.getSubimage(0, 200, 200, 200);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if(sideToMove==playerColor) {
			this.mouseX = e.getX();
			this.mouseY = e.getY();
			int target = mouseX/SCALE + (mouseY/SCALE)*8;
			long from = BitboardUtils.SQUARE[target];
			for(Piece piece: board.getPieces()) {
				if(piece.getColor()==playerColor&&(from&piece.getBitboard())!=0) {
					selectedPieceBitboard = piece.getBitboard()&from;
					selectedPiece = piece;
				}
			}
		}
		
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		this.mouseX = e.getX();
		this.mouseY = e.getY();
		repaint();
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(selectedPiece!=null&&sideToMove==playerColor) {
			int to = mouseX/SCALE + (mouseY/SCALE)*8;
			int from = Long.numberOfLeadingZeros(selectedPieceBitboard);
			for(Move move: board.generateLegalMoves()) {
				if(move.getFrom()==from&&move.getTo()==to||((to-from)==2&&move instanceof CastleKingside)
						||((to-from)==-2&&move instanceof CastleQueenside)) {
					move.makeMove(board);
					board.updateSideToMove();
					sideToMove = playerColor.getOther();
					selectedPiece = null;
					repaint();
					HashMap<Move, Integer> moves = Search.negaMaxRoot(board, 4);
					Move bestMove = Search.getBestMove(moves);
					bestMove.makeMove(board);
					board.updateSideToMove();
					sideToMove = playerColor;
					break;
				}
			}
			selectedPiece = null;
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
