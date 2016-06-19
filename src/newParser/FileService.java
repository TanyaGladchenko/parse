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
import entity.ProtocolVO;
import entity.StateGame;

public class FileService {

	private static Pattern patternTagPair;
	private static Pattern patternMoveText;
	private static Pattern patternGameTermination;
	private static Pattern patternWhite;
	private static Pattern patternBlack;
	private static Pattern patternFen;
	private static Pattern patternOneMove;
	private static Pattern patternMoveComment;
	
	private List<Moves> listMoves;
	private String title;
	private int number;
	private String whitePlayer;
	private String blackPlayer;
	private String event;
	private Position position;

	public void loadProperties(String fileProp) {
		Properties prop = new Properties();
		FileInputStream fis;
		try {
			fis = new FileInputStream(fileProp);
			prop.load(fis);
			patternTagPair = Pattern.compile(prop.getProperty("tag_pair"));
			patternMoveText = Pattern.compile(prop.getProperty("move_text"));
			patternGameTermination = Pattern.compile(prop.getProperty("game_termination"));
			patternFen = Pattern.compile(prop.getProperty("fen"));
			patternWhite = Pattern.compile(".+W" + prop.getProperty("wb"));
			patternBlack = Pattern.compile(".+B" + prop.getProperty("wb"));
			patternOneMove = Pattern.compile(prop.getProperty("one_move"));
			patternMoveComment = Pattern.compile(prop.getProperty("move_comment"));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public List<ProtocolVO> loadPdnfile(String filepdn) throws IOException {
		BufferedReader br = null;
		br = new BufferedReader(new FileReader(filepdn));
		String s;
		List<ProtocolVO> listProtocol = new ArrayList<ProtocolVO>();
		List<Moves> listMoves = new ArrayList<Moves>();
		while ((s = br.readLine()) != null) {
			Matcher m = patternTagPair.matcher(s);
			if (m.matches()) {
				parseTag(m);
			} else {
				m = patternMoveText.matcher(s);
				if (m.matches()) {
					listMoves.addAll(parseMoves(s));
				} else {
					m = patternGameTermination.matcher(s);
					if (m.matches()) {
						String statusName = s.trim();
						ProtocolVO p = createProtocol(statusName);
						position = null;
						listMoves.clear();
						listProtocol.add(p);
					}
				}
			}
		}
		return listProtocol;
	}

	private ProtocolVO createProtocol(String statusName) {
		ProtocolVO protocol = new ProtocolVO();
		protocol.setPosition(position);
		title = whitePlayer + "-" + blackPlayer;
		protocol.setTitle(title);
		protocol.setListMoves(listMoves);
		protocol.setStateGame(new StateGame(0L, statusName));
		title = "";
		whitePlayer = "";
		blackPlayer = "";
		event = "";
		number = 0;
		return protocol;
	}

	public List<Moves> parseMoves(String str) {
		List<Moves> list = new ArrayList<Moves>();
		Matcher matcherMove = patternOneMove.matcher(str);

		while (matcherMove.find()) {
			Moves moves = new Moves();
			moves.setNumber(number++);
			String strMove = matcherMove.group(2);
			Matcher mc = patternMoveComment.matcher(strMove);
			if (mc.find()) {
				moves.setMoveWhite(mc.group(1));
				moves.setCommentWhite(mc.group(3));
				if (mc.find()) {
					moves.setMoveBlack(mc.group(1));
					moves.setCommentBlack(mc.group(3));
				}
			}

			list.add(moves);
		}
		return list;
	}

	public void parseTag(Matcher m) {
		String tag = m.group(1);
		TagName tn = TagName.valueOf(tag.toUpperCase());
		String tagContext = m.group(2);

		switch (tn) {
		case EVENT:
			event = tagContext;
			break;
		case WHITE:
			whitePlayer = tagContext;
			break;
		case BLACK:
			blackPlayer = tagContext;
			break;
		case FEN:
			parseFen(tagContext);
		default:
			break;

		}
	}

	public void parseFen(String strFen) {
		position = new Position();
		Matcher m = patternFen.matcher(strFen);
		m.matches();
		if (m.group(1).equals("B")) {
			position.setBlackMoveFirst(true);
		}
		Matcher mw = patternWhite.matcher(strFen);
		mw.matches();
		position.setWhiteMan(mw.group(1));
		position.setWhiteKing(mw.group(3).replace("K", ""));

		Matcher mb = patternBlack.matcher(strFen);
		mb.matches();
		position.setBlackMan(mb.group(1));
		position.setBlackKing(mb.group(3).replace("K", ""));
		String title = "position";
		if (event == null) {
			title = event;
		}
		position.setTitle(title);
	}
}
