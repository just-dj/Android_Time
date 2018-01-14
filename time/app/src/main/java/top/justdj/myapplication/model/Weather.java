package top.justdj.myapplication.model;

import top.justdj.myapplication.R;

public class Weather {
	public static final Integer[] weatherImg = new Integer[]{
			R.drawable.weather1,
			R.drawable.weather2,
			R.drawable.weather3,
			R.drawable.weather4,
			R.drawable.weather5,
			R.drawable.weather6,
			R.drawable.weather7,
			R.drawable.weather8,
			R.drawable.weather9,
			R.drawable.weather10,
			R.drawable.weather11,
			R.drawable.weather12,
	};
	
	public static int getWeatherPosition(int index){
		for (int i = 0; i < weatherImg.length;++i){
			if (weatherImg[i] == index)
				return i;
		}
		return 0;
	}
}
