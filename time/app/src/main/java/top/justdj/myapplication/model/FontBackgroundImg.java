package top.justdj.myapplication.model;

import top.justdj.myapplication.R;

public class FontBackgroundImg {
		public static final Integer[] backgroundImg = new Integer[]{
				R.drawable.bg0,
				R.drawable.bg1,
				R.drawable.bg2,
				R.drawable.bg3,
				R.drawable.bg4,
				R.drawable.bg5,
				R.drawable.bg6,
				R.drawable.bg7,
				R.drawable.bg8,
				R.drawable.bg9,
				R.drawable.bg10,
				R.drawable.bg11,
				R.drawable.bg12,
				R.drawable.bg13,
				R.drawable.bg14,
				R.drawable.bg15,
				R.drawable.bg16,
				R.drawable.bg17,
				R.drawable.bg18,
				R.drawable.bg19,
				R.drawable.bg20,
				R.drawable.bg21,
				R.drawable.bg22,
				R.drawable.bg23,
		};
		
		public static int getColorPosition(int index){
			for (int i = 0; i < backgroundImg.length;++i){
				if (backgroundImg[i] == index)
					return i;
			}
			return 0;
		}
}

