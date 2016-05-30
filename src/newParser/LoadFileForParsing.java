package newParser;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import entity.Moves;
import entity.Position;

public class LoadFileForParsing {

	private static Pattern patternTagPair;
	private static Pattern patternMoveText;
	private static Pattern patternGameTermination;
	private static Pattern patternWhite;
	private static Pattern patternBlack;
	private static Pattern patternFen;
	private static Pattern patternOneMove;
	private static Pattern patternMoveComment;
	private static List<Moves> listMoves;

	public static void main(String[] args) throws Exception {
		Properties prop = new Properties();
		try {
			FileInputStream fis = new FileInputStream("src\\java\\resources\\regex.properties");
			prop.load(fis);
			patternTagPair = Pattern.compile(prop.getProperty("tag_pair"));
			patternMoveText = Pattern.compile(prop.getProperty("move_text"));
			patternGameTermination = Pattern.compile(prop.getProperty("game_termination"));
			patternFen = Pattern.compile(prop.getProperty("fen"));
			patternWhite = Pattern.compile(".+W" + prop.getProperty("wb"));
			patternBlack = Pattern.compile(".+B" + prop.getProperty("wb"));
			patternOneMove = Pattern.compile(prop.getProperty("one_move"));
			patternMoveComment = Pattern.compile(prop.getProperty("move_comment"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("src\\java\\resources\\pdn_file.pdn"));
			String s;
			while ((s = br.readLine()) != null) {
				Matcher m = patternTagPair.matcher(s);
				if (m.matches()) {
					parseTag(m);
				} else {
					m = patternMoveText.matcher(s);
					if (m.matches()) {
						listMoves.addAll(parseMoves(s));
					} else{
						m = patternGameTermination.matcher(s);
						if (m.matches()){
							listMoves.clear();
						}	
					}
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static List<Moves> parseMoves(String str) {
		List<Moves> list = new ArrayList<Moves>();
		Matcher matcherMove = patternOneMove.matcher(str);

		int i = 1;
		while (matcherMove.find()) {
			Moves moves = new Moves();
			moves.setNumber(i++);
			String strMove = matcherMove.group(2);
			Matcher mc = patternMoveComment.matcher(strMove);
			if (mc.find()){
				moves.setMoveWhite(mc.group(1));
				moves.setCommentWhite(mc.group(3));
				if (mc.find()){
					moves.setMoveBlack(mc.group(1));
					moves.setCommentBlack(mc.group(3));
				}
			}
			
			list.add(moves);
		}
		return list;
	}

	public static void parseTag(Matcher m) throws Exception {
		String tag = m.group(1);
		TagName tn = TagName.valueOf(tag.toUpperCase());
		String tagContext = m.group(2);
		String title = "";

		switch (tn) {
		case EVENT:
			title += " - " + tagContext;
			break;
		case WHITE:
			title += " - " + tagContext;
			break;
		case BLACK:
			title += " - " + tagContext;
			break;
		case FEN:
			parseFen(tagContext);
		default:
			break;

		}
	}

	public static void parseFen(String strFen) {
		Position p = new Position();
		Matcher m = patternFen.matcher(strFen);
		m.matches();
		if (m.group(1).equals("B")) {
			p.setBlackMoveFirst(true);
		}
		Matcher mw = patternWhite.matcher(strFen);
		mw.matches();
		p.setWhiteMan(mw.group(1));
		p.setWhiteKing(mw.group(3).replace("K", ""));

		Matcher mb = patternBlack.matcher(strFen);
		mb.matches();
		p.setBlackMan(mb.group(1));
		p.setBlackKing(mb.group(3).replace("K", ""));
		System.out.println(p);

	}
}
