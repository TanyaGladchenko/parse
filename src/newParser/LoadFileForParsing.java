package newParser;

import java.util.List;

import entity.ProtocolVO;

public class LoadFileForParsing {

	public static void main(String[] args) throws Exception {
		FileService fs = new FileService();
		fs.loadProperties("src\\java\\resources\\regex.properties");
		List<ProtocolVO> list = fs.loadPdnfile("src\\java\\resources\\pdn_file.pdn");

		for (ProtocolVO p : list) {
			System.out.println(p);
		}
	}
}
