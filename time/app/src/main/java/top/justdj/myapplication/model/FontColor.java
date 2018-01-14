package top.justdj.myapplication.model;

public class FontColor {
	public static final String[] fontColor = new String[]{
			"#30393E",
			"#E16B8C",
			"#F4A7B9",
			"#FEDFE1",
			"#9F353A",
			"#CB4042",
			"#734338",
			"#C73E3A",
			"#B47157",
			"#F05E1C",
			"#B35C37",
			"#E3916E",
			"#947A6D",
			"#FC9F4D",
			"#82663A",
			"#6C6024",
			"#877F6C",
			"#90B44B",
			"#91AD70",
			"#86C166",
			"#91B493",
			"#6A8372",
			"#00896C",
			"#0F4C3A",
			"#33A6B8",
			"#81C7D4",
			"#3A8FB7",
			"#7B90D2",
			"#6A4C9C",
			"#572A3F",
			"#622954",
			"#FFFFFB",
			"#535953",
			"#3A3226",
			"#0C0C0C",
			"#855B32",
			"#724832",
			"#BC9F77",
			"#A5A051",
			"#616138"
	};
	
	public static int getColorPosition(String color){
		for (int i = 0; i < fontColor.length;++i){
			if (color.equals(fontColor[i]))
				return i;
		}
		return 0;
	}
}
